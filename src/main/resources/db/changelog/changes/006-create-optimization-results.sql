CREATE TABLE optimization_results (
    id                      UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                 UUID            NOT NULL,
    strategy_type           VARCHAR(50)     NOT NULL,
    budget_constraint       NUMERIC(15,2)   NOT NULL,
    total_roi_achieved      NUMERIC(5,2),
    total_savings_identified NUMERIC(15,2),
    selected_items          TEXT,
    rules_applied           TEXT,
    llm_explanation         TEXT,
    created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_optimization_results_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_optimization_results_user_id ON optimization_results(user_id);

