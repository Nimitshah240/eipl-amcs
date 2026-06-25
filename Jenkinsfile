pipeline {
    // 1. Force this pipeline to run natively on your laptop instead of inside the Linux container
    agent { label 'windows-build' }

    parameters {
        string(name: 'APP_VERSION', defaultValue: '1.3', description: 'Enter the version number for this build')
    }

    environment {
        // 2. Swapped to actual physical Windows paths since we are running natively
        STAGING_APP_DIR = 'N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application'
        ISS_FILE_PATH   = 'N:\\Jenkins\\BANAS - SETUP\\setup-1.4.iss'
        INNO_COMPILER   = 'C:\\Program Files (x86)\\Inno Setup 6\\ISCC.exe'
    }

    stages {
        stage('Maven Compile & Package') {
            steps {
                echo 'Executing Native Windows Maven Build...'
                // 3. Changed 'sh' to 'bat' for native execution
                bat 'mvn clean package'
            }
        }

        stage('Stage Core Executables') {
            steps {
                echo 'Updating Application JAR and library dependencies...'
                // 4. Converted Linux commands (rm, cp) to Windows Batch equivalents
                bat """
                    @echo off
                    rem Clear out older versioned JARs
                    if exist "${env.STAGING_APP_DIR}\\eipl-amcs-merge-*.jar" del /f /q "${env.STAGING_APP_DIR}\\eipl-amcs-merge-*.jar"

                    rem Copy and rename new versioned JAR
                    copy "target\\eipl-amcs-merge-*.jar" "${env.STAGING_APP_DIR}\\eipl-amcs-merge-${params.APP_VERSION}.jar"

                    rem Sync dependency lib folder completely
                    if not exist "${env.STAGING_APP_DIR}\\lib" mkdir "${env.STAGING_APP_DIR}\\lib"
                    del /f /q "${env.STAGING_APP_DIR}\\lib\\*"
                    xcopy /y /e "target\\lib\\*" "${env.STAGING_APP_DIR}\\lib\\"
                """
            }
        }

        stage('Configure Batch Script') {
            steps {
                echo 'Injecting dynamic execution variables into start.bat...'
                bat """
                    @echo off
                    echo java -jar eipl-amcs-merge-${params.APP_VERSION}.jar > "${env.STAGING_APP_DIR}\\start.bat"
                """
            }
        }

        stage('Sync Repository Resources') {
            steps {
                echo 'Syncing reports, messages, and collection folders...'
                bat """
                    @echo off
                    xcopy /y /e "src\\main\\resources\\resources\\report\\milkcollection\\*" "${env.STAGING_APP_DIR}\\resources\\report\\milkcollection\\"
                    xcopy /y /e "src\\main\\resources\\resources\\messages\\*" "${env.STAGING_APP_DIR}\\resources\\messages\\"
                    xcopy /y /e "src\\main\\resources\\resources\\collection\\*" "${env.STAGING_APP_DIR}\\resources\\collection\\"
                """
            }
        }

        stage('Compile Inno Setup Installer') {
            steps {
                echo 'Triggering Local Native Inno Setup Compiler...'
                // 5. Run your laptop's real Inno Setup directly without Wine or Xvfb!
                bat """
                    "${env.INNO_COMPILER}" /DMyAppVersion="${params.APP_VERSION}" "${env.ISS_FILE_PATH}"
                """
            }
        }
    }

    post {
        success {
            echo "🏁 Success! The installation setup file is ready natively in your N:/Jenkins/Output folder."
        }
        failure {
            echo "❌ Pipeline failed. Check console outputs."
        }
    }
}