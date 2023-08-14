pipeline (
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm // 코드 체크아웃 (코드가 scm에 등록되어 있는지 확인하는 단계)
            }
        }

        stage('Build') {
            steps {
                // Docker Image Build
                script {
                    docker.build('flint-back-dockerfile:latest', '.')
                }
            }
        }

        stage('Run') {
            steps{
                // Docker 컨테이너 실행
                script {
                    def dockerImage = docker.image(flint-back-dockerfile:latest)

                    dockerImage.run('p 8080:80 -d')
                }
            }
        }
    }
    post {
        always {
            def dockerImage = docker.image('flint-back-dockerfile:latest')

            dockerImage.stop()
            dockerImage.remove(force: true)
        }
    }
)