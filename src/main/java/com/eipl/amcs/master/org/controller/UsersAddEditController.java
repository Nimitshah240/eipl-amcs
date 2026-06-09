package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.model.Role;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.model.UserRole;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.org.task.RoleLoadTask;
import com.eipl.amcs.master.org.task.UserRoleLoadTask;
import com.eipl.amcs.master.org.task.UserRoleSaveTask;
import com.eipl.amcs.master.org.task.UserSaveTask;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
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
    private E_ComboBox<Role> cboxRoleCode;
    @FXML
    private E_Button btnUpdate;
    @FXML
    private E_Button btnClose;
    private UserRole userRole;
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
        }
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
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

    public void initData() {
        if (this.user != null) {
            if (resourceBundle != null && btnUpdate != null) {
                btnUpdate.setText(resourceBundle.getString("update"));
            }
            loadControls();

            // This compiles cleanly using the overloaded user constructor
            var findRoleTask = new UserRoleLoadTask(this.user);

            findRoleTask.setOnSucceeded(e -> {
                List<UserRole> rolesList = findRoleTask.getValue();

                // Safely extracts the true persistent data row containing the DB Primary Key Code
                if (rolesList != null && !rolesList.isEmpty()) {
                    this.userRole = rolesList.get(0);
                } else {
                    this.userRole = null;
                }

                setupComboBox();
            });

            findRoleTask.setOnFailed(e -> {
                this.userRole = null;
                setupComboBox();
            });

            new Thread(findRoleTask).start();
        }
    }


    @Override
    public void loadControls() {
        if (this.user == null) return;

        txtUsername.setText(this.user.getUsername());
        txtUsername.setDisable(true);

        if (this.user.getName() != null) txtName.setText(this.user.getName());
        if (this.user.getMobileNo() != null) txtMobile.setText(this.user.getMobileNo());
        if (this.user.getPassword() != null) txtPassword.setText(this.user.getPassword());

        if (userRole != null && userRole.getRole() != null) {
            cboxRoleCode.getSelectionModel().select(userRole.getRole());
        }
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

    @Override
    public void setupComboBox() {
        cboxRoleCode.setConverter(new StringConverter<Role>() {
            @Override
            public String toString(Role role) {
                return (role != null) ? role.getName() : "";
            }

            @Override
            public Role fromString(String string) {
                return null;
            }
        });
        var roleTask = new RoleLoadTask();
        roleTask.setOnSucceeded(e -> {
            List<Role> rolesList = roleTask.getValue();
            cboxRoleCode.setItems(FXCollections.observableList(rolesList));
            if (userRole != null && userRole.getRole() != null) {
                rolesList.stream()
                        .filter(r -> r.getName().equals(userRole.getRole().getName()))
                        .findFirst()
                        .ifPresent(matchedRole -> cboxRoleCode.getSelectionModel().select(matchedRole));
            }
        });
        new Thread(roleTask).start();
    }


    private User setValuesInObject() {
        user.setName(txtName.getText());
        user.setMobileNo(txtMobile.getText());
        user.setPassword(txtPassword.getText());

        if (userRole == null) {
            userRole = new UserRole();
            userRole.setUser(this.user);
            if (this.user.getSociety() != null) userRole.setSociety(this.user.getSociety());
            if (this.user.getUnionCode() != null) userRole.setUnionCode(this.user.getUnionCode());
        }

        userRole.setRole(cboxRoleCode.getSelectionModel().getSelectedItem());
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
            errorMsg.append(resourceBundle.getString("Name.contains.invalid.characters.") + "\n");
        }

        String newMobile = txtMobile.getText();
        if (newMobile == null || newMobile.trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("mobile.cannot.be.empty.") + "\n");
        } else if (!newMobile.matches("\\d{10}")) {
            errorMsg.append(resourceBundle.getString("Mobile.Number.must.be.10.digits.") + "\n");
        }

        String newPassword = txtPassword.getText();
        if (newPassword == null || newPassword.trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("Password.Cannot.Be.Empty.") + "\n");
        }

        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {

    }

    @Override
    public void updateData() {
        var userTask = new UserSaveTask(user);
        userTask.setOnSucceeded(e -> {
            try {
                userTask.get();
                saveUserRoleSequentially();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                showGenericUserError();
            }
        });

        userTask.setOnFailed(e -> showGenericUserError());
        new Thread(userTask).start();
    }

    private void saveUserRoleSequentially() {
        if (userRole == null) {
            userRole = new UserRole();
            userRole.setUser(this.user);
            if (this.user.getSociety() != null) userRole.setSociety(this.user.getSociety());
            if (this.user.getUnionCode() != null) userRole.setUnionCode(this.user.getUnionCode());
        }

        Role selectedRole = cboxRoleCode.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("userroles"),
                    "Please select a valid User Role.");
            alert.createAlert();
            return;
        }
        userRole.setRole(selectedRole);

        var roleTask = new UserRoleSaveTask(userRole);
        roleTask.setOnSucceeded(ev -> {
            if (roleTask.getValue()) {
                showSuccessAndRefreshUI();
            } else {
                showErrorAlert();
            }
        });
        roleTask.setOnFailed(ev -> showErrorAlert());
        new Thread(roleTask).start();
    }

    private void showSuccessAndRefreshUI() {
        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("user"),
                resourceBundle.getString("record.update.successful"));
        alert.createAlert();

        if (callback != null) {
            callback.reloadData(true);
        }
        stage.close();
    }

    private void showGenericUserError() {
        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("user"),
                resourceBundle.getString("error.occurred"));
        alert.createAlert();
    }

    private void showErrorAlert() {
        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("userroles"),
                resourceBundle.getString("error.occurred"));
        alert.createAlert();
    }
}
