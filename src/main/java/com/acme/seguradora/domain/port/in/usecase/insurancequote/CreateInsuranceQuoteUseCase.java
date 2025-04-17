package com.acme.seguradora.domain.port.in.usecase.insurancequote;

import com.acme.seguradora.domain.model.InsuranceQuote;

public interface CreateInsuranceQuoteUseCase {
    InsuranceQuote execute(InsuranceQuote insuranceQuote);
}
