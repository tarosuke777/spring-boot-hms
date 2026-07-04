pipeline {
    agent any

    stages {       
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
                // 1. デフォルトメッセージを用意
                def coverageText = ""
                
                // 2. JaCoCoのアクションからカバレッジの数値を動的に取得
                def jacocoAction = currentBuild.rawBuild.getAction(hudson.plugins.jacoco.JacocoBuildAction.class)
                
                if (jacocoAction != null) {
                    def instructionCov = jacocoAction.getInstructionCoverage().getPercentage()
                    def branchCov = jacocoAction.getBranchCoverage().getPercentage()
                    
                    coverageText = "\\n📊 JaCoCoカバレッジレポート:\\n・命令カバレッジ: ${instructionCov}%\\n・分岐カバレッジ: ${branchCov}%"
                } else {
                    coverageText = "\\n⚠️ JaCoCoレポートが見つかりませんでした。"
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