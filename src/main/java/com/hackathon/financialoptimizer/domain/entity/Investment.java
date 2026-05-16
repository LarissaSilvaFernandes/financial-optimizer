package com.hackathon.financialoptimizer.domain.entity;

import com.hackathon.financialoptimizer.domain.valueobject.InvestmentCategory;
import com.hackathon.financialoptimizer.domain.valueobject.RiskTolerance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Investment {

    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private BigDecimal requiredAmount;
    private BigDecimal expectedRoi;
    private RiskTolerance riskLevel;
    private int priorityScore;
    private InvestmentCategory category;
    private LocalDateTime createdAt;

    public Investment(UUID id, UUID userId, String name, String description,
                      BigDecimal requiredAmount, BigDecimal expectedRoi,
                      RiskTolerance riskLevel, int priorityScore,
                      InvestmentCategory category, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.requiredAmount = requiredAmount;
        this.expectedRoi = expectedRoi;
        this.riskLevel = riskLevel;
        this.priorityScore = priorityScore;
        this.category = category;
        this.createdAt = createdAt;
    }

    public static Investment create(UUID userId, String name, String description,
                                    BigDecimal requiredAmount, BigDecimal expectedRoi,
                                    RiskTolerance riskLevel, int priorityScore,
                                    InvestmentCategory category) {
        return new Investment(UUID.randomUUID(), userId, name, description, requiredAmount,
                expectedRoi, riskLevel, priorityScore, category, LocalDateTime.now());
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getRequiredAmount() { return requiredAmount; }
    public BigDecimal getExpectedRoi() { return expectedRoi; }
    public RiskTolerance getRiskLevel() { return riskLevel; }
    public int getPriorityScore() { return priorityScore; }
    public InvestmentCategory getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public UUID id() { return id; }
    public UUID userId() { return userId; }
    public String name() { return name; }
    public String description() { return description; }
    public BigDecimal requiredAmount() { return requiredAmount; }
    public BigDecimal expectedRoi() { return expectedRoi; }
    public RiskTolerance riskLevel() { return riskLevel; }
    public int priorityScore() { return priorityScore; }
    public InvestmentCategory category() { return category; }
    public LocalDateTime createdAt() { return createdAt; }
}
