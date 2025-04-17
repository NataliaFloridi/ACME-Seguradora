package com.acme.seguradora.domain.port.out.port;

import com.acme.seguradora.domain.model.InsuranceQuote;

public interface InsuranceQuotePort {
    InsuranceQuote saveInsuranceQuote(InsuranceQuote insuranceQuote);
    InsuranceQuote findInsuranceQuoteById(Long insuranceQuoteId);
    InsuranceQuote updateInsuranceQuoteWithPolicyId(Long insuranceQuoteId, Long policyId);
}
