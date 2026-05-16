package com.hackathon.financialoptimizer.domain.entity;

import com.hackathon.financialoptimizer.domain.valueobject.TransactionCategory;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private UUID userId;
    private String description;
    private BigDecimal amount;
    private TransactionCategory category;
    private TransactionType type;
    private LocalDate transactionDate;
    private boolean essential;
    private BigDecimal potentialReduction;
    private int utilityScore;
    private String source;
    private LocalDateTime createdAt;

    public Transaction(UUID id, UUID userId, String description, BigDecimal amount,
                       TransactionCategory category, TransactionType type,
                       LocalDate transactionDate, boolean essential,
                       BigDecimal potentialReduction, int utilityScore,
                       String source, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.transactionDate = transactionDate;
        this.essential = essential;
        this.potentialReduction = potentialReduction;
        this.utilityScore = utilityScore;
        this.source = source;
        this.createdAt = createdAt;
    }

    public static Transaction create(UUID userId, String description, BigDecimal amount,
                                     TransactionCategory category, TransactionType type,
                                     LocalDate transactionDate, boolean essential,
                                     BigDecimal potentialReduction, int utilityScore,
                                     String source) {
        return new Transaction(UUID.randomUUID(), userId, description, amount, category,
                type, transactionDate, essential, potentialReduction, utilityScore, source,
                LocalDateTime.now());
    }

    public BigDecimal potentialSavingAmount() {
        if (potentialReduction == null || potentialReduction.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(potentialReduction).divide(BigDecimal.valueOf(100));
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public TransactionCategory getCategory() { return category; }
    public TransactionType getType() { return type; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public boolean isEssential() { return essential; }
    public BigDecimal getPotentialReduction() { return potentialReduction; }
    public int getUtilityScore() { return utilityScore; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public UUID id() { return id; }
    public UUID userId() { return userId; }
    public String description() { return description; }
    public BigDecimal amount() { return amount; }
    public TransactionCategory category() { return category; }
    public TransactionType type() { return type; }
    public LocalDate transactionDate() { return transactionDate; }
    public boolean essential() { return essential; }
    public BigDecimal potentialReduction() { return potentialReduction; }
    public int utilityScore() { return utilityScore; }
    public String source() { return source; }
    public LocalDateTime createdAt() { return createdAt; }
}
