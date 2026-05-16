package com.hackathon.financialoptimizer.infrastructure.persistence.adapter;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.port.FinancialProfileRepository;
import com.hackathon.financialoptimizer.domain.valueobject.RiskTolerance;
import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.FinancialProfileJpaEntity;
import com.hackathon.financialoptimizer.infrastructure.persistence.repository.FinancialProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FinancialProfileRepositoryAdapter implements FinancialProfileRepository {

    private final FinancialProfileJpaRepository jpaRepository;

    @Override
    public FinancialProfile save(FinancialProfile profile) {
        FinancialProfileJpaEntity entity = toEntity(profile);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<FinancialProfile> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(this::toDomain);
    }

    private FinancialProfileJpaEntity toEntity(FinancialProfile p) {
        return FinancialProfileJpaEntity.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .monthlyIncome(p.getMonthlyIncome())
                .totalBudget(p.getTotalBudget())
                .savingsGoal(p.getSavingsGoal())
                .riskTolerance(p.getRiskTolerance().label())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private FinancialProfile toDomain(FinancialProfileJpaEntity e) {
        return new FinancialProfile(e.getId(), e.getUserId(), e.getMonthlyIncome(),
                e.getTotalBudget(), e.getSavingsGoal(),
                RiskTolerance.from(e.getRiskTolerance()),
                e.getCreatedAt(), e.getUpdatedAt());
    }
}
