package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.converter.TaxConvertor;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.task.TaxLoadTask;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
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
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.task.ProductSaleGetNextCodeTask;
import com.eipl.amcs.operation.inventory.task.ProductSaleSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductSaleTransactionsByInvoiceNoLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ProductSaleAddEditController implements MyInitialization, PopupCallback {

    private final BigDecimal discount = BigDecimal.valueOf(0);
    private final ObjectProperty<ProductSaleTransaction> propSaleTxn;
    private final List<SaleTxnTaxDto> saleTxnTaxDtoList = new ArrayList<>();
    private final List<ProductSaleInstallment> installmentList = new ArrayList<>();
    private final ObservableList<ProductSaleTransaction> listProductSaleTransaction;
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate, btnDelete, btnInstallments, btnProductSave;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxType;
    @FXML
    private E_TextField txtInvoiceNo, txtConsumerName;
    @FXML
    private E_NumericField txtConsumerCode, txtCreditLimit, txtQuantity, txtRate, txtAmount, txtNetAmount,
            txtTotalAmount, txtNoOfInstallment, txtTotalDiscount, txtTotalAmountTax, txtNetPayable;
    @FXML
    private DatePicker dpDate, dpDeductionStartDate;
    @FXML
    private RadioButton rbtnCash, rbtnCredit;
    @FXML
    private ComboBox<Product> cboxProduct;
    @FXML
    private ComboBox<Tax> cboxTaxCode;
    @FXML
    private TableView<ProductSaleTransaction> tableProductSaleTransaction;
    @FXML
    private TableColumn<ProductSaleTransaction, Number> colQuantity, colRate, colAmount, colActualAmount, colTaxAmount;
    @FXML
    private TableColumn<ProductSaleTransaction, Product> colProduct;
    private Map<TaxDetail, BigDecimal> taxBifurcation = null;
    private ProductSale productSale;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private SaleTxnTaxDto saleTxnTaxDto;
    private List<SocietyPaymentCycle> paymentCycleList;
    private List<TaxDto> taxDtoList;

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
        cboxProduct.setEditable(false);
        cboxTaxCode.setEditable(false);
        this.resourceBundle = resourceBundle;
        FocusUtils.requestFocus(txtConsumerCode);
        dpDate.setValue(LocalDate.now());
        rbtnCash.setSelected(true);
        setupComboBox();
        setupTable();
        loadData();

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductSale.fxml"))));
        btnSaveUpdate.setOnAction(e -> {

            if (rbtnCredit.isSelected()) {
                productSale.setPaymentMode((short) 0);
                productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getText()));
                productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
            } else {
                if (rbtnCash.isSelected()) {
                    productSale.setPaymentMode((short) 1);
                    productSale.setNoOfInstallments((short) 0);
                    productSale.setDeductionStartDate(null);
                }
            }


            if (rbtnCash.isSelected())
                validateAndSave();
            else
                checkPaymentCycleAndSaveInstallment((short) 1);
        });

        btnProductSave.setOnAction(e -> {
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
            FocusUtils.requestFocus(btnSaveUpdate);
        });
        btnInstallments.setOnAction(e -> checkPaymentCycleAndSaveInstallment((short) 0));
        btnDelete.setOnAction(e -> deleteData());

        propSaleTxn.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
        });
        txtConsumerCode.setOnAction(e -> {
            String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtConsumerCode.getText()));
            setConsumerName(code);
            FocusUtils.requestFocus(cboxProduct);
        });
        txtConsumerCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtConsumerCode.getText().length() > 0) {
                String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtConsumerCode.getText()));
                setConsumerName(code);
                FocusUtils.requestFocus(cboxProduct);
            }
        });

        txtQuantity.focusedProperty().addListener((ob, oldVal, newVal) -> {
            if (!newVal) {
                calculateAmount();
                calculateTaxAmount();
            }
        });

        rbtnCredit.setOnAction(event -> {
            dpDeductionStartDate.setDisable(false);
            dpDeductionStartDate.setValue(LocalDate.now());
            txtNoOfInstallment.setDisable(false);
            txtNoOfInstallment.setText("1");
            btnInstallments.setDisable(false);
        });
        rbtnCash.setOnAction(event -> {
            dpDeductionStartDate.setDisable(true);
            txtNoOfInstallment.setDisable(true);
            btnInstallments.setDisable(true);
        });


        cboxProduct.setOnAction(e -> {
            if (cboxProduct.getValue() != null) {
                Tax tax = cboxTaxCode.getItems().stream()
                        .filter(p -> p.getCode().equalsIgnoreCase(cboxProduct.getValue().getTax().getCode()))
                        .findFirst().orElse(null);
                if (tax != null)
                    cboxTaxCode.getSelectionModel().select(tax);
                else
                    cboxTaxCode.getSelectionModel().select(cboxTaxCode.getItems().stream()
                            .filter(p -> p.getName().equalsIgnoreCase("NIL")).findFirst().orElse(null));

                fetchSaleRate();
                FocusUtils.requestFocus(txtQuantity);
            }
        });

        cboxTaxCode.setOnAction(e -> {
            if (cboxTaxCode.getValue() != null) {
                if (!txtAmount.getText().isEmpty()) {
                    taxBifurcation = null;
                    BigDecimal taxableAmt = new BigDecimal(txtAmount.getText()).subtract(discount).setScale(2, RoundingMode.HALF_UP);
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
                    FocusUtils.requestFocus(btnProductSave);
                }
            }
        });
    }

    @Override
    public void clearControls() {
        txtRate.setText("");
        txtAmount.setText("");
        txtQuantity.setText("");
        txtNetAmount.setText("");
        cboxProduct.setValue(null);
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
        txn.setRate(new BigDecimal(txtRate.getText()));
        txn.setQuantity(BigDecimal.valueOf(Integer.valueOf(txtQuantity.getText())));
        txn.setAmount(new BigDecimal(txtAmount.getText()));
        txn.setDiscount(new BigDecimal(0));
        txn.setTaxAmount(taxAmount);
        txn.setNetAmount(new BigDecimal(txtNetAmount.getText()));
        txn.setUnionCode(MainApp.identityDto.getUnion().getCode());
        txn.setSocietyCode(MainApp.identityDto.getSociety().getCode());
        txn.setTaxCode(cboxTaxCode.getValue().getCode());
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


    private void calculateAmount() {
        if (!txtQuantity.getText().isEmpty() && !txtRate.getText().isEmpty()) {
            BigDecimal qty = new BigDecimal(txtQuantity.getText());
            BigDecimal rate = new BigDecimal(txtRate.getText());
            BigDecimal amt = qty.multiply(rate).setScale(2, RoundingMode.HALF_DOWN);
            txtAmount.setText(amt.toString());
        }
    }

    private void calculateTaxAmount() {
        if (cboxTaxCode.getValue() != null) {
            if (!txtAmount.getText().isEmpty()) {
                taxBifurcation = null;
                BigDecimal taxableAmt = new BigDecimal(txtAmount.getText()).subtract(discount).setScale(2, RoundingMode.HALF_UP);
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
        if (cboxType.getValue().getKey() == 1) { // Member table
            var task = new MemberByIdLoadTask(text);
            task.setOnSucceeded(e -> {
                try {
                    Member member = task.get();
                    if (member != null) {
                        txtConsumerName.setText(member.getFirstName());
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else if (cboxType.getValue().getKey() == 4) {
            var task = new CustomerByIdLoadTask(text);
            task.setOnSucceeded(e -> {
                try {
                    Customer list = task.get();
                    if (list != null && list.getType() == 4) {
                        txtConsumerName.setText(list.getName());
                        txtCreditLimit.setText("0");
                        txtCreditLimit.setDisable(true);
                        rbtnCredit.setDisable(true);
                    } else {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                                resourceBundle.getString("error.occurred"));
                        alert.createAlert();
                        FocusUtils.requestFocus(txtConsumerCode);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else if (cboxType.getValue().getKey() == 2) {
            var task = new MemberByIdLoadTask(text);
            task.setOnSucceeded(e -> {
                try {
                    Member list = task.get();
                    if (list != null && list.getMemberType().getCode() == 2) {
                        txtConsumerName.setText(list.getFirstName());
                        rbtnCredit.setDisable(true);
                    } else {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                                resourceBundle.getString("error.occurred"));
                        alert.createAlert();
                        FocusUtils.requestFocus(txtConsumerCode);
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
        cboxType.setConverter(new CustomerTypeConvertor(cboxType));
        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
        cboxTaxCode.setConverter(new TaxConvertor(cboxTaxCode));
    }

    public void deleteData() {
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
            colActualAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            propSaleTxn.bind(tableProductSaleTransaction.getSelectionModel().selectedItemProperty());
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
            this.productSale = productSale;
            if (productSale != null) {
                txtConsumerCode.setText(productSale.getConsumerCode().substring(7, 11));
                setConsumerName(productSale.getConsumerCode());
                dpDate.setValue(productSale.getInvoiceDate());
                txtInvoiceNo.setText(productSale.getInvoiceNo());
                btnSaveUpdate.setText(resourceBundle.getString("update"));
                txtTotalAmountTax.setText(productSale.getTaxAmount().toString());
                txtNetPayable.setText(productSale.getNetAmount().toString());
                txtTotalDiscount.setText(productSale.getDiscount().toString());
                txtTotalAmount.setText(productSale.getAmount().toString());
                calculateCredit(productSale.getConsumerCode(), productSale.getConsumerType());
                loadSaleTransaction();
                if (productSale.getPaymentMode() == 0) {
                    rbtnCredit.setSelected(true);
                    txtNoOfInstallment.setDisable(false);
                    txtNoOfInstallment.setText(String.valueOf(productSale.getNoOfInstallments()));
                    dpDeductionStartDate.setValue(productSale.getDeductionStartDate());
                    dpDeductionStartDate.setDisable(false);
                }
            } else {
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

        if (btnSaveUpdate.getText().equalsIgnoreCase(resourceBundle.getString("save"))) {
            saveData();
        } else {
            updateData();
        }
    }

    // 0-from btn installment, 1-from btn save
    private void checkPaymentCycleAndSaveInstallment(short from) {
        var task = new FetchAllPaymentCycleLoadTask(dpDeductionStartDate.getValue(), Integer.parseInt(txtNoOfInstallment.getText()));
        task.setOnSucceeded(e -> {
            try {
                paymentCycleList = task.get();
                if (paymentCycleList.size() >= Integer.parseInt(txtNoOfInstallment.getText())) {
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
        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "InstallmentsAddEdit", installmentList, this);
    }

    private void prepareInstallment() {
        if (cboxType.getValue().getKey() != (short) 1)
            return;
        if (installmentList == null || installmentList.isEmpty()) {
            Integer loopNo = Integer.parseInt(txtNoOfInstallment.getText());
            BigDecimal totalAmount = new BigDecimal(txtNetPayable.getText());
            BigDecimal noOfInstallment = new BigDecimal(txtNoOfInstallment.getText());
            BigDecimal num = totalAmount.divide(noOfInstallment, RoundingMode.HALF_DOWN);
            BigDecimal actualInstallment = new BigDecimal(txtNetPayable.getText());
            BigDecimal lastAmount = actualInstallment.subtract(num.multiply(new BigDecimal(loopNo - 1)));
            for (int i = 1; i <= loopNo; i++) {
                ProductSaleInstallment psi = new ProductSaleInstallment();
                psi.setActualInstallment(actualInstallment);
                if (i == loopNo) {
                    psi.setInstallmentAmount(lastAmount);
                } else {
                    psi.setInstallmentAmount(num);
                }
                psi.setDeductionDate(paymentCycleList.get(i - 1).getToDate().toLocalDate());
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
        productSale.setConsumerCode(generateCode(txtConsumerCode.getText()));
        productSale.setDock(MainApp.identityDto.getDock());
        productSale.setInvoiceDate(dpDate.getValue());
        productSale.setTransactionType((short) 1);
        productSale.setConsumerType(cboxType.getValue().getKey());
        if (rbtnCredit.isSelected()) {
            productSale.setPaymentMode((short) 0);
            productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getText()));
            productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
        } else {
            if (rbtnCash.isSelected()) {
                productSale.setPaymentMode((short) 1);
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
            productSale.setAmount(new BigDecimal(txtTotalAmount.getText()));
            productSale.setDiscount(new BigDecimal(txtTotalDiscount.getText()));
            productSale.setTaxAmount(new BigDecimal(txtTotalAmountTax.getText()));
            productSale.setNetAmount(new BigDecimal(txtNetPayable.getText()));
            productSale.setxCol3("productsale");
            productSale.setNoOfInstallments(Short.parseShort(txtNoOfInstallment.getText()));
            if (rbtnCredit.isSelected()) {
                productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
            }
            productSale.setSociety(MainApp.identityDto.getSociety());
            productSale.setUnion(MainApp.identityDto.getUnion());
            productSale.setDock(MainApp.identityDto.getDock());
            if (rbtnCredit.isSelected()) {
                productSale.setPaymentMode((short) 0);
                productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getText()));
                productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
            } else {
                if (rbtnCash.isSelected()) {
                    productSale.setPaymentMode((short) 1);
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
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductSale.fxml")));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        productSale.setAmount(new BigDecimal(txtTotalAmount.getText()));
        productSale.setDiscount(new BigDecimal(txtTotalDiscount.getText()));
        productSale.setTaxAmount(new BigDecimal(txtTotalAmountTax.getText()));
        productSale.setNetAmount(new BigDecimal(txtNetPayable.getText()));
        if (rbtnCredit.isSelected()) {
            productSale.setPaymentMode((short) 0);
            productSale.setNoOfInstallments(Short.valueOf(txtNoOfInstallment.getText()));
            productSale.setDeductionStartDate(dpDeductionStartDate.getValue());
        } else {
            if (rbtnCash.isSelected()) {
                productSale.setPaymentMode((short) 1);
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
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductSale.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
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
        if (txtConsumerCode.getText().trim().equals("")) {
            errorMsg.append(resourceBundle.getString("product.sale.validation.consumer.code.empty") + "\n");
        }
        if (rbtnCredit.isSelected()) {
            if (dpDeductionStartDate.getValue() == null)
                errorMsg.append(resourceBundle.getString("product.sale.deduction.empty"));
            if (txtNoOfInstallment.getText() == null || txtNoOfInstallment.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("productsale.installment.empty"));
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void loadData() {
        cboxType.setItems(FXCollections.observableList(CommonUtils.getCustomerTypesForProductSale().stream().
                filter(e -> e.getKey() <= (short) 2 || e.getKey() == (short) 4).collect(Collectors.toList())));
        cboxType.getSelectionModel().select(0);
        if (productSale != null) {
            Optional<CustomerTypeKeyValDto> dd = cboxType.getItems().stream()
                    .filter(p -> p.getKey() == productSale.getConsumerType()).findFirst();
            if (dd.isPresent())
                cboxType.getSelectionModel().select(dd.get());
        }

        var task1 = new ProductLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                cboxProduct.setItems(FXCollections.observableList(task1.get().stream().filter(ee -> ee.getCreatedBy() == null ||
                        ee.getCreatedBy().equalsIgnoreCase("SYSTEM")).collect(Collectors.toList())));
                new AutoCompleteComboBoxListener<>(cboxProduct);
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
                new AutoCompleteComboBoxListener<>(cboxTaxCode);
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
        txtTotalDiscount.setText(CommonUtils.scale2RoundUp(totalDis).toString());
        txtTotalAmountTax.setText(CommonUtils.scale2RoundUp(totalTaxAmt).toString());
        txtNetPayable.setText(CommonUtils.scale2RoundUp(netAmt).toString());
    }
}



