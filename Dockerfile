# ========================
# ステージ 1: ビルドステージ (JARの作成のみ)
# ========================
FROM gradle:jdk25 AS builder 

# ENV PLAYWRIGHT_BROWSERS_PATH=/tmp/ms-playwright

# ENV GRADLE_USER_HOME=/tmp/.gradle
# RUN echo "--- GRADLE_USER_HOME is: $GRADLE_USER_HOME ---"

# # RUN apk update && apk add --no-cache bash build-base

# # Playwrightの依存関係をインストールするために必要なパッケージを追加
# # RUN apt-get update && apt-get install -y --no-install-recommends \
# #     libxcb-shm0 \
# #     libx11-xcb1 \
# #     libxrandr2 \
# #     libxcomposite1 \
# #     libxcursor1 \
# #     libxdamage1 \
# #     libxi6 \
# #     libxext6 \
# #     libxfixes3 \
# #     libx11-6 \
# #     libxcb1 \
# #     libgtk-3-0 \
# #     libgdk-pixbuf2.0-0 \
# #     libpangocairo-1.0-0 \
# #     libpango-1.0-0 \
# #     libatk1.0-0 \
# #     libcairo-gobject2 \
# #     libcairo2 \
# #     libglib2.0-0 \
# #     libxrender1 \
# #     libasound2 \
# #     libdbus-1-3 \
# #     && rm -rf /var/lib/apt/lists/*

# RUN apt-get update && apt-get install -y --no-install-recommends \
#     libxcb-shm0 \
#     libx11-xcb1 \
#     libxrandr2 \
#     libxcomposite1 \
#     libxcursor1 \
#     libxdamage1 \
#     libxi6 \
#     libxext6 \
#     libxfixes3 \
#     libx11-6 \
#     libxcb1 \
#     libgtk-3-0 \
#     libpangocairo-1.0-0 \
#     libpango-1.0-0 \
#     libatk1.0-0 \
#     libcairo-gobject2 \
#     libcairo2 \
#     libglib2.0-0 \
#     libxrender1 \
#     libdbus-1-3 \
#     libgdk-pixbuf2.0-bin \
#     libasound2t64 \
#     libglib2.0-0t64 \
#     && rm -rf /var/lib/apt/lists/*

# FROM mcr.microsoft.com/playwright/java:v1.61.0-jammy AS builder

# Jenkinsのshコマンド内容をDockerに移す
# COPY . /app

# RUN find /app -maxdepth 3

WORKDIR /app
COPY . /app
RUN chmod +x gradlew
RUN ./gradlew clean build

# ========================
# ステージ 2: 実行ステージ (JARの実行のみ)
# ========================
# FROM openjdk:21-jdk
# FROM eclipse-temurin:21-jre-ubi10-minimal
FROM eclipse-temurin:25-jre-alpine
# タイムゾーンを日本時間に変更（コンテナ内の実行環境をJSTに）
ENV TZ Asia/Tokyo 

# ビルドステージから作成されたJARをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

# # JenkinsのワークスペースでビルドされたJARをコンテナにコピー
# COPY build/libs/*.jar app.jar

# 実行
ENTRYPOINT ["java", "-jar", "app.jar"]