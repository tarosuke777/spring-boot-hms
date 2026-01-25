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

INSERT INTO movie (movie_name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Sample Movie 1', 'Sample Note 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'adm  in', 0);

INSERT INTO movie (movie_name, note, created_at, updated_at, created_by, updated_by, version) 
VALUES ('Sample Movie 2', 'Sample Note 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'user', 'user', 0);