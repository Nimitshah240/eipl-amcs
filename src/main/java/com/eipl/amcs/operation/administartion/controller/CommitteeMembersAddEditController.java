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
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.operation.administartion.converter.DesignationConvertor;
import com.eipl.amcs.operation.administartion.task.CommitteeMembersSaveTask;
import com.eipl.amcs.operation.administartion.task.DesignationLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class CommitteeMembersAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_Button btnClose, btnSaveUpdate;

    @FXML
    private E_ComboBox<Designation> cboxDesignation;
    @FXML
    private E_TextField txtName, txtCode;

    @FXML
    private E_DatePicker dpElectionDate, dpTenureFromDate, dpTenureToDate;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private CommitteeMembers dto = null;

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }


    public void setCommitteeMembers(CommitteeMembers dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            // getNextMemberCode();
        }
        loadDesignation();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        loadDesignation();
        dpElectionDate.setValue(LocalDate.now());
        dpTenureToDate.setValue(LocalDate.now());
        dpTenureFromDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    public void loadControls() {
//         cboxDesignation.setConverter(new DesignationConvertor(cboxDesignation));
        cboxDesignation.getSelectionModel().select(dto.getDesignation());
        txtName.setText(dto.getMemberName());
        txtCode.setText(CommonUtils.getMemberShortCode(dto.getMemberCode()));
        dpElectionDate.setValue(dto.getElectionDate());
        dpTenureFromDate.setValue(dto.getTenureFromDate());
        dpTenureToDate.setValue(dto.getTenureToDate());
    }

    //
    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                dto = setValuesInObject();
                updateData();
            }
        } else {
            dto = new CommitteeMembers();
            dto = setValuesInObject();
            saveData();
        }
    }

    private CommitteeMembers setValuesInObject() {

        dto.setMemberCode(MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(txtCode.getText()));
        dto.setElectionDate(dpElectionDate.getValue());
        dto.setTenureFromDate(dpTenureFromDate.getValue());
        dto.setTenureToDate(dpTenureToDate.getValue());
        dto.setDesignation(cboxDesignation.getValue());
        dto.setMemberName(txtName.getText());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnionCode(MainApp.identityDto.getUnion().getCode());
        return dto;
    }

    private boolean validate() {
        if (cboxDesignation.getValue() == null)
            errorMsg.append(resourceBundle.getString("designationnullerror") + "\n");
        if (dpElectionDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("electiondateullerror") + "\n");

        if (dpTenureFromDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("tenurefromdatenullerror") + "\n");
        if (dpTenureToDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("tenuretodatenullerror") + "\n");
        if (txtName.getText().trim() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {

        var task = new CommitteeMembersSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                        resourceBundle.getString("committeemembers.insert.successful"));
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
        var task = new CommitteeMembersSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                        resourceBundle.getString("committeemembers.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/CommitteeMembers.fxml")));
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxDesignation.setConverter(new DesignationConvertor(cboxDesignation));
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


}
