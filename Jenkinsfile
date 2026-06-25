pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk   'Zulu11'
    }

    environment {
        APP_DIR    = '/mnt/jenkins/BANAS - SETUP/eipl-amcs'
        OUTPUT_DIR = '/mnt/jenkins/Output'
        ISS_FILE   = 'Z:\\mnt\\jenkins\\BANAS - SETUP\\eipl-amcs\\setup.iss'
        ISCC       = '/mnt/innosetup/ISCC.exe'
        DISPLAY    = ':99'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Pulling latest code...'
                checkout scm
            }
        }

        stage('Maven Compile & Package') {
            steps {
                echo 'Building the application JAR...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Copy JAR to Application Folder') {
            steps {
                echo 'Copying built JAR into installer application folder...'
                sh """
                    mkdir -p "${APP_DIR}/application"
                    cp target/*.jar "${APP_DIR}/application/"
                """
            }
        }

        stage('Create EXE Installer') {
            steps {
                echo 'Running Inno Setup via Wine to build the EXE...'
                sh """
                    mkdir -p "${OUTPUT_DIR}"
                    Xvfb :99 -screen 0 1024x768x16 &
                    sleep 2
                    WINEDEBUG=-all DISPLAY=:99 wine "${ISCC}" "${ISS_FILE}"
                """
            }
        }

    }

    post {
        success {
            echo "✅ EXE installer built successfully. Check ${OUTPUT_DIR} for the output."
        }
        failure {
            echo "❌ Build failed. Check the logs above."
        }
    }
}