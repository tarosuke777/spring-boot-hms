INSERT INTO user(user_name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 'ROLE_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);
INSERT INTO user(user_name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 'ROLE_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);

INSERT INTO author (name, created_at, updated_at, created_by, updated_by, version) 
VALUES 
('Test Author A', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'admin', 'admin', 0),
('Test Author B', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'user', 'user', 0);

-- Test Author A に紐づく本
INSERT INTO book (book_name, author_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Book 01 - A', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'admin', 'admin', 0 
FROM author WHERE name = 'Test Author A';

INSERT INTO book (book_name, author_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Book 02 - A', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'admin', 'admin', 0 
FROM author WHERE name = 'Test Author A';

-- Test Author B に紐づく本
INSERT INTO book (book_name, author_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Book 03 - B', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'user', 'user', 0 
FROM author WHERE name = 'Test Author B';
