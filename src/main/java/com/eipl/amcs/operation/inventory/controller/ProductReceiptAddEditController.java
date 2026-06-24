package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.task.TaxLoadTask;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.task.UnitLoadTask;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateSaveTask;
import com.eipl.amcs.master.operation.model.Vendor;
import com.eipl.amcs.master.operation.task.VendorLoadTask;
import com.eipl.amcs.operation.inventory.dto.ProductReceiptDto;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.eipl.amcs.operation.inventory.task.ProductReceiptGetNextCodeTask;
import com.eipl.amcs.operation.inventory.task.ProductReceiptSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductReceiptTransactionsByGrnNoLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ProductReceiptAddEditController implements MyInitialization, PopupCallback {
    private final ObjectProperty<ProductReceiptTransaction> propReceiptTxn;
    private final List<ReceiptTxnTaxDto> receiptTxnTaxDtoList = new ArrayList<>();
    private final ObservableList<ProductReceiptTransaction> listProductReceiptTransaction;
    private final int SCALE = 2;
    private final RoundingMode RATE_ROUND = RoundingMode.HALF_UP;
    @FXML
    private StackPane root;
    @FXML
    private E_DatePicker dpChallanDate;
    @FXML
    private E_TextField txtTotalTax, txtAmount, txtBillNo, txtProductTotalAmount, txtTaxAmount, txtTotalAmount, txtRate, txtQuantity, txtGrnNo, txtChallanNo, txtDescription, txtDiscount, txtNetAmount, txtSaleRate;
    @FXML
    private AutoSearchTextField<Vendor> cboxParty;
    @FXML
    private AutoSearchTextField<Product> cboxProduct;
    @FXML
    private E_Button btnDeleteProduct, btnAddProduct, btnSaveUpdate, btnClose;
    @FXML
    private TableView<ProductReceiptTransaction> tableProductReceiptTransaction;
    @FXML
    private TableColumn<ProductReceiptTransaction, String> colProductCode;
    @FXML
    private TableColumn<ProductReceiptTransaction, Product> colProductName;
    @FXML
    private TableColumn<ProductReceiptTransaction, Number> colTotalAmount,
            colQuantity, colAmount, colRate, colTax, colProductSale;
    @FXML
    private List<ProductReceiptTransaction> listTransactions;
    @FXML
    private Label lblSaleRate;
    private ProductReceipt productReceipt;
    private ProductPurchaseRate productPurchaseRate;
    private final Map<String, ProductPurchaseRate> productPurchaseRateMap = new HashMap<>();
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private List<Product> productList;
    private List<Unit> unitList;
    private List<TaxDto> taxDtoList;
    private ProductReceiptTransaction r;
    private PopupCallback callback;
    private Stage stage;

    @FXML
    private AutoSearchTextField<Tax> cboxTax;
    private Map<TaxDetail, BigDecimal> taxBifurcation = null;


    public ProductReceiptAddEditController() {
        propReceiptTxn = new SimpleObjectProperty<>();
        listProductReceiptTransaction = FXCollections.observableArrayList();
    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpChallanDate.setValue(LocalDate.now());
        listTransactions = new ArrayList<>();

        setupComboBox();
        setupTable();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        FocusUtils.requestFocus(txtBillNo);

        cboxProduct.setOnAction(e -> {
            Tax tax = cboxTax.getItems().stream()
                    .filter(p -> p != null && cboxProduct.getValue().getTax() != null && p.getCode().equalsIgnoreCase(cboxProduct.getValue().getTax().getCode()))
                    .findFirst().orElse(null);
            if (tax != null)
                cboxTax.getSelectionModel().select(tax);
            else
                cboxTax.getSelectionModel().select(cboxTax.getItems().stream()
                        .filter(p -> p.getName().equalsIgnoreCase("NIL")).findFirst().orElse(null));

            if (cboxProduct.getValue() != null)
                fetchPurchaseRate();
        });

        dpChallanDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cboxProduct.getValue() != null)
                fetchPurchaseRate();
        });

        cboxTax.setOnAction(e -> calculateTaxAmount());

        txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateAmount();
        });
        txtRate.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateAmount();
        });

        btnAddProduct.setOnAction(e -> {
            validateAndSaveProduct();
            FocusUtils.requestFocus(cboxProduct);
        });
        btnDeleteProduct.setOnAction(e -> deleteData());

        propReceiptTxn.addListener((observable, oldValue, newValue) -> {
            btnDeleteProduct.setDisable(newValue == null);
            r = newValue;
        });

        txtTaxAmount.setText("0");
        txtTotalAmount.setText("0");
        txtSaleRate.setText("0");
        txtSaleRate.setOnAction(e -> {
            FocusUtils.requestFocus(btnAddProduct);
            e.consume();
        });
        tableProductReceiptTransaction.setOnKeyPressed(event -> {
            ProductReceiptTransaction dto = tableProductReceiptTransaction.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propReceiptTxn.get();
                    if (dto != null)
                        deleteData();
                    break;
            }
        });

        if (MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("FIFO")) {
            txtSaleRate.setVisible(true);
            lblSaleRate.setVisible(true);
        } else {
            txtSaleRate.setVisible(false);
            lblSaleRate.setVisible(false);
        }

    }

    public void setDate(LocalDate date) {
        dpChallanDate.setValue(date);
    }

    public void setProductReceipt(ProductReceipt productReceipt) {
        this.productReceipt = productReceipt;
        if (productReceipt != null) {
            txtChallanNo.setText(productReceipt.getChallanNo());
            txtGrnNo.setText(productReceipt.getGrnNo());
            dpChallanDate.setValue(productReceipt.getChallanDate());
            txtTotalTax.setText(productReceipt.getTaxAmount().toString());
            txtNetAmount.setText(productReceipt.getNetAmount().toString());
            txtBillNo.setText(productReceipt.getBillNo());
            txtTotalAmount.setText(productReceipt.getAmount().toString());
            btnSaveUpdate.setText(CommonUtils.getResourceString(resourceBundle, "update"));
            loadReceiptTransaction();
        } else {
            btnSaveUpdate.setText(CommonUtils.getResourceString(resourceBundle, "save"));
            getNextCode();
        }
        loadData();
    }

    private void loadReceiptTransaction() {
        var task = new ProductReceiptTransactionsByGrnNoLoadTask(txtGrnNo.getText());
        task.setOnSucceeded(e -> {
            try {
                List<ReceiptTxnTaxDto> list = task.get();
                if (list == null)
                    return;

                receiptTxnTaxDtoList.addAll(list);
                for (int i = 0; i < receiptTxnTaxDtoList.size(); i++) {
                    listTransactions.add(receiptTxnTaxDtoList.get(i).getTransaction());
                }
                listProductReceiptTransaction.addAll(listTransactions);
                tableProductReceiptTransaction.setItems(FXCollections.observableList(listProductReceiptTransaction));
                propReceiptTxn.bind(tableProductReceiptTransaction.getSelectionModel().selectedItemProperty());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNextCode() {
        var task = new ProductReceiptGetNextCodeTask();
        task.setOnSucceeded(event -> {
            try {
                txtGrnNo.setText(task.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void validateAndSave() {
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equalsIgnoreCase(CommonUtils.getResourceString(resourceBundle, "save")))
            productReceipt = new ProductReceipt();
        setValuesInObject();

        if (btnSaveUpdate.getText().equalsIgnoreCase(CommonUtils.getResourceString(resourceBundle, "save")))
            saveData();
        else
            updateData();
    }

    @Override
    public void setupComboBox() {
        dpChallanDate.setConverter(new LocalDateConvertor());
        dpChallanDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpChallanDate.setValue(dpChallanDate.getConverter().fromString(dpChallanDate.getEditor().getText()));
            }
        });
    }

    public void setupTable() {
        try {
            colProductCode.setCellValueFactory(
                    data -> new SimpleStringProperty(data.getValue().getProduct().getCode()));
            colProductName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct()));
            colQuantity
                    .setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuantity()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colTotalAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNetAmount()));
            colTax.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTaxAmount()));
            colProductSale.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSaleRate()));
            propReceiptTxn.bind(tableProductReceiptTransaction.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValuesInObject() {
        productReceipt.setSociety(MainApp.identityDto.getSociety());
        productReceipt.setUnion(MainApp.identityDto.getUnion());
        productReceipt.setGrnDate(dpChallanDate.getValue());
        productReceipt.setGrnNo(txtGrnNo.getText());
        productReceipt.setChallanNo(txtChallanNo.getText());
        productReceipt.setChallanDate(dpChallanDate.getValue());
        productReceipt.setBillNo(txtBillNo.getText());
        productReceipt.setVendor(cboxParty.getValue());
    }

    @Override
    public void saveData() {
        if (receiptTxnTaxDtoList.isEmpty() || receiptTxnTaxDtoList == null) {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                    resourceBundle.getString("add.product"));
            alert.createAlert();
        } else {
            productReceipt.setAmount(new BigDecimal(txtTotalAmount.getText()));
            productReceipt.setTaxAmount(new BigDecimal(txtTotalTax.getText()));
            productReceipt.setNetAmount(new BigDecimal(txtNetAmount.getText()));
            productReceipt.setDiscount(BigDecimal.ZERO);

            ProductReceiptDto productReceiptDto = new ProductReceiptDto();
            productReceiptDto.setProductReceipt(productReceipt);
            productReceiptDto.setReceiptTxnTaxDtoList(receiptTxnTaxDtoList);

            var task = new ProductReceiptSaveTask(productReceiptDto, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(CommonUtils.getResourceString(resourceBundle, subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                                sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                            CommonUtils.getResourceString(resourceBundle, "product.receipt.insert.successful"));
                    alert.createAlert();
                    saveProductPurchaseRates();
                    this.callback.reloadData(true);
                    this.stage.close();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            task.setOnFailed(e -> {
                Throwable t = task.getException();
                String errorMessage = "error.occurred";
                if (t.getMessage().contains("StockIsLessThanZero")) {
                    errorMessage = "stock.going.to.zero";
                }
                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString(errorMessage));
                alert1.createAlert();
            });
            new Thread(task).start();
        }
    }

    @Override
    public void updateData() {
        productReceipt.setAmount(new BigDecimal(txtTotalAmount.getText()));
        productReceipt.setTaxAmount(new BigDecimal(txtTotalTax.getText()));
        productReceipt.setNetAmount(new BigDecimal(txtNetAmount.getText()));
        productReceipt.setDiscount(BigDecimal.ZERO);

        ProductReceiptDto productReceiptDto = new ProductReceiptDto();
        List<ReceiptTxnTaxDto> list2 = new ArrayList<>();
        for (ReceiptTxnTaxDto txnTaxDto : receiptTxnTaxDtoList) {
            txnTaxDto.getTransaction().setDiscount(BigDecimal.ZERO);
            for (ProductReceiptTransaction productSaleTransaction : listProductReceiptTransaction) {
                if (txnTaxDto.getTransaction() == productSaleTransaction) {
                    list2.add(txnTaxDto);
                }
            }
        }
        productReceiptDto.setProductReceipt(productReceipt);
        productReceiptDto.setReceiptTxnTaxDtoList(list2);
        var task = new ProductReceiptSaveTask(productReceiptDto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(CommonUtils.getResourceString(resourceBundle, subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                        CommonUtils.getResourceString(resourceBundle, "product.receipt.update.successful"));
                alert.createAlert();
                saveProductPurchaseRates();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            Throwable t = task.getException();
            String errorMessage = "error.occurred";
            if (t.getMessage().contains("StockIsLessThanZero")) {
                errorMessage = "stock.going.to.zero";
            }
            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    resourceBundle.getString(errorMessage));
            alert1.createAlert();
        });
        new Thread(task).start();
    }

    private void saveProductPurchaseRates() {
        for (ReceiptTxnTaxDto txnDto : receiptTxnTaxDtoList) {
            ProductReceiptTransaction txn = txnDto.getTransaction();
            if (txn.getProduct() == null) {
                continue;
            }

            String pCode = txn.getProduct().getCode();
            ProductPurchaseRate rateObj = productPurchaseRateMap.getOrDefault(pCode, new ProductPurchaseRate());
            short mode = (rateObj.getCode() != null) ? (short) 1 : (short) 0;
            rateObj.setProduct(txn.getProduct());
            rateObj.setRate(txn.getRate());
            rateObj.setWefDate(dpChallanDate.getValue());
            rateObj.setSociety(MainApp.identityDto.getSociety());
            rateObj.setUnion(MainApp.identityDto.getUnion());
            rateObj.setEntryType((short) 1); // Indicating GRN Entry

            var task = new ProductPurchaseRateSaveTask(rateObj, mode);
            new Thread(task).start();
        }
    }

    public void deleteData() {
            ReceiptTxnTaxDto removeReceiptTxnTaxDto = null;
            for (ReceiptTxnTaxDto receiptTxnTaxDto : receiptTxnTaxDtoList) {
                if (receiptTxnTaxDto.getTransaction() == r) {
                    removeReceiptTxnTaxDto = receiptTxnTaxDto;
                    break;
                }
            }
            receiptTxnTaxDtoList.remove(removeReceiptTxnTaxDto);
            listProductReceiptTransaction.remove(propReceiptTxn.get());
            tableProductReceiptTransaction.setItems(listProductReceiptTransaction);
            calculateSummary();
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (dpChallanDate.getValue() == null) {
            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "product.receipt.validation.challan.date.empty") + "\n");
        }
        if (cboxParty.getSelectionModel().getSelectedItem() == null) {
            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "product.receipt.validation.member.empty") + "\n");
        }
        if (txtBillNo.getText().trim().equals("")) {
            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "product.receipt.validation.challan.no.empty") + "\n");
        }

        return errorMsg.length() == 0;
    }

    @Override
    public void loadData() {
        VendorLoadTask task = new VendorLoadTask();
        task.setOnSucceeded(event -> {
            try {
                List<Vendor> list = task.get();
                if (list != null) {
                    cboxParty.setItems(FXCollections.observableArrayList(list));
                    if (cboxParty.getItems() != null) {
                        if (productReceipt != null)
//                            cboxParty.getSelectionModel().select(0);
//                        else
                            cboxParty.getSelectionModel().select(productReceipt.getVendor());
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new ProductLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                productList = task1.get();
                if (!productList.isEmpty()) {
                    cboxProduct.setItems(FXCollections.observableList(productList));
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task2 = new TaxLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                taxDtoList = task2.get();
                cboxTax.setItems(FXCollections.observableList(CommonUtils.getTaxFromDto(taxDtoList)));

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

        var task3 = new UnitLoadTask();
        task3.setOnSucceeded(e -> {
            try {
                unitList = task3.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task3).start();
    }

    @Override
    public void returnProductReceiptTxnTaxDto(ReceiptTxnTaxDto dto) {
        if (dto != null) {
            Optional<ProductReceiptTransaction> trans = listProductReceiptTransaction.stream()
                    .filter(p -> p.getProduct().getCode().equalsIgnoreCase(dto.getTransaction().getProduct().getCode()))
                    .findAny();
            if (trans.isPresent()) {
                ProductReceiptTransaction existingTxn = trans.get();
                existingTxn.setQuantity(dto.getTransaction().getQuantity());
                existingTxn.setRate(dto.getTransaction().getRate());
                existingTxn.setAmount(dto.getTransaction().getAmount());
                existingTxn.setTaxAmount(dto.getTransaction().getTaxAmount());
                existingTxn.setNetAmount(dto.getTransaction().getNetAmount());

                receiptTxnTaxDtoList.stream()
                        .filter(txnDto -> txnDto.getTransaction().getProduct().getCode().equalsIgnoreCase(dto.getTransaction().getProduct().getCode()))
                        .findFirst()
                        .ifPresent(txnDto -> txnDto.setReceiptTaxList(dto.getReceiptTaxList()));

                tableProductReceiptTransaction.refresh();
            } else {
                receiptTxnTaxDtoList.add(dto);
                listProductReceiptTransaction.add(dto.getTransaction());
                tableProductReceiptTransaction.setItems(listProductReceiptTransaction);
            }
            calculateSummary();
        }
    }

    private void calculateSummary() {
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal totalDis = BigDecimal.ZERO;
        BigDecimal totalTaxAmt = BigDecimal.ZERO;
        BigDecimal netAmt = BigDecimal.ZERO;

        for (ProductReceiptTransaction transaction : listProductReceiptTransaction) {
            totalAmt = totalAmt.add(transaction.getAmount());
            totalTaxAmt = totalTaxAmt.add(transaction.getTaxAmount());
            netAmt = netAmt.add(transaction.getNetAmount());
        }
        txtTotalAmount.setText(CommonUtils.scale2RoundUp(totalAmt).toString());
        txtTotalTax.setText(CommonUtils.scale2RoundUp(totalTaxAmt).toString());
        txtNetAmount.setText(CommonUtils.scale2RoundUp(netAmt).toString());
    }

    private void validateAndSaveProduct() {
        errorMsg = new StringBuilder();
        if (!validateProduct()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        ReceiptTxnTaxDto receiptTxnTaxDto = new ReceiptTxnTaxDto();
        receiptTxnTaxDto.setTransaction(setValuesInReceiptTransaction());
        receiptTxnTaxDto.setReceiptTaxList(setValuesInReceiptTax());
        returnProductReceiptTxnTaxDto(receiptTxnTaxDto);
//        stage.close();
        clearControls();
    }

    private boolean validateProduct() {
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
        if (txtSaleRate.isVisible()) {
            BigDecimal salerate = new BigDecimal(txtSaleRate.getText());
            BigDecimal purchaserate = new BigDecimal(txtRate.getText());

            if (salerate.compareTo(BigDecimal.ZERO) <= 0 || salerate.compareTo(purchaserate) < 0) {
                errorMsg.append(CommonUtils.getResourceString(resourceBundle, "entervalidsalerate") + "\n");
            }
        }
        return errorMsg.length() == 0;
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
        txn.setUnit(txn.getProduct().getPrimaryUom());
        txn.setRate(new BigDecimal(txtRate.getText()));
        txn.setSaleRate(new BigDecimal(txtSaleRate.getText()));
        txn.setQuantity(Integer.valueOf(String.valueOf(new BigDecimal(txtQuantity.getText()))));
        txn.setAmount(new BigDecimal(txtAmount.getText()));
        txn.setTax(cboxTax.getValue());
        txn.setTaxAmount(new BigDecimal(txtTaxAmount.getText()));
        txn.setNetAmount(new BigDecimal(txtProductTotalAmount.getText()));
        txn.setUnionCode(MainApp.identityDto.getUnion().getCode());
        txn.setSocietyCode(MainApp.identityDto.getSociety().getCode());
        txn.setDiscount(BigDecimal.ZERO);
        return txn;
    }

    private void calculateTaxAmount() {
        if (!txtAmount.getText().isEmpty()) {
            taxBifurcation = null;
            BigDecimal discount = BigDecimal.ZERO;
            BigDecimal taxableAmt = new BigDecimal(txtAmount.getText()).subtract(discount).setScale(SCALE, RATE_ROUND);

            BigDecimal taxAmount = BigDecimal.ZERO;
            if (!(cboxTax.getValue() == null) && !cboxTax.getValue().getName().equalsIgnoreCase("NIL")) {
                taxBifurcation = CommonUtils.calculateAndFetchTaxBifurcation(taxDtoList.stream()
                        .filter(p -> p.getTax().getCode().equalsIgnoreCase(cboxTax.getValue().getCode())).findFirst().orElse(null), taxableAmt);
                if (taxBifurcation != null) {
                    for (TaxDetail taxDtl : taxBifurcation.keySet()) {
                        taxAmount = taxAmount.add(taxBifurcation.get(taxDtl)).setScale(SCALE, RATE_ROUND);
                    }
                }
            }
            txtTaxAmount.setText(taxAmount.toString());
            BigDecimal totalAmt = taxableAmt.add(taxAmount).setScale(SCALE, RATE_ROUND);
            txtProductTotalAmount.setText(totalAmt.toString());
        }
    }

    private void calculateAmount() {
        if (!txtQuantity.getText().isEmpty() && !txtRate.getText().isEmpty()) {
            BigDecimal qty = new BigDecimal(txtQuantity.getText());
            BigDecimal rate = new BigDecimal(txtRate.getText());
            BigDecimal amt = qty.multiply(rate).setScale(0, RoundingMode.HALF_DOWN);
            txtAmount.setText(amt.toString());
            if (cboxTax.getSelectionModel().getSelectedItem() != null) {
                calculateTaxAmount();
            } else {
                txtProductTotalAmount.setText(amt.toString());
            }
        }
    }

    private void fetchPurchaseRate() {
        Product selectedProduct = cboxProduct.getValue();
        LocalDate selectedDate = dpChallanDate.getValue();
        if (selectedProduct == null || selectedDate == null) return;

        String pCode = selectedProduct.getCode();
        var task = new ProductPurchaseRateByProductTask(pCode, selectedDate);
        task.setOnSucceeded(e -> {
            try {
                productPurchaseRate = task.get();
                if (productPurchaseRate == null) {
                    txtRate.setText("0");
                    productPurchaseRateMap.remove(pCode);
                } else {
                    txtRate.setText(productPurchaseRate.getRate().toString());
                    // Only update existing record if the date is an exact match
                    if (productPurchaseRate.getWefDate() != null && productPurchaseRate.getWefDate().isEqual(selectedDate)) {
                        productPurchaseRateMap.put(pCode, productPurchaseRate);
                    } else {
                        // Different date found: for "this" date, we want a new row (Insert)
                        productPurchaseRateMap.remove(pCode);
                    }
                }
                calculateAmount();
                calculateTaxAmount();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void clearControls() {
        txtRate.setText("0");
        txtSaleRate.setText("0");
        txtQuantity.setText("0");
        txtAmount.setText("0");
        txtProductTotalAmount.setText("0");
        cboxProduct.getSelectionModel().clearSelection();
        cboxTax.getSelectionModel().clearSelection();
    }
}