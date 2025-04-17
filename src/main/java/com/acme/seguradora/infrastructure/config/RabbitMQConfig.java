package com.acme.seguradora.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true", matchIfMissing = false)
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.insurance}")
    private String exchange;

    @Value("${rabbitmq.queue.quote-received}")
    private String quoteReceivedQueue;

    @Value("${rabbitmq.queue.policy-created}")
    private String policyCreatedQueue;

    @Value("${rabbitmq.routing-key.quote-received}")
    private String quoteReceivedRoutingKey;

    @Value("${rabbitmq.routing-key.policy-created}")
    private String policyCreatedRoutingKey;

    @Bean
    public TopicExchange insuranceExchange() {
        return new TopicExchange(exchange);
    }

    @Bean("quoteReceivedQueue")
    public Queue quoteReceivedQueue() {
        return new Queue(quoteReceivedQueue);
    }

    @Bean("policyCreatedQueue")
    public Queue policyCreatedQueue() {
        return new Queue(policyCreatedQueue);
    }

    @Bean
    public Binding quoteReceivedBinding(@Qualifier("quoteReceivedQueue") Queue quoteReceivedQueue, TopicExchange insuranceExchange) {
        return BindingBuilder
            .bind(quoteReceivedQueue)
            .to(insuranceExchange)
            .with(quoteReceivedRoutingKey);
    }

    @Bean
    public Binding policyCreatedBinding(@Qualifier("policyCreatedQueue") Queue policyCreatedQueue, TopicExchange insuranceExchange) {
        return BindingBuilder
            .bind(policyCreatedQueue)
            .to(insuranceExchange)
            .with(policyCreatedRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
} 