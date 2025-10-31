package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.task.ProductReceiptDeleteTask;
import com.eipl.amcs.operation.inventory.task.ProductReceiptLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProductReceiptController implements MyInitialization {
    private final ObjectProperty<ProductReceipt> propProductReceiptDto;
    @FXML
    StackPane root;
    @FXML
    TableView<ProductReceipt> tableProductReceipt;
    @FXML
    TableColumn<ProductReceipt, String> colGrnNo, colChallanNo;
    @FXML
    TableColumn<ProductReceipt, Customer> colParty;
    @FXML
    TableColumn<ProductReceipt, Number> colAmount;
    @FXML
    TableColumn<ProductReceipt, LocalDate> colGrnDate, colChallanDate;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnSearch;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    private ResourceBundle resourceBundle;

    public ProductReceiptController() {
        propProductReceiptDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });

        this.resourceBundle = resourceBundle;


        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        loadData();
        setupTable();
        btnSearch.setOnAction(e -> loadData());
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_RECEIPT_ADD"))
                throw new UnAuthorizedAccessException();
            var controller = (ProductReceiptAddEditController) MainApp.getFxmlLoaderUtil()
                    .loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductReceiptAddEdit.fxml"));
            controller.setProductReceipt(null);
            MainApp.contentPane.setCenter(controller.getRoot());
        });
        propProductReceiptDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_RECEIPT_EDIT"))
                throw new UnAuthorizedAccessException();
            var controller = (ProductReceiptAddEditController) MainApp.getFxmlLoaderUtil()
                    .loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductReceiptAddEdit.fxml"));
            controller.setProductReceipt(propProductReceiptDto.get());
            MainApp.contentPane.setCenter(controller.getRoot());
        });
        btnDelete.setOnAction(actionEvent -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_RECEIPT_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

    @Override
    public void setupTable() {
        try {
            dpFromDate.setConverter(new LocalDateConvertor());
            dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
                }
            });
            dpToDate.setConverter(new LocalDateConvertor());
            dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
                }
            });

            colGrnNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGrnNo().replace(MainApp.identityDto.getSociety().getCode() + "/", "")));
            colGrnDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getGrnDate()));
            colGrnDate.setCellFactory(new LocalDateCellFactory<>());
            colChallanNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChallanNo()));
            colChallanDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getChallanDate()));
            colChallanDate.setCellFactory(new LocalDateCellFactory<>());
            colParty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCustomer()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNetAmount()));
            propProductReceiptDto.bind(tableProductReceipt.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("ProductReceipt setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableProductReceipt.setItems(null);
        ProductReceiptLoadTask task = new ProductReceiptLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<ProductReceipt> list = task.get();
                if (list != null)
                    tableProductReceipt.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("product.receipt"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            ProductReceipt dto = propProductReceiptDto.get();
            if (dto != null) {
                var task = new ProductReceiptDeleteTask(dto.getGrnNo());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product.receipt"),
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
