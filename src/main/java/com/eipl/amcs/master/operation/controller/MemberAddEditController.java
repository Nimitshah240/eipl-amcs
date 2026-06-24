package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.*;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.geo.task.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.model.*;
import com.eipl.amcs.master.operation.task.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.master.org.task.BranchLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MemberAddEditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private E_DatePicker dpBirthDate, dpRegistrationDate, dpBirthDate1;
    @FXML
    private TextArea txtAddress;
    @FXML
    private RadioButton rbtnCash, rbtnBank, rbtnIsFarmer;
    @FXML
    private AutoSearchTextField<MemberType> cboxMemberType;
    @FXML
    private AutoSearchTextField<State> cboxState;
    @FXML
    private AutoSearchTextField<District> cboxDistrict;
    @FXML
    private AutoSearchTextField<SubDistrict> cboxSubDistrict;
    @FXML
    private AutoSearchTextField<Village> cboxVillage;
    @FXML
    private AutoSearchTextField<Hamlet> cboxHamlet;
    @FXML
    private AutoSearchTextField<MilkType> cboxDefaultMilkType;
    @FXML
    private AutoSearchTextField<Gender> cboxGender, cboxGender1;
    @FXML
    private AutoSearchTextField<Bank> cboxBank;
    @FXML
    private AutoSearchTextField<Branch> cboxBranch;

    @FXML
    private AutoSearchTextField<AppConstant.LandType> cboxLandType;
    @FXML
    private AutoSearchTextField<AppConstant.FarmerType> cboxFarmerType;
    @FXML
    private AutoSearchTextField<AppConstant.Occupation> cboxOccupation;
    @FXML
    private AutoSearchTextField<AppConstant.MaritalStatus> cboxMaritalStatus;
    @FXML
    private AutoSearchTextField<AppConstant.CattleDetail> cboxCattleDetail;
    @FXML
    private AutoSearchTextField<Relationship> cboxRelation, cboxRelation1;
    @FXML
    private AutoSearchTextField<CasteCategory> cboxCaste;
    @FXML
    private AutoSearchTextField<AppConstant.RationCardType> cboxRationCardType;
    @FXML
    private E_TextField txtCodeEx, txtSapNo, txtCode, txtMobileNo, txtPinCode, txtMiddleName,
            txtName, txtLastName, txtMiddleLocalName, txtLocalLastName,
            txtEmail, txtPanNo, txtAadharCardNo, txtNoOfCow, txtNoOfBuffalo, txtAcNo, txtIfsc, txtCreditLimit,
            txtRationCardNo, txtMemberName, txtFarmerCode, txtFarmerName, txtAge, txtAadharCardNo1, txtNomineeName, txtLand, txtRegistrationNo, txtQualification,
            txtMilky, txtDry, txtCalf, txtChildFarmerName, txtChildFarmerCode;
    @FXML
    private E_TextFieldLocal txtLocalName, txtNomineeNameLocal;
    @FXML
    private Button btnSaveUpdate, btnClose;
    @FXML
    private E_Button btnAddCattleDetail, btnDeleteCattleDetail, btnAdd1, btnDelete1, btnAddChild, btnDeleteChild;
    @FXML
    private GridPane gridBankDetail;
    @FXML
    private ToggleGroup paymentType;
    @FXML
    private TableView<MemberCattleDetail> tblCattleDetail;
    @FXML
    private TableView<MemberFamilyDetail> tblFamilyDetail;
    @FXML
    private TableView<Member> tblFarmerMapping;

    @FXML
    private TableColumn<MemberFamilyDetail, String> colRationCard, colRationType, colMemberName, colFarmerCode,
            colFarmerName, colRelation, colBirthDate, colAge, colGender, colAadhar, colIsFarmer;

    @FXML
    private TableColumn<MemberCattleDetail, String> colCattleDetail, colMilky, colDry, colCalf, colTotal;
    @FXML
    private TableColumn<Member, String> colChildFarmerCode, colChildFarmerName;

    @FXML
    private E_CheckBox chkIsEducated, chkIsCookingGas, chkIsMember, chkIsMilkMachine, chkIsPiyetLand, chkIsChafCutter, IsToilet;

    private final ObjectProperty<MemberCattleDetail> propMemberCattleDetail;
    private final ObjectProperty<MemberFamilyDetail> propMembmerFamiliyDetail;
    private final ObjectProperty<Member> propFarmerMapping;

    private List<MemberCattleDetail> memberCattleDetailList = new ArrayList<>();
    private List<MemberFamilyDetail> memberFamilyDetailList = new ArrayList<>();
    private List<Member> farmerMapping = new ArrayList<>();
    private Member mappingFarmer = new Member();


    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private MemberDto dto = null;
    private Member member = null;
    private MemberDetail memberDetail = null;

    public MemberAddEditController() {
        propMembmerFamiliyDetail = new SimpleObjectProperty<>();
        propMemberCattleDetail = new SimpleObjectProperty<>();
        propFarmerMapping = new SimpleObjectProperty<>();
    }


    public Member getMember() {
        return member;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMember(Member member) {
        if (member != null) {
            this.member = member;
            loadMemberFamilyDetail();
            loadMemberCattleDetail();
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadMemberDetail(member.getCode());
            loadMappedFarmer(member.getCode());
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
        setupComboBox();
//        txtNoOfCow.setText("0");
//        txtNoOfBuffalo.setText("0");
        setupTable();
        loadState();
        loadBank();
        loadRelation();
        loadCasteCategory();
        dpRegistrationDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> {
            this.stage.close();
        });
        txtCalf.setText("0");
        txtMilky.setText("0");
        txtDry.setText("0");
        ToggleGroup paymentGroup = new ToggleGroup();
        rbtnCash.setToggleGroup(paymentGroup);
        rbtnBank.setToggleGroup(paymentGroup);

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

        btnSaveUpdate.setOnAction(e -> validateAndSave());

        btnAddChild.setOnAction(e -> {
            addFarmerMapping();
            mappingFarmer = null;
        });
        btnDeleteChild.setOnAction(e -> {
            Member member = propFarmerMapping.get();
            if (member != null)
                deleteFarmerMapping(member);
        });
//        cboxState.setOnAction(e -> {
//            if (cboxState.getValue() != null) {
//                cboxDistrict.getItems().clear();
//                cboxDistrict.valueProperty().set(null);
//                cboxSubDistrict.getItems().clear();
//                cboxSubDistrict.valueProperty().set(null);
//                cboxVillage.getItems().clear();
//                cboxVillage.valueProperty().set(null);
//                cboxHamlet.getItems().clear();
//                cboxHamlet.valueProperty().set(null);
//                loadDistrict(cboxState.getValue());
//            }
//        });
//        cboxDistrict.setOnAction(event -> {
//            if (cboxDistrict.getValue() != null) {
//                cboxSubDistrict.getItems().clear();
//                cboxSubDistrict.valueProperty().set(null);
//                cboxVillage.getItems().clear();
//                cboxVillage.valueProperty().set(null);
//                cboxHamlet.getItems().clear();
//                cboxHamlet.valueProperty().set(null);
//                loadSubDistrict(cboxDistrict.getSelectionModel().getSelectedItem());
//            }
//        });
//        cboxSubDistrict.setOnAction(event -> {
//            if (cboxSubDistrict.getValue() != null) {
//                cboxVillage.getItems().clear();
//                cboxVillage.valueProperty().set(null);
//                cboxHamlet.getItems().clear();
//                cboxHamlet.valueProperty().set(null);
//                loadVillage(cboxSubDistrict.getSelectionModel().getSelectedItem());
//            }
//        });
//        cboxVillage.setOnAction(event -> {
//            if (cboxVillage.getValue() != null) {
//                cboxHamlet.getItems().clear();
//                cboxHamlet.valueProperty().set(null);
//                loadHamlet(cboxVillage.getSelectionModel().getSelectedItem());
//            }
//        });
        cboxMemberType.setOnAction(event -> {
            MemberType selectedType = cboxMemberType.getSelectionModel().getSelectedItem();
            if (selectedType != null) {
                if (selectedType.getCode() == 1) {
                    dpRegistrationDate.setDisable(false);
                } else {
                    dpRegistrationDate.setValue(null);
                    dpRegistrationDate.setDisable(true);
                }
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
        chkIsMember.selectedProperty().addListener((observablevalue, oldvalue, newvalue) -> {
            if (newvalue) {
                txtRegistrationNo.setDisable(false);
            } else {
                txtRegistrationNo.setDisable(true);
            }
        });

        chkIsEducated.selectedProperty().addListener((observablevalue, oldvalue, newvalue) -> {
            if (newvalue) {
                txtQualification.setDisable(false);
                txtQualification.setEditable(true);
            } else {
                txtQualification.setDisable(true);
                txtQualification.setEditable(false);
            }
        });
        cboxBank.setOnAction(event -> {
            if (cboxBank.getValue() != null) {
                cboxBranch.getItems().clear();
                cboxBranch.valueProperty().set(null);
                loadBranch(cboxBank.getSelectionModel().getSelectedItem());
            }
        });

        cboxBank.setOnAction(event -> {
            if (cboxBank.getValue() != null) {
                cboxBranch.getItems().clear();
                cboxBranch.valueProperty().set(null);
                loadBranch(cboxBank.getSelectionModel().getSelectedItem());
            }
        });

        btnAddCattleDetail.setOnAction(e -> {
            addMemberCattleDetail();
            FocusUtils.requestFocus(cboxCattleDetail);
        });

        btnDeleteCattleDetail.setOnAction(e -> {
            MemberCattleDetail memberCattleDetail = propMemberCattleDetail.get();
            if (memberCattleDetail != null)
                deleteMemberCattleDetail(memberCattleDetail);
        });

        btnAdd1.setOnAction(e -> {
            addMemberFamilyDetail();
        });
        btnDelete1.setOnAction(e -> {
            MemberFamilyDetail memberFamilyDetail = propMembmerFamiliyDetail.get();
            if (memberFamilyDetail != null)
                deleteMemberFamilyDetail(memberFamilyDetail);
        });

        txtChildFarmerCode.setOnAction(e -> {
            if (txtChildFarmerCode.getText() == null || txtChildFarmerCode.getText().trim().isBlank())
                return;
            loadMember(MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtChildFarmerCode.getText())));
            FocusUtils.requestFocus(btnAddChild);
        });


//        txtCodeEx.focusedProperty().addListener((observableValue, oldVal, newVal) -> {
//            if (!newVal) {
//                txtCode.setText(MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(txtCodeEx.getText()));
//            }
//        });

        txtIfsc.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                FocusUtils.requestFocus(txtNomineeName);
                e.consume();
            }
        });

        txtCalf.setOnAction(e -> {
            FocusUtils.requestFocus(btnAddCattleDetail);
        });
        txtAadharCardNo1.setOnAction(e -> {
            FocusUtils.requestFocus(btnAdd1);
        });

        Platform.runLater(() -> {
            FocusUtils.requestFocus(txtName);
        });

        txtSapNo.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                FocusUtils.requestFocus(btnSaveUpdate);
                e.consume();
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
    public void setupTable() {
//        colRationCard.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRationCardNo()));
//        colRationType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRationCardType()));
        colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFamilyMemberName()));
//        colFarmerCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFarmerCode()));
//        colFarmerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFarmerName()));
        colRelation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRelationship().getRelationship()));

        colBirthDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDob() != null
                        ? data.getValue().getDob().toString() : ""));

        colAge.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAge() != null ? String.valueOf(data.getValue().getAge()) : ""));
        colGender.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender().toString()));
        colAadhar.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAadharCard()));
//        colIsFarmer.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isFarmer() ? "Yes" : "No"));
        colCattleDetail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCattleDetail()));
        colMilky.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMilky() != null ? String.valueOf(data.getValue().getMilky()) : ""));
        colDry.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDry() != null ? String.valueOf(data.getValue().getDry()) : ""));
        colCalf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCalf() != null ? String.valueOf(data.getValue().getCalf()) : ""));
        colTotal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTotal() != null ? String.valueOf(data.getValue().getTotal()) : ""));
        colChildFarmerCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx() != null ? String.valueOf(data.getValue().getCodeEx()) : ""));
        colChildFarmerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName() != null ? String.valueOf(data.getValue().toMemberName()) : ""));
        propMemberCattleDetail.bind(tblCattleDetail.getSelectionModel().selectedItemProperty());
        propMembmerFamiliyDetail.bind(tblFamilyDetail.getSelectionModel().selectedItemProperty());
        propFarmerMapping.bind(tblFarmerMapping.getSelectionModel().selectedItemProperty());

    }

    @Override
    public void saveData() {
        var task = new MemberSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString("member.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
//                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new MemberSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString("member.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
//                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
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
//                txtCodeEx.setText(nextCode.replace(MainApp.identityDto.getSociety().getCode(), ""));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInObject() {
        member.setActive(true);
        member.setCode(txtCode.getText());
        member.setCodeEx(txtCode.getText().substring(txtCode.getText().length() - 4));
        member.setSociety(MainApp.identityDto.getSociety());
        member.setMemberType(cboxMemberType.getValue());
        member.setMilkType(cboxDefaultMilkType.getValue());
        member.setMobileNo(txtMobileNo.getText());
        member.setFirstName(txtName.getText());
        member.setDcsMember(chkIsMember.isSelected());
        member.setCasteCategory(cboxCaste.getSelectionModel().getSelectedItem());
        member.setSapFarmerCode(txtSapNo.getText());
//        member.setxCol1(MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(txtGroupCode.getText()));
        //member.setMiddleName(txtMiddleName.getText() == null ? "" : txtMiddleName.getText());
        // member.setLastName(txtLastName.getText() == null ? "" : txtLastName.getText());
        member.setFirstNameLocal(txtLocalName.getText() == null ? "" : txtLocalName.getText());
        //member.setMiddleNameLocal(txtMiddleLocalName.getText() == null ? "" : txtMiddleLocalName.getText());
        //member.setLastNameLocal(txtLocalLastName.getText() == null ? "" : txtLocalLastName.getText());
        member.setCreditLimit(new BigDecimal(txtCreditLimit.getText()));
        memberDetail.setUnionCode(MainApp.identityDto.getUnion().getCode());
        memberDetail.setRegistrationDate(dpRegistrationDate.getValue());
        memberDetail.setGender(cboxGender.getValue());
        memberDetail.setBirthDate(dpBirthDate.getValue());
        memberDetail.setAddress(txtAddress.getText());
        memberDetail.setPincode(txtPinCode.getText());

        memberDetail.setOccupation(cboxOccupation.getValue() == null ? null : cboxOccupation.getValue().name());
        memberDetail.setNomineeName(txtNomineeName.getText());
        memberDetail.setRelationship(cboxRelation.getSelectionModel().getSelectedItem());
        memberDetail.setLocalNomineeName(txtNomineeNameLocal.getText());
        memberDetail.setEducated(chkIsEducated.isSelected());
        if (chkIsEducated.isSelected()) {
            memberDetail.setQualification(txtQualification.getText());
        } else {
            memberDetail.setQualification(null);
        }
        memberDetail.setCookingGas(chkIsCookingGas.isSelected());
        memberDetail.setMilkMachine(chkIsMilkMachine.isSelected());
        memberDetail.setPiyetLand(chkIsPiyetLand.isSelected());
        memberDetail.setChafCutter(chkIsChafCutter.isSelected());
        memberDetail.setToilet(IsToilet.isSelected());
        memberDetail.setLand(txtLand.getText());
        memberDetail.setLandType(cboxLandType.getValue() == null ? null : cboxLandType.getValue().name());
        memberDetail.setFarmerType(cboxFarmerType.getValue() == null ? null : cboxFarmerType.getValue().name());
        memberDetail.setMaritalStatus(cboxMaritalStatus.getValue() == null ? null : cboxMaritalStatus.getValue().name());
        memberDetail.setRegistrationNo(txtRegistrationNo.getText());
        memberDetail.setMemberTypeCode(cboxMemberType.getSelectionModel().getSelectedItem() == null ? null : String.valueOf(cboxMemberType.getSelectionModel().getSelectedItem().getCode()));
        //  memberDetail.setState(cboxState.getValue() == null ? MainApp.identityDto.getSociety().getState() : cboxState.getValue());
        // memberDetail.setDistrict(cboxDistrict.getValue() == null ? MainApp.identityDto.getSociety().getDistrict() : cboxDistrict.getValue());
        //memberDetail.setSubDistrict(cboxSubDistrict.getValue() == null ? MainApp.identityDto.getSociety().getSubDistrict() : cboxSubDistrict.getValue());
        //memberDetail.setVillage(cboxVillage.getValue() == null ? MainApp.identityDto.getSociety().getVillage() : cboxVillage.getValue());
        //memberDetail.setHamlet(cboxHamlet.getValue() == null ? MainApp.identityDto.getSociety().getHamlet() : cboxHamlet.getValue());

        memberDetail.setEmail(txtEmail.getText());
        memberDetail.setPanNo(txtPanNo.getText());
        memberDetail.setAadharNo(txtAadharCardNo.getText());
        //  memberDetail.setNumberOfCow(txtNoOfCow.getText().isEmpty() ? (short) 0 : Short.valueOf(txtNoOfCow.getText()));
        // memberDetail.setNumberOfBuffalo(txtNoOfBuffalo.getText().isEmpty() ? (short) 0 : Short.valueOf(txtNoOfBuffalo.getText()));
        memberDetail.setPaymentMode((short) (rbtnBank.isSelected() ? 1 : 0));
        memberDetail.setBank(cboxBank.getValue());
        memberDetail.setBranch(cboxBranch.getValue());
        memberDetail.setAccountNo(txtAcNo.getText());
        memberDetail.setIfsc(txtIfsc.getText());
        memberDetail.setMember(member);
        memberDetail.setCode(member.getCode());

        dto = new MemberDto(member, memberDetail, memberFamilyDetailList, memberCattleDetailList, farmerMapping);
    }

    private boolean validate() {
        errorMsg = new StringBuilder();

//        if (txtCodeEx.getText() == null || txtCodeEx.getText().isEmpty())
        //          errorMsg.append(resourceBundle.getString("codeexnullerror") + "\n");
        if (txtCode.getText() == null || txtCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("codenullerror") + "\n");
        if (cboxDefaultMilkType.getValue() == null)
            errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
        if (txtAadharCardNo.getText() == null || txtAadharCardNo.getText().trim() == null || txtAadharCardNo.getText().trim().isEmpty() || !txtAadharCardNo.getText().matches("\\d{12}"))
            errorMsg.append(resourceBundle.getString("aadharcardnonullerror") + "\n");
        if (txtCreditLimit.getText() == null || txtCreditLimit.getText().trim() == null || txtCreditLimit.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("creditlimitnonullerror") + "\n");
        if (txtMobileNo.getText() == null || txtMobileNo.getText().trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("mobilenonullerror") + "\n");
        } else if (!txtMobileNo.getText().matches("^[0-9]{10}$")) {
            errorMsg.append(resourceBundle.getString("mobileDigitError") + "\n");
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void setupComboBox() {
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

        cboxLandType.setItems(FXCollections.observableArrayList(AppConstant.LandType.values()));
        cboxFarmerType.setItems(FXCollections.observableArrayList(AppConstant.FarmerType.values()));
        cboxMaritalStatus.setItems(FXCollections.observableArrayList(AppConstant.MaritalStatus.values()));
        cboxOccupation.setItems(FXCollections.observableArrayList(AppConstant.Occupation.values()));
        cboxCattleDetail.setItems(FXCollections.observableArrayList(AppConstant.CattleDetail.values()));
        cboxRationCardType.setItems(FXCollections.observableArrayList(AppConstant.RationCardType.values()));
    }

    @Override
    public void loadData() {
        var task = new MemberTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MemberType> list = task.get();
                if (list != null) {
                    cboxMemberType.setItems(FXCollections.observableList(list));
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
                    cboxGender1.setItems(FXCollections.observableList(list));
                    if (memberDetail != null) {
                        cboxGender.setValue(memberDetail.getGender());
                        cboxGender1.setValue(memberDetail.getGender());
                    }
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
//                    cboxState.setItems(FXCollections.observableList(list));
//                    new AutoCompleteComboBoxListener<>(cboxState);
//                    cboxState.getSelectionModel().select(0);

//                    if (memberDetail != null)
//                        cboxState.setValue(memberDetail.getState());
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
//                    cboxDistrict.setItems(FXCollections.observableList(list));
//                    new AutoCompleteComboBoxListener<>(cboxDistrict);
//                    if (memberDetail != null)
//                        cboxDistrict.setValue(memberDetail.getDistrict());
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

    private void loadMappedFarmer(String code) {
        var task = new MappedFarmerLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                farmerMapping = task.get();
                if (farmerMapping != null) {
                    tblFarmerMapping.setItems(FXCollections.observableList(farmerMapping));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void loadMember(String code) {
        var task = new MemberByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                mappingFarmer = task.get();
                if (mappingFarmer != null) {
                    txtChildFarmerCode.setText(mappingFarmer.getCodeEx());
                    txtChildFarmerName.setText(mappingFarmer.getFirstName());
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void loadRelation() {
        var task = new RelationLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Relationship> list = task.get();
                cboxRelation.setItems(FXCollections.observableList(list));
                cboxRelation1.setItems(FXCollections.observableList(list));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void loadCasteCategory() {
        var task = new CasteCategoryLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<CasteCategory> list = task.get();
                if (list != null) {
                    cboxCaste.setItems(FXCollections.observableList(list));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
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
//            txtNoOfCow.setText(memberDetail.getNumberOfCow() != null ? memberDetail.getNumberOfCow().toString() : "0");
//            txtNoOfBuffalo.setText(memberDetail.getNumberOfBuffalo() != null ? memberDetail.getNumberOfBuffalo().toString() : "0");
            if (memberDetail.getPaymentMode() != null) {
                if (memberDetail.getPaymentMode() == 1) {
                    rbtnBank.setSelected(true);
                    cboxBank.setValue(memberDetail.getBank());
                    txtAcNo.setText(memberDetail.getAccountNo());
                    txtIfsc.setText(memberDetail.getIfsc());
                }
                cboxRelation.setValue(memberDetail.getRelationship());
            }
            dpBirthDate.setValue(memberDetail.getBirthDate());
            txtPinCode.setText(memberDetail.getPincode());
            txtAddress.setText(memberDetail.getAddress());
        }
//        txtCodeEx.setText(member.getCodeEx());
//        txtCodeEx.setDisable(true);
        txtCode.setText(member.getCode());
        txtName.setText(member.getFirstName());
//        txtMiddleName.setText(member.getMiddleName());
//        txtLastName.setText(member.getLastName());
        txtLocalName.setText(member.getFirstNameLocal());
//        txtMiddleLocalName.setText(member.getMiddleNameLocal());
//        txtLocalLastName.setText(member.getLastNameLocal());
        txtMobileNo.setText(member.getMobileNo());
//        txtGroupCode.setText(member.getxCol1().replace(MainApp.identityDto.getSociety().getCode(), ""));
        if (member.getCreditLimit() != null)
            txtCreditLimit.setText(member.getCreditLimit().toString());

        chkIsCookingGas.setSelected(memberDetail.isCookingGas());
        chkIsEducated.setSelected(memberDetail.isEducated());
        chkIsMilkMachine.setSelected(memberDetail.isMilkMachine());
        chkIsPiyetLand.setSelected(memberDetail.isPiyetLand());
        chkIsChafCutter.setSelected(memberDetail.isChafCutter());
        IsToilet.setSelected(memberDetail.isToilet());
        chkIsMember.setSelected(member.isDcsMember());
        cboxCaste.getSelectionModel().select(member.getCasteCategory());
        txtSapNo.setText(member.getSapFarmerCode());
        txtRegistrationNo.setText(memberDetail.getRegistrationNo());
        txtLand.setText(memberDetail.getLand());
        txtNomineeName.setText(memberDetail.getNomineeName());
        txtNomineeNameLocal.setText(memberDetail.getLocalNomineeName());
        cboxOccupation.getSelectionModel().select(AppConstant.Occupation.fromLabel(memberDetail.getOccupation()));
        cboxMaritalStatus.getSelectionModel().select(AppConstant.MaritalStatus.fromLabel(memberDetail.getMaritalStatus()));
//        cboxRelation.getSelectionModel().select(AppConstant.MaritalStatus.fromLabel(memberDetail.getMaritalStatus()));
        cboxLandType.getSelectionModel().select(AppConstant.LandType.fromLabel(memberDetail.getLandType()));
        cboxFarmerType.getSelectionModel().select(AppConstant.FarmerType.fromLabel(memberDetail.getFarmerType()));
        txtQualification.setText(memberDetail.getQualification());
        txtQualification.setDisable(!memberDetail.isEducated());
        txtQualification.setEditable(memberDetail.isEducated());
    }

    private boolean validateMemberCattleDetail() {
        try {
            errorMsg = new StringBuilder();

            if (txtCodeEx.getText() == null || txtCodeEx.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("codeexnullerror") + "\n");
            if (txtCode.getText() == null || txtCode.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("codenullerror") + "\n");
            if (cboxDefaultMilkType.getValue() == null)
                errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
            if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
            if (txtAadharCardNo.getText() == null || txtAadharCardNo.getText().trim() == null || txtAadharCardNo.getText().trim().isEmpty() || !txtAadharCardNo.getText().matches("\\d{12}"))
                errorMsg.append(resourceBundle.getString("aadharcardnonullerror") + "\n");
            if (txtCreditLimit.getText() == null || txtCreditLimit.getText().trim() == null || txtCreditLimit.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("creditlimitnonullerror") + "\n");
            if (txtMobileNo.getText() == null || txtMobileNo.getText().trim().isEmpty()) {
                errorMsg.append(resourceBundle.getString("mobilenonullerror") + "\n");
            } else if (!txtMobileNo.getText().matches("^[0-9]{10}$")) {
                errorMsg.append(resourceBundle.getString("mobileDigitError") + "\n");
            }
            return errorMsg.length() == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addMemberCattleDetail() {
        try {
            MemberCattleDetail memberCattleDetail = new MemberCattleDetail();
            memberCattleDetail.setCalf(Integer.valueOf(txtCalf.getText()));
            memberCattleDetail.setDry(Integer.valueOf(txtDry.getText()));
            memberCattleDetail.setMilky(Integer.valueOf(txtMilky.getText()));
            AppConstant.CattleDetail selected = cboxCattleDetail.getValue();
            String cattleDetailValue = selected.getLabel();
            memberCattleDetail.setCattleDetail(cattleDetailValue);
            Integer total = memberCattleDetail.getCalf() + memberCattleDetail.getDry() + memberCattleDetail.getMilky();
            memberCattleDetail.setTotal(total);
            memberCattleDetailList.add(memberCattleDetail);
            tblCattleDetail.setItems(FXCollections.observableList(memberCattleDetailList));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteMemberCattleDetail(MemberCattleDetail memberCattleDetail) {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {

                if (memberCattleDetail.getMemberCattleDetailCode() != null && !memberCattleDetail.getMemberCattleDetailCode().isBlank()) {
                    var task = new MemberCattleDetailDeleteTask(memberCattleDetail.getMemberCattleDetailCode());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                                        resourceBundle.getString("error.occurred"));
                                alert1.createAlert();
                                return;
                            }
                            memberCattleDetailList.remove(memberCattleDetail);
                            tblCattleDetail.setItems(FXCollections.observableList(memberCattleDetailList));

                            this.callback.reloadData(true);
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(task).start();
                } else {
                    memberCattleDetailList.remove(memberCattleDetail);
                    tblCattleDetail.setItems(FXCollections.observableList(memberCattleDetailList));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateMemberFamilyDetail() {
        try {
            errorMsg = new StringBuilder();

            if (txtRationCardNo.getText() == null || txtRationCardNo.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("rationcardnullerror") + "\n");
            if (txtMemberName.getText() == null || txtMemberName.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("membernamenullerror") + "\n");
            if (cboxRationCardType.getValue() == null)
                errorMsg.append(resourceBundle.getString("rationcardtypenullerror") + "\n");
            if (cboxRelation1.getValue() == null)
                errorMsg.append(resourceBundle.getString("relationnullerror") + "\n");
            return errorMsg.length() == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addMemberFamilyDetail() {
        try {
            errorMsg = new StringBuilder();
            if (!validateMemberFamilyDetail()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        errorMsg.toString());
                alert.createAlert();
                return;
            }
            MemberFamilyDetail memberFamilyDetail = new MemberFamilyDetail();
            memberFamilyDetail.setRationCardNo(txtRationCardNo.getText());
            memberFamilyDetail.setRationCardType(cboxRationCardType.getValue().getLabel());
            memberFamilyDetail.setFarmer(rbtnIsFarmer.isSelected());
            memberFamilyDetail.setFarmerCode(txtFarmerCode.getText());
            memberFamilyDetail.setFarmerName(txtFarmerName.getText());
            memberFamilyDetail.setFamilyMemberName(txtMemberName.getText());
            memberFamilyDetail.setRelationship(cboxRelation1.getValue());
            memberFamilyDetail.setDob(dpBirthDate.getValue());
            memberFamilyDetail.setAge(Integer.valueOf(txtAge.getText()));
            memberFamilyDetail.setGender(cboxGender1.getValue());
            memberFamilyDetail.setAadharCard(txtAadharCardNo1.getText());
            memberFamilyDetailList.add(memberFamilyDetail);
            tblFamilyDetail.setItems(FXCollections.observableList(memberFamilyDetailList));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteMemberFamilyDetail(MemberFamilyDetail memberFamilyDetail) {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {

                if (memberFamilyDetail.getMemberFamilyDetailCode() != null && !memberFamilyDetail.getMemberFamilyDetailCode().isBlank()) {
                    var task = new MemberFamilyDetailDeleteTask(memberFamilyDetail.getMemberFamilyDetailCode());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                                        resourceBundle.getString("error.occurred"));
                                alert1.createAlert();
                                return;
                            }
                            memberFamilyDetailList.remove(memberFamilyDetail);
                            tblFamilyDetail.setItems(FXCollections.observableList(memberFamilyDetailList));

                            this.callback.reloadData(true);
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(task).start();
                } else {
                    memberFamilyDetailList.remove(memberFamilyDetail);
                    tblFamilyDetail.setItems(FXCollections.observableList(memberFamilyDetailList));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMemberCattleDetail() {
        try {
            var task = new MemberCattleDetailLoadTask(member.getCode());
            task.setOnSucceeded(e -> {
                try {
                    List<MemberCattleDetail> list = task.get();
                    if (list != null)
                        memberCattleDetailList = list;
                    tblCattleDetail.setItems(FXCollections.observableList(memberCattleDetailList));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMemberFamilyDetail() {
        try {
            var task = new MemberFamilyDetailLoadTask(member.getCode());
            task.setOnSucceeded(e -> {
                try {
                    List<MemberFamilyDetail> list = task.get();
                    if (list != null)
                        memberFamilyDetailList = list;
                    tblFamilyDetail.setItems(FXCollections.observableList(memberFamilyDetailList));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void addFarmerMapping() {
        try {
            if (mappingFarmer != null && mappingFarmer.getCode() != null) {
                farmerMapping.add(mappingFarmer);
                tblFarmerMapping.setItems(FXCollections.observableList(farmerMapping));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFarmerMapping(Member member) {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {

                MemberDto memberDto = new MemberDto();
                member.setxCol1(null);
                memberDto.setMember(member);
                var task = new MemberSaveTask(memberDto, (short) 2);
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = (Boolean) task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        farmerMapping.remove(member);
                        tblFarmerMapping.setItems(FXCollections.observableList(farmerMapping));

                        this.callback.reloadData(true);
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}