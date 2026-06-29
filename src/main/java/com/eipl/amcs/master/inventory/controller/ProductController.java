package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductDeleteTask;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.utils.TableLocalizationUtil;
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

public class ProductController implements MyInitialization, PopupCallback {
    private final ObjectProperty<Product> propProductDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<Product> tableProduct;
    @FXML
    TableColumn<Product, String> colCode, colName, colLocalName, colReferenceCode;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;
    private ResourceBundle resourceBundle;

    public ProductController() {
        propProductDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propProductDto.addListener((observable, oldValue, newValue) -> {
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
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductAddEdit", null, this, resourceBundle.getString("product"));
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            editProduct(propProductDto.get());
        });


        tableProduct.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Product data = row.getItem();
                    editProduct(data);
                }
            });
            return row;
        });

        tableProduct.setOnKeyPressed(event -> {
            Product dto = tableProduct.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propProductDto.get();
                    if (dto != null)
                        deleteData();
                    break;
                case ENTER:
                    editProduct(dto);
                    break;
            }
        });
    }

    private void editProduct(Product dto) {
        if (dto == null)
            return;
        if (dto.getCreatedBy() == null || dto.getCreatedBy().equalsIgnoreCase("SYSTEM")) {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_EDIT"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductAddEdit", dto, this, resourceBundle.getString("product"));
        } else {
            MyAlert errorAlert = new WarningAlert(MainApp.stage, resourceBundle.getString("product"), resourceBundle.getString("product.update.fail"));
            errorAlert.createAlert();
        }
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colReferenceCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReferenceCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            propProductDto.bind(tableProduct.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableProduct);
        } catch (Exception e) {
            System.out.println("Product setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableProduct.setItems(null);
        ProductLoadTask task = new ProductLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Product> list = task.get();
                if (list != null)
                    tableProduct.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        Product dto = propProductDto.get();
        if (dto == null)
            return;

        if (dto.getCreatedBy() == null || dto.getCreatedBy().equalsIgnoreCase("SYSTEM")) {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_DELETE"))
                throw new UnAuthorizedAccessException();
        } else {
            MyAlert errorAlert = new WarningAlert(MainApp.stage, resourceBundle.getString("product"), resourceBundle.getString("product.delete.fail"));
            errorAlert.createAlert();
            return;
        }

        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("product"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            var task = new ProductDeleteTask(dto.getCode());
            task.setOnSucceeded(e -> {
                try {
                    Boolean respDelete = task.get();
                    if (respDelete == null || !respDelete.booleanValue()) {
                        MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product"),
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

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
