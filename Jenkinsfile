pipeline {
    agent any

    stages {
        stage('Docker Build & Extract Reports') {
            steps {
                echo 'Forcibly cleaning up old root-owned artifacts...'
                // root権限で生成されたbuildフォルダを強制的に削除
                sh 'sudo rm -rf build'

                echo 'Building Docker Compose services and running tests inside Docker...'
                                
                // 1. Docker Composeでビルド（この内部のマルチステージビルドでテストとJaCoCoが走ります）
                sh 'sudo docker compose build'

                echo 'Extracting JaCoCo reports from Docker builder stage...'
                script {
                    // 2. マルチステージビルドの "builder" ステージをターゲットに一時イメージを作成（キャッシュが効くので一瞬です）
                    sh 'sudo docker build --target builder -t hms-builder:tmp .'
                    
                    // 3. 一時コンテナを作成
                    sh 'sudo docker create --name tmp_reporter hms-builder:tmp'
                    
                    // 4. コンテナ内のレポートをJenkinsのワークスペースへコピー
                    sh 'sudo docker cp tmp_reporter:/app/build build'
                    
                    // 5. 後片付け（一時コンテナと一時イメージの削除）
                    sh 'sudo docker rm tmp_reporter'
                    sh 'sudo docker rmi hms-builder:tmp'
                    
                    // 6. Jenkinsがファイルを読み取れるように権限を調整
                    sh 'sudo chmod -R 755 build'
                }

                // 7. コピーしてきたレポートを Jenkins にパブリッシュ
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

        stage('Deploy') {
            steps {
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
                        coverageText = "\\n📊 JaCoCoカバレッジレポート:\\n・命令カバレッジ: ${covPct}\\n🔗 レポート詳細: ${env.BUILD_URL}JaCoCo_20Report/"
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