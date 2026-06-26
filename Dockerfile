FROM jenkins/jenkins:lts-jdk17

USER root

RUN apt-get update && \
    apt-get install -y \
        git \
        maven \
        wget \
        unzip && \
    apt-get clean

USER jenkins