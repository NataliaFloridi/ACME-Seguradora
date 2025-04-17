package com.acme.insurancecompany.infrastructure.adapter.out.persistence.adapter.catalog;

import org.springframework.stereotype.Component;

import com.acme.insurancecompany.domain.exception.ErrorCode;
import com.acme.insurancecompany.domain.exception.ResourceNotFoundException;
import com.acme.insurancecompany.domain.exception.ValidationException;
import com.acme.insurancecompany.domain.port.out.port.CatalogPort;
import com.acme.insurancecompany.infrastructure.adapter.out.client.CatalogClient;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.OfferResponse;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.ProductResponse;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogAdapter implements CatalogPort {

    private final CatalogClient catalogClient;

    @Override
    public ProductResponse getProduct(String productId) {
        try {
            return catalogClient.getProduct(productId);
        } catch (FeignException.NotFound e) {
            log.error("Produto não encontrado: {}", productId);
            throw new ResourceNotFoundException("Produto não encontrado");
        } catch (FeignException e) {
            log.error("Erro ao consultar produto: {}", e.getMessage());
            throw new ValidationException(ErrorCode.ERRO_AO_BUSCAR_PRODUTO.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao consultar produto: {}", e.getMessage());
            throw new ValidationException(ErrorCode.ERRO_AO_BUSCAR_PRODUTO.getMessage());
        }
    }

    @Override
    public OfferResponse getOffer(String offerId) {
        try {
            return catalogClient.getOffer(offerId);
        } catch (FeignException.NotFound e) {
            log.error("Oferta não encontrada: {}", offerId);
            throw new ResourceNotFoundException("Oferta não encontrada");
        } catch (FeignException e) {
            log.error("Erro ao consultar oferta: {}", e.getMessage());
            throw new ValidationException(ErrorCode.ERRO_AO_BUSCAR_OFERTA.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao consultar oferta: {}", e.getMessage());
            throw new ValidationException(ErrorCode.ERRO_AO_BUSCAR_OFERTA.getMessage());
        }
    }
}