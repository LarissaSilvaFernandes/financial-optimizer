package com.hackathon.financialoptimizer.application.service.optimization;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.valueobject.OptimizationItem;
import com.hackathon.financialoptimizer.domain.valueobject.StrategyType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimizationContext {

    private final StrategyType strategyType;
    private final BigDecimal budgetConstraint;
    private final List<OptimizationItem> items;
    private final FinancialProfile financialProfile;

    private OptimizationContext(Builder builder) {
        this.strategyType = builder.strategyType;
        this.budgetConstraint = builder.budgetConstraint;
        this.items = Collections.unmodifiableList(builder.items);
        this.financialProfile = builder.financialProfile;
    }

    public StrategyType strategyType()       { return strategyType; }
    public BigDecimal budgetConstraint()     { return budgetConstraint; }
    public List<OptimizationItem> items()    { return items; }
    public FinancialProfile financialProfile() { return financialProfile; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private StrategyType strategyType;
        private BigDecimal budgetConstraint;
        private List<OptimizationItem> items = new ArrayList<>();
        private FinancialProfile financialProfile;

        public Builder strategyType(StrategyType strategyType) {
            this.strategyType = strategyType;
            return this;
        }

        public Builder budgetConstraint(BigDecimal budgetConstraint) {
            this.budgetConstraint = budgetConstraint;
            return this;
        }

        public Builder items(List<OptimizationItem> items) {
            this.items = new ArrayList<>(items);
            return this;
        }

        public Builder financialProfile(FinancialProfile financialProfile) {
            this.financialProfile = financialProfile;
            return this;
        }

        public OptimizationContext build() {
            if (strategyType == null)     throw new IllegalStateException("strategyType is required");
            if (budgetConstraint == null) throw new IllegalStateException("budgetConstraint is required");
            if (financialProfile == null) throw new IllegalStateException("financialProfile is required");
            return new OptimizationContext(this);
        }
    }
}
