package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.model.Permission;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.utils.AppConstant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FooterBarController implements MyInitialization, PopupCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(FooterBarController.class);
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    @FXML
    AnchorPane root;
    @FXML
    private Label lblFinancialYear, lblUserName, lbltiming, lblVersion, lblIp, lblNetConnection;
    private ResourceBundle resourceBundle;
    private List<Permission> permissions;
    private Map<Permission, Map<Permission, List<Permission>>> menu;
    public DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadControls();
        checkConnection();
        startClock();
        lblVersion.setText("Version: " + AppConstant.versionNo + " - " + LocalDate.now());

        String baseUrl = MainApp.getProperty("baseurl", "");
        try {
            if (baseUrl != null && !baseUrl.isEmpty()) {
                URI uri = new URI(baseUrl);
                lblIp.setText(uri.getHost());
            } else {
                lblIp.setText("");
            }
        } catch (Exception e) {
            lblIp.setText(baseUrl);
        }
    }

    @Override
    public void loadControls() {
        lbltiming.setText(LocalDateTime.now().format(formatter1));
        if (lblUserName != null) lblUserName.setText(MainApp.user.getUsername());
        try {
            if (lblFinancialYear != null) lblFinancialYear.setText(MainApp.getFinancialYear().toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void checkConnection() {
        Thread networkThread = new Thread(() -> {
            while (true) {
                boolean connected = isInternetAvailable();
                Platform.runLater(() -> {
                    if (connected) {
                        lblNetConnection.setText("Connected");
                        lblNetConnection.setTextFill(Color.GREEN);
                    } else {
                        lblNetConnection.setText("Disconnected");
                        lblNetConnection.setTextFill(Color.RED);
                    }
                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        networkThread.setDaemon(true);
        networkThread.start();
    }

    public void startClock() {
        Thread clockThread = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    lbltiming.setText(LocalDateTime.now().format(formatter1));
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        clockThread.setDaemon(true);
        clockThread.start();
    }

    private boolean isInternetAvailable() {
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}