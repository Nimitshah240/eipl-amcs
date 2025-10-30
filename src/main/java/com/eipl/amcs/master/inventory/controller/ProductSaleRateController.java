package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.task.ProductSaleRateDeleteTask;
import com.eipl.amcs.master.inventory.task.ProductSaleRateLoadTask;
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

public class ProductSaleRateController implements MyInitialization, PopupCallback {

    private final ObjectProperty<ProductSaleRate> propSaleRateDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<ProductSaleRate> tableProductSaleRate;
    @FXML
    TableColumn<ProductSaleRate, Number> colRate, colSecretaryCommissionRate;
    @FXML
    TableColumn<ProductSaleRate, String> colCode;
    @FXML
    TableColumn<ProductSaleRate, LocalDate> colWefDate;
    @FXML
    TableColumn<ProductSaleRate, Product> colProduct;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;
    private ResourceBundle resourceBundle;

    public ProductSaleRateController() {
        propSaleRateDto = new SimpleObjectProperty<>();
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
        propSaleRateDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_RATE_EDIT"))
                throw new UnAuthorizedAccessException();
            ProductSaleRate dto = propSaleRateDto.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductSaleRateAddEdit", dto, this);
        });
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_RATE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductSaleRateAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_RATE_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colSecretaryCommissionRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSecretaryCommissionRate()));
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate()));
            colWefDate.setCellFactory(new LocalDateCellFactory<>());

            colProduct.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct()));
            propSaleRateDto.bind(tableProductSaleRate.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void loadData() {
        tableProductSaleRate.setItems(null);
        ProductSaleRateLoadTask task = new ProductSaleRateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<ProductSaleRate> list = task.get();
                if (list != null)
                    tableProductSaleRate.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            ProductSaleRate dto = propSaleRateDto.get();
            if (dto != null) {
                var task = new ProductSaleRateDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
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
