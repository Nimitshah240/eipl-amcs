package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.service.ProductService;
import com.eipl.amcs.util.CommonUtil;
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

public class ProductController implements MyInitialization, PopupCallback {
    @FXML
    AnchorPane root;
    @FXML
    TableView<Product> tableProduct;
    @FXML
    TableColumn<Product, String> colCode, colName, colLocalName, colReferenceCode;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;

    private ResourceBundle resourceBundle;

    private final ObjectProperty<Product> propProductDto;

    private ProductService productService;

    public ProductController() {
        propProductDto = new SimpleObjectProperty<>();
        productService = context.getBean(ProductService.class);
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
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            if (propProductDto.get().getCreatedBy() == null || propProductDto.get().getCreatedBy().equalsIgnoreCase("SYSTEM")) {
                if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_DELETE"))
                    throw new UnAuthorizedAccessException();
                deleteData();
            }
        });
        btnEdit.setOnAction(e -> {
            if (propProductDto.get().getCreatedBy() == null || propProductDto.get().getCreatedBy().equalsIgnoreCase("SYSTEM")) {
                if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_EDIT"))
                    throw new UnAuthorizedAccessException();
                Product dto = propProductDto.get();
                if (dto != null)
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductAddEdit", dto, this);
            }
        });
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colReferenceCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReferenceCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            propProductDto.bind(tableProduct.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        tableProduct.setItems(null);
        try {
            List<Product> list = productService.findAllBySociety(MainApp.identityDto.getSociety().getCode());
            if (list != null)
                tableProduct.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("product"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Product dto = propProductDto.get();
            if (dto != null) {
                try {
                    productService.delete(dto.getCode(), CommonUtil.setIdentityHeader());
                    loadData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
