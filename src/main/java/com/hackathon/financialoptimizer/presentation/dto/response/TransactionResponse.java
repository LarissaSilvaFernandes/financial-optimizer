package com.hackathon.financialoptimizer.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        String description,
        BigDecimal amount,
        String category,
        String categoryLabel,
        String type,
        LocalDate transactionDate,
        boolean essential,
        BigDecimal potentialReduction,
        BigDecimal potentialSavingAmount,
        int utilityScore,
        String source,
        LocalDateTime createdAt
) {}
