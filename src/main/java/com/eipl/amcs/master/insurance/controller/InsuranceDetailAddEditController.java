package com.eipl.amcs.master.insurance.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.GenderConvertor;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.EncryptionUtil;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static com.eipl.amcs.MainApp.context;

public class InsuranceDetailAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_TextField txtMemberCode, txtMemberName, txtAdharNo, txtNomineeAdharNo, txtNomineeMemberName;
    @FXML
    private E_Button btnSaveUpdate, btnClose;
    @FXML
    private E_DatePicker dpBirthDate, dpDateOfJoiningScheme;
    @FXML
    private ComboBox<Gender> cboxGender;
    private List<Gender> genderList;
    private String memberCode;
    private Member member;
    @FXML
    private TextField txtAge;

    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private InsuranceDetail insuranceDetail = null;
    private PopupCallback callback;
    public InsuranceMaster insuranceMaster = null;
    public InsuranceDetailSummary insuranceDetailSummary = null;
    public String insuranceDetailCode = "";
    public String insuranceDetailSrNo = "";
    public String yearOfDetailFromPortal = String.valueOf(LocalDate.now().getYear() % 100);
    public List<InsuranceDetail> insuranceDetailList = null;

    private InsuranceMasterService insuranceMasterService;
    private GenderService genderService;
    private NextCodeService nextCodeService;

    public InsuranceDetailAddEditController() {
        genderService = context.getBean(GenderService.class);
        insuranceMasterService = context.getBean(InsuranceMasterService.class);
        nextCodeService = context.getBean(NextCodeService.class);
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

    public void setInsuranceDetailDto(Map<String, Object> dto) {
        try {
            if (dto != null && dto.get("insuranceDetail") != null) {
                this.insuranceDetail = (InsuranceDetail) dto.get("insuranceDetail");
                btnSaveUpdate.setText(resourceBundle.getString("update"));
                txtMemberName.setDisable(true);
                cboxGender.setDisable(true);
                txtAdharNo.setDisable(true);
                dpBirthDate.setDisable(true);
            } else {
                txtMemberName.setDisable(false);
                cboxGender.setDisable(false);
                txtAdharNo.setDisable(false);
                dpBirthDate.setDisable(false);
            }
            assert dto != null;
            insuranceMaster = (InsuranceMaster) dto.get("insuranceMaster");
            insuranceDetailSummary = (InsuranceDetailSummary) dto.get("insuranceDetailSummary");
            insuranceDetailList = (List<InsuranceDetail>) dto.get("insuranceDetailList");
            if (insuranceDetailList != null && !insuranceDetailList.isEmpty()) {
                String yearOfDetailFromPortal = insuranceDetailList.get(0).getMemberId().substring(0, 2);
            }
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        FocusUtils.requestFocus(txtMemberCode);
        getNextCode();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        dpBirthDate.setConverter(new LocalDateConvertor());
        dpDateOfJoiningScheme.setValue(LocalDate.now());
        dpBirthDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpBirthDate.setValue(dpBirthDate.getConverter().fromString(dpBirthDate.getEditor().getText()));
            }
        });
        dpBirthDate.setOnAction(e -> {
            if (dpBirthDate.getValue() != null && insuranceMaster.getInsuranceStartDate() != null) {
                int age = Period.between(dpBirthDate.getValue(), insuranceMaster.getInsuranceStartDate()).getYears();
                txtAge.setText(String.valueOf(age));
            }
        });
        txtMemberCode.setOnAction(e -> {
//            fetchMemberDetails();
        });
        setupTextFormatters();

    }

    private void setupTextFormatters() {
        UnaryOperator<TextFormatter.Change> aadhaarFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,12}")) {
                return change;
            }
            return null;
        };

        txtAdharNo.setTextFormatter(new TextFormatter<>(aadhaarFilter));
        txtNomineeAdharNo.setTextFormatter(new TextFormatter<>(aadhaarFilter));
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.insuranceDetail != null) {
                setValuesInObject();
                updateData();
            }
        } else {
            insuranceDetail = new InsuranceDetail();
            insuranceDetail.setInsuranceDetailCode(insuranceDetailCode);
            insuranceDetail.setDcsCode(insuranceDetailSummary.getDcsCode());
            insuranceDetail.setPlantCode(insuranceDetailSummary.getPlantCode());
            insuranceDetail.setMccPlantCode(insuranceDetailSummary.getMccPlantCode());
            insuranceDetail.setBmcCode(insuranceDetailSummary.getBmcCode());
            insuranceDetail.setUnionCode(insuranceDetailSummary.getUnionCode());
            insuranceDetail.setDcsCode(insuranceDetailSummary.getDcsCode());
            insuranceDetail.setDcsCode(insuranceDetailSummary.getDcsCode());
            insuranceDetail.setStatus(insuranceDetailSummary.getStatus());
            insuranceDetail.setDelete(false);
            insuranceDetail.setSrNo(insuranceDetailSrNo);
            insuranceDetail.setOriginatingOrgCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
            insuranceDetail.setOriginatingType(0);
            insuranceDetail.setMemberId(yearOfDetailFromPortal + MainApp.identityDto.getSociety().getCodeEx() + insuranceDetailSrNo);
            insuranceDetail.setMemberCode(insuranceDetailSummary.getDcsCode() + CommonUtils.getMemberShortCode(txtMemberCode.getText()));
            setValuesInObject();
            saveData();
        }
    }

    private boolean validate() {
        errorMsg = new StringBuilder();

        if (txtMemberCode.getText() == null || txtMemberCode.getText().isEmpty()) {
            errorMsg.append(resourceBundle.getString("codenullerror")).append("\n");
        } else if (!txtMemberCode.getText().matches("\\d+")) {
            errorMsg.append(resourceBundle.getString("member.code.invalid") + "\n");
        } else {
            int memberCode = Integer.parseInt(txtMemberCode.getText());
            if (memberCode > 9999 || memberCode <= 0) {
                errorMsg.append(resourceBundle.getString("codenullerror")).append("\n");
            }
        }
        if (txtMemberName.getText() == null || txtMemberName.getText().isEmpty()) {
            errorMsg.append(resourceBundle.getString("membernamenullerror") + "\n");
        } else if (!txtMemberName.getText().matches("^[A-Za-z ]+$")) {
            errorMsg.append(resourceBundle.getString("member.code.invalid") + "\n");
        }

        if (txtNomineeMemberName.getText() == null || txtNomineeMemberName.getText().isEmpty()) {
            errorMsg.append(resourceBundle.getString("nomineenamenullerror") + "\n");
        } else if (!txtNomineeMemberName.getText().matches("^[A-Za-z ]+$")) {
            errorMsg.append(resourceBundle.getString("nominee.name.invalid") + "\n");
        }

        if (txtNomineeAdharNo.getText() == null || txtNomineeAdharNo.getText().trim().isEmpty() ||
                !txtNomineeAdharNo.getText().matches("\\d{12}")) {
            errorMsg.append(resourceBundle.getString("nomineeaadharcardnonullerror")).append("\n");
        }
        if (txtAdharNo.getText() == null || txtAdharNo.getText().trim().isEmpty() ||
                !txtAdharNo.getText().matches("\\d{12}")) {
            errorMsg.append(resourceBundle.getString("aadharcardnonullerror")).append("\n");
        }
        if (txtAge.getText() != null &&
                (Integer.parseInt(txtAge.getText()) < insuranceMaster.getMemberMinAge() || Integer.parseInt(txtAge.getText()) > insuranceMaster.getMemberMaxAge())) {
            errorMsg.append(resourceBundle.getString("agecriterianotmatch") + "\n");
        }

        return errorMsg.length() == 0;
    }

    private void getNextCode() {
        try {
//            NIMIT ASYNC
            insuranceDetailCode = nextCodeService.getNextCode("InsuranceDetail", "insuranceDetailCode", "VLC" + "-" + MainApp.identityDto.getIdentity().getSocietyRefCode() + "-", 1);
            insuranceDetailSrNo = nextCodeService.getNextCode("InsuranceDetail", "srNo", MainApp.identityDto.getIdentity().getSocietyRefCode(), 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupComboBox() {
        cboxGender.setConverter(new GenderConvertor(cboxGender));
    }

    private void setValuesInObject() {
        insuranceDetail.setGenderCode(String.valueOf(cboxGender.getValue().getCode()));
        insuranceDetail.setNomineeMemberName(txtNomineeMemberName.getText());
        insuranceDetail.setNomineeAdharNo(EncryptionUtil.encrypt(txtNomineeAdharNo.getText()));
        insuranceDetail.setMemberName(txtMemberName.getText());
        insuranceDetail.setMemberCode(insuranceDetailSummary.getDcsCode() + CommonUtils.getMemberShortCode(txtMemberCode.getText()));
        insuranceDetail.setAdharNo(EncryptionUtil.encrypt(txtAdharNo.getText()));
        insuranceDetail.setDob(EncryptionUtil.encrypt(dpBirthDate.getValue().format(AppConstant.Formatter5)));
        insuranceDetail.setDateOfJoiningScheme(dpDateOfJoiningScheme.getValue());
        insuranceDetail.setAge(Integer.valueOf(txtAge.getText()));
        insuranceDetail.setDcsCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
        insuranceDetail.setDcsName(insuranceDetailSummary.getDcsName());
        insuranceDetail.setUnionCode(MainApp.identityDto.getUnion().getCode());
        insuranceDetail.setInsuranceMasterCode(insuranceMaster.getInsuranceMasterCode());
    }

    @Override
    public void saveData() {
        try {
            insuranceMasterService.saveDetails(insuranceDetail, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                    resourceBundle.getString("insurance.insert.successful"));
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
            insuranceMasterService.updateDetails(insuranceDetail, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                    resourceBundle.getString("insurance.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void loadControls() {

        if (this.insuranceDetail != null) {
            if (!insuranceDetail.getMemberCode().contains("0000")) {
                txtMemberCode.setDisable(false);
            }
            dpBirthDate.setValue(AppConstant.parseDateWithMultipleFormats(EncryptionUtil.decrypt(insuranceDetail.getDob())));
            dpDateOfJoiningScheme.setValue(insuranceDetail.getDateOfJoiningScheme());
            if (insuranceDetail.getGenderCode() != null && !insuranceDetail.getGenderCode().equalsIgnoreCase("null"))
                cboxGender.getSelectionModel().select(genderList.stream().filter(e -> e.getCode() == Integer.parseInt(insuranceDetail.getGenderCode())).findFirst().get());
            txtAge.setText(insuranceDetail.getAge().toString());
            txtMemberName.setText(insuranceDetail.getMemberName());
            txtMemberCode.setText(insuranceDetail.getMemberCode().substring(insuranceDetail.getMemberCode().length() - 4));
            txtAdharNo.setText(EncryptionUtil.decrypt(insuranceDetail.getAdharNo()));
            txtNomineeMemberName.setText(insuranceDetail.getNomineeMemberName());
            txtNomineeAdharNo.setText(EncryptionUtil.decrypt(insuranceDetail.getNomineeAdharNo()));
        }
    }

    public void loadData() {
        try {
            genderList = genderService.findAll();
            cboxGender.setItems(FXCollections.observableList(genderList));
            cboxGender.getSelectionModel().select(0);
            if (insuranceDetail != null)
                loadControls();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}