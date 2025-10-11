package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.service.ProductSaleRateService;
import com.eipl.amcs.master.inventory.service.ProductService;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class ProductSaleRateController implements MyInitialization, PopupCallback {

    @FXML
    AnchorPane root;
    @FXML
    TableView<ProductSaleRate> tableProductSaleRate;
    @FXML
    TableColumn<ProductSaleRate, Number> colRate, colSecretaryCommissionRate;
    @FXML
    TableColumn<ProductSaleRate, String> colCode, colPurchaseCode;
    @FXML
    TableColumn<ProductSaleRate, LocalDate> colWefDate;
    @FXML
    TableColumn<ProductSaleRate, Society> colSociety;
    @FXML
    TableColumn<ProductSaleRate, Product> colProduct;
    @FXML
    TableColumn<ProductSaleRate, Union> colUnion;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;

    private ResourceBundle resourceBundle;
    private final ObjectProperty<ProductSaleRate> propSaleRateDto;

    private ProductSaleRateService productSaleRateService;
    private ProductService productService;

    public ProductSaleRateController() {
        productService = context.getBean(ProductService.class);
        propSaleRateDto = new SimpleObjectProperty<>();
        productSaleRateService = context.getBean(ProductSaleRateService.class);
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
        try {
            List<ProductSaleRate> list = productSaleRateService.findAll();
            if (list != null)
                tableProductSaleRate.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteData() {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                ProductSaleRate dto = propSaleRateDto.get();
                if (dto != null) {
                    Optional<ProductSaleRate> productData = productSaleRateService.findById(dto.getCode());
                    if (productData == null || !productData.isPresent())
                        return;
                    productSaleRateService.delete(productData.get(), CommonUtil.setIdentityHeader());
                    loadData();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
