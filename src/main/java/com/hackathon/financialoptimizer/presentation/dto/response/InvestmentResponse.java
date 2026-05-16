package com.hackathon.financialoptimizer.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InvestmentResponse(
        UUID id,
        String name,
        String description,
        BigDecimal requiredAmount,
        BigDecimal expectedRoi,
        String riskLevel,
        int priorityScore,
        String category,
        String categoryLabel,
        LocalDateTime createdAt
) {}
