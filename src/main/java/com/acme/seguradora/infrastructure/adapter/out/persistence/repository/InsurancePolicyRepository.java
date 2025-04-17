package com.acme.seguradora.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acme.seguradora.infrastructure.adapter.out.persistence.entity.InsurancePolicyEntity;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicyEntity, Long> {
    InsurancePolicyEntity findByQuoteId(String quoteId);
} 