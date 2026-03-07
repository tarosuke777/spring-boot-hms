ALTER TABLE user ADD COLUMN role_temp INT DEFAULT 2;

-- Migrate existing data: ADMIN -> 1, others -> 2
UPDATE user SET role_temp = CASE 
    WHEN role IN ('ROLE_ADMIN') THEN 1 
    ELSE 2 
END;

-- Drop the old VARCHAR column
ALTER TABLE user DROP COLUMN role;

-- Rename the temporary column to role
ALTER TABLE user CHANGE role_temp role INT NOT NULL DEFAULT 2;
