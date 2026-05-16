package com.hackathon.financialoptimizer.infrastructure.persistence.repository;

import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.InvestmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestmentJpaRepository extends JpaRepository<InvestmentJpaEntity, UUID> {
    List<InvestmentJpaEntity> findByUserId(UUID userId);
    Optional<InvestmentJpaEntity> findByIdAndUserId(UUID id, UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);
}
