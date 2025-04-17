package com.acme.seguradora.infrastructure.adapter.out.persistence.converter;

import com.acme.seguradora.domain.model.MonthlyPremiumAmount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

@Slf4j
@Converter
public class MonthlyPremiumAmountConverter implements AttributeConverter<MonthlyPremiumAmount, String> {

    private final ObjectMapper objectMapper;

    public MonthlyPremiumAmountConverter() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String convertToDatabaseColumn(MonthlyPremiumAmount attribute) {
        try {
            if (attribute == null) return null;
            ObjectNode jsonNode = objectMapper.createObjectNode();
            
            if (attribute.getMaxAmount() != null) {
                jsonNode.put("max_amount", attribute.getMaxAmount());
            }
            if (attribute.getMinAmount() != null) {
                jsonNode.put("min_amount", attribute.getMinAmount());
            }
            if (attribute.getSuggestedAmount() != null) {
                jsonNode.put("suggested_amount", attribute.getSuggestedAmount());
            }
            
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            log.error("Erro ao converter MonthlyPremiumAmount para JSON: {}", e.getMessage());
            throw new RuntimeException("Erro ao converter MonthlyPremiumAmount para JSON", e);
        }
    }

    @Override
    public MonthlyPremiumAmount convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) return null;
            
            // Se o valor é um número, cria um MonthlyPremiumAmount apenas com o suggestedAmount
            if (dbData.matches("-?\\d+(\\.\\d+)?")) {
                return MonthlyPremiumAmount.builder()
                    .suggestedAmount(new BigDecimal(dbData))
                    .build();
            }
            
            // Tenta converter de JSON para MonthlyPremiumAmount
            JsonNode node = objectMapper.readTree(dbData);
            
            BigDecimal maxAmount = null;
            BigDecimal minAmount = null;
            BigDecimal suggestedAmount = null;
            
            if (node.has("max_amount")) {
                maxAmount = node.get("max_amount").isNull() ? null : new BigDecimal(node.get("max_amount").asText());
            }
            if (node.has("min_amount")) {
                minAmount = node.get("min_amount").isNull() ? null : new BigDecimal(node.get("min_amount").asText());
            }
            if (node.has("suggested_amount")) {
                suggestedAmount = node.get("suggested_amount").isNull() ? null : new BigDecimal(node.get("suggested_amount").asText());
            }
            
            return MonthlyPremiumAmount.builder()
                .maxAmount(maxAmount)
                .minAmount(minAmount)
                .suggestedAmount(suggestedAmount)
                .build();
                
        } catch (Exception e) {
            log.error("Erro ao converter JSON para MonthlyPremiumAmount: {}", e.getMessage());
            throw new RuntimeException("Erro ao converter JSON para MonthlyPremiumAmount", e);
        }
    }
} 