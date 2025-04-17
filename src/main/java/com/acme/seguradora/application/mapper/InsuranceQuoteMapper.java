package com.acme.seguradora.application.mapper;

import org.springframework.stereotype.Component;

import com.acme.seguradora.application.dto.InsuranceQuoteRequest;
import com.acme.seguradora.application.dto.InsuranceQuoteResponse;
import com.acme.seguradora.domain.enums.InsuranceCategory;
import com.acme.seguradora.domain.model.InsuranceQuote;
import com.acme.seguradora.infrastructure.adapter.out.persistence.entity.InsuranceQuoteEntity;

@Component
public class InsuranceQuoteMapper {

    public InsuranceQuote toDomain(InsuranceQuoteRequest request) {
        return InsuranceQuote.builder()
                .productId(request.getProductId())
                .offerId(request.getOfferId())
                .category(request.getCategory().toString())
                .totalMonthlyPremiumAmount(request.getTotalMonthlyPremiumAmount())
                .totalCoverageAmount(request.getTotalCoverageAmount())
                .coverages(request.getCoverages())
                .assistances(request.getAssistances())
                .customer(request.getCustomer())
                .status("PENDENTE")
                .build();
    }

    public InsuranceQuoteEntity toEntity(InsuranceQuote domain) {
        return InsuranceQuoteEntity.builder()
                .id(domain.getId())
                .insurancePolicyId(domain.getInsurancePolicyId())
                .productId(domain.getProductId())
                .offerId(domain.getOfferId())
                .category(InsuranceCategory.valueOf(domain.getCategory()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .totalMonthlyPremiumAmount(domain.getTotalMonthlyPremiumAmount())
                .totalCoverageAmount(domain.getTotalCoverageAmount())
                .coverages(domain.getCoverages())
                .assistances(domain.getAssistances())
                .customer(domain.getCustomer())
                .status(domain.getStatus())
                .build();
    }

    public InsuranceQuote toDomain(InsuranceQuoteEntity entity) {
        return InsuranceQuote.builder()
                .id(entity.getId())
                .insurancePolicyId(entity.getInsurancePolicyId())
                .productId(entity.getProductId())
                .offerId(entity.getOfferId())
                .category(entity.getCategory() != null ? entity.getCategory().name() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .totalMonthlyPremiumAmount(entity.getTotalMonthlyPremiumAmount())
                .totalCoverageAmount(entity.getTotalCoverageAmount())
                .coverages(entity.getCoverages())
                .assistances(entity.getAssistances())
                .customer(entity.getCustomer())
                .status(entity.getStatus())
                .build();
    }

    public InsuranceQuoteResponse toResponse(InsuranceQuote domain) {
        return InsuranceQuoteResponse.builder()
                .id(domain.getId())
                .insurancePolicyId(domain.getInsurancePolicyId())
                .productId(domain.getProductId())
                .offerId(domain.getOfferId())
                .category(InsuranceCategory.valueOf(domain.getCategory()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .totalMonthlyPremiumAmount(domain.getTotalMonthlyPremiumAmount())
                .totalCoverageAmount(domain.getTotalCoverageAmount())
                .coverages(domain.getCoverages())
                .assistances(domain.getAssistances())
                .customer(domain.getCustomer())
                .status(domain.getStatus())
                .build();
    }
} 