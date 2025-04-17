package com.acme.insurancecompany.infrastructure.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Configuration
public class LoggingConfig {

    @Bean
    public OncePerRequestFilter loggingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                
                // Gera um ID único para o trace
                String traceId = UUID.randomUUID().toString();
                MDC.put("traceId", traceId);
                
                // Wraps request e response para permitir múltiplas leituras
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
                
                try {
                    filterChain.doFilter(requestWrapper, responseWrapper);
                    
                    // Log da requisição
                    logRequest(requestWrapper);
                    
                    // Log da resposta
                    logResponse(responseWrapper);
                    
                    // Copia a resposta para o response original
                    responseWrapper.copyBodyToResponse();
                } finally {
                    MDC.clear();
                }
            }
            
            private void logRequest(ContentCachingRequestWrapper request) {
                String method = request.getMethod();
                String uri = request.getRequestURI();
                String queryString = request.getQueryString();
                String body = new String(request.getContentAsByteArray());
                
                MDC.put("method", method);
                MDC.put("uri", uri);
                MDC.put("query", queryString);
                
                // Log estruturado em JSON
                System.out.printf("""
                    {
                        "timestamp": "%s",
                        "level": "INFO",
                        "service": "acme-seguradora",
                        "traceId": "%s",
                        "message": "Requisição recebida",
                        "method": "%s",
                        "uri": "%s",
                        "query": "%s",
                        "body": %s
                    }
                    """, 
                    java.time.Instant.now(),
                    MDC.get("traceId"),
                    method,
                    uri,
                    queryString,
                    body
                );
            }
            
            private void logResponse(ContentCachingResponseWrapper response) {
                int status = response.getStatus();
                String body = new String(response.getContentAsByteArray());
                
                // Log estruturado em JSON
                System.out.printf("""
                    {
                        "timestamp": "%s",
                        "level": "INFO",
                        "service": "acme-seguradora",
                        "traceId": "%s",
                        "message": "Resposta enviada",
                        "status": %d,
                        "body": %s
                    }
                    """, 
                    java.time.Instant.now(),
                    MDC.get("traceId"),
                    status,
                    body
                );
            }
        };
    }
} 