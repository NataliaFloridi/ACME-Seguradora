package com.acme.insurancecompany.application.usecase.insurancequote;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateInsuranceQuoteWithPolicyUseCaseImplTest {

    @Mock
    private InsuranceQuotePort insuranceQuotePort;

    @InjectMocks
    private UpdateInsuranceQuoteWithPolicyUseCaseImpl useCase;

    private InsuranceQuote quote;
    private InsuranceQuote updatedQuote;

    @BeforeEach
    void setUp() {
        quote = InsuranceQuote.builder()
            .id(1L)
            .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
            .offerId("adc56d77-348c-4bf0-908f-22d402ee715c")
            .category(InsuranceCategory.HOME.name())
            .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder()
                .suggestedAmount(BigDecimal.valueOf(75.25))
                .build())
            .totalCoverageAmount(BigDecimal.valueOf(825000.00))
            .insurancePolicyId(1001L)
            .build();

        updatedQuote = InsuranceQuote.builder()
            .id(1L)
            .productId("1b2da7cc-b367-4196-8a78-9cfeec21f587")
            .offerId("adc56d77-348c-4bf0-908f-22d402ee715c")
            .category(InsuranceCategory.HOME.name())
            .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder()
                .suggestedAmount(BigDecimal.valueOf(75.25))
                .build())
            .totalCoverageAmount(BigDecimal.valueOf(825000.00))
            .insurancePolicyId(1001L)
            .build();
    }

    @Test
    void shouldUpdateQuoteWithPolicyInfo() {
        when(insuranceQuotePort.updateInsuranceQuoteWithPolicyId(quote.getId(), quote.getInsurancePolicyId()))
            .thenReturn(updatedQuote);

        InsuranceQuote result = useCase.execute(quote);

        assertThat(result).isNotNull().isEqualTo(updatedQuote);
        verify(insuranceQuotePort, times(1)).updateInsuranceQuoteWithPolicyId(quote.getId(), quote.getInsurancePolicyId());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUpdateFails() {
        when(insuranceQuotePort.updateInsuranceQuoteWithPolicyId(quote.getId(), quote.getInsurancePolicyId()))
            .thenThrow(new RuntimeException("Erro ao atualizar"));

        assertThatThrownBy(() -> useCase.execute(quote))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Erro ao atualizar cotação de seguro");

        verify(insuranceQuotePort, times(1)).updateInsuranceQuoteWithPolicyId(quote.getId(), quote.getInsurancePolicyId());
    }
} 