package com.acme.insurancecompany.application.usecase.insurancequote;

import org.springframework.stereotype.Component;

import com.acme.insurancecompany.domain.exception.ErrorCode;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.FindInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.port.out.port.InsuranceQuotePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindInsuranceQuoteUseCaseImpl implements FindInsuranceQuoteUseCase {

    private final InsuranceQuotePort insuranceQuotePort;

    @Override
    public InsuranceQuote execute(Long insuranceQuoteId) {
        if (insuranceQuoteId == null) {
            log.error("ID da cotação não pode ser nulo");
            throw new IllegalArgumentException(ErrorCode.COTACAO_ID_VAZIO.getMessage());
        }
        
        return insuranceQuotePort.findInsuranceQuoteById(insuranceQuoteId);
    }
}
