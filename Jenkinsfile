pipeline {
    agent any // Bypasses the windows agent requirement completely!

    parameters {
        string(name: 'APP_VERSION', defaultValue: '1.3', description: 'Enter the version number for this build')
    }

    environment {
        STAGING_APP_DIR = '/mnt/jenkins/BANAS - SETUP/eipl-amcs/application'
        ISS_FILE_PATH   = '/mnt/jenkins/BANAS - SETUP/setup-1.4.iss'

        // Use portable build tools inside the local workspace
        TOOL_DIR        = "${WORKSPACE}/build_tools"
        JAVA_HOME       = "${WORKSPACE}/build_tools/zulu11"
        M2_HOME         = "${WORKSPACE}/build_tools/maven"
        PATH            = "${env.M2_HOME}/bin:${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Setup Build Tools') {
            steps {
                echo 'Provisioning portable Zulu 11 and Apache Maven binaries...'
                sh '''
                    mkdir -p "${TOOL_DIR}"
                    cd "${TOOL_DIR}"

                    if [ ! -d "zulu11" ]; then
                        echo "Downloading Azul Zulu 11..."
                        curl -sL "https://cdn.azul.com/zulu/bin/zulu11.76.21-ca-jdk11.0.25-linux_x64.tar.gz" -o zulu.tar.gz
                        tar -xzf zulu.tar.gz
                        mv zulu11.* zulu11
                        rm zulu.tar.gz
                    fi

                    if [ ! -d "maven" ]; then
                        echo "Downloading Apache Maven..."
                        curl -sL "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz" -o maven.tar.gz
                        tar -xzf maven.tar.gz
                        mv apache-maven-3.9.6 maven
                        rm maven.tar.gz
                    fi
                '''
            }
        }

        stage('Maven Compile & Package') {
            steps {
                echo 'Executing Verification & Compiling Java Application...'
                sh '''
                    java -version
                    mvn -version
                    mvn clean package
                '''
            }
        }

        stage('Stage Core Executables') {
            steps {
                echo 'Updating Application JAR and library dependencies...'
                sh """
                    rm -f "${env.STAGING_APP_DIR}"/eipl-amcs-merge-*.jar
                    cp target/eipl-amcs-merge-*.jar "${env.STAGING_APP_DIR}/eipl-amcs-merge-${params.APP_VERSION}.jar"
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
                echo 'Syncing reports, messages, and collection folders...'
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
                        fi
                    done
                """
            }
        }

        stage('Compile Inno Setup Installer') {
            steps {
                echo 'Triggering pre-extracted Inno Setup compiler from your N drive mount...'
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
            echo "❌ Pipeline failed. Check build logs."
        }
    }
}