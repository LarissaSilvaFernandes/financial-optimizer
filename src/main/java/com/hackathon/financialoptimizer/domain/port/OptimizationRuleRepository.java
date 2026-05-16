package com.hackathon.financialoptimizer.domain.port;

import com.hackathon.financialoptimizer.domain.entity.OptimizationRule;

import java.util.List;

public interface OptimizationRuleRepository {
    List<OptimizationRule> findAllActive();
    List<OptimizationRule> findByRuleType(String ruleType);
}
