package com.acme.insurancecompany.application.usecase.insurancequote;

import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.model.MonthlyPremiumAmount;
import com.acme.insurancecompany.domain.port.out.broker.MessageBrokerPort;
import com.acme.insurancecompany.domain.port.out.port.InsuranceQuotePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInsuranceQuoteUseCaseImplTest {

    @Mock
    private InsuranceQuotePort insuranceQuotePort;

    @Mock
    private MessageBrokerPort messageBrokerPort;

    @InjectMocks
    private CreateInsuranceQuoteUseCaseImpl useCase;

    @Test
    void shouldCreateInsuranceQuote() {
        InsuranceQuote quote = InsuranceQuote.builder()
            .id(1L)
            .productId("PROD-001")
            .offerId("OFFER-001")
            .totalCoverageAmount(BigDecimal.valueOf(1000.00))
            .totalMonthlyPremiumAmount(MonthlyPremiumAmount.builder()
                .suggestedAmount(BigDecimal.valueOf(100.00))
                .build())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .status("PENDENTE")
            .build();

        when(insuranceQuotePort.saveInsuranceQuote(any())).thenReturn(quote);

        InsuranceQuote result = useCase.execute(quote);

        assertThat(result).isNotNull().isEqualTo(quote);
        verify(messageBrokerPort).sendQuoteCreatedMessage(any());
    }
} 