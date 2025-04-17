package com.acme.insurancecompany.application.dto;

import java.math.BigDecimal;
import java.util.List;

import com.acme.insurancecompany.domain.enums.InsuranceCategory;
import com.acme.insurancecompany.domain.model.Assistance;
import com.acme.insurancecompany.domain.model.Coverage;
import com.acme.insurancecompany.domain.model.Customer;
import com.acme.insurancecompany.domain.model.MonthlyPremiumAmount;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class InsuranceQuoteRequest {
    
    @NotBlank(message = "O ID do produto é obrigatório")
    @JsonProperty("product_id")
    private String productId;
    
    @NotBlank(message = "O ID da oferta é obrigatório")
    @JsonProperty("offer_id")
    private String offerId;
    
    @NotNull(message = "A categoria é obrigatória")
    private InsuranceCategory category;
    
    @NotNull(message = "O valor do prêmio mensal é obrigatório")
    @JsonProperty("total_monthly_premium_amount")
    private MonthlyPremiumAmount totalMonthlyPremiumAmount;
    
    @NotNull(message = "O valor total das coberturas é obrigatório")
    @JsonProperty("total_coverage_amount")
    private BigDecimal totalCoverageAmount;
    
    @NotNull(message = "As coberturas são obrigatórias")
    @Size(min = 1, message = "Deve haver pelo menos uma cobertura")
    private List<Coverage> coverages;
    
    @NotNull(message = "As assistências são obrigatórias")
    @Size(min = 1, message = "Deve haver pelo menos uma assistência")
    private List<Assistance> assistances;
    
    @NotNull(message = "Os dados do cliente são obrigatórios")
    @Valid
    private Customer customer;
}
