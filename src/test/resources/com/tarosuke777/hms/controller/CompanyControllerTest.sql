INSERT INTO user(id, name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES(1, 'admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(id, name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES(2, 'user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (1, 'CompanyA', 'NoteA', 'HistoryA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (2, 'CompanyB', 'NoteB', 'HistoryB', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'user'), (SELECT id FROM user WHERE name = 'user'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (3, 'CompanyC', 'NoteC', 'HistoryC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (4, 'CompanyD', 'NoteD', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (5, 'CompanyE', 'NoteE', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (6, 'CompanyF', 'NoteF', 'HistoryF', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (7, 'CompanyG', 'NoteG', 'HistoryG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (8, 'CompanyH', 'NoteH', 'HistoryH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (9, 'CompanyI', 'NoteI', 'HistoryI', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (10, 'CompanyJ', 'NoteJ', 'HistoryJ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (11, 'CompanyK', 'NoteK', 'HistoryK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
INSERT INTO company (id, name, note, job_application_history, created_at, updated_at, created_by, updated_by, version) 
VALUES (12, 'CompanyL', 'NoteL', 'HistoryL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM user WHERE name = 'admin'), (SELECT id FROM user WHERE name = 'admin'), 0);
