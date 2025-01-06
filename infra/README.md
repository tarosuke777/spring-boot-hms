## はじめに

HMS を AWS で動かす場合の構成 及び 作成時の注意事項を記載。

## 構成

| CodePipeline                                            | Infra                                     |
| ------------------------------------------------------- | ----------------------------------------- |
| ![aws-codepipeline.drawio](aws-codepipeline.drawio.svg) | ![aws-infra.drawio](aws-infra.drawio.svg) |

## 初回作成手順

1.aws-codePipleline.yml を実行
2.Connection が保留状態のため、手動で承認 3.ビルドステージまで実行
4.aws-infra.yml を実行

## 注意事項

### 初回作成時

1.aws-codePipleline の Deploy で ECS を参照する為、エラーとなる

### 削除時

1.PublicRepository は削除エラーになるため手動で削除が必要
2.S3::Bucket は削除エラーになるため手動で削除が必要

### その他

1.buildspec.yml 内に ECS のタスク名があるため、タスク名を変更時は修正が必要
