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

class MaximizeRoiStrategyTest {

    private MaximizeRoiStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MaximizeRoiStrategy();
    }

    @Test
    void shouldIncludeAllInvestments() {
        var profile = buildProfile();
        var investment = buildInvestment(new BigDecimal("500"), new BigDecimal("15.0"), new RiskTolerance.High());

        var items = strategy.prepareItems(List.of(), List.of(investment), profile);

        assertThat(items).hasSize(1);
        assertThat(items.getFirst()).isInstanceOf(OptimizationItem.InvestmentItem.class);
    }

    @Test
    void shouldIncludeNonEssentialExpenses() {
        var profile = buildProfile();
        var nonEssential = buildTransaction(TransactionCategory.ENTERTAINMENT, false, new BigDecimal("200"), 30.0, 4);
        var essential = buildTransaction(TransactionCategory.HOUSING, true, new BigDecimal("1000"), 10.0, 9);

        var items = strategy.prepareItems(List.of(nonEssential, essential), List.of(), profile);

        assertThat(items).hasSize(1);
        var expenseItem = (OptimizationItem.ExpenseItem) items.getFirst();
        assertThat(expenseItem.essential()).isFalse();
    }

    @Test
    void shouldCalculateCorrectValueForInvestment() {
        var profile = buildProfile();
        var investment = buildInvestment(new BigDecimal("1000"), new BigDecimal("20.0"), new RiskTolerance.Medium());
        investment = new Investment(investment.id(), investment.userId(), investment.name(),
                investment.description(), investment.requiredAmount(), investment.expectedRoi(),
                investment.riskLevel(), 8, investment.category(), investment.createdAt());

        var items = strategy.prepareItems(List.of(), List.of(investment), profile);

        double expectedValue = investment.expectedRoi().doubleValue() * investment.priorityScore() / 10.0;
        assertThat(items.getFirst().value()).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnEmptyWhenNoTransactionsOrInvestments() {
        var items = strategy.prepareItems(List.of(), List.of(), buildProfile());

        assertThat(items).isEmpty();
    }

    private FinancialProfile buildProfile() {
        return new FinancialProfile(
                UUID.randomUUID(), UUID.randomUUID(),
                new BigDecimal("5000"), new BigDecimal("2000"),
                new BigDecimal("500"), new RiskTolerance.High(),
                LocalDateTime.now(), LocalDateTime.now()
        );
    }

    private Investment buildInvestment(BigDecimal amount, BigDecimal roi, RiskTolerance risk) {
        return new Investment(
                UUID.randomUUID(), UUID.randomUUID(), "Test Investment", "desc",
                amount, roi, risk, 7, InvestmentCategory.STOCKS, LocalDateTime.now()
        );
    }

    private Transaction buildTransaction(TransactionCategory cat, boolean essential,
                                         BigDecimal amount, double reduction, int utility) {
        return new Transaction(
                UUID.randomUUID(), UUID.randomUUID(), "Test Expense", amount,
                cat, TransactionType.EXPENSE, LocalDate.now(), essential,
                BigDecimal.valueOf(reduction), utility, "MANUAL", LocalDateTime.now()
        );
    }
}
