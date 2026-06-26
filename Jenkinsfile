pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git branch: 'dev_nimit',
                    url: 'https://github.com/Nimitshah240/eipl-amcs.git'
            }
        }

        stage('Environment') {
            steps {
                sh '''
                    java -version
                    mvn -version
                    git --version
                '''
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
    }
}