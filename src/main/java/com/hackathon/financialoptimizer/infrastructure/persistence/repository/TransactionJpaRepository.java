package com.hackathon.financialoptimizer.infrastructure.persistence.repository;

import com.hackathon.financialoptimizer.infrastructure.persistence.jpa.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    List<TransactionJpaEntity> findByUserId(UUID userId);
    List<TransactionJpaEntity> findByUserIdAndType(UUID userId, String type);
    List<TransactionJpaEntity> findByUserIdAndCategory(UUID userId, String category);
    Optional<TransactionJpaEntity> findByIdAndUserId(UUID id, UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);
    List<TransactionJpaEntity> findByUserIdAndTypeAndEssentialFalse(UUID userId, String type);
}
