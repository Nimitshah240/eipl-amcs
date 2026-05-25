package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;

import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.Message;
import com.eipl.amcs.operation.administartion.task.MessageDeleteTask;
import com.eipl.amcs.operation.administartion.task.MessageLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MessageController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Message> propMessageDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<Message> tableMessage;
    @FXML
    TableColumn<Message, String> colFromDate, colFromShift, colToDate, colToShift, colMessage, colMessageLocal;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    Button btnSearch, btnAdd, btnEdit, btnDelete, btnClose;
    private ResourceBundle resourceBundle;


    public MessageController() {
        propMessageDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propMessageDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        setupTable();
        loadData();
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MessageAddEdit", null, this, resourceBundle.getString("message"));
        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            Message dto = propMessageDto.get();
            if (dto != null)
                editMessage(dto);
        });
    }

    private void editMessage(Message message) {
        if (message != null)
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MessageAddEdit", message, this, resourceBundle.getString("message"));
    }

    @Override
    public void setupTable() {
        try {
            colFromDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFromDate().toString()));
            colFromShift.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFromShift().getName()));
            colToDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getToDate().toString()));
            colToShift.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getToShift().getName()));
            colMessage.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMessage()));
            colMessageLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMessageLocal()));
            propMessageDto.bind(tableMessage.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("Message setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        MessageLoadTask task = new MessageLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Message> list = task.get();
                if (list != null) {
                    tableMessage.setItems(FXCollections.observableList(list));
                } else {
                    tableMessage.setItems(FXCollections.observableArrayList());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("message"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Message dto = propMessageDto.get();
            if (dto != null) {
                var task = new MessageDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("message"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        reloadData(true);
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}