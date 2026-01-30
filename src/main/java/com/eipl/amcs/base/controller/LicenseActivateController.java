package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.base.task.IdentityLoadTask;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LicenseActivateController implements MyInitialization {

    @FXML
    private StackPane root;

    @FXML
    private TextField txtActivationKey, txtLicenceKey;

    @FXML
    private Button btnActivateKey, btnClose;

    private ResourceBundle resourceBundle;
    private Stage stage;
    private boolean success = false;
    private PopupCallback callback;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();

        btnActivateKey.setOnAction(event -> saveData());

        if (btnClose != null) {
            btnClose.setOnAction(e -> {
                this.success = false;
                closePopup();
            });
        }
    }

    @Override
    public void setupTable() {
        txtActivationKey.setPromptText("Enter Activation Key");
        txtLicenceKey.setPromptText("Enter 4 digit Pass Code");
    }

    @Override
    public void saveData() {
        String enteredActivationKey = txtActivationKey.getText();
        String enteredLicenseKey = txtLicenceKey.getText();

        if (isInvalid(enteredActivationKey) || isInvalid(enteredLicenseKey)) {
            showAlert("Activation", "Both keys are required.");
            return;
        }

        var task = new IdentityLoadTask();
        task.setOnSucceeded(e -> {
            try {
                Identity storedIdentity = task.get();
                processResult(storedIdentity, enteredActivationKey, enteredLicenseKey);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                showAlert("Error", "An unexpected error occurred.");
            }
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert("Database Error", "Failed to fetch identity from database.");
        });

        new Thread(task).start();
    }

    private void processResult(Identity identity, String enteredAct, String enteredLic) {
        if (identity != null) {
            boolean isValid = enteredAct.equalsIgnoreCase(identity.getToken()) &&
                    enteredLic.equalsIgnoreCase(identity.getXCol1());

            if (isValid) {
                this.success = true;
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                        resourceBundle.getString("activation.success"));
                alert.createAlert();
                closePopup();
            } else {
                showAlert("Error", "Invalid Activation/License Keys.");
            }
        } else {
            showAlert("Error", "No identity found in database.");
        }
    }

    private boolean isInvalid(String val) {
        return val == null || val.trim().isEmpty();
    }

    private void showAlert(String title, String content) {
        MyAlert alert = new ErrorAlert(MainApp.getStage(), title, content);
        alert.createAlert();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    private void closePopup() {
        this.callback.reloadData(this.success);
        if (stage != null) this.stage.close();
    }
}