package com.hackathon.financialoptimizer.infrastructure.persistence.repository;

import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.FinancialProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FinancialProfileJpaRepository extends JpaRepository<FinancialProfileJpaEntity, UUID> {
    Optional<FinancialProfileJpaEntity> findByUserId(UUID userId);
}
