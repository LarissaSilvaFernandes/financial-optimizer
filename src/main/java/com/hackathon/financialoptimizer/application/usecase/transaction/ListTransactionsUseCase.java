package com.hackathon.financialoptimizer.application.usecase.transaction;

import com.hackathon.financialoptimizer.domain.port.TransactionRepository;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTransactionsUseCase {

    private final TransactionRepository transactionRepository;
    private final SecurityUtils securityUtils;

    public List<TransactionResponse> execute() {
        var userId = securityUtils.getCurrentUserId();
        return transactionRepository.findByUserId(userId).stream()
                .map(RegisterTransactionUseCase::toResponse)
                .toList();
    }
}
