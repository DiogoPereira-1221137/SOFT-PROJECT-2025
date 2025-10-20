pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo '📥 A fazer checkout do repositório...'
                git branch: 'main', url: 'https://github.com/DiogoPereira-1221137/SOFT-PROJECT-2025.git'
            }
        }


        stage('Build') {
            steps {
                echo '🚀 A iniciar o build...'
                bat 'mvn clean compile'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {

                    withSonarQubeEnv(installationName: 'Sonarqube') {
                     bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar'

                }
            }
        }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Gerando artefato...'
                bat 'mvn package'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

    }

    post {
        always {
            echo '🏁 Pipeline terminada!'
        }
    }
}
