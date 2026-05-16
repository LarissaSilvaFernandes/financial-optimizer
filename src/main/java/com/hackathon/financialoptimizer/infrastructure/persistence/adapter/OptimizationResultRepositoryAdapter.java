package com.hackathon.financialoptimizer.infrastructure.persistence.adapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.financialoptimizer.domain.entity.OptimizationResult;
import com.hackathon.financialoptimizer.domain.port.OptimizationResultRepository;
import com.hackathon.financialoptimizer.domain.valueobject.StrategyType;
import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.OptimizationResultJpaEntity;
import com.hackathon.financialoptimizer.infrastructure.persistence.repository.OptimizationResultJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OptimizationResultRepositoryAdapter implements OptimizationResultRepository {

    private final OptimizationResultJpaRepository jpaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public OptimizationResult save(OptimizationResult result) {
        return toDomain(jpaRepository.save(toEntity(result)));
    }

    @Override
    public Optional<OptimizationResult> findByIdAndUserId(UUID id, UUID userId) {
        return jpaRepository.findByIdAndUserId(id, userId).map(this::toDomain);
    }

    @Override
    public List<OptimizationResult> findByUserId(UUID userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDomain).toList();
    }

    @SneakyThrows
    private OptimizationResultJpaEntity toEntity(OptimizationResult r) {
        return OptimizationResultJpaEntity.builder()
                .id(r.getId())
                .userId(r.getUserId())
                .strategyType(r.getStrategyType().name())
                .budgetConstraint(r.getBudgetConstraint())
                .totalRoiAchieved(r.getTotalRoiAchieved())
                .totalSavingsIdentified(r.getTotalSavingsIdentified())
                .selectedItemsJson(objectMapper.writeValueAsString(r.getSelectedItems()))
                .rulesAppliedJson(objectMapper.writeValueAsString(r.getRulesApplied()))
                .llmExplanation(r.getLlmExplanation())
                .createdAt(r.getCreatedAt())
                .build();
    }

    @SneakyThrows
    private OptimizationResult toDomain(OptimizationResultJpaEntity e) {
        List<OptimizationResult.SelectedItem> items = objectMapper.readValue(
                e.getSelectedItemsJson(),
                new TypeReference<List<OptimizationResult.SelectedItem>>() {});

        List<String> rules = objectMapper.readValue(
                e.getRulesAppliedJson(),
                new TypeReference<List<String>>() {});

        return new OptimizationResult(
                e.getId(), e.getUserId(),
                StrategyType.valueOf(e.getStrategyType()),
                e.getBudgetConstraint(), e.getTotalRoiAchieved(),
                e.getTotalSavingsIdentified(), items, rules,
                e.getLlmExplanation(), e.getCreatedAt());
    }
}
