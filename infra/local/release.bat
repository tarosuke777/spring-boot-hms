@echo off

echo �Ώۃt�H���_�Ɉړ����܂�...
cd /d "C:\dev\vscode\workspace\spring-boot-hms"

echo �Ώۃt�H���_�Ɉړ����܂����B

echo Docker Compose�Ńr���h���܂�...
docker compose build

echo Docker Compose�ŃR���e�i���~���č폜���܂�...
docker compose down

echo Docker Compose�ŃR���e�i���č쐬���܂�...
docker compose up -d

echo �������܂����B
pause