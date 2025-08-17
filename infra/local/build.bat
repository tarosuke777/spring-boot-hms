@echo off

echo 対象フォルダに移動します...
cd /d "C:\dev\vscode\workspace\spring-boot-hms"

echo 対象フォルダに移動しました。

echo ソースの最新化を開始します...
git fetch
git merge

echo Gradleビルドを開始します...
set JAVA_HOME="C:\dev\java\openjdk\jdk-21.0.2"
gradlew build

echo 完了しました。
pause