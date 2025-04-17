package com.acme.insurancecompany.application.usecase.insurancequote;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.acme.insurancecompany.domain.event.InsuranceQuoteCreatedEvent;
import com.acme.insurancecompany.domain.exception.ErrorCode;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.CreateInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.port.out.broker.MessageBrokerPort;
import com.acme.insurancecompany.domain.port.out.port.InsuranceQuotePort;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CreateInsuranceQuoteUseCaseImpl implements CreateInsuranceQuoteUseCase {
    
    private final InsuranceQuotePort insuranceQuotePort;
    private final MessageBrokerPort messageBrokerPort;

    @Override
    public InsuranceQuote execute(InsuranceQuote insuranceQuote) {
        try {
            InsuranceQuote savedQuote = insuranceQuotePort.saveInsuranceQuote(insuranceQuote);
        
            InsuranceQuoteCreatedEvent event = InsuranceQuoteCreatedEvent.builder()
                .quoteId(savedQuote.getId())
                .productId(savedQuote.getProductId())
                .offerId(savedQuote.getOfferId())
                .totalCoverageAmount(savedQuote.getTotalCoverageAmount())
                .createdAt(LocalDateTime.now())
                .build();

        messageBrokerPort.sendQuoteCreatedMessage(event);
        return savedQuote;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.ERRO_AO_PROCESSAR_COTACAO.getMessage() + e.getMessage());
        }
    }
}