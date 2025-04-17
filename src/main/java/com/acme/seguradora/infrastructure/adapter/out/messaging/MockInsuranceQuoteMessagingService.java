package com.acme.seguradora.infrastructure.adapter.out.messaging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.acme.seguradora.domain.model.InsuranceQuote;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mockInsuranceQuoteMessagingService")
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "false", matchIfMissing = true)
public class MockInsuranceQuoteMessagingService implements InsuranceQuoteMessagingService {

    @Override
    public void sendQuoteCreatedEvent(InsuranceQuote quote) {
        log.info("MOCK: Evento de cotação de seguro enviado com sucesso. ID da cotação: {}", quote.getId());
    }
} 