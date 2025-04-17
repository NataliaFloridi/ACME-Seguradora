package com.acme.insurancecompany.infrastructure.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.acme.insurancecompany.application.dto.InsuranceQuoteRequest;
import com.acme.insurancecompany.application.dto.InsuranceQuoteResponse;
import com.acme.insurancecompany.application.mapper.InsuranceQuoteMapper;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.CreateInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.port.in.usecase.insurancequote.FindInsuranceQuoteUseCase;
import com.acme.insurancecompany.domain.validator.InsuranceQuoteValidator;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/insurance-quotes")
public class InsuranceQuoteController {
                                                                                                                        
    private final CreateInsuranceQuoteUseCase createInsuranceQuoteUseCase;
    private final FindInsuranceQuoteUseCase findInsuranceQuoteUseCase;
    private final InsuranceQuoteMapper insuranceQuoteMapper;
    private final InsuranceQuoteValidator insuranceQuoteValidator;

    @PostMapping
    public ResponseEntity<InsuranceQuoteResponse> createInsuranceQuote(@Valid @RequestBody InsuranceQuoteRequest request) {
        try {
            insuranceQuoteValidator.validate(request);
            var insuranceQuote = insuranceQuoteMapper.toDomain(request);
            var created = createInsuranceQuoteUseCase.execute(insuranceQuote);
            var response = insuranceQuoteMapper.toResponse(created);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao criar cotação: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{quoteId}")
    public ResponseEntity<InsuranceQuoteResponse> findInsuranceQuoteById(@PathVariable("quoteId") Long quoteId) {
        try {
            var insuranceQuote = findInsuranceQuoteUseCase.execute(quoteId);
            var response = insuranceQuoteMapper.toResponse(insuranceQuote);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao buscar cotação com ID {}: {}", quoteId, e.getMessage(), e);
            throw e;
        }
    }
} 