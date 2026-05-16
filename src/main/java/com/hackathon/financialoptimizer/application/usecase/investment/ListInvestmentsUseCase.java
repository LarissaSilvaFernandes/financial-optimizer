package com.hackathon.financialoptimizer.application.usecase.investment;

import com.hackathon.financialoptimizer.domain.port.InvestmentRepository;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.response.InvestmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListInvestmentsUseCase {

    private final InvestmentRepository investmentRepository;
    private final SecurityUtils securityUtils;

    public List<InvestmentResponse> execute() {
        var userId = securityUtils.getCurrentUserId();
        return investmentRepository.findByUserId(userId).stream()
                .map(RegisterInvestmentUseCase::toResponse)
                .toList();
    }
}
