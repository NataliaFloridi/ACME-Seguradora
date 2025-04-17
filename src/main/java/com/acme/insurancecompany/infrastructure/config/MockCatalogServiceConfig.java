package com.acme.insurancecompany.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.acme.insurancecompany.infrastructure.adapter.out.client.CatalogClient;
import com.acme.insurancecompany.infrastructure.mock.CatalogServiceMock;

import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;

@Configuration
@Profile("test")
public class MockCatalogServiceConfig {

    @Bean
    public CatalogServiceMock catalogServiceMock() {
        return new CatalogServiceMock();
    }

    @Bean(name = "mockCatalogServiceClient")
    public CatalogClient mockCatalogServiceClient(ApplicationContext context, CatalogServiceMock mock) {
        mock.start();
        return new FeignClientBuilder(context)
            .forType(CatalogClient.class, "catalog-service")
            .url("http://localhost:8083")
            .build();
    }
} 