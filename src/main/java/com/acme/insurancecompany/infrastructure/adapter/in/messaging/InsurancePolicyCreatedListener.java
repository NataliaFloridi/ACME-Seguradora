package com.acme.insurancecompany.infrastructure.adapter.in.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.acme.insurancecompany.domain.event.InsurancePolicyCreatedEvent;
import com.acme.insurancecompany.domain.port.out.port.InsuranceQuotePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true")
public class InsurancePolicyCreatedListener {

    private final InsuranceQuotePort insuranceQuotePort;

    @RabbitListener(queues = "${rabbitmq.queue.policy-created}")
    public void handlePolicyCreated(InsurancePolicyCreatedEvent event) {
        try {
            insuranceQuotePort.updateInsuranceQuoteWithPolicyId(
                event.getQuoteId(),
                event.getPolicyId()
            );
        } catch (Exception e) {
            log.error("Erro ao atualizar cotação com a apólice: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
} 