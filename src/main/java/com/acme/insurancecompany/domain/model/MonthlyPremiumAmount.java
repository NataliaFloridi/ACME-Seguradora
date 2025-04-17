package com.acme.insurancecompany.domain.model;   

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MonthlyPremiumAmount {

    private BigDecimal maxAmount;
    private BigDecimal minAmount;
    private BigDecimal suggestedAmount;
    
    @JsonCreator
    public static MonthlyPremiumAmount fromValue(@JsonProperty("total_monthly_premium_amount") double value) {
        return MonthlyPremiumAmount.builder()
            .suggestedAmount(BigDecimal.valueOf(value))
            .build();
    }

    @JsonCreator
    public MonthlyPremiumAmount(BigDecimal suggestedAmount) {
        this.suggestedAmount = suggestedAmount;
    }
} 