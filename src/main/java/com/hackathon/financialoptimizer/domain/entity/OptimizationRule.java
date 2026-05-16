package com.hackathon.financialoptimizer.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class OptimizationRule {

    private UUID id;
    private String name;
    private String description;
    private String ruleType;
    private boolean active;
    private LocalDateTime createdAt;

    public OptimizationRule(UUID id, String name, String description,
                            String ruleType, boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ruleType = ruleType;
        this.active = active;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getRuleType() { return ruleType; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Record-style accessors
    public UUID id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public String ruleType() { return ruleType; }
    public boolean active() { return active; }
    public LocalDateTime createdAt() { return createdAt; }
}
