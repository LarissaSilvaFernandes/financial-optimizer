package com.hackathon.financialoptimizer.domain.port;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;

import java.util.Optional;
import java.util.UUID;

public interface FinancialProfileRepository {
    FinancialProfile save(FinancialProfile profile);
    Optional<FinancialProfile> findByUserId(UUID userId);
}
