package com.acme.insurancecompany.application.usecase.insurancequote;

import com.acme.insurancecompany.domain.exception.ResourceNotFoundException;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.port.out.port.InsuranceQuotePort;
import com.acme.insurancecompany.domain.enums.InsuranceCategory;
import com.acme.insurancecompany.domain.model.MonthlyPremiumAmount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindInsuranceQuoteUseCaseImplTest {

    @Mock
    private InsuranceQuotePort insuranceQuotePort;

    @InjectMocks
    private FindInsuranceQuoteUseCaseImpl useCase;

    private Long quoteId;
    private InsuranceQuote quote;

    @BeforeEach
    void setUp() {
        quoteId = 1L;
        quote = InsuranceQuote.builder()
            .id(quoteId)
            .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
            .offerId("adc56d77-348c-4bf0-908f-22d402ee715c")
            .category(InsuranceCategory.HOME.name())
            .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder()
                .suggestedAmount(BigDecimal.valueOf(75.25))
                .build())
            .totalCoverageAmount(BigDecimal.valueOf(825000.00))
            .build();
    }

    @Test
    void shouldFindInsuranceQuoteById() {
        when(insuranceQuotePort.findInsuranceQuoteById(quoteId)).thenReturn(quote);

        InsuranceQuote result = useCase.execute(quoteId);

        assertThat(result).isNotNull().isEqualTo(quote);
        verify(insuranceQuotePort).findInsuranceQuoteById(quoteId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenQuoteDoesNotExist() {
        when(insuranceQuotePort.findInsuranceQuoteById(quoteId))
            .thenThrow(new ResourceNotFoundException("Cotação não encontrada"));

        assertThatThrownBy(() -> useCase.execute(quoteId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Cotação não encontrada");

        verify(insuranceQuotePort).findInsuranceQuoteById(quoteId);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenQuoteIdIsNull() {
        assertThatThrownBy(() -> useCase.execute(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("O ID da cotação não pode ser Nulo, informe uma cotação válida");
    }
} 