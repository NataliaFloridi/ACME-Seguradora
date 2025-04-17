package com.acme.insurancecompany.infrastructure.adapter.out.messaging;

import com.acme.insurancecompany.domain.model.InsuranceQuote;

public interface InsuranceQuoteMessagingService {
    void sendQuoteCreatedEvent(InsuranceQuote quote);
} 