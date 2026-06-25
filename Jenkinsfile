pipeline {
    agent any

    environment {
        APP_DIR        = '/mnt/jenkins/BANAS - SETUP/eipl-amcs'
        OUTPUT_DIR     = '/mnt/jenkins/Output'
        ISS_FILE       = '/mnt/jenkins/BANAS - SETUP/eipl-amcs/setup.iss'
        ISCC           = 'wine "/root/.wine/drive_c/Program Files (x86)/Inno Setup 6/ISCC.exe"'
        DISPLAY        = ':99'
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
                echo 'Copying built JAR into the installer application folder...'
                sh """
                    mkdir -p "${APP_DIR}/application"
                    cp target/*.jar "${APP_DIR}/application/"
                """
            }
        }

        stage('Create EXE Installer') {
            steps {
                echo 'Running Inno Setup to build the EXE...'
                sh """
                    mkdir -p "${OUTPUT_DIR}"
                    Xvfb :99 -screen 0 1024x768x16 &
                    sleep 2
                    WINEDEBUG=-all ${ISCC} "Z:\\\\mnt\\\\jenkins\\\\BANAS - SETUP\\\\eipl-amcs\\\\setup.iss"
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