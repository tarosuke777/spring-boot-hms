# td-agent の公式イメージをベースにする
FROM fluent/fluentd:v1.16-debian-td-agent-1.0
USER root

# 必要なプラグインをインストール
RUN td-agent-gem install fluent-plugin-slack fluent-plugin-datadog

# デフォルトのユーザーに戻す
USER td-agent