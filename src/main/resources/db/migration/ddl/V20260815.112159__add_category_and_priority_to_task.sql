ALTER TABLE task ADD COLUMN category INT NOT NULL DEFAULT 1 COMMENT 'タブ分類 (1: 今すぐやるタスク, 2: 将来やりたいこと)';
ALTER TABLE task ADD COLUMN importance INT DEFAULT NULL COMMENT '重要度 (1: 高, 2: 低)';
ALTER TABLE task ADD COLUMN urgency INT DEFAULT NULL COMMENT '緊急度 (1: 高, 2: 低)';