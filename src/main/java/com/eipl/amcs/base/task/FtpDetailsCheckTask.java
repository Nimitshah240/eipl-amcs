package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.FtpRequestPayload;
import com.eipl.amcs.network.RealTimeResponse;
import javafx.concurrent.Task;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;


public class FtpDetailsCheckTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpDetailsCheckTask.class);

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = "http://amcsapp.emilkpro.in/webservice/eipl/v1/eipl-app/verify-identity";

            FtpRequestPayload payload = new FtpRequestPayload(null, "", "AMULAMCS", "", "", "");
            ResponseEntity<RealTimeResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(payload), RealTimeResponse.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;

            RealTimeResponse respBody = response.getBody();

            if (respBody.getData() != null && !respBody.getData().isEmpty()) {
                String ftpHost = String.valueOf(respBody.getData().get("ftpHost"));
                String ftpPort = String.valueOf(respBody.getData().get("ftpPort"));
                String ftpUsername = String.valueOf(respBody.getData().get("ftpUsername"));
                String ftpPassword = String.valueOf(respBody.getData().get("ftpPassword"));
                System.err.println("connection....");

                FTPClient ftp = new FTPClient();
                try {
                    File folder = new File(MainApp.getProperty("backuppath", null).replace(" ", ""));
                    File latestZipFile = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                            .filter(file -> file.isFile() && file.getName().toLowerCase().endsWith(".zip"))
                            .max(Comparator.comparingLong(File::lastModified))
                            .orElse(null);
                    String localFilePath = MainApp.getProperty("backuppath", null).replace(" ", "") + "\\" + latestZipFile.getName();
                    System.err.println("path....");


                    ftp.connect(ftpHost, Integer.parseInt(ftpPort));
                    System.err.println("connect....");
                    int replyCode = ftp.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(replyCode)) {
                        System.err.println("FTP server refused connection. Exiting.");
                    }

                    System.err.println("reply....");
                    if (ftp.login(ftpUsername, ftpPassword)) {
                        System.err.println("login....");
                        ftp.enterLocalPassiveMode();
                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                        boolean fileExists = Arrays.stream(ftp.listFiles()).anyMatch(e -> e.getName().substring(0, 15).equalsIgnoreCase(latestZipFile.getName().substring(0, 7)));
                        if (!fileExists) {
                            System.err.println("exists....");
                            File localFile = new File(localFilePath);
                            try (FileInputStream inputStream = new FileInputStream(localFile)) {
                                if (ftp.storeFile(localFile.getName(), inputStream)) {
                                } else {
                                    System.err.println("Failed to upload file. Check FTP permissions and try again.");
                                }
                            }
                        } else {
                        }
                    } else {
                        System.err.println("FTP login failed. Check username and password.");
                    }
                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                } finally {
                    try {
                        if (ftp.isConnected()) {
                            ftp.logout();
                            ftp.disconnect();
                        }
                    } catch (IOException e) {
                        System.err.println("Error closing FTP connection: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Map.of();
    }
}
