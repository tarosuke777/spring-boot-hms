CREATE OR REPLACE VIEW hms.v_game_videos AS
SELECT
    id,
    title,
    name,
    file_name,
    created_at,
    updated_at
FROM hv.videos;
