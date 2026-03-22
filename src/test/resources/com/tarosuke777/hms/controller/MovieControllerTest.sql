INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO casts (name, created_at, updated_at, created_by, updated_by, version) 
VALUES 
('Test Cast A', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 1, 1, 0),
('Test Cast B', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 2, 2, 0);

INSERT INTO movie (name, cast_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Movie 01 - A', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0 
FROM casts WHERE name = 'Test Cast A';
INSERT INTO movie (name, cast_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Movie 02 - A', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0 
FROM casts WHERE name = 'Test Cast A';
INSERT INTO movie (name, cast_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Movie 03 - B', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), (SELECT id FROM user WHERE name = 'user'), (SELECT id FROM user WHERE name = 'user'), 0 
FROM casts WHERE name = 'Test Cast B';
