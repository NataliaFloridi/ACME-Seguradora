package com.acme.seguradora.domain.port.in.usecase.insurancequote;

import com.acme.seguradora.domain.model.InsuranceQuote;

public interface UpdateInsuranceQuoteWithPolicyUseCase {
    InsuranceQuote execute(InsuranceQuote insuranceQuote);
}
