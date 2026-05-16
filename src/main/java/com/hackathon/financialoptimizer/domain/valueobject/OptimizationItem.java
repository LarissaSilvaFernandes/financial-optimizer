package com.hackathon.financialoptimizer.domain.valueobject;

import java.math.BigDecimal;
import java.util.UUID;


public sealed interface OptimizationItem
        permits OptimizationItem.ExpenseItem, OptimizationItem.InvestmentItem {

    UUID id();
    String name();
    BigDecimal weight();
    double value();

    record ExpenseItem(
            UUID id,
            String name,
            BigDecimal weight,
            double value,
            TransactionCategory category,
            boolean essential
    ) implements OptimizationItem {}

    record InvestmentItem(
            UUID id,
            String name,
            BigDecimal weight,
            double value,
            RiskTolerance riskLevel,
            InvestmentCategory category
    ) implements OptimizationItem {}

    default String itemType() {
        return switch (this) {
            case ExpenseItem    ignored -> "EXPENSE";
            case InvestmentItem ignored -> "INVESTMENT";
        };
    }
}
