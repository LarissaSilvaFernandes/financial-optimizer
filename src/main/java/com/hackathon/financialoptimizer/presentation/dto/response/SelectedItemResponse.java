package com.hackathon.financialoptimizer.presentation.dto.response;

import java.math.BigDecimal;

public record SelectedItemResponse(
        String id,
        String name,
        String itemType,
        BigDecimal weight,
        double value,
        String category,
        String riskLevel
) {}
