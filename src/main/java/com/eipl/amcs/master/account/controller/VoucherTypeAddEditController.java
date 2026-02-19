package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.master.account.task.VoucherTypeNumberLoadTask;
import com.eipl.amcs.master.account.task.VoucherTypeSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class VoucherTypeAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private E_TextField txtCode, txtName, txtLocalName;
    @FXML
    private E_ComboBox<Ledger> cboxLedgers;
    @FXML
    private E_ComboBox<Integer> cboxVoucherType; // 0-cash,1-bank
    @FXML
    private E_ComboBox<Boolean> cboxCreditDebit; // 0-debit,1-credit
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private VoucherType dto = null;


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


    public void setVoucherType(VoucherType dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextVoucherTypeCode();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        txtLocalName.setOnAction(e -> {
            FocusUtils.requestFocus(btnSaveUpdate);
        });
        cboxVoucherType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (newValue == 0) { // Cash
                cboxLedgers.setDisable(false);
                cboxCreditDebit.setValue(false); // Debit
                loadLedgers();
            } else if (newValue == 1) { // Bank
                cboxLedgers.setDisable(true);
                cboxLedgers.getSelectionModel().clearSelection();
                cboxCreditDebit.getSelectionModel().clearSelection();
            }
        });
    }

    @Override
    public void setupComboBox() {
        cboxVoucherType.setItems(FXCollections.observableArrayList(0, 1));
        cboxVoucherType.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer object) {
                if (object == null) return "";
                return object == 0 ? "Cash" : "Bank";
            }

            @Override
            public Integer fromString(String string) {
                return "Cash".equals(string) ? 0 : 1;
            }
        });

        cboxCreditDebit.setItems(FXCollections.observableArrayList(false, true));
        cboxCreditDebit.setConverter(new StringConverter<>() {
            @Override
            public String toString(Boolean object) {
                if (object == null) return "";
                return !object ? "Debit" : "Credit";
            }

            @Override
            public Boolean fromString(String string) {
                return "Credit".equals(string);
            }
        });

        cboxLedgers.setConverter(new StringConverter<>() {
            @Override
            public String toString(Ledger object) {
                if (object == null) return "";
                return CommonUtils.getLocalString(object.getName(), object.getNameLocal());
            }

            @Override
            public Ledger fromString(String string) {
                return null; // Not needed for this use case
            }
        });
    }

    private void loadLedgers() {
        var task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Ledger> ledgers = task.get();
                if (ledgers != null && !ledgers.isEmpty()) {
                    cboxLedgers.setItems(FXCollections.observableArrayList(ledgers));
                } else {
                    cboxLedgers.setItems(FXCollections.observableArrayList());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadControls() {
        txtCode.setText(dto.getCode().toString());
        txtName.setText(dto.getName());
        txtLocalName.setText(dto.getNameLocal());
        cboxVoucherType.setValue(dto.getVoucherType());
        cboxCreditDebit.setValue(dto.getCreditDebit());
        if (dto.getLedger() != null) {
            cboxLedgers.setValue(dto.getLedger());
        }
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
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
            dto = new VoucherType();
            dto = setValuesInObject();
            saveData();
        }
    }

    private void getNextVoucherTypeCode() {
        var task = new VoucherTypeNumberLoadTask(MainApp.identityDto.getSociety().getCode());
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

    private VoucherType setValuesInObject() {
        dto.setCode(txtCode.getText());
        dto.setName(txtName.getText());
        dto.setNameLocal(txtLocalName.getText());
        dto.setActive(true);
        dto.setVoucherType(cboxVoucherType.getValue());
        dto.setCreditDebit(cboxCreditDebit.getValue());
        dto.setLedger(cboxLedgers.getValue());
        return dto;
    }

    private boolean validate() {
        if (txtName.getText() == null)
            errorMsg.append(resourceBundle.getString("referencecodenullerror") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new VoucherTypeSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                        resourceBundle.getString("vouchertype.insert.successful"));
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
        var task = new VoucherTypeSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                        resourceBundle.getString("vouchertype.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
