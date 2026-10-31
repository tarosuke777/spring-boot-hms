CREATE TABLE IF NOT EXISTS v_game_videos (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    file_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

DELETE FROM v_game_videos;

INSERT INTO v_game_videos (id, title, name, file_name, created_at, updated_at)
VALUES (1, 'Test Game Video', 'Test recording', 'test-game.mp4',
        TIMESTAMP '2026-10-01 10:00:00', TIMESTAMP '2026-10-01 10:00:00');

INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO book (name, created_at, updated_at, created_by, updated_by, version, genre, is_adult)
SELECT 'Test Activity Book', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0, 3, false
FROM user u
WHERE u.name = 'admin';

INSERT INTO book (name, created_at, updated_at, created_by, updated_by, version, genre, is_adult)
SELECT 'Test Adult Activity Book', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0, 3, true
FROM user u
WHERE u.name = 'admin';

INSERT INTO movie (name, genre, is_adult, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Activity Video', 1, false, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO movie (name, genre, is_adult, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Adult Activity Video', 1, true, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO book (name, created_at, updated_at, created_by, updated_by, version, genre, is_adult)
SELECT 'Other User Book', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0, 3, false
FROM user u
WHERE u.name = 'user';

INSERT INTO movie (name, genre, is_adult, created_at, updated_at, created_by, updated_by, version)
SELECT 'Other User Video', 1, true, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'user';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-25', 'BOOK', b.id, 'activity-newest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Activity Book'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-24', 'VIDEO', m.id, 'activity-second-newest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN movie m ON m.name = 'Test Activity Video'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-23', 'BOOK', b.id, 'activity-same-day-earlier', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Activity Book'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-23', 'BOOK', b.id, 'activity-same-day-later', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Activity Book'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-22', 'BOOK', b.id, 'activity-fourth', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Activity Book'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-21', 'BOOK', b.id, 'activity-fifth', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN book b ON b.name = 'Test Activity Book'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-20', 'MOVIE', m.id, 'activity-oldest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
JOIN movie m ON m.name = 'Test Activity Video'
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-26', 'VIDEO', 7, 'other-activity-user', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'user';
