package com.hackathon.financialoptimizer.application.service.optimization;

import com.hackathon.financialoptimizer.application.service.optimization.strategy.*;
import com.hackathon.financialoptimizer.domain.valueobject.StrategyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimizationStrategyFactory {

    private final MaximizeRoiStrategy maximizeRoiStrategy;
    private final MinimizeRiskStrategy minimizeRiskStrategy;
    private final BalancedStrategy balancedStrategy;

    public OptimizationStrategy create(StrategyType type) {
        return switch (type) {
            case MAXIMIZE_ROI  -> maximizeRoiStrategy;
            case MINIMIZE_RISK -> minimizeRiskStrategy;
            case BALANCED      -> balancedStrategy;
        };
    }
}
