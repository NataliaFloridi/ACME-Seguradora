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
import com.acme.seguradora.domain.exception.ResourceNotFoundException;
import com.acme.seguradora.domain.model.InsuranceQuote;
import com.acme.seguradora.domain.port.out.port.InsuranceQuotePort;

@ExtendWith(MockitoExtension.class)
public class FindInsuranceQuoteUseCaseImplTest {
    
    @Mock
    private InsuranceQuotePort insuranceQuotePort;

    @InjectMocks
    private FindInsuranceQuoteUseCaseImpl findInsuranceQuoteUseCaseImpl;
    
    @Test
    void testFindInsuranceQuoteById() {
        Long quoteId = 1L;
        InsuranceQuote expectedQuote = InsuranceQuote.builder()
            .id(quoteId)
            .build();

        when(insuranceQuotePort.findInsuranceQuoteById(any())).thenReturn(expectedQuote);
        
        InsuranceQuote result = findInsuranceQuoteUseCaseImpl.execute(quoteId);

        assertEquals(expectedQuote, result);
        verify(insuranceQuotePort, times(1)).findInsuranceQuoteById(quoteId);
    }

    @Test
    void testFindInsuranceQuoteById_NotFound() {
        Long quoteId = 1L;
        when(insuranceQuotePort.findInsuranceQuoteById(any())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            findInsuranceQuoteUseCaseImpl.execute(quoteId));
            
        assertEquals(ErrorCode.COTACAO_NAO_ENCONTRADA.getMessage(), exception.getMessage());
        verify(insuranceQuotePort, times(1)).findInsuranceQuoteById(quoteId);
    }

    @Test
    void testFindInsuranceQuoteById_Exception() {
        Long quoteId = 1L;
        when(insuranceQuotePort.findInsuranceQuoteById(any())).thenThrow(new RuntimeException("Test exception"));


        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            findInsuranceQuoteUseCaseImpl.execute(quoteId));
            
        assertEquals(ErrorCode.ERRO_AO_BUSCAR_COTACAO_DE_SEGURO.getMessage(), exception.getMessage());
        
        verify(insuranceQuotePort, times(1)).findInsuranceQuoteById(quoteId);
    }
}
