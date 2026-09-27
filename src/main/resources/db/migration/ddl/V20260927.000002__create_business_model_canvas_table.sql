CREATE TABLE business_model_canvas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    business_id INT NOT NULL UNIQUE,
    customer_segments TEXT,
    value_proposition TEXT,
    channels TEXT,
    customer_relationships TEXT,
    revenue_streams TEXT,
    key_resources TEXT,
    key_activities TEXT,
    key_partners TEXT,
    cost_structure TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    updated_by INT NOT NULL,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_business_model_canvas_business FOREIGN KEY (business_id) REFERENCES business(id) ON DELETE CASCADE
);
