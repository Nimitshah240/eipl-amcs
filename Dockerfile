FROM jenkins/jenkins:lts

USER root

# Install Wine (32+64 bit) and Xvfb only - no Inno Setup needed
RUN dpkg --add-architecture i386 && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        wget \
        gnupg \
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

USER jenkins