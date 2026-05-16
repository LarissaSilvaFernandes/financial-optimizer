package com.hackathon.financialoptimizer.application.usecase.optimization;

import com.hackathon.financialoptimizer.application.service.algorithm.KnapsackOptimizer;
import com.hackathon.financialoptimizer.application.service.optimization.OptimizationContext;
import com.hackathon.financialoptimizer.application.service.optimization.OptimizationStrategyFactory;
import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.Investment;
import com.hackathon.financialoptimizer.domain.entity.OptimizationResult;
import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.exception.EntityNotFoundException;
import com.hackathon.financialoptimizer.domain.port.FinancialProfileRepository;
import com.hackathon.financialoptimizer.domain.port.InvestmentRepository;
import com.hackathon.financialoptimizer.domain.port.OptimizationResultRepository;
import com.hackathon.financialoptimizer.domain.port.OptimizationRuleRepository;
import com.hackathon.financialoptimizer.domain.port.TransactionRepository;
import com.hackathon.financialoptimizer.domain.valueobject.OptimizationItem;
import com.hackathon.financialoptimizer.domain.valueobject.StrategyType;
import com.hackathon.financialoptimizer.infrastructure.security.SecurityUtils;
import com.hackathon.financialoptimizer.presentation.dto.request.OptimizationRequest;
import com.hackathon.financialoptimizer.presentation.dto.response.OptimizationResultResponse;
import com.hackathon.financialoptimizer.presentation.dto.response.SelectedItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RunOptimizationUseCase {

    private final TransactionRepository transactionRepository;
    private final InvestmentRepository investmentRepository;
    private final FinancialProfileRepository financialProfileRepository;
    private final OptimizationResultRepository optimizationResultRepository;
    private final OptimizationRuleRepository optimizationRuleRepository;
    private final OptimizationStrategyFactory strategyFactory;
    private final KnapsackOptimizer knapsackOptimizer;
    private final SecurityUtils securityUtils;

    @Transactional
    public OptimizationResultResponse execute(OptimizationRequest request) {
        UUID userId = securityUtils.getCurrentUserId();

        FinancialProfile profile = financialProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("FinancialProfile", userId));

        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        List<Investment> investments = investmentRepository.findByUserId(userId);

        StrategyType strategyType = StrategyType.valueOf(request.strategyType());
        var strategy = strategyFactory.create(strategyType);

        List<OptimizationItem> items = strategy.prepareItems(transactions, investments, profile);

        OptimizationContext context = OptimizationContext.builder()
                .strategyType(strategyType)
                .budgetConstraint(request.budgetConstraint())
                .items(items)
                .financialProfile(profile)
                .build();

        KnapsackOptimizer.KnapsackResult result = knapsackOptimizer.optimize(
                context.items(), context.budgetConstraint());

        List<String> rulesApplied = optimizationRuleRepository.findAllActive().stream()
                .map(rule -> rule.name() + ": " + rule.description())
                .toList();

        List<OptimizationResult.SelectedItem> selectedItems = result.selectedItems().stream()
                .map(item -> toSelectedItem(item))
                .toList();

        BigDecimal totalSavings = selectedItems.stream()
                .filter(si -> "EXPENSE".equals(si.itemType()))
                .map(si -> si.weight())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OptimizationResult optimizationResult = new OptimizationResult(
                UUID.randomUUID(),
                userId,
                strategyType,
                request.budgetConstraint(),
                BigDecimal.valueOf(result.totalValue()),
                totalSavings,
                selectedItems,
                rulesApplied,
                null,
                LocalDateTime.now()
        );

        OptimizationResult saved = optimizationResultRepository.save(optimizationResult);

        return toResponse(saved);
    }

    private OptimizationResult.SelectedItem toSelectedItem(OptimizationItem item) {
        return switch (item) {
            case OptimizationItem.ExpenseItem e -> new OptimizationResult.SelectedItem(
                    e.id().toString(), e.name(), "EXPENSE", e.weight(), e.value(),
                    e.category().name(), null);
            case OptimizationItem.InvestmentItem i -> new OptimizationResult.SelectedItem(
                    i.id().toString(), i.name(), "INVESTMENT", i.weight(), i.value(),
                    i.category().name(), i.riskLevel().getClass().getSimpleName().toUpperCase());
        };
    }

    static OptimizationResultResponse toResponse(OptimizationResult result) {
        List<SelectedItemResponse> items = result.selectedItems().stream()
                .map(si -> new SelectedItemResponse(
                        si.id(), si.name(), si.itemType(), si.weight(),
                        si.value(), si.category(), si.riskLevel()))
                .toList();

        return new OptimizationResultResponse(
                result.id(),
                result.strategyType().name(),
                result.budgetConstraint(),
                result.totalRoiAchieved(),
                result.totalSavingsIdentified(),
                items,
                result.rulesApplied(),
                result.llmExplanation(),
                result.createdAt()
        );
    }
}
