CREATE TABLE IF NOT EXISTS user (
    user_id INT auto_increment PRIMARY KEY,
    user_name VARCHAR(50),
    password VARCHAR(100),
    role VARCHAR(50),
    email VARCHAR(256)
);
