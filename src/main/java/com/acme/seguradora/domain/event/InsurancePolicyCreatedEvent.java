package com.acme.seguradora.domain.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePolicyCreatedEvent {
    
    @JsonProperty("policy_id")
    private Long policyId;
    
    @JsonProperty("quote_id")
    private Long quoteId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @JsonProperty("issued_at")
    private LocalDateTime issuedAt;
    
    @JsonProperty("policy_number")
    private String policyNumber;
    
    private String status;
} 