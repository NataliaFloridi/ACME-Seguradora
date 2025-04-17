package com.acme.seguradora.infrastructure.adapter.out.persistence.adapter.broker;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.acme.seguradora.domain.event.InsuranceQuoteCreatedEvent;
import com.acme.seguradora.domain.model.InsuranceQuote;
import com.acme.seguradora.domain.port.out.broker.MessageBrokerPort;
import com.acme.seguradora.infrastructure.adapter.out.messaging.InsuranceQuoteMessagingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageBrokerAdapter implements MessageBrokerPort {

    @Qualifier("mockInsuranceQuoteMessagingService")
    private final InsuranceQuoteMessagingService insuranceQuoteMessagingService;

    @Override
    public void sendQuoteCreatedMessage(InsuranceQuoteCreatedEvent event) {
        log.info("Enviando mensagem de cotação criada: {}", event);
        
        InsuranceQuote quote = InsuranceQuote.builder()
            .id(event.getQuoteId())
            .productId(event.getProductId())
            .offerId(event.getOfferId())
            .totalCoverageAmount(event.getTotalCoverageAmount())
            .createdAt(event.getCreatedAt())
            .build();
            
        insuranceQuoteMessagingService.sendQuoteCreatedEvent(quote);
    }
} 