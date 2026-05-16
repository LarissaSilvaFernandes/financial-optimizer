package com.hackathon.financialoptimizer.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OptimizationResultResponse(
        UUID id,
        String strategyType,
        BigDecimal budgetConstraint,
        double totalRoiAchieved,
        BigDecimal totalSavingsIdentified,
        List<SelectedItemResponse> selectedItems,
        List<String> rulesApplied,
        String llmExplanation,
        LocalDateTime createdAt
) {}
