package com.hackathon.financialoptimizer.domain.entity;

import com.hackathon.financialoptimizer.domain.valueobject.RiskTolerance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class FinancialProfile {

    private UUID id;
    private UUID userId;
    private BigDecimal monthlyIncome;
    private BigDecimal totalBudget;
    private BigDecimal savingsGoal;
    private RiskTolerance riskTolerance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FinancialProfile(UUID id, UUID userId, BigDecimal monthlyIncome,
                            BigDecimal totalBudget, BigDecimal savingsGoal,
                            RiskTolerance riskTolerance, LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.monthlyIncome = monthlyIncome;
        this.totalBudget = totalBudget;
        this.savingsGoal = savingsGoal;
        this.riskTolerance = riskTolerance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FinancialProfile create(UUID userId, BigDecimal monthlyIncome,
                                          BigDecimal totalBudget, BigDecimal savingsGoal,
                                          RiskTolerance riskTolerance) {
        LocalDateTime now = LocalDateTime.now();
        return new FinancialProfile(UUID.randomUUID(), userId, monthlyIncome,
                totalBudget, savingsGoal, riskTolerance, now, now);
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public BigDecimal getSavingsGoal() { return savingsGoal; }
    public RiskTolerance getRiskTolerance() { return riskTolerance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Record-style accessors
    public UUID id() { return id; }
    public UUID userId() { return userId; }
    public BigDecimal monthlyIncome() { return monthlyIncome; }
    public BigDecimal totalBudget() { return totalBudget; }
    public BigDecimal savingsGoal() { return savingsGoal; }
    public RiskTolerance riskTolerance() { return riskTolerance; }
    public LocalDateTime createdAt() { return createdAt; }
    public LocalDateTime updatedAt() { return updatedAt; }

    public void update(BigDecimal monthlyIncome, BigDecimal totalBudget,
                       BigDecimal savingsGoal, RiskTolerance riskTolerance) {
        this.monthlyIncome = monthlyIncome;
        this.totalBudget = totalBudget;
        this.savingsGoal = savingsGoal;
        this.riskTolerance = riskTolerance;
        this.updatedAt = LocalDateTime.now();
    }
}
