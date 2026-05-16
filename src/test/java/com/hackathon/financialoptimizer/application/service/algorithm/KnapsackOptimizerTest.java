package com.hackathon.financialoptimizer.application.service.algorithm;

import com.hackathon.financialoptimizer.domain.valueobject.InvestmentCategory;
import com.hackathon.financialoptimizer.domain.valueobject.OptimizationItem;
import com.hackathon.financialoptimizer.domain.valueobject.RiskTolerance;
import com.hackathon.financialoptimizer.domain.valueobject.TransactionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class KnapsackOptimizerTest {

    private KnapsackOptimizer optimizer;

    @BeforeEach
    void setUp() {
        optimizer = new KnapsackOptimizer();
    }

    @Test
    void shouldReturnEmptyWhenNoItems() {
        var result = optimizer.optimize(Collections.emptyList(), new BigDecimal("1000"));

        assertThat(result.selectedItems()).isEmpty();
        assertThat(result.totalValue()).isEqualTo(0.0);
        assertThat(result.totalWeight()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnEmptyWhenBudgetIsZero() {
        List<OptimizationItem> items = List.of(investmentItem("500", 10.0));

        var result = optimizer.optimize(items, BigDecimal.ZERO);

        assertThat(result.selectedItems()).isEmpty();
    }

    @Test
    void shouldSelectAllItemsWhenBudgetIsLarge() {
        List<OptimizationItem> items = List.of(
                investmentItem("100", 5.0),
                investmentItem("200", 7.0),
                expenseItem("50", 3.0)
        );

        var result = optimizer.optimize(items, new BigDecimal("1000"));

        assertThat(result.selectedItems()).hasSize(3);
    }

    @Test
    void shouldNotExceedBudgetConstraint() {
        List<OptimizationItem> items = List.of(
                investmentItem("300", 8.0),
                investmentItem("400", 9.0),
                investmentItem("500", 7.0)
        );

        var result = optimizer.optimize(items, new BigDecimal("600"));

        assertThat(result.totalWeight()).isLessThanOrEqualTo(new BigDecimal("600"));
    }

    @Test
    void shouldSelectOptimalCombinationKnownResult() {
        // Budget: 500 — items: 200@5, 300@9, 400@4
        // Optimal: select 200@5 + 300@9 = total value 14, total weight 500
        OptimizationItem item1 = investmentItem("200", 5.0);
        OptimizationItem item2 = investmentItem("300", 9.0);
        OptimizationItem item3 = investmentItem("400", 4.0);

        var result = optimizer.optimize(List.of(item1, item2, item3), new BigDecimal("500"));

        assertThat(result.totalValue()).isEqualTo(14.0);
        assertThat(result.selectedItems()).hasSize(2);
        assertThat(result.totalWeight()).isEqualByComparingTo(new BigDecimal("500"));
    }

    @Test
    void shouldHandleSingleItemFittingBudget() {
        List<OptimizationItem> items = List.of(investmentItem("500", 10.0));

        var result = optimizer.optimize(items, new BigDecimal("1000"));

        assertThat(result.selectedItems()).hasSize(1);
        assertThat(result.totalValue()).isEqualTo(10.0);
    }

    @Test
    void shouldHandleSingleItemNotFittingBudget() {
        List<OptimizationItem> items = List.of(investmentItem("1500", 10.0));

        var result = optimizer.optimize(items, new BigDecimal("500"));

        assertThat(result.selectedItems()).isEmpty();
    }

    private OptimizationItem.InvestmentItem investmentItem(String weight, double value) {
        return new OptimizationItem.InvestmentItem(
                UUID.randomUUID(), "Investment " + weight,
                new BigDecimal(weight), value,
                new RiskTolerance.Medium(), InvestmentCategory.FIXED_INCOME
        );
    }

    private OptimizationItem.ExpenseItem expenseItem(String weight, double value) {
        return new OptimizationItem.ExpenseItem(
                UUID.randomUUID(), "Expense " + weight,
                new BigDecimal(weight), value,
                TransactionCategory.ENTERTAINMENT, false
        );
    }
}
