package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.service.ProductPurchaseRateService;
import com.eipl.amcs.master.inventory.service.ProductService;
import com.eipl.amcs.util.CommonUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class ProductPurchaseRateAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<Product> cboxProduct;
    @FXML
    private TextField txtPurchaseRate;
    @FXML
    private GridPane grid;
    @FXML
    private DatePicker dpWefDate;

    private Stage stage;
    private ResourceBundle resourceBundle;
    private PopupCallback callback;
    private StringBuilder errorMsg = null;
    private ProductPurchaseRate dto = null;

    private ProductPurchaseRateService productPurchaseRateService;
    private ProductService productService;
    private NextCodeService nextCodeService;

    public ProductPurchaseRateAddEditController() {
        productPurchaseRateService = context.getBean(ProductPurchaseRateService.class);
        productService = context.getBean(ProductService.class);
        nextCodeService = context.getBean(NextCodeService.class);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setProductPurchaseRate(ProductPurchaseRate dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        }
        loadProduct();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        loadProduct();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    @Override
    public void setupComboBox() {
        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
        dpWefDate.setConverter(new LocalDateConvertor());
        dpWefDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpWefDate.setValue(dpWefDate.getConverter().fromString(dpWefDate.getEditor().getText()));
            }
        });
        dpWefDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpWefDate.setValue(dpWefDate.getConverter().fromString(dpWefDate.getEditor().getText()));
            }
        });
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productpurchaserate"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            dto = setValuesInObjectUpdate();
            updateData();
        } else {
            dto = setValuesInObject();
            saveData();
        }
    }

    private ProductPurchaseRate setValuesInObject() {
        ProductPurchaseRate productPurchaseRate = new ProductPurchaseRate();
        productPurchaseRate.setProduct(cboxProduct.getValue());
        productPurchaseRate.setSociety(MainApp.identityDto.getSociety());
        productPurchaseRate.setUnion(MainApp.identityDto.getUnion());
        productPurchaseRate.setWefDate(dpWefDate.getValue());
        productPurchaseRate.setRate(new BigDecimal(txtPurchaseRate.getText()));
        return productPurchaseRate;
    }

    private ProductPurchaseRate setValuesInObjectUpdate() {
        dto.setProduct(cboxProduct.getValue());
        dto.setWefDate(dpWefDate.getValue());
        dto.setRate(new BigDecimal(txtPurchaseRate.getText()));
        return dto;
    }

    @Override
    public void loadControls() {
        cboxProduct.getSelectionModel().select(dto.getProduct());
        txtPurchaseRate.setText(String.valueOf(dto.getRate()));
        dpWefDate.setValue(dto.getWefDate());

    }

    private boolean validate() {
        if (cboxProduct.getValue() == null)
            errorMsg.append("Product can not be null or empty\n");
        if (txtPurchaseRate.getText() == null)
            errorMsg.append("Union can not be null or empty\n");
        if (dpWefDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("wefdatenullerror") + "\n");
        try {
            if (Double.parseDouble(txtPurchaseRate.getText()) <= 0 || Double.parseDouble(txtPurchaseRate.getText()) >= 1000000)
                errorMsg.append(resourceBundle.getString("entervalidrate") + "\n");
            Double.parseDouble(txtPurchaseRate.getText().trim());
        } catch (NumberFormatException e) {
            errorMsg.append(resourceBundle.getString("purchaseratenullerror") + "\n");
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        try {
            productPurchaseRateService.save(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productpurchaserate"),
                    resourceBundle.getString("productpurchaserate.insert.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateData() {
        try {
            productPurchaseRateService.update(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productpurchaserate"),
                    resourceBundle.getString("productpurchaserate.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getNextProductPurchaseRateNumber(ProductPurchaseRate productPurchaseRate) {
        try {
            if (productPurchaseRate == null)
                return;
            String nextCode = nextCodeService.getNextCode("ProductPurchaseRate", "code", productPurchaseRate.getCode(), 4);
            if (nextCode == null || nextCode.isEmpty())
                return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadProduct() {
        try {
            List<Product> list = productService.findAllBySociety(MainApp.identityDto.getSociety().getCode());
            if (list != null)
                cboxProduct.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
	