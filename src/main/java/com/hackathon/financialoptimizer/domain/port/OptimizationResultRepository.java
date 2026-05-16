package com.hackathon.financialoptimizer.domain.port;

import com.hackathon.financialoptimizer.domain.entity.OptimizationResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OptimizationResultRepository {
    OptimizationResult save(OptimizationResult result);
    Optional<OptimizationResult> findByIdAndUserId(UUID id, UUID userId);
    List<OptimizationResult> findByUserId(UUID userId);
}
