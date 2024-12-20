pipeline {
    agent any
    
    environment {
        EMAIL_TO = 'chimhouda@gmail.com'
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
                emailext body: "This is a test email from the Jenkins pipeline.",
                    subject: "Test Email from Pipeline",
                    to: '${EMAIL_TO}'
                    
                    
                
        }
        failure {
            emailext  body: 'Le build a échoué.',
                subject: 'Build Failed',
                to: '${EMAIL_TO}'
                
        }
    }
}
