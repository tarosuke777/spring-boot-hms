CREATE TABLE IF NOT EXISTS music (
  music_id INT auto_increment PRIMARY KEY,
  music_name VARCHAR(50),
  artist_id INT
);

CREATE TABLE IF NOT EXISTS artist (
  artist_id INT auto_increment PRIMARY KEY,
  artist_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS user (
    user_id INT auto_increment PRIMARY KEY,
    user_name VARCHAR(50),
    password VARCHAR(100),
    role VARCHAR(50),
    email VARCHAR(256)
);
