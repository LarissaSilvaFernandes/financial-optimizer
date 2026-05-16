package com.hackathon.financialoptimizer.domain.valueobject;

public enum StrategyType {
    MAXIMIZE_ROI, MINIMIZE_RISK, BALANCED;

    public String label() {
        return switch (this) {
            case MAXIMIZE_ROI  -> "Maximizar ROI";
            case MINIMIZE_RISK -> "Minimizar Risco";
            case BALANCED      -> "Balanceado";
        };
    }
}
