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
    private StringBuilder errorMsg = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            btnUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        btnUpdate.setOnAction(e -> validateAndSave());
        btnClose.setOnAction(e -> stage.close());
    }

    @Override
    public void loadControls() {
        txtUsername.setText(this.user.getUsername());
        txtUsername.setDisable(true);
        if (this.user.getName() != null) txtName.setText(this.user.getName());
        if (this.user.getMobileNo() != null) txtMobile.setText(this.user.getMobileNo());
        if (this.user.getPassword() != null) txtPassword.setText(this.user.getPassword());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("user"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.user != null) {
                user = setValuesInObject();
                updateData();
            }
        }
    }

    private User setValuesInObject() {
        user.setName(txtName.getText());
        user.setMobileNo(txtMobile.getText());
        user.setPassword(txtPassword.getText());
        return user;
    }

    private boolean validate() {
        if (user == null) {
            errorMsg.append(resourceBundle.getString("No.user.is.selected.for.editing.") + "\n");
        }

        String newName = txtName.getText();
        if (newName == null || newName.trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("name.cannot.be.empty.") + "\n");
        } else if (!newName.matches("^[a-zA-Z\\s.]+$")) {
            errorMsg.append(resourceBundle.getString("Name.contains.invalid.characters.")+"\n");
        }

        String newMobile = txtMobile.getText();
        if (newMobile == null || newMobile.trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("mobile.cannot.be.empty.") + "\n");
        } else if (!newMobile.matches("\\d{10}")) {
            errorMsg.append(resourceBundle.getString("Mobile.Number.must.be.10.digits.")+"\n");
        }

        String newPassword = txtPassword.getText();
        if (newPassword == null || newPassword.trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("Password.Cannot.Be.Empty.")+"\n");
        }

        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {

    }

    @Override
    public void updateData() {
        var task = new UserSaveTask(user);
        task.setOnSucceeded(e -> {
            try {
                task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("user"),
                        resourceBundle.getString("record.update.successful"));
                alert.createAlert();
                callback.reloadData(true);
                stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("user"),
                        resourceBundle.getString("error.occurred"));
                alert.createAlert();
            }
        });
        new Thread(task).start();
    }
}
