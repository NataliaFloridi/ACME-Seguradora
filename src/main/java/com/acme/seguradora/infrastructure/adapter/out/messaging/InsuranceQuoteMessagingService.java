package com.acme.seguradora.infrastructure.adapter.out.messaging;

import com.acme.seguradora.domain.model.InsuranceQuote;

public interface InsuranceQuoteMessagingService {
    void sendQuoteCreatedEvent(InsuranceQuote quote);
} 