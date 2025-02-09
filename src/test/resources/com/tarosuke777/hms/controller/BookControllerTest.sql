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

INSERT INTO author(author_name) VALUES('著者１'),('著者２');

INSERT INTO book(book_name, author_id) select '本１', author_id from author LIMIT 1, 1;
INSERT INTO book(book_name, author_id) select '本２', author_id from author LIMIT 1, 1;
INSERT INTO book(book_name, author_id) select '本３', author_id from author LIMIT 2, 1;
