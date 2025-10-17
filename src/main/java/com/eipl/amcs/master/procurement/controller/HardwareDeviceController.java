package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.master.procurement.task.HardwareDeviceLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class HardwareDeviceController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<HardwareDevice> tableHardwareDevices;
    @FXML
    TableColumn<HardwareDevice, String> colCode;
    @FXML
    TableColumn<HardwareDevice, String> colDeviceName, colDeviceType, colTareChar, colStartChar, colEndChar;
    @FXML
    TableColumn<HardwareDevice, Number> colBitRate, colBaudRate, colParity;
    @FXML
    Button btnClose;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadData();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colDeviceName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDeviceName()));
            colEndChar.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndChar()));
            colStartChar.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartChar()));
            colTareChar.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTareChar()));
            colBaudRate.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBaudRate()));
            colDeviceType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getDeviceType(data.getValue().getDeviceType())));
            colParity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getParity()));
            colBitRate.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBitRate()));
        } catch (Exception e) {
            System.out.println("HardwareDevice setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new HardwareDeviceLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<HardwareDevice> list = task.get();
                if (list != null)
                    tableHardwareDevices.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
