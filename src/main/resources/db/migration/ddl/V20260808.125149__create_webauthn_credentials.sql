CREATE TABLE webauthn_credentials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    credential_id VARCHAR(512) NOT NULL,
    public_key BLOB NOT NULL,
    count BIGINT NOT NULL DEFAULT 0,
    label VARCHAR(255),
    attestation_object BLOB,
    CONSTRAINT uk_webauthn_credentials_credential_id UNIQUE (credential_id),
    CONSTRAINT fk_webauthn_credentials_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);