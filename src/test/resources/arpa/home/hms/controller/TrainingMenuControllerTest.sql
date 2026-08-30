INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO training_menu (name, target_area_id, link, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Push Up', 1, 'https://example.com/pushup', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO training_menu (name, target_area_id, link, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Squat', 2, 'https://example.com/squat', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'user'), (SELECT id FROM user WHERE name = 'user'), 0);