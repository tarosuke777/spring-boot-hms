INSERT INTO user(user_name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 'ROLE_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);
INSERT INTO user(user_name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES('user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 'ROLE_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);

INSERT INTO task (name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('サンプルタスク1', 'タスクの詳細メモが入ります。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', 0);

INSERT INTO task (name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('サンプルタスク2', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'user', 'user', 0);