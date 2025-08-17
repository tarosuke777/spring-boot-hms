@echo off

echo 対象フォルダに移動します...
cd /d "C:\dev\vscode\workspace\spring-boot-hms"

echo 対象フォルダに移動しました。

echo Docker Composeでビルドします...
docker compose build

echo Docker Composeでコンテナを停止して削除します...
docker compose down

echo Docker Composeでコンテナを再作成します...
docker compose up -d

echo 完了しました。
pause