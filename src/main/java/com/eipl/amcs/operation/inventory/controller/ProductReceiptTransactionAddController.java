package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.TaxConvertor;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.global.convertor.UnitConvertor;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnDto;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

public class ProductReceiptTransactionAddController implements MyInitialization {
    private final int SCALE = 2;
    private final RoundingMode RATE_ROUND = RoundingMode.HALF_UP;
    @FXML
    private StackPane root;
    @FXML
    private ComboBox<Product> cboxProduct;
    @FXML
    private ComboBox<Unit> cboxUnit;
    @FXML
    private ComboBox<Tax> cboxTax;
    @FXML
    private TextField txtRate, txtQuantity, txtAmount, txtDiscount, txtTaxAmount, txtTotalAmount, txtRemark;
    @FXML
    private Button btnSave, btnClose;
    private ReceiptTxnDto receiptTxnDto;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private PopupCallback callback;
    private Map<TaxDetail, BigDecimal> taxBifurcation = null;
    private ReceiptTxnTaxDto receiptTxnTaxDto;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setReceiptTxnDto(ReceiptTxnDto receiptTxnDto) {
        this.receiptTxnDto = receiptTxnDto;
        if (receiptTxnDto != null) {
            cboxProduct.setItems(FXCollections.observableList(receiptTxnDto.getProductList()));
            cboxUnit.setItems(FXCollections.observableList(receiptTxnDto.getUnitList()));
            cboxTax.setItems(FXCollections.observableList(CommonUtils.getTaxFromDto(receiptTxnDto.getTaxDtoList())));
            new AutoCompleteComboBoxListener<>(cboxProduct);
            new AutoCompleteComboBoxListener<>(cboxUnit);
            new AutoCompleteComboBoxListener<>(cboxTax);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        cboxProduct.setOnAction(e -> {
            Tax tax = cboxTax.getItems().stream()
                    .filter(p -> p.getCode().equalsIgnoreCase(cboxProduct.getValue().getTax().getCode()))
                    .findFirst().orElse(null);
            if (tax != null)
                cboxTax.getSelectionModel().select(tax);
            else
                cboxTax.getSelectionModel().select(cboxTax.getItems().stream()
                        .filter(p -> p.getName().equalsIgnoreCase("NIL")).findFirst().orElse(null));

            Unit unit = cboxUnit.getItems().stream()
                    .filter(p -> p.getCode() == cboxProduct.getValue().getPrimaryUom().getCode())
                    .findFirst().orElse(null);

            if (unit != null)
                cboxUnit.getSelectionModel().select(1);

            if (cboxProduct.getValue() != null)
                fetchPurchaseRate();
        });
        cboxTax.setOnAction(e -> calculateTaxAmount());

        btnSave.setOnAction(e -> validateAndSave());
        btnClose.setOnAction(e -> {
            stage.close();
        });

        txtQuantity.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            calculateAmount();
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
        cboxUnit.setConverter(new UnitConvertor(cboxUnit));
        cboxTax.setConverter(new TaxConvertor(cboxTax));

    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        receiptTxnTaxDto = new ReceiptTxnTaxDto();
        receiptTxnTaxDto.setTransaction(setValuesInReceiptTransaction());
        receiptTxnTaxDto.setReceiptTaxList(setValuesInReceiptTax());
        callback.returnProductReceiptTxnTaxDto(receiptTxnTaxDto);
        stage.close();
    }

    private List<ProductReceiptTax> setValuesInReceiptTax() {
        if (taxBifurcation == null) {
            return null;
        }
        List<ProductReceiptTax> list = new ArrayList<>();
        for (TaxDetail taxDetail : taxBifurcation.keySet()) {
            ProductReceiptTax tax = new ProductReceiptTax();
            tax.setTaxDetail(taxDetail);
            tax.setUnionCode(MainApp.identityDto.getUnion().getCode());
            tax.setValue(taxBifurcation.get(taxDetail));
            tax.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            list.add(tax);
        }
        return list;
    }

    private ProductReceiptTransaction setValuesInReceiptTransaction() {
        ProductReceiptTransaction txn = new ProductReceiptTransaction();
        txn.setProduct(cboxProduct.getValue());
        txn.setUnit(cboxUnit.getValue());
        txn.setRate(new BigDecimal(txtRate.getText()));
        txn.setQuantity(new BigDecimal(txtQuantity.getText()));
        txn.setAmount(new BigDecimal(txtAmount.getText()));
        txn.setDiscount(new BigDecimal(txtDiscount.getText()));
        txn.setTax(cboxTax.getValue());
        txn.setTaxAmount(new BigDecimal(txtTaxAmount.getText()));
        txn.setNetAmount(new BigDecimal(txtTotalAmount.getText()));
        txn.setRemark(txtRemark.getText());
        txn.setUnionCode(MainApp.identityDto.getUnion().getCode());
        txn.setSocietyCode(MainApp.identityDto.getSociety().getCode());

        return txn;
    }

    private boolean validate() {
        if (cboxProduct.getValue() == null)
            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productsale.transaction.validation.product.empty") + "\n");
        if (txtRate.getText() == null || txtRate.getText().isEmpty() || !CommonUtils.isNumeric(txtRate.getText()))
            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productsale.transaction.validation.rate.empty") + "\n");
        else {
            BigDecimal rate = new BigDecimal(txtRate.getText());
            if (rate.doubleValue() <= 0)
                errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productsale.transaction.validation.rate.empty") + "\n");
        }
        if (txtQuantity.getText() == null || txtQuantity.getText().isEmpty() || !CommonUtils.isNumeric(txtQuantity.getText()))
            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productsale.transaction.validation.quantity.empty") + "\n");
        else {
            BigDecimal qty = new BigDecimal(txtQuantity.getText());
            if (qty.doubleValue() <= 0)
                errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productsale.transaction.validation.quantity.empty") + "\n");
        }
        return errorMsg.length() == 0;
    }

    private void calculateTaxAmount() {
        if (!txtAmount.getText().isEmpty()) {
            taxBifurcation = null;
            BigDecimal discount = txtDiscount.getText() == null || txtDiscount.getText().isEmpty() ? new BigDecimal("0") : new BigDecimal(txtDiscount.getText());
            BigDecimal taxableAmt = new BigDecimal(txtAmount.getText()).subtract(discount).setScale(SCALE, RATE_ROUND);

            BigDecimal taxAmount = BigDecimal.ZERO;
            if (!(cboxTax.getValue() == null) && !cboxTax.getValue().getName().equalsIgnoreCase("NIL")) {
                taxBifurcation = CommonUtils.calculateAndFetchTaxBifurcation(receiptTxnDto.getTaxDtoList().stream()
                        .filter(p -> p.getTax().getCode().equalsIgnoreCase(cboxTax.getValue().getCode())).findFirst().orElse(null), taxableAmt);
                if (taxBifurcation != null) {
                    for (TaxDetail taxDtl : taxBifurcation.keySet()) {
                        taxAmount = taxAmount.add(taxBifurcation.get(taxDtl)).setScale(SCALE, RATE_ROUND);
                    }
                }
            }
            txtTaxAmount.setText(taxAmount.toString());
            BigDecimal totalAmt = taxableAmt.add(taxAmount).setScale(SCALE, RATE_ROUND);
            txtTotalAmount.setText(totalAmt.toString());
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

    private void fetchPurchaseRate() {
        var task = new ProductPurchaseRateByProductTask(cboxProduct.getValue().getCode(), receiptTxnDto.getDate());
        task.setOnSucceeded(e -> {
            try {
                ProductPurchaseRate rate = task.get();
                if (rate == null) {
                    txtRate.setText("0");
                } else {
                    txtRate.setText(rate.getRate().toString());
                }
                calculateAmount();
                calculateTaxAmount();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
