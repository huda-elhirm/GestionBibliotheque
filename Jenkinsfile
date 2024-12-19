pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool 'Maven'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/huda-elhirm/GestionBibliotheque.git'
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
            emailext to: 'houdaelhirm@gmail.com',
                subject: 'Build Success',
                body: 'Le build a été complété avec succès.'
        }
        failure {
            emailext to: 'houdaelhirm@gmail.com',
                subject: 'Build Failed',
                body: 'Le build a échoué.'
        }
    }
}
