package com.hackathon.financialoptimizer.domain.valueobject;

public sealed interface RiskTolerance permits RiskTolerance.Low, RiskTolerance.Medium, RiskTolerance.High {

    String label();
    int level();

    record Low() implements RiskTolerance {
        public String label() { return "LOW"; }
        public int level() { return 1; }
    }

    record Medium() implements RiskTolerance {
        public String label() { return "MEDIUM"; }
        public int level() { return 2; }
    }

    record High() implements RiskTolerance {
        public String label() { return "HIGH"; }
        public int level() { return 3; }
    }

    static RiskTolerance from(String value) {
        return switch (value.toUpperCase()) {
            case "LOW"    -> new Low();
            case "MEDIUM" -> new Medium();
            case "HIGH"   -> new High();
            default -> throw new IllegalArgumentException("Invalid risk tolerance: " + value);
        };
    }

    default boolean isCompatibleWith(RiskTolerance profileTolerance) {
        return this.level() <= profileTolerance.level();
    }
}
