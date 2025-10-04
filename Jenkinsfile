pipeline {
    agent any

    stages {
        stage('Build with Gradle') {
            steps {
                echo 'Granting execute permission to gradlew...'
                sh 'chmod +x gradlew'

                echo 'Building application with Gradle...'
                sh './gradlew clean build'
            }
        }
        
        stage('Prepare Docker and Deploy') { // ステージ名を変更
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
}