package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.operation.administartion.task.MeetingAgendaNumberLoadTask;
import com.eipl.amcs.operation.administartion.task.MeetingAgendaSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MeetingAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSave;

    @FXML
    private E_ComboBox<String> cboxMeetingType;
    @FXML
    private E_TextField txtCode, txtMeetingTime, txtSubjectLine;
    @FXML
    private TextArea txtAgendaDetails;

    @FXML
    private E_DatePicker dpDate, dpMeetingDate;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private MeetingAgenda dto = null;

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


    public void setMeeting(MeetingAgenda dto) {
        if (dto != null) {
            this.dto = dto;
            btnSave.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextMemberCode();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        cboxMeetingType.getItems().addAll(CommonUtils.meetingTypeList);
        dpDate.setValue(LocalDate.now());
        dpMeetingDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> this.stage.close());
        btnSave.setOnAction(e -> validateAndSave());
        setupComboBox();
    }


    public void loadControls() {
        cboxMeetingType.getSelectionModel().select(dto.getMeetingType());
        txtCode.setText(dto.getCode());
        txtMeetingTime.setText(dto.getMeetingTime());
        txtAgendaDetails.setText(dto.getDetailedAgenda());
        txtSubjectLine.setText(dto.getSubjectLine());
        dpDate.setValue(dto.getDate());
        dpMeetingDate.setValue(dto.getMeetingDate());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSave.getText().equals(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                dto = setValuesInObject();
                updateData();
            }
        } else {
            dto = new MeetingAgenda();
            dto = setValuesInObject();
            saveData();
        }
    }

    private MeetingAgenda setValuesInObject() {
        dto.setDate(dpDate.getValue());
        dto.setMeetingDate(dpMeetingDate.getValue());
        dto.setMeetingType(CommonUtils.getMeetingTypeFromString(cboxMeetingType.getValue()));
        dto.setCode(txtCode.getText());
        dto.setMeetingTime(txtMeetingTime.getText());
        dto.setSubjectLine(txtSubjectLine.getText());
        dto.setDetailedAgenda(txtAgendaDetails.getText());
        dto.setUnion(MainApp.identityDto.getUnion());
        dto.setSociety(MainApp.identityDto.getSociety());
        return dto;
    }

    private void getNextMemberCode() {
        var task = new MeetingAgendaNumberLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(event -> {
            try {
                String no = task.get();
                System.out.println(no);
                txtCode.setText(no);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private boolean validate() {
        if (cboxMeetingType.getValue() == null)
            errorMsg.append(resourceBundle.getString("meetingtypenullerror") + "\n");
        if (txtSubjectLine.getText().trim() == null || txtSubjectLine.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("subjectlinenullerror") + "\n");
        if (txtAgendaDetails.getText().trim() == null || txtAgendaDetails.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("agendadetailsnullerror") + "\n");
        return errorMsg.length() == 0;

    }

    @Override
    public void saveData() {
        var task = new MeetingAgendaSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                        resourceBundle.getString("meeting.insert.successful"));
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
        var task = new MeetingAgendaSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                        resourceBundle.getString("meeting.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Meeting.fxml")));
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
        cboxMeetingType.getSelectionModel().select(0);
        dpDate.setConverter(new LocalDateConvertor());
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });
        dpMeetingDate.setConverter(new LocalDateConvertor());
        dpMeetingDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpMeetingDate.setValue(dpMeetingDate.getConverter().fromString(dpMeetingDate.getEditor().getText()));
            }
        });
    }
}
