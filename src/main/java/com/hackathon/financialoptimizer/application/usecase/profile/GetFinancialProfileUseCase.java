package com.hackathon.financialoptimizer.application.usecase.profile;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.FinancialProfileRepository;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.response.FinancialProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFinancialProfileUseCase {

    private final FinancialProfileRepository profileRepository;
    private final SecurityUtils securityUtils;

    public FinancialProfileResponse execute() {
        var userId = securityUtils.getCurrentUserId();
        FinancialProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("FinancialProfile", userId));
        return new FinancialProfileResponse(profile.getId(), profile.getMonthlyIncome(),
                profile.getTotalBudget(), profile.getSavingsGoal(),
                profile.getRiskTolerance().label(), profile.getUpdatedAt());
    }
}
