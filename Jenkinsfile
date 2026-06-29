pipeline {
    agent any

    tools {
        jdk 'Zulu-11'
        maven 'Maven-3.9'
    }

    stages {
        stage('Check Tools') {
            steps {
                bat 'java -version'
                bat 'javac -version'
                bat 'mvn -version'
                bat 'git --version'
            }
        }
    }
}