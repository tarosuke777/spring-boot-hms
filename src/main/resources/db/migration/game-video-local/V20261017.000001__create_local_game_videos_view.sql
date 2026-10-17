CREATE SCHEMA IF NOT EXISTS hv;

CREATE TABLE IF NOT EXISTS hv.videos (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    file_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

INSERT INTO hv.videos (id, title, name, file_name, created_at, updated_at)
VALUES
    (1, 'ゲーム動画サンプル', 'ローカルプレビュー', 'local-preview.mp4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

CREATE VIEW v_game_videos AS
SELECT id, title, name, file_name, created_at, updated_at
FROM hv.videos;
