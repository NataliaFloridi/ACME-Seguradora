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

import com.acme.insurancecompany.application.usecase.insurancequote.CreateInsuranceQuoteUseCaseImpl;
import com.acme.insurancecompany.domain.event.InsuranceQuoteCreatedEvent;
import com.acme.insurancecompany.domain.exception.ErrorCode;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.port.out.broker.MessageBrokerPort;
import com.acme.insurancecompany.domain.port.out.port.InsuranceQuotePort;

@ExtendWith(MockitoExtension.class)
public class CreateInsuranceQuoteUseCaseImplTest {
    
    @Mock
    private InsuranceQuotePort insuranceQuotePort;
    
    @Mock
    private MessageBrokerPort messageBrokerPort;

    @InjectMocks
    private CreateInsuranceQuoteUseCaseImpl createInsuranceQuoteUseCaseImpl;
    
    @Test
    void testCreateInsuranceQuote() {
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .id(1L)
            .build();

        when(insuranceQuotePort.saveInsuranceQuote(any())).thenReturn(insuranceQuote);
     
        InsuranceQuote result = createInsuranceQuoteUseCaseImpl.execute(insuranceQuote);

        assertEquals(insuranceQuote, result);
        verify(insuranceQuotePort, times(1)).saveInsuranceQuote(insuranceQuote);
        verify(messageBrokerPort, times(1)).sendQuoteCreatedMessage(new InsuranceQuoteCreatedEvent());
    }

    @Test
    void testCreateInsuranceQuote_Exception() {
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();

        when(insuranceQuotePort.saveInsuranceQuote(any())).thenThrow(new RuntimeException("Test exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            createInsuranceQuoteUseCaseImpl.execute(insuranceQuote));

        assertEquals(ErrorCode.ERRO_AO_PROCESSAR_COTACAO.getMessage(), exception.getMessage());
       
        verify(insuranceQuotePort, times(1)).saveInsuranceQuote(insuranceQuote);
        verify(messageBrokerPort, times(0)).sendQuoteCreatedMessage(new InsuranceQuoteCreatedEvent());
    }

    @Test
    void testCreateInsuranceQuote_IllegalArgumentException() {
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();

        when(insuranceQuotePort.saveInsuranceQuote(any())).thenThrow(new IllegalArgumentException("Test exception"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            createInsuranceQuoteUseCaseImpl.execute(insuranceQuote));

        assertEquals(ErrorCode.ERRO_AO_PROCESSAR_COTACAO.getMessage(), exception.getMessage());

        verify(insuranceQuotePort, times(1)).saveInsuranceQuote(insuranceQuote);
        verify(messageBrokerPort, times(0)).sendQuoteCreatedMessage(new InsuranceQuoteCreatedEvent());
    }

}
