package com.acme.insurancecompany.domain.port.in.usecase.insurancequote;

import com.acme.insurancecompany.domain.model.InsuranceQuote;

public interface CreateInsuranceQuoteUseCase {
    InsuranceQuote execute(InsuranceQuote insuranceQuote);
}
