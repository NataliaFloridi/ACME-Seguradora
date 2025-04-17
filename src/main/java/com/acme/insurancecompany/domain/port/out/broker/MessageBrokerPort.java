package com.acme.insurancecompany.domain.port.out.broker;

import com.acme.insurancecompany.domain.event.InsuranceQuoteCreatedEvent;

public interface MessageBrokerPort {
    void sendQuoteCreatedMessage(InsuranceQuoteCreatedEvent event);
} 