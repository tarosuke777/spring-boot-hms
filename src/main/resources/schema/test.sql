INSERT INTO music(music_name, artist_id) VALUES('好きになってはいけない理由',1),('ゆずれない',1),('サクラメイキュウ',2);
INSERT INTO artist(artist_name) VALUES('藤川千愛'),('分島花音'),('test');

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