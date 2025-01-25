INSERT INTO user(
     user_name
    ,password
    ,role
    ,email
) VALUES
(
     'admin'
    ,'{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m'
    ,'ROLE_ADMIN'
    ,'admin@tarosuke777.com'
),
(
     'user'
    ,'{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m'
    ,'ROLE_USER'
    ,'user@tarosuke777.com'
);

INSERT INTO artist(artist_name) VALUES('藤川千愛'),('分島花音');

INSERT INTO music(music_name, artist_id) select '好きになってはいけない理由', artist_id from artist LIMIT 1, 1;
INSERT INTO music(music_name, artist_id) select 'ゆずれない', artist_id from artist LIMIT 1, 1;
INSERT INTO music(music_name, artist_id) select 'サクラメイキュウ', artist_id from artist LIMIT 2, 1;
