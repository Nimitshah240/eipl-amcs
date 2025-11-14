package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.master.inventory.task.ProductSaleRateNumberLoadTask;
import com.eipl.amcs.master.inventory.task.ProductSaleRateSaveTask;
import com.eipl.amcs.master.org.model.Society;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProductSaleRateAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<Product> cboxProduct;
    @FXML
    private E_NumericField txtSaleRate, txtPurchaseRate;

    @FXML
    private TextField txtSecretaryCommission;
    @FXML
    private DatePicker dpWefDate;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private ProductSaleRate dto = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setProductSaleRate(ProductSaleRate dto) {
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
        txtSecretaryCommission.setDisable(true);
        setupComboBox();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        dpWefDate.setValue(LocalDate.now());
        txtPurchaseRate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                calculateCommission();
            }
        });
        txtSaleRate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                calculateCommission();
            }
        });
    }

    private void calculateCommission() {
        try {
            BigDecimal saleRate = new BigDecimal(
                    txtSaleRate.getText() == null || txtSaleRate.getText().trim().isEmpty() ?
                            "0" : txtSaleRate.getText().trim()
            );

            BigDecimal purchaseRate = new BigDecimal(
                    txtPurchaseRate.getText() == null || txtPurchaseRate.getText().trim().isEmpty() ?
                            "0" : txtPurchaseRate.getText().trim()
            );

            BigDecimal commission = saleRate.subtract(purchaseRate);
            txtSecretaryCommission.setText(String.valueOf(commission));
        } catch (Exception w) {
            w.printStackTrace();
        }
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
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

    @Override
    public void setupComboBox() {
        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
        dpWefDate.setConverter(new LocalDateConvertor());
        dpWefDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpWefDate.setValue(dpWefDate.getConverter().fromString(dpWefDate.getEditor().getText()));
            }
        });
    }


    private ProductSaleRate setValuesInObject() {
        ProductSaleRate productSaleRate = new ProductSaleRate();
        productSaleRate.setProduct(cboxProduct.getValue());
        productSaleRate.setSociety(MainApp.identityDto.getSociety());
        productSaleRate.setUnion(MainApp.identityDto.getUnion());
        getNextProductSaleRateNumber(MainApp.identityDto.getSociety());
        productSaleRate.setWefDate(dpWefDate.getValue());
        productSaleRate.setRate(new BigDecimal(txtSaleRate.getText()));
        productSaleRate.setPurchaseCode(txtPurchaseRate.getText());
        productSaleRate.setSecretaryCommissionRate(new BigDecimal(txtSecretaryCommission.getText()));
        return productSaleRate;
    }

    private ProductSaleRate setValuesInObjectUpdate() {
        dto.setProduct(cboxProduct.getValue());
        dto.setWefDate(dpWefDate.getValue());
        dto.setRate(new BigDecimal(txtSaleRate.getText()));
        dto.setPurchaseCode(txtPurchaseRate.getText());
        dto.setSecretaryCommissionRate(new BigDecimal(txtSecretaryCommission.getText()));
        return dto;
    }

    @Override
    public void loadControls() {
        cboxProduct.getSelectionModel().select(dto.getProduct());
        txtPurchaseRate.setText(String.valueOf(dto.getPurchaseCode()));
        dpWefDate.setValue(dto.getWefDate());
        txtSaleRate.setText(String.valueOf(dto.getRate()));
        txtSecretaryCommission.setText(String.valueOf(dto.getSecretaryCommissionRate()));
    }

    private boolean validate() {
        if (cboxProduct.getValue() == null)
            errorMsg.append("Product can not be null or empty\n");
        if (dpWefDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("wefdatenullerror") + "\n");
        try {
            if (Double.parseDouble(txtSaleRate.getText()) <= 0)
                errorMsg.append(resourceBundle.getString("entervalidsalerate") + "\n");
        } catch (NumberFormatException e) {
            errorMsg.append(resourceBundle.getString("saleratenullerror") + "\n");
        }
        try {
            if (Double.parseDouble(txtPurchaseRate.getText()) <= 0)
                errorMsg.append(resourceBundle.getString("entervalidpurchaserate") + "\n");
        } catch (NumberFormatException e) {
            errorMsg.append(resourceBundle.getString("purchaseratenullerror") + "\n");
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new ProductSaleRateSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
                        resourceBundle.getString("productsalerate.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new ProductSaleRateSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsalerate"),
                        resourceBundle.getString("productsalerate.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void getNextProductSaleRateNumber(Society society) {
        if (society == null)
            return;
        var task = new ProductSaleRateNumberLoadTask(society.getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty()) {
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void loadProduct() {
        var task = new ProductLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Product> list = task.get();
                if (list != null)
                    cboxProduct.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
