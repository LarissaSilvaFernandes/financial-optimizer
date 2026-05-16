package com.hackathon.financialoptimizer.application.service.optimization.strategy;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.Investment;
import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Component
public class BalancedStrategy implements OptimizationStrategy {

    @Override
    public StrategyType type() {
        return StrategyType.BALANCED;
    }

    @Override
    public List<OptimizationItem> prepareItems(List<Transaction> expenses,
                                               List<Investment> investments,
                                               FinancialProfile profile) {
        List<OptimizationItem> items = new ArrayList<>();

        // Investimentos com fator de penalidade por risco
        investments.stream()
                .filter(inv -> inv.getRequiredAmount().compareTo(BigDecimal.ZERO) > 0)
                .map(inv -> {
                    double riskPenalty = switch (inv.getRiskLevel().label()) {
                        case "LOW"    -> 1.0;
                        case "MEDIUM" -> 0.8;
                        case "HIGH"   -> 0.5;
                        default       -> 0.7;
                    };
                    return new OptimizationItem.InvestmentItem(
                            inv.getId(), inv.getName(),
                            inv.getRequiredAmount(),
                            inv.getExpectedRoi().doubleValue() * inv.getPriorityScore() / 10.0 * riskPenalty,
                            inv.getRiskLevel(), inv.getCategory()
                    );
                })
                .forEach(items::add);

        // Cortes de gastos não-essenciais (todos)
        expenses.stream()
                .filter(t -> !t.isEssential()
                        && t.getPotentialReduction() != null
                        && t.getPotentialReduction().compareTo(BigDecimal.ZERO) > 0)
                .map(t -> new OptimizationItem.ExpenseItem(
                        t.getId(), t.getDescription(),
                        t.potentialSavingAmount(),
                        (10 - t.getUtilityScore()) * t.getPotentialReduction().doubleValue() / 100.0,
                        t.getCategory(), false
                ))
                .forEach(items::add);

        return items;
    }

    @Override
    public List<String> getRuleNames() {
        return List.of(
                "Regra 50-30-20 de orçamento",
                "Diversificação para perfis moderados",
                "Cortar gastos com entretenimento não-essenciais",
                "Fundo de emergência antes de investimentos"
        );
    }
}
