package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.task.TaxLoadTask;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.task.UnitLoadTask;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.master.operation.convertor.CustomerConvertor;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.operation.inventory.dto.ProductReceiptDto;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnDto;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
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
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ProductReceiptAddEditController implements MyInitialization, PopupCallback {
    private final ObjectProperty<ProductReceiptTransaction> propReceiptTxn;
    private final List<ReceiptTxnTaxDto> receiptTxnTaxDtoList = new ArrayList<>();
    private final ObservableList<ProductReceiptTransaction> listProductReceiptTransaction;
    @FXML
    private StackPane root;
    @FXML
    private DatePicker dpChallanDate;
    @FXML
    private TextField txtGrnNo, txtChallanNo, txtDescription, txtTotalAmount, txtDiscount, txtTotalTax, txtNetAmount;
    @FXML
    private ComboBox<Customer> cboxParty;
    @FXML
    private Button btnAddProduct, btnDelete, btnSaveUpdate, btnClose;
    @FXML
    private TableView<ProductReceiptTransaction> tableProductReceiptTransaction;
    @FXML
    private TableColumn<ProductReceiptTransaction, String> colProductCode;
    @FXML
    private TableColumn<ProductReceiptTransaction, Product> colProductName;
    @FXML
    private TableColumn<ProductReceiptTransaction, Number> colTotalAmount,
            colQuantity, colAmount, colRate;
    @FXML
    private List<ProductReceiptTransaction> listTransactions;
    private ProductReceipt productReceipt;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private List<Product> productList;
    private List<Unit> unitList;
    private List<TaxDto> taxDtoList;
    private ProductReceiptTransaction r;

    public ProductReceiptAddEditController() {
        propReceiptTxn = new SimpleObjectProperty<>();
        listProductReceiptTransaction = FXCollections.observableArrayList();
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
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductReceipt.fxml"))));
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        FocusUtils.requestFocus(txtChallanNo);
        btnAddProduct.setOnAction(e -> {
            if (!validate()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                        errorMsg.toString());
                alert.createAlert();
                return;
            }
            ReceiptTxnDto receiptTxnDto = new ReceiptTxnDto();
            receiptTxnDto.setProductList(productList);
            receiptTxnDto.setTaxDtoList(taxDtoList);
            receiptTxnDto.setUnitList(unitList);
            receiptTxnDto.setDate(LocalDate.now());
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductReceiptTransaction", receiptTxnDto, this);
        });
        btnDelete.setOnAction(e -> deleteData());

        propReceiptTxn.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            r = newValue;
        });


    }

    public void setProductReceipt(ProductReceipt productReceipt) {
        this.productReceipt = productReceipt;
        if (productReceipt != null) {
            txtChallanNo.setText(productReceipt.getChallanNo());
            txtGrnNo.setText(productReceipt.getGrnNo());
            dpChallanDate.setValue(productReceipt.getChallanDate());
            txtDescription.setText(productReceipt.getDescription());
            txtTotalTax.setText(productReceipt.getTaxAmount().toString());
            txtNetAmount.setText(productReceipt.getNetAmount().toString());
            txtDiscount.setText(productReceipt.getDiscount().toString());
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
        cboxParty.setConverter(new CustomerConvertor(cboxParty));
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
            propReceiptTxn
                    .bind(tableProductReceiptTransaction.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValuesInObject() {
        productReceipt.setSociety(MainApp.identityDto.getSociety());
        productReceipt.setUnion(MainApp.identityDto.getUnion());
        productReceipt.setDescription(txtDescription.getText());
        productReceipt.setGrnDate(LocalDate.now());
        productReceipt.setGrnNo(txtGrnNo.getText());
        productReceipt.setChallanNo(txtChallanNo.getText());
        productReceipt.setChallanDate(dpChallanDate.getValue());
        productReceipt.setCustomer(cboxParty.getValue());
    }

    @Override
    public void saveData() {
        if (receiptTxnTaxDtoList.isEmpty() || receiptTxnTaxDtoList == null) {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                    resourceBundle.getString("add.product"));
            alert.createAlert();
        } else {
            productReceipt.setAmount(new BigDecimal(txtTotalAmount.getText()));
            productReceipt.setDiscount(new BigDecimal(txtDiscount.getText()));
            productReceipt.setTaxAmount(new BigDecimal(txtTotalTax.getText()));
            productReceipt.setNetAmount(new BigDecimal(txtNetAmount.getText()));

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
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductReceipt.fxml")));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    @Override
    public void updateData() {
        productReceipt.setAmount(new BigDecimal(txtTotalAmount.getText()));
        productReceipt.setDiscount(new BigDecimal(txtDiscount.getText()));
        productReceipt.setTaxAmount(new BigDecimal(txtTotalTax.getText()));
        productReceipt.setNetAmount(new BigDecimal(txtNetAmount.getText()));
        ProductReceiptDto productReceiptDto = new ProductReceiptDto();
        List<ReceiptTxnTaxDto> list2 = new ArrayList<>();
        for (ReceiptTxnTaxDto txnTaxDto : receiptTxnTaxDtoList) {
            for (ProductReceiptTransaction productSaleTransaction : listProductReceiptTransaction) {
                if (txnTaxDto.getTransaction() == productSaleTransaction)
                    list2.add(txnTaxDto);
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
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductReceipt.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
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
        if (txtChallanNo.getText().trim().equals("")) {
            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "product.receipt.validation.challan.no.empty") + "\n");
        }

        return errorMsg.length() == 0;
    }

    @Override
    public void loadData() {
        CustomerLoadTask task = new CustomerLoadTask();
        task.setOnSucceeded(event -> {
            try {
                List<Customer> list = task.get();
                if (list != null) {
                    cboxParty.setItems(FXCollections.observableArrayList(list));
                    new AutoCompleteComboBoxListener<>(cboxParty);
                    if (cboxParty.getItems() != null) {
                        if (productReceipt == null)
                            cboxParty.getSelectionModel().select(0);
                        else
                            cboxParty.getSelectionModel().select(productReceipt.getCustomer());
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
                productList = task1.get().stream().filter(ee -> ee.getCreatedBy() == null ||
                        ee.getCreatedBy().equalsIgnoreCase("SYSTEM")).collect(Collectors.toList());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task2 = new TaxLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                taxDtoList = task2.get();
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
                MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "product.receipt"),
                        CommonUtils.getResourceString(resourceBundle, "productsale.transaction.validation.product.repeat"));
                alert.createAlert();
                return;
            }

            receiptTxnTaxDtoList.add(dto);
            listProductReceiptTransaction.add(dto.getTransaction());
            tableProductReceiptTransaction.setItems(listProductReceiptTransaction);
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
            totalDis = totalDis.add(transaction.getDiscount());
            totalTaxAmt = totalTaxAmt.add(transaction.getTaxAmount());
            netAmt = netAmt.add(transaction.getNetAmount());
        }
        txtTotalAmount.setText(CommonUtils.scale2RoundUp(totalAmt).toString());
        txtDiscount.setText(CommonUtils.scale2RoundUp(totalDis).toString());
        txtTotalTax.setText(CommonUtils.scale2RoundUp(totalTaxAmt).toString());
        txtNetAmount.setText(CommonUtils.scale2RoundUp(netAmt).toString());
    }
}