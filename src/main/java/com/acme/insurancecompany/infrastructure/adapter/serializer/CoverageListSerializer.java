package com.acme.insurancecompany.infrastructure.adapter.serializer;

import java.io.IOException;
import java.util.List;

import com.acme.insurancecompany.domain.model.Coverage;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CoverageListSerializer extends JsonSerializer<List<Coverage>> {
    @Override
    public void serialize(List<Coverage> coverages, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        gen.writeStartObject();
        for (Coverage coverage : coverages) {
            gen.writeNumberField(coverage.getName(), coverage.getAmount());
        }
        gen.writeEndObject();
    }
} 