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

import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.model.Message;
import com.eipl.amcs.operation.administartion.task.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MessageAddEditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private E_ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private E_DatePicker dpFromDate, dpToDate;
    @FXML
    private E_TextField txtMessage, txtMessageGuj;
    @FXML
    private Button btnSaveUpdate, btnClose;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private Message message;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadShifts();

        btnSaveUpdate.setOnAction(e -> validateAndSave());
        if (btnClose != null) {
            btnClose.setOnAction(e -> {
                if (this.callback != null) {
                    this.callback.reloadData(true);
                }
                this.stage.close();
            });
        }
    }

    public void setMessage(Message dto) {
        if (dto != null) {
            this.message = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        }
    }

    public void loadControls() { //Edit
        txtMessage.setText(message.getMessage());
        txtMessageGuj.setText(message.getMessageLocal());
        dpFromDate.setValue(message.getFromDate());
        dpToDate.setValue(message.getToDate());
        cboxFromShift.getSelectionModel().select(message.getFromShift());
        cboxToShift.getSelectionModel().select(message.getToShift());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("message"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.message != null) {
                message = setValuesInObject();
                updateData();
            }
        } else {
            message = new Message();
            message = setValuesInObject();
            saveData();
        }
    }

    private Message setValuesInObject() {
        message.setFromDate(dpFromDate.getValue());
        message.setToDate(dpToDate.getValue());
        message.setFromShift(cboxFromShift.getValue());
        message.setToShift(cboxToShift.getValue());
        message.setMessage(txtMessage.getText());
        message.setMessageLocal(txtMessageGuj.getText());

        return message;
    }

    private boolean validate() {
        if (dpFromDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("fromdatenullerror") + "\n");
        if (dpToDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("todatenullerror") + "\n");
        if (cboxFromShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("fromshiftnullerror") + "\n");
        if (cboxToShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("toshiftnullerror") + "\n");
        if (txtMessage.getText() == null || txtMessage.getText().isBlank())
            errorMsg.append(resourceBundle.getString("messagenullerror") + "\n");
        return errorMsg.length() == 0;
    }

    private void loadShifts() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    cboxFromShift.setItems(FXCollections.observableList(list));
                    cboxToShift.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {}

    @Override
    public void saveData() {
        var task = new MessageSaveTask(message, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("message"),
                        resourceBundle.getString("message.insert.successful"));
                alert.createAlert();
                if (this.callback != null) {
                    this.callback.reloadData(true);
                }
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("message"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new MessageSaveTask(message, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("message"),
                        resourceBundle.getString("message.update.successful"));
                alert.createAlert();
                if (this.callback != null) {
                    this.callback.reloadData(true);
                }
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("message"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }
}