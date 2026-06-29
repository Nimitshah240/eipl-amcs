package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleLoadTask;
import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.task.ProductSaleGetNextCodeTask;
import com.eipl.amcs.operation.inventory.task.ProductSaleSaveTask;
import com.eipl.amcs.operation.procurement.task.MemberTotalAmountLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KapaatAddEditController implements MyInitialization {
    private final List<ProductSaleInstallment> installmentList = new ArrayList<>();
    private final List<ProductSaleTransaction> transactionList = new ArrayList<>();
    private final List<SaleTxnTaxDto> saleTxnTaxDtoList = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    private final ObjectProperty<ProductSaleTransaction> propSaleTransaction;
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSave;
    @FXML
    private E_Button btnAdd, btnDelete;
    @FXML
    private AutoSearchTextField<SocietyPaymentCycle> cboxPaymentCycle;
    @FXML
    private E_TextField txtName;
    @FXML
    private E_NumericField txtCode, txtBalance, txtTotal, txtDue, txtAmount, txtMobileNo;
    @FXML
    private TableView<ProductSaleTransaction> tableData;
    @FXML
    private DatePicker dpDeductionDate;
    @FXML
    private TableColumn<ProductSaleTransaction, String> colProduct, colAmount;
    private Stage stage;
    @FXML
    private Label lblTotal;
    private ResourceBundle resourceBundle;
    private PopupCallback callback;
    private List<Product> productList;
    private List<SocietyPaymentCycle> paymentCycleList;
    private BigDecimal netPayable = BigDecimal.ZERO;
    private BigDecimal due = BigDecimal.ZERO;
    private BigDecimal productSaleAmount = BigDecimal.ZERO;
    private Member member;
    private String memberCode;
    private ProductSaleDto productSaleDto;
    private String invoiceNo;
    private StringBuilder errorMsg = null;
    @FXML
    private AutoSearchTextField<Product> cboxProduct;

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public KapaatAddEditController() {
        propSaleTransaction = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        setupTable();
        loadPaymentCycle();
        setupComboBox();
        getNextCode();
        productSaleAmount = BigDecimal.ZERO;
        dpDeductionDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> {
            this.callback.reloadData(true);
            this.stage.close();
        });
        btnAdd.setOnAction(e -> {
            addProduct();
            FocusUtils.requestFocus(cboxProduct);
        });
        txtAmount.setOnAction(e -> {
            FocusUtils.requestFocus(btnAdd);
        });
        txtTotal.setText("0");
        txtCode.focusedProperty().addListener((ob, oldVal, newVal) -> {
            calculateTotal();
            txtDue.setText("0");
            txtBalance.setText("0");
        });
        txtCode.setOnAction(e -> {
            fetchMemberDetails();
        });
        btnSave.setOnAction(e -> {
            saveData();
        });
        root.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case S:
                    saveData();
                    break;
                case C:
                    FocusUtils.requestFocus(txtCode);
                case ESCAPE:
                    this.callback.reloadData(true);
                    this.stage.close();
                    break;
                default:
            }
        });


        tableData.setOnKeyPressed(event -> {
            ProductSaleTransaction dto = tableData.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propSaleTransaction.get();
                    if (dto != null)
                        deleteProductSale(dto);
                    break;
            }
        });
    }

    private void fetchMemberDetails() {
        memberCode = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtCode.getInputText()));
        var task = new MemberByIdLoadTask(memberCode);
        task.setOnSucceeded(e -> {
            try {
                member = task.get();
                if (member != null) {
                    txtName.setText(member.toMemberName());
                    txtMobileNo.setText(member.getMobileNo());
                    getBalance(memberCode);
                } else {
                    txtName.clear();
                    FocusUtils.requestFocus(txtCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getBalance(String code) {
        var task = new MemberTotalAmountLoadTask(cboxPaymentCycle.getValue().getCode(), code);
        task.setOnSucceeded(e -> {
            try {
                BigDecimal balance = task.get();
                if (balance != null && balance.compareTo(BigDecimal.ZERO) != 0) {
                    txtBalance.setText(balance.toString());
                    txtDue.setText("0");
                    txtTotal.setText(balance.toString());
                } else {
                }
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    @Override
    public void loadData() {
        var task = new ProductLoadTask();
        task.setOnSucceeded(e -> {
            try {
                productList = new ArrayList<>();
                productList = task.get();
                if (productList != null && !productList.isEmpty())
                    cboxProduct.setItems(productList);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNextCode() {
        var task = new ProductSaleGetNextCodeTask();
        task.setOnSucceeded(event -> {
            try {
                invoiceNo = task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setupTable() {
        try {
            colProduct.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct().toString()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount() != null ? data.getValue().getAmount().toString() : "0"));
            propSaleTransaction.bind(tableData.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableData);
        } catch (Exception e1) {
            System.out.println("KapatAddEdit setuptable Exception");
            e1.printStackTrace();
        }
    }

    private void setControls() {
        if (productList == null)
            return;
        due = BigDecimal.ZERO;
        netPayable = new BigDecimal(txtBalance.getInputText());
        for (Product mb : productList) {
            if (mb.getxCol1() != null && !mb.getxCol1().equalsIgnoreCase("")) {
                due = due.add(new BigDecimal(mb.getxCol1()));
            } else {
                due = due.add(BigDecimal.ZERO);
            }
        }
        System.out.println(due);
        netPayable = netPayable.subtract(due);
        txtTotal.setText(netPayable.toString());
        txtDue.setText(due.toString());
    }

    public void loadPaymentCycle() {
        var task = new SocietyPaymentCycleLoadTask();
        task.setOnSucceeded(e -> {
            try {
                paymentCycleList = new ArrayList<>();
                List<SocietyPaymentCycle> paymentCycleList1 = new ArrayList<>();
                paymentCycleList.addAll(task.get());
                paymentCycleList1.addAll(paymentCycleList.stream().filter(e1 -> !e1.getBilling() && !e1.getLockBillingProcess() &&
                                e1.getFromDate().toLocalDate().isAfter
                                        (LocalDate.now().minusMonths(3)))
                        .collect(Collectors.toList()));
                cboxPaymentCycle.setItems(FXCollections.observableList(paymentCycleList1));
                cboxPaymentCycle.getSelectionModel().select(0);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    @Override
    public void saveData() {
        try {
            if (!validate()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
                        errorMsg.toString());
                alert.createAlert();
                return;
            }
            saveNewSale();
            var task = new ProductSaleSaveTask(productSaleDto, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(resourceBundle.getString(subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                                sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                            resourceBundle.getString("save"));
                    alert.createAlert();
                    txtCode.setText("");
                    txtName.setText("");
                    FocusUtils.requestFocus(txtCode);
                    txtTotal.setText("0");
                    txtDue.setText("0");
                    txtBalance.setText("0");
                    productSaleDto = new ProductSaleDto();
                    installmentList.clear();
                    saleTxnTaxDtoList.clear();
                    transactionList.clear();
                    getNextCode();
                    cboxProduct.getSelectionModel().clearSelection();
                    tableData.getItems().clear();
                    transactionList.clear();
                    tableData.refresh();
                    txtMobileNo.setText("");
                    this.callback.reloadData(true);
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            task.setOnFailed(e -> {
                Throwable t = task.getException();
                String errorMessage = "error.occurred";
                if (t.getMessage().contains("billing.already.completed")) {
                    errorMessage = "billing.already.done";
                } else if (t.getMessage().contains("paymentcyclenotfound")) {
                    errorMessage = "paymentcyclenotfound";
                }
                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                        resourceBundle.getString(errorMessage));
                alert1.createAlert();
            });
            new Thread(task).start();
        } catch (Exception e) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        }
    }


    private void createDto() {
        ProductSale productSale = new ProductSale();
        ProductSaleInstallment installment = new ProductSaleInstallment();
        ProductSaleTransaction transaction;
        productSaleAmount = BigDecimal.ZERO;
        SaleTxnTaxDto saleTxnTaxDto;
        for (Product product : productList) {
            if (product.getxCol1() == null || product.getxCol1().equalsIgnoreCase("0") || product.getxCol1().equalsIgnoreCase(""))
                continue;
            transaction = new ProductSaleTransaction();
            transaction.setAmount(new BigDecimal(product.getxCol1()));
            transaction.setProduct(product);
            transaction.setDiscount(BigDecimal.ZERO);
            transaction.setNetAmount(transaction.getAmount());
            transaction.setQuantity(BigDecimal.valueOf(1));
            transaction.setRate(transaction.getAmount());
            transaction.setTaxAmount(BigDecimal.ZERO);
            transaction.setTaxCode(null);
            transaction.setUnionCode(MainApp.identityDto.getUnion().getCode());
            transaction.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            transaction.setUnitCode(null);
            transaction.setProductSaleToMember(productSale);
            transactionList.add(transaction);
            productSaleAmount = productSaleAmount.add(transaction.getAmount());
            saleTxnTaxDto = new SaleTxnTaxDto(transaction, null);
            saleTxnTaxDtoList.add(saleTxnTaxDto);
        }
        //ProductSaleInstallment
        installment.setInstallmentAmount(productSaleAmount);
        installment.setActualInstallment(productSaleAmount);
        installment.setBilling(false);
        installment.setDeductionDate(dpDeductionDate.getValue());
        installment.setType(1);
        installment.setPreviousPendingAmount(BigDecimal.ZERO);
        installment.setMember(member);
        installment.setSocietyPaymentCycle(cboxPaymentCycle.getValue());
        installment.setUnionCode(MainApp.identityDto.getUnion().getCode());
        installment.setSocietyCode(MainApp.identityDto.getSociety().getCode());
        installment.setInvoiceNo(invoiceNo);
        installmentList.add(installment);

        //ProductSale
        productSale.setAmount(productSaleAmount);
        productSale.setConsumerCode(memberCode);
        productSale.setConsumerType((short) 1);
        productSale.setSociety(MainApp.identityDto.getSociety());
        productSale.setUnion(MainApp.identityDto.getUnion());
        productSale.setDeductionStartDate(dpDeductionDate.getValue());
        productSale.setDiscount(BigDecimal.ZERO);
        productSale.setDock(MainApp.identityDto.getDock());
        productSale.setInvoiceDate(dpDeductionDate.getValue());
        productSale.setxCol3(LocalDate.now().toString());
        productSale.setNetAmount(productSaleAmount);
        productSale.setNoOfInstallments((short) 1);
        productSale.setPaymentMode((short) 1);
        productSale.setTransactionType((short) 1);
        productSale.setTaxAmount(null);
        productSale.setVoucherNo(null);
        productSale.setxCol3("kapaat");
        productSale.setInvoiceNo(invoiceNo);

        productSaleDto = new ProductSaleDto();
        productSaleDto.setProductSale(productSale);
        productSaleDto.setSaleInstallments(installmentList);
        productSaleDto.setSaleTxnTaxDtoList(saleTxnTaxDtoList);
    }

    public void clearControls() {
        cboxProduct.getSelectionModel().clearSelection();
        txtAmount.setText("0");
    }

    private void addProduct() {
        try {

            BigDecimal amt = new BigDecimal(txtAmount.getInputText());
            if (amt.compareTo(BigDecimal.ZERO) <= 0) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"), resourceBundle.getString("invalid.amount"));
                alert.createAlert();
                return;
            }
            Product product = cboxProduct.getSelectionModel().getSelectedItem();
            ProductSaleTransaction transaction;
            transaction = new ProductSaleTransaction();
            transaction.setAmount(amt);
            transaction.setProduct(product);
            transaction.setDiscount(BigDecimal.ZERO);
            transaction.setNetAmount(amt);
            transaction.setQuantity(BigDecimal.valueOf(1));
            transaction.setRate(amt);
            transaction.setTaxAmount(BigDecimal.ZERO);
            transaction.setTaxCode(null);
            transaction.setUnionCode(MainApp.identityDto.getUnion().getCode());
            transaction.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            transaction.setUnitCode(null);
            transactionList.add(transaction);
            calculateTotal();
            clearControls();
            tableData.setItems(FXCollections.observableList(transactionList));
        } catch (
                RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveNewSale() {
        try {
            ProductSaleInstallment installment = new ProductSaleInstallment();
            ProductSale productSale = new ProductSale();

            //ProductSaleInstallment
            installment.setInstallmentAmount(productSaleAmount);
            installment.setActualInstallment(productSaleAmount);
            installment.setBilling(false);
            installment.setDeductionDate(dpDeductionDate.getValue());
            installment.setType(1);
            installment.setPreviousPendingAmount(BigDecimal.ZERO);
            installment.setMember(member);
            installment.setSocietyPaymentCycle(cboxPaymentCycle.getValue());
            installment.setUnionCode(MainApp.identityDto.getUnion().getCode());
            installment.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            installment.setInvoiceNo(invoiceNo);
            installmentList.add(installment);

            //ProductSale
            productSale.setAmount(productSaleAmount);
            productSale.setConsumerCode(memberCode);
            productSale.setConsumerType((short) 1);
            productSale.setSociety(MainApp.identityDto.getSociety());
            productSale.setUnion(MainApp.identityDto.getUnion());
            productSale.setDeductionStartDate(dpDeductionDate.getValue());
            productSale.setDiscount(BigDecimal.ZERO);
            productSale.setDock(MainApp.identityDto.getDock());
            productSale.setInvoiceDate(dpDeductionDate.getValue());
            productSale.setxCol3(LocalDate.now().toString());
            productSale.setNetAmount(productSaleAmount);
            productSale.setNoOfInstallments((short) 1);
            productSale.setPaymentMode((short) 1);
            productSale.setTransactionType((short) 1);
            productSale.setTaxAmount(null);
            productSale.setVoucherNo(null);
            productSale.setxCol3("kapaat");
            productSale.setInvoiceNo(invoiceNo);


            for (ProductSaleTransaction pst : transactionList) {
                pst.setProductSaleToMember(productSale);
                SaleTxnTaxDto saleTxnTaxDto;
                saleTxnTaxDto = new SaleTxnTaxDto(pst, null);
                saleTxnTaxDtoList.add(saleTxnTaxDto);
            }
            productSaleDto = new ProductSaleDto();
            productSaleDto.setProductSale(productSale);
            productSaleDto.setSaleInstallments(installmentList);
            productSaleDto.setSaleTxnTaxDtoList(saleTxnTaxDtoList);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void calculateTotal() {
        BigDecimal amt = BigDecimal.ZERO;
        for (ProductSaleTransaction pst : transactionList) {
            amt = amt.add(pst.getAmount());
        }
        txtTotal.setText(amt.toString());
        productSaleAmount = amt;

    }

    private void deleteProductSale(ProductSaleTransaction productSaleTransaction) {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                if (productSaleTransaction.getInvoiceTxnNo() != null && !productSaleTransaction.getInvoiceTxnNo().isBlank()) {
                    alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                            resourceBundle.getString("cannot.delete.saved.txn"));
                    alert.createConfirmationAlert();
                } else {
                    transactionList.remove(productSaleTransaction);
                    tableData.setItems(FXCollections.observableList(transactionList));
                    calculateTotal();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (dpDeductionDate.getValue() == null) {
            errorMsg.append(resourceBundle.getString("product.sale.validation.date.empty") + "\n");
        }
        if (txtCode.getInputText().trim().equals("")) {
            errorMsg.append(resourceBundle.getString("product.sale.validation.consumer.code.empty") + "\n");
        }
        if (cboxPaymentCycle.getValue() == null) {
            errorMsg.append(resourceBundle.getString("societypaymentcycle.not.available"));
        }
        if (transactionList.isEmpty()) {
            errorMsg.append(resourceBundle.getString("add.product"));
        }
        return errorMsg.length() == 0;
    }
}
