package com.acme.seguradora.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class InsuranceQuote {
    private Long id;
    private Long insurancePolicyId;
    private String productId;
    private String offerId;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MonthlyPremiumAmount totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
    private List<Coverage> coverages;
    private List<Assistance> assistances;
    private Customer customer;
    private String status;
}