package com.acme.insurancecompany.application.dto;

import java.time.LocalDate;

import com.acme.insurancecompany.domain.enums.CustomerType;
import com.acme.insurancecompany.domain.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class CustomerRequest {
    
    @NotBlank(message = "O número do documento é obrigatório")
    @Pattern(regexp = "^\\d{11}$", message = "CPF inválido")
    @JsonProperty("document_number")
    private String documentNumber;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotNull(message = "O tipo de cliente é obrigatório")
    private CustomerType type;

    @NotNull(message = "O gênero é obrigatório")
    private Gender gender;

    @NotNull(message = "A data de nascimento é obrigatória")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "O número de telefone é obrigatório")
    @JsonProperty("phone_number")
    private Long phoneNumber;
} 