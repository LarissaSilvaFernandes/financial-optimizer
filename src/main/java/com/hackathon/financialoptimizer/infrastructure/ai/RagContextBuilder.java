package com.hackathon.financialoptimizer.infrastructure.ai;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.OptimizationResult;
import com.hackathon.financialoptimizer.domain.entity.OptimizationRule;
import com.hackathon.financialoptimizer.domain.port.OptimizationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RagContextBuilder {

    private final OptimizationRuleRepository optimizationRuleRepository;

    public String build(OptimizationResult result, FinancialProfile profile) {
        List<OptimizationRule> activeRules = optimizationRuleRepository.findAllActive();

        String rulesContext = activeRules.stream()
                .map(r -> "- [%s] %s".formatted(r.ruleType(), r.description()))
                .collect(Collectors.joining("\n"));

        String selectedContext = result.selectedItems().stream()
                .map(item -> "  * %s (%s): peso=R$%.2f, valor=%.2f, categoria=%s%s"
                        .formatted(
                                item.name(), item.itemType(),
                                item.weight(), item.value(),
                                item.category(),
                                item.riskLevel() != null ? ", risco=" + item.riskLevel() : ""))
                .collect(Collectors.joining("\n"));

        return """
                === PERFIL FINANCEIRO ===
                Renda mensal: R$%.2f
                Orçamento disponível: R$%.2f
                Meta de poupança: R$%.2f
                Tolerância ao risco: %s

                === ESTRATÉGIA UTILIZADA ===
                %s

                === RESTRIÇÃO DE ORÇAMENTO ===
                R$%.2f

                === ITENS SELECIONADOS PELO ALGORITMO ===
                %s

                === REGRAS DE NEGÓCIO APLICADAS (RAG) ===
                %s
                """.formatted(
                profile.monthlyIncome(),
                profile.totalBudget(),
                profile.savingsGoal() != null ? profile.savingsGoal() : 0,
                profile.riskTolerance().getClass().getSimpleName(),
                result.strategyType().label(),
                result.budgetConstraint(),
                selectedContext,
                rulesContext
        );
    }
}
