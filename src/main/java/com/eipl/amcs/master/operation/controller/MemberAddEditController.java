package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.geo.converter.*;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.geo.service.*;
import com.eipl.amcs.master.global.convertor.GenderConvertor;
import com.eipl.amcs.master.global.convertor.MemberTypeConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.global.service.MemberTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.convertor.BranchConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.service.BankService;
import com.eipl.amcs.master.org.service.BranchService;
import com.eipl.amcs.util.CommonUtil;
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

import static com.eipl.amcs.MainApp.context;

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
            txtEmail, txtPanNo, txtAadharCardNo, txtNoOfCow, txtNoOfBuffalo, txtAcNo, txtIfsc, txtCreditLimit, txtGroupCode;
    @FXML
    private Button btnSaveUpdate, btnClose;
    @FXML
    private GridPane gridBankDetail;

    private MemberService memberService;
    private NextCodeService nextCodeService;
    private MemberTypeService memberTypeService;
    private MilkTypeService milkTypeService;
    private GenderService genderService;
    private DistrictService districtService;
    private StateService stateService;
    private SubDistrictService subDistrictService;
    private BankService bankService;
    private BranchService branchService;
    private HamletService hamletService;
    private VillageService villageService;


    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private MemberDto dto = null;
    private Member member = null;
    private MemberDetail memberDetail = null;

    public MemberAddEditController() {
        memberService = context.getBean(MemberService.class);
        nextCodeService = context.getBean(NextCodeService.class);
        genderService = context.getBean(GenderService.class);
        milkTypeService = context.getBean(MilkTypeService.class);
        memberTypeService = context.getBean(MemberTypeService.class);
        stateService = context.getBean(StateService.class);
        districtService = context.getBean(DistrictService.class);
        villageService = context.getBean(VillageService.class);
        hamletService = context.getBean(HamletService.class);
        branchService = context.getBean(BranchService.class);
        bankService = context.getBean(BankService.class);
        subDistrictService = context.getBean(SubDistrictService.class);
    }

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
        try {
            MemberDto memberDto = memberService.save(dto, CommonUtil.setIdentityHeader());
            if (memberDto != null) {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString("member.insert.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateData() {
        try {
            MemberDto memberDto = memberService.update(dto, CommonUtil.setIdentityHeader());
            if (memberDto != null) {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString("member.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getNextMemberCode() {
        try {
            String nextCode = nextCodeService.getNextCode("Member", "code", MainApp.identityDto.getSociety().getCode(), 4);
            if (nextCode == null || nextCode.isEmpty())
                return;
            txtCode.setText(nextCode);
            txtCodeEx.setText(nextCode.replace(MainApp.identityDto.getSociety().getCode(), ""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        if (cboxDefaultMilkType.getValue() == null)
            errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
        if (txtAadharCardNo.getText() == null || txtAadharCardNo.getText().trim() == null || txtAadharCardNo.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("aadharcardnonullerror") + "\n");
        if (txtMobileNo.getText().trim() == null || txtMobileNo.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("mobilenonullerror") + "\n");

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

    //    NIMIT - MAKE IT ASYNC
    @Override
    public void loadData() {
        try {
            List<MemberType> list = memberTypeService.findAll();
            if (list != null) {
                cboxMemberType.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxMemberType);
                cboxMemberType.getSelectionModel().select(0);
                if (member != null)
                    cboxMemberType.setValue(member.getMemberType());
            }
//        HERE ASYNC
            List<MilkType> milkTypeList = milkTypeService.findAll();
            if (milkTypeList != null) {
                cboxDefaultMilkType.setItems(FXCollections.observableList(milkTypeList));
                new AutoCompleteComboBoxListener<>(cboxDefaultMilkType);
                if (member != null)
                    cboxDefaultMilkType.setValue(member.getMilkType());
            }

//        HERE ASYNC
            List<Gender> genderList = genderService.findAll();
            if (genderList != null) {
                cboxGender.setItems(FXCollections.observableList(genderList));
                new AutoCompleteComboBoxListener<>(cboxGender);
                if (memberDetail != null)
                    cboxGender.setValue(memberDetail.getGender());
            }
        } catch (Exception e) {
            System.out.println("Nimit error : " + e);
            throw new RuntimeException(e);
        }
    }

    private void loadState() {
        try {
            List<State> list = stateService.findAll();
            if (list != null) {
                cboxState.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxState);
                cboxState.getSelectionModel().select(0);

                if (memberDetail != null)
                    cboxState.setValue(memberDetail.getState());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDistrict(State state) {
        try {
            List<District> list = districtService.findAll(state.getCode());
            if (list != null) {
                cboxDistrict.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxDistrict);
                if (memberDetail != null)
                    cboxDistrict.setValue(memberDetail.getDistrict());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSubDistrict(District district) {
        try {
            List<SubDistrict> list = subDistrictService.findAll(district.getCode());
            if (list != null) {
                cboxSubDistrict.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxSubDistrict);
                if (memberDetail != null)
                    cboxSubDistrict.setValue(memberDetail.getSubDistrict());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadVillage(SubDistrict subDistrict) {
        try {
            List<Village> list = villageService.findAll(subDistrict.getCode());
            if (list != null) {
                cboxVillage.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxVillage);
                if (memberDetail != null)
                    cboxVillage.setValue(memberDetail.getVillage());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadHamlet(Village village) {
        try {
            List<Hamlet> list = hamletService.findAll(village.getCode());
            if (list != null) {
                cboxHamlet.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxHamlet);
                if (memberDetail != null)
                    cboxHamlet.setValue(memberDetail.getHamlet());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadBank() {
        try {
            List<Bank> list = bankService.findAll();
            if (list != null) {
                cboxBank.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxBank);
                if (memberDetail != null)
                    cboxBank.setValue(memberDetail.getBank());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadBranch(Bank bank) {
        try {
            List<Branch> list = branchService.findAll(bank.getCode());
            if (list != null) {
                cboxBranch.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxBranch);
                if (memberDetail != null)
                    cboxBranch.setValue(memberDetail.getBranch());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMemberDetail(String code) {
        try {
            Member member = memberService.findByMemberCode(code);
            MemberDetail md = memberService.findDetailByMember(member);
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
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setValuesInControls() {
        if (memberDetail != null) {
            dpRegistrationDate.setValue(memberDetail.getRegistrationDate());
            txtEmail.setText(memberDetail.getEmail());
            txtAadharCardNo.setText(memberDetail.getAadharNo());
            txtPanNo.setText(memberDetail.getPanNo());
            txtNoOfCow.setText(memberDetail.getNumberOfCow() != null ? memberDetail.getNumberOfCow().toString() : "0");
            txtNoOfCow.setText(memberDetail.getNumberOfBuffalo() != null ? memberDetail.getNumberOfBuffalo().toString() : "0");
            if (memberDetail.getPaymentMode() != null) {
                if (memberDetail.getPaymentMode() == 1) {
                    rbtnBank.setSelected(true);
                    cboxBank.setValue(memberDetail.getBank());
                    txtAcNo.setText(memberDetail.getAccountNo());
                    txtIfsc.setText(memberDetail.getIfsc());
                }
            }
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
        if (member.getCreditLimit() != null)
            txtCreditLimit.setText(member.getCreditLimit().toString());
    }
}
