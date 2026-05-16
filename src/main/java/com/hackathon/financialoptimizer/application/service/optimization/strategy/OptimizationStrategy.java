package com.hackathon.financialoptimizer.application.service.optimization.strategy;

import com.hackathon.financialoptimizer.domain.entity.FinancialProfile;
import com.hackathon.financialoptimizer.domain.entity.Investment;
import com.hackathon.financialoptimizer.domain.entity.Transaction;
import com.hackathon.financialoptimizer.domain.valueobject.OptimizationItem;
import com.hackathon.financialoptimizer.domain.valueobject.StrategyType;

import java.util.List;

public interface OptimizationStrategy {

    StrategyType type();

    List<OptimizationItem> prepareItems(List<Transaction> expenses,
                                        List<Investment> investments,
                                        FinancialProfile profile);

    List<String> getRuleNames();
}
