package com.acme.seguradora.domain.validator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.acme.seguradora.application.dto.InsuranceQuoteRequest;
import com.acme.seguradora.domain.exception.ErrorCode;
import com.acme.seguradora.domain.exception.ValidationException;
import com.acme.seguradora.domain.model.Assistance;
import com.acme.seguradora.domain.model.Coverage;
import com.acme.seguradora.domain.port.out.port.CatalogPort;
import com.acme.seguradora.infrastructure.adapter.out.client.response.OfferResponse;
import com.acme.seguradora.infrastructure.adapter.out.client.response.ProductResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InsuranceQuoteValidator {

    private final CatalogPort catalogPort;

    public void validate(InsuranceQuoteRequest request) {
        validateProduct(request);
        validateOffer(request);
        validateCoverages(request);
        validateAssistances(request);
        validatePremiumAmount(request);
        validateTotalCoverageAmount(request);
    }

    @Cacheable(value = "products", key = "#productId", unless = "#result == null")
    private ProductResponse getProduct(String productId) {
        return catalogPort.getProduct(productId);
    }

    @Cacheable(value = "offers", key = "#offerId", unless = "#result == null")
    private OfferResponse getOffer(String offerId) {
        return catalogPort.getOffer(offerId);
    }

    private void validateProduct(InsuranceQuoteRequest request) {
        try {
            ProductResponse product = getProduct(request.getProductId());
            
            if (product == null) {
                throw new ValidationException(ErrorCode.PRODUTO_NAO_ENCONTRADO.getMessage());
            }
            
            if (!product.isActive()) {
                log.error("Produto {} está inativo", request.getProductId());
                throw new ValidationException(ErrorCode.PRODUTO_INATIVO.getMessage());
            }

            boolean offerBelongsToProduct = product.getOffers() != null && 
                product.getOffers().stream()
                    .anyMatch(offer -> offer.getId().equals(request.getOfferId()));

            if (!offerBelongsToProduct) {
                log.error("Oferta {} não pertence ao produto {}", request.getOfferId(), request.getProductId());
                throw new ValidationException(ErrorCode.OFERTA_NAO_PERTENCE_PRODUTO.getMessage());
            }
        } catch (RuntimeException e) {
            if (e instanceof ValidationException) {
                throw e;
            }
            throw new ValidationException(ErrorCode.ERRO_AO_BUSCAR_PRODUTO.getMessage());
        }
    }

    private void validateOffer(InsuranceQuoteRequest request) {
        try {
            OfferResponse offer = getOffer(request.getOfferId());
            
            if (offer == null) {
                throw new ValidationException(ErrorCode.OFERTA_NAO_ENCONTRADA.getMessage());
            }
            
            if (!offer.isActive()) {
                log.error("Oferta {} está inativa", request.getOfferId());
                throw new ValidationException(ErrorCode.OFERTA_INATIVA.getMessage());
            }
        } catch (RuntimeException e) {
            if (e instanceof ValidationException) {
                throw e;
            }
            throw new ValidationException(ErrorCode.ERRO_AO_BUSCAR_OFERTA.getMessage());
        }
    }

    private void validateCoverages(InsuranceQuoteRequest request) {
        OfferResponse offer = getOffer(request.getOfferId());
        
        Set<String> requestCoverages = request.getCoverages().stream()
            .map(Coverage::getName)
            .collect(Collectors.toSet());
        Set<String> offerCoverages = offer.getCoverages().stream()
            .map(Coverage::getName)
            .collect(Collectors.toSet());
        
        // Valida se a cobertura informada está na lista de coberturas da oferta
        if (!offerCoverages.containsAll(requestCoverages)) {
            Set<String> invalidCoverages = requestCoverages.stream()
                .filter(c -> !offerCoverages.contains(c))
                .collect(Collectors.toSet());
            log.error("Coberturas inválidas encontradas: {}", invalidCoverages);
            throw new ValidationException(
                String.format(ErrorCode.COBERTURA_INDISPONIVEL.getMessage(), 
                    String.join(", ", invalidCoverages))
            );
        }

        //verifica se o valor da cobertura é abaixo do valor máximo permitido
        request.getCoverages().forEach(coverage -> {
            BigDecimal maxValue = offer.getCoverages().stream()
                .filter(c -> c.getName().equals(coverage.getName()))
                .findFirst()
                .map(Coverage::getAmount)
                .orElseThrow(() -> new ValidationException(
                    String.format(ErrorCode.COBERTURA_INDISPONIVEL.getMessage(), coverage.getName())
                ));
            if (coverage.getAmount().compareTo(maxValue) > 0) {
                log.error("Valor da cobertura {} excede o máximo permitido: {} > {}", 
                    coverage.getName(), coverage.getAmount(), maxValue);
                throw new ValidationException(
                    String.format(Locale.US, ErrorCode.VALOR_COBERTURA_EXCEDE_MAXIMO.getMessage(), 
                        coverage.getName(), coverage.getAmount(), maxValue)
                );
            }
        });
    }

    private void validateAssistances(InsuranceQuoteRequest request) {
        OfferResponse offer = getOffer(request.getOfferId());
        
        Set<String> requestAssistances = request.getAssistances().stream()
            .map(Assistance::getName)
            .collect(Collectors.toSet());
        Set<String> offerAssistances = offer.getAssistances().stream()
            .map(Assistance::getName)
            .collect(Collectors.toSet());
        
        //verifica se a assistência informada está na lista de assistências da oferta
        if (!offerAssistances.containsAll(requestAssistances)) {
            Set<String> invalidAssistances = new HashSet<>(requestAssistances);
            invalidAssistances.removeAll(offerAssistances);
            log.error("Assistências inválidas encontradas: {}", invalidAssistances);
            throw new ValidationException(
                String.format(ErrorCode.ASSISTENCIA_INDISPONIVEL.getMessage(), 
                    String.join(", ", invalidAssistances))
            );
        }
    }

    private void validatePremiumAmount(InsuranceQuoteRequest request) {
        OfferResponse offer = getOffer(request.getOfferId());
        
        BigDecimal premium = request.getTotalMonthlyPremiumAmount().getSuggestedAmount();
        BigDecimal minAmount = offer.getMonthlyPremiumAmount().getMinAmount();
        BigDecimal maxAmount = offer.getMonthlyPremiumAmount().getMaxAmount();

        //verifica se o valor do prêmio mensal está dentro do intervalo permitido (min e max)
        if (premium.compareTo(minAmount) < 0 || premium.compareTo(maxAmount) > 0) {
            log.error("Valor do prêmio mensal {} fora do intervalo permitido: min={}, max={}", 
                premium, minAmount, maxAmount);
            throw new ValidationException(
                String.format(Locale.US, ErrorCode.VALOR_PREMIO_FORA_INTERVALO.getMessage(),
                    premium, minAmount, maxAmount)
            );
        }
    }

    private void validateTotalCoverageAmount(InsuranceQuoteRequest request) {
        //calcula o valor total das coberturas informado
        BigDecimal totalCalculated = request.getCoverages().stream()
            .map(Coverage::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        //verifica se o valor total das coberturas informado corresponde à soma dos valores individuais
        if (totalCalculated.compareTo(request.getTotalCoverageAmount()) != 0) {
            log.error("Valor total das coberturas incorreto: informado={}, calculado={}", 
                request.getTotalCoverageAmount(), totalCalculated);
            throw new ValidationException(
                String.format(Locale.US, ErrorCode.VALOR_TOTAL_COBERTURAS_INCORRETO.getMessage(), 
                    request.getTotalCoverageAmount(), totalCalculated)
            );
        }
    }
} 