CREATE TABLE IF NOT EXISTS book (
  book_id INT auto_increment PRIMARY KEY,
  book_name VARCHAR(50),
  author_id INT,
  link VARCHAR(255),
  note TEXT
);
