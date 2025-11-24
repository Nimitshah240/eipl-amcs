package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.eipl.amcs.operation.administartion.task.StaffSalaryHeadCodeLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffSalaryHeadSaveTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SalaryHeadAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_Button btnClose, btnSaveUpdate;

    @FXML
    private ComboBox cboxType;
    @FXML
    private E_TextField txtName, txtCode;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private StaffSalaryHead dto = null;

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }


    public void setCommitteeMembers(StaffSalaryHead dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextCode();
            loadData();
        }
    }

    private void getNextCode() {
        var task = new StaffSalaryHeadCodeLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty())
                    return;
                txtCode.setText(nextCode.substring(MainApp.getUser().getSociety().getCode().length()));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        cboxType.getItems().addAll(resourceBundle.getString("addition"), resourceBundle.getString("deduction"));
        cboxType.getSelectionModel().select(0);
    }

    public void loadControls() {
        cboxType.getSelectionModel().select(dto.getType() == 0 ? resourceBundle.getString("deduction") : resourceBundle.getString("addition"));
        txtName.setText(dto.getName());
        txtCode.setText(dto.getCode().toString());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffsalaryhead"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            setValuesInObject();
            if (this.dto != null) {
                dto = setValuesInObject();
                updateData();
            }
        } else {
            dto = new StaffSalaryHead();
            dto = setValuesInObject();
            setValuesInObject();
            saveData();
        }
    }

    private StaffSalaryHead setValuesInObject() {
        dto.setType(cboxType.getSelectionModel().getSelectedIndex() - 1);
        dto.setName(txtName.getText());
        dto.setCode(Integer.valueOf(txtCode.getText()));
        dto.setSociety(MainApp.identityDto.getSociety());
        return dto;
    }

    private boolean validate() {
        if (cboxType.getValue() == null)
            errorMsg.append(resourceBundle.getString("typenullerror") + "\n");

        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");

        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new StaffSalaryHeadSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffsalaryhead"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("staffsalaryhead"),
                        resourceBundle.getString("staffsalaryhead.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new StaffSalaryHeadSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffsalaryhead"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("staffsalaryhead"),
                        resourceBundle.getString("staffsalaryhead.update.successful"));
                alert.createAlert();
//                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/SalaryHead.fxml")));
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
