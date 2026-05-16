package com.hackathon.financialoptimizer.application.usecase.transaction;

import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.port.TransactionRepository;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionCategory;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionType;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.request.TransactionRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final SecurityUtils securityUtils;

    public TransactionResponse execute(TransactionRequest request) {
        var userId = securityUtils.getCurrentUserId();
        TransactionCategory category = TransactionCategory.valueOf(request.category());
        TransactionType type = TransactionType.valueOf(request.type());

        Transaction transaction = Transaction.create(
                userId, request.description(), request.amount(),
                category, type, request.transactionDate(),
                request.essential(), request.potentialReduction(),
                request.utilityScore(), "MANUAL"
        );

        return toResponse(transactionRepository.save(transaction));
    }

    public static TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(t.getId(), t.getDescription(), t.getAmount(),
                t.getCategory().name(), t.getCategory().label(),
                t.getType().name(), t.getTransactionDate(), t.isEssential(),
                t.getPotentialReduction(), t.potentialSavingAmount(),
                t.getUtilityScore(), t.getSource(), t.getCreatedAt());
    }
}
