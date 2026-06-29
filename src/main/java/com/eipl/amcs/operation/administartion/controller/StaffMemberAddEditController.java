package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.*;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.master.org.task.BranchLoadTask;
import com.eipl.amcs.operation.administartion.task.DesignationLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffCodeLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffMemberSaveTask;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class StaffMemberAddEditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private E_DatePicker dpBirthDate, dpJoiningDate, dpApprovedDate, dpResignationDate;
    @FXML
    private CheckBox chkCommittee, chkDisabled, chkTrained;
    @FXML
    private AutoSearchTextField<Designation> cboxDesignation;
    @FXML
    private AutoSearchTextField<Branch> cboxBranch;
    @FXML
    private AutoSearchTextField<Bank> cboxBank;
    @FXML
    private RadioButton rbtnCash, rbtnBank;

    @FXML
    private AutoSearchTextField<Gender> cboxGender;


    @FXML
    private E_TextField txtNomineeName, txtRelation, txtGuarantorName, txtBloodGroup, txtName, txtQualification, txtAddress, txtIfsc;

    @FXML
    private E_NumericField txtCode, txtGuarantorMobileNo, txtPfLoanAmount, txtPfAmount, txtMobileNo, txtSalary, txtFarmerCode, txtAcNo;
    @FXML
    private E_TextFieldLocal txtNameLocal;
    @FXML
    private GridPane gridBankDetail;
    @FXML
    Button btnSaveUpdate, btnClose;
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
        FocusUtils.requestFocus(txtName);
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        loadDesignation();
        loadBank();
        loadBranch();
        loadData();
        gridBankDetail.setDisable(true);

        ToggleGroup paymentGroup = new ToggleGroup();
        rbtnCash.setToggleGroup(paymentGroup);
        rbtnBank.setToggleGroup(paymentGroup);

        cboxBank.setOnAction(event -> {
            if (cboxBank.getValue() != null) {
                cboxBranch.getItems().clear();
                cboxBranch.valueProperty().set(null);
                loadBranch();
            }
        });

        paymentGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isBankSelected = (newVal == rbtnBank);
            gridBankDetail.setDisable(!isBankSelected);
            if (!isBankSelected) {
                cboxBank.setValue(null);
                cboxBranch.setValue(null);
                txtAcNo.clear();
                txtIfsc.clear();
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
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                        resourceBundle.getString("staffmember.insert.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/administration/StaffMember.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new StaffMemberSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                        resourceBundle.getString("staffmember.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/administration/StaffMember.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffmember"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }


    private void setValuesInObject() {
        dto = new StaffMember();
        dto.setCode(txtCode.getInputText());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setMobileNo(txtMobileNo.getInputText());
        dto.setName(txtName.getText());
        dto.setNameLocal(txtNameLocal.getText());
        dto.setBloodGroup(txtBloodGroup.getText());
        dto.setSalary(txtSalary.getInputText());
        dto.setAddress(txtAddress.getText());
        dto.setMemberCode(txtFarmerCode.getInputText());
        dto.setQualification(txtQualification.getText());
        dto.setDesignation(cboxDesignation.getValue());
        dto.setPaymentMode((rbtnBank.isSelected() ? 1 : 0));
        dto.setGender(cboxGender.getValue());
        dto.setBank(cboxBank.getValue());
        dto.setBranch(cboxBranch.getValue());
        dto.setBankAccountNo(txtAcNo.getInputText());
        dto.setIfsc(txtIfsc.getText());
        dto.setBirthDate(dpBirthDate.getValue());
        dto.setApprovedDate(dpApprovedDate.getValue());
        dto.setTenureFromDate(dpJoiningDate.getValue());
        dto.setTenureToDate(dpResignationDate.getValue());
        dto.setCode(dto.getCode());
        dto.setUnionCode(MainApp.identityDto.getUnion().getCode());
        dto.setIsCommittee(chkCommittee.isSelected());
        dto.setIsDisabled(chkDisabled.isSelected());
        dto.setIsTrained(chkTrained.isSelected());
        dto.setNomineeName(txtNomineeName.getText());
        dto.setNomineeRelation(txtRelation.getText());
        dto.setGuarantorName(txtGuarantorName.getText());
        dto.setGuarantorMobile(txtGuarantorMobileNo.getInputText());
        dto.setPfLoanAmount(txtPfLoanAmount.getInputText().trim().isBlank() ? BigDecimal.ZERO : new BigDecimal(txtPfLoanAmount.getInputText()));
        dto.setPfAmount(txtPfAmount.getInputText().trim().isBlank() ? BigDecimal.ZERO : new BigDecimal(txtPfAmount.getInputText()));

    }

    private boolean validate() {
        errorMsg = new StringBuilder();

        if (txtCode.getInputText() == null || txtCode.getInputText().isEmpty())
            errorMsg.append(resourceBundle.getString("codenullerror") + "\n");

        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");

        if (cboxDesignation.getValue() == null)
            errorMsg.append(resourceBundle.getString("designationnullerror") + "\n");

        if (cboxGender.getValue() == null)
            errorMsg.append(resourceBundle.getString("gendernullerror") + "\n");

        if (rbtnBank.isSelected()) {
            if (cboxBank.getValue() == null)
                errorMsg.append(resourceBundle.getString("banknullerror") + "\n");
//            if (cboxBranch.getValue() == null)
//                errorMsg.append(resourceBundle.getString("branchnullerror") + "\n");
            if (txtAcNo.getInputText() == null || txtAcNo.getInputText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("acnonullerror") + "\n");
            if (txtIfsc.getText() == null || txtIfsc.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("ifscnonullerror") + "\n");
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void setupComboBox() {
        dpJoiningDate.setConverter(new LocalDateConvertor());
        dpJoiningDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpJoiningDate.setValue(dpJoiningDate.getConverter().fromString(dpJoiningDate.getEditor().getText()));
            }
        });
        dpResignationDate.setConverter(new LocalDateConvertor());
        dpResignationDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpResignationDate.setValue(dpResignationDate.getConverter().fromString(dpResignationDate.getEditor().getText()));
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
//                    new AutoCompleteComboBoxListener<>(cboxGender);
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
//                    new AutoCompleteComboBoxListener<>(cboxBank);
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
//                    new AutoCompleteComboBoxListener<>(cboxBranch);
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
            txtQualification.setText(dto.getQualification());
            txtNameLocal.setText(dto.getNameLocal());
            txtAddress.setText(dto.getAddress());
            txtBloodGroup.setText(dto.getBloodGroup());
            txtSalary.setText(dto.getSalary());
            txtFarmerCode.setText(dto.getMemberCode());
            txtMobileNo.setText(dto.getMobileNo());
            cboxGender.setValue(dto.getGender());
            cboxDesignation.setValue(dto.getDesignation());
            dpResignationDate.setValue(dto.getTenureToDate());
            dpJoiningDate.setValue(dto.getTenureFromDate());
            dpBirthDate.setValue(dto.getBirthDate());
            dpApprovedDate.setValue(dto.getApprovedDate());
            chkCommittee.setSelected(dto.getIsCommittee());
            chkDisabled.setSelected(dto.getIsDisabled());
            chkTrained.setSelected(dto.getIsTrained());
            txtNomineeName.setText(dto.getNomineeName());
            txtRelation.setText(dto.getNomineeRelation());
            txtGuarantorName.setText(dto.getGuarantorName());
            txtGuarantorMobileNo.setText(dto.getGuarantorMobile());
            txtPfLoanAmount.setText(dto.getPfLoanAmount() == null ? "" : String.valueOf(dto.getPfLoanAmount()));
            txtPfAmount.setText(dto.getPfAmount() == null ? "" : String.valueOf(dto.getPfAmount()));

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
        }
    }
}
