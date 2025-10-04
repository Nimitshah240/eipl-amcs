package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.TaxConvertor;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.task.ProductSaleRateByProductLoadTask;
import com.eipl.amcs.operation.inventory.dto.SaleTxnDto;
import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import com.eipl.amcs.operation.inventory.task.ProductStockByCodeLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProductSaleTransactionAddController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_ComboBox<Product> cboxProduct;
    @FXML
    private E_ComboBox<Unit> cboxUnit;
    @FXML
    private E_ComboBox<Tax> cboxTaxCode;
    @FXML
    private E_NumericField txtRate, txtQuantity, txtAmount, txtDiscount, txtTaxAmount, txtCurrentStock, txtNetAmount;
    @FXML
    private Button btnSaveUpdate, btnClose;

    private SaleTxnDto saleTxnDto;
    private ProductStock productStock = null;

    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private PopupCallback callback;
    private Map<TaxDetail, BigDecimal> taxBifurcation = null;

    private final int SCALE = 2;
    private final RoundingMode RATE_ROUND = RoundingMode.HALF_UP;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setSaleTxnDto(SaleTxnDto saleTxnDto) {

        try {
            this.saleTxnDto = saleTxnDto;
            if (saleTxnDto != null && !saleTxnDto.getProductList().isEmpty()) {
                cboxProduct.setItems(FXCollections.observableList(saleTxnDto.getProductList()));
                cboxUnit.setItems(FXCollections.observableList(saleTxnDto.getUnitList()));
                cboxTaxCode.setItems(FXCollections.observableList(CommonUtils.getTaxFromDto(saleTxnDto.getTaxDtoList())));
                new AutoCompleteComboBoxListener<>(cboxProduct);
                new AutoCompleteComboBoxListener<>(cboxUnit);
                new AutoCompleteComboBoxListener<>(cboxTaxCode);
            } else {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), "Product",
                        "Data Not Available!");
                alert.createAlert();
                stage.close();
                return;
            }
        } catch (Exception e) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), "Product",
                    "Data Not Available!");
            alert.createAlert();
            stage.close();
            return;
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        txtDiscount.setText("0");
        setupComboBox();
        cboxProduct.setOnAction(e -> {
            calculateStock();
            Tax tax = cboxTaxCode.getItems().stream()
                    .filter(p -> p.getCode().equalsIgnoreCase(cboxProduct.getValue().getTax().getCode()))
                    .findFirst().orElse(null);
            if (tax != null)
                cboxTaxCode.getSelectionModel().select(tax);
            else
                cboxTaxCode.getSelectionModel().select(cboxTaxCode.getItems().stream()
                        .filter(p -> p.getName().equalsIgnoreCase("NIL")).findFirst().orElse(null));

            Unit unit = cboxUnit.getItems().stream()
                    .filter(p -> p.getCode() == cboxProduct.getValue().getPrimaryUom().getCode())
                    .findFirst().orElse(null);
            if (unit != null)
                cboxUnit.getSelectionModel().select(unit);

            if (cboxProduct.getValue() != null)
                fetchSaleRate();
        });

        cboxTaxCode.setOnAction(e -> calculateTaxAmount());

        btnSaveUpdate.setOnAction(e -> validateAndSave());
        btnClose.setOnAction(e -> {
            stage.close();
        });

        txtQuantity.focusedProperty().addListener((ob, oldVal, newVal) -> {
            if (!newVal) {
                calculateAmount();
                calculateTaxAmount();
            }
        });

        txtDiscount.focusedProperty().addListener((ob, oldVal, newVal) -> {
            if (!newVal) {
                calculateTaxAmount();
            }
        });
    }

    @Override
    public void setupComboBox() {
        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
//        cboxUnit.setConverter(new UnitConvertor(cboxUnit));
        cboxTaxCode.setConverter(new TaxConvertor(cboxTaxCode));
    }

    private SaleTxnTaxDto saleTxnTaxDto;

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product.receipt"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        saleTxnTaxDto = new SaleTxnTaxDto();
        saleTxnTaxDto.setTransaction(setValuesInSaleTransaction());
        saleTxnTaxDto.setSaleTaxList(setValuesInSaleTax());
        callback.returnProductSaleTxnTaxDto(saleTxnTaxDto);
        stage.close();
    }

    private List<ProductSaleTax> setValuesInSaleTax() {
        if (taxBifurcation == null) {
            return null;
        }
        List<ProductSaleTax> list = new ArrayList<>();
        for (TaxDetail taxDetail : taxBifurcation.keySet()) {
            ProductSaleTax tax = new ProductSaleTax();
            tax.setTaxDetail(taxDetail);
            tax.setUnionCode(MainApp.identityDto.getUnion().getCode());
            tax.setValue(taxBifurcation.get(taxDetail));
            tax.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            list.add(tax);
        }
        return list;
    }

    private ProductSaleTransaction setValuesInSaleTransaction() {
        ProductSaleTransaction txn = new ProductSaleTransaction();
        txn.setProduct(cboxProduct.getValue());
        txn.setRate(new BigDecimal(txtRate.getText()));
        txn.setQuantity(BigDecimal.valueOf(Integer.valueOf(txtQuantity.getText())));
        txn.setAmount(new BigDecimal(txtAmount.getText()));
        if (txtDiscount.getText() == null || txtDiscount.getText().isEmpty())
            txn.setDiscount(new BigDecimal(0));
        else
            txn.setDiscount(new BigDecimal(txtDiscount.getText()));
        txn.setTaxAmount(new BigDecimal(txtTaxAmount.getText()));
        txn.setNetAmount(new BigDecimal(txtNetAmount.getText()));
        txn.setUnionCode(MainApp.identityDto.getUnion().getCode());
        txn.setSocietyCode(MainApp.identityDto.getSociety().getCode());
        txn.setTaxCode(cboxTaxCode.getValue().getCode());
        return txn;
    }

    private boolean validate() {
        if (cboxProduct.getValue() == null)
            errorMsg.append(resourceBundle.getString("productsale.transaction.validation.product.empty") + "\n");
        BigDecimal rate = new BigDecimal(txtRate.getText());
        if (rate.doubleValue() <= 0)
            errorMsg.append(resourceBundle.getString("productsale.transaction.validation.rate.empty") + "\n");
        if (txtQuantity.getText() == null || txtQuantity.getText().isEmpty() || !CommonUtils.isNumeric(txtQuantity.getText()))
            errorMsg.append(resourceBundle.getString("productsale.transaction.validation.quantity.empty") + "\n");
        BigDecimal qty = new BigDecimal(txtQuantity.getText());
        if (qty.doubleValue() <= 0)
            errorMsg.append(resourceBundle.getString("productsale.transaction.validation.quantity.empty") + "\n");
        return errorMsg.length() == 0;
    }

    private void calculateTaxAmount() {
        if (cboxTaxCode.getValue() != null) {
            if (!txtAmount.getText().isEmpty()) {
                taxBifurcation = null;
                BigDecimal discount = txtDiscount.getText() == null || txtDiscount.getText().isEmpty() ? new BigDecimal("0") : new BigDecimal(txtDiscount.getText());
                BigDecimal taxableAmt = new BigDecimal(txtAmount.getText()).subtract(discount).setScale(SCALE, RATE_ROUND);

                BigDecimal taxAmount = BigDecimal.ZERO;
                if (!cboxTaxCode.getValue().getName().equalsIgnoreCase("NIL")) {
                    taxBifurcation = CommonUtils.calculateAndFetchTaxBifurcation(saleTxnDto.getTaxDtoList().stream()
                            .filter(p -> p.getTax().getCode().equalsIgnoreCase(cboxTaxCode.getValue().getCode())).findFirst().orElse(null), taxableAmt);
                    if (taxBifurcation != null) {
                        for (TaxDetail taxDtl : taxBifurcation.keySet()) {
                            taxAmount = taxAmount.add(taxBifurcation.get(taxDtl)).setScale(SCALE, RATE_ROUND);
                        }
                    }
                }
                txtTaxAmount.setText(taxAmount.toString());
                BigDecimal totalAmt = taxableAmt.add(taxAmount).setScale(SCALE, RATE_ROUND);
                txtNetAmount.setText(totalAmt.toString());
            }
        }
    }

    private void calculateAmount() {
        if (!txtQuantity.getText().isEmpty() && !txtRate.getText().isEmpty()) {
            BigDecimal qty = new BigDecimal(txtQuantity.getText());
            BigDecimal rate = new BigDecimal(txtRate.getText());
            BigDecimal amt = qty.multiply(rate).setScale(SCALE, RATE_ROUND);
            txtAmount.setText(amt.toString());
        }
    }

    private void fetchSaleRate() {
        var task = new ProductSaleRateByProductLoadTask(cboxProduct.getValue().getCode(), saleTxnDto.getDate());
        task.setOnSucceeded(e -> {
            try {
                ProductSaleRate rate = task.get();
                if (rate == null) {
                    txtRate.setText("0");
                } else {
                    txtRate.setText(rate.getRate().toString());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void calculateStock() {
        var task = new ProductStockByCodeLoadTask(cboxProduct.getSelectionModel().getSelectedItem().getCode());
        task.setOnSucceeded(e -> {
            try {
                productStock = task.get();
                if (productStock != null) {
                    txtCurrentStock.setText(String.valueOf(productStock.getStock()));
                } else {
                    txtCurrentStock.setText("0");
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
