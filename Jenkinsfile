pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Pulling latest code from your repository...'
                checkout scm
            }
        }

        stage('Maven Compile & Package') {
            steps {
                echo 'Building the application JAR...'
                sh 'mvn clean package -DskipTests'
            }
        }
    }

    post {
        success {
            echo "🏁 Success! The JAR file has been built inside the target/ folder."
        }
        failure {
            echo "❌ Build failed. Please verify the log output above."
        }
    }
}