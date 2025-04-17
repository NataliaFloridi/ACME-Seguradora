package com.acme.insurancecompany.infrastructure.adapter.out.persistence.converter;

import com.acme.insurancecompany.domain.model.Coverage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Converter
public class CoverageConverter implements AttributeConverter<List<Coverage>, String> {

    private final ObjectMapper objectMapper;

    public CoverageConverter() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String convertToDatabaseColumn(List<Coverage> attribute) {
        try {
            if (attribute == null) {
                return null;
            }
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter lista de coberturas para JSON", e);
        }
    }

    @Override
    public List<Coverage> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }
            JsonNode jsonNode = objectMapper.readTree(dbData);
            
            if (jsonNode.isArray()) {
                return objectMapper.readValue(dbData, new TypeReference<List<Coverage>>() {});
            } else if (jsonNode.isObject()) {
                try {
                    Map<String, BigDecimal> map = objectMapper.readValue(dbData, new TypeReference<Map<String, BigDecimal>>() {});
                    List<Coverage> coverages = new ArrayList<>();
                    for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
                        coverages.add(Coverage.builder()
                            .name(entry.getKey())
                            .amount(entry.getValue())
                            .build());
                    }
                    return coverages;
                } catch (Exception e) {
                    throw new RuntimeException("Formato inválido para coberturas: " + e.getMessage());
                }
            } else {
                log.error("Formato JSON inválido: {}", dbData);
                throw new RuntimeException("Formato JSON inválido para coberturas");
            }
        } catch (Exception e) {
            log.error("Erro ao converter JSON para lista de coberturas. JSON: {}, Erro: {}", dbData, e.getMessage());
            throw new RuntimeException("Erro ao converter JSON para lista de coberturas: " + e.getMessage());
        }
    }
} 