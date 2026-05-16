CREATE TABLE transactions (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID            NOT NULL,
    description         VARCHAR(255)    NOT NULL,
    amount              NUMERIC(15,2)   NOT NULL,
    category            VARCHAR(50)     NOT NULL,
    type                VARCHAR(20)     NOT NULL,
    transaction_date    DATE            NOT NULL,
    is_essential        BOOLEAN         NOT NULL DEFAULT FALSE,
    potential_reduction NUMERIC(5,2),
    utility_score       INTEGER,
    source              VARCHAR(50),
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_transactions_user_id    ON transactions(user_id);
CREATE INDEX idx_transactions_type       ON transactions(user_id, type);
CREATE INDEX idx_transactions_category   ON transactions(user_id, category);


