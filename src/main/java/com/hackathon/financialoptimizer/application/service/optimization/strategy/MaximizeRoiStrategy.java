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
public class MaximizeRoiStrategy implements OptimizationStrategy {

    @Override
    public StrategyType type() {
        return StrategyType.MAXIMIZE_ROI;
    }

    @Override
    public List<OptimizationItem> prepareItems(List<Transaction> expenses,
                                               List<Investment> investments,
                                               FinancialProfile profile) {
        List<OptimizationItem> items = new ArrayList<>();

        // Investimentos: todos permitidos, valor = roi * priorityScore
        investments.stream()
                .filter(inv -> inv.getRequiredAmount().compareTo(BigDecimal.ZERO) > 0)
                .map(inv -> new OptimizationItem.InvestmentItem(
                        inv.getId(), inv.getName(),
                        inv.getRequiredAmount(),
                        inv.getExpectedRoi().doubleValue() * inv.getPriorityScore() / 10.0,
                        inv.getRiskLevel(), inv.getCategory()
                ))
                .forEach(items::add);

        // Gastos não-essenciais com potencial de corte
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
                "Priorizar renda fixa para perfis conservadores",
                "Maximização de ROI em perfis arrojados",
                "Cortar gastos com entretenimento não-essenciais"
        );
    }
}
