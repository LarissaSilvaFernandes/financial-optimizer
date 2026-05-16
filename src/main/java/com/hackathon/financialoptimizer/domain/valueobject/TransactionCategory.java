package com.hackathon.financialoptimizer.domain.valueobject;

public enum TransactionCategory {
    HOUSING, FOOD, TRANSPORT, ENTERTAINMENT, HEALTH, EDUCATION, OTHER;

    public String label() {
        return switch (this) {
            case HOUSING       -> "Moradia";
            case FOOD          -> "Alimentação";
            case TRANSPORT     -> "Transporte";
            case ENTERTAINMENT -> "Entretenimento";
            case HEALTH        -> "Saúde";
            case EDUCATION     -> "Educação";
            case OTHER         -> "Outros";
        };
    }

    public boolean isEssentialByDefault() {
        return switch (this) {
            case HOUSING, FOOD, HEALTH -> true;
            default -> false;
        };
    }
}
