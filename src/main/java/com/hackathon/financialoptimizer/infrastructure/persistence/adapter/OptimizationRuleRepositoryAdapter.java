package com.hackathon.financialoptimizer.infrastructure.persistence.adapter;

import com.hackathon.financialoptimizer.domain.entity.OptimizationRule;
import com.hackathon.financialoptimizer.domain.port.OptimizationRuleRepository;
import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.OptimizationRuleJpaEntity;
import com.hackathon.financialoptimizer.infrastructure.persistence.repository.OptimizationRuleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OptimizationRuleRepositoryAdapter implements OptimizationRuleRepository {

    private final OptimizationRuleJpaRepository jpaRepository;

    @Override
    public List<OptimizationRule> findAllActive() {
        return jpaRepository.findByActiveTrue().stream().map(this::toDomain).toList();
    }

    @Override
    public List<OptimizationRule> findByRuleType(String ruleType) {
        return jpaRepository.findByRuleTypeAndActiveTrue(ruleType).stream()
                .map(this::toDomain).toList();
    }

    private OptimizationRule toDomain(OptimizationRuleJpaEntity e) {
        return new OptimizationRule(e.getId(), e.getName(), e.getDescription(),
                e.getRuleType(), e.isActive(), e.getCreatedAt());
    }
}
