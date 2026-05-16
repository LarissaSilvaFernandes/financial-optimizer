package com.hackathon.financialoptimizer.domain.entity;

import com.hackathon.financialoptimizer.domain.valueobject.StrategyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OptimizationResult {

    private UUID id;
    private UUID userId;
    private StrategyType strategyType;
    private BigDecimal budgetConstraint;
    private BigDecimal totalRoiAchieved;
    private BigDecimal totalSavingsIdentified;
    private List<SelectedItem> selectedItems;
    private List<String> rulesApplied;
    private String llmExplanation;
    private LocalDateTime createdAt;

    public record SelectedItem(
            String id,
            String name,
            String itemType,
            BigDecimal weight,
            double value,
            String category,
            String riskLevel
    ) {}

    public OptimizationResult(UUID id, UUID userId, StrategyType strategyType,
                               BigDecimal budgetConstraint, BigDecimal totalRoiAchieved,
                               BigDecimal totalSavingsIdentified, List<SelectedItem> selectedItems,
                               List<String> rulesApplied, String llmExplanation,
                               LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.strategyType = strategyType;
        this.budgetConstraint = budgetConstraint;
        this.totalRoiAchieved = totalRoiAchieved;
        this.totalSavingsIdentified = totalSavingsIdentified;
        this.selectedItems = selectedItems;
        this.rulesApplied = rulesApplied;
        this.llmExplanation = llmExplanation;
        this.createdAt = createdAt;
    }

    public static OptimizationResult create(UUID userId, StrategyType strategyType,
                                             BigDecimal budgetConstraint,
                                             BigDecimal totalRoiAchieved,
                                             BigDecimal totalSavingsIdentified,
                                             List<SelectedItem> selectedItems,
                                             List<String> rulesApplied) {
        return new OptimizationResult(UUID.randomUUID(), userId, strategyType,
                budgetConstraint, totalRoiAchieved, totalSavingsIdentified,
                selectedItems, rulesApplied, null, LocalDateTime.now());
    }

    public void setLlmExplanation(String explanation) {
        this.llmExplanation = explanation;
    }


    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public StrategyType getStrategyType() { return strategyType; }
    public BigDecimal getBudgetConstraint() { return budgetConstraint; }
    public BigDecimal getTotalRoiAchieved() { return totalRoiAchieved; }
    public BigDecimal getTotalSavingsIdentified() { return totalSavingsIdentified; }
    public List<SelectedItem> getSelectedItems() { return selectedItems; }
    public List<String> getRulesApplied() { return rulesApplied; }
    public String getLlmExplanation() { return llmExplanation; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Record-style accessors (used by use cases and response mapping)
    public UUID id() { return id; }
    public UUID userId() { return userId; }
    public StrategyType strategyType() { return strategyType; }
    public BigDecimal budgetConstraint() { return budgetConstraint; }
    public double totalRoiAchieved() { return totalRoiAchieved != null ? totalRoiAchieved.doubleValue() : 0.0; }
    public BigDecimal totalSavingsIdentified() { return totalSavingsIdentified; }
    public List<SelectedItem> selectedItems() { return selectedItems; }
    public List<String> rulesApplied() { return rulesApplied; }
    public String llmExplanation() { return llmExplanation; }
    public LocalDateTime createdAt() { return createdAt; }
}
