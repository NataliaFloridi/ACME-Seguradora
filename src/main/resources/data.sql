-- Inserindo dados de exemplo na tabela insurance_quotes
INSERT INTO insurance_quotes (
    id,
    insurance_policy_id,
    product_id,
    offer_id,
    category,
    created_at,
    updated_at,
    total_monthly_premium_amount,
    total_coverage_amount,
    coverages,
    assistances,
    customer,
    status
) VALUES (
    22345,
    756969,
    '1b2da7cc-b367-4196-8a78-9cfeec21f587',
    'adc56d77-348c-4bf0-908f-22d402ee715c',
    'HOME',
    '2024-05-22 20:37:17.090098',
    '2024-05-22 21:05:02.090098',
    60.25,
    1280000.00,
    '{"Incêndio": 500000.00, "Desastres naturais": 600000.00, "Responsabiliadade civil": 80000.00, "Roubo": 100000.00}',
    '["Encanador", "Eletricista", "Chaveiro 24h", "Assistência Funerária"]',
    '{"document_number": "36205578900", "name": "John Wick", "type": "NATURAL", "gender": "MALE", "date_of_birth": "1973-05-02", "email": "johnwick@gmail.com", "phone_number": 11950503030}',
    'CREATED'
); 