package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.controls.table.DataEntryRow;
import com.eipl.amcs.controls.table.SummaryRow;
import com.eipl.amcs.controls.table.TableRowModel;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import com.eipl.amcs.master.account.task.LedgerDeleteTask;
import com.eipl.amcs.master.account.task.RojmedOpeningBalanceLoadTask;
import com.eipl.amcs.master.account.task.VoucherTransactionByDateLoadTask;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptRepository;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptTransactionRepository;
import com.eipl.amcs.operation.inventory.repository.ProductSaleRepository;
import com.eipl.amcs.operation.inventory.repository.ProductSaleTransactionRepository;
import com.eipl.amcs.utils.FocusUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    TableView<TableRowModel> tableData;
    @FXML
    TableView<TableRowModel> tableData1;

    @FXML
    E_DatePicker dpDate;
    @FXML
    Button btnClose, btnCredit, btnDebit, btnJournal, btnSale, btnPurchase, btnPrint, btnPrev, btnNext, btnReportGenerate;
    @FXML
    private Label lblRojmelHeader, lblTotalCredit, lblTotalDebit, lblTotalCredit2, lblTotalDebit2;

    BigDecimal crTotal = BigDecimal.ZERO;
    BigDecimal drTotal = BigDecimal.ZERO;
    BigDecimal openingBalance = BigDecimal.ZERO;
    BigDecimal closingBalance = BigDecimal.ZERO;
    private ResourceBundle resourceBundle;
    private List<VoucherTransaction> voucherTransactionList = new ArrayList<>();
    private List<VoucherTransaction> crVoucherTransactionList = new ArrayList<>();
    private List<VoucherTransaction> drVoucherTransactionList = new ArrayList<>();
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
        dpDate.setOnAction(e -> {
            FocusUtils.requestFocus(btnCredit);
        });
        dpDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getOpeningLedgerBalance();
                lblRojmelHeader.setText(String.format(resourceBundle.getString("rojmedtodate"), dpDate.getValue().format(DATE_FORMATTER)));
            }
        });
        dpDate.setValue(LocalDate.now());
        btnCredit.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryCredit", dpDate.getValue(), this, resourceBundle.getString("credit.entry"));
        });
        btnDebit.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryDebit", dpDate.getValue(), this, resourceBundle.getString("debit.entry"));
        });
        btnJournal.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "Havalo", dpDate.getValue(), this, resourceBundle.getString("journal.entry"));
        });
        btnSale.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductSaleAddEdit", dpDate.getValue(), this, resourceBundle.getString("productsale"));
        });
        btnPurchase.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductReceiptAddEdit", dpDate.getValue(), this, resourceBundle.getString("productreceipt"));
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnReportGenerate.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/report/account/StockValuation.fxml")));
        });

        btnNext.setOnAction(e -> {
            dpDate.setValue(dpDate.getValue().plusDays(1));
        });
        btnPrev.setOnAction(e -> {
            dpDate.setValue(dpDate.getValue().minusDays(1));
        });
        btnPrint.setOnAction(e -> {
            exportToExcel();
        });

        loadData();
        dpDate.setConverter(new LocalDateConvertor());

        root.setFocusTraversable(true);
        Platform.runLater(() -> root.requestFocus());
        root.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case R:
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryCredit", dpDate.getValue(), this, resourceBundle.getString("credit.entry"));
                    break;
                case T:
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryDebit", dpDate.getValue(), this, resourceBundle.getString("debit.entry"));
                    break;
                case J:
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "Havalo", dpDate.getValue(), this, resourceBundle.getString("journal.entry"));
                    break;
                case N:
                    dpDate.setValue(dpDate.getValue().plusDays(1));
                    break;
                case P:
                    if (event.isControlDown()) {
                        exportToExcel();
                        break;
                    }
                    dpDate.setValue(dpDate.getValue().minusDays(1));
                    break;
                case ESCAPE:
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
                    break;
                case F2:
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "NarrationAddEdit", null, this, resourceBundle.getString("narrationname"));
                    break;
                case F5:
                    loadData();
                    break;
                case A:
                    if (event.isControlDown()) {
                        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerAddEdit", null, this, resourceBundle.getString("ledger"));
                    }
                    break;
                case DIGIT1:
                case NUMPAD1:
                    if (event.isControlDown()) {
                        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductReceiptAddEdit", dpDate.getValue(), this, resourceBundle.getString("productreceipt"));
                    }
                    break;
                case DIGIT2:
                case NUMPAD2:
                    if (event.isControlDown()) {
                        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductSaleAddEdit", dpDate.getValue(), this, resourceBundle.getString("productsale"));
                    }
                    break;
            }
        });

    }

    private void exportToExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls"));
        File file = fileChooser.showSaveDialog(MainApp.getStage());

        if (file != null) {
            try (Workbook workbook = new HSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Rojmed Report");

                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                CellStyle subHeaderStyleCredit = workbook.createCellStyle();
                subHeaderStyleCredit.setAlignment(HorizontalAlignment.CENTER);
                Font subHeaderFontCredit = workbook.createFont();
                subHeaderFontCredit.setBold(true);
                subHeaderFontCredit.setColor(IndexedColors.GREEN.getIndex());
                subHeaderStyleCredit.setFont(subHeaderFontCredit);

                CellStyle subHeaderStyleDebit = workbook.createCellStyle();
                subHeaderStyleDebit.setAlignment(HorizontalAlignment.CENTER);
                Font subHeaderFontDebit = workbook.createFont();
                subHeaderFontDebit.setBold(true);
                subHeaderFontDebit.setColor(IndexedColors.RED.getIndex());
                subHeaderStyleDebit.setFont(subHeaderFontDebit);

                Row headerRow = sheet.createRow(0);
                Cell mainHeaderCell = headerRow.createCell(0);
                mainHeaderCell.setCellValue(lblRojmelHeader.getText());
                mainHeaderCell.setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

                int rowNum = 2;

                Row tableTitlesRow = sheet.createRow(rowNum++);
                Cell creditTitleCell = tableTitlesRow.createCell(0);
                creditTitleCell.setCellValue("Credit");
                creditTitleCell.setCellStyle(subHeaderStyleCredit);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));

                Cell debitTitleCell = tableTitlesRow.createCell(4);
                debitTitleCell.setCellValue("Debit");
                debitTitleCell.setCellStyle(subHeaderStyleDebit);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 4, 6));

                Row columnHeadersRow = sheet.createRow(rowNum++);
                ObservableList<TableColumn<TableRowModel, ?>> creditColumns = tableData.getColumns();
                for (int i = 0; i < creditColumns.size(); i++) {
                    sheet.autoSizeColumn(i);
                    columnHeadersRow.createCell(i).setCellValue(creditColumns.get(i).getText());
                }

                ObservableList<TableColumn<TableRowModel, ?>> debitColumns = tableData1.getColumns();
                for (int i = 0; i < debitColumns.size(); i++) {
                    sheet.autoSizeColumn(i);
                    columnHeadersRow.createCell(i + 4).setCellValue(debitColumns.get(i).getText());
                }

                ObservableList<TableRowModel> creditItems = tableData.getItems();
                ObservableList<TableRowModel> debitItems = tableData1.getItems();
                int maxRows = Math.max(creditItems.size(), debitItems.size());

                for (int i = 0; i < maxRows; i++) {
                    Row dataRow = sheet.createRow(rowNum++);

                    if (i < creditItems.size()) {
                        TableRowModel creditItem = creditItems.get(i);
                        for (int j = 0; j < creditColumns.size(); j++) {
                            String cellValue = (String) creditColumns.get(j).getCellObservableValue(creditItem).getValue();
                            dataRow.createCell(j).setCellValue(cellValue);
                        }
                    }

                    if (i < debitItems.size()) {
                        TableRowModel debitItem = debitItems.get(i);
                        for (int j = 0; j < debitColumns.size(); j++) {
                            String cellValue = (String) debitColumns.get(j).getCellObservableValue(debitItem).getValue();
                            dataRow.createCell(j + 4).setCellValue(cellValue);
                        }
                    }
                }

                rowNum++;
                Row totalRow1 = sheet.createRow(rowNum++);
                totalRow1.createCell(0).setCellValue("Total Credit");
                totalRow1.createCell(1).setCellValue(lblTotalCredit.getText());
                totalRow1.createCell(4).setCellValue("Total Debit");
                totalRow1.createCell(5).setCellValue(lblTotalDebit.getText());

                Row totalRow2 = sheet.createRow(rowNum++);
                totalRow2.createCell(0).setCellValue("Total");
                totalRow2.createCell(1).setCellValue(lblTotalCredit2.getText());
                totalRow2.createCell(4).setCellValue("Total");
                totalRow2.createCell(5).setCellValue(lblTotalDebit2.getText());

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    MyAlert alert = new InformationAlert(MainApp.getStage(), "Export Success", "Excel file has been generated successfully.");
                    alert.createAlert();
                }

            } catch (IOException e) {
                e.printStackTrace();
                MyAlert alert = new ErrorAlert(MainApp.getStage(), "Export Error", "An error occurred while generating the Excel file.");
                alert.createAlert();
            }
        }
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
                    if (voucherTransactionList == null)
                        voucherTransactionList = new ArrayList<>();

                    voucherTransactionMap = voucherTransactionList.stream()
                            .collect(Collectors.toMap(
                                    vt -> vt.getCode(),
                                    vt -> vt
                            ));
                    voucherTransactionList = bifurcateProductReceiptTransaction(voucherTransactionList);
                    voucherTransactionList = bifurcateProductSaleTransaction(voucherTransactionList);

                    crVoucherTransactionList = voucherTransactionList.stream()
                            .filter(vt -> vt.getCreditDebit() == true)
                            .collect(Collectors.toList());

                    drVoucherTransactionList = voucherTransactionList.stream()
                            .filter(vt -> vt.getCreditDebit() == false)
                            .collect(Collectors.toList());

                    calculateCrDrTotal();
                    generateTable();

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void calculateCrDrTotal() {
        crTotal = BigDecimal.ZERO;
        drTotal = BigDecimal.ZERO;
        if (openingBalance.compareTo(BigDecimal.ZERO) < 0)
            crTotal = crTotal.add(new BigDecimal(Math.abs(openingBalance.doubleValue())).setScale(2, RoundingMode.HALF_DOWN));
        else
            drTotal = drTotal.add(new BigDecimal(Math.abs(openingBalance.doubleValue())).setScale(2, RoundingMode.HALF_DOWN));

        crTotal = crTotal.add(crVoucherTransactionList.stream()
                .filter(vt -> vt.getCode() != null)
                .map(VoucherTransaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        drTotal = drTotal.add(drVoucherTransactionList.stream()
                .filter(vt -> vt.getCode() != null)
                .map(VoucherTransaction::getAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

        lblTotalCredit.setText(crTotal.toString());
        lblTotalDebit.setText(drTotal.toString());
        calculateClosingBalance();
    }

    private void calculateClosingBalance() {
        closingBalance = crTotal.subtract(drTotal);

        if (closingBalance.compareTo(BigDecimal.ZERO) < 0) {
            lblTotalCredit2.setText(closingBalance.add(crTotal).toString());
            lblTotalDebit2.setText(closingBalance.add(drTotal).toString());
        } else {
            lblTotalCredit2.setText(closingBalance.add(drTotal).toString());
            lblTotalDebit2.setText(closingBalance.add(drTotal).toString());
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
            List<String> columnNames = Arrays.asList(resourceBundle.getString("ledger"), resourceBundle.getString("sub.amount"), resourceBundle.getString("amount"));

            for (String name : columnNames) {
                // Create Column for Table 1
                TableColumn<TableRowModel, String> col1 = new TableColumn<>(name);
                if (name.contains(resourceBundle.getString("sub.amount")) || name.contains(resourceBundle.getString("amount"))) {
                    col1.getStyleClass().add("cell-right-aligned");
                    col1.setPrefWidth(130);
                } else {
                    col1.setPrefWidth(tableData.getWidth() - 270);
                }
                col1.setCellValueFactory(data -> data.getValue().columnProperty(name));

                tableData.getColumns().add(col1);

                // Create a completely NEW instance for Table 2
                TableColumn<TableRowModel, String> col2 = new TableColumn<>(name);
                if (name.contains(resourceBundle.getString("sub.amount")) || name.contains(resourceBundle.getString("amount"))) {
                    col2.getStyleClass().add("cell-right-aligned");
                    col2.setPrefWidth(130);
                } else {
                    col2.setPrefWidth(tableData.getWidth() - 270);
                }
                col2.setCellValueFactory(data -> data.getValue().columnProperty(name));
                tableData1.getColumns().add(col2);
            }
            if (voucherTransactionList == null)
                voucherTransactionList = new ArrayList<>();

            // Filter data
            List<VoucherTransaction> creditVoucherTransaction = voucherTransactionList.stream()
                    .filter(vt -> vt.getCreditDebit() == true && vt.getCode() != null)
                    .collect(Collectors.toList());

            List<VoucherTransaction> debitVoucherTransaction = voucherTransactionList.stream()
                    .filter(vt -> vt.getCreditDebit() == false && vt.getCode() != null)
                    .collect(Collectors.toList());

            // Assign items - these will now remain independent
            tableData.setItems(generateData(creditVoucherTransaction, true));
            tableData1.setItems(generateData(debitVoucherTransaction, false));


            tableData.setRowFactory(tv -> {
                TableRow<TableRowModel> row = new TableRow<TableRowModel>() {
                    @Override
                    protected void updateItem(TableRowModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item instanceof SummaryRow) {
                            setStyle("-fx-background-color: #9dc5d1; -fx-font-weight: bold;");
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
                            setStyle("-fx-background-color: #9dc5d1; -fx-font-weight: bold;");
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


    private ObservableList<TableRowModel> generateData(List<VoucherTransaction> voucherTransactionList, boolean credit_debit) {
        ObservableList<TableRowModel> finaldata = FXCollections.observableArrayList();

        Map<Ledger, List<VoucherTransaction>> ledgerTransactionMap = new HashMap<>();

        for (VoucherTransaction voucherTransaction : voucherTransactionList) {
            List<VoucherTransaction> tempTxnList = ledgerTransactionMap.get(voucherTransaction.getLedger());

            if (tempTxnList == null || tempTxnList.isEmpty())
                tempTxnList = new ArrayList<>();

            tempTxnList.add(voucherTransaction);
            ledgerTransactionMap.put(voucherTransaction.getLedger(), tempTxnList);
        }

        ObservableList<TableRowModel> data = FXCollections.observableArrayList();
        for (Ledger ledger : ledgerTransactionMap.keySet()) {
            SummaryRow sum1 = new SummaryRow();
            sum1.setColumnValue(resourceBundle.getString("ledger"), ledger.toString());
            BigDecimal totalAmt = BigDecimal.ZERO;

            List<DataEntryRow> dataEntryRowList = new ArrayList<>();
            for (VoucherTransaction voucherTransaction : ledgerTransactionMap.get(ledger)) {
                totalAmt = totalAmt.add(voucherTransaction.getAmount());
                DataEntryRow row1 = new DataEntryRow();
                row1.setColumnValue(resourceBundle.getString("ledger"), " - " + voucherTransaction.getNarration());
                row1.setColumnValue(resourceBundle.getString("sub.amount"), voucherTransaction.getAmount().toString());
                row1.setId(voucherTransaction.getCode());
                dataEntryRowList.add(row1);
            }
            sum1.setColumnValue(resourceBundle.getString("amount"), totalAmt.toString());
            data.add(sum1);
            data.addAll(dataEntryRowList);
        }
        if (credit_debit) {
            if (openingBalance.compareTo(BigDecimal.ZERO) <= 0) {
                SummaryRow sum1 = new SummaryRow();
                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("opening.balance"));
                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(openingBalance.doubleValue())));
                finaldata.add(sum1);
            }
            finaldata.addAll(data);
            if (closingBalance.compareTo(BigDecimal.ZERO) < 0) {
                SummaryRow sum1 = new SummaryRow();
                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("closing.balance"));
                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(closingBalance.doubleValue())));
                finaldata.add(sum1);
            }
        } else {
            if (openingBalance.compareTo(BigDecimal.ZERO) > 0) {
                SummaryRow sum1 = new SummaryRow();
                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("opening.balance"));
                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(openingBalance.doubleValue())));
                finaldata.add(sum1);
            }
            finaldata.addAll(data);
            if (closingBalance.compareTo(BigDecimal.ZERO) >= 0) {
                SummaryRow sum1 = new SummaryRow();
                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("closing.balance"));
                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(closingBalance.doubleValue())));
                finaldata.add(sum1);
            }
        }

//        if (credit_debit) {
//            if (openingBalance.compareTo(BigDecimal.ZERO) <= 0) {
//                SummaryRow sum1 = new SummaryRow();
//                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("opening.balance"));
//                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(openingBalance.doubleValue())));
//                finaldata.add(sum1);
//                finaldata.addAll(data);
//            }
//            if (closingBalance.compareTo(BigDecimal.ZERO) < 0) {
//                SummaryRow sum1 = new SummaryRow();
//                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("closing.balance"));
//                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(closingBalance.doubleValue())));
//                if (!(openingBalance.compareTo(BigDecimal.ZERO) <= 0))
//                    finaldata.addAll(data);
//                finaldata.add(sum1);
//            }
//        } else {
//            if (openingBalance.compareTo(BigDecimal.ZERO) > 0) {
//                SummaryRow sum1 = new SummaryRow();
//                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("opening.balance"));
//                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(openingBalance.doubleValue())));
//                finaldata.add(sum1);
//                finaldata.addAll(data);
//            }
//            if (closingBalance.compareTo(BigDecimal.ZERO) >= 0) {
//                SummaryRow sum1 = new SummaryRow();
//                sum1.setColumnValue(resourceBundle.getString("ledger"), resourceBundle.getString("closing.balance"));
//                sum1.setColumnValue(resourceBundle.getString("amount"), String.valueOf(Math.abs(closingBalance.doubleValue())));
//                if (!(openingBalance.compareTo(BigDecimal.ZERO) > 0))
//                    finaldata.addAll(data);
//                finaldata.add(sum1);
//            }
//        }

        return finaldata;
    }

    private void handleCreditDoubleClick(String id) {
        if (id == null || id.contains("temp"))
            return;

        VoucherTransaction vt = voucherTransactionMap.get(id);
        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryCredit", vt.getVoucher(), this, resourceBundle.getString("credit.entry"));
    }

    private void handleDebitDoubleClick(String id) {
        if (id == null)
            return;

        VoucherTransaction vt = voucherTransactionMap.get(id);
        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherEntryDebit", vt.getVoucher(), this, resourceBundle.getString("debit.entry"));
    }

    private void getOpeningLedgerBalance() {
        FinancialYearRepository financialYearRepository = EmcsAppContext.getContext().getBean(FinancialYearRepository.class);

        FinancialYear financialYear = financialYearRepository.findCurrentFinancialYear(dpDate.getValue()).orElse(null);
        if (financialYear == null)
            return;

        LocalDate fromDate = financialYear.getStartDate();
        var task = new RojmedOpeningBalanceLoadTask(fromDate, dpDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                openingBalance = task.get();
                log.info(openingBalance.toString());
                loadVoucherTransactionByDate(dpDate.getValue(), dpDate.getValue());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private List<VoucherTransaction> bifurcateProductReceiptTransaction(List<VoucherTransaction> voucherTransactionList) {
        try {
            List<VoucherTransaction> updatedVoucherTxn = new ArrayList<>();
            List<VoucherTransaction> productReceiptTransaction = new ArrayList<>();

            productReceiptTransaction = voucherTransactionList.stream()
                    .filter(vt -> vt.getVoucher().getProcessName() != null &&
                            vt.getVoucher().getProcessName().contains("product_receipt")
                            && vt.getCreditDebit() == false)
                    .collect(Collectors.toList());

            List<String> productReceiptCodes = productReceiptTransaction.stream()
                    .map(vt -> vt.getVoucher().getProcessReference())
                    .collect(Collectors.toList());

            ProductReceiptRepository productReceiptRepository = EmcsAppContext.getContext().getBean(ProductReceiptRepository.class);
            ProductReceiptTransactionRepository productReceiptTransactionRepository = EmcsAppContext.getContext().getBean(ProductReceiptTransactionRepository.class);
            List<ProductReceipt> productReceipts = productReceiptRepository.findAllById(productReceiptCodes);
            List<ProductReceiptTransaction> productReceiptTransactions = productReceiptTransactionRepository.findByProductReceiptIn(productReceipts);
            Set<String> ledgerSet = productReceiptTransactions.stream()
                    .map(prt -> prt.getProduct().getPurchaseLedger().getCode())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            productReceiptTransaction = voucherTransactionList.stream()
                    .filter(vt -> ledgerSet.contains(vt.getLedger().getCode()))
                    .collect(Collectors.toList());

            Map<Ledger, Map<Product, List<ProductReceiptTransaction>>> ledgerProductTxnMap =
                    productReceiptTransactions.stream()
                            .filter(prt -> prt.getProduct() != null && prt.getProduct().getPurchaseLedger() != null)
//                            .peek(prt -> {
//                                Ledger unproxiedLedger = Hibernate.unproxy(prt.getProduct().getPurchaseLedger(), Ledger.class);
//                                prt.getProduct().setPurchaseLedger(unproxiedLedger);
//                            })
                            .collect(Collectors.groupingBy(
                                    prt -> prt.getProduct().getPurchaseLedger(),
                                    Collectors.groupingBy(ProductReceiptTransaction::getProduct)
                            ));

            int tempCode = 0;
            for (Ledger ledger : ledgerProductTxnMap.keySet()) {
                Map<Product, List<ProductReceiptTransaction>> map = ledgerProductTxnMap.get(ledger);
                for (Product product : map.keySet()) {
                    List<ProductReceiptTransaction> productReceiptTransactions1 = map.get(product);
                    BigDecimal amount = BigDecimal.ZERO;
                    Integer qty = 0;
                    for (ProductReceiptTransaction productReceiptTransaction1 : productReceiptTransactions1) {
                        amount = amount.add(productReceiptTransaction1.getAmount());
                        qty += productReceiptTransaction1.getQuantity();
                    }
                    tempCode++;
                    VoucherTransaction voucherTransaction = new VoucherTransaction();
                    voucherTransaction.setCode("temp" + tempCode);
                    voucherTransaction.setLedger(ledger);
                    voucherTransaction.setAmount(amount);
                    voucherTransaction.setNarration(product.toString() + " - " + qty + " x " + amount.divide(new BigDecimal(qty), 2, RoundingMode.HALF_DOWN));
                    voucherTransaction.setCreditDebit(false);
                    updatedVoucherTxn.add(voucherTransaction);
                }
            }
            voucherTransactionList.removeAll(productReceiptTransaction);
            updatedVoucherTxn.addAll(voucherTransactionList);
            return updatedVoucherTxn;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<VoucherTransaction> bifurcateProductSaleTransaction(List<VoucherTransaction> voucherTransactionList) {
        try {
            List<VoucherTransaction> updatedVoucherTxn = new ArrayList<>();
            List<VoucherTransaction> productSaleTransaction = new ArrayList<>();

            productSaleTransaction = voucherTransactionList.stream()
                    .filter(vt -> vt.getVoucher() != null && vt.getVoucher().getProcessName() != null &&
                            vt.getVoucher().getProcessName().contains("tbl_product_sale")
                            && vt.getCreditDebit() == true)
                    .collect(Collectors.toList());

            Set<String> voucherNo = productSaleTransaction.stream()
                    .map(prt -> prt.getVoucher().getCode())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (voucherNo == null || voucherNo.isEmpty())
                return voucherTransactionList;

            ProductSaleRepository productSaleRepository = EmcsAppContext.getContext().getBean(ProductSaleRepository.class);
            ProductSaleTransactionRepository productSaleTransactionRepository = EmcsAppContext.getContext().getBean(ProductSaleTransactionRepository.class);
            List<ProductSale> productSales = productSaleRepository.findByVoucherNoIn((new ArrayList<>(voucherNo)));

            List<ProductSaleTransaction> productSaleTransactions = productSaleTransactionRepository.findByProductSaleIn(productSales);
            Set<String> ledgerSet = productSaleTransactions.stream()
                    .map(prt -> prt.getProduct().getSaleLedger().getCode())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            productSaleTransaction = voucherTransactionList.stream()
                    .filter(vt -> ledgerSet.contains(vt.getLedger().getCode()))
                    .collect(Collectors.toList());

            Map<Ledger, Map<Product, List<ProductSaleTransaction>>> ledgerProductTxnMap =
                    productSaleTransactions.stream()
                            .filter(prt -> prt.getProduct() != null && prt.getProduct().getSaleLedger() != null)
                            .collect(Collectors.groupingBy(
                                    prt -> prt.getProduct().getSaleLedger(),
                                    Collectors.groupingBy(ProductSaleTransaction::getProduct)
                            ));

            int tempCode = 0;
            for (Ledger ledger : ledgerProductTxnMap.keySet()) {
                Map<Product, List<ProductSaleTransaction>> map = ledgerProductTxnMap.get(ledger);
                for (Product product : map.keySet()) {
                    List<ProductSaleTransaction> productReceiptTransactions1 = map.get(product);
                    BigDecimal amount = BigDecimal.ZERO;
                    BigDecimal qty = BigDecimal.ZERO;
                    for (ProductSaleTransaction productReceiptTransaction1 : productReceiptTransactions1) {
                        amount = amount.add(productReceiptTransaction1.getAmount());
                        qty = qty.add(productReceiptTransaction1.getQuantity());
                    }
                    tempCode++;
                    VoucherTransaction voucherTransaction = new VoucherTransaction();
                    voucherTransaction.setCode("tempsale" + tempCode);
                    voucherTransaction.setLedger(ledger);
                    voucherTransaction.setAmount(amount);
                    voucherTransaction.setNarration(product.toString() + " - " + qty + " x " + amount.divide(qty, 2, RoundingMode.HALF_DOWN));
                    voucherTransaction.setCreditDebit(true);
                    updatedVoucherTxn.add(voucherTransaction);
                }
            }
            voucherTransactionList.removeAll(productSaleTransaction);
            updatedVoucherTxn.addAll(voucherTransactionList);
            return updatedVoucherTxn;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
