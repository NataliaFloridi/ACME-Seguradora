package com.acme.insurancecompany.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acme.insurancecompany.infrastructure.adapter.out.persistence.entity.InsuranceQuoteEntity;

@Repository
public interface InsuranceQuoteRepository extends JpaRepository<InsuranceQuoteEntity, Long> {
}