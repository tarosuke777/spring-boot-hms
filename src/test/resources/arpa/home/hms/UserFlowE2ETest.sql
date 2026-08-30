INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('admin', '{bcrypt}$2a$10$3yC0fG9uCq.iDsiIvbYs6esRSh7AVkfVQ2yur9cRLeb3zQUipBKvq', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('user', '{bcrypt}$2a$10$3yC0fG9uCq.iDsiIvbYs6esRSh7AVkfVQ2yur9cRLeb3zQUipBKvq', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
