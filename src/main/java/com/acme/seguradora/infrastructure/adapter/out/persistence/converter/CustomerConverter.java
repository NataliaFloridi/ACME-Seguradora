package com.acme.seguradora.infrastructure.adapter.out.persistence.converter;

import java.io.IOException;

import com.acme.seguradora.domain.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class CustomerConverter implements AttributeConverter<Customer, String> {

    private final ObjectMapper objectMapper;

    public CustomerConverter() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String convertToDatabaseColumn(Customer attribute) {
        try {
            if (attribute == null) {
                return null;
            }
            String json = objectMapper.writeValueAsString(attribute);
            return json;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter Customer para JSON", e);
        }
    }

    @Override
    public Customer convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }
            Customer customer = objectMapper.readValue(dbData, Customer.class);
            return customer;
        } catch (IOException e) {
            log.error("Erro ao converter JSON para Customer. JSON: {}, Erro: {}", dbData, e.getMessage());
            throw new RuntimeException("Erro ao converter JSON para Customer", e);
        }
    }
} 