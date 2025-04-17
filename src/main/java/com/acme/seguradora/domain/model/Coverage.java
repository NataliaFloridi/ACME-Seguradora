package com.acme.seguradora.domain.model;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class Coverage {
    
    @NotBlank(message = "O nome da cobertura é obrigatório")
    private String name;
    
    @NotNull(message = "O valor da cobertura é obrigatório")
    @Positive(message = "O valor da cobertura deve ser positivo")
    private BigDecimal amount;

    @JsonIgnore
    private final Map<String, BigDecimal> coverages = Map.of(
        "Incêndio", new BigDecimal("500000.00"),
        "Desastres naturais", new BigDecimal("600000.00"),
        "Responsabilidade civil", new BigDecimal("80000.00"),
        "Roubo", new BigDecimal("100000.00")
    );

    @JsonIgnore
    private final BigDecimal totalCoverageAmount = new BigDecimal("1280000.00");
}