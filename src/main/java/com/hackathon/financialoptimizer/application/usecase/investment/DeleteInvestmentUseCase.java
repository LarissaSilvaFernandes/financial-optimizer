package com.hackathon.financialoptimizer.application.usecase.investment;

import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.InvestmentRepository;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteInvestmentUseCase {

    private final InvestmentRepository investmentRepository;
    private final SecurityUtils securityUtils;

    public void execute(UUID investmentId) {
        var userId = securityUtils.getCurrentUserId();
        investmentRepository.findByIdAndUserId(investmentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Investment", investmentId));
        investmentRepository.deleteByIdAndUserId(investmentId, userId);
    }
}
