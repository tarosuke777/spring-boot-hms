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
                        sh 'sudo docker compose run --name jenkins-builder-container builder ./gradlew build'
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

                // try {
                //     def xmlPath = "build/reports/jacoco/test/jacocoTestReport.xml"
                    
                //     if (fileExists(xmlPath)) {
                //         // XMLファイルを読み込んでパース
                //         def xmlText = readFile(xmlPath)
                //         def report = new XmlParser(false, false).parseText(xmlText)
                        
                //         // <counter type="INSTRUCTION"> を探す
                //         def instructionCounter = report.counter.find { it.'@type' == 'INSTRUCTION' }
                        
                //         if (instructionCounter) {
                //             double missed = instructionCounter.'@missed'.toDouble()
                //             double covered = instructionCounter.'@covered'.toDouble()
                //             double total = missed + covered
                            
                //             // カバレッジのパーセンテージを計算 (四捨五入して整数に)
                //             int coveragePct = total > 0 ? Math.round((covered / total) * 100) : 0
                            
                //             coverageText = "\n📊 JaCoCoカバレッジレポート:\n・命令カバレッジ: ${coveragePct}%\n🔗 レポート詳細: ${env.BUILD_URL}JaCoCo_20Report/"
                //         } else {
                //             coverageText = "\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                //         }
                //     } else {
                //         coverageText = "\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                //     }
                // } catch (Exception e) {
                //     echo "XMLパースエラー: ${e.message}"
                //     coverageText = "\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                // }

                // try {
                //     // JaCoCoのindex.htmlの「Total」行からパーセンテージを抽出するシェル芸
                //     def htmlPath = "build/reports/jacoco/test/html/index.html"
                //     if (fileExists(htmlPath)) {
                //         // 最初のパーセンテージ（通常これが全体の命令カバレッジ）を取得
                //         def covPct = sh(script: "grep -oE '[0-9]+%' ${htmlPath} | head -n 1", returnStdout: true).trim()
                //         coverageText = "\\n📊 JaCoCoカバレッジレポート:\\n・命令カバレッジ: ${covPct}\\n🔗 レポート詳細: ${env.BUILD_URL}JaCoCo_20Report/"
                //     } else {
                //         coverageText = "\\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                //     }
                // } catch (Exception e) {
                //     // パースに失敗した場合は安全にリンクのみにする
                //     coverageText = "\\n🔗 JaCoCoレポート詳細: ${env.BUILD_URL}jacoco/"
                // }
                
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