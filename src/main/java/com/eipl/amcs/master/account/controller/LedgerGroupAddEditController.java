package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.account.converter.LedgerTypeConvertor;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.task.LedgerGroupNumberLoadTask;
import com.eipl.amcs.master.account.task.LedgerGroupSaveTask;
import com.eipl.amcs.master.account.task.LedgerTypeLoadTask;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LedgerGroupAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_Button btnClose, btnSaveUpdate;

    @FXML
    private E_ComboBox<LedgerType> cboxLedgertype;
    @FXML
    private E_TextField txtName, txtCode, txtLocalName;


    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private LedgerGroup dto = null;

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


    public void setLedgerGroup(LedgerGroup dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextLedgerGroupCode();
        }
        loadLedgerGroup();

    }

    private void getNextLedgerGroupCode() {
        var task = new LedgerGroupNumberLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty())
                    return;
                txtCode.setText(nextCode);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        FocusUtils.requestFocus(txtName);
        setupComboBox();
        loadLedgerGroup();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());

    }


    public void loadControls() {
//         cboxDesignation.setConverter(new DesignationConvertor(cboxDesignation));
        cboxLedgertype.getSelectionModel().select(dto.getLedgerType());
        txtName.setText(dto.getName());
        txtLocalName.setText(dto.getNameLocal());
        txtCode.setText(dto.getCode().toString());
        //   txtCode.setText(CommonUtils.getMemberShortCode(dto.getMember().getCode()));


    }

    //
    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgergroup"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                dto = setValuesInObject();
                updateData();
            }
        } else {
            dto = new LedgerGroup();
            dto = setValuesInObject();
            saveData();
        }
    }

    private LedgerGroup setValuesInObject() {
        dto.setLedgerType(cboxLedgertype.getValue());
        dto.setName(txtName.getText());
        dto.setNameLocal(txtLocalName.getText());
        dto.setActive(true);
        dto.setCode(Integer.valueOf(txtCode.getText()));
        return dto;
    }

    private boolean validate() {
        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
        if (cboxLedgertype.getValue() == null)
            errorMsg.append(resourceBundle.getString("error.occurred") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {

        var task = new LedgerGroupSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgergroup"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledgergroup"),
                        resourceBundle.getString("ledgergroup.insert.successful"));
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
        var task = new LedgerGroupSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgergroup"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledgergroup"),
                        resourceBundle.getString("ledgergroup.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/LedgerGroup.fxml")));
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxLedgertype.setConverter(new LedgerTypeConvertor(cboxLedgertype));
        cboxLedgertype.getSelectionModel().select(0);
    }

    private void loadLedgerGroup() {
        var task = new LedgerTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<LedgerType> list = task.get();
                if (list != null)
                    cboxLedgertype.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


}
