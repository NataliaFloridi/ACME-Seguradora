package com.acme.seguradora.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return io.micrometer.core.instrument.Metrics.globalRegistry;
    }

    @Bean
    public io.micrometer.core.instrument.Counter cotacoesCriadasCounter(MeterRegistry registry) {
        return registry.counter("cotacoes.criadas.total");
    }

    @Bean
    public io.micrometer.core.instrument.Counter cotacoesConsultadasCounter(MeterRegistry registry) {
        return registry.counter("cotacoes.consultadas.total");
    }

    @Bean
    public io.micrometer.core.instrument.Counter cotacoesInvalidasCounter(MeterRegistry registry) {
        return registry.counter("cotacoes.invalidas.total");
    }

    @Bean
    public io.micrometer.core.instrument.Timer cotacaoProcessamentoTimer(MeterRegistry registry) {
        return registry.timer("cotacao.processamento.tempo");
    }

    @Bean
    public io.micrometer.core.instrument.Gauge cotacoesPendentesGauge(MeterRegistry registry) {
        return io.micrometer.core.instrument.Gauge.builder("cotacoes.pendentes.total", 0, value -> value)
                .register(registry);
    }

    @Bean
    public io.micrometer.core.instrument.Gauge cotacoesEmitidasGauge(MeterRegistry registry) {
        return io.micrometer.core.instrument.Gauge.builder("cotacoes.emitidas.total", 0, value -> value)
                .register(registry);
    }
}