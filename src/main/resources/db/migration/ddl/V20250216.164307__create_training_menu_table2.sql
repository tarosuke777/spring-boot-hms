CREATE TABLE IF NOT EXISTS training (
  training_id INT auto_increment PRIMARY KEY,
  training_date DATE,
  training_menu_id INT,
  weight INT,
  reps INT
);
