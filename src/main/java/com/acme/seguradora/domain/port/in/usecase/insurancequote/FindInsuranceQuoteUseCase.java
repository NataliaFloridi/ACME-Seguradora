package com.acme.seguradora.domain.port.in.usecase.insurancequote;

import com.acme.seguradora.domain.model.InsuranceQuote;

public interface FindInsuranceQuoteUseCase {
    InsuranceQuote execute(Long insuranceQuoteId);
}