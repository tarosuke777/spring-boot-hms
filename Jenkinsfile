pipeline {
    agent any

    stages {
        stage('init') {
            steps {
                echo 'Forcibly cleaning up old root-owned artifacts...'
                sh 'sudo rm -rf build'
                
                echo 'Creating build directory...'
                sh 'sudo mkdir build'
            }
        }

        stage('Build & Test & Report') {
            steps {
                script {
                    try{
                        echo 'Building builder inside Docker...'
                        sh 'sudo docker compose build builder'

                        echo 'Running builder inside Docker...'
                        sh 'sudo docker compose run --name jenkins-builder-container builder ./gradlew build -x spotlessCheck'
                    } finally {
                        echo 'Copying build artifacts from builder container...'
                        sh 'sudo docker cp jenkins-builder-container:/app/build .'
                        
                        echo 'Cleaning up builder container...'
                        sh 'sudo docker rm -f jenkins-builder-container || true'
                    }
                }
            }
        }

        stage('Create Docker Images') {
            steps {
                echo 'Building Docker Compose services and running tests inside Docker...'
                sh 'sudo docker compose build hms-ap'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Stopping and removing old containers...'
                sh 'sudo docker compose down hms-ap'
                
                echo 'Starting new containers...'
                sh 'sudo docker compose up -d hms-ap'
            }
        }
    }

    post {
        // ビルド成功時に実行
        success {
            echo 'Build succeeded! Analyzing JaCoCo report and sending notification...'
            script {

                echo 'Publishing JaCoCo Report...'
                publishHTML([
                    allowMissing: true, 
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report',
                    reportTitles: ''
                ])

                def coverageText = ""

                try {
                    def csvPath = "build/reports/jacoco/test/jacocoTestReport.csv"
                    if (fileExists(csvPath)) {
                        def covPct = sh(script: 'awk -F, \'NR > 1 {missed+=$4; covered+=$5} END {if (missed+covered > 0) printf "%.0f%%", (covered/(missed+covered))*100; else print "0%"}\' ' + csvPath, returnStdout: true).trim()

                        coverageText = "\n📊 JaCoCoカバレッジレポート:\n・命令カバレッジ: ${covPct}\n🔗 レポート詳細: ${env.BUILD_URL}JaCoCo_20Report/"
                    } else {
                        coverageText = "\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                    }
                } catch (Exception e) {
                    echo "エラー: ${e.message}"
                    coverageText = "\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
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