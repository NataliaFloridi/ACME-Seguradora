package com.acme.insurancecompany.domain.port.out.port;

import com.acme.insurancecompany.domain.model.InsuranceQuote;

public interface InsuranceQuotePort {
    InsuranceQuote saveInsuranceQuote(InsuranceQuote insuranceQuote);
    InsuranceQuote findInsuranceQuoteById(Long insuranceQuoteId);
    InsuranceQuote updateInsuranceQuoteWithPolicyId(Long insuranceQuoteId, Long policyId);
}
