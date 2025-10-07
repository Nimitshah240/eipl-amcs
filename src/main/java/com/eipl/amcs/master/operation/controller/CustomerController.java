package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.util.CommonUtil;
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

import static com.eipl.amcs.MainApp.context;

public class CustomerController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<Customer> tableCustomer;
    @FXML
    TableColumn<Customer, String> colCode, colName, colLocalName, colMobileNo, colStatus, colType;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;

    private CustomerService service;
    private NextCodeService nextCodeService;

    @Override
    public Node getRoot() {
        return root;
    }

    private ResourceBundle resourceBundle;
    private ObjectProperty<Customer> propCustomer;

    public CustomerController() {
        service = context.getBean(CustomerService.class);
        nextCodeService = context.getBean(NextCodeService.class);
        propCustomer = new SimpleObjectProperty<>();
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
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMobileNo()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
            propCustomer.bind(tableCustomer.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            tableCustomer.setItems(null);
            List<Customer> list = service.findAllBySociety(MainApp.identityDto.getSociety().getCode());
            if (list != null) tableCustomer.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Customer dto = propCustomer.get();
            if (dto != null) {
                Optional<Customer> customerData = service.findById(dto.getCode());
                if (customerData != null || customerData.isPresent()) {
                    service.delete(customerData.get().getCode(), CommonUtil.setIdentityHeader());
                    loadData();
                }
            }
        }
    }
}