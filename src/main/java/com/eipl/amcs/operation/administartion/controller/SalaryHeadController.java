package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.operation.administartion.dto.StaffSalaryHead;
import com.eipl.amcs.operation.administartion.task.StaffSalaryHeadDeleteTask;
import com.eipl.amcs.operation.administartion.task.StaffSalaryHeadLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SalaryHeadController implements MyInitialization, PopupCallback {
    @FXML
    AnchorPane root;
    @FXML
    TableView<StaffSalaryHead> tableSalaryHead;
    @FXML
    TableColumn<StaffSalaryHead, String> colType, colName;

    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;

    private ResourceBundle resourceBundle;

    private final ObjectProperty<StaffSalaryHead> propStaffSalaryHeadDto;


    public SalaryHeadController() {
        propStaffSalaryHeadDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propStaffSalaryHeadDto.addListener((observable, oldValue, newValue) -> {
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
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SalaryHeadAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {

            deleteData();
        });
        btnEdit.setOnAction(e -> {
            StaffSalaryHead dto = propStaffSalaryHeadDto.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SalaryHeadAddEdit", dto, this);
        });
    }

    @Override
    public void setupTable() {
        try {
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colType.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getType() == 0 ? "DEDUCTION" : "ADDITION"));
            propStaffSalaryHeadDto.bind(tableSalaryHead.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("SalaryHead setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableSalaryHead.setItems(null);
        StaffSalaryHeadLoadTask task = new StaffSalaryHeadLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<StaffSalaryHead> list = task.get();
                if (list != null)
                    tableSalaryHead.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("staffsalaryhead"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            StaffSalaryHead dto = propStaffSalaryHeadDto.get();
            if (dto != null) {
                var task = new StaffSalaryHeadDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || respDelete.booleanValue() == false) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffsalaryhead"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
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
