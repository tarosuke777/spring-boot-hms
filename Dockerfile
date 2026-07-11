# ========================
# ステージ 1: ビルドステージ
# docker compose build builderで、ビルドまで行う。
# docker compose run --name jenkins-builder-container builder ./gradlew build で、ビルドステージのコンテナを起動して、ビルドを実行する。
# エラーの有無に関わらず、docker cp jenkins-builder-container:/app/build .　で、ビルド成果物をホストにコピーする。
# ※ コンテナ起動でビルドしている理由は、ビルド時にエラーが起きた場合、成果物をホストにコピーできないため
# ========================
# FROM gradle:jdk25 AS builder
FROM mcr.microsoft.com/playwright/java:v1.61.0-jammy AS builder

WORKDIR /app
COPY . /app
RUN chmod +x gradlew

# ========================
# ステージ 2: 実行ステージ
# docker compose build hms-ap で、実行ステージのイメージを作成する。
# docker compose up -d hms-ap で、実行ステージのコンテナを起動する。
# ========================
# FROM openjdk:21-jdk
# FROM eclipse-temurin:21-jre-ubi10-minimal
FROM eclipse-temurin:25-jre-alpine AS final-build
# タイムゾーンを日本時間に変更（コンテナ内の実行環境をJSTに）
ENV TZ Asia/Tokyo 

# ビルドステージから作成されたJARをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

# 実行
ENTRYPOINT ["java", "-jar", "app.jar"]
