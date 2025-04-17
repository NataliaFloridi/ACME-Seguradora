DROP TABLE IF EXISTS insurance_quotes;

CREATE TABLE insurance_quotes (
    id BIGINT PRIMARY KEY,
    insurance_policy_id BIGINT,
    product_id VARCHAR(255) NOT NULL,
    offer_id VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    total_monthly_premium_amount DECIMAL(19,2) NOT NULL,
    total_coverage_amount DECIMAL(19,2) NOT NULL,
    coverages TEXT,
    assistances TEXT,
    customer TEXT,
    status VARCHAR(50) NOT NULL
); 