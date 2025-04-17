package com.acme.seguradora.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.acme.seguradora.domain.model.Assistance;
import com.acme.seguradora.domain.model.Coverage;
import com.acme.seguradora.domain.model.Customer;
import com.acme.seguradora.domain.model.MonthlyPremiumAmount;
import com.acme.seguradora.domain.enums.InsuranceCategory;
import com.acme.seguradora.infrastructure.adapter.out.persistence.converter.AssistanceConverter;
import com.acme.seguradora.infrastructure.adapter.out.persistence.converter.CoverageConverter;
import com.acme.seguradora.infrastructure.adapter.out.persistence.converter.CustomerConverter;
import com.acme.seguradora.infrastructure.adapter.out.persistence.converter.MonthlyPremiumAmountConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insurance_quotes")
public class InsuranceQuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quote_seq")
    @SequenceGenerator(name = "quote_seq", sequenceName = "insurance_quotes_seq", allocationSize = 1)
    private Long id;

    @Column(name = "insurance_policy_id")
    private Long insurancePolicyId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "offer_id", nullable = false)
    private String offerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceCategory category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "total_monthly_premium_amount", nullable = false)
    @Convert(converter = MonthlyPremiumAmountConverter.class)
    private MonthlyPremiumAmount totalMonthlyPremiumAmount;

    @Column(name = "total_coverage_amount", nullable = false)
    private BigDecimal totalCoverageAmount;

    @Convert(converter = CoverageConverter.class)
    @Column(columnDefinition = "text")
    private List<Coverage> coverages;

    @Convert(converter = AssistanceConverter.class)
    @Column(columnDefinition = "text")
    private List<Assistance> assistances;

    @Convert(converter = CustomerConverter.class)
    @Column(columnDefinition = "text")
    private Customer customer;

    @Column(nullable = false)
    private String status;
} 