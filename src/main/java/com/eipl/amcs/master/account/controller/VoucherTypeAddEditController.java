package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.FocusUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class VoucherTypeAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private E_TextField txtCode, txtName, txtLocalName;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private VoucherType dto = null;
    private NextCodeService nextCodeService;
    private VoucherTypeService voucherTypeService;

    public VoucherTypeAddEditController() {
        voucherTypeService = context.getBean(VoucherTypeService.class);
        nextCodeService = context.getBean(NextCodeService.class);
    }

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
    }

    public void loadControls() {
        txtCode.setText(dto.getCode().toString());
        txtName.setText(dto.getName());
        txtLocalName.setText(dto.getNameLocal());
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
        try {
            String nextCode = nextCodeService.getNextCode("VoucherType", "code", MainApp.identityDto.getSociety().getCode(), 0);
            if (nextCode == null || nextCode.isEmpty())
                return;
            txtCode.setText(nextCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private VoucherType setValuesInObject() {
        dto.setCode(Integer.parseInt(txtCode.getText()));
        dto.setName(txtName.getText());
        dto.setNameLocal(txtLocalName.getText());
        dto.setActive(true);
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
            voucherTypeService.save(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                    resourceBundle.getString("vouchertype.insert.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                    "Error");
            alert.createAlert();
        }
    }

    @Override
    public void updateData() {
        try {
            voucherTypeService.update(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                    resourceBundle.getString("vouchertype.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                    "Error");
            alert.createAlert();
        }
    }
}
