package com.hackathon.financialoptimizer.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "optimization_results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OptimizationResultJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "strategy_type", nullable = false, length = 50)
    private String strategyType;

    @Column(name = "budget_constraint", nullable = false, precision = 15, scale = 2)
    private BigDecimal budgetConstraint;

    @Column(name = "total_roi_achieved", precision = 5, scale = 2)
    private BigDecimal totalRoiAchieved;

    @Column(name = "total_savings_identified", precision = 15, scale = 2)
    private BigDecimal totalSavingsIdentified;

    @Column(name = "selected_items", columnDefinition = "TEXT")
    private String selectedItemsJson;

    @Column(name = "rules_applied", columnDefinition = "TEXT")
    private String rulesAppliedJson;

    @Column(name = "llm_explanation", columnDefinition = "TEXT")
    private String llmExplanation;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
