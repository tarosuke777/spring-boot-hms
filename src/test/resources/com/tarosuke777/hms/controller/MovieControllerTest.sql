INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO movie (name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Sample Movie 1', 'Sample Note 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);

INSERT INTO movie (name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Sample Movie 2', 'Sample Note 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'user'), (SELECT id FROM user WHERE name = 'user'), 0);