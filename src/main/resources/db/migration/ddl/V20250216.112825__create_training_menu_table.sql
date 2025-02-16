CREATE TABLE IF NOT EXISTS training_menu (
  training_menu_id INT auto_increment PRIMARY KEY,
  training_menu_name VARCHAR(50),
  target_area_id INT,
  link VARCHAR(255)
);
