INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzJ9M92u.e5m', 2,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO author (name, created_at, updated_at, created_by, updated_by, version)
VALUES ('Test Author A', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 1, 1, 0);

INSERT INTO book (name, author_id, created_at, updated_at, created_by, updated_by, version, genre,
                  is_adult)
SELECT 'Test Book 01 - A', id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6),
       (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0, 3,
       false
FROM author
WHERE name = 'Test Author A';

INSERT INTO movie (name, genre, is_adult, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Movie 01 - A', 1, false, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-25', 'newest', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id,
       0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-24', 'second-newest', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6),
       u.id, u.id, 0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-23', 'same-day-earlier', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6),
       u.id, u.id, 0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-23', 'same-day-later', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6),
       u.id, u.id, 0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-22', 'fourth', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id,
       0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-21', 'fifth', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id,
       0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-20', 'oldest', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id,
       0
FROM book b
JOIN user u ON u.name = 'admin'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO book_reading_log (book_id, read_date, memo, created_at, updated_at, created_by,
                              updated_by, version)
SELECT b.id, DATE '2026-09-26', 'other-user', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id,
       0
FROM book b
JOIN user u ON u.name = 'user'
WHERE b.name = 'Test Book 01 - A';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-25', 'https://atcoder.jp/contests/newest', 'coding-newest',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-24', 'https://atcoder.jp/contests/second-newest', 'coding-second-newest',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-23', 'https://atcoder.jp/contests/same-day-earlier',
       'coding-same-day-earlier', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-23', 'https://atcoder.jp/contests/same-day-later', 'coding-same-day-later',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-22', 'https://atcoder.jp/contests/fourth', 'coding-fourth',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-21', 'https://atcoder.jp/contests/fifth', 'coding-fifth',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-20', 'https://atcoder.jp/contests/oldest', 'coding-oldest',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO coding_log (solved_date, problem_url, memo, created_at, updated_at, created_by,
                        updated_by, version)
SELECT DATE '2026-09-26', 'https://example.com/other-coding-user', 'other-coding-user',
       CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'user';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-25', 'BOOK', b.id, 'activity-newest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Book 01 - A'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-24', 'VIDEO', m.id, 'activity-second-newest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN movie m ON m.name = 'Test Movie 01 - A'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-23', 'BOOK', b.id, 'activity-same-day-earlier', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Book 01 - A'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-23', 'BOOK', b.id, 'activity-same-day-later', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Book 01 - A'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-22', 'BOOK', b.id, 'activity-fourth', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Book 01 - A'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-21', 'BOOK', b.id, 'activity-fifth', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Book 01 - A'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-20', 'BOOK', b.id, 'activity-oldest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Book 01 - A'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-26', 'MOVIE', 7, 'other-activity-user', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'user';
