package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.model.MomAction;
import com.eipl.amcs.operation.administartion.task.MomActionDeleteTask;
import com.eipl.amcs.operation.administartion.task.MomActionLoadTask;
import com.eipl.amcs.operation.administartion.task.MomActionSaveTask;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MomActionController implements MyInitialization, PopupCallback {
    private final ObjectProperty<MomAction> propAction;
    @FXML
    StackPane root;
    @FXML
    TableView<MomAction> tableActionTaken;
    @FXML
    TableColumn<MomAction, String> colCode, colAction, colMinutesOfMeeting;
    @FXML
    GridPane gridMaster;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete;
    @FXML
    TextArea txtActionTaken;
    @FXML
    ComboBox<String> cboxSubjectLine, cboxMinutesOfMeeting;
    @FXML
    private DatePicker dpDate;
    private MomAction dto;
    private Mom mom;
    private StringBuilder errorMsg = null;
    private List<MomAction> momActionList;
    private ResourceBundle resourceBundle;
    private Stage stage;
    private PopupCallback callback;

    public MomActionController() {
        propAction = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpDate.setValue(LocalDate.now());
        gridMaster.setDisable(true);
        this.resourceBundle = resourceBundle;
        propAction.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        setupTable();
        btnClose.setOnAction(e -> {
            this.stage.close();
        });
        btnAdd.setOnAction(e -> {
            btnEdit.setDisable(true);
            btnDelete.setDisable(true);
            if (btnAdd.getText().equalsIgnoreCase(resourceBundle.getString("add"))) {
                gridMaster.setDisable(false);
                cboxSubjectLine.setDisable(true);
                cboxMinutesOfMeeting.setDisable(true);
                FocusUtils.requestFocus(txtActionTaken);
                btnAdd.setText(resourceBundle.getString("save"));
            } else {
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
                FocusUtils.requestFocus(txtActionTaken);
                btnEdit.setText(resourceBundle.getString("update"));
                txtActionTaken.setText(propAction.get().getAction_taken());
            } else {
                setValuesInObjectUpdate();
                updateData();
                btnAdd.setDisable(false);
                btnEdit.setText(resourceBundle.getString("edit"));
                gridMaster.setDisable(true);
            }
        });
        btnDelete.setOnAction(e -> {
            deleteData();
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
            dto = new MomAction();
            setValuesInObject();
            saveData();
        }


    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colMinutesOfMeeting.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMom().getMom()));
            colAction.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAction_taken()));
            propAction.bind(tableActionTaken.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("MomAction setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableActionTaken.setItems(null);
        MomActionLoadTask task = new MomActionLoadTask(mom.getCode());
        task.setOnSucceeded(e -> {
            try {
                momActionList = task.get();
                if (momActionList != null)
                    tableActionTaken.setItems(FXCollections.observableList(momActionList));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void setValuesInObject() {
        dto = new MomAction();
        dto.setDate(dpDate.getValue());
        dto.setMom(mom);
        dto.setMeetingType((short) 1);
        dto.setMeetingAgenda(mom.getMeetingAgenda());
        dto.setAction_taken(txtActionTaken.getText());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnion(MainApp.identityDto.getUnion());
    }

    private void setValuesInObjectUpdate() {
        dto = propAction.get();
        dto.setDate(dpDate.getValue());
        dto.setMom(mom);
        dto.setMeetingType((short) 1);
        dto.setMeetingAgenda(mom.getMeetingAgenda());
        dto.setAction_taken(txtActionTaken.getText());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnion(MainApp.identityDto.getUnion());
    }

    private boolean validate() {
        if (txtActionTaken.getText().trim() == null || txtActionTaken.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("actiontakennullerror") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setMom(Mom mom) {
        this.mom = mom;
        cboxMinutesOfMeeting.setValue(mom.getMom());
        cboxSubjectLine.setValue(mom.getMeetingAgenda().getSubjectLine());
        loadData();
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("actiontaken"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            MomAction dto = propAction.get();
            if (dto != null) {
                var task = new MomActionDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("actiontaken"),
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
        var task = new MomActionSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("actiontaken"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("actiontaken"),
                        resourceBundle.getString("actiontaken.insert.successful"));
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
        var task = new MomActionSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("actiontaken"),
                            sb.toString());
                    alert.createAlert();

                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("actiontaken"),
                        resourceBundle.getString("actiontaken.update.successful"));
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
    public void clearControls() {
        txtActionTaken.setText("");
    }
}
