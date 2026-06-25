pipeline {
    agent any

    parameters {
        string(name: 'APP_VERSION', defaultValue: '1.3', description: 'Enter the version number for this build')
    }

    environment {
        // Mapped Linux paths inside the Docker container
        STAGING_APP_DIR = '/mnt/jenkins/BANAS - SETUP/eipl-amcs/application'
        ISS_FILE_PATH   = '/mnt/jenkins/BANAS - SETUP/setup-1.4.iss'
    }

    stages {
        stage('Maven Compile & Package') {
            steps {
                echo 'Locating and running containerized Java/Maven tools...'
                sh '''
                    # Find exactly where apt-get installed Zulu 11 and Maven binaries
                    export JAVA_HOME=\$(readlink -f /usr/bin/java | sed "s:/bin/java::")
                    export M2_HOME=/usr/share/maven
                    export PATH="\$M2_HOME/bin:\$JAVA_HOME/bin:\$PATH"

                    # Print out locations to your build log console for validation
                    echo "Resolved JAVA_HOME: \$JAVA_HOME"
                    echo "Resolved M2_HOME: \$M2_HOME"

                    java -version
                    mvn -version

                    # Execute compilation execution loop
                    mvn clean package
                '''
            }
        }

        stage('Stage Core Executables') {
            steps {
                echo 'Updating Application JAR and library dependencies...'
                sh """
                    # 1. Clear out older versioned JARs
                    rm -f "${env.STAGING_APP_DIR}"/eipl-amcs-merge-*.jar

                    # 2. Copy and rename new versioned JAR
                    cp target/eipl-amcs-merge-*.jar "${env.STAGING_APP_DIR}/eipl-amcs-merge-${params.APP_VERSION}.jar"

                    # 3. Completely sync the dependency lib folder
                    mkdir -p "${env.STAGING_APP_DIR}/lib"
                    rm -rf "${env.STAGING_APP_DIR}/lib/*"
                    cp -r target/lib/* "${env.STAGING_APP_DIR}/lib/"
                """
            }
        }

        stage('Configure Batch Script') {
            steps {
                echo 'Injecting dynamic execution variables into start.bat...'
                sh """
                    BAT_FILE="${env.STAGING_APP_DIR}/start.bat"
                    NEW_JAR="eipl-amcs-merge-${params.APP_VERSION}.jar"

                    if [ -f "\$BAT_FILE" ]; then
                        sed -i "s/java -jar eipl-amcs-merge-.*\\.jar/java -jar \$NEW_JAR/g" "\$BAT_FILE"
                    else
                        echo "java -jar \$NEW_JAR" > "\$BAT_FILE"
                    fi
                """
            }
        }

        stage('Sync Repository Resources') {
            steps {
                echo 'Syncing reports, messages, and collection folders from Git Workspace...'
                sh """
                    declare -a resources=(
                        "resources/report/milkcollection"
                        "resources/messages"
                        "resources/collection"
                    )

                    for subfolder in "\${resources[@]}"; do
                        SOURCE="src/main/resources/\$subfolder"
                        DESTINATION="${env.STAGING_APP_DIR}/\$subfolder"

                        if [ -d "\$SOURCE" ]; then
                            mkdir -p "\$DESTINATION"
                            rm -rf "\$DESTINATION"/*
                            cp -r "\$SOURCE"/* "\$DESTINATION/"
                        else
                            echo "Warning: Source folder missing in Git workspace: \$SOURCE"
                        fi
                    done
                """
            }
        }

        stage('Compile Inno Setup Installer') {
            steps {
                echo 'Triggering Inno Setup inside Wine environment...'
                sh """
                    iscc /DMyAppVersion="${params.APP_VERSION}" "${env.ISS_FILE_PATH}"
                """
            }
        }
    }

    post {
        success {
            echo "🏁 Success! The installation setup file is ready in your N:/Jenkins/Output folder."
        }
        failure {
            echo "❌ Pipeline failed. Check your file/folder structures or build logs above."
        }
    }
}