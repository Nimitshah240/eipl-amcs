pipeline {
    agent any

    environment {
        MAVEN_HOME = '/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven3'
        PATH       = "/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven3/bin:${env.PATH}"
        APP_DIR    = '/mnt/jenkins/BANAS - SETUP/eipl-amcs'
        OUTPUT_DIR = '/mnt/jenkins/Output'
        ISS_FILE   = 'Z:\\mnt\\jenkins\\BANAS - SETUP\\eipl-amcs\\setup.iss'
        ISCC       = '/mnt/innosetup/ISCC.exe'
        DISPLAY    = ':99'
        WINEDEBUG  = '-all'
        WINEPREFIX = '/root/.wine'
    }

    stages {

        stage('Verify Tools') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

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
                    sleep 3
                    DISPLAY=:99 wineboot --init
                    sleep 5
                    DISPLAY=:99 wine "${ISCC}" "${ISS_FILE}"
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