FROM jenkins/jenkins:lts

USER root

# Install dependencies + Wine (32+64 bit)
RUN dpkg --add-architecture i386 && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        gnupg \
        software-properties-common \
        wine \
        wine32 \
        wine64 \
        winbind \
        xvfb && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Install Azul Zulu JDK 11
RUN wget -qO /tmp/zulu11.tar.gz \
    https://cdn.azul.com/zulu/bin/zulu11.0.30-ca-jdk11.0.30-linux_x64.tar.gz && \
    mkdir -p /opt/zulu11 && \
    tar -xzf /tmp/zulu11.tar.gz -C /opt/zulu11 --strip-components=1 && \
    rm /tmp/zulu11.tar.gz

ENV JAVA_HOME=/opt/zulu11
ENV PATH="$JAVA_HOME/bin:$PATH"

# Install Maven
RUN wget -qO /tmp/maven.tar.gz \
    https://downloads.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    mkdir -p /opt/maven && \
    tar -xzf /tmp/maven.tar.gz -C /opt/maven --strip-components=1 && \
    rm /tmp/maven.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH="$MAVEN_HOME/bin:$PATH"

# Install Inno Setup 6 via Wine (silent)
RUN wget -qO /tmp/innosetup.exe \
    https://files.jrsoftware.org/is/6/innosetup-6.3.3.exe && \
    WINEDEBUG=-all DISPLAY=:99 Xvfb :99 -screen 0 1024x768x16 & \
    sleep 3 && \
    wine /tmp/innosetup.exe /VERYSILENT /SUPPRESSMSGBOXES /NORESTART /SP- && \
    rm /tmp/innosetup.exe

USER jenkins