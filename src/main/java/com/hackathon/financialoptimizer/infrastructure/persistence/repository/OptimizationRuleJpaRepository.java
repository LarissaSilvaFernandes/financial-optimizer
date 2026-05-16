package com.hackathon.financialoptimizer.infrastructure.persistence.repository;

import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.OptimizationRuleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OptimizationRuleJpaRepository extends JpaRepository<OptimizationRuleJpaEntity, UUID> {
    List<OptimizationRuleJpaEntity> findByActiveTrue();
    List<OptimizationRuleJpaEntity> findByRuleTypeAndActiveTrue(String ruleType);
}
