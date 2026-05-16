package com.hackathon.financialoptimizer.domain.port;

import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionCategory;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);
    List<Transaction> findByUserId(UUID userId);
    List<Transaction> findByUserIdAndType(UUID userId, TransactionType type);
    List<Transaction> findByUserIdAndCategory(UUID userId, TransactionCategory category);
    void deleteByIdAndUserId(UUID id, UUID userId);
    List<Transaction> findExpensesByUserId(UUID userId);
}
