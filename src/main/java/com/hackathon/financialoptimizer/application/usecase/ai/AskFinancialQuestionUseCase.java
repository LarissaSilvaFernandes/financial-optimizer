package com.hackathon.financialoptimizer.application.usecase.ai;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.FinancialProfileRepository;
import com.hackathon.financialoptimizer.domain.port.TransactionRepository;
import com.hackathon.financialoptimizer.infrastructure.ai.SpringAiService;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AskFinancialQuestionUseCase {

    private final FinancialProfileRepository financialProfileRepository;
    private final TransactionRepository transactionRepository;
    private final SpringAiService springAiService;
    private final SecurityUtils securityUtils;

    public String execute(String question) {
        UUID userId = securityUtils.getCurrentUserId();

        FinancialProfile profile = financialProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("FinancialProfile", userId));

        List<Transaction> recentTransactions = transactionRepository.findByUserId(userId).stream()
                .sorted((a, b) -> b.transactionDate().compareTo(a.transactionDate()))
                .limit(10)
                .toList();

        String userContext = buildUserContext(profile, recentTransactions);
        return springAiService.answerFinancialQuestion(question, userContext);
    }

    private String buildUserContext(FinancialProfile profile, List<Transaction> transactions) {
        String txContext = transactions.stream()
                .map(t -> "  - %s: R$%.2f (%s/%s)".formatted(
                        t.description(), t.amount(), t.category().label(), t.type().name()))
                .collect(Collectors.joining("\n"));

        return """
                Renda mensal: R$%.2f
                Orçamento disponível: R$%.2f
                Tolerância ao risco: %s
                Últimas transações:
                %s
                """.formatted(
                profile.monthlyIncome(),
                profile.totalBudget(),
                profile.riskTolerance().getClass().getSimpleName(),
                txContext
        );
    }
}
