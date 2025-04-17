package com.acme.insurancecompany.infrastructure.adapter.serializer;

import java.io.IOException;
import java.util.List;

import com.acme.insurancecompany.domain.model.Assistance;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class AssistanceListSerializer extends JsonSerializer<List<Assistance>> {
    @Override
    public void serialize(List<Assistance> assistances, JsonGenerator gen, SerializerProvider serializers) 
            throws IOException {
        gen.writeStartArray();
        for (Assistance assistance : assistances) {
            gen.writeString(assistance.getName());
        }
        gen.writeEndArray();
    }
} 