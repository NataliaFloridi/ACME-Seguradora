package com.acme.insurancecompany.infrastructure.adapter.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.acme.insurancecompany.domain.model.Assistance;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class AssistanceListDeserializer extends JsonDeserializer<List<Assistance>> {

    @Override
    public List<Assistance> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        List<Assistance> assistances = new ArrayList<>();
        
        if (node.isArray()) {
            for (JsonNode item : node) {
                if (item.isTextual()) {
                    Assistance assistance = Assistance.builder()
                        .name(item.asText())
                        .build();
                    assistances.add(assistance);
                }
            }
        }
        
        return assistances;
    }
} 