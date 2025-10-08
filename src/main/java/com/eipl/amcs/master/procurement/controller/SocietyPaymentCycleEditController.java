package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.service.ShiftService;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class SocietyPaymentCycleEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private TextField txtPaymentCycleId;
    @FXML
    private GridPane grid;
    @FXML
    private CheckBox chkLockBillingProcess, chkIsBillingCompleted;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private SocietyPaymentCycle dto = null;
    private SocietyPaymentCycleService societyPaymentCycleService;
    private ShiftService shiftService;

    public SocietyPaymentCycleEditController() {
        societyPaymentCycleService = context.getBean(SocietyPaymentCycleService.class);
        shiftService = context.getBean(ShiftService.class);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setSocietyPaymentCycle(SocietyPaymentCycle dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        }
        loadShift();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        setupComboBox();
        loadShift();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    @Override
    public void setupComboBox() {
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });

    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            dto = setValuesInObject();
            updateData();
            //saveData();
        }
    }

    private SocietyPaymentCycle setValuesInObject() {
        SocietyPaymentCycle societyPaymentCycle = new SocietyPaymentCycle();
        if (txtPaymentCycleId.getText() != null)
            societyPaymentCycle.setCode(txtPaymentCycleId.getText());
        societyPaymentCycle.setToDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()));
        societyPaymentCycle.setFromDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
        societyPaymentCycle.setToShift(cboxToShift.getValue());
        societyPaymentCycle.setFromShift(cboxFromShift.getValue());
        societyPaymentCycle.setLockBillingProcess(chkLockBillingProcess.isSelected());
        societyPaymentCycle.setBilling(chkIsBillingCompleted.isSelected());
        societyPaymentCycle.setIntervalValue(dto.getIntervalValue());
        return societyPaymentCycle;
    }

    private boolean validate() {
        if (cboxFromShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("fromshiftnullerror") + "\n");
        if (cboxToShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("toshiftnullerror") + "\n");
        if (dpFromDate.getValue() == null || dpToDate.getValue() == null) {
            if (dpFromDate.getValue() == null) {
                errorMsg.append(resourceBundle.getString("enterfromdate") + "\n");
            }
            if (dpToDate.getValue() == null) {
                errorMsg.append(resourceBundle.getString("entertodate") + "\n");
            }
        } else {
            if (dpFromDate.getValue().isAfter(dpToDate.getValue())) {
                errorMsg.append(resourceBundle.getString("fromdatesmallerthantodate") + "\n");
            }
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        try {
            societyPaymentCycleService.save(Arrays.asList(dto), CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                    resourceBundle.getString("societypaymentcycle.insert.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateData() {
        try {
            societyPaymentCycleService.update(txtPaymentCycleId.getText(), dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                    resourceBundle.getString("societypaymentcycle.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadShift() {
        try {
            List<Shift> list = shiftService.findAll();
            if (list != null)
                cboxFromShift.setItems(FXCollections.observableList(list));
            cboxToShift.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadControls() {
        txtPaymentCycleId.setText(dto.getCode());
        dpFromDate.setValue(dto.getFromDate().toLocalDate());
        dpToDate.setValue(dto.getToDate().toLocalDate());
        cboxToShift.getSelectionModel().select(dto.getToShift());
        cboxFromShift.getSelectionModel().select(dto.getFromShift());
        if (dto.getBilling()) {
            chkIsBillingCompleted.setSelected(true);
        }
        if (dto.getLockBillingProcess()) {
            chkLockBillingProcess.setSelected(true);
        }
    }

}

