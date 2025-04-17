package com.acme.seguradora.infrastructure.adapter.out.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.acme.insurancecompany.domain.event.InsuranceQuoteCreatedEvent;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.infrastructure.adapter.out.persistence.adapter.broker.MessageBrokerAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class MessageBrokerAdapterTest {
    
    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    private MessageBrokerAdapter messageBrokerAdapter;
    
    @Test
    void testSendQuoteCreatedMessage() throws JsonProcessingException {

        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();

        when(objectMapper.writeValueAsString(insuranceQuote)).thenReturn("test");

        InsuranceQuoteCreatedEvent event = new InsuranceQuoteCreatedEvent();
        messageBrokerAdapter.sendQuoteCreatedMessage(event);  
            
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());
        verify(objectMapper, times(1)).writeValueAsString(insuranceQuote);
    }

    @Test
    void testSendQuoteCreatedMessage_Exception() throws JsonProcessingException {
        InsuranceQuote insuranceQuote = InsuranceQuote.builder()
            .build();

        when(objectMapper.writeValueAsString(insuranceQuote)).thenThrow(new JsonProcessingException("Test exception") {});
        InsuranceQuoteCreatedEvent event = new InsuranceQuoteCreatedEvent();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            messageBrokerAdapter.sendQuoteCreatedMessage(event);
        });

        assertEquals("Erro ao enviar mensagem para o broker", exception.getMessage());
        verify(rabbitTemplate, times(0)).convertAndSend(anyString(), anyString(), anyString());
        verify(objectMapper, times(1)).writeValueAsString(insuranceQuote);
    }
}
