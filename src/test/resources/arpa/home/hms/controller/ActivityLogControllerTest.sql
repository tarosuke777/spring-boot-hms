INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version)
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-25', 'BOOK', 1, 'activity-newest', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6),
       u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-24', 'BOOK', 1, 'activity-second-newest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-23', 'BOOK', 1, 'activity-same-day-earlier', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-23', 'BOOK', 1, 'activity-same-day-later', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-22', 'BOOK', 1, 'activity-fourth', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-21', 'BOOK', 1, 'activity-fifth', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6),
       u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-20', 'BOOK', 1, 'activity-oldest', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'admin';

INSERT INTO activity_log (activity_date, target_type, target_id, memo, created_at, updated_at,
                          created_by, updated_by, version)
SELECT DATE '2026-09-26', 'MOVIE', 7, 'other-activity-user', CURRENT_TIMESTAMP(6),
       CURRENT_TIMESTAMP(6), u.id, u.id, 0
FROM user u
WHERE u.name = 'user';
