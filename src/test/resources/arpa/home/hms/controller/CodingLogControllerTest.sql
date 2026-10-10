INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('other-user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO coding_log(solved_date, problem_url, memo, created_at, updated_at, created_by, updated_by, version)
SELECT DATE '2026-10-09', 'https://atcoder.jp/contests/123/tasks/abc123', 'AtCoder memo',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), id, id, 0
FROM user WHERE name = 'admin';
INSERT INTO coding_log(solved_date, problem_url, memo, created_at, updated_at, created_by, updated_by, version)
SELECT DATE '2026-10-10', 'https://example.com/other-user', 'other-user',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), id, id, 0
FROM user WHERE name = 'other-user';
