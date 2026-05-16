package com.hackathon.financialoptimizer.domain.port;

import com.hackathon.financialoptimizer.domain.entity.Investment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestmentRepository {
    Investment save(Investment investment);
    Optional<Investment> findByIdAndUserId(UUID id, UUID userId);
    List<Investment> findByUserId(UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);
}
