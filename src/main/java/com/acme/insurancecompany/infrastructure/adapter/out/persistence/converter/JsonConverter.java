package com.acme.insurancecompany.infrastructure.adapter.out.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class JsonConverter<T> implements AttributeConverter<T, String> {

    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeReference;

    public JsonConverter(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        try {
            if (attribute == null) {
                return null;
            }
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter objeto para JSON: {}", e.getMessage());
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(dbData, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter JSON para objeto: {}", e.getMessage());
            throw new RuntimeException("Erro ao converter JSON para objeto", e);
        }
    }
} 