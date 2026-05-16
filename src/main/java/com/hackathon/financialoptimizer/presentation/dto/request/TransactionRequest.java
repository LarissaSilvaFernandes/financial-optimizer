package com.hackathon.financialoptimizer.presentation.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotBlank String description,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank @Pattern(regexp = "HOUSING|FOOD|TRANSPORT|ENTERTAINMENT|HEALTH|EDUCATION|OTHER")
        String category,
        @NotBlank @Pattern(regexp = "EXPENSE|INCOME") String type,
        @NotNull LocalDate transactionDate,
        boolean essential,
        @DecimalMin("0") @DecimalMax("100") BigDecimal potentialReduction,
        @Min(1) @Max(10) int utilityScore
) {}
