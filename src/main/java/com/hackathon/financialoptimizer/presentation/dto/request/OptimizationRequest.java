package com.hackathon.financialoptimizer.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record OptimizationRequest(
        @NotBlank(message = "strategyType is required")
        @Pattern(regexp = "MAXIMIZE_ROI|MINIMIZE_RISK|BALANCED",
                 message = "strategyType must be MAXIMIZE_ROI, MINIMIZE_RISK or BALANCED")
        String strategyType,

        @NotNull(message = "budgetConstraint is required")
        @DecimalMin(value = "0.01", message = "budgetConstraint must be positive")
        BigDecimal budgetConstraint
) {}
