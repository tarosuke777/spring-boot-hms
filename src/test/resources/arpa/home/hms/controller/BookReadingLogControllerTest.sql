INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO author (name, created_at, updated_at, created_by, updated_by, version)
VALUES
('Test Author A', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 1, 1, 0),
('Test Author B', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 2, 2, 0);

INSERT INTO book (name, author_id, created_at, updated_at, created_by, updated_by, version, genre, is_adult)
SELECT 'Test Book 01 - A', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0, 3, false
FROM author WHERE name = 'Test Author A';

INSERT INTO book (name, author_id, created_at, updated_at, created_by, updated_by, version, genre, is_adult)
SELECT 'Test Book 02 - A', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0, 2, true
FROM author WHERE name = 'Test Author A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by, updated_by, version)
SELECT b.id, DATE '2026-09-20', 'テストメモ', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';
