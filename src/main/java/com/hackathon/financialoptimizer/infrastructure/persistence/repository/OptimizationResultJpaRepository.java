package com.hackathon.financialoptimizer.infrastructure.persistence.repository;

import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.OptimizationResultJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OptimizationResultJpaRepository extends JpaRepository<OptimizationResultJpaEntity, UUID> {
    List<OptimizationResultJpaEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<OptimizationResultJpaEntity> findByIdAndUserId(UUID id, UUID userId);
}
