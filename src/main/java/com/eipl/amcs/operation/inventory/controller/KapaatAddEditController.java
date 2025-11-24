package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.master.procurement.converter.SocietyPaymentCycleConvertor;
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
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KapaatAddEditController implements MyInitialization {
    private final List<ProductSaleInstallment> installmentList = new ArrayList<>();
    private final List<ProductSaleTransaction> transactionList = new ArrayList<>();
    private final List<SaleTxnTaxDto> saleTxnTaxDtoList = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSave;
    @FXML
    private ComboBox<SocietyPaymentCycle> cboxPaymentCycle;
    @FXML
    private TextField txtCode, txtBalance, txtTotal, txtDue, txtName;
    @FXML
    private TableView<Product> tableData;
    @FXML
    private TableColumn<Product, String> colName, colAmount;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        setupTable();
        loadPaymentCycle();
        setupComboBox();
        getNextCode();
        btnClose.setOnAction(e -> {
            this.callback.reloadData(true);
            this.stage.close();
        });
        txtCode.focusedProperty().addListener((ob, oldVal, newVal) -> {
            txtTotal.setText("0");
            txtDue.setText("0");
            txtBalance.setText("0");
            if (!newVal) {
                fetchMemberDetails();
            }
        });
        txtCode.setOnAction(e -> {
            fetchMemberDetails();
            FocusUtils.requestFocus(lblTotal);
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
    }

    private void fetchMemberDetails() {
        memberCode = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtCode.getText()));
        var task = new MemberByIdLoadTask(memberCode);
        task.setOnSucceeded(e -> {
            try {
                member = task.get();
                if (member != null) {
                    txtName.setText(member.getFirstName());
                    getBalance(memberCode);
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"), "Invalid member");
                    alert.createAlert();
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
    public void setupComboBox() {
        cboxPaymentCycle.setConverter(new SocietyPaymentCycleConvertor(cboxPaymentCycle));
    }

    @Override
    public void loadData() {
        var task = new ProductLoadTask();
        task.setOnSucceeded(e -> {
            try {
                productList = new ArrayList<>();
                productList.addAll(task.get().stream().filter(ee -> ee.getCreatedBy() == null ||
                        ee.getCreatedBy().equalsIgnoreCase("SYSTEM")).collect(Collectors.toList()));
                tableData.setItems(FXCollections.observableList(productList));
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
            tableData.setEditable(true);
            colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().toString()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getxCol1() != null ? data.getValue().getxCol1() : "0"));
            colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
            colAmount.setOnEditCommit(event -> {
                try {
                    Product w = event.getRowValue();
                    if (new BigDecimal(event.getNewValue()).compareTo(BigDecimal.ZERO) < 0) {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                                resourceBundle.getString("greaterthan.amount"));
                        alert.createAlert();
                        throw new IllegalArgumentException();
                    } else {
                        w.setxCol1(event.getNewValue());
                        setControls();
                        FocusUtils.requestFocus(lblTotal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e1) {
            System.out.println("KapatAddEdit setuptable Exception");
            e1.printStackTrace();
        }
    }

    private void setControls() {
        if (productList == null)
            return;
        due = BigDecimal.ZERO;
        netPayable = new BigDecimal(txtBalance.getText());
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
        tableData.refresh();
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
            if (Double.parseDouble(txtDue.getText()) > 0) {
                createDto();
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
                        loadData();
                        txtTotal.setText("0");
                        txtDue.setText("0");
                        txtBalance.setText("0");
                        productSaleDto = new ProductSaleDto();
                        installmentList.clear();
                        saleTxnTaxDtoList.clear();
                        transactionList.clear();
                        getNextCode();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            } else {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                        resourceBundle.getString("error.occurred"));
                alert.createAlert();
            }
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
        installment.setDeductionDate(cboxPaymentCycle.getValue().getToDate().toLocalDate().minusDays(1));
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
        productSale.setDeductionStartDate(cboxPaymentCycle.getValue().getToDate().toLocalDate().minusDays(1));
        productSale.setDiscount(BigDecimal.ZERO);
        productSale.setDock(MainApp.identityDto.getDock());
        productSale.setInvoiceDate(cboxPaymentCycle.getValue().getToDate().toLocalDate().minusDays(1));
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
    }

}
