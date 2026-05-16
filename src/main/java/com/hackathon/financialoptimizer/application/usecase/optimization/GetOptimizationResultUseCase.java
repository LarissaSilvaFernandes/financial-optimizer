package com.hackathon.financialoptimizer.application.usecase.optimization;

import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.OptimizationResultRepository;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.response.OptimizationResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetOptimizationResultUseCase {

    private final OptimizationResultRepository optimizationResultRepository;
    private final SecurityUtils securityUtils;

    public List<OptimizationResultResponse> listAll() {
        UUID userId = securityUtils.getCurrentUserId();
        return optimizationResultRepository.findByUserId(userId).stream()
                .map(RunOptimizationUseCase::toResponse)
                .toList();
    }

    public OptimizationResultResponse getById(UUID id) {
        UUID userId = securityUtils.getCurrentUserId();
        return optimizationResultRepository.findByIdAndUserId(id, userId)
                .map(RunOptimizationUseCase::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("OptimizationResult", id));
    }
}
