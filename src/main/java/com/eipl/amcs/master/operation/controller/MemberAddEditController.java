package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.geo.converter.*;
//import com.eipl.amcs.master.geo.dto.*;
import com.eipl.amcs.master.geo.task.*;
import com.eipl.amcs.master.global.convertor.GenderConvertor;
import com.eipl.amcs.master.global.convertor.MemberTypeConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.dto.MemberDetail;
import com.eipl.amcs.master.operation.dto.MemberDto;
import com.eipl.amcs.master.operation.task.MemberCodeLoadTask;
import com.eipl.amcs.master.operation.task.MemberDetailLoadTask;
import com.eipl.amcs.master.operation.task.MemberSaveTask;
import com.eipl.amcs.master.operation.task.MemberTypeLoadTask;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.convertor.BranchConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.master.org.task.BranchLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.*;

public class MemberAddEditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private DatePicker dpBirthDate, dpRegistrationDate;
    @FXML
    private TextArea txtAddress;
    @FXML
    private RadioButton rbtnCash, rbtnBank;
    @FXML
    private ComboBox<MemberType> cboxMemberType;
    @FXML
    private ComboBox<State> cboxState;
    @FXML
    private ComboBox<District> cboxDistrict;
    @FXML
    private ComboBox<SubDistrict> cboxSubDistrict;
    @FXML
    private ComboBox<Village> cboxVillage;
    @FXML
    private ComboBox<Hamlet> cboxHamlet;
    @FXML
    private ComboBox<MilkType> cboxDefaultMilkType;
    @FXML
    private ComboBox<Gender> cboxGender;
    @FXML
    private ComboBox<Bank> cboxBank;
    @FXML
    private ComboBox<Branch> cboxBranch;
    @FXML
    private TextField txtCodeEx, txtCode, txtMobileNo, txtPincode, txtMiddleName,
            txtName, txtLastName, txtLocalName, txtMiddleLocalName, txtLocalLastName,
            txtEmail, txtPanNo, txtAadharCardNo, txtNoOfCow, txtNoOfBuffalo, txtAcNo, txtIfsc, txtCreditLimit,txtGroupCode;
    @FXML
    private Button btnSaveUpdate, btnClose;
    @FXML
    private GridPane gridBankDetail;

    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private MemberDto dto = null;
    private Member member = null;
    private MemberDetail memberDetail = null;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        if (member != null) {
            this.member = member;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadMemberDetail(member.getCode());
            if (member.getCreditLimit() != null)
                txtCreditLimit.setText(member.getCreditLimit().toString());
        } else {
            getNextMemberCode();
            loadData();
            txtCreditLimit.setText(MainApp.getProperty(AppConstant.Props.DEFAULT_CREDIT_LIMIT, "0"));
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        gridBankDetail.setDisable(true);
        FocusUtils.requestFocus(txtCodeEx);
        setupComboBox();
        txtNoOfCow.setText("0");
        txtNoOfBuffalo.setText("0");

        loadState();
        loadBank();
        dpRegistrationDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml"))));
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        cboxState.setOnAction(e -> {
            if (cboxState.getValue() != null) {
                cboxDistrict.getItems().clear();
                cboxDistrict.valueProperty().set(null);
                cboxSubDistrict.getItems().clear();
                cboxSubDistrict.valueProperty().set(null);
                cboxVillage.getItems().clear();
                cboxVillage.valueProperty().set(null);
                cboxHamlet.getItems().clear();
                cboxHamlet.valueProperty().set(null);
                loadDistrict(cboxState.getValue());
            }
        });
        cboxDistrict.setOnAction(event -> {
            if (cboxDistrict.getValue() != null) {
                cboxSubDistrict.getItems().clear();
                cboxSubDistrict.valueProperty().set(null);
                cboxVillage.getItems().clear();
                cboxVillage.valueProperty().set(null);
                cboxHamlet.getItems().clear();
                cboxHamlet.valueProperty().set(null);
                loadSubDistrict(cboxDistrict.getSelectionModel().getSelectedItem());
            }
        });
        cboxSubDistrict.setOnAction(event -> {
            if (cboxSubDistrict.getValue() != null) {
                cboxVillage.getItems().clear();
                cboxVillage.valueProperty().set(null);
                cboxHamlet.getItems().clear();
                cboxHamlet.valueProperty().set(null);
                loadVillage(cboxSubDistrict.getSelectionModel().getSelectedItem());
            }
        });
        cboxVillage.setOnAction(event -> {
            if (cboxVillage.getValue() != null) {
                cboxHamlet.getItems().clear();
                cboxHamlet.valueProperty().set(null);
                loadHamlet(cboxVillage.getSelectionModel().getSelectedItem());
            }
        });
        cboxMemberType.setOnAction(event -> {
            if (cboxMemberType.getSelectionModel().getSelectedItem().getCode() == 1) {
                dpRegistrationDate.setDisable(false);
            } else {
                dpRegistrationDate.setValue(null);
                dpRegistrationDate.setDisable(true);
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
        cboxBank.setOnAction(event -> {
            if (cboxBank.getValue() != null) {
                cboxBranch.getItems().clear();
                cboxBranch.valueProperty().set(null);
                loadBranch(cboxBank.getSelectionModel().getSelectedItem());
            }
        });

        txtCodeEx.focusedProperty().addListener((observableValue, oldVal, newVal) -> {
            if (!newVal) {
                txtCode.setText(MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(txtCodeEx.getText()));
            }
        });

    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            setValuesInObject();
            updateData();
        } else {
            member = new Member();
            memberDetail = new MemberDetail();
            setValuesInObject();
            saveData();
        }
    }

    @Override
    public void saveData() {
        var task = new MemberSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString("member.insert.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new MemberSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString("member.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNextMemberCode() {
        var task = new MemberCodeLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty())
                    return;
                txtCode.setText(nextCode);
                txtCodeEx.setText(nextCode.replace(MainApp.identityDto.getSociety().getCode(), ""));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInObject() {
        member.setActive(true);
        member.setCode(txtCode.getText());
//        member.setCodeEx(txtCode.getText().substring(7, 11));
        member.setCodeEx(txtCode.getText().substring(txtCode.getText().length() - 4));
        member.setSociety(MainApp.identityDto.getSociety());
        member.setMemberType(cboxMemberType.getValue());
        member.setMilkType(cboxDefaultMilkType.getValue());
        member.setMobileNo(txtMobileNo.getText());
//        member.setxCol1(MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtGroupCode.getText())));
        member.setFirstName(txtName.getText());
        member.setMiddleName(txtMiddleName.getText() == null ? "" : txtMiddleName.getText());
        member.setLastName(txtLastName.getText() == null ? "" : txtLastName.getText());
        member.setFirstNameLocal(txtLocalName.getText() == null ? "" : txtLocalName.getText());
        member.setMiddleNameLocal(txtMiddleLocalName.getText() == null ? "" : txtMiddleLocalName.getText());
        member.setLastNameLocal(txtLocalLastName.getText() == null ? "" : txtLocalLastName.getText());
        memberDetail.setUnionCode(MainApp.identityDto.getUnion().getCode());
        memberDetail.setRegistrationDate(dpRegistrationDate.getValue());
        memberDetail.setGender(cboxGender.getValue());
        memberDetail.setBirthDate(dpBirthDate.getValue());
        memberDetail.setAddress(txtAddress.getText());
        memberDetail.setPincode(txtPincode.getText());
        memberDetail.setState(cboxState.getValue() == null ? MainApp.identityDto.getSociety().getState() : cboxState.getValue());
        memberDetail.setDistrict(cboxDistrict.getValue() == null ? MainApp.identityDto.getSociety().getDistrict() : cboxDistrict.getValue());
        memberDetail.setSubDistrict(cboxSubDistrict.getValue() == null ? MainApp.identityDto.getSociety().getSubDistrict() : cboxSubDistrict.getValue());
        memberDetail.setVillage(cboxVillage.getValue() == null ? MainApp.identityDto.getSociety().getVillage() : cboxVillage.getValue());
        memberDetail.setHamlet(cboxHamlet.getValue() == null ? MainApp.identityDto.getSociety().getHamlet() : cboxHamlet.getValue());

        memberDetail.setEmail(txtEmail.getText());
        memberDetail.setPanNo(txtPanNo.getText());
        memberDetail.setAadharNo(txtAadharCardNo.getText());
        //ahiyalakho
        memberDetail.setNumberOfCow(txtNoOfCow.getText().isEmpty() ? (short) 0 : Short.valueOf(txtNoOfCow.getText()));
        memberDetail.setNumberOfBuffalo(txtNoOfBuffalo.getText().isEmpty() ? (short) 0 : Short.valueOf(txtNoOfBuffalo.getText()));
        memberDetail.setPaymentMode((short) (rbtnBank.isSelected() ? 1 : 0));
        memberDetail.setBank(cboxBank.getValue());
        memberDetail.setBranch(cboxBranch.getValue());
        memberDetail.setAccountNo(txtAcNo.getText());
        memberDetail.setIfsc(txtIfsc.getText());
        memberDetail.setMember(member);
        memberDetail.setCode(member.getCode());

        dto = new MemberDto(member, memberDetail);
    }

    private boolean validate() {
        errorMsg = new StringBuilder();

        if (txtCodeEx.getText() == null || txtCodeEx.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("codeexnullerror") + "\n");
        if (txtCode.getText() == null || txtCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("codenullerror") + "\n");
//        if (cboxMemberType.getValue() == null)
//            errorMsg.append(resourceBundle.getString("membertypenullerror") + "\n");
        if (cboxDefaultMilkType.getValue() == null)
            errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");


        if (txtAadharCardNo.getText() == null || txtAadharCardNo.getText().trim() == null || txtAadharCardNo.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("aadharcardnonullerror") + "\n");

        if (txtMobileNo.getText().trim() == null || txtMobileNo.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("mobilenonullerror") + "\n");


//        try {
//            if (txtMobileNo.getText() == null || txtMobileNo.getText().isEmpty() || Long.parseLong(txtMobileNo.getText()) >= 10000000000L || Long.parseLong(txtMobileNo.getText()) <= 999999999L)
//                errorMsg.append(resourceBundle.getString("mobilenonullerror") + "\n");
//
//            if (Long.parseLong(txtNoOfCow.getText()) < 0 || Long.parseLong(txtNoOfCow.getText()) >= 100000)
//                errorMsg.append(resourceBundle.getString("noofcowerror") + "\n");
//            if (Long.parseLong(txtNoOfBuffalo.getText()) < 0 || Long.parseLong(txtNoOfBuffalo.getText()) >= 100000)
//                errorMsg.append(resourceBundle.getString("noofbuffaloerror") + "\n");
//        } catch (NumberFormatException e) {
//            errorMsg.append("Enter Valid Values");
//        }
//        if (rbtnBank.isSelected()) {
//            if (cboxBank.getValue() == null)
//                errorMsg.append(resourceBundle.getString("banknullerror") + "\n");
//            if (cboxBranch.getValue() == null)
//                errorMsg.append(resourceBundle.getString("branchnullerror") + "\n");
//            if (txtAcNo.getText() == null || txtAcNo.getText().trim().isEmpty())
//                errorMsg.append(resourceBundle.getString("acnonullerror") + "\n");
//            if (txtIfsc.getText() == null || txtIfsc.getText().trim().isEmpty())
//                errorMsg.append(resourceBundle.getString("ifscnonullerror") + "\n");
//        }
        return errorMsg.length() == 0;
    }

    @Override
    public void setupComboBox() {
        cboxMemberType.setConverter(new MemberTypeConvertor(cboxMemberType));
        cboxDefaultMilkType.setConverter(new MilkTypeConvertor(cboxDefaultMilkType));
        cboxGender.setConverter(new GenderConvertor(cboxGender));
        cboxState.setConverter(new StateConvertor(cboxState));
        cboxDistrict.setConverter(new DistrictConvertor(cboxDistrict));
        cboxSubDistrict.setConverter(new SubDistrictConvertor(cboxSubDistrict));
        cboxVillage.setConverter(new VillageConvertor(cboxVillage));
        cboxHamlet.setConverter(new HamletConvertor(cboxHamlet));
        cboxBank.setConverter(new BankConvertor(cboxBank));
        cboxBranch.setConverter(new BranchConvertor(cboxBranch));

        dpBirthDate.setConverter(new LocalDateConvertor());
        dpBirthDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpBirthDate.setValue(dpBirthDate.getConverter().fromString(dpBirthDate.getEditor().getText()));
            }
        });
        dpRegistrationDate.setConverter(new LocalDateConvertor());
        dpRegistrationDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpRegistrationDate.setValue(dpRegistrationDate.getConverter().fromString(dpRegistrationDate.getEditor().getText()));
            }
        });
    }

    @Override
    public void loadData() {
        var task = new MemberTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MemberType> list = task.get();
                if (list != null) {
                    cboxMemberType.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxMemberType);
                    cboxMemberType.getSelectionModel().select(0);

                    if (member != null)
                        cboxMemberType.setValue(member.getMemberType());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        // milk type
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null) {
                    cboxDefaultMilkType.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxDefaultMilkType);
                    if (member != null)
                        cboxDefaultMilkType.setValue(member.getMilkType());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        // gender
        var task2 = new GenderLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<Gender> list = task2.get();
                if (list != null) {
                    cboxGender.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxGender);
                    if (memberDetail != null)
                        cboxGender.setValue(memberDetail.getGender());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    private void loadState() {
        var task = new StateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<State> list = task.get();
                if (list != null) {
                    cboxState.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxState);
                    cboxState.getSelectionModel().select(0);

                    if (memberDetail != null)
                        cboxState.setValue(memberDetail.getState());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadDistrict(State state) {
        var task = new DistrictLoadTask(state);
        task.setOnSucceeded(e -> {
            try {
                List<District> list = task.get();
                if (list != null) {
                    cboxDistrict.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxDistrict);
                    if (memberDetail != null)
                        cboxDistrict.setValue(memberDetail.getDistrict());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadSubDistrict(District district) {
        var task = new SubDistrictLoadTask(district);
        task.setOnSucceeded(e -> {
            try {
                List<SubDistrict> list = task.get();
                if (list != null) {
                    cboxSubDistrict.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxSubDistrict);
                    if (memberDetail != null)
                        cboxSubDistrict.setValue(memberDetail.getSubDistrict());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadVillage(SubDistrict subDistrict) {
        var task = new VillageLoadTask(subDistrict);
        task.setOnSucceeded(e -> {
            try {
                List<Village> list = task.get();
                if (list != null) {
                    cboxVillage.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxVillage);
                    if (memberDetail != null)
                        cboxVillage.setValue(memberDetail.getVillage());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadHamlet(Village village) {
        var task = new HamletLoadTask(village);
        task.setOnSucceeded(e -> {
            try {
                List<Hamlet> list = task.get();
                if (list != null) {
                    cboxHamlet.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxHamlet);
                    if (memberDetail != null)
                        cboxHamlet.setValue(memberDetail.getHamlet());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadBank() {
        var task = new BankLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Bank> list = task.get();
                if (list != null) {
                    cboxBank.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxBank);
                    if (memberDetail != null)
                        cboxBank.setValue(memberDetail.getBank());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadBranch(Bank bank) {
        var task = new BranchLoadTask(bank);
        task.setOnSucceeded(e -> {
            try {
                List<Branch> list = task.get();
                if (list != null) {
                    cboxBranch.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxBranch);
                    if (memberDetail != null)
                        cboxBranch.setValue(memberDetail.getBranch());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMemberDetail(String code) {
        var task = new MemberDetailLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                MemberDetail md = task.get();
                if (md != null) {
                    this.memberDetail = md;
                    loadData();
                    loadState();
                    setValuesInControls();
                } else {
                    loadData();
                    loadState();
                    setValuesInControls();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInControls() {
        if (memberDetail != null) {
            dpRegistrationDate.setValue(memberDetail.getRegistrationDate());
            txtEmail.setText(memberDetail.getEmail());
            txtAadharCardNo.setText(memberDetail.getAadharNo());
            txtPanNo.setText(memberDetail.getPanNo());
            txtNoOfCow.setText(memberDetail.getNumberOfCow() != null ? memberDetail.getNumberOfCow().toString() : "0");
            txtNoOfCow.setText(memberDetail.getNumberOfBuffalo() != null ? memberDetail.getNumberOfBuffalo().toString() : "0");
//            txtNoOfBuffalo.setText(memberDetail.getNumberOfBuffalo().toString());
            if (memberDetail.getPaymentMode() != null) {
                if (memberDetail.getPaymentMode() == 1) {
                    rbtnBank.setSelected(true);
                    cboxBank.setValue(memberDetail.getBank());
                    txtAcNo.setText(memberDetail.getAccountNo());
                    txtIfsc.setText(memberDetail.getIfsc());
                }
            }
//            txtAcNo.setText(memberDetail.getAccountNo());
//            txtIfsc.setText(memberDetail.getIfsc());
            dpBirthDate.setValue(memberDetail.getBirthDate());
            txtPincode.setText(memberDetail.getPincode());
            txtAddress.setText(memberDetail.getAddress());
        }
        txtCodeEx.setText(member.getCodeEx());
        txtCodeEx.setDisable(true);
        txtCode.setText(member.getCode());
        txtName.setText(member.getFirstName());
        txtMiddleName.setText(member.getMiddleName());
        txtLastName.setText(member.getLastName());
        txtLocalName.setText(member.getFirstNameLocal());
        txtMiddleLocalName.setText(member.getMiddleNameLocal());
        txtLocalLastName.setText(member.getLastNameLocal());
        txtMobileNo.setText(member.getMobileNo());
//        txtGroupCode.setText(member.getxCol1().replace(MainApp.identityDto.getSociety().getCode(),""));
        if (member.getCreditLimit() != null)
            txtCreditLimit.setText(member.getCreditLimit().toString());
    }
}
