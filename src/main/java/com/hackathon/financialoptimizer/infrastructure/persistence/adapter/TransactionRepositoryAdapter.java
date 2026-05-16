package com.hackathon.financialoptimizer.infrastructure.persistence.adapter;

import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.port.TransactionRepository;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionCategory;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionType;
import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.TransactionJpaEntity;
import com.hackathon.financialoptimizer.infrastructure.persistence.repository.TransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    @Override
    public Transaction save(Transaction t) {
        return toDomain(jpaRepository.save(toEntity(t)));
    }

    @Override
    public Optional<Transaction> findByIdAndUserId(UUID id, UUID userId) {
        return jpaRepository.findByIdAndUserId(id, userId).map(this::toDomain);
    }

    @Override
    public List<Transaction> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findByUserIdAndType(UUID userId, TransactionType type) {
        return jpaRepository.findByUserIdAndType(userId, type.name()).stream()
                .map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findByUserIdAndCategory(UUID userId, TransactionCategory category) {
        return jpaRepository.findByUserIdAndCategory(userId, category.name()).stream()
                .map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteByIdAndUserId(UUID id, UUID userId) {
        jpaRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public List<Transaction> findExpensesByUserId(UUID userId) {
        return jpaRepository.findByUserIdAndType(userId, "EXPENSE").stream()
                .map(this::toDomain).toList();
    }

    private TransactionJpaEntity toEntity(Transaction t) {
        return TransactionJpaEntity.builder()
                .id(t.getId())
                .userId(t.getUserId())
                .description(t.getDescription())
                .amount(t.getAmount())
                .category(t.getCategory().name())
                .type(t.getType().name())
                .transactionDate(t.getTransactionDate())
                .essential(t.isEssential())
                .potentialReduction(t.getPotentialReduction())
                .utilityScore(t.getUtilityScore())
                .source(t.getSource())
                .createdAt(t.getCreatedAt())
                .build();
    }

    private Transaction toDomain(TransactionJpaEntity e) {
        return new Transaction(e.getId(), e.getUserId(), e.getDescription(), e.getAmount(),
                TransactionCategory.valueOf(e.getCategory()),
                TransactionType.valueOf(e.getType()),
                e.getTransactionDate(), e.isEssential(),
                e.getPotentialReduction(),
                e.getUtilityScore() != null ? e.getUtilityScore() : 5,
                e.getSource(), e.getCreatedAt());
    }
}
