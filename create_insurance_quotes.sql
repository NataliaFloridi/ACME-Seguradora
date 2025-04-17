CREATE TABLE insurance_quotes (
    id NUMBER(19) PRIMARY KEY,
    insurance_policy_id NUMBER(19),
    product_id VARCHAR2(36),
    offer_id VARCHAR2(36),
    category VARCHAR2(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    total_monthly_premium_amount NUMBER(19,2),
    total_coverage_amount NUMBER(19,2),
    coverages CLOB,
    assistances CLOB,
    customer CLOB,
    status VARCHAR2(20)
);

CREATE SEQUENCE insurance_quotes_seq START WITH 1 INCREMENT BY 1;

COMMENT ON COLUMN insurance_quotes.id IS 'Identificador único da cotação';
COMMENT ON COLUMN insurance_quotes.insurance_policy_id IS 'ID da apólice de seguro';
COMMENT ON COLUMN insurance_quotes.product_id IS 'ID do produto';
COMMENT ON COLUMN insurance_quotes.offer_id IS 'ID da oferta';
COMMENT ON COLUMN insurance_quotes.category IS 'Categoria do seguro (ex: HOME)';
COMMENT ON COLUMN insurance_quotes.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN insurance_quotes.updated_at IS 'Data de atualização do registro';
COMMENT ON COLUMN insurance_quotes.total_monthly_premium_amount IS 'Valor total do prêmio mensal';
COMMENT ON COLUMN insurance_quotes.total_coverage_amount IS 'Valor total da cobertura';
COMMENT ON COLUMN insurance_quotes.coverages IS 'JSON com as coberturas e seus valores';
COMMENT ON COLUMN insurance_quotes.assistances IS 'JSON com lista de assistências';
COMMENT ON COLUMN insurance_quotes.customer IS 'JSON com dados do cliente';
COMMENT ON COLUMN insurance_quotes.status IS 'Status da cotação'; 