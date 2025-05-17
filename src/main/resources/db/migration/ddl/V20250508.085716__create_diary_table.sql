CREATE TABLE IF NOT EXISTS diary (
    diary_id INT AUTO_INCREMENT PRIMARY KEY,
    diary_date DATE,
    todo_plan VARCHAR(255),
    todo_actual VARCHAR(255),
    fun_plan INT,
    fun_actual INT,
    comment_plan VARCHAR(255),
    comment_actual VARCHAR(255)
);