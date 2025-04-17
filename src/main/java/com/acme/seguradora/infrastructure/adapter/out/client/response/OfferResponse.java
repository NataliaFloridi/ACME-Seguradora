package com.acme.seguradora.infrastructure.adapter.out.client.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.acme.seguradora.domain.model.Assistance;
import com.acme.seguradora.domain.model.Coverage;
import com.acme.seguradora.domain.model.MonthlyPremiumAmount;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class OfferResponse {
    
    private String id;
    
    @JsonProperty("product_id")
    private String productId;
    
    private String name;
    private boolean active;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    private List<Coverage> coverages;
    private List<Assistance> assistances;
    
    @JsonProperty("monthly_premium_amount")
    private MonthlyPremiumAmount monthlyPremiumAmount;

} 