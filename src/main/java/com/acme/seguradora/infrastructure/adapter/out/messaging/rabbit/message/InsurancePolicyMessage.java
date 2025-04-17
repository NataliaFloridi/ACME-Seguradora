package com.acme.seguradora.infrastructure.adapter.out.messaging.rabbit.message;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePolicyMessage {
    private String insurancePolicyId;
    private String insuranceQuoteId;
    private LocalDateTime issuedAt;
    private String status;
} 