pipeline {
    agent any

    stages {
        stage('Test & Analyze') {
            steps {
                echo 'Running tests and generating JaCoCo report...'

                sh 'chmod +x gradlew'

                // テストを実行してレポートを生成
                sh './gradlew clean test jacocoTestReport'
                
                // JenkinsにJaCoCoレポートを認識させる（これで環境変数がセットされます）
                jacoco(
                    execPattern: '**/build/jacoco/*.exec',
                    classPattern: '**/build/classes/java/main',
                    sourcePattern: '**/src/main/java'
                )
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
                def instructionCov = env.JACOCO_INSTRUCTION_COVERAGE
                def branchCov = env.JACOCO_BRANCH_COVERAGE
                def coverageText = ""
                
                // 環境変数が存在する場合のみテキストを作成
                if (instructionCov != null && branchCov != null) {
                    coverageText = "\\n📊 JaCoCoカバレッジレポート:\\n・命令カバレッジ: ${instructionCov}%\\n・分岐カバレッジ: ${branchCov}%\\n🔗 レポート詳細: ${env.BUILD_URL}jacoco/"
                } else {
                    // もし環境変数から取れない場合はリンクのみ
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