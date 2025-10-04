package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateDeleteTask;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateLoadTask;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProductPurchaseRateController implements MyInitialization, PopupCallback {

    @FXML
    AnchorPane root;
    @FXML
    TableView<ProductPurchaseRate> tableProductPurchaseRate;
    @FXML
    TableColumn<ProductPurchaseRate, Number> colRate;
    @FXML
    TableColumn<ProductPurchaseRate, String> colCode;
    @FXML
    TableColumn<ProductPurchaseRate, LocalDate> colWefDate;
    @FXML
    TableColumn<ProductPurchaseRate, Society> colSociety;
    @FXML
    TableColumn<ProductPurchaseRate, Product> colProduct;
    @FXML
    TableColumn<ProductPurchaseRate, Union> colUnion;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;

    private ResourceBundle resourceBundle;
    private final ObjectProperty<ProductPurchaseRate> propPurchaseRateDto;

    public ProductPurchaseRateController() {
        propPurchaseRateDto = new SimpleObjectProperty<>();
    }


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resourceBundle=resourceBundle;
        setupTable();
        loadData();
        propPurchaseRateDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_PURCHASE_RATE_EDIT"))
                throw new UnAuthorizedAccessException();
            ProductPurchaseRate dto = propPurchaseRateDto.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductPurchaseRateAddEdit", dto, this);
        });
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_PURCHASE_RATE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductPurchaseRateAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_PURCHASE_RATE_DELETE"))
                throw new UnAuthorizedAccessException();
                deleteData();
    });
    }

    @Override
    public void setupTable() {
        try {
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate()));
            colWefDate.setCellFactory(new LocalDateCellFactory<>());

            colProduct.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct()));
            propPurchaseRateDto.bind(tableProductPurchaseRate.getSelectionModel().selectedItemProperty());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableProductPurchaseRate.setItems(null);
        ProductPurchaseRateLoadTask task = new ProductPurchaseRateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<ProductPurchaseRate> list = task.get();
                if (list != null)
                    tableProductPurchaseRate.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("productpurchaserate"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            ProductPurchaseRate dto = propPurchaseRateDto.get();
            if (dto != null) {
                var task = new ProductPurchaseRateDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || respDelete.booleanValue() == false) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productpurchaserate"),
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
