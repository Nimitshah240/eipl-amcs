package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.*;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
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
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.master.inventory.task.ProductSaleRateByProductLoadTask;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import com.eipl.amcs.master.operation.task.CustomerByIdLoadTask;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.master.operation.task.MemberCreditLimitLoadTask;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.FetchAllPaymentCycleLoadTask;
import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.*;
import com.eipl.amcs.operation.inventory.task.*;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.LocalSaleByMemberAndDateLoadTask;
import com.eipl.amcs.operation.procurement.task.MilkCollectionByMemberAndDateLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ProductSaleAddEditController implements MyInitialization, PopupCallback {

    private final BigDecimal discount = BigDecimal.valueOf(0);
    private final ObjectProperty<ProductSaleTransaction> propSaleTxn;
    private final List<SaleTxnTaxDto> saleTxnTaxDtoList = new ArrayList<>();
    private final List<ProductSaleInstallment> installmentList = new ArrayList<>();
    private final ObservableList<ProductSaleTransaction> listProductSaleTransaction;
    @FXML
    private GridPane gridMaster;
    @FXML
    private StackPane root;
    @FXML
    private E_Button btnClose, btnSaveUpdate, btnDelete, btnInstallments, btnProductSave;
    @FXML
    private AutoSearchTextField<CustomerTypeKeyValDto> cboxType;
    @FXML
    private E_TextField txtInvoiceNo, txtConsumerName,  txtBatch;
    @FXML
    private E_NumericField txtConsumerCode, txtCreditLimit,txtDifferance, txtQuantity, txtRate, txtAmount, txtNetAmount,
            txtTotalAmount, txtNoOfInstallment, txtTotalDiscount, txtMilkAmount, txtDeductionAmount, txtTotalAmountTax, txtNetPayable;
    @FXML
    private E_DatePicker dpMilkFromDate, dpMilkToDate, dpDeductionToDate, dpDeductionFromDate, dpDate, dpDeductionStartDate;
    @FXML
    private RadioButton rbtnCash, rbtnCredit;
    @FXML
    private AutoSearchTextField<Product> cboxProduct;
    @FXML
    private AutoSearchTextField<Tax> cboxTaxCode;
    @FXML
    private TableView<ProductSaleTransaction> tableProductSaleTransaction;
    @FXML
    private TableColumn<ProductSaleTransaction, Number> colQuantity, colRate, colAmount, colActualAmount, colTaxAmount;
    @FXML
    private TableColumn<ProductSaleTransaction, Product> colProduct;
    @FXML
    private E_Label lblStock;

    private ProductStock stock;

    private Map<TaxDetail, BigDecimal> taxBifurcation = null;
    private ProductSale productSale;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private SaleTxnTaxDto saleTxnTaxDto;
    private List<SocietyPaymentCycle> paymentCycleList;
    private List<TaxDto> taxDtoList;
    private ProductSaleTransaction r = null;
    private PopupCallback callback;
    private Stage stage;

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ProductSaleAddEditController() {
        propSaleTxn = new SimpleObjectProperty<>();
        listProductSaleTransaction = FXCollections.observableArrayList();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        FocusUtils.requestFocus(txtConsumerCode);
        dpDate.setValue(LocalDate.now());
        dpDeductionStartDate.setValue(LocalDate.now());
        dpDate.setConverter(new LocalDateConvertor());
        dpDeductionFromDate.setConverter(new LocalDateConvertor());
        rbtnCredit.setSelected(true);
        txtNoOfInstallment.setText("1");
        cboxType.setDisable(true);
        dpDeductionStartDate.setDisable(false);
        setupComboBox();
        setupTable();
        btnClose.setOnAction(e -> this.stage.close());
        txtDifferance.setText("0");
        txtMilkAmount.setText("0");
        txtDeductionAmount.setText("0");
        dpDate.setOnAction(e -> {
            if (!dpDeductionStartDate.isDisable()) {
                dpDeductionStartDate.setValue(dpDate.getValue());
            }
        });
        btnSaveUpdate.setOnAction(e -> {
            if (rbtnCredit.isSelected()) {
                productSale.setPaymentMode((short) 1);
                productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getInputText()));
                productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
            } else if (rbtnCash.isSelected()) {
                productSale.setPaymentMode((short) 0);
                productSale.setNoOfInstallments((short) 0);
                productSale.setDeductionStartDate(null);
            }
            if (rbtnCash.isSelected())
                validateAndSave();
            else
                checkPaymentCycleAndSaveInstallment((short) 1);
        });

        btnProductSave.setOnAction(e -> {
            if (!validateProductSave()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                        errorMsg.toString());
                alert.createAlert();
                FocusUtils.requestFocus(cboxProduct);
                return;
            }
            if (productSale == null) {
                productSale = new ProductSale();
                setValuesInObject();
            }
            saleTxnTaxDto = new SaleTxnTaxDto();
            saleTxnTaxDto.setSaleTaxList(setValuesInSaleTax());
            ProductSaleTransaction txn = setValuesInSaleTransaction();
            saleTxnTaxDto.setTransaction(txn);
            saleTxnTaxDtoList.add(saleTxnTaxDto);
            listProductSaleTransaction.add(txn);
            tableProductSaleTransaction.setItems(listProductSaleTransaction);
            calculateSummary();
            clearControls();
            FocusUtils.requestFocus(cboxProduct);
            cboxTaxCode.getSelectionModel().clearSelection();
            cboxProduct.getSelectionModel().clearSelection();
        });
        btnInstallments.setOnAction(e -> checkPaymentCycleAndSaveInstallment((short) 0));
        btnDelete.setOnAction(e -> deleteData());

        propSaleTxn.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            r = newValue;
        });

        txtConsumerCode.setOnAction(e -> {
            String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtConsumerCode.getInputText()));
            setConsumerName(code);
        });
        txtConsumerCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && !txtConsumerCode.getInputText().isEmpty() && !txtConsumerCode.getInputText().equalsIgnoreCase("0")) {
                String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtConsumerCode.getInputText()));
                setConsumerName(code);
            }
        });

        txtRate.textProperty().addListener((ob, oldVal, newVal) -> {
            if (!oldVal.equalsIgnoreCase(newVal)) {
                if (!txtQuantity.getInputText().isEmpty()) {
                    if (new BigDecimal(txtQuantity.getInputText()).compareTo(BigDecimal.ZERO) <= 0) {
                        txtQuantity.setText("");
                        txtAmount.setText("");
                        txtNetAmount.setText("");
                        return;
                    }
                }
                calculateAmount();
                calculateTaxAmount();
            }
        });
        txtQuantity.textProperty().addListener((ob, oldVal, newVal) -> {
            if (!oldVal.equalsIgnoreCase(newVal)) {
                if (!txtQuantity.getInputText().isEmpty()) {
                    if (new BigDecimal(txtQuantity.getInputText()).compareTo(BigDecimal.ZERO) <= 0) {
                        txtQuantity.setText("");
                        txtAmount.setText("");
                        txtNetAmount.setText("");
                        FocusUtils.requestFocus(txtQuantity);
                        return;
                    }
                }
                calculateAmount();
                calculateTaxAmount();
            }
        });

        dpMilkFromDate.setDisable(false);
        dpMilkToDate.setDisable(false);
        dpDeductionFromDate.setDisable(false);
        dpDeductionToDate.setDisable(false);
        dpDeductionStartDate.setDisable(false);
        dpDeductionStartDate.setValue(dpDate.getValue());
        rbtnCredit.setOnAction(event -> {
            cboxType.setDisable(true);
            cboxType.getSelectionModel().select(0);
            txtConsumerCode.clear();
            txtConsumerName.clear();
            txtNoOfInstallment.setDisable(true);
            txtNoOfInstallment.setText("1");
            btnInstallments.setDisable(false);
            txtConsumerCode.setDisable(false);
        });
        rbtnCash.setOnAction(event -> {
            cboxType.setDisable(false);
            Optional<CustomerTypeKeyValDto> customerDto = cboxType.getItems().stream()
                    .filter(item -> item.getKey() > 2).findFirst();
            customerDto.ifPresent(dto -> cboxType.getSelectionModel().select(dto));
            txtConsumerCode.setText("1");
            String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtConsumerCode.getInputText()));
            setConsumerName(code);
            dpMilkFromDate.setDisable(true);
            dpMilkToDate.setDisable(true);
            dpDeductionFromDate.setDisable(true);
            dpDeductionToDate.setDisable(true);
            dpDeductionStartDate.setDisable(true);
            txtNoOfInstallment.setDisable(true);
            btnInstallments.setDisable(true);
            cboxType.setDisable(true);
            txtConsumerCode.setDisable(true);
        });


        cboxProduct.setOnAction(e -> {
            if (cboxProduct.getValue() != null) {
                Tax tax = cboxTaxCode.getItems().stream()
                        .filter(p -> p.getCode().equalsIgnoreCase(cboxProduct.getValue().getTax() == null ? null : cboxProduct.getValue().getTax().getCode()))
                        .findFirst().orElse(null);
                if (tax != null)
                    cboxTaxCode.getSelectionModel().select(tax);
                else
                    cboxTaxCode.getSelectionModel().select(cboxTaxCode.getItems().stream()
                            .filter(p -> p.getName().equalsIgnoreCase("NIL")).findFirst().orElse(null));

                fetchSaleRate();
                if (cboxProduct.getValue() != null)
                    fetchStock();
            }
        });

        cboxTaxCode.setOnAction(e -> {
            if (cboxTaxCode.getValue() != null) {
                if (!txtAmount.getInputText().isEmpty()) {
                    taxBifurcation = null;
                    BigDecimal taxableAmt = new BigDecimal(txtAmount.getInputText()).subtract(discount).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal taxAmount = BigDecimal.ZERO;
                    if (!cboxTaxCode.getValue().getName().equalsIgnoreCase("NIL")) {
                        taxBifurcation = CommonUtils.calculateAndFetchTaxBifurcation(taxDtoList.stream()
                                .filter(p -> p.getTax().getCode().equalsIgnoreCase(cboxTaxCode.getValue().getCode())).findFirst().orElse(null), taxableAmt);
                        if (taxBifurcation != null) {
                            for (TaxDetail taxDtl : taxBifurcation.keySet()) {
                                taxAmount = taxAmount.add(taxBifurcation.get(taxDtl)).setScale(2, RoundingMode.HALF_UP);
                            }
                        }
                    }
                    BigDecimal totalAmt = taxableAmt.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
                    txtNetAmount.setText(totalAmt.toString());
                }
            }
        });

        dpMilkFromDate.setOnAction(e -> getDeductionAndPurchaseData());
        dpMilkToDate.setOnAction(e -> getDeductionAndPurchaseData());
        dpDeductionFromDate.setOnAction(e -> getDeductionData());
        dpDeductionToDate.setOnAction(e -> getDeductionData());
        txtConsumerCode.setOnAction(e -> {
            getDeductionAndPurchaseData();
        });

        txtRate.setOnAction(e -> {
            FocusUtils.requestFocus(btnProductSave);
            e.consume();
        });

        tableProductSaleTransaction.setOnKeyPressed(event -> {
            ProductSaleTransaction dto = tableProductSaleTransaction.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propSaleTxn.get();
                    if (dto != null)
                        deleteData();
                    break;
            }
        });

        txtRate.setDisable(MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("fifo"));

        txtQuantity.setOnAction(e -> {
            if (txtRate.isDisable() || !txtRate.isEditable())
                FocusUtils.requestFocus(btnProductSave);
        });
    }

    public void setDate(LocalDate date) {
        dpDate.setValue(date);
    }

    @Override
    public void clearControls() {
        txtRate.setText("0");
        txtAmount.setText("0");
        txtQuantity.setText("0");
        txtNetAmount.setText("0");
        cboxProduct.setValue(null);
        lblStock.setText("");
        txtBatch.setText("");
        FocusUtils.requestFocus(cboxProduct);
    }

    private ProductSaleTransaction setValuesInSaleTransaction() {
        BigDecimal taxAmount = BigDecimal.ZERO;
        if (taxBifurcation != null) {
            for (BigDecimal value : taxBifurcation.values()) {
                taxAmount = taxAmount.add(value);
            }
        }
        ProductSaleTransaction txn = new ProductSaleTransaction();
        txn.setProduct(cboxProduct.getValue());
        txn.setRate(new BigDecimal(txtRate.getInputText()));
        txn.setQuantity(new BigDecimal(txtQuantity.getInputText()));
        txn.setAmount(new BigDecimal(txtAmount.getInputText()));
        txn.setDiscount(new BigDecimal(0));
        txn.setTaxAmount(taxAmount);
        txn.setNetAmount(new BigDecimal(txtNetAmount.getInputText()));
        txn.setUnionCode(MainApp.identityDto.getUnion().getCode());
        txn.setSocietyCode(MainApp.identityDto.getSociety().getCode());
        txn.setTaxCode(cboxTaxCode.getValue().getCode());
        txn.setBatchNo(txtBatch.getText());
        return txn;
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

    private void fetchSaleRate() {
        var task = new ProductSaleRateByProductLoadTask(cboxProduct.getValue().getCode(), dpDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                ProductSaleRate rate = task.get();
//                if (rate == null) {
//                    txtRate.setText("0");
//                } else {
//                    txtRate.setText(rate.getRate().toString());
//                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void fetchStock() {
        var task = new ProductStockByCodeLoadTask(cboxProduct.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                this.stock = task.get();
                if (stock == null) {
                    lblStock.setText("0 " + resourceBundle.getString("quantityy"));
                } else {
                    lblStock.setText(stock.getStock().setScale(2, RoundingMode.UP) + " " + resourceBundle.getString("quantityy"));
                }

                if (MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("FIFO") && (this.stock != null && (this.stock.getSaleRate() == null || this.stock.getSaleRate().compareTo(BigDecimal.ZERO) == 0))) {
                    txtRate.setDisable(false);
                    txtRate.setEditable(true);
                } else {
                    txtRate.setEditable(true);
                    txtRate.setText(this.stock == null ? "0" : String.valueOf(this.stock.getSaleRate() == null ? 0 : this.stock.getSaleRate()));
                }
                txtBatch.setText(this.stock == null ? null : this.stock.getBatchNo());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void calculateAmount() {
        if (!txtQuantity.getInputText().isEmpty() && !txtRate.getInputText().isEmpty()) {
            BigDecimal qty = new BigDecimal(txtQuantity.getInputText());
            BigDecimal rate = new BigDecimal(txtRate.getInputText());
            BigDecimal amt = qty.multiply(rate).setScale(2, RoundingMode.HALF_DOWN);
            txtAmount.setText(amt.toString());
        }
    }

    private void calculateTaxAmount() {
        if (cboxTaxCode.getValue() != null) {
            if (!txtAmount.getInputText().isEmpty()) {
                taxBifurcation = null;
                BigDecimal taxableAmt = new BigDecimal(txtAmount.getInputText()).subtract(discount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal taxAmount = BigDecimal.ZERO;
                if (!cboxTaxCode.getValue().getName().equalsIgnoreCase("NIL")) {
                    taxBifurcation = CommonUtils.calculateAndFetchTaxBifurcation(taxDtoList.stream()
                            .filter(p -> p.getTax().getCode().equalsIgnoreCase(cboxTaxCode.getValue().getCode())).findFirst().orElse(null), taxableAmt);
                    if (taxBifurcation != null) {
                        for (TaxDetail taxDtl : taxBifurcation.keySet()) {
                            taxAmount = taxAmount.add(taxBifurcation.get(taxDtl)).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                }
                BigDecimal totalAmt = taxableAmt.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
                txtNetAmount.setText(totalAmt.toString());
            }
        }
    }

    private void setConsumerName(String text) {
        if (cboxType.getValue() != null && cboxType.getValue().getKey() < 3) { // Member table
            var task = new MemberByIdLoadTask(text);
            task.setOnSucceeded(e -> {
                try {
                    Member member = task.get();
                    if (member != null) {
                        txtConsumerName.setText(member.toMemberName());
                    } else {
                        txtConsumerCode.setText("");
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                                resourceBundle.getString("membernotfound")
                        );
                        alert.createAlert();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else if (cboxType.getValue() != null && cboxType.getValue().getKey() > 2) {
            var task = new CustomerByIdLoadTask(text, Integer.valueOf(cboxType.getValue().getKey()));
            task.setOnSucceeded(e -> {
                try {
                    Customer list = task.get();
                    if (list != null) {
                        txtConsumerName.setText(list.getName());
//                        txtCreditLimit.setText("0");
//                        txtCreditLimit.setDisable(true);
                    } else {
                        txtConsumerCode.setText("");
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                                resourceBundle.getString("customernotfound"));
                        alert.createAlert();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else if (cboxType.getValue() != null && cboxType.getValue().getKey() == 2) {
            var task = new MemberByIdLoadTask(text);
            task.setOnSucceeded(e -> {
                try {
                    Member list = task.get();
                    if (list != null && list.getMemberType().getCode() == 2) {
                        txtConsumerName.setText(list.toMemberName());
                    } else {
                        txtConsumerCode.setText("");
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                                resourceBundle.getString("error.occurred"));
                        alert.createAlert();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    public void calculateCredit(String consumerCode, Short consumerType) {
        var task = new MemberCreditLimitLoadTask(consumerCode, consumerType);
        task.setOnSucceeded(e -> {
            try {
                MemberCreditLimit list = task.get();
                if (list != null) {
                    BigDecimal credit;
                    credit = list.getBalance();
                    txtCreditLimit.setText(credit.toString());
                }
            } catch (InterruptedException | ExecutionException ex) {
                txtCreditLimit.setText("na madyu");
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });
        dpDeductionStartDate.setConverter(new LocalDateConvertor());
        dpDeductionStartDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDeductionStartDate.setValue(dpDeductionStartDate.getConverter().fromString(dpDeductionStartDate.getEditor().getText()));
            }
        });
        cboxType.setOnAction(e -> {
                    txtConsumerCode.clear();
                    txtConsumerName.clear();
                    txtDifferance.setText("0");
                    txtMilkAmount.setText("0");
                    txtDeductionAmount.setText("0");
                    if (cboxType.getSelectionModel().getSelectedItem().getKey() > 2) {
                        rbtnCredit.setDisable(true);
                        rbtnCash.setSelected(true);
                    } else {
                        rbtnCredit.setDisable(false);
                    }
                }
        );
    }

    public void deleteData() {
        SaleTxnTaxDto removesaleTxnTaxDto = null;
        for (SaleTxnTaxDto saleTxnTaxDto1 : saleTxnTaxDtoList) {
            if (saleTxnTaxDto1.getTransaction() == r) {
                removesaleTxnTaxDto = saleTxnTaxDto1;
                break;
            }
        }

        saleTxnTaxDtoList.remove(removesaleTxnTaxDto);
        listProductSaleTransaction.remove(propSaleTxn.get());
        tableProductSaleTransaction.setItems(listProductSaleTransaction);
        calculateSummary();
    }

    public void setupTable() {
        try {
            colProduct.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct()));
            colQuantity.setCellValueFactory(data -> new SimpleObjectProperty<>(Double.parseDouble(String.valueOf(data.getValue().getQuantity()))));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colTaxAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTaxAmount()));
            colActualAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNetAmount()));
            propSaleTxn.bind(tableProductSaleTransaction.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableProductSaleTransaction);
        } catch (Exception e) {
            System.out.println("ProductSaleAddEdit setuptable Exception");
            e.printStackTrace();
        }
    }

    private String generateCode(String code) {
        if (!(code.length() >= 4)) {
            while (code.length() != 4) {
                code = "0" + code;
            }
        }
        code = MainApp.identityDto.getSociety().getCode() + code;
        return code;
    }


    public void setProductSale(ProductSale productSale) {
        try {
            cboxType.setItems(FXCollections.observableList(CommonUtils.getCustomerTypesForProductSale()));
            cboxType.getSelectionModel().select(0);

            this.productSale = productSale;
            if (productSale != null) {
                Optional<CustomerTypeKeyValDto> dd = cboxType.getItems().stream()
                        .filter(p -> p.getKey() == productSale.getConsumerType()).findFirst();
                if (dd.isPresent())
                    cboxType.getSelectionModel().select(dd.get());

                txtConsumerCode.setText(productSale.getConsumerCode().substring(MainApp.getUser().getSociety().getCode().length()));
                setConsumerName(productSale.getConsumerCode());
                dpDate.setValue(productSale.getInvoiceDate());
                txtInvoiceNo.setText(productSale.getInvoiceNo());
                btnSaveUpdate.setText(resourceBundle.getString("update"));
                txtTotalAmountTax.setText(productSale.getTaxAmount() == null ? "0" : productSale.getTaxAmount().toString());
                txtNetPayable.setText(productSale.getNetAmount().toString());
//                txtTotalDiscount.setText(productSale.getDiscount().toString());
                txtTotalAmount.setText(productSale.getAmount().toString());
                calculateCredit(productSale.getConsumerCode(), productSale.getConsumerType());
                loadSaleTransaction();
                if (productSale.getPaymentMode() == 0) {
                    rbtnCredit.setSelected(false);
                    rbtnCash.setSelected(true);
                    txtNoOfInstallment.setDisable(true);
                    dpDeductionStartDate.setDisable(true);
                }
                if (productSale.getPaymentMode() == 1) {
                    rbtnCredit.setSelected(true);
                    rbtnCash.setSelected(false);
                    txtNoOfInstallment.setDisable(false);
                    txtNoOfInstallment.setText(String.valueOf(productSale.getNoOfInstallments()));
                    dpDeductionStartDate.setValue(productSale.getDeductionStartDate());
                    dpDeductionStartDate.setDisable(false);
                }
            } else {
                cboxType.getSelectionModel().select(0);
                btnSaveUpdate.setText(resourceBundle.getString("save"));
                getNextCode();
            }
            loadData();
        } catch (Exception e) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"), "Error");
            alert.createAlert();
            System.out.println("Nimit error : " + e);
            throw new RuntimeException(e);
        }
    }

    private void getNextCode() {
        var task = new ProductSaleGetNextCodeTask();
        task.setOnSucceeded(event -> {
            try {
                txtInvoiceNo.setText(task.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void validateAndSave() {
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        BigDecimal diff = BigDecimal.ZERO;
        if (!txtDifferance.getInputText().equals("0"))
            diff = new BigDecimal(txtDifferance.getInputText()).subtract(new BigDecimal(txtNetPayable.getInputText()));

        if (rbtnCredit.isSelected() && diff.compareTo(BigDecimal.ZERO) < 0) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                    resourceBundle.getString("productsale.transaction.creditlimit.not.availabel"));
            alert.createAlert();
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.CANCEL)
                return;
        }
        if (rbtnCredit.isSelected()) {
            String formattedDate = dpDeductionStartDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String pattern = resourceBundle.getString("save.confirmation.msg");
            String msg = MessageFormat.format(pattern, formattedDate);
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("productsale"), msg);
            alert.createAlert();
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.CANCEL)
                return;
        }
        if (btnSaveUpdate.getText().equalsIgnoreCase(resourceBundle.getString("save"))) {
            saveData();
        } else {
            updateData();
        }
    }

    // 0-from btn installment, 1-from btn save
    private void checkPaymentCycleAndSaveInstallment(short from) {
        if (dpDeductionStartDate.getValue().isBefore(dpDate.getValue())) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                    resourceBundle.getString("productsale.installment.start.validation.date.past"));
            alert.createAlert();
            return;
        }
        var task = new FetchAllPaymentCycleLoadTask(dpDeductionStartDate.getValue(), Integer.parseInt(txtNoOfInstallment.getInputText()));
        task.setOnSucceeded(e -> {
            try {
                paymentCycleList = task.get();
                if (paymentCycleList.size() >= Integer.parseInt(txtNoOfInstallment.getInputText())) {
                    if (from == (short) 0) {
                        prepareInstallment();
                        viewInstallmentPopup();
                    } else if ((short) 1 == from) {
                        prepareInstallment();
                        validateAndSave();
                    }
                } else {
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                            resourceBundle.getString("societypaymentcycle.not.available"));
                    alert.createAlert();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void viewInstallmentPopup() {
        if (installmentList == null || installmentList.isEmpty())
            return;
        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "InstallmentsAddEdit", installmentList, this, resourceBundle.getString("installmenttitle"));
    }

    private void prepareInstallment() {
        if (cboxType.getValue().getKey() != (short) 1)
            return;
        if (installmentList == null || installmentList.isEmpty()) {
            Integer loopNo = Integer.parseInt(txtNoOfInstallment.getInputText());
            BigDecimal totalAmount = new BigDecimal(txtNetPayable.getInputText());
            BigDecimal noOfInstallment = new BigDecimal(txtNoOfInstallment.getInputText());
            BigDecimal num = totalAmount.divide(noOfInstallment, RoundingMode.HALF_DOWN);
            BigDecimal actualInstallment = new BigDecimal(txtNetPayable.getInputText());
            BigDecimal lastAmount = actualInstallment.subtract(num.multiply(new BigDecimal(loopNo - 1)));
            for (int i = 1; i <= loopNo; i++) {
                ProductSaleInstallment psi = new ProductSaleInstallment();
                psi.setActualInstallment(actualInstallment);
                if (i == loopNo) {
                    psi.setInstallmentAmount(lastAmount);
                } else {
                    psi.setInstallmentAmount(num);
                }
//                psi.setDeductionDate(paymentCycleList.get(i - 1).getToDate().toLocalDate());
                psi.setDeductionDate(dpDeductionStartDate.getValue());
                psi.setSocietyPaymentCycle(paymentCycleList.get(i - 1));
                psi.setSocietyCode(MainApp.identityDto.getSociety().getCode());
                psi.setUnionCode(MainApp.identityDto.getUnion().getCode());
                psi.setBilling(false);
                psi.setType(1);
                psi.setPreviousPendingAmount(BigDecimal.ZERO);
                installmentList.add(psi);
            }
        } else {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("installment"),
                    resourceBundle.getString("regenerate"));
            Optional<ButtonType> result = alert.createYesNoConfirmationAlert();
            if (result.get() == ButtonType.YES) {
                installmentList.clear();
                prepareInstallment();
            }
        }
    }

    private void setValuesInObject() {
        productSale.setInvoiceNo(txtInvoiceNo.getText());
        productSale.setSociety(MainApp.identityDto.getSociety());
        productSale.setUnion(MainApp.identityDto.getUnion());
        productSale.setConsumerCode(generateCode(txtConsumerCode.getInputText()));
        productSale.setDock(MainApp.identityDto.getDock());
        productSale.setInvoiceDate(dpDate.getValue());
        productSale.setTransactionType((short) 1);
        productSale.setConsumerType(cboxType.getValue().getKey());
        if (rbtnCredit.isSelected()) {
            productSale.setPaymentMode((short) 1);
            productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getInputText()));
            productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
        } else {
            if (rbtnCash.isSelected()) {
                productSale.setPaymentMode((short) 0);
                productSale.setNoOfInstallments((short) 0);
                productSale.setDeductionStartDate(null);
            }
        }
    }

    @Override
    public void saveData() {
        if (saleTxnTaxDtoList == null || saleTxnTaxDtoList.isEmpty()) {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                    resourceBundle.getString("add.product"));
            alert.createAlert();
        } else {
            productSale.setAmount(new BigDecimal(txtTotalAmount.getInputText()));
            productSale.setInvoiceDate(dpDate.getValue());
            productSale.setTaxAmount(new BigDecimal(txtTotalAmountTax.getInputText()));
            productSale.setNetAmount(new BigDecimal(txtNetPayable.getInputText()));
            productSale.setBmcCode(MainApp.identityDto.getSociety().getBmc().getCode());
            productSale.setMccPlantCode(MainApp.identityDto.getSociety().getMcc().getCode());
            productSale.setPlantCode(MainApp.identityDto.getSociety().getPlant().getCode());
            productSale.setxCol3("productsale");
            productSale.setNoOfInstallments(Short.parseShort(txtNoOfInstallment.getInputText()));
            if (rbtnCredit.isSelected()) {
                productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
            }
            productSale.setSociety(MainApp.identityDto.getSociety());
            productSale.setUnion(MainApp.identityDto.getUnion());
            productSale.setDock(MainApp.identityDto.getDock());
            if (rbtnCredit.isSelected()) {
                productSale.setPaymentMode((short) 1);
                productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getInputText()));
                productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
            } else {
                if (rbtnCash.isSelected()) {
                    productSale.setPaymentMode((short) 0);
                    productSale.setNoOfInstallments((short) 0);
                    productSale.setDeductionStartDate(null);
                }
            }
            ProductSaleDto productsaleDto = new ProductSaleDto();
            productsaleDto.setProductSale(productSale);
            productsaleDto.setSaleTxnTaxDtoList(saleTxnTaxDtoList);
            productsaleDto.setSaleInstallments(installmentList);
            productsaleDto.getProductSale().setUnion(MainApp.identityDto.getUnion());
            productsaleDto.getProductSale().setSociety(MainApp.identityDto.getSociety());
            productsaleDto.getProductSale().setDock(MainApp.identityDto.getDock());
            var task = new ProductSaleSaveTask(productsaleDto, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(resourceBundle.getString(subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                                sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                            resourceBundle.getString("productsale.insert.successful"));
                    alert.createAlert();
                    txtConsumerCode.setText("");
                    txtConsumerName.setText("");
                    cboxType.getSelectionModel().select(0);
                    tableProductSaleTransaction.getItems().clear();
                    initialize(null, resourceBundle);
                    this.callback.reloadData(true);
                    txtTotalAmount.setText("0");
                    txtTotalAmountTax.setText("0");
                    txtNetAmount.setText("0");
                    lblStock.setText("");
                    txtNetPayable.setText("0");
//                    this.stage.close();
//                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductSale.fxml")));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            task.setOnFailed(e -> {
                Throwable t = task.getException();
                String errorMessage = "error.occurred";
                if (t.getMessage().contains("billing.already.completed")) {
                    errorMessage = "billing.already.done";
                } else if (t.getMessage().contains("StockIsLessThanZero")) {
                    errorMessage = "stock.going.to.zero";
                } else if (t.getMessage().contains("paymentcyclenotfound")) {
                    errorMessage = "paymentcyclenotfound";
                }
                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                        resourceBundle.getString(errorMessage));
                alert1.createAlert();
            });

            new Thread(task).start();
        }
    }

    private void loadSaleTransaction() {
        var task = new ProductSaleTransactionsByInvoiceNoLoadTask(txtInvoiceNo.getText());
        task.setOnSucceeded(e -> {
            try {
                List<SaleTxnTaxDto> list = task.get();
                if (list == null)
                    return;
                saleTxnTaxDtoList.addAll(list);
                for (int i = 0; i < saleTxnTaxDtoList.size(); i++) {
                    listProductSaleTransaction.add(saleTxnTaxDtoList.get(i).getTransaction());
                }
                tableProductSaleTransaction.setItems(FXCollections.observableList(listProductSaleTransaction));
                propSaleTxn.bind(tableProductSaleTransaction.getSelectionModel().selectedItemProperty());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        productSale.setAmount(new BigDecimal(txtTotalAmount.getInputText()));
//        productSale.setDiscount(new BigDecimal(txtTotalDiscount.getText()));
        productSale.setTaxAmount(new BigDecimal(txtTotalAmountTax.getInputText()));
        productSale.setNetAmount(new BigDecimal(txtNetPayable.getInputText()));
        if (rbtnCredit.isSelected()) {
            productSale.setPaymentMode((short) 1);
            productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getInputText()));
            productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
        } else {
            if (rbtnCash.isSelected()) {
                productSale.setPaymentMode((short) 0);
                productSale.setNoOfInstallments((short) 0);
                productSale.setDeductionStartDate(null);
            }
        }
        ProductSaleDto productsaleDto = new ProductSaleDto();
        productsaleDto.setProductSale(productSale);
        List<SaleTxnTaxDto> list2 = new ArrayList<>();
        for (SaleTxnTaxDto txnTaxDto : saleTxnTaxDtoList) {
            for (ProductSaleTransaction productSaleTransaction : listProductSaleTransaction) {
                if (txnTaxDto.getTransaction() == productSaleTransaction)
                    list2.add(txnTaxDto);
            }
        }
        productsaleDto.setSaleTxnTaxDtoList(list2);
        productsaleDto.setSaleInstallments(installmentList);
        var task = new ProductSaleSaveTask(productsaleDto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append((subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                        resourceBundle.getString("productsale.update.successful"));
                alert.createAlert();
                tableProductSaleTransaction.getItems().clear();
                initialize(null, resourceBundle);
                txtConsumerCode.setText("");
                txtConsumerName.setText("");
                cboxType.getSelectionModel().select(0);
                tableProductSaleTransaction.getItems().clear();
                initialize(null, resourceBundle);
                this.callback.reloadData(true);
//                this.callback.reloadData(true);
//                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            Throwable t = task.getException();
            String errorMessage = "error.occurred";
            if (t.getMessage().contains("billing.already.completed")) {
                errorMessage = "billing.already.done";
            }
            if (t.getMessage().contains("StockIsLessThanZero")) {
                errorMessage = "stock.going.to.zero";
            }
            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                    resourceBundle.getString(errorMessage));
            alert1.createAlert();
        });
        new Thread(task).start();
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (dpDate.getValue() == null) {
            errorMsg.append(resourceBundle.getString("product.sale.validation.date.empty") + "\n");
        }
        if (cboxType.getValue() == null) {
            errorMsg.append(resourceBundle.getString("product.sale.validation.type.empty") + "\n");
        }
        if (txtConsumerCode.getInputText().trim().equals("")) {
            errorMsg.append(resourceBundle.getString("product.sale.validation.consumer.code.empty") + "\n");
        }
        if (rbtnCredit.isSelected()) {
            if (dpDeductionStartDate.getValue() == null)
                errorMsg.append(resourceBundle.getString("product.sale.deduction.empty"));
            if (txtNoOfInstallment.getInputText() == null || txtNoOfInstallment.getInputText().isEmpty())
                errorMsg.append(resourceBundle.getString("productsale.installment.empty"));
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void loadData() {
        var task1 = new ProductLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                cboxProduct.setItems(FXCollections.observableList(task1.get()));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task2 = new TaxLoadTask();
        task2.setOnSucceeded(ee -> {
            try {
                taxDtoList = task2.get();
                cboxTaxCode.setItems(FXCollections.observableList(CommonUtils.getTaxFromDto(taxDtoList)));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    private void calculateSummary() {
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal totalDis = BigDecimal.ZERO;
        BigDecimal totalTaxAmt = BigDecimal.ZERO;
        BigDecimal netAmt = BigDecimal.ZERO;

        for (ProductSaleTransaction transaction : listProductSaleTransaction) {
            totalAmt = totalAmt.add(transaction.getAmount());
            totalTaxAmt = totalTaxAmt.add(transaction.getTaxAmount());
            netAmt = netAmt.add(transaction.getNetAmount());
        }
        txtTotalAmount.setText(CommonUtils.scale2RoundUp(totalAmt).toString());
//        txtTotalDiscount.setText(CommonUtils.scale2RoundUp(totalDis).toString());
        txtTotalAmountTax.setText(CommonUtils.scale2RoundUp(totalTaxAmt).toString());
        txtNetPayable.setText(CommonUtils.scale2RoundUp(netAmt).toString());
    }

    private boolean validateProductSave() {
        errorMsg = new StringBuilder();
        if (txtRate.getInputText().trim().isEmpty() || new BigDecimal(txtRate.getInputText().trim()).compareTo(BigDecimal.ZERO) == 0) {
            errorMsg.append(resourceBundle.getString("productsale.transaction.validation.rate.empty") + "\n");
        }

        if (txtQuantity.getInputText().trim().isEmpty() || new BigDecimal(txtQuantity.getInputText().trim()).compareTo(BigDecimal.ZERO) == 0) {
            errorMsg.append(resourceBundle.getString("qtyzero") + "\n");
        }
        if (stock == null || stock.getStock().subtract(new BigDecimal(txtQuantity.getInputText())).compareTo(BigDecimal.ZERO) < 0)
            errorMsg.append(resourceBundle.getString("stock.not.available") + "\n");

        if (dpDate.getValue() == null || dpDate.getValue().isAfter(LocalDate.now()))
            errorMsg.append(resourceBundle.getString("future.sale.not.possible") + "\n");

        if (cboxProduct.getSelectionModel().getSelectedItem() == null)
            errorMsg.append(resourceBundle.getString("productnullerror") + "\n");

        return errorMsg.length() == 0;

    }

    private void getDeductionAndPurchaseData() {
        if ((txtConsumerCode.getInputText() != null || !txtConsumerCode.getInputText().isBlank() && cboxType.getSelectionModel().getSelectedIndex() == 0)) {
            String memberCode = generateCode(txtConsumerCode.getInputText());
            if (dpMilkFromDate.getValue() != null && dpMilkToDate.getValue() != null) {
                var task = new MilkCollectionByMemberAndDateLoadTask(dpMilkFromDate.getValue(), dpMilkToDate.getValue(), memberCode);
                task.setOnSucceeded(e -> {
                    try {
                        List<MilkCollection> list = task.get();
                        BigDecimal collectionAmt = BigDecimal.ZERO;
                        for (MilkCollection milkCollection : list) {
                            collectionAmt = collectionAmt.add(milkCollection.getAmount());
                        }
                        txtMilkAmount.setText(collectionAmt.toString());
                        getDeductionData();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
                new Thread(task).start();
            }
        }
    }

    private void getDeductionData() {
        String memberCode = generateCode(txtConsumerCode.getInputText());
        if (dpDeductionFromDate.getValue() != null && dpDeductionToDate.getValue() != null) {
            var task = new ProductSaleByMemberAndDateLoadTask(memberCode, dpDeductionFromDate.getValue(), dpDeductionToDate.getValue());
            task.setOnSucceeded(e -> {
                try {
                    List<ProductSale> list = task.get();
                    BigDecimal psAmt = BigDecimal.ZERO;
                    for (ProductSale productSale : list) {
                        psAmt = psAmt.add(productSale.getAmount());
                    }

                    var task2 = new LocalSaleByMemberAndDateLoadTask(memberCode, dpDeductionFromDate.getValue(), dpDeductionToDate.getValue());
                    BigDecimal finalPsAmt = psAmt;
                    task2.setOnSucceeded(es -> {
                        try {
                            List<LocalMilkSale> localMilkSaleListlist = task2.get();
                            BigDecimal lmsAmt = BigDecimal.ZERO;
                            for (LocalMilkSale localMilkSale : localMilkSaleListlist) {
                                lmsAmt = lmsAmt.add(localMilkSale.getAmount());
                            }
                            txtDeductionAmount.setText(finalPsAmt.add(lmsAmt).toString());
                            BigDecimal finalValue = new BigDecimal(txtMilkAmount.getInputText()).subtract(finalPsAmt.add(lmsAmt));
                            txtDifferance.setText(finalValue.toString());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    new Thread(task2).start();


                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        }
    }

    private void reloadScreen() {
        // 1. Reset core data models and lists
        this.productSale = null;
        this.r = null;
        this.taxBifurcation = null;
        this.saleTxnTaxDtoList.clear();
        this.listProductSaleTransaction.clear();
        this.installmentList.clear();

        // 2. Clear out tables and summaries
        tableProductSaleTransaction.getItems().clear();
        txtTotalAmount.setText("0");
        txtTotalAmountTax.setText("0");
        txtNetPayable.setText("0");
        txtTotalDiscount.setText("0");
        lblStock.setText("0 " + resourceBundle.getString("quantityy"));
        dpDate.setValue(LocalDate.now());
        rbtnCredit.setSelected(true);
        rbtnCash.setSelected(false);
        cboxType.setDisable(true);
        if (!cboxType.getItems().isEmpty()) {
            cboxType.getSelectionModel().select(0);
        }
        txtConsumerCode.clear();
        txtConsumerName.clear();
        txtCreditLimit.clear();
        txtDifferance.setText("0");
        txtMilkAmount.setText("0");
        txtDeductionAmount.setText("0");
        txtNoOfInstallment.setText("1");
        cboxProduct.getSelectionModel().clearSelection();
        cboxTaxCode.getSelectionModel().clearSelection();
        getNextCode();

        btnSaveUpdate.setText(resourceBundle.getString("save"));
        FocusUtils.requestFocus(txtConsumerCode);
        lblStock.setText("");
    }
}



