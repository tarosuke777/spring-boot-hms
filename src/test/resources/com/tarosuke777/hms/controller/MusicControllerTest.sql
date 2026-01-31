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

INSERT INTO artist (name, created_at, updated_at, created_by, updated_by, version) 
VALUES 
('Test Artist A', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'admin', 'admin', 0),
('Test Artist B', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'user', 'user', 0);

-- Test Artist A に紐づく曲
INSERT INTO music (music_name, artist_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Track 01 - A', artist_id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'admin', 'admin', 0 
FROM artist WHERE artist_name = 'Test Artist A';

INSERT INTO music (music_name, artist_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Track 02 - A', artist_id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'admin', 'admin', 0 
FROM artist WHERE artist_name = 'Test Artist A';

-- Test Artist B に紐づく曲
INSERT INTO music (music_name, artist_id, created_at, updated_at, created_by, updated_by, version)
SELECT 'Test Track 03 - B', artist_id, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 'user', 'user', 0 
FROM artist WHERE artist_name = 'Test Artist B';