package com.eipl.amcs.master.insurance.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.global.convertor.GenderConvertor;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.task.InsuranceDetailCodeTask;
import com.eipl.amcs.master.insurance.task.InsuranceDetailGetSerialNoTask;
import com.eipl.amcs.master.insurance.task.InsuranceDetailSaveTask;
import com.eipl.amcs.master.operation.controller.MemberAddEditController;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.EncryptionUtil;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;

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
//                if (insuranceDetail.getOriginatingOrgType().equalsIgnoreCase("PORTAL"))
//                    txtMemberName.setDisable(true);
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
        } catch (
                Exception e) {
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
//            txtAge.setText(String.valueOf(Period.between(dpBirthDate.getValue(), LocalDate.now()).getYears()));
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
//            insuranceDetail.setOriginatingOrgType("VLC");
            insuranceDetail.setOriginatingType(0);
//            int year = LocalDate.now().getYear();
//            insuranceDetail.setMemberId((year % 100) + MainApp.identityDto.getSociety().getCodeEx() + insuranceDetailSrNo);
//            memberId from portal side
            insuranceDetail.setMemberId(yearOfDetailFromPortal + MainApp.identityDto.getSociety().getCodeEx() + insuranceDetailSrNo);
            insuranceDetail.setMemberCode(insuranceDetailSummary.getDcsCode() + CommonUtils.getMemberShortCode(txtMemberCode.getText()));
//            insuranceDetail.setMemberCode(insuranceDetailSummary.getDcsCode() + txtMemberCode.getText());
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

    private void fetchMemberDetails() {
        memberCode = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtMemberCode.getText()));
        var task = new MemberByIdLoadTask(memberCode);
        task.setOnSucceeded(e -> {
            try {
//                member = task.get();
                if (task.get() != null) {
                    Member member = task.get();
                    txtMemberName.setText((member.getFirstName() != null ? member.getFirstName() : " ") + ' ' + (member.getMiddleName() != null ? member.getMiddleName() : " ") + ' ' + (member.getLastName() != null ? member.getLastName() : " "));
//                    txtMemberName.setText(member.toMemberName());
                } else {
                    txtMemberName.clear();
//                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
//                            resourceBundle.getString("membernotfound"));
                    MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                            resourceBundle.getString("alert.cancel"));
                    alert.createAlert();
                    Optional<ButtonType> resp = alert.createConfirmationAlert();
                    if (resp.isPresent() && resp.get() == ButtonType.OK) {
                        this.stage.close();
                        MemberAddEditController controller = (MemberAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/operation/MemberAddEdit.fxml"));
                        controller.setMember(null);
                        MainApp.getContentPane().setCenter(controller.getRoot());

                    }
                    txtMemberCode.setText("");
                    FocusUtils.requestFocus(txtMemberCode);
                }
//                txtMemberName.setText((member.getFirstName() != null ? member.getFirstName() : " ") + ' ' + (member.getMiddleName() != null ? member.getMiddleName() : " ") + ' ' + (member.getLastName() != null ? member.getLastName() : " "));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNextCode() {
        var task = new InsuranceDetailCodeTask();
        task.setOnSucceeded(event -> {
            try {
                insuranceDetailCode = task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();


        var task1 = new InsuranceDetailGetSerialNoTask();
        task1.setOnSucceeded(event -> {
            try {
                insuranceDetailSrNo = task1.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task1).start();


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
//        insuranceDetail.setMemberCode(MainApp.identityDto.getIdentity().getSocietyRefCode()+txtMemberCode.getText());
//        String societyRefCode = MainApp.identityDto.getIdentity().getSocietyRefCode();
//        String memberCodeText = txtMemberCode.getText();
//        while (memberCodeText.length() < 4) {
//            memberCodeText = "0" + memberCodeText;
//        }
//        insuranceDetail.setMemberCode(societyRefCode + memberCodeText);

//        insuranceDetail.setMemberCode(insuranceDetailSummary.getDcsCode() + CommonUtils.getMemberShortCode(txtMemberCode.getText()));
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
//        trimMemberCode();
        var task = new InsuranceDetailSaveTask(insuranceDetail, 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(CommonUtils.getResourceString(resourceBundle, resourceBundle.getString(subError.getMessage())) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                        resourceBundle.getString("insurance.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
//        trimMemberCode();
        var task = new InsuranceDetailSaveTask(insuranceDetail, 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("insurance"),
                        resourceBundle.getString("insurance.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void trimMemberCode() {
        if (insuranceDetail != null && insuranceDetail.getMemberCode() != null) {
            String trimmed = insuranceDetail.getMemberCode().trim();
            if (trimmed.length() > 4) {
                trimmed = trimmed.substring(trimmed.length() - 4); // Get last 4 characters
            }
            insuranceDetail.setMemberCode(trimmed);
        }
    }

    @Override
    public void loadControls() {

        if (this.insuranceDetail != null) {
            if (!insuranceDetail.getMemberCode().contains("0000")) {
//                txtMemberCode.setDisable(true);
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
        GenderLoadTask task = new GenderLoadTask();
        task.setOnSucceeded(e -> {
            try {
                genderList = task.get();
                cboxGender.setItems(FXCollections.observableList(genderList));
                cboxGender.getSelectionModel().select(0);
                if (insuranceDetail != null)
                    loadControls();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}