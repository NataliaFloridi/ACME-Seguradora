package com.acme.insurancecompany.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.acme.insurancecompany.domain.enums.InsuranceCategory;
import com.acme.insurancecompany.domain.model.Assistance;
import com.acme.insurancecompany.domain.model.Coverage;
import com.acme.insurancecompany.domain.model.Customer;
import com.acme.insurancecompany.domain.model.MonthlyPremiumAmount;
import com.acme.insurancecompany.infrastructure.adapter.serializer.AssistanceListSerializer;
import com.acme.insurancecompany.infrastructure.adapter.serializer.CoverageListSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
public class InsuranceQuoteResponse {
    
    private Long id;
    
    @JsonProperty("insurance_policy_id")
    private Long insurancePolicyId;
    
    @JsonProperty("product_id")
    private String productId;
    
    @JsonProperty("offer_id")
    private String offerId;
    
    private InsuranceCategory category;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    @JsonProperty("total_monthly_premium_amount")
    private MonthlyPremiumAmount totalMonthlyPremiumAmount;
    
    @JsonProperty("total_coverage_amount")
    private BigDecimal totalCoverageAmount;
    
    @JsonSerialize(using = CoverageListSerializer.class)
    private List<Coverage> coverages;

    @JsonSerialize(using = AssistanceListSerializer.class)
    private List<Assistance> assistances;

    private Customer customer;
    
    private String status;
}
