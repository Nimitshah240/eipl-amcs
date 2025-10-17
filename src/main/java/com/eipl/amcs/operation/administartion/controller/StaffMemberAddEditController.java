package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.global.convertor.GenderConvertor;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.convertor.BranchConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.master.org.task.BranchLoadTask;
import com.eipl.amcs.operation.administartion.dto.converter.DesignationConvertor;
import com.eipl.amcs.operation.administartion.task.DesignationLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffCodeLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffMemberSaveTask;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class StaffMemberAddEditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private E_DatePicker dptenureFromDate, dptenureToDate;
    @FXML
    private E_ComboBox<Designation> cboxDesignation;
    @FXML
    private E_ComboBox<Branch> cboxBranch;
    @FXML
    private E_ComboBox<Bank> cboxBank;
    @FXML
    private RadioButton rbtnCash, rbtnBank;

    @FXML
    private E_ComboBox<Gender> cboxGender;

    @FXML
    private E_TextField txtCode, txtMobileNo, txtPincode, txtName,
            txtEmailId, txtIfsc, txtAcNo, txtPanNo;
    @FXML
    private GridPane gridBankDetail;
    @FXML
    private E_Button btnSaveUpdate, btnClose;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private PopupCallback callback;
    private StaffMember dto;

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public Node getRoot() {
        return root;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStaffMember(StaffMember dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            setValuesInControls();
        } else {
            getNextStaffCode();
            loadData();

        }

    }

    private void getNextStaffCode() {
        var task = new StaffCodeLoadTask(MainApp.identityDto.getSociety().getCode());
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
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter
                (MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/administration/StaffMember.fxml"))));
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        loadDesignation();
        loadBank();
        loadBranch();
        loadData();
        gridBankDetail.setDisable(true);

        cboxBank.setOnAction(event -> {
            if (cboxBank.getValue() != null) {
                cboxBranch.getItems().clear();
                cboxBranch.valueProperty().set(null);
                loadBranch();
            }
        });

        rbtnCash.selectedProperty().addListener((observablevalue, oldvalue, newvalue) -> {
            if (newvalue) {
                gridBankDetail.setDisable(true);
                cboxBank.valueProperty().set(null);
                cboxBranch.valueProperty().set(null);
                txtAcNo.clear();
                txtIfsc.clear();
            } else {
                gridBankDetail.setDisable(false);
            }
        });


    }

    private void loadDesignation() {
        var task = new DesignationLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Designation> list = task.get();
                if (list != null)
                    cboxDesignation.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            setValuesInObject();
            updateData();
        } else {
            setValuesInObject();
            saveData();
        }
    }

    @Override
    public void saveData() {
        var task = new StaffMemberSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage());
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                        resourceBundle.getString("staffmember.insert.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/administration/StaffMember.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new StaffMemberSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                        resourceBundle.getString("staffmember.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/administration/StaffMember.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void setValuesInObject() {
        dto = new StaffMember();
        dto.setCode(txtCode.getText());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setMobileNo(txtMobileNo.getText());
        dto.setName(txtName.getText());
        dto.setDesignation(cboxDesignation.getValue());
        dto.setPaymentMode((rbtnBank.isSelected() ? 1 : 0));
        dto.setGender(cboxGender.getValue());
        dto.setPinCode(txtPincode.getText());
        dto.setEmailId(txtEmailId.getText());
        dto.setPanNo(txtPanNo.getText());
        dto.setBank(cboxBank.getValue());
        dto.setBranch(cboxBranch.getValue());
        dto.setBankAccountNo(txtAcNo.getText());
        dto.setIfsc(txtIfsc.getText());
        dto.setTenureFromDate(dptenureFromDate.getValue());
        dto.setTenureToDate(dptenureToDate.getValue());
        dto.setCode(dto.getCode());
        dto.setUnionCode(MainApp.identityDto.getUnion().getCode());
    }

    private boolean validate() {
        errorMsg = new StringBuilder();


        if (txtCode.getText() == null || txtCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("codenullerror") + "\n");

        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");

        if (cboxDesignation.getValue() == null)
            errorMsg.append(resourceBundle.getString("designationnullerror") + "\n");

        if (rbtnBank.isSelected()) {
            if (cboxBank.getValue() == null)
                errorMsg.append(resourceBundle.getString("banknullerror") + "\n");
            if (cboxBranch.getValue() == null)
                errorMsg.append(resourceBundle.getString("branchnullerror") + "\n");
            if (txtAcNo.getText() == null || txtAcNo.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("acnonullerror") + "\n");
            if (txtIfsc.getText() == null || txtIfsc.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("ifscnonullerror") + "\n");
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void setupComboBox() {

        cboxGender.setConverter(new GenderConvertor(cboxGender));
        cboxBank.setConverter(new BankConvertor(cboxBank));
        cboxBranch.setConverter(new BranchConvertor(cboxBranch));
        cboxDesignation.setConverter(new DesignationConvertor(cboxDesignation));
        dptenureFromDate.setConverter(new LocalDateConvertor());
        dptenureFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dptenureFromDate.setValue(dptenureFromDate.getConverter().fromString(dptenureFromDate.getEditor().getText()));
            }
        });
        dptenureToDate.setConverter(new LocalDateConvertor());
        dptenureToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dptenureToDate.setValue(dptenureToDate.getConverter().fromString(dptenureToDate.getEditor().getText()));
            }
        });

    }

    @Override
    public void loadData() {
        var task2 = new GenderLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<Gender> list = task2.get();
                if (list != null) {
                    cboxGender.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxGender);
                    if (dto != null)
                        cboxGender.setValue(dto.getGender());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    private void loadBank() {
        var task = new BankLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Bank> list = task.get();
                if (list != null) {
                    cboxBank.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxBank);
                    if (dto != null)
                        cboxBank.setValue(dto.getBank());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadBranch() {
        var task = new BranchLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Branch> list = task.get();
                if (list != null) {
                    cboxBranch.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxBranch);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInControls() {
        if (dto != null) {
            txtCode.setText(dto.getCode());
            txtName.setText(dto.getName());
            txtMobileNo.setText(dto.getMobileNo());
            txtEmailId.setText(dto.getEmailId());
            txtPanNo.setText(dto.getPanNo());
            cboxGender.setValue(dto.getGender());
//            txtAcNo.setText(dto.getBankAccountNo());
//            txtIfsc.setText(dto.getIfsc());
            txtPincode.setText(dto.getPinCode());
            cboxDesignation.setValue(dto.getDesignation());
            dptenureToDate.setValue(dto.getTenureToDate());
            dptenureFromDate.setValue(dto.getTenureFromDate());
            if (dto.getPaymentMode() == (short) 0) {
                rbtnCash.setSelected(true);
            } else {
                txtAcNo.setText(dto.getBankAccountNo());
                txtIfsc.setText(dto.getIfsc());
                if (dto.getBank() != null) {
                    cboxBank.setValue(dto.getBank());
                }
                if (dto.getBranch() != null) {
                    cboxBranch.setValue(dto.getBranch());
                }
            }
            if (dto.getPaymentMode() != null)
                rbtnCash.setSelected(dto.getPaymentMode() == (short) 0);
            if (dto.getPaymentMode() != null)
                rbtnBank.setSelected(dto.getPaymentMode() == (short) 1);
        }

    }


}



