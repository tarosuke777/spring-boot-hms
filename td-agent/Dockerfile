# td-agent の公式イメージをベースにする
FROM fluent/td-agent:v4
USER root

# 必要なプラグインをインストール
RUN td-agent-gem install fluent-plugin-slack fluent-plugin-datadog

# デフォルトのユーザーに戻す
USER td-agent