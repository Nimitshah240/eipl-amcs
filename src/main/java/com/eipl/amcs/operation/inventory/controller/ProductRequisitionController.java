package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionDeleteTask;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionLoadTask;
import com.eipl.amcs.utils.AppConstant;
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

public class ProductRequisitionController implements MyInitialization {
    private final ObjectProperty<ProductRequisition> propProductRequisitionDto;
    @FXML
    StackPane root;
    @FXML
    TableView<ProductRequisition> tableProductRequisition;
    @FXML
    TableColumn<ProductRequisition, String> colChallanNo, colStatus;
    @FXML
    TableColumn<ProductRequisition, String> colRequisitionDate, colChallanDate;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnSearch;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    private ResourceBundle resourceBundle;

    public ProductRequisitionController() {
        propProductRequisitionDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEdit.setText(resourceBundle.getString("view"));
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
        dpFromDate.setValue(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()));
        dpToDate.setValue(LocalDate.now());
        loadData();
        setupTable();
        btnSearch.setOnAction(e -> loadData());
        btnAdd.setOnAction(e -> {
//            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_RECEIPT_ADD"))
//                throw new UnAuthorizedAccessException();
            var controller = (ProductRequisitionAddEditController) MainApp.getFxmlLoaderUtil()
                    .loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductRequisitionAddEdit.fxml"));
            controller.setProductRequisition(null);
            MainApp.contentPane.setCenter(controller.getRoot());
        });
        propProductRequisitionDto.addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
//                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
                if (!newValue.getStatus().equalsIgnoreCase("Sent")) {
                    btnEdit.setText(resourceBundle.getString("view"));
                    btnEdit.setDisable(false);
                }
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_RECEIPT_EDIT"))
                throw new UnAuthorizedAccessException();
//            if (propProductRequisitionDto.get().getStatus().equalsIgnoreCase("SENT")) {
            var controller = (ProductRequisitionAddEditController) MainApp.getFxmlLoaderUtil()
                    .loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductRequisitionAddEdit.fxml"));
            controller.setProductRequisition(propProductRequisitionDto.get());
            MainApp.contentPane.setCenter(controller.getRoot());
//            } else {
//                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product.requisition"),
//                        resourceBundle.getString("error.occurred"));
//                alert1.createAlert();
//                return;
//            }
        });
        btnDelete.setOnAction(actionEvent -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_RECEIPT_DELETE"))
                throw new UnAuthorizedAccessException();

            if (propProductRequisitionDto.get().getStatus().equalsIgnoreCase("SENT")) {
                deleteData();
            } else {
                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product.requisition"),
                        resourceBundle.getString("error.occurred"));
                alert1.createAlert();
            }
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

            colChallanNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode().replace(MainApp.identityDto.getSociety().getCode() + "/", "")));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
            colRequisitionDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRequisitionDate().format(AppConstant.Formatter6)));
//            colChallanDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRequisitionDate().format(AppConstant.Formatter6)));
            propProductRequisitionDto.bind(tableProductRequisition.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("ProductRequisition setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableProductRequisition.setItems(null);
        ProductRequisitionLoadTask task = new ProductRequisitionLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<ProductRequisition> list = task.get();
                if (list != null)
                    tableProductRequisition.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("product.requisition"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            ProductRequisition dto = propProductRequisitionDto.get();
            if (dto != null) {
                var task = new ProductRequisitionDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product.requisition"),
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
