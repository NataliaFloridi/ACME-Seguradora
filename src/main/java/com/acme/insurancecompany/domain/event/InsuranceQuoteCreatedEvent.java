package com.acme.insurancecompany.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.acme.insurancecompany.domain.enums.InsuranceCategory;
import com.acme.insurancecompany.domain.model.Assistance;
import com.acme.insurancecompany.domain.model.Coverage;
import com.acme.insurancecompany.domain.model.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class InsuranceQuoteCreatedEvent {
    
    private Long quoteId;
    private String productId;
    private String offerId;
    private InsuranceCategory category;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;
    
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
    private List<Coverage> coverages;
    private List<Assistance> assistances;
    private Customer customer;
} 