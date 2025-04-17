package com.acme.seguradora.infrastructure.adapter.out.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.acme.seguradora.domain.event.InsuranceQuoteCreatedEvent;
import com.acme.seguradora.domain.model.InsuranceQuote;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("insuranceQuoteMessagingService")
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true")
public class RabbitMQInsuranceQuoteMessagingService implements InsuranceQuoteMessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String quoteRoutingKey;
    private final ObjectMapper objectMapper;
    private final RetryTemplate retryTemplate;

    public RabbitMQInsuranceQuoteMessagingService(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.name}") String exchangeName,
            @Value("${rabbitmq.routing.key.quote}") String quoteRoutingKey,
            ObjectMapper objectMapper,
            RetryTemplate retryTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.quoteRoutingKey = quoteRoutingKey;
        this.objectMapper = objectMapper;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void sendQuoteCreatedEvent(InsuranceQuote quote) {
        try {
            InsuranceQuoteCreatedEvent event = InsuranceQuoteCreatedEvent.builder()
                    .quoteId(quote.getId())
                    .productId(quote.getProductId())
                    .offerId(quote.getOfferId())
                    .totalCoverageAmount(quote.getTotalCoverageAmount())
                    .createdAt(quote.getCreatedAt())
                    .build();

            String message = objectMapper.writeValueAsString(event);
            
            retryTemplate.execute(context -> {
                try {
                    rabbitTemplate.convertAndSend(exchangeName, quoteRoutingKey, message);
                    log.info("Evento de cotação de seguro enviado com sucesso. ID da cotação: {}", quote.getId());
                    return null;
                } catch (AmqpException e) {
                    log.warn("Tentativa {} falhou ao enviar evento de cotação. ID da cotação: {}", 
                        context.getRetryCount() + 1, quote.getId(), e);
                    throw e;
                }
            });
        } catch (Exception e) {
            log.error("Erro ao processar evento de cotação de seguro após todas as tentativas. ID da cotação: {}", 
                quote.getId(), e);
            throw new RuntimeException("Erro ao processar evento de cotação de seguro", e);
        }
    }
} 