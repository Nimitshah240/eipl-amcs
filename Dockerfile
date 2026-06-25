FROM jenkins/jenkins:lts

USER root

# Install prerequisites, add Azul's GPG key and repository
RUN apt-get update && apt-get install -y gnupg curl apt-transport-https && \
    curl -s https://assets.azul.com/files/azul-repo.key | gpg --dearmor -o /usr/share/keyrings/azul.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/azul.gpg] https://repos.azul.com/zulu/deb stable main" | tee /etc/apt/sources.list.y/azul.list

# Install Azul Zulu OpenJDK 11, Maven, Wine, and clean up cache
RUN apt-get update && apt-get install -y \
    zulu11-jdk \
    maven \
    wine \
    wine64 \
    && apt-get clean

# Download and install Inno Setup 6 via Wine quietly
RUN mkdir -p /opt/innosetup && \
    curl -SL "https://files.jrsoftware.org/is/6/innosetup-6.2.2.exe" -o /tmp/is.exe && \
    wine /tmp/is.exe /VERYSILENT /SUPPRESSMSGBOXES /NORESTART /DIR="C:\InnoSetup" && \
    rm /tmp/is.exe

# Set up an alias to run ISCC simply inside Linux
RUN echo '#!/bin/bash\nwine "C:\\InnoSetup\\ISCC.exe" "$@"' > /usr/local/bin/iscc && \
    chmod +x /usr/local/bin/iscc

USER jenkins