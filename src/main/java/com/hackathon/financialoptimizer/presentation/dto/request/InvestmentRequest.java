package com.hackathon.financialoptimizer.presentation.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record InvestmentRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal requiredAmount,
        @NotNull @DecimalMin("0.01") @DecimalMax("9999.99") BigDecimal expectedRoi,
        @NotBlank @Pattern(regexp = "LOW|MEDIUM|HIGH") String riskLevel,
        @Min(1) @Max(10) int priorityScore,
        @NotBlank @Pattern(regexp = "STOCKS|FIXED_INCOME|REAL_ESTATE|CRYPTO|SAVINGS")
        String category
) {}
