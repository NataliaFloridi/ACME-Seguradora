package com.acme.seguradora.application.usecase.insurancequote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.acme.seguradora.domain.exception.ErrorCode;
import com.acme.seguradora.domain.model.InsuranceQuote;
import com.acme.seguradora.domain.port.out.port.InsuranceQuotePort;

@ExtendWith(MockitoExtension.class)
public class UpdateInsuranceQuoteWithPolicyUseCaseImplTest {
    
    @Mock
    private InsuranceQuotePort insuranceQuotePort;

    @InjectMocks
    private UpdateInsuranceQuoteWithPolicyUseCaseImpl updateInsuranceQuoteWithPolicyUseCaseImpl;
    
    @Test
    void testUpdateInsuranceQuoteWithPolicy() {
        Long quoteId = 1L;
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .id(1L)
            .build();
            
        when(insuranceQuotePort.findInsuranceQuoteById(any())).thenReturn(insuranceQuote);
        when(insuranceQuotePort.updateInsuranceQuoteWithPolicyId(any(), any())).thenReturn(insuranceQuote);

        InsuranceQuote result = updateInsuranceQuoteWithPolicyUseCaseImpl.execute(insuranceQuote);

        assertEquals(insuranceQuote, result);

        verify(insuranceQuotePort, times(1)).findInsuranceQuoteById(quoteId);
        verify(insuranceQuotePort, times(1)).updateInsuranceQuoteWithPolicyId(quoteId, insuranceQuote.getInsurancePolicyId());
            
    }
    
    @Test
    void testUpdateInsuranceQuoteWithPolicy_Exception() {
        Long quoteId = 1L;
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .id(1L)
            .build();

        when(insuranceQuotePort.findInsuranceQuoteById(any())).thenReturn(insuranceQuote);
        when(insuranceQuotePort.updateInsuranceQuoteWithPolicyId(any(), any())).thenThrow(new RuntimeException("Test exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            updateInsuranceQuoteWithPolicyUseCaseImpl.execute(insuranceQuote));

        assertEquals(ErrorCode.ERRO_AO_ATUALIZAR_COTACAO_DE_SEGURO.getMessage(), exception.getMessage());

        verify(insuranceQuotePort, times(1)).findInsuranceQuoteById(quoteId);
        verify(insuranceQuotePort, times(1)).updateInsuranceQuoteWithPolicyId(quoteId, insuranceQuote.getInsurancePolicyId());
    }    
}
