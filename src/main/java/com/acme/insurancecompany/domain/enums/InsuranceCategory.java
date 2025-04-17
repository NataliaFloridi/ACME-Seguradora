package com.acme.insurancecompany.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InsuranceCategory {
    HOME("HOME"),
    AUTO("AUTO"),
    LIFE("LIFE"),
    TRAVEL("TRAVEL"),
    HEALTH("HEALTH");

    private final String value;  
} 