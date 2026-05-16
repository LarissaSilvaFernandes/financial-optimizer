package com.hackathon.financialoptimizer.presentation.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record FinancialProfileRequest(
        @NotNull @DecimalMin("0.01") BigDecimal monthlyIncome,
        @NotNull @DecimalMin("0.01") BigDecimal totalBudget,
        BigDecimal savingsGoal,
        @NotBlank @Pattern(regexp = "LOW|MEDIUM|HIGH") String riskTolerance
) {}
