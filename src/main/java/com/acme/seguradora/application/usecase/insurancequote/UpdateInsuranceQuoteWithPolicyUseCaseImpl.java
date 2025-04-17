package com.acme.seguradora.application.usecase.insurancequote;

import org.springframework.stereotype.Component;

import com.acme.seguradora.domain.exception.ErrorCode;
import com.acme.seguradora.domain.model.InsuranceQuote;
import com.acme.seguradora.domain.port.in.usecase.insurancequote.UpdateInsuranceQuoteWithPolicyUseCase;
import com.acme.seguradora.domain.port.out.port.InsuranceQuotePort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateInsuranceQuoteWithPolicyUseCaseImpl implements UpdateInsuranceQuoteWithPolicyUseCase {

    private final InsuranceQuotePort insuranceQuotePort;

    @Override
    @Transactional
    public InsuranceQuote execute(InsuranceQuote insuranceQuote) {
        try {
            return insuranceQuotePort.updateInsuranceQuoteWithPolicyId(
                insuranceQuote.getId(), 
                insuranceQuote.getInsurancePolicyId()
            );
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.ERRO_AO_ATUALIZAR_COTACAO_DE_SEGURO.getMessage(), e);
        }
    }
}

