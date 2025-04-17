package com.acme.insurancecompany.infrastructure.adapter.out.persistence.adapter.insurancequote; 

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.acme.insurancecompany.application.mapper.InsuranceQuoteMapper;
import com.acme.insurancecompany.domain.exception.QuotePersistenceException;
import com.acme.insurancecompany.domain.exception.ResourceNotFoundException;
import com.acme.insurancecompany.domain.model.InsuranceQuote;
import com.acme.insurancecompany.domain.port.out.port.InsuranceQuotePort;
import com.acme.insurancecompany.infrastructure.adapter.out.persistence.entity.InsuranceQuoteEntity;
import com.acme.insurancecompany.infrastructure.adapter.out.persistence.repository.InsuranceQuoteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InsuranceQuotePersistenceAdapter implements InsuranceQuotePort {

    private final InsuranceQuoteRepository insuranceQuoteRepository;
    private final InsuranceQuoteMapper insuranceQuoteMapper;

    public InsuranceQuotePersistenceAdapter(InsuranceQuoteRepository insuranceQuoteRepository, InsuranceQuoteMapper insuranceQuoteMapper) {
        this.insuranceQuoteRepository = insuranceQuoteRepository;
        this.insuranceQuoteMapper = insuranceQuoteMapper;
    }

    @Override
    public InsuranceQuote saveInsuranceQuote(InsuranceQuote insuranceQuote) {
        try {
            InsuranceQuoteEntity entity = insuranceQuoteMapper.toEntity(insuranceQuote);
            InsuranceQuoteEntity savedEntity = insuranceQuoteRepository.save(entity);
            return insuranceQuoteMapper.toDomain(savedEntity);
        } catch (DataAccessException e) {
            log.error("Falha técnica ao salvar cotação: {}", e.getLocalizedMessage());
            throw new QuotePersistenceException("Não foi possível salvar a cotação", e);
        } catch (EntityNotFoundException ex) {
            log.error("Recurso não encontrado ao salvar cotação: {}", ex.getMessage());
            throw new ResourceNotFoundException("Recurso não encontrado", ex);
        }
    }
    
    @Override
    public InsuranceQuote findInsuranceQuoteById(Long insuranceQuoteId) {
        try {
            InsuranceQuoteEntity entity = insuranceQuoteRepository.findById(insuranceQuoteId)
                .orElseThrow(() -> {
                    log.error("Cotação não encontrada para o ID: {}", insuranceQuoteId);
                    return new ResourceNotFoundException("Cotação não encontrada");
                });
            return insuranceQuoteMapper.toDomain(entity);
        } catch (DataAccessException e) {
            log.error("Falha técnica ao buscar cotação: {}", e.getLocalizedMessage());
            throw new QuotePersistenceException("Não foi possível buscar a cotação", e);
        }
    }
    @Override
    public InsuranceQuote updateInsuranceQuoteWithPolicyId(Long insuranceQuoteId, Long policyId) {
        try {
            InsuranceQuoteEntity entity = insuranceQuoteRepository.findById(insuranceQuoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cotação não encontrada"));

            entity.setInsurancePolicyId(policyId);
            entity.setStatus("POLICY_CREATED");

            InsuranceQuoteEntity updatedEntity = insuranceQuoteRepository.save(entity);
            return insuranceQuoteMapper.toDomain(updatedEntity);
        } catch (DataAccessException e) {
            log.error("Falha técnica ao atualizar cotação: {}", e.getLocalizedMessage());
            throw new QuotePersistenceException("Não foi possível atualizar a cotação", e);
        } 
    }
} 