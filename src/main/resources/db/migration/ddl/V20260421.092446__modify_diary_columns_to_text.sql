-- todo_plan の変更
ALTER TABLE diary CHANGE todo_plan todo_plan TEXT;

-- todo_actual の変更
ALTER TABLE diary CHANGE todo_actual todo_actual TEXT;

-- comment_plan の変更
ALTER TABLE diary CHANGE comment_plan comment_plan TEXT;

-- comment_actual の変更
ALTER TABLE diary CHANGE comment_actual comment_actual TEXT;