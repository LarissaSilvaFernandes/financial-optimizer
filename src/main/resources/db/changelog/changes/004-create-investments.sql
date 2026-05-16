CREATE TABLE investments (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID            NOT NULL,
    name            VARCHAR(255)    NOT NULL,
    description     TEXT,
    required_amount NUMERIC(15,2)   NOT NULL,
    expected_roi    NUMERIC(5,2)    NOT NULL,
    risk_level      VARCHAR(20)     NOT NULL,
    priority_score  INTEGER,
    category        VARCHAR(50),
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_investments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_investments_user_id ON investments(user_id);


