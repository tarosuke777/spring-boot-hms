INSERT INTO user(
     user_name
    ,password
    ,role
) VALUES
(
     'admin'
    ,'{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m'
    ,'ROLE_ADMIN'
),
(
     'user'
    ,'{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m'
    ,'ROLE_USER'
);

INSERT INTO diary (diary_date, todo_plan, todo_actual, fun_plan, fun_actual, comment_plan, comment_actual, created_at, updated_at, created_by, updated_by, version) 
VALUES ('2026-01-23', 'Java学習', 'Java学習完了', 5, 5, '集中する', 'よくできた', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0);

INSERT INTO diary (diary_date, todo_plan, todo_actual, fun_plan, fun_actual, comment_plan, comment_actual, created_at, updated_at, created_by, updated_by, version) 
VALUES ('2026-01-24', '読書', NULL, 3, NULL, '1章読む', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0);