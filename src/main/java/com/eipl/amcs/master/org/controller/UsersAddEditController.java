package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.org.task.UserSaveTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class UsersAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtMobile;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClose;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private User user;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setUser(User user) {
        this.user = user;
        if (this.user != null) {
                txtUsername.setText(this.user.getUsername());
                txtUsername.setDisable(true);
                if (this.user.getName() != null) txtName.setText(this.user.getName());
                if (this.user.getMobileNo() != null) txtMobile.setText(this.user.getMobileNo());
                if (this.user.getPassword() != null) txtPassword.setText(this.user.getPassword());
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        btnUpdate.setOnAction(e -> validateAndSave());
        btnClose.setOnAction(e -> stage.close());
    }

    private void validateAndSave() {
        if (user == null) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("error.occurred"), "No user is selected for editing.");
            alert.createAlert();
            return;
        }

        String newName = txtName.getText();
        if (newName == null || newName.trim().isEmpty()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("names"), "Name Can Not Be Empty.");
            alert.createAlert();
            return;
        }
        if (!newName.matches("^[a-zA-Z\\s.]+$")) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("names"), "Name contains invalid characters.");
            alert.createAlert();
            return;
        }
        user.setName(newName);

        String newMobile = txtMobile.getText();
        if (newMobile == null || newMobile.trim().isEmpty()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("mobile"), "Mobile Number Cannot Be Empty.");
            alert.createAlert();
            return;
        }
        if (!newMobile.matches("\\d{10}")) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("mobile"), "Mobile Number must be 10 digits.");
            alert.createAlert();
            return;
        }
        user.setMobileNo(newMobile);

        String newPassword = txtPassword.getText();
        if (newPassword == null || newPassword.trim().isEmpty()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("passwords"), "Password Cannot Be Empty.");
            alert.createAlert();
            return;
        }
        user.setPassword(newPassword);

        updateData();
    }

    @Override
    public void updateData() {
        var task = new UserSaveTask(user);
        task.setOnSucceeded(e -> {
            try {
                task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("password"),
                        resourceBundle.getString("record.update.successful"));
                alert.createAlert();
                callback.reloadData(true);
                stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("password"),
                        resourceBundle.getString("error.occurred"));
                alert.createAlert();
            }
        });
        new Thread(task).start();
    }
}
