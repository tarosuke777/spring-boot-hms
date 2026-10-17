CREATE TABLE IF NOT EXISTS v_game_videos (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    file_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

DELETE FROM v_game_videos;

INSERT INTO v_game_videos (id, title, name, file_name, created_at, updated_at)
VALUES
    (1, 'Test Game A', 'First recording', 'first.mp4', TIMESTAMP '2026-10-01 10:00:00', TIMESTAMP '2026-10-01 10:00:00'),
    (2, 'Test Game B', NULL, 'second.mp4', TIMESTAMP '2026-10-02 10:00:00', TIMESTAMP '2026-10-02 10:00:00'),
    (3, 'Test Game C', 'Latest recording', 'game sample.mp4', TIMESTAMP '2026-10-03 10:00:00', TIMESTAMP '2026-10-03 10:00:00');
