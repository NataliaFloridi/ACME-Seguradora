package com.acme.seguradora.infrastructure.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

@Component
public class CatalogMockServer {
    
    private WireMockServer wireMockServer;
    
    public CatalogMockServer(ObjectMapper objectMapper) {
        this.wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        this.wireMockServer.start();
        
        configureStubs();
    }
    
    public void stop() {
        this.wireMockServer.stop();
    }
    
    public int getPort() {
        return this.wireMockServer.port();
    }
    
    private void configureStubs() {
        wireMockServer.stubFor(get(urlEqualTo("/products/1b2da7cc-b367-4196-8a78-9cfeec21f587"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
                        "name": "Seguro de Vida",
                        "created_at": "2021-07-01T00:00:00Z",
                        "active": true,
                        "offers": [
                            "adc56d77-348c-4bf0-908f-22d402ee715c",
                            "bdc56d77-348c-4bf0-908f-22d402ee715c",
                            "cdc56d77-348c-4bf0-908f-22d402ee715c"
                        ]
                    }
                """)));
        
        wireMockServer.stubFor(get(urlEqualTo("/offers/adc56d77-348c-4bf0-908f-22d402ee715c"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "id": "adc56d77-348c-4bf0-908f-22d402ee715c",
                        "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
                        "name": "Seguro de Vida Familiar",
                        "created_at": "2021-07-01T00:00:00Z",
                        "active": true,
                        "coverages": {
                            "Incêndio": 500000.00,
                            "Desastres naturais": 600000.00,
                            "Responsabiliadade civil": 80000.00,
                            "Roubo": 100000.00
                        },
                        "assistances": [
                            "Encanador",
                            "Eletricista",
                            "Chaveiro 24h",
                            "Assistência Funerária"
                        ],
                        "monthly_premium_amount": {
                            "max_amount": 100.74,
                            "min_amount": 50.00,
                            "suggested_amount": 60.25
                        }
                    }
                """)));
    }
} 