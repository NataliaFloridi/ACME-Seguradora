package com.acme.seguradora.domain.port.out.broker;

import com.acme.seguradora.domain.event.InsuranceQuoteCreatedEvent;

public interface MessageBrokerPort {
    void sendQuoteCreatedMessage(InsuranceQuoteCreatedEvent event);
} 