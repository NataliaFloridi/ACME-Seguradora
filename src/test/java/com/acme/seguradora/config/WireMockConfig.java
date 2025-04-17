package com.acme.seguradora.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@TestConfiguration
@PropertySource("classpath:application-test.yml")
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireMockServer() {
        return new WireMockServer(WireMockConfiguration.options()
            .dynamicPort()
            .extensions(new com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer(true))
            .withRootDirectory("src/test/resources/wiremock"));
    }

    @Bean
    public String catalogServiceUrl(WireMockServer wireMockServer) {
        return "http://localhost:" + wireMockServer.port() + "/api/catalogo";
    }
} 