INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO casts(name, link, created_at, updated_at, created_by, updated_by, version)
VALUES ('Test Cast A', 'https://example.com/cast-a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO casts(name, link, created_at, updated_at, created_by, updated_by, version)
VALUES ('Test Cast B', null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'user'), (SELECT id FROM user WHERE name = 'user'), 0);

INSERT INTO movie(name, cast_id, link, note, genre, is_adult, created_at, updated_at, created_by, updated_by, version)
VALUES ('Test Movie 1', (SELECT id FROM casts WHERE name = 'Test Cast A' LIMIT 1), 'https://example.com/movie-1', 'Test Note 1', 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO movie(name, cast_id, link, note, genre, is_adult, created_at, updated_at, created_by, updated_by, version)
VALUES ('Test Movie 2', (SELECT id FROM casts WHERE name = 'Test Cast A' LIMIT 1), null, null, 2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);

