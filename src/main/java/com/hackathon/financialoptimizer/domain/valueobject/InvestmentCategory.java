package com.hackathon.financialoptimizer.domain.valueobject;

public enum InvestmentCategory {
    STOCKS, FIXED_INCOME, REAL_ESTATE, CRYPTO, SAVINGS;

    public String label() {
        return switch (this) {
            case STOCKS       -> "Ações";
            case FIXED_INCOME -> "Renda Fixa";
            case REAL_ESTATE  -> "Fundos Imobiliários";
            case CRYPTO       -> "Criptomoedas";
            case SAVINGS      -> "Poupança / CDB";
        };
    }
}
