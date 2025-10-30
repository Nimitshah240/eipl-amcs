package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.task.IdentityTask;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.task.RateTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SplashController implements MyInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(SplashController.class);
    @FXML
    Label lbl;
    @FXML
    StackPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            File appProperty = new File("resources/app.properties");
            if (appProperty.exists()) //
                checkHealth();
            else {
                Platform.runLater(() -> {
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Activation.fxml")));
                });
            }
        } catch (Exception exception) {
            lbl.setText("An error occurred!");
        }
    }

    private void checkHealth() {
        lbl.setText("Please wait...");
        initializeIdentity();
    }

    private void initializeIdentity() {
        var task = new IdentityTask(MainApp.getProperty(AppConstant.Props.IDENTITY_DOCK, null),
                MainApp.getProperty(AppConstant.Props.IDENTITY_SOCIETY, null),
                MainApp.getProperty(AppConstant.Props.IDENTITY_UNION, null));
        task.setOnSucceeded(t -> {
            try {
                MainApp.identityDto = task.get();
                if (MainApp.identityDto == null)
                    lbl.setText("Society initialization error!");
                else {
                    RateTask rateTask = new RateTask();
                    rateTask.setOnSucceeded(e -> {
                    });
                    rateTask.setOnFailed(e -> {
                    });
                    new Thread(rateTask).start();
                    lbl.textProperty().bind(rateTask.messageProperty());
                    MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "ABC");
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
