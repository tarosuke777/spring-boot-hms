INSERT INTO user(id, name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES(1, 'admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(id, name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES(2, 'user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO company (name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('CompanyA', 'NoteA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('CompanyB', 'NoteB', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'user'), (SELECT id FROM user WHERE name = 'user'), 0);
