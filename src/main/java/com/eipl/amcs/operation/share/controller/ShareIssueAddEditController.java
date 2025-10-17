package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.master.operation.task.MemberListSaveTask;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.task.ShareGetNextCodeTask;
import com.eipl.amcs.operation.share.task.ShareIssueSaveTask;
import com.eipl.amcs.operation.share.task.ShareRateByWefDateLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ShareIssueAddEditController implements MyInitialization {

    BigDecimal shareAmount;
    private PopupCallback callback;
    @FXML
    private StackPane root;
    private Stage stage;
    @FXML
    private E_DatePicker dpDate;
    @FXML
    private E_TextField txtVoucherNo, txtShareCode, txtMemberCode, txtMemberName, txtNoOfShare, txtCertificateNo, txtAmount, txtLedgerNo;
    @FXML
    private Button btnSave, btnClose;
    @FXML
    private CheckBox chkIsMember;
    private ResourceBundle resourceBundle;
    private Share share;
    private Member member;
    private StringBuilder errorMsg = null;
    private MemberDetail memberDetail;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        getNextCode();
        dpDate.setValue(LocalDate.now());
        getShareAmount(LocalDate.now());
        btnClose.setOnAction(e -> this.stage.close());
        FocusUtils.requestFocus(txtShareCode);
        btnSave.setOnAction(e -> {
            validateAndSave();
        });
        txtLedgerNo.textProperty().addListener((observable, oldValue, newValue) -> {
            FocusUtils.requestFocus(btnSave);
        });
        txtMemberCode.textProperty().addListener((observable, oldValue, newValue) -> {
            String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtMemberCode.getText()));
            setConsumerName(code);
        });

        txtNoOfShare.textProperty().addListener((observable, oldValue, newValue) -> {
            txtAmount.setText(new BigDecimal(txtNoOfShare.getText()).multiply(shareAmount).toString());
        });

        dpDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getShareAmount(dpDate.getValue());
            }
        });

        chkIsMember.setOnAction(event -> {
            txtMemberName.setDisable(chkIsMember.isSelected());
        });


    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("shareissue"),
                    errorMsg.toString());
            alert.createAlert();
        } else {
            if (!chkIsMember.isSelected()) {
                createMember();

            } else {
                setValuesInObject();
                saveData();
            }
        }
    }

    private boolean validate() {
        errorMsg = new StringBuilder();

        if (txtMemberCode.getText() == null || txtMemberCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("membercodenullerror") + "\n");

        if (txtNoOfShare.getText() == null || txtNoOfShare.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("noofsharenullerror") + "\n");

        if (txtShareCode.getText() == null || txtShareCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("sharecodenullerror") + "\n");

        if (txtMemberName.getText() == null || txtMemberName.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("membernamenullerror") + "\n");
        return errorMsg.length() == 0;
    }


    private void createMember() {
        member = new Member();
        memberDetail = new MemberDetail();
        member.setFirstName(txtMemberName.getText());
        member.setMiddleName("");
        member.setLastName("");
        member.setFirstNameLocal(txtMemberName.getText());
        member.setActive(true);
        member.setCode(MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtMemberCode.getText())));
        member.setCodeEx(String.format("%04d", CommonUtils.strToInteger(txtMemberCode.getText())));
        member.setSociety(MainApp.identityDto.getSociety());
        MemberType m = new MemberType();
        MilkType m1 = new MilkType();
        m1.setCode(1);

        m.setCode(1);
        member.setMemberType(m);
        member.setMilkType(m1);
        member.setMobileNo("");
        member.setMiddleNameLocal("");
        member.setLastNameLocal("");
        memberDetail.setUnionCode(MainApp.identityDto.getUnion().getCode());
        memberDetail.setRegistrationDate(null);
        Gender gender = new Gender();
        gender.setCode(1);
        memberDetail.setGender(gender);
        memberDetail.setBirthDate(null);
        memberDetail.setAddress("");
        memberDetail.setPincode("");
        memberDetail.setState(null);
        memberDetail.setDistrict(null);
        memberDetail.setSubDistrict(null);
        memberDetail.setVillage(null);
        memberDetail.setHamlet(null);
        memberDetail.setEmail("");
        memberDetail.setPanNo("");
        memberDetail.setAadharNo("");
        //ahiyalakho


        MemberDetail memberDetail = new MemberDetail();
        memberDetail.setNumberOfCow(null);
        memberDetail.setNumberOfBuffalo(null);
        memberDetail.setPaymentMode(null);
        memberDetail.setBank(null);
        memberDetail.setBranch(null);
        memberDetail.setAccountNo("");
        memberDetail.setIfsc("");
        memberDetail.setMember(member);
        memberDetail.setCode(member.getCode());


        memberDetail.setMember(member);

        memberDetail.setAccountNo("");

        List<MemberDto> dtoList = new ArrayList<>();
        dtoList.add(new MemberDto(member, memberDetail));
        MemberListSaveTask task = new MemberListSaveTask(dtoList);
        task.setOnSucceeded(e -> {
            setValuesInObject();
            saveData();
        });
        new Thread(task).start();
    }

    private void getShareAmount(LocalDate date) {
        ShareRateByWefDateLoadTask task = new ShareRateByWefDateLoadTask(date);
        task.setOnSucceeded(e -> {
            try {
                shareAmount = task.get().getShareAmount();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void setValuesInObject() {
        try {
            share = new Share();
            share.setCode(txtVoucherNo.getText());
            share.setIssueDate(dpDate.getValue());
            share.setNoOfShare(Integer.valueOf(txtNoOfShare.getText()));
            share.setShareAmount(new BigDecimal(txtAmount.getText()));
            share.setShareCode(String.format("%04d", CommonUtils.strToInteger(txtShareCode.getText())));
            share.setCheckMember(chkIsMember.isSelected());
            share.setCancelled(false);
            share.setTransferred(false);
            share.setCertificateNo(txtCertificateNo.getText());
            share.setLedgerNo(txtLedgerNo.getText());
            share.setRefund(false);
            share.setActive(true);
            share.setNoOfRefundShare(0);
            share.setNoOfTransferredShare(0);
            share.setUnitCost(shareAmount);
            share.setSociety(MainApp.identityDto.getSociety());
            share.setUnionCode(MainApp.identityDto.getUnion().getCode());
            share.setMember(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        ShareIssueSaveTask task = new ShareIssueSaveTask(share, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("share"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("share"),
                        resourceBundle.getString("share.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        this.callback.reloadData(true);
        this.stage.close();
        new Thread(task).start();
    }


    private void setConsumerName(String text) {
        txtMemberName.setText("");
        var task = new MemberByIdLoadTask(text);
        task.setOnSucceeded(e -> {
            try {
                member = task.get();
                if (member != null) {

                    txtMemberName.setText(member.toMemberName());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void getNextCode() {
        ShareGetNextCodeTask task = new ShareGetNextCodeTask();
        task.setOnSucceeded(e -> {
            try {
                String code = task.get();
                if (code != null) {
                    txtVoucherNo.setText(code);
                }
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }
}
