pipeline {
    agent any

    tools {
        jdk 'Zulu-11'
        maven 'Maven-3.9'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Prepare Setup Files') {
            steps {
                bat '''
                echo Cleaning old files...

                if exist "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\resources" rmdir /S /Q "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\resources"
                if exist "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\lib" rmdir /S /Q "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\lib"

                if exist "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\start.bat" del /F /Q "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\start.bat"

                if exist "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\eipl-amcs-merge-1.3.jar" del /F /Q "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\eipl-amcs-merge-1.3.jar"

                mkdir "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\resources"

                echo Copying jar...
                copy "target\\eipl-amcs-merge-1.3.jar" "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\"

                echo Copying libraries...
                xcopy "target\\lib" "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\lib" /E /I /Y

                echo Copying resources...
                xcopy "resources\\collection" "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\resources\\collection" /E /I /Y
                xcopy "resources\\messages" "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\resources\\messages" /E /I /Y
                xcopy "resources\\report" "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\resources\\report" /E /I /Y

                echo Creating start.bat...

                (
                echo @echo off
                echo cd /d %%~dp0
                echo java -jar eipl-amcs-merge-1.3.jar
                ) > "N:\\Jenkins\\BANAS - SETUP\\eipl-amcs\\application\\start.bat"

                '''
            }
        }
        stage('Create Setup') {
            steps {
                bat '''
                "C:\\Program Files (x86)\\Inno Setup 6\\ISCC.exe" "N:\\Jenkins\\BANAS - SETUP\\setup.iss"
                '''
            }
        }
    }
}