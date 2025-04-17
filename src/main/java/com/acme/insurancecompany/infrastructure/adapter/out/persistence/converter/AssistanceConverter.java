package com.acme.insurancecompany.infrastructure.adapter.out.persistence.converter;

import com.acme.insurancecompany.domain.model.Assistance;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Converter
public class AssistanceConverter implements AttributeConverter<List<Assistance>, String> {

    private final ObjectMapper objectMapper;

    public AssistanceConverter() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String convertToDatabaseColumn(List<Assistance> attribute) {
        try {
            if (attribute == null) {
                return null;
            }
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            log.error("Erro ao converter lista de assistências para JSON: {}", e.getMessage());
            throw new RuntimeException("Erro ao converter lista de assistências para JSON", e);
        }
    }

    @Override
    public List<Assistance> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(dbData, new TypeReference<List<Assistance>>() {});
        } catch (Exception e) {
            log.error("Erro ao converter JSON para lista de assistências: {}", e.getMessage());
            throw new RuntimeException("Erro ao converter JSON para lista de assistências", e);
        }
    }
} 