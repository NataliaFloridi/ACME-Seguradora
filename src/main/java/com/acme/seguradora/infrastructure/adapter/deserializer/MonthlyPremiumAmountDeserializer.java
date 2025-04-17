package com.acme.seguradora.infrastructure.adapter.deserializer;

import java.io.IOException;
import java.math.BigDecimal;

import com.acme.seguradora.domain.model.MonthlyPremiumAmount;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class MonthlyPremiumAmountDeserializer extends JsonDeserializer<MonthlyPremiumAmount> {

    @Override
    public MonthlyPremiumAmount deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        if (node.isNumber()) {
            return MonthlyPremiumAmount.builder()
                .suggestedAmount(BigDecimal.valueOf(node.asDouble()))
                .build();
        }
        
        if (node.isObject()) {
            BigDecimal maxAmount = node.has("max_amount") ? 
                BigDecimal.valueOf(node.get("max_amount").asDouble()) : null;
            BigDecimal minAmount = node.has("min_amount") ? 
                BigDecimal.valueOf(node.get("min_amount").asDouble()) : null;
            BigDecimal suggestedAmount = node.has("suggested_amount") ? 
                BigDecimal.valueOf(node.get("suggested_amount").asDouble()) : null;
                
            return MonthlyPremiumAmount.builder()
                .maxAmount(maxAmount)
                .minAmount(minAmount)
                .suggestedAmount(suggestedAmount)
                .build();
        }
        
        return null;
    }
} 