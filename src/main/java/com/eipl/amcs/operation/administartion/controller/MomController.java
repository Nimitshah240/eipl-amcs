package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.operation.administartion.dto.MeetingAgenda;
import com.eipl.amcs.operation.administartion.dto.Mom;
import com.eipl.amcs.operation.administartion.task.MomDeleteTask;
import com.eipl.amcs.operation.administartion.task.MomLoadTask;
import com.eipl.amcs.operation.administartion.task.MomSaveTask;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MomController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<Mom> tableMom;
    @FXML
    TableColumn<Mom, String> colCode, colSubjectLine, colMom, colStatus;
    @FXML
    TableColumn<Mom, Integer> colDate, colMeetingType;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete, btnActionTaken;
    private MeetingAgenda meetingAgenda;
    private Mom dto;
    private List<Mom> momList;

    @FXML
    private GridPane gridMaster;
    @FXML
    private TextArea txtMinuteOfMeeting;

    @FXML
    private CheckBox chkOpen;

    private StringBuilder errorMsg = null;

    @FXML
    ComboBox<String> cboxSubjectLine;
    private ResourceBundle resourceBundle;

    private final ObjectProperty<Mom> propMomDto;

    public MomController() {
        propMomDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        chkOpen.setSelected(true);
        propMomDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                btnActionTaken.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
                btnActionTaken.setDisable(true);
            }
        });

        setupTable();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/administration/Meeting.fxml")));
        });
        btnAdd.setOnAction(e -> {
            btnEdit.setDisable(true);
            btnDelete.setDisable(true);
            if (btnAdd.getText().equalsIgnoreCase(resourceBundle.getString("add"))) {
                gridMaster.setDisable(false);
                cboxSubjectLine.setDisable(true);
                FocusUtils.requestFocus(txtMinuteOfMeeting);
                btnAdd.setText(resourceBundle.getString("save"));
            } else {
                //  setValuesInObject();
                // saveData();
                validateAndSave();
                btnAdd.setText(resourceBundle.getString("add"));
                gridMaster.setDisable(true);
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            }
        });
        btnEdit.setOnAction(e -> {
            btnAdd.setDisable(true);
            if (btnEdit.getText().equalsIgnoreCase(resourceBundle.getString("edit"))) {
                gridMaster.setDisable(false);
                cboxSubjectLine.setDisable(true);
                loadControls();
                FocusUtils.requestFocus(txtMinuteOfMeeting);
                btnEdit.setText(resourceBundle.getString("update"));
            } else {
                setValuesInObjectUpdate();
                updateData();
                btnEdit.setText(resourceBundle.getString("edit"));
                gridMaster.setDisable(true);
                btnAdd.setDisable(false);
            }
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnActionTaken.setOnAction(e -> {
            if (propMomDto.get() != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MomAction", propMomDto.get(), this);
        });

    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnAdd.getText().equals(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                setValuesInObject();
                updateData();
            }
        } else {
            dto = new Mom();
            setValuesInObject();
            saveData();
        }
    }

    private void setValuesInObjectUpdate() {
        dto = propMomDto.get();
        dto.setMeetingAgenda(meetingAgenda);
        dto.setStatus(chkOpen.isSelected() ? 1 : 0);
        dto.setMom(txtMinuteOfMeeting.getText());
        dto.setMeetingType(meetingAgenda.getMeetingType());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnion(MainApp.identityDto.getUnion());
    }


    @Override
    public void loadData() {
        tableMom.setItems(null);
        var task = new MomLoadTask(meetingAgenda.getCode());
        task.setOnSucceeded(e -> {
            try {
                momList = task.get();
                if (momList != null && !momList.isEmpty())
                    tableMom.setItems(FXCollections.observableList(momList));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colSubjectLine.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMeetingAgenda().getSubjectLine()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() == 0 ? "Close" : "Open"));
            colMom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMom()));

            propMomDto.bind(tableMom.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("Mom setuptable Exception");
            e.printStackTrace();
        }
    }

    private boolean validate() {

        if (txtMinuteOfMeeting.getText().trim() == null || txtMinuteOfMeeting.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("minutesofmeetingnullerror") + "\n");

        return errorMsg.length() == 0;

    }


    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }

    public void setStage(Stage stage) {
    }

    public void setCallback(PopupCallback callback) {
    }

    public void setMom(MeetingAgenda meetingAgenda) {
        this.meetingAgenda = meetingAgenda;
        cboxSubjectLine.setValue(meetingAgenda.getSubjectLine());
        loadData();
    }

    public void setValuesInObject() {
        dto = new Mom();
//        dto.setCode("1");
        dto.setMeetingAgenda(meetingAgenda);
        dto.setStatus(chkOpen.isSelected() ? 1 : 0);
        dto.setMom(txtMinuteOfMeeting.getText());
        dto.setMeetingType(meetingAgenda.getMeetingType());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnion(MainApp.identityDto.getUnion());
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("mom"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Mom dto = propMomDto.get();
            if (dto != null) {
                var task = new MomDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || respDelete.booleanValue() == false) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("mom"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                        clearControls();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }


    @Override
    public void saveData() {
        var task = new MomSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("mom"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mom"),
                        resourceBundle.getString("mom.insert.successful"));
                alert.createAlert();
                loadData();
                clearControls();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new MomSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("mom"),
                            sb.toString());
                    alert.createAlert();

                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mom"),
                        resourceBundle.getString("mom.update.successful"));
                alert.createAlert();
                loadData();
                clearControls();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void loadControls() {
        cboxSubjectLine.setValue(meetingAgenda.getSubjectLine());
        txtMinuteOfMeeting.setText(propMomDto.get().getMom());
        chkOpen.setSelected(propMomDto.get().getStatus() == 1);
    }

    public void clearControls() {
        txtMinuteOfMeeting.setText("");
        chkOpen.setSelected(false);
    }

}
