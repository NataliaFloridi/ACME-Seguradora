package com.acme.insurancecompany.domain.port.in.usecase.insurancequote;

import com.acme.insurancecompany.domain.model.InsuranceQuote;

public interface FindInsuranceQuoteUseCase {
    InsuranceQuote execute(Long insuranceQuoteId);
}