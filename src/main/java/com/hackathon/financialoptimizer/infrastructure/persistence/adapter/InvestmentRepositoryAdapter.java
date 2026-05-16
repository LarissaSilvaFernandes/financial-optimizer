package com.hackathon.financialoptimizer.infrastructure.persistence.adapter;

import com.hackathon.financialoptimizer.domain.entity.Investment;
import com.hackathon.financialoptimizer.domain.port.InvestmentRepository;
import com.hackathon.financialoptimizer.domain.valueobject.InvestmentCategory;
import com.hackathon.financialoptimizer.domain.valueobject.RiskTolerance;
import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.InvestmentJpaEntity;
import com.hackathon.financialoptimizer.infrastructure.persistence.repository.InvestmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InvestmentRepositoryAdapter implements InvestmentRepository {

    private final InvestmentJpaRepository jpaRepository;

    @Override
    public Investment save(Investment investment) {
        return toDomain(jpaRepository.save(toEntity(investment)));
    }

    @Override
    public Optional<Investment> findByIdAndUserId(UUID id, UUID userId) {
        return jpaRepository.findByIdAndUserId(id, userId).map(this::toDomain);
    }

    @Override
    public List<Investment> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteByIdAndUserId(UUID id, UUID userId) {
        jpaRepository.deleteByIdAndUserId(id, userId);
    }

    private InvestmentJpaEntity toEntity(Investment i) {
        return InvestmentJpaEntity.builder()
                .id(i.getId())
                .userId(i.getUserId())
                .name(i.getName())
                .description(i.getDescription())
                .requiredAmount(i.getRequiredAmount())
                .expectedRoi(i.getExpectedRoi())
                .riskLevel(i.getRiskLevel().label())
                .priorityScore(i.getPriorityScore())
                .category(i.getCategory().name())
                .createdAt(i.getCreatedAt())
                .build();
    }

    private Investment toDomain(InvestmentJpaEntity e) {
        return new Investment(e.getId(), e.getUserId(), e.getName(), e.getDescription(),
                e.getRequiredAmount(), e.getExpectedRoi(),
                RiskTolerance.from(e.getRiskLevel()),
                e.getPriorityScore() != null ? e.getPriorityScore() : 5,
                InvestmentCategory.valueOf(e.getCategory()),
                e.getCreatedAt());
    }
}
