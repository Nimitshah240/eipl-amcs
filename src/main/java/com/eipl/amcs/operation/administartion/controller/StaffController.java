package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.operation.administartion.task.StaffMembersDeleteTask;
import com.eipl.amcs.operation.administartion.task.StaffMembersLoadTask;
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

public class StaffController implements MyInitialization, PopupCallback {
    private final ObjectProperty<StaffMember> propStaffMembertDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<StaffMember> tableStaffMember;
    @FXML
    TableColumn<StaffMember, String> colDesignation, colMembername, colTenureFromDate, colTenureToDate;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnSalary;
    private ResourceBundle resourceBundle;


    public StaffController() {
        propStaffMembertDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        propStaffMembertDto.addListener((observable, oldValue, newValue) -> {
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

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "StaffMemberAddEdit", null, this, resourceBundle.getString("staff"));
        });
        btnEdit.setOnAction(e -> {
            StaffMember staffMember = propStaffMembertDto.get();
            editStaff(staffMember);
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnSalary.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "StaffSalary", null, this, resourceBundle.getString("staffsalary"));
        });

        tableStaffMember.setRowFactory(tv -> {
            TableRow<StaffMember> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    StaffMember data = row.getItem();
                    editStaff(data);
                }
            });
            return row;
        });

        tableStaffMember.setOnKeyPressed(event -> {
            StaffMember dto = tableStaffMember.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case ENTER:
                    editStaff(dto);
                    break;
                case DELETE:
                    deleteData();
                    break;
            }
        });
    }

    private void editStaff(StaffMember staffMember) {
        try {
            if (staffMember != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "StaffMemberAddEdit", staffMember, this, resourceBundle.getString("staff"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setupTable() {
        try {
            colMembername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colDesignation.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDesignation().getName()));
            colTenureFromDate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTenureFromDate()));
            colTenureToDate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTenureToDate()));
            propStaffMembertDto.bind(tableStaffMember.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("Staff setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableStaffMember.setItems(null);

        StaffMembersLoadTask task = new StaffMembersLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<StaffMember> list = task.get();
                if (list != null) {
                    tableStaffMember.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            StaffMember dto = propStaffMembertDto.get();
            if (dto != null) {
                var task = new StaffMembersDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
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
}