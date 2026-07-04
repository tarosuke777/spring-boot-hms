pipeline {
    agent any

    stages {
        stage('Test & Analyze') {
            steps {
                echo 'Running tests and generating JaCoCo report...'

                sh 'chmod +x gradlew'

                // テストを実行してレポートを生成
                sh './gradlew clean test jacocoTestReport build'

                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report',
                    reportTitles: ''
                ])

            }
        }

        stage('Prepare Docker and Deploy') {
            steps {
                echo 'Building Docker Compose services...'
                sh 'sudo docker compose build'
                
                echo 'Stopping and removing old containers...'
                sh 'sudo docker compose down'
                
                echo 'Starting new containers...'
                sh 'sudo docker compose up -d'
            }
        }
    }

    post {
        // ビルド成功時に実行
        success {
            echo 'Build succeeded! Analyzing JaCoCo report and sending notification...'
            script {
                def coverageText = ""
                
                try {
                    // JaCoCoのindex.htmlの「Total」行からパーセンテージを抽出するシェル芸
                    def htmlPath = "build/reports/jacoco/test/html/index.html"
                    if (fileExists(htmlPath)) {
                        // 最初のパーセンテージ（通常これが全体の命令カバレッジ）を取得
                        def covPct = sh(script: "grep -oE '[0-9]+%' ${htmlPath} | head -n 1", returnStdout: true).trim()
                        coverageText = "\\n📊 JaCoCoカバレッジレポート:\\n・命令カバレッジ: ${covPct}\\n🔗 レポート詳細: ${env.BUILD_URL}HTML_20Report/"
                    } else {
                        coverageText = "\\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                    }
                } catch (Exception e) {
                    // パースに失敗した場合は安全にリンクのみにする
                    coverageText = "\\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                }
                
                // 3. 取得したカバレッジ情報を結合してWebhookを送信
                sh """
                    curl -X POST -H "Content-Type: application/json" \
                    -d '{"content":"✅ ビルド成功: ${env.JOB_NAME} #${env.BUILD_NUMBER}${coverageText}", "channelId":"1"}' \
                    http://hc-ap:8080/hc/ap/messages/webhook
                """
            }
        }
        
        // ビルド失敗時に実行
        failure {
            echo 'Build failed! Sending notification...'
            sh """
                curl -X POST -H "Content-Type: application/json" \
                -d '{"content":"❌ ビルド失敗: ${env.JOB_NAME} #${env.BUILD_NUMBER}", "channelId":"1"}' \
                http://hc-ap:8080/hc/ap/messages/webhook
            """
        }
    }
}