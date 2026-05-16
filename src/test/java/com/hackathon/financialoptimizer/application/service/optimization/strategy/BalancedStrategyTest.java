package com.hackathon.financialoptimizer.application.service.optimization.strategy;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.Investment;
import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BalancedStrategyTest {

    private BalancedStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new BalancedStrategy();
    }

    @Test
    void shouldApplyRiskPenaltyToHighRiskInvestment() {
        var profile = buildProfile();
        var highRisk = buildInvestment(new BigDecimal("500"), new BigDecimal("20.0"), new RiskTolerance.High(), 10);
        var lowRisk = buildInvestment(new BigDecimal("500"), new BigDecimal("20.0"), new RiskTolerance.Low(), 10);

        var items = strategy.prepareItems(List.of(), List.of(highRisk, lowRisk), profile);

        var highItem = items.stream()
                .filter(i -> i instanceof OptimizationItem.InvestmentItem ii &&
                        ii.riskLevel() instanceof RiskTolerance.High)
                .findFirst().orElseThrow();
        var lowItem = items.stream()
                .filter(i -> i instanceof OptimizationItem.InvestmentItem ii &&
                        ii.riskLevel() instanceof RiskTolerance.Low)
                .findFirst().orElseThrow();

        assertThat(highItem.value()).isLessThan(lowItem.value());
    }

    @Test
    void shouldExcludeEssentialExpenses() {
        var profile = buildProfile();
        var essential = buildTransaction(TransactionCategory.HOUSING, true);
        var nonEssential = buildTransaction(TransactionCategory.ENTERTAINMENT, false);

        var items = strategy.prepareItems(List.of(essential, nonEssential), List.of(), profile);

        assertThat(items).hasSize(1);
        var expenseItem = (OptimizationItem.ExpenseItem) items.getFirst();
        assertThat(expenseItem.category()).isEqualTo(TransactionCategory.ENTERTAINMENT);
    }

    @Test
    void shouldCombineBothExpensesAndInvestments() {
        var profile = buildProfile();
        var investment = buildInvestment(new BigDecimal("300"), new BigDecimal("10.0"), new RiskTolerance.Medium(), 5);
        var expense = buildTransaction(TransactionCategory.ENTERTAINMENT, false);

        var items = strategy.prepareItems(List.of(expense), List.of(investment), profile);

        assertThat(items).hasSize(2);
        assertThat(items).anyMatch(i -> i instanceof OptimizationItem.InvestmentItem);
        assertThat(items).anyMatch(i -> i instanceof OptimizationItem.ExpenseItem);
    }

    private FinancialProfile buildProfile() {
        return new FinancialProfile(
                UUID.randomUUID(), UUID.randomUUID(),
                new BigDecimal("5000"), new BigDecimal("2000"),
                new BigDecimal("500"), new RiskTolerance.Medium(),
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    private Investment buildInvestment(BigDecimal amount, BigDecimal roi, RiskTolerance risk, int priority) {
        return new Investment(
                UUID.randomUUID(), UUID.randomUUID(), "Investment", "desc",
                amount, roi, risk, priority, InvestmentCategory.FIXED_INCOME, LocalDateTime.now()
        );
    }

    private Transaction buildTransaction(TransactionCategory cat, boolean essential) {
        return new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "Expense", new BigDecimal("200"),
                cat, TransactionType.EXPENSE, LocalDate.now(), essential,
                new BigDecimal("25.0"), 5, "MANUAL", LocalDateTime.now()
        );
    }
}
