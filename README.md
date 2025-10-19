# HMS (Hobby Management System)

## 1. 概要

このプロジェクトは、Spring Boot をベースとした個人の趣味管理システム（Hobby Management System）です。
音楽、書籍、映画、トレーニング、日記など、多岐にわたる趣味の記録と管理を目的としています。

## 2. 主な機能

- **ユーザー管理**:
  - サインアップ、ログイン、ログアウト機能
- **趣味の記録**:
  - 音楽・アーティスト情報の管理
  - 書籍・著者情報の管理
  - 映画情報の管理
  - トレーニングメニュー・実績の管理
  - 日記の管理
- **CRUD 操作**:
  - 各種趣味データに対する登録、閲覧、更新、削除機能

## 3. 技術スタック

### バックエンド

- Java 25
- Spring Boot 3.5.6
- Spring Security
- MyBatis 3.0.4
- Flyway 11.3.0

### フロントエンド

- Thymeleaf
- HTML / CSS
- JavaScript
- jQuery 3.7.1
- Bootstrap 5.3.3

### データベース

- MySQL 8.4.3
- H2 (テスト用)

### その他

- Docker / Docker Compose
- Lombok
- ModelMapper

## 4. 実行環境

### Docker Compose を使用する場合

1.  プロジェクトのルートディレクトリで以下のコマンドを実行します。
    ```bash
    docker-compose up -d --build
    ```
2.  Web ブラウザで `http://localhost` にアクセスします。

### ローカル環境で直接実行する場合

1.  MySQL データベースを起動します。
2.  `src/main/resources/application-local.yml` にローカルのデータベース接続情報を設定します。
3.  以下のコマンドを実行してアプリケーションを起動します。
    ```bash
    ./gradlew bootRun
    ```
4.  Web ブラウザで `http://localhost:8080` にアクセスします。

## 5.補足

### DB

#### Generate Version

```bash
date '+%Y%m%d.%H%M%S'
```

#### H2-DB ConsoleURL

`http://localhost:8080/h2-console/login.jsp`
