@echo off

echo �Ώۃt�H���_�Ɉړ����܂�...
cd /d "C:\dev\vscode\workspace\spring-boot-hms"

echo �Ώۃt�H���_�Ɉړ����܂����B

echo �\�[�X�̍ŐV�����J�n���܂�...
git fetch
git merge

echo Gradle�r���h���J�n���܂�...
set JAVA_HOME="C:\dev\java\openjdk\jdk-21.0.2"
gradlew build

echo �������܂����B
pause