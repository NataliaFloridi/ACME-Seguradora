package com.acme.seguradora.application.dto;

import java.time.LocalDate;

import com.acme.seguradora.domain.enums.CustomerType;
import com.acme.seguradora.domain.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class CustomerResponse {

    @JsonProperty("document_number")
    private String documentNumber;
    
    private String name;

    private CustomerType type;
    
    private Gender gender;
    
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    
    private String email;
    
    @JsonProperty("phone_number")
    private Long phoneNumber;
} 