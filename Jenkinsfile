pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool 'Maven'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch : 'main', url : 'https://github.com/huda-elhirm/GestionBibliotheque.git'
            }
        }
        stage('Build') {
            steps {
                sh '${MAVEN_HOME}/bin/mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh '${MAVEN_HOME}/bin/mvn test'
            }
        }
        stage('Quality Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '${MAVEN_HOME}/bin/mvn sonar:sonar'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Déploiement simulé réussi'
            }
        }
    }
    post {
        success {
                try {
                emailext(
                    to: 'chimhouda@gmail.com',
                    subject: "Test Email from Pipeline",
                    body: "This is a test email from the Jenkins pipeline."
                )
                echo "Test email sent successfully."
                } catch (Exception e) {
                    echo "Failed to send test email: ${e.message}"
                }
        }
        failure {
            emailext ( to: 'chimhouda@gmail.com',
                subject: 'Build Failed',
                body: 'Le build a échoué.' )
        }
    }
}
