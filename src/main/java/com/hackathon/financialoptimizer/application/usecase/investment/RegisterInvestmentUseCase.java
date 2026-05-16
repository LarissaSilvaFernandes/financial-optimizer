package com.hackathon.financialoptimizer.application.usecase.investment;

import com.hackathon.financialoptimizer.domain.entity.Investment;
import com.hackathon.financialoptimizer.domain.port.InvestmentRepository;
import com.hackathon.financialoptimizer.domain.valueobject.InvestmentCategory;
import com.hackathon.financialoptimizer.domain.valueobject.RiskTolerance;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.request.InvestmentRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.InvestmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterInvestmentUseCase {

    private final InvestmentRepository investmentRepository;
    private final SecurityUtils securityUtils;

    public InvestmentResponse execute(InvestmentRequest request) {
        var userId = securityUtils.getCurrentUserId();
        Investment investment = Investment.create(
                userId, request.name(), request.description(),
                request.requiredAmount(), request.expectedRoi(),
                RiskTolerance.from(request.riskLevel()),
                request.priorityScore(),
                InvestmentCategory.valueOf(request.category())
        );
        return toResponse(investmentRepository.save(investment));
    }

    public static InvestmentResponse toResponse(Investment i) {
        return new InvestmentResponse(i.getId(), i.getName(), i.getDescription(),
                i.getRequiredAmount(), i.getExpectedRoi(), i.getRiskLevel().label(),
                i.getPriorityScore(), i.getCategory().name(), i.getCategory().label(),
                i.getCreatedAt());
    }
}
