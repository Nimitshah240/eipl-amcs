FROM jenkins/jenkins:lts

USER root

RUN dpkg --add-architecture i386 && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        wine \
        wine32 \
        wine64 \
        xvfb && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

USER jenkins