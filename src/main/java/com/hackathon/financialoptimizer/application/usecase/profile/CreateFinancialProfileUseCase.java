package com.hackathon.financialoptimizer.application.usecase.profile;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.port.FinancialProfileRepository;
import com.hackathon.financialoptimizer.domain.valueobject.RiskTolerance;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.request.FinancialProfileRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.FinancialProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateFinancialProfileUseCase {

    private final FinancialProfileRepository profileRepository;
    private final SecurityUtils securityUtils;

    public FinancialProfileResponse execute(FinancialProfileRequest request) {
        UUID userId = securityUtils.getCurrentUserId();
        RiskTolerance risk = RiskTolerance.from(request.riskTolerance());

        FinancialProfile profile = profileRepository.findByUserId(userId)
                .map(existing -> {
                    existing.update(request.monthlyIncome(), request.totalBudget(),
                            request.savingsGoal(), risk);
                    return existing;
                })
                .orElseGet(() -> FinancialProfile.create(userId, request.monthlyIncome(),
                        request.totalBudget(), request.savingsGoal(), risk));

        FinancialProfile saved = profileRepository.save(profile);
        return toResponse(saved);
    }

    private FinancialProfileResponse toResponse(FinancialProfile p) {
        return new FinancialProfileResponse(p.getId(), p.getMonthlyIncome(), p.getTotalBudget(),
                p.getSavingsGoal(), p.getRiskTolerance().label(), p.getUpdatedAt());
    }
}
