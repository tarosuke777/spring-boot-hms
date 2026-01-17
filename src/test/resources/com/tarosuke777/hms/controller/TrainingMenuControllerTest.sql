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

INSERT INTO training_menu (training_menu_name, target_area_id, link, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Push Up', 1, 'https://example.com/pushup', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0);
INSERT INTO training_menu (training_menu_name, target_area_id, link, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Squat', 2, 'https://example.com/squat', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 0);