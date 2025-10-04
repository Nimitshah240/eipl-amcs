package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.task.TaxLoadTask;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.task.UnitLoadTask;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductGroupConvertor;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.task.ProductGroupLoadTask;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionGetNextCodeTask;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionTransactionsByProductRequisitionCodeLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ProductRequisitionAddEditController implements MyInitialization, PopupCallback {
    @FXML
    private StackPane root;
    @FXML
    private DatePicker dpRequisitionDate, dpExpectedDeliveryDate;
    @FXML
    private TextField txtRequisitionNo, txtDescription, txtQuantity;
    @FXML
    private ComboBox<Product> cboxProduct;
    @FXML
    private ComboBox<ProductGroup> cboxProductGroup;
    @FXML
    private ComboBox<Unit> cboxUnit;
    @FXML
    private Button btnAdd, btnDelete, btnSaveUpdate, btnClose;
    @FXML
    private TableView<ProductRequisitionTransaction> tableProductRequisitionTransaction;
    @FXML
    private TableColumn<ProductRequisitionTransaction, String> colProductCode, colStatus, colRemarks;
    @FXML
    private TableColumn<ProductRequisitionTransaction, Product> colProductName;
    @FXML
    private TableColumn<ProductRequisitionTransaction, Number> colTotalAmount, colTaxAmount, colDiscount, colQuantity, colAmount, colAQ;
    //    @FXML
//    private TableColumn<ProductRequisitionTransaction, Unit> colUnit;
    private ObjectProperty<ProductRequisitionTransaction> propReceiptTxn;

    private List<ReceiptTxnTaxDto> receiptTxnTaxDtoList = new ArrayList<>();
    private List<ProductRequisitionTransaction> listTransactions;
    private ProductRequisition productRequisition;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private List<ProductGroup> productGroupList;
    private List<Product> productList;
    private List<Unit> unitList;
    private List<TaxDto> taxDtoList;

    public ProductRequisitionAddEditController() {
        propReceiptTxn = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        FocusUtils.requestFocus(txtDescription);
        dpRequisitionDate.setValue(LocalDate.now());
        dpRequisitionDate.setDisable(true);
        dpExpectedDeliveryDate.setValue(LocalDate.now().plusDays(7));
        setupComboBox();
        setupTable();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductRequisition.fxml"))));
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        btnAdd.setOnAction(e -> {
            if (!txtQuantity.getText().equalsIgnoreCase("") && txtQuantity.getText() != null)
                addTransaction();
        });
        btnDelete.setOnAction(e -> deleteData());
        propReceiptTxn.addListener((observable, oldValue, newValue) -> {
            if (productRequisition.getStatus().equalsIgnoreCase("Sent")) {
                if (newValue != null) {
                    btnDelete.setDisable(false);
                } else {
                    btnDelete.setDisable(true);
                }
            }
        });
        cboxProductGroup.setOnAction(e -> {
            loadProducts();
//            calculateRateAndAmount();
        });
//        cboxProduct.setOnAction(e -> {
//            if (cboxProduct.getValue() != null && cboxProduct.getValue().getPrimaryUom() != null) {
//                cboxUnit.getSelectionModel().select(cboxProduct.getValue().getPrimaryUom());
//            }
////            calculateRateAndAmount();
//        });
//        txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (txtQuantity.getText() != null && !txtQuantity.getText().equalsIgnoreCase("")) {
//                try {
//                    txtAmount.setText(String.valueOf(Double.parseDouble(txtRate.getText()) * Double.parseDouble(txtQuantity.getText())));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        txtRate.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (txtQuantity.getText() != null && !txtQuantity.getText().equalsIgnoreCase("")) {
//                try {
//                    txtAmount.setText(String.valueOf(Double.parseDouble(txtRate.getText()) * Double.parseDouble(txtQuantity.getText())));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }


//    private void calculateRateAndAmount() {
//        if (cboxProduct.getValue() != null) {
//            ProductPurchaseRateByProductTask task = new ProductPurchaseRateByProductTask(cboxProduct.getValue().getCode(), LocalDate.now());
//            task.setOnSucceeded(e -> {
//                try {
//                    ProductPurchaseRate purchaseRate = task.get();
//                    if (purchaseRate != null) {
//                        txtRate.setText(String.valueOf(purchaseRate.getRate()));
//                        if (txtQuantity.getText() != null && !txtQuantity.getText().trim().equalsIgnoreCase("") && Double.parseDouble(txtQuantity.getText()) > 0) {
//                            txtAmount.setText(String.valueOf(Double.parseDouble(txtRate.getText()) * Double.parseDouble(txtQuantity.getText())));
//                        }
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            });
//            new Thread(task).start();
//        }
//    }

    private void addTransaction() {
        if (listTransactions == null) {
            listTransactions = new ArrayList<>();
        }
        if (cboxProduct.getValue() != null) {
            ProductRequisitionTransaction transaction = new ProductRequisitionTransaction();
//        transaction.setAmount(new BigDecimal(txtAmount.getText()));
            transaction.setProduct(cboxProduct.getValue());
            transaction.setExpectedDeliveryDate(dpExpectedDeliveryDate.getValue());
            transaction.setQuantity(new BigDecimal(txtQuantity.getText()));
//        transaction.setRate(new BigDecimal(txtRate.getText()));
//        transaction.setAmount(new BigDecimal(txtAmount.getText()));
            transaction.setRequisitionDate(LocalDateTime.now());
            transaction.setDiscountAmount(BigDecimal.ZERO);
            transaction.setCancel(false);
            transaction.setStatus("Sent");
            transaction.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            transaction.setUnionCode(MainApp.identityDto.getUnion().getCode());
            listTransactions.add(transaction);
            tableProductRequisitionTransaction.setItems(FXCollections.observableArrayList(listTransactions));
            clearControls();
        }
    }

    @Override
    public void clearControls() {
//        cboxProductGroup.getSelectionModel().clearSelection();
        cboxProduct.getSelectionModel().clearSelection();
//        if (cboxUnit.getValue() != null)
//            cboxUnit.getSelectionModel().clearSelection();
        txtQuantity.setText("");
//        txtRate.setText("");
//        txtAmount.setText("");
    }

    public void setProductRequisition(ProductRequisition productRequisition) {
        this.productRequisition = productRequisition;
        if (productRequisition != null) {
            dpRequisitionDate.setValue(productRequisition.getRequisitionDate().toLocalDate());
            txtDescription.setText(productRequisition.getDescription());
            txtRequisitionNo.setText(productRequisition.getCode());
            btnSaveUpdate.setText(CommonUtils.getResourceString(resourceBundle, "update"));
            loadRequisitionTransaction();
            if (!productRequisition.getStatus().equalsIgnoreCase("Sent")) {
                btnSaveUpdate.setDisable(true);
                btnAdd.setDisable(true);
                btnDelete.setDisable(true);
                cboxProduct.setDisable(true);
                cboxProductGroup.setDisable(true);
                dpExpectedDeliveryDate.setDisable(true);
                dpRequisitionDate.setDisable(true);
                txtDescription.setDisable(true);
                txtQuantity.setDisable(true);
            }
            btnSaveUpdate.setDisable(true);
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            cboxProduct.setDisable(true);
            cboxProductGroup.setDisable(true);
            dpExpectedDeliveryDate.setDisable(true);
            dpRequisitionDate.setDisable(true);
            txtDescription.setDisable(true);
            txtQuantity.setDisable(true);
        } else {
            btnSaveUpdate.setText(CommonUtils.getResourceString(resourceBundle, "save"));
            getNextCode();
        }
        loadData();
    }

    private void loadRequisitionTransaction() {
        var task = new ProductRequisitionTransactionsByProductRequisitionCodeLoadTask(txtRequisitionNo.getText());
        task.setOnSucceeded(e -> {
            try {
                ProductRequisitionDto list = task.get();
                if (list == null)
                    return;
                if (listTransactions == null) {
                    listTransactions = new ArrayList<>();
                }
                listTransactions.addAll(list.getTransactionList());
                tableProductRequisitionTransaction.setItems(FXCollections.observableList(listTransactions));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNextCode() {
        var task = new ProductRequisitionGetNextCodeTask();
        task.setOnSucceeded(event -> {
            try {
                txtRequisitionNo.setText(task.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void validateAndSave() {
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "productrequisition"), errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equalsIgnoreCase(CommonUtils.getResourceString(resourceBundle, "save")))
            productRequisition = new ProductRequisition();
        setValuesInObject();

        if (btnSaveUpdate.getText().equalsIgnoreCase(CommonUtils.getResourceString(resourceBundle, "save"))) {
            MyAlert alert1 = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("product.requisition"),
                    resourceBundle.getString("youcannotupdate"));
            Optional<ButtonType> resp = alert1.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                saveData();
            }
        } else {
            productRequisition.setRequisitionDate(LocalDateTime.now());
            productRequisition.setDescription(txtDescription.getText());
            updateData();
        }
    }

    @Override
    public void setupComboBox() {
        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
        cboxProductGroup.setConverter(new ProductGroupConvertor(cboxProductGroup));
        dpExpectedDeliveryDate.setConverter(new LocalDateConvertor());
        dpExpectedDeliveryDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpExpectedDeliveryDate.setValue(dpExpectedDeliveryDate.getConverter().fromString(dpExpectedDeliveryDate.getEditor().getText()));
            }
        });
        dpRequisitionDate.setConverter(new LocalDateConvertor());
        dpRequisitionDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpRequisitionDate.setValue(dpRequisitionDate.getConverter().fromString(dpRequisitionDate.getEditor().getText()));
            }
        });
    }

    public void setupTable() {
        try {
            colProductCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getReferenceCode()));
            colProductName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct()));
            colQuantity.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuantity()));
            colAQ.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getApprovedQuantity()));
            colRemarks.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getxCol2()));
            colStatus.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatus() != null ? data.getValue().getStatus() : ""));
//            colUnit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct().getPrimaryUom()));
            //colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            //colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));

            propReceiptTxn.bind(tableProductRequisitionTransaction.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValuesInObject() {
        productRequisition.setCode(txtRequisitionNo.getText());
        productRequisition.setRequisitionDate(LocalDateTime.now());
        productRequisition.setDescription(txtDescription.getText());
        productRequisition.setSociety(MainApp.identityDto.getSociety());
        productRequisition.setUnionCode(MainApp.identityDto.getUnion().getCode());
        productRequisition.setCancel(false);
        productRequisition.setDelete(false);
        productRequisition.setStatus("Sent");
    }

    @Override
    public void saveData() {
        if (listTransactions != null && !listTransactions.isEmpty()) {
            ProductRequisitionDto productRequisitionDto = new ProductRequisitionDto();
            productRequisitionDto.setProductRequisition(productRequisition);
            productRequisitionDto.setTransactionList(listTransactions);
            var task = new ProductRequisitionSaveTask(productRequisitionDto, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(CommonUtils.getResourceString(resourceBundle, subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "productrequisition"), sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "productrequisition"), CommonUtils.getResourceString(resourceBundle, "productrequisition.insert.successful"));
                    alert.createAlert();
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductRequisition.fxml")));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    @Override
    public void updateData() {
        if (listTransactions != null && !listTransactions.isEmpty()) {
            ProductRequisitionDto productRequisitionDto = new ProductRequisitionDto();
            productRequisitionDto.setProductRequisition(productRequisition);
            productRequisitionDto.setTransactionList(listTransactions);
            var task = new ProductRequisitionSaveTask(productRequisitionDto, (short) 1);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(CommonUtils.getResourceString(resourceBundle, subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "productrequisition"), sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "productrequisition"), CommonUtils.getResourceString(resourceBundle, "productrequisition.insert.successful"));
                    alert.createAlert();
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductRequisition.fxml")));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    public void deleteData() {
        listTransactions.remove(propReceiptTxn.get());
        tableProductRequisitionTransaction.setItems(FXCollections.observableArrayList(listTransactions));
        calculateSummary();
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
//        if (dpExpectedDeliveryDate.getValue() == null) {
//            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productrequisition.validation.date.empty") + "\n");
//        }
//        if (dpRequisitionDate.getValue() == null) {
//            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productrequisition.validation.challan.date.empty") + "\n");
//        }
//        if (cboxProduct.getSelectionModel().getSelectedItem() == null) {
//            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productrequisition.validation.member.empty") + "\n");
//        }
//        if (txtDescription.getText().trim().equals("")) {
//            errorMsg.append(CommonUtils.getResourceString(resourceBundle, "productrequisition.validation.challan.no.empty") + "\n");
//        }

        return errorMsg.length() == 0;
    }

    @Override
    public void loadData() {
        var task1 = new ProductGroupLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                productGroupList = task1.get();
                if (productGroupList != null) {
                    cboxProductGroup.getItems().addAll(productGroupList.stream().filter(ee -> ee.getCreatedBy() != null && ee.getCreatedBy().equalsIgnoreCase("PORTAL")).collect(Collectors.toList()));
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
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

        var task3 = new UnitLoadTask();
        task3.setOnSucceeded(e -> {
            try {
                unitList = task3.get();
                if (unitList != null) {
                    cboxUnit.getItems().addAll(unitList);
                    cboxUnit.getSelectionModel().select(1);
                    cboxUnit.setDisable(true);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task3).start();
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        cboxProduct.getItems().removeAll();
        cboxProduct.getItems().setAll(productList);
        var task1 = new ProductLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                productList = task1.get();
                if (productList != null && !productList.isEmpty()) {
                    cboxProduct.getItems().setAll(productList.stream().filter(
                                    ee -> ee.getProductGroup() != null && Objects.equals(ee.getProductGroup().getCode(),
                                            cboxProductGroup.getValue().getCode()) && ee.getCreatedBy() != null && !ee.getCreatedBy().equalsIgnoreCase("SYSTEM"))
                            .collect(Collectors.toList()));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }


    private void calculateSummary() {
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal totalDis = BigDecimal.ZERO;
        BigDecimal totalTaxAmt = BigDecimal.ZERO;
        BigDecimal netAmt = BigDecimal.ZERO;

        for (ProductRequisitionTransaction transaction : listTransactions) {
//            totalAmt = totalAmt.add(transaction.getAmount());
//            totalDis = totalDis.add(transaction.getDiscount());
//            totalTaxAmt = totalTaxAmt.add(transaction.getTaxAmount());
//            netAmt = netAmt.add(transaction.getNetAmount());
        }
    }
}