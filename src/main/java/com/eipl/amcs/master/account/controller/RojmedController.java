package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.table.DataEntryRow;
import com.eipl.amcs.controls.table.SummaryRow;
import com.eipl.amcs.controls.table.TableRowModel;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.task.LedgerDeleteTask;
import com.eipl.amcs.master.account.task.RojmedOpeningBalanceLoadTask;
import com.eipl.amcs.master.account.task.VoucherTransactionByDateLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.eipl.amcs.utils.AppConstant.DATE_FORMATTER;

@Slf4j
public class RojmedController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Ledger> propLedger;
    @FXML
    StackPane root;
    @FXML
    TableView tableData;
    @FXML
    TableView tableData1;

    @FXML
    E_DatePicker dpDate;
    @FXML
    Button btnClose, btnCredit, btnDebit, btnJournal, btnSale, btnPurchase, btnPrint, btnPrev, btnNext;
    @FXML
    private Label lblOpeningBalance, lblClosingBalance, lblRojmelHeader, lblTotalCredit, lblTotalDebit;

    BigDecimal crTotal = BigDecimal.ZERO;
    BigDecimal drTotal = BigDecimal.ZERO;
    BigDecimal openingBalance = BigDecimal.ZERO;
    BigDecimal closingBalance = BigDecimal.ZERO;
    private ResourceBundle resourceBundle;
    private List<Voucher> voucherList = new ArrayList<>();
    private List<VoucherTransaction> voucherTransactionList = new ArrayList<>();
    private Map<String, VoucherTransaction> voucherTransactionMap = new HashMap<>();

    public RojmedController() {
        propLedger = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        dpDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getOpeningLedgerBalance();
                lblRojmelHeader.setText(String.format(resourceBundle.getString("rojmedtodate"), dpDate.getValue().format(DATE_FORMATTER)));
            }
        });
        dpDate.setValue(LocalDate.now());
        btnCredit.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryCredit", null, this, resourceBundle.getString("credit.entry"));
        });
        btnDebit.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryDebit", null, this, resourceBundle.getString("debit.entry"));
        });
        btnJournal.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "Havalo", null, this, resourceBundle.getString("journal.entry"));
        });
        btnSale.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductSale.fxml")));
        });
        btnPurchase.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductReceipt.fxml")));
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnNext.setOnAction(e -> {
            dpDate.setValue(dpDate.getValue().plusDays(1));
        });
        btnPrev.setOnAction(e -> {
            dpDate.setValue(dpDate.getValue().minusDays(1));
        });
        loadData();
    }


    @Override
    public void loadData() {
        getOpeningLedgerBalance();
    }

    private void loadVoucherTransactionByDate(LocalDate fromDate, LocalDate toDate) {
        try {
            var task = new VoucherTransactionByDateLoadTask(fromDate, toDate);
            task.setOnSucceeded(e -> {
                try {
                    voucherTransactionList = task.get();
                    if (voucherTransactionList != null) {
                        voucherTransactionMap = voucherTransactionList.stream()
                                .collect(Collectors.toMap(
                                        vt -> vt.getCode(),
                                        vt -> vt
                                ));

                    }
                    generateTable();
                    closingBalance = openingBalance.add(crTotal.subtract(drTotal));

                    if (closingBalance.compareTo(BigDecimal.ZERO) < 0) {
                        lblClosingBalance.setText(Math.abs(closingBalance.doubleValue()) + " Debit");
                        lblClosingBalance.setTextFill(Color.RED);
                    } else {
                        lblClosingBalance.setText(closingBalance + " Credit");
                        lblClosingBalance.setTextFill(Color.GREEN);
                    }

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Ledger dto = propLedger.get();
            if (dto != null) {
                var task = new LedgerDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    private void generateTable() {
        try {
            tableData.getColumns().clear();
            tableData1.getColumns().clear();
            tableData.getItems().clear();
            tableData1.getItems().clear();
            lblTotalCredit.setText("0");
            lblTotalDebit.setText("0");
            crTotal = BigDecimal.ZERO;
            drTotal = BigDecimal.ZERO;
            List<String> columnNames = Arrays.asList(resourceBundle.getString("ledger"),"", resourceBundle.getString("amount"));

            for (String name : columnNames) {
                // Create Column for Table 1
                TableColumn<TableRowModel, String> col1 = new TableColumn<>(name);
                col1.setCellValueFactory(data -> data.getValue().columnProperty(name));
                col1.setPrefWidth(120);
                tableData.getColumns().add(col1);

                // Create a completely NEW instance for Table 2
                TableColumn<TableRowModel, String> col2 = new TableColumn<>(name);
                col2.setCellValueFactory(data -> data.getValue().columnProperty(name));
                col2.setPrefWidth(120);
                tableData1.getColumns().add(col2);
            }
            if (voucherTransactionList == null || voucherTransactionList.isEmpty())
                return;

            // Filter data
            List<VoucherTransaction> creditVoucherTransaction = voucherTransactionList.stream()
                    .filter(vt -> vt.getCreditDebit() == true)
                    .collect(Collectors.toList());

            List<VoucherTransaction> debitVoucherTransaction = voucherTransactionList.stream()
                    .filter(vt -> vt.getCreditDebit() == false)
                    .collect(Collectors.toList());

            // Assign items - these will now remain independent
            tableData.setItems(generateData(creditVoucherTransaction));
            tableData1.setItems(generateData(debitVoucherTransaction));

            crTotal = creditVoucherTransaction.stream()
                    .map(VoucherTransaction::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            drTotal = debitVoucherTransaction.stream()
                    .map(VoucherTransaction::getAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

            lblTotalCredit.setText(crTotal.toString());
            lblTotalDebit.setText(drTotal.toString());

            tableData.setRowFactory(tv -> {
                TableRow<TableRowModel> row = new TableRow<TableRowModel>() {
                    @Override
                    protected void updateItem(TableRowModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item instanceof SummaryRow) {
                            setStyle("-fx-background-color: #e8f4ff; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                };

                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        TableRowModel rowData = row.getItem();
                        if (rowData instanceof DataEntryRow) {
                            DataEntryRow dataRow = (DataEntryRow) rowData;
                            String id = dataRow.getId();

                            handleCreditDoubleClick(id);
                        }
                    }
                });
                return row;
            });

            tableData1.setRowFactory(tv -> {
                TableRow<TableRowModel> row = new TableRow<TableRowModel>() {
                    @Override
                    protected void updateItem(TableRowModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item instanceof SummaryRow) {
                            setStyle("-fx-background-color: #e8f4ff; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                };

                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        TableRowModel rowData = row.getItem();
                        if (rowData instanceof DataEntryRow) {
                            DataEntryRow dataRow = (DataEntryRow) rowData;
                            String id = dataRow.getId();

                            handleDebitDoubleClick(id);
                        }
                    }
                });
                return row;
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ObservableList<TableRowModel> generateData(List<VoucherTransaction> voucherTransactionList) {
        ObservableList<TableRowModel> data = FXCollections.observableArrayList();

        Map<Ledger, List<VoucherTransaction>> ledgerTransactionMap = new HashMap<>();

        for (VoucherTransaction voucherTransaction : voucherTransactionList) {
            List<VoucherTransaction> tempTxnList = ledgerTransactionMap.get(voucherTransaction.getLedger());

            if (tempTxnList == null || tempTxnList.isEmpty())
                tempTxnList = new ArrayList<>();

            tempTxnList.add(voucherTransaction);
            ledgerTransactionMap.put(voucherTransaction.getLedger(), tempTxnList);
        }

        for (Ledger ledger : ledgerTransactionMap.keySet()) {
            SummaryRow sum1 = new SummaryRow();
            sum1.setColumnValue(resourceBundle.getString("ledger"), ledger.toString());
            BigDecimal totalAmt = BigDecimal.ZERO;

            List<DataEntryRow> dataEntryRowList = new ArrayList<>();
            for (VoucherTransaction voucherTransaction : ledgerTransactionMap.get(ledger)) {
                totalAmt = totalAmt.add(voucherTransaction.getAmount());
                DataEntryRow row1 = new DataEntryRow();
                row1.setColumnValue(resourceBundle.getString("ledger"), " - " + voucherTransaction.getNarration());
                row1.setColumnValue("", voucherTransaction.getAmount().toString());
                row1.setId(voucherTransaction.getCode());
                dataEntryRowList.add(row1);
            }
            sum1.setColumnValue(resourceBundle.getString("amount"), totalAmt.toString());
            data.add(sum1);
            data.addAll(dataEntryRowList);
        }

        return data;
    }

    private void handleCreditDoubleClick(String id) {
        VoucherTransaction vt = voucherTransactionMap.get(id);
        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryCredit", vt.getVoucher(), this, resourceBundle.getString("credit.entry"));
    }

    private void handleDebitDoubleClick(String id) {
        VoucherTransaction vt = voucherTransactionMap.get(id);
        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryDebit", vt.getVoucher(), this, resourceBundle.getString("debit.entry"));
    }

    private void getOpeningLedgerBalance() {
        var task = new RojmedOpeningBalanceLoadTask(dpDate.getValue().minusDays(1));
        task.setOnSucceeded(e -> {
            try {
                openingBalance = task.get();
                log.info(openingBalance.toString());
                loadVoucherTransactionByDate(dpDate.getValue(), dpDate.getValue());

                if (openingBalance.compareTo(BigDecimal.ZERO) < 0) {
                    lblOpeningBalance.setText(Math.abs(openingBalance.doubleValue()) + " Debit");
                    lblOpeningBalance.setTextFill(Color.RED);
                } else {
                    lblOpeningBalance.setText(openingBalance.doubleValue() + " Credit");
                    lblOpeningBalance.setTextFill(Color.GREEN);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
