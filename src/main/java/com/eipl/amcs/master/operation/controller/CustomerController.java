package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.task.CustomerDeleteTask;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.utils.CommonUtils;
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

public class CustomerController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<Customer> tableCustomer;
    @FXML
    TableColumn<Customer, String> colCode, colName, colLocalName, colMobileNo, colStatus, colType;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;
    private ResourceBundle resourceBundle;
    private final ObjectProperty<Customer> propCustomer;
    public CustomerController() {
        propCustomer = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_CUSTOMER_ADD"))
                throw new UnAuthorizedAccessException();
            CustomerAddEditController controller = (CustomerAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/operation/CustomerAddEdit.fxml"));
            controller.setCustomer(null);
            MainApp.getContentPane().setCenter(controller.getRoot());
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_CUSTOMER_EDIT"))
                throw new UnAuthorizedAccessException();
            if (propCustomer.get() != null) {
                CustomerAddEditController controller = (CustomerAddEditController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/master/operation/CustomerAddEdit.fxml"));
                controller.setCustomer(propCustomer.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            }
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_CUSTOMER_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });

        propCustomer.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
    }

    @Override
    public void setupTable() {
        try {
            colType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getCustomerTypeStrFromShort(data.getValue().getType().shortValue())));
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
//        colType.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getType()==3?"Customer":data.getValue().getType()==4?"Institute":"Consumer"));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMobileNo()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
            propCustomer.bind(tableCustomer.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("BillCriteria setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableCustomer.setItems(null);
        CustomerLoadTask task = new CustomerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Customer> list = task.get();
                if (list != null)
                    tableCustomer.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Customer dto = propCustomer.get();
            if (dto != null) {
                var task = new CustomerDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("customer"),
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