package com.acme.insurancecompany.infrastructure.adapter.in.web;

import com.acme.insurancecompany.application.dto.InsuranceQuoteRequest;
import com.acme.insurancecompany.application.dto.InsuranceQuoteResponse;
import com.acme.insurancecompany.application.mapper.InsuranceQuoteMapper;
import com.acme.insurancecompany.domain.enums.InsuranceCategory;
import com.acme.insurancecompany.domain.exception.BusinessException;
import com.acme.insurancecompany.domain.exception.ResourceNotFoundException;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.model.MonthlyPremiumAmount;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.CreateInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.FindInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.validator.InsuranceQuoteValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceQuoteControllerTest {

    @Mock
    private CreateInsuranceQuoteUseCase createInsuranceQuoteUseCase;

    @Mock
    private FindInsuranceQuoteUseCase findInsuranceQuoteUseCase;

    @Mock
    private InsuranceQuoteMapper insuranceQuoteMapper;

    @Mock
    private InsuranceQuoteValidator insuranceQuoteValidator;

    @InjectMocks
    private InsuranceQuoteController controller;

    private InsuranceQuoteRequest validRequest;
    private InsuranceQuote expectedQuote;
    private InsuranceQuoteResponse expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = buildValidRequest();
        expectedQuote = buildExpectedQuote();
        expectedResponse = buildExpectedResponse();
    }

    @Test
    void shouldCreateInsuranceQuote() {
        doNothing().when(insuranceQuoteValidator).validate(validRequest);
        when(insuranceQuoteMapper.toDomain(validRequest)).thenReturn(expectedQuote);
        when(createInsuranceQuoteUseCase.execute(expectedQuote)).thenReturn(expectedQuote);
        when(insuranceQuoteMapper.toResponse(expectedQuote)).thenReturn(expectedResponse);

        ResponseEntity<InsuranceQuoteResponse> response = controller.createInsuranceQuote(validRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);

        verify(insuranceQuoteValidator, times(1)).validate(validRequest);
        verify(createInsuranceQuoteUseCase, times(1)).execute(expectedQuote);
    }

    @Test
    void shouldFindInsuranceQuoteById() {
        Long quoteId = 1L;
        when(findInsuranceQuoteUseCase.execute(quoteId)).thenReturn(expectedQuote);
        when(insuranceQuoteMapper.toResponse(expectedQuote)).thenReturn(expectedResponse);

        ResponseEntity<InsuranceQuoteResponse> response = controller.findInsuranceQuoteById(quoteId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);

        verify(findInsuranceQuoteUseCase, times(1)).execute(quoteId);
    }

    @Test
    void shouldReturnNotFoundWhenQuoteDoesNotExist() {
        Long quoteId = 999L;
        when(findInsuranceQuoteUseCase.execute(quoteId))
                .thenThrow(new ResourceNotFoundException("Cotação não encontrada"));

        assertThrows(ResourceNotFoundException.class, () -> {
            controller.findInsuranceQuoteById(quoteId);
        });

        verify(findInsuranceQuoteUseCase, times(1)).execute(quoteId);
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        String errorMessage = "Erro de validação";
        doNothing().when(insuranceQuoteValidator).validate(validRequest);
        when(createInsuranceQuoteUseCase.execute(any()))
                .thenThrow(new BusinessException(errorMessage, "VALIDATION_ERROR", "Detalhes do erro"));

        assertThrows(BusinessException.class, () -> {
            controller.createInsuranceQuote(validRequest);
        });

        verify(insuranceQuoteValidator, times(1)).validate(validRequest);
    }

    private InsuranceQuoteRequest buildValidRequest() {
        return InsuranceQuoteRequest.builder()
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .offerId("adc56d77-348c-4bf0-908f-22d402ee715c")
                .category(InsuranceCategory.HOME)
                .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder()
                        .suggestedAmount(BigDecimal.valueOf(75.25))
                        .build())
                .totalCoverageAmount(BigDecimal.valueOf(825000.00))
                .build();
    }

    private InsuranceQuote buildExpectedQuote() {
        return InsuranceQuote.builder()
                .id(1L)
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .offerId("adc56d77-348c-4bf0-908f-22d402ee715c")
                .category(InsuranceCategory.HOME.name())
                .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder()
                        .suggestedAmount(BigDecimal.valueOf(75.25))
                        .build())
                .totalCoverageAmount(BigDecimal.valueOf(825000.00))
                .build();
    }

    private InsuranceQuoteResponse buildExpectedResponse() {
        return InsuranceQuoteResponse.builder()
                .id(1L)
                .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
                .offerId("adc56d77-348c-4bf0-908f-22d402ee715c")
                .category(InsuranceCategory.HOME)
                .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder()
                        .suggestedAmount(BigDecimal.valueOf(75.25))
                        .build())
                .totalCoverageAmount(BigDecimal.valueOf(825000.00))
                .build();
    }
} 