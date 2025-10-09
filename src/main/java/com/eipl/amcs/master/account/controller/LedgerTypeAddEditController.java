package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.service.LedgerTypeService;
import com.eipl.amcs.util.CommonUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class LedgerTypeAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private E_TextField txtCode, txtName, txtLocalName;
    @FXML
    private CheckBox chkBalanceSheet, chkProfitLoss;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private LedgerType dto = null;
    private LedgerTypeService service;
    private NextCodeService nextCodeService;


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

    public LedgerTypeAddEditController() {
        service = context.getBean(LedgerTypeService.class);
        nextCodeService = context.getBean(NextCodeService.class);
    }

    public void setLedgerType(LedgerType dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextLedgerTypeCode();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    public void loadControls() {
        txtCode.setText(dto.getCode().toString());
        txtName.setText(dto.getName());
        chkBalanceSheet.setSelected(dto.isBalanceSheet());
        chkProfitLoss.setSelected(dto.isProfitLoss());
        txtLocalName.setText(dto.getNameLocal());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
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
            dto = new LedgerType();
            dto = setValuesInObject();
            saveData();
        }
    }

    private void getNextLedgerTypeCode() {
        try {
            String nextCode = nextCodeService.getNextCode("LedgerType", "code", MainApp.identityDto.getSociety().getCode(), 0);
            if (nextCode == null || nextCode.isEmpty())
                return;
            txtCode.setText(nextCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private LedgerType setValuesInObject() {
        dto.setCode(Integer.parseInt(txtCode.getText()));
        dto.setName(txtName.getText());
        dto.setNameLocal(txtLocalName.getText());
        dto.setActive(true);
        dto.setBalanceSheet(chkBalanceSheet.isSelected());
        dto.setProfitLoss(chkProfitLoss.isSelected());
        return dto;
    }

    private boolean validate() {
        if (txtName.getText() == null)
            errorMsg.append(resourceBundle.getString("referencecodenullerror") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        try {
            service.save(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
                    resourceBundle.getString("ledgertype.insert.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
                    "Error");
            alert.createAlert();
        }
    }

    @Override
    public void updateData() {
        try {
            service.update(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
                    resourceBundle.getString("ledgertype.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
                    "Error");
            alert.createAlert();
        }
    }


}
