package com.acme.seguradora.infrastructure.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Component
public class CatalogServiceMock {
    
    private final WireMockServer wireMockServer;
    private final ObjectMapper objectMapper;
    
    public CatalogServiceMock() {
        this.wireMockServer = new WireMockServer(WireMockConfiguration.options()
            .port(8083)
            .extensions(new com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer(true))
            .withRootDirectory("src/main/resources/wiremock"));
        this.objectMapper = new ObjectMapper();
    }
    
    public void start() {
        this.wireMockServer.start();
        configureMappings();
    }
    
    public void stop() {
        this.wireMockServer.stop();
    }
    
    private void configureMappings() {
        try {
            JsonNode productJson = objectMapper.readTree(Files.readString(Paths.get("src/main/resources/wiremock/mappings/product.json")));
            this.wireMockServer.stubFor(get(urlEqualTo("/api/catalog/produtos/1b2da7cc-b367-4196-8a78-9cfeec21f587"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(productJson.get("response").get("jsonBody")))));

            JsonNode offersJson = objectMapper.readTree(Files.readString(Paths.get("src/main/resources/wiremock/mappings/offers.json")));
            this.wireMockServer.stubFor(get(urlEqualTo("/api/catalog/ofertas/adc56d77-348c-4bf0-908f-22d402ee715c"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(offersJson.get("response").get("jsonBody")))));
 
            JsonNode assistancesJson = objectMapper.readTree(Files.readString(Paths.get("src/main/resources/wiremock/mappings/assistances.json")));
            this.wireMockServer.stubFor(get(urlEqualTo("/api/catalog/assistencias"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(objectMapper.writeValueAsString(assistancesJson.get("response").get("jsonBody")))));
                    
            log.info("Mapeamentos configurados com sucesso");
        } catch (IOException e) {
            log.error("Erro ao configurar mapeamentos", e);
            throw new RuntimeException(e);
        }
    }
} 