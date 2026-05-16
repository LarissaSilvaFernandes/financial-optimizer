CREATE TABLE financial_profiles (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID            NOT NULL UNIQUE,
    monthly_income  NUMERIC(15,2)   NOT NULL,
    total_budget    NUMERIC(15,2)   NOT NULL,
    savings_goal    NUMERIC(15,2),
    risk_tolerance  VARCHAR(20)     NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_financial_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


