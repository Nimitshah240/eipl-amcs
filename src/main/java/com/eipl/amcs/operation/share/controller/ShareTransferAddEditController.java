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
import com.eipl.amcs.operation.share.task.ShareByShareCodeLoadTask;
import com.eipl.amcs.operation.share.task.ShareGetNextCodeTask;
import com.eipl.amcs.operation.share.task.ShareTransferSaveTask;
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
import java.util.stream.Collectors;

public class ShareTransferAddEditController implements MyInitialization {

    private PopupCallback callback;
    @FXML
    private StackPane root;
    private Stage stage;
    @FXML
    private E_DatePicker dpDate;
    @FXML
    private E_TextField txtVoucherNo, txtOldMemberCode, txtShareCode, txtNewMemberCode, txtNoOfShare, txtCertificateNo, txtAmount, txtOldMemberName, txtNewMemberName;

    public ResourceBundle resourceBundle;
    @FXML
    private Button btnSave, btnClose;
    @FXML
    private CheckBox chkIsMember;
    Integer oldShareNo = 0;
    BigDecimal oldShareAmount = BigDecimal.ZERO;
    private Share oldShare = new Share();
    private MemberDetail memberDetail;
    private Member member;

    private List<Share> oldShareList = new ArrayList<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    private Share newShare = new Share();
    private StringBuilder errorMsg = null;
    private Member oldMember;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        getNextCode();

        btnSave.setOnAction(e -> {
            validateAndSave();
        });
        FocusUtils.requestFocus(txtOldMemberCode);
        txtNoOfShare.textProperty().addListener((observable, oldValue, newValue) -> {

        });
        txtCertificateNo.textProperty().addListener((observable, oldValue, newValue) -> {
            FocusUtils.requestFocus(btnSave);
        });
        txtOldMemberCode.textProperty().addListener((observable, oldValue, newValue) -> {
//            String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtOldMemberCode.getText()));
            String code = String.format("%04d", CommonUtils.strToInteger(txtOldMemberCode.getText()));

            loadShareByMember(code);
        });
        txtNewMemberCode.textProperty().addListener((observable, oldValue, newValue) -> {
            String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtNewMemberCode.getText()));
            setConsumerNewName(code);
        });

        chkIsMember.setOnAction(event -> {
            if (chkIsMember.isSelected()) {
                txtNewMemberName.setDisable(true);
            } else {
                txtNewMemberName.setDisable(false);
            }
        });
        dpDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> this.stage.close());

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

    private void setConsumerName(String text) {
        txtOldMemberName.setText("");
        var task = new MemberByIdLoadTask(text);
        task.setOnSucceeded(e -> {
            try {
                oldMember = task.get();
                if (oldMember != null) {

                    txtOldMemberName.setText(oldMember.toMemberName());
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setConsumerNewName(String text) {
        txtNewMemberName.setText("");
        var task = new MemberByIdLoadTask(text);
        task.setOnSucceeded(e -> {
            try {
                member = task.get();
                if (member != null) {
                    txtNewMemberName.setText(member.toMemberName());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInObject() {
        try {
            if (txtNoOfShare.getText() != null && !txtNoOfShare.getText().equalsIgnoreCase("") && Double.parseDouble(txtNoOfShare.getText()) > 0) {
                newShare.setCode(txtVoucherNo.getText());
                newShare.setIssueDate(dpDate.getValue());
                newShare.setNoOfShare(oldShareNo);
                newShare.setShareAmount(oldShareAmount);
                newShare.setCertificateNo(txtCertificateNo.getText());
                newShare.setCancelled(false);
                newShare.setTransferred(false);
                newShare.setRefund(false);
                newShare.setShareCode(String.format("%04d", CommonUtils.strToInteger(txtShareCode.getText())));
                newShare.setCheckMember(chkIsMember.isSelected());
                newShare.setActive(true);
                newShare.setTransferredFrom(oldMember);
                newShare.setTransferredFromCode(oldShare.getShareCode());
                newShare.setNoOfRefundShare(0);
                newShare.setNoOfTransferredShare(0);
                newShare.setUnitCost(oldShare.getUnitCost());
                newShare.setLedgerNo(oldShare.getLedgerNo());
                newShare.setUnionCode(MainApp.identityDto.getUnion().getCode());
                newShare.setMember(member);
                newShare.setxCol1(oldShare.getCode());
                newShare.setSociety(MainApp.identityDto.getSociety());

//                saveData();
            } else {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "share"),
                        CommonUtils.getResourceString(resourceBundle, "share.transfer"));
                alert.createAlert();
//            popup
                System.out.println("he");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharetransfer"),
                    errorMsg.toString());
            alert.createAlert();
            return;
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

        if (txtOldMemberCode.getText() == null || txtOldMemberCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("oldmembernullerror") + "\n");

        if (txtNoOfShare.getText() == null || txtNoOfShare.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("noofsharenullerror") + "\n");

        if (txtShareCode.getText() == null || txtShareCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("sharecodenullerror") + "\n");

        if (txtOldMemberName.getText() == null || txtOldMemberName.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("oldmembernamenullerror") + "\n");

        if (txtNewMemberCode.getText() == null || txtNewMemberCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("newmembercodenullerror") + "\n");

        if (txtNewMemberName.getText() == null || txtNewMemberName.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("newmembernamenullerror") + "\n");

        return errorMsg.length() == 0;
    }

    private void createMember() {
        member = new Member();
        memberDetail = new MemberDetail();
        member.setFirstName(txtNewMemberName.getText());
        member.setMiddleName("");
        member.setLastName("");
        member.setFirstNameLocal(txtNewMemberName.getText());
        member.setActive(true);
        member.setCode(MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtNewMemberCode.getText())));
        member.setCodeEx(String.format("%04d", CommonUtils.strToInteger(txtNewMemberCode.getText())));
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

    public void saveData() {
        ShareTransferSaveTask task = new ShareTransferSaveTask(newShare, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharetransfer"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("sharetransfer"),
                        resourceBundle.getString("sharetransfer.insert.successful"));
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

    void loadShareByMember(String code) {
            ShareByShareCodeLoadTask task = new ShareByShareCodeLoadTask(code);
            task.setOnSucceeded(e -> {
                try {
                    oldShareList = task.get().stream().filter(e1 -> !e1.getTransferred()).collect(Collectors.toList());
                    oldShare = oldShareList.get(0);
                    for (Share share : oldShareList) {
                        oldShareAmount = oldShareAmount.add(share.getShareAmount());
                        oldShareNo = oldShareNo + share.getNoOfShare();
                    }
                    txtNoOfShare.setText(String.valueOf(oldShareNo));
                    txtAmount.setText(String.valueOf(oldShareAmount));
                    setConsumerName(oldShare.getMember().getCode());
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
    }

}
