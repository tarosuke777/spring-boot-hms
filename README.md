# GIT
### Commit Message Template
```
vi ~/.gitcommit_template
git config --global commit.template ~/.gitcommit_template
```

```
### Commit Message
# ==== Commit Messages ====

# ==== Commit Messages(Template) ====
# <Prefix> <Title>
# 例) feat ログイン機能を実装

# ==== Prefix Emoji ====
# feat: 機能の追加や変更
# docs: ドキュメントの更新
# fix: バグ修正
# style: フォーマット、書式修正
# refactor: リファクタリング
# test: テストコードの追加や修正
# chore: ビルドツールの導入や依存関係を更新など
```

# DB
### Generate Version 
date '+%Y%m%d.%H%M%S'

### H2-DB ConsoleURL
http://localhost:8080/h2-console/login.jsp