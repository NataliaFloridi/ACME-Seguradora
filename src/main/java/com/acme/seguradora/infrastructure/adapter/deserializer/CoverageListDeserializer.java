package com.acme.seguradora.infrastructure.adapter.deserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.acme.seguradora.domain.model.Coverage;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class CoverageListDeserializer extends JsonDeserializer<List<Coverage>> {

    @Override
    public List<Coverage> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        List<Coverage> coverages = new ArrayList<>();
        
        if (node.isObject()) {
            Iterator<Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> field = fields.next();
                Coverage coverage = Coverage.builder()
                    .name(field.getKey())
                    .amount(BigDecimal.valueOf(field.getValue().asDouble()))
                    .build();
                coverages.add(coverage);
            }
        }
        
        return coverages;
    }
} 