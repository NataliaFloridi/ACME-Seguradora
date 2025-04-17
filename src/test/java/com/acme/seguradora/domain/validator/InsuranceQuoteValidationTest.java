package com.acme.seguradora.domain.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.insurancecompany.application.dto.InsuranceQuoteRequest;
import com.acme.insurancecompany.domain.exception.ValidationException;
import com.acme.insurancecompany.domain.model.Assistance;
import com.acme.insurancecompany.domain.model.Coverage;
import com.acme.insurancecompany.domain.model.MonthlyPremiumAmount;
import com.acme.insurancecompany.domain.port.out.port.CatalogPort;
import com.acme.insurancecompany.domain.validator.InsuranceQuoteValidator;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.OfferResponse;
import com.acme.insurancecompany.infrastructure.adapter.out.client.response.ProductResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InsuranceQuoteValidationTest {

    @Mock
    private CatalogPort catalogPort;

    @InjectMocks
    private InsuranceQuoteValidator insuranceQuoteValidation;
   
    @Test
    void testValidate() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(Arrays.asList(Coverage.builder().name("COBERTURA1").amount(new BigDecimal("100.00")).build()))
            .totalCoverageAmount(new BigDecimal("100.00"))
            .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder().suggestedAmount(new BigDecimal("200.00")).build())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();

        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();

        MonthlyPremiumAmount monthlyPremiumAmount = MonthlyPremiumAmount.builder()
            .minAmount(new BigDecimal("100.00"))
            .maxAmount(new BigDecimal("500.00"))
            .build();

        OfferResponse offer = OfferResponse.builder()
            .active(true)
            .productId("PRODUTO1")
            .coverages(Arrays.asList(Coverage.builder().name("COBERTURA1").amount(new BigDecimal("200.00")).build()))
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .monthlyPremiumAmount(monthlyPremiumAmount)
            .build();

        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(offer);  

        insuranceQuoteValidation.validate(request);

        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, atLeastOnce()).getOffer(any());
    }   

    @Test
    void testValidateProductNotFound() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Produto não encontrado", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
    }

    @Test
    void testValidateProductInactive() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .build();
       
        when(catalogPort.getProduct(any())).thenReturn(ProductResponse.builder().active(false).build());
       
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Produto está inativo", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
    }

    @Test
    void testValidateOfferNotFound() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(List.of())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();
       
        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(null);
       
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Oferta não encontrada", exception.getMessage());
        verify(catalogPort, times(1)).getOffer(any());
    }

    @Test
    void testValidateOfferNotBelongsToProduct() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(List.of())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();

        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA2").build()))
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Oferta não pertence ao produto", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, never()).getOffer(any());
    }

    @Test
    void testValidateOfferInactive() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .build();
            
        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();
            
        OfferResponse offer = OfferResponse.builder()
            .active(false)
            .productId("PRODUTO1")
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(offer);
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Oferta está inativa", exception.getMessage());
        verify(catalogPort, times(1)).getOffer(any());
    }

    @Test
    void testValidateCoverageNotAvailable() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(Arrays.asList(Coverage.builder().name("COBERTURA1").amount(new BigDecimal("100.00")).build()))
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();
            
        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();
            
        OfferResponse offer = OfferResponse.builder()
            .active(true)
            .productId("PRODUTO1")
            .coverages(Arrays.asList(Coverage.builder().name("COBERTURA2").amount(new BigDecimal("200.00")).build()))
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(offer);
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Cobertura indisponível na oferta: COBERTURA1", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, atLeastOnce()).getOffer(any());
    }

    @Test
    void testValidateCoverageAmountExceedsMaximum() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(Arrays.asList(Coverage.builder().name("COBERTURA1").amount(new BigDecimal("300.00")).build()))
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();
            
        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();
            
        OfferResponse offer = OfferResponse.builder()
            .active(true)
            .productId("PRODUTO1")
            .coverages(Arrays.asList(Coverage.builder().name("COBERTURA1").amount(new BigDecimal("200.00")).build()))
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(offer);
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Valor da cobertura COBERTURA1 (300.00) excede o valor máximo permitido (200.00)", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, atLeastOnce()).getOffer(any());
    }

    @Test
    void testValidateAssistanceNotAvailable() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(List.of())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();
            
        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();
            
        OfferResponse offer = OfferResponse.builder()
            .active(true)
            .productId("PRODUTO1")
            .coverages(List.of())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA2").build()))
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(offer);
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Assistência indisponível na oferta: ASSISTENCIA1", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, atLeastOnce()).getOffer(any());
    }

    @Test
    void testValidatePremiumAmountOutOfRange() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(List.of())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder().suggestedAmount(new BigDecimal("600.00")).build())
            .build();
            
        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();
            
        MonthlyPremiumAmount monthlyPremiumAmount = MonthlyPremiumAmount.builder()
            .minAmount(new BigDecimal("100.00"))
            .maxAmount(new BigDecimal("500.00"))
            .build();
            
        OfferResponse offer = OfferResponse.builder()
            .active(true)
            .productId("PRODUTO1")
            .coverages(List.of())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .monthlyPremiumAmount(monthlyPremiumAmount)
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(offer);
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Valor do prêmio mensal (600.00) está fora do intervalo permitido (100.00 - 500.00)", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, atLeastOnce()).getOffer(any());
    }

    @Test
    void testValidateTotalCoverageAmountInvalid() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .coverages(List.of(
                Coverage.builder().name("COBERTURA1").amount(new BigDecimal("100.00")).build(),
                Coverage.builder().name("COBERTURA2").amount(new BigDecimal("200.00")).build()
            ))
            .totalCoverageAmount(new BigDecimal("400.00"))
            .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder().suggestedAmount(new BigDecimal("200.00")).build())
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .build();
            
        ProductResponse product = ProductResponse.builder()
            .active(true)
            .offers(Arrays.asList(OfferResponse.builder().id("OFERTA1").build()))
            .build();
            
        OfferResponse offer = OfferResponse.builder()
            .active(true)
            .productId("PRODUTO1")
            .coverages(List.of(
                Coverage.builder().name("COBERTURA1").amount(new BigDecimal("100.00")).build(),
                Coverage.builder().name("COBERTURA2").amount(new BigDecimal("200.00")).build()
            ))
            .assistances(Arrays.asList(Assistance.builder().name("ASSISTENCIA1").build()))
            .monthlyPremiumAmount(MonthlyPremiumAmount.builder()
                .minAmount(new BigDecimal("100.00"))
                .maxAmount(new BigDecimal("500.00"))
                .build())
            .build();
            
        when(catalogPort.getProduct(any())).thenReturn(product);
        when(catalogPort.getOffer(any())).thenReturn(offer);
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Valor total das coberturas (400.00) não corresponde à soma dos valores individuais (300.00)", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, atLeastOnce()).getOffer(any());
    }

    @Test
    void testValidateWhenCatalogServiceFails() {
        InsuranceQuoteRequest request = InsuranceQuoteRequest.builder()
            .productId("PRODUTO1")
            .offerId("OFERTA1")
            .build();
            
        when(catalogPort.getProduct(any())).thenThrow(new RuntimeException("Erro de comunicação"));
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            insuranceQuoteValidation.validate(request);
        });
        
        assertEquals("Erro ao buscar produto", exception.getMessage());
        verify(catalogPort, times(1)).getProduct(any());
        verify(catalogPort, never()).getOffer(any());
    }
}
