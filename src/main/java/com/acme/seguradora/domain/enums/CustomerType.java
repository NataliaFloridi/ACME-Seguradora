package com.acme.seguradora.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerType {
    NATURAL("NATURAL"),
    LEGAL("LEGAL");

    private final String value;
} 