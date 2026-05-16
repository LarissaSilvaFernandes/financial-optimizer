package com.hackathon.financialoptimizer.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FinancialProfileResponse(
        UUID id,
        BigDecimal monthlyIncome,
        BigDecimal totalBudget,
        BigDecimal savingsGoal,
        String riskTolerance,
        LocalDateTime updatedAt
) {}
