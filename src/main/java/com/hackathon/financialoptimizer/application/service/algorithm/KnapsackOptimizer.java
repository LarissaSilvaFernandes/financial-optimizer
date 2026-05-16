package com.hackathon.financialoptimizer.application.service.algorithm;

import com.hackathon.financialoptimizer.domain.valueobject.OptimizationItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Component
public class KnapsackOptimizer {

    private static final int SCALE = 10;

    public record KnapsackResult(
            List<OptimizationItem> selectedItems,
            double totalValue,
            BigDecimal totalWeight
    ) {}

    public KnapsackResult optimize(List<OptimizationItem> items, BigDecimal budget) {
        if (items.isEmpty() || budget.compareTo(BigDecimal.ZERO) <= 0) {
            return new KnapsackResult(List.of(), 0.0, BigDecimal.ZERO);
        }

        int n = items.size();
        int capacity = (int) (budget.doubleValue() / SCALE);

        if (capacity <= 0) {
            return new KnapsackResult(List.of(), 0.0, BigDecimal.ZERO);
        }

        double[][] dp = new double[n + 1][capacity + 1];

        int[] weights = new int[n];
        double[] values = new double[n];

        for (int i = 0; i < n; i++) {
            weights[i] = Math.max(1, (int) (items.get(i).weight().doubleValue() / SCALE));
            values[i] = items.get(i).value();
        }

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                dp[i][w] = dp[i - 1][w];
                if (weights[i - 1] <= w) {
                    double withItem = dp[i - 1][w - weights[i - 1]] + values[i - 1];
                    if (withItem > dp[i][w]) {
                        dp[i][w] = withItem;
                    }
                }
            }
        }

        List<OptimizationItem> selected = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(items.get(i - 1));
                w -= weights[i - 1];
            }
        }

        BigDecimal totalWeight = selected.stream()
                .map(OptimizationItem::weight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new KnapsackResult(selected, dp[n][capacity], totalWeight);
    }
}
