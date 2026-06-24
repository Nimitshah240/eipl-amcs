package com.eipl.amcs.operation.inventory.controller;


import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.operation.inventory.model.DeadStock;
import com.eipl.amcs.operation.inventory.task.DeadStockNumberLoadTask;
import com.eipl.amcs.operation.inventory.task.DeadStockSaveTask;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class DeadStockAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_TextField txtName, txtNameLocal;
    @FXML
    private E_NumericField txtQuantity, txtCode, txtAmount;
    @FXML
    private E_DatePicker dpPurchaseDate;
    @FXML
    private E_ComboBox<Ledger> cboxLedger;
    @FXML
    private Button btnSaveUpdate, btnClose;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private DeadStock dto = null;

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

    public void setDeadStock(DeadStock dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextDeadStockCode();
        }
    }

    private void getNextDeadStockCode() {
        var task = new DeadStockNumberLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                txtCode.setText(task.get());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadControls() {
        txtCode.setText(dto.getCode().toString());
        txtName.setText(dto.getName());
        txtNameLocal.setText(dto.getNameLocal());
        txtQuantity.setText(dto.getQty().toString());
        txtAmount.setText(dto.getAmount().toString());
        dpPurchaseDate.setValue(dto.getPurchaseDate());
        cboxLedger.getSelectionModel().select(dto.getLedger());
    }

    @Override
    public void setupComboBox() {
        cboxLedger.setConverter(new LedgerConvertor(cboxLedger));
        new AutoCompleteComboBoxListener<>(cboxLedger);
    }

    @Override
    public void loadData() {
        loadLedgers();
    }

    private void loadLedgers() {
        var task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Ledger> ledgers = task.get();
                if (ledgers != null) {
                    cboxLedger.setItems(FXCollections.observableArrayList(ledgers));
                    if (dto != null && dto.getLedger() != null) {
                        for (Ledger ledger : ledgers) {
                            if (ledger.getCode().equals(dto.getLedger().getCode())) {
                                cboxLedger.getSelectionModel().select(ledger);
                                break;
                            }
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        setupComboBox();
        loadData();
        FocusUtils.requestFocus(dpPurchaseDate);
        btnSaveUpdate.setOnAction(e -> {
            validateAndSave();
        });

        btnClose.setOnAction(e -> {
            if (stage != null) {
                stage.close();
            }
        });
    }

    private void validateAndSave() {
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("deadstock"), errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equalsIgnoreCase(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                this.dto = setValuesInObject();
                updateData();
            }
        } else {
            this.dto = new DeadStock();
            this.dto = setValuesInObject();
            saveData();
        }
    }

    @Override
    public void saveData() {
        var task = new DeadStockSaveTask(this.dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                DeadStock savedDto = task.get();
                if (savedDto != null) {
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("deadstock"),
                            resourceBundle.getString("data.save.success"));
                    alert.createAlert();
                    if (callback != null) {
                        callback.reloadData(true);
                    }
                    if (stage != null) {
                        stage.close();
                    }
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("deadstock"),
                            resourceBundle.getString("data.save.fail"));
                    alert.createAlert();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new DeadStockSaveTask(this.dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                DeadStock savedDto = task.get();
                if (savedDto != null) {
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("deadstock"),
                            resourceBundle.getString("data.update.success"));
                    alert.createAlert();
                    if (callback != null) {
                        callback.reloadData(true);
                    }
                    if (stage != null) {
                        stage.close();
                    }
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("deadstock"),
                            resourceBundle.getString("data.update.fail"));
                    alert.createAlert();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private DeadStock setValuesInObject() {
        dto.setCode(txtCode.getInputText());
        dto.setName(txtName.getText());
        dto.setNameLocal(txtNameLocal.getText());
        dto.setQty(new BigDecimal(txtQuantity.getInputText()));
        dto.setAmount(new BigDecimal(txtAmount.getInputText()));
        dto.setPurchaseDate(dpPurchaseDate.getValue());
        dto.setLedger(cboxLedger.getValue());
        dto.setSocietyCode(MainApp.identityDto.getSociety());
        dto.setUnionCode(MainApp.identityDto.getUnion().getCode());

        if (btnSaveUpdate.getText().equalsIgnoreCase(resourceBundle.getString("update"))) {
            dto.setupdateData();
        } else {
            dto.setInitData();
        }
        return dto;
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (txtName.getText() == null || txtName.getText().isEmpty()) {
            errorMsg.append(resourceBundle.getString("name.cannot.be.null")).append("\n");
        }
        if (txtQuantity.getInputText() == null || txtQuantity.getInputText().isEmpty()) {
            errorMsg.append(resourceBundle.getString("qty.cannot.be.null")).append("\n");
        }
        if (txtAmount.getInputText() == null || txtAmount.getInputText().isEmpty()) {
            errorMsg.append(resourceBundle.getString("amount.cannot.be.null")).append("\n");
        }
        if (dpPurchaseDate.getValue() == null) {
            errorMsg.append(resourceBundle.getString("purchase.date.null.error")).append("\n");
        }
        if (cboxLedger.getValue() == null) {
            errorMsg.append(resourceBundle.getString("ledger.cannot.be.null")).append("\n");
        }
        return errorMsg.length() == 0;
    }
}