package com.hackathon.financialoptimizer.application.usecase.transaction;

import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.TransactionRepository;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final SecurityUtils securityUtils;

    public void execute(UUID transactionId) {
        var userId = securityUtils.getCurrentUserId();
        transactionRepository.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction", transactionId));
        transactionRepository.deleteByIdAndUserId(transactionId, userId);
    }
}
