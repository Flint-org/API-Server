pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm // 코드 체크아웃 (코드가 scm에 등록되어 있는지 확인하는 단계)
            }
        }

        stage('Project Build') {
            steps {
                // Docker Image Build
                sh './gradlew build'
            }
        }

        stage('Docker Build & Run') {
            steps {
                // Docker Image Build
                sh 'docker build -t flint-back-docker:CD'
                withCredentials([usernamePassword(credentialsId: 'back', passwordVariable: 'flint', usernameVariable: 'rlgus2738')]) {
                    sh 'docker login -u rlgus2738 -p tktktkfkd5!'
                }
                sh 'docker tag flint-back-docker:CD rlgus2738/flint-back-docker:CD'
                sh 'docker push rlgus2738/flint-back-docker:CD'
            }
        }
    }
    post {
        always {
            sh 'docker logout'
        }
    }
}