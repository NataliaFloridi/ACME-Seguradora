package com.acme.insurancecompany.domain.model;

import java.time.LocalDate;

import com.acme.insurancecompany.domain.enums.CustomerType;
import com.acme.insurancecompany.domain.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class Customer {

    @NotBlank(message = "O número do documento é obrigatório")
    @Pattern(regexp = "^\\d{11}$|^\\d{14}$", message = "Documento deve conter 11 (CPF) ou 14 (CNPJ) dígitos numéricos")
    @JsonProperty("document_number")
    private String documentNumber;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotNull(message = "O tipo de pessoa é obrigatório")
    private CustomerType type;

    private Gender gender;

    @NotNull(message = "A data de nascimento é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "O número de telefone é obrigatório")
    @JsonProperty("phone_number")
    private Long phoneNumber;

    private String customerId;
} 