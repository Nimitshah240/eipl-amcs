package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import com.eipl.amcs.master.account.task.RojmedOpeningBalanceLoadTask;
import com.eipl.amcs.master.account.task.VoucherTransactionByDateLoadTask;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptRepository;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptTransactionRepository;
import com.eipl.amcs.operation.inventory.repository.ProductSaleRepository;
import com.eipl.amcs.operation.inventory.repository.ProductSaleTransactionRepository;
import com.eipl.amcs.report.dto.*;
import com.eipl.amcs.report.task.*;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.eipl.amcs.utils.AppConstant.Formatter3;

public class RptStockValuationController implements MyInitialization {
    @FXML
    private E_Button btnClose;

    @FXML
    private E_Button btnRojmed, btnGenerate, btnTrialBalance, btnTredingReport, btnProfitLoss, btnBalanceSheet, btnGenerate1;
    @FXML
    private Label lblAsOnDate;

    @FXML
    private E_DatePicker dpAsOnDate, dpFromDate, dpToDate, dpFromDate1, dpToDate1;
    @FXML
    private E_DatePicker dpStockValuation;
    @FXML
    private SwingNode reportNode;
    @FXML
    private AutoSearchTextField<Product> cboxProduct;
    @FXML
    private RadioButton rbtVertical, rbtHorizontal;
    @FXML
    private AutoSearchTextField<String> cboxFormat, cboxLanguage, cboxLanguage1;
    private List<Product> listProductList;
    private List<LedgerBalance> listBSLiability, listBSAsset;

    private List<LedgerBalance> listPLExpense, listPLIncome;
    @FXML
    private AnchorPane root;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        cboxFormat.getItems().addAll("Format 1", "Format 2");
        cboxFormat.getSelectionModel().select(0);
        dpStockValuation.setValue(LocalDate.now());
        dpStockValuation.setConverter(new LocalDateConvertor());
        dpAsOnDate.setValue(LocalDate.now());
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        loadProduct();
        setupComboBox();
        cboxFormat.setOnAction(e -> {
            if (cboxFormat.getSelectionModel().getSelectedIndex() == 0) {
                dpToDate.setDisable(true);
                lblAsOnDate.setText(resources.getString("asondate"));
            } else {
                dpToDate.setDisable(false);
                lblAsOnDate.setText(resources.getString("fromdate"));
            }
        });
        btnGenerate1.setOnAction(e -> {
            loadProductStockValuation();
        });
        FocusUtils.requestFocus(dpFromDate1);
        btnGenerate.setOnAction(e -> {
            validateAndGenerate();
        });
        btnRojmed.setOnAction(e -> {
            loadDataRojmed();
        });
        btnProfitLoss.setOnAction(e -> {
            loadDataProfitLoss();
        });
        btnBalanceSheet.setOnAction(e -> {
            loadDataBalanceSheet();
            loadDataBalanceSheetGrouping();
        });
        btnTredingReport.setOnAction(e -> {
            loadDataTreadingReport();
        });
        btnTrialBalance.setOnAction(e -> {
            loadDataTrialBalance();
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage.setValue("Hindi");
        } else {
            cboxLanguage.setValue("English");
        }
        String[] arr1 = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr1)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage1.setValue("English");
        }

        ToggleGroup group = new ToggleGroup();
        rbtHorizontal.setToggleGroup(group);
        rbtVertical.setToggleGroup(group);
        rbtHorizontal.setSelected(true);
    }

    public void loadProductStockValuation() {
        var task = new StockValuationTask(MainApp.identityDto.getSociety().getCode(), dpStockValuation.getValue(), MainApp.getLocale());
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("stockvaluation.title"),
                    resourceBundle.getString("alert.insert.success"));
            alert.createAlert();
        });
        task.setOnFailed(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("stockvaluation.title.fail"),
                    resourceBundle.getString("errorlog"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    private void validateAndGenerate() {
        switch (cboxFormat.getSelectionModel().getSelectedIndex()) {
            case 0:
                loadData();
                break;
            case 1:
                loadData1();
                break;
        }
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    @Override
    public void loadData() {
        String localeStr = getLocaleString();
        StockValuationTaskWithProduct task = new StockValuationTaskWithProduct(MainApp.identityDto.getSociety().getCode(), dpFromDate.getValue(), localeStr, cboxProduct.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                List<ProductStockValuation> list = task.get();
                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (localeStr.equals("en")) {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                params.put("p_as_on_date", dpFromDate.getValue());
                params.put("p_locale", localeStr);
                params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
                params.put("p_product_code", cboxProduct.getValue().getCode());
                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_STOCK_VALUATION, params, new JRBeanCollectionDataSource(list));
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadProduct() {
        var task1 = new ProductLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Product> list = task1.get();
                if (list != null && !list.isEmpty()) {
                    listProductList = new ArrayList<>();
                    listProductList.add(new Product("0", "ALL", "બધા"));
                    listProductList.addAll(1, list);
                    cboxProduct.setItems(FXCollections.observableList(listProductList));
                    cboxProduct.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

    public void loadData1() {
        String localeStr = getLocaleString();
        StockValuationTaskWithSaleAndPurchase task = new StockValuationTaskWithSaleAndPurchase(MainApp.identityDto.getSociety().getCode(), dpFromDate.getValue(), dpToDate.getValue(), localeStr, cboxProduct.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                List<ProductStockValuationWithSaleAndPurchase> list = task.get();
                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (localeStr.equals("en")) {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                params.put("p_from_date", dpFromDate.getValue().toString());
                params.put("p_to_date", dpToDate.getValue().toString());
                params.put("p_locale", localeStr);
                params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
                params.put("p_product_code", cboxProduct.getValue().getCode());
                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_STOCK_VALUATION_WITH_SALE_PURCHASE, params, new JRBeanCollectionDataSource(list));
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadDataProfitLoss() {
        String localeStr = getLocaleString();
        ProfitLossTask profitLossTask = new ProfitLossTask(MainApp.identityDto.getSociety().getCode(),
                dpFromDate1.getValue(), dpToDate1.getValue(), localeStr);
        profitLossTask.setOnSucceeded(ee -> {
            try {
                List<LedgerBalance> list = profitLossTask.get();
                if (list == null) {
                    list = Collections.emptyList();
                }
                Map<String, Object> param = new HashMap<>();
                param.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (localeStr.equals("en")) {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                param.put("p_from_date", dpFromDate1.getValue());
                param.put("p_to_date", dpToDate1.getValue());
                param.put("p_locale", localeStr);
                param.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
                JasperPrint print = null;
                listPLIncome = list.stream().filter(p -> p != null && p.getIncomeExpense() == 1).collect(Collectors.toList());
                listPLExpense = list.stream().filter(p -> p != null && p.getIncomeExpense() == 0).collect(Collectors.toList());
                double incomeTotal = listPLIncome != null
                        ? listPLIncome.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum() : 0;
                double expenseTotal = listPLExpense != null
                        ? Math.abs(listPLExpense.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum()) : 0;
                param.put("p_balance", incomeTotal - expenseTotal);

                if (rbtHorizontal.isSelected()) {
                    param.put("p_imcome_side", listPLIncome);
                    param.put("p_expense_side", listPLExpense);

                    listPLIncome = list.stream()
                            .filter(p -> p != null && p.getIncomeExpense() == 1)
                            .collect(Collectors.toList());

                    listPLExpense = list.stream()
                            .filter(p -> p != null && p.getIncomeExpense() == 0)
                            .collect(Collectors.toList());

                    int maxRows = Math.max(
                            listPLIncome != null ? listPLIncome.size() : 0,
                            listPLExpense != null ? listPLExpense.size() : 0
                    );

                    List<BalanceSheetRow> listPLRows = new ArrayList<>();
                    for (int i = 0; i < maxRows; i++) {
                        LedgerBalance income = (listPLIncome != null && i < listPLIncome.size())
                                ? listPLIncome.get(i) : null;
                        LedgerBalance expense = (listPLExpense != null && i < listPLExpense.size())
                                ? listPLExpense.get(i) : null;
                        listPLRows.add(new BalanceSheetRow(income, expense));
                    }

                    param.put("p_pl_rows", listPLRows);

                    print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_PROFIT_LOSS_REPORT, param, new JRBeanCollectionDataSource(listPLRows));
                } else {
                    // (NIMIT | 15.03.2026): This code is used for vertical report design.
                    list.stream().forEach(item -> {
                        double balance = item.getBalance();
                        if (balance < 0) {
                            item.setDebit(Math.abs(balance));
                        } else {
                            item.setCredit(balance);
                        }
                    });
                    // TODO(ANANT | 15.03.2026): HERE CHANGE YOUR REPORT PATH AND IN REPORT ADD THE COLUMN NAME SAME AS BALANCESHEETROW COLUMN NAME.
                    print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_PROFIT_LOSS_REPORT, param, new JRBeanCollectionDataSource(list));
                }
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(profitLossTask).start();
    }

    public void loadDataTreadingReport() {
        String localeStr = getLocaleString();
        LoadStockValuationTask task = new LoadStockValuationTask(MainApp.identityDto.getSociety().getCode(), MainApp.getFinancialYear().getEndDate(), localeStr);
        task.setOnSucceeded(e -> {
            try {
                List<com.eipl.amcs.master.account.model.ProductStockValuation> listStockValuation = task.get();
                List<ProductStockValuation> list = new ArrayList<>();
                if (listStockValuation != null) {
                    for (com.eipl.amcs.master.account.model.ProductStockValuation psv : listStockValuation) {
                        list.add(new com.eipl.amcs.report.dto.ProductStockValuation(psv.getProductCode(), psv.getProductName(), psv.getStock(), psv.getValuation(), psv.getUnit()));
                    }
                }

                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCodeEx());
                params.put("p_as_on_date", MainApp.getFinancialYear().getEndDate());
                params.put("p_locale", localeStr);
                params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));

                double stockValuationTask = list.stream().mapToDouble(m -> m.getValuation()).sum();

                TradingTask tradingTask = new TradingTask(MainApp.identityDto.getSociety().getCode(), dpFromDate1.getValue(), dpToDate1.getValue(), localeStr);
                tradingTask.setOnSucceeded(ee -> {
                    try {
                        List<LedgerBalance> listtradingTask = tradingTask.get();
                        Map<String, Object> TradingTaskparams = new HashMap<>();
                        TradingTaskparams.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                        if (localeStr.equals("en")) {
                            TradingTaskparams.put("p_society_name", MainApp.identityDto.getSociety().getName());
                        } else {
                            TradingTaskparams.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                        }
                        TradingTaskparams.put("p_from_date", dpFromDate1.getValue());
                        TradingTaskparams.put("p_to_date", dpToDate1.getValue());


                        TradingTaskparams.put("p_locale", localeStr);
                        TradingTaskparams.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
                        JasperPrint print = null;
                        if (rbtVertical.isSelected()) {
                            print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_TRADINGREPORT, TradingTaskparams, new JRBeanCollectionDataSource(listtradingTask));
                        } else {
                            // (NIMIT | 15.03.2026): This code is used for horizontal report design.

                            listBSAsset = listtradingTask.stream().filter(p -> p.getBalance() < 0).collect(Collectors.toList());
                            listBSLiability = listtradingTask.stream().filter(p -> p.getBalance() > 0).collect(Collectors.toList());

                            int maxRows = Math.max(
                                    listBSLiability != null ? listBSLiability.size() : 0,
                                    listBSAsset != null ? listBSAsset.size() : 0
                            );

                            List<BalanceSheetRow> listBSRows = new ArrayList<>();
                            for (int i = 0; i < maxRows; i++) {
                                LedgerBalance liability = (listBSLiability != null && i < listBSLiability.size())
                                        ? listBSLiability.get(i) : null;
                                LedgerBalance asset = (listBSAsset != null && i < listBSAsset.size())
                                        ? listBSAsset.get(i) : null;
                                listBSRows.add(new BalanceSheetRow(liability, asset));
                            }

                            // TODO(ANANT | 15.03.2026): HERE CHANGE YOUR REPORT PATH AND IN REPORT ADD THE COLUMN NAME SAME AS BALANCESHEETROW COLUMN NAME.
                            print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_TRADINGREPORT, TradingTaskparams, new JRBeanCollectionDataSource(listBSRows));
                        }
                        JasperViewer.viewReport(print, false);
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
                new Thread(tradingTask).start();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadDataBalanceSheet() {
        String localeStr = getLocaleString();
        BalanceSheetTask balanceSheetTask = new BalanceSheetTask(MainApp.identityDto.getSociety().getCode(),
                dpFromDate1.getValue(), dpToDate1.getValue(), localeStr);
        balanceSheetTask.setOnSucceeded(ee -> {
            try {
                List<LedgerBalance> list = balanceSheetTask.get();
                if (list == null) {
                    list = new ArrayList<>();
                }
                Map<String, Object> param = new HashMap<>();
                param.put("p_society_code", MainApp.identityDto.getSociety().getCodeEx());
                if (localeStr.equals("en")) {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                param.put("p_from_date", LocalDate.parse(dpFromDate1.getValue().toString()));
                param.put("p_to_date", LocalDate.parse(dpToDate1.getValue().toString()));
                param.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
                param.put("p_locale", localeStr);


                double diff = 0;
                if (listPLExpense != null && listPLIncome != null) {
                    diff = listPLIncome.stream().mapToDouble(m -> m.getBalance()).sum()
                            - Math.abs(listPLExpense.stream().mapToDouble(m -> m.getBalance()).sum());
                } else if (listPLIncome != null) {
                    diff = listPLIncome.stream().mapToDouble(m -> m.getBalance()).sum() - 0;
                } else if (listPLExpense != null) {
                    diff = 0 - Math.abs(listPLExpense.stream().mapToDouble(m -> m.getBalance()).sum());
                }

                listBSLiability = list.stream().filter(p -> p.getIncomeExpense() == 1).collect(Collectors.toList());
                if (listBSLiability != null) {
                    if (diff > 0) {
                        listBSLiability.add(new LedgerBalance("", "PL Ledger", 0, 0, Math.abs(diff), 1));
                    }
                }
                listBSAsset = list.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
                if (listBSAsset != null) {
                    if (diff < 0) {
                        listBSAsset.add(new LedgerBalance("", "PL Ledger", 0, 0, Math.abs(diff), 0));
                    }
                }

                JasperPrint print = null;
                if (rbtHorizontal.isSelected()) {
                    int maxRows = Math.max(
                            listBSLiability != null ? listBSLiability.size() : 0,
                            listBSAsset != null ? listBSAsset.size() : 0
                    );

                    List<BalanceSheetRow> listBSRows = new ArrayList<>();
                    for (int i = 0; i < maxRows; i++) {
                        LedgerBalance liability = (listBSLiability != null && i < listBSLiability.size())
                                ? listBSLiability.get(i) : null;
                        LedgerBalance asset = (listBSAsset != null && i < listBSAsset.size())
                                ? listBSAsset.get(i) : null;
                        listBSRows.add(new BalanceSheetRow(liability, asset));
                    }

                    print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_BALANCESHEET, param, new JRBeanCollectionDataSource(listBSRows));
                } else {
                    // (NIMIT | 15.03.2026): This code is used for vertical report design.
                    list.clear();
                    list.addAll(listBSLiability);
                    list.addAll(listBSAsset);

                    list.sort(Comparator.comparing(
                            LedgerBalance::getLedgerCode,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    ));

                    list.stream().forEach(item -> {
                        double balance = item.getBalance();

                        if (balance < 0) {
                            item.setDebit(Math.abs(balance));
                            item.setCredit(0.0);
                        } else {
                            item.setCredit(balance);
                            item.setDebit(0.0);
                        }
                    });
                    // TODO(ANANT | 15.03.2026): HERE CHANGE YOUR REPORT PATH AND IN REPORT ADD THE COLUMN NAME SAME AS BALANCESHEETROW COLUMN NAME.
                    print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_BALANCESHEET, param, new JRBeanCollectionDataSource(list));
                }
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(balanceSheetTask).start();
    }

    private void loadDataTrialBalance() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", String.valueOf(dpFromDate1.getValue()));
        params.put("p_to_date", String.valueOf(dpToDate1.getValue()));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.LEDGER_SUMMARY, params);
        JasperViewer.viewReport(print, false);
    }

    LocalDate currentDate = LocalDate.now();

    private void loadDataRojmed() {
        LocalDate fromDate = dpFromDate1.getValue();
        LocalDate toDate = dpToDate1.getValue();

        processDate(fromDate, fromDate, toDate);
    }

    private void processDate(LocalDate fromDate,
                             LocalDate processingDate,
                             LocalDate endDate) {

        if (processingDate.isAfter(endDate)) {
            System.out.println("ALL COMPLETED");


            Map<String, Object> params = new HashMap<>();
            String localeStr = cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
            params.put("p_society_name", MainApp.identityDto.getSociety().getName());
            params.put("p_financial_year", MainApp.getFinancialYear().getCode());
            params.put("p_locale", localeStr);
            params.put("p_from_date", dpFromDate1.getValue());
            params.put("p_to_date", dpToDate1.getValue());
            params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
            JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.ROJMED, params, new JRBeanCollectionDataSource(listRojmed));
            JasperViewer.viewReport(print, false);
            return;
        }

        System.out.println("Processing Date : " + processingDate);

        getOpeningLedgerBalance(fromDate, processingDate)
                .thenCompose(v -> loadVoucherTransactionByDate(processingDate, processingDate))
                .thenRun(() -> {
                    System.out.println("Completed Date : " + processingDate);
                    currentDate = processingDate.plusDays(1);
                    processDate(fromDate, processingDate.plusDays(1), endDate);
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    BigDecimal openingBalance = BigDecimal.ZERO;

    private CompletableFuture<Void> getOpeningLedgerBalance(LocalDate fromDate, LocalDate toDate) {

        CompletableFuture<Void> future = new CompletableFuture<>();

        FinancialYearRepository financialYearRepository = EmcsAppContext.getContext().getBean(FinancialYearRepository.class);

        FinancialYear financialYear = financialYearRepository.findCurrentFinancialYear(toDate).orElse(null);

        if (financialYear == null) {
            future.completeExceptionally(new RuntimeException("Financial Year not found"));
            return future;
        }

        var task = new RojmedOpeningBalanceLoadTask(financialYear.getStartDate(), toDate);

        task.setOnSucceeded(e -> {
            try {

                openingBalance = task.getValue();

                System.out.println("Opening Balance " + toDate + " = " +
                        openingBalance);

                future.complete(null);

            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });

        task.setOnFailed(e ->
                future.completeExceptionally(task.getException()));

        new Thread(task).start();

        return future;
    }

    private List<VoucherTransaction> crVoucherTransactionList = new ArrayList<>();
    private List<VoucherTransaction> drVoucherTransactionList = new ArrayList<>();

    private CompletableFuture<Void> loadVoucherTransactionByDate(
            LocalDate fromDate,
            LocalDate toDate) {

        CompletableFuture<Void> future =
                new CompletableFuture<>();

        var task =
                new VoucherTransactionByDateLoadTask(
                        fromDate,
                        toDate);

        task.setOnSucceeded(e -> {

            try {

                List<VoucherTransaction> voucherTransactionList =
                        task.getValue();

                if (voucherTransactionList == null)
                    voucherTransactionList = new ArrayList<>();

                voucherTransactionList =
                        bifurcateProductReceiptTransaction(
                                voucherTransactionList);

                voucherTransactionList =
                        bifurcateProductSaleTransaction(
                                voucherTransactionList);

                crVoucherTransactionList =
                        voucherTransactionList.stream()
                                .filter(VoucherTransaction::getCreditDebit)
                                .collect(Collectors.toList());

                drVoucherTransactionList =
                        voucherTransactionList.stream()
                                .filter(v -> !v.getCreditDebit())
                                .collect(Collectors.toList());

                calculateCrDrTotal();

                creatingRojmedDto(fromDate);

                future.complete(null);

            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });

        task.setOnFailed(e ->
                future.completeExceptionally(task.getException()));

        new Thread(task).start();

        return future;
    }

    private List<VoucherTransaction> bifurcateProductReceiptTransaction
            (List<VoucherTransaction> voucherTransactionList) {
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

    private List<VoucherTransaction> bifurcateProductSaleTransaction
            (List<VoucherTransaction> voucherTransactionList) {
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
        calculateClosingBalance();

        addOpeningAndClosingBalance(true);
        addOpeningAndClosingBalance(false);
    }

    BigDecimal crTotal = BigDecimal.ZERO;
    BigDecimal drTotal = BigDecimal.ZERO;
    BigDecimal closingBalance = BigDecimal.ZERO;

    private void calculateClosingBalance() {
        closingBalance = drTotal.subtract(crTotal);
    }

    private void addOpeningAndClosingBalance(boolean creditDebit) {

        BigDecimal absOpening = openingBalance.abs();
        BigDecimal absClosing = closingBalance.abs();

        if (creditDebit) {

            // OPENING CREDIT
            if (openingBalance.compareTo(BigDecimal.ZERO) < 0) {

                VoucherTransaction vt = new VoucherTransaction();

                Ledger ledger = new Ledger();
                ledger.setName("Opening Balance");
                ledger.setNameLocal(resourceBundle.getString("opening.balance"));

                vt.setLedger(ledger);
                vt.setCode("tempLedgerOpening");
                vt.setNarration(resourceBundle.getString("opening.balance"));
                vt.setAmount(absOpening);

                crVoucherTransactionList.add(0, vt);
            }

            // CLOSING CREDIT
            if (closingBalance.compareTo(BigDecimal.ZERO) > 0) {

                VoucherTransaction vt = new VoucherTransaction();

                Ledger ledger = new Ledger();
                ledger.setName("Closing Balance");
                ledger.setNameLocal(resourceBundle.getString("closing.balance"));

                vt.setLedger(ledger);
                vt.setCode("tempLedgerClosing");
                vt.setNarration(resourceBundle.getString("closing.balance"));
                vt.setAmount(absClosing);

                crVoucherTransactionList.add(vt);
            }
        } else {

            // OPENING DEBIT
            if (openingBalance.compareTo(BigDecimal.ZERO) > 0) {

                VoucherTransaction vt = new VoucherTransaction();

                Ledger ledger = new Ledger();
                ledger.setName("Opening Balance");
                ledger.setNameLocal(resourceBundle.getString("opening.balance"));

                vt.setLedger(ledger);
                vt.setCode("tempLedgerOpening");
                vt.setNarration(resourceBundle.getString("opening.balance"));
                vt.setAmount(absOpening);

                drVoucherTransactionList.add(0, vt);
            }

            // CLOSING DEBIT
            if (closingBalance.compareTo(BigDecimal.ZERO) < 0) {

                VoucherTransaction vt = new VoucherTransaction();

                Ledger ledger = new Ledger();
                ledger.setName("Closing Balance");
                ledger.setNameLocal(resourceBundle.getString("closing.balance"));

                vt.setLedger(ledger);
                vt.setCode("tempLedgerClosing");
                vt.setNarration(resourceBundle.getString("closing.balance"));
                vt.setAmount(absClosing);

                drVoucherTransactionList.add(vt);
            }
        }
    }

    List<RojmedDto> listRojmed = new ArrayList<>();

    private void creatingRojmedDto(LocalDate processingDate) {

        VoucherTransaction openingCr = null;
        VoucherTransaction closingCr = null;
        VoucherTransaction openingDr = null;
        VoucherTransaction closingDr = null;

        List<VoucherTransaction> crTransactions = new ArrayList<>();
        List<VoucherTransaction> drTransactions = new ArrayList<>();

        if (crVoucherTransactionList != null) {
            for (VoucherTransaction vt : crVoucherTransactionList) {

                if ("tempLedgerOpening".equals(vt.getCode())) {
                    openingCr = vt;
                } else if ("tempLedgerClosing".equals(vt.getCode())) {
                    closingCr = vt;
                } else {
                    crTransactions.add(vt);
                }
            }
        }

        if (drVoucherTransactionList != null) {
            for (VoucherTransaction vt : drVoucherTransactionList) {

                if ("tempLedgerOpening".equals(vt.getCode())) {
                    openingDr = vt;
                } else if ("tempLedgerClosing".equals(vt.getCode())) {
                    closingDr = vt;
                } else {
                    drTransactions.add(vt);
                }
            }
        }

        Map<Ledger, List<VoucherTransaction>> crLedgerTransactionMap = new LinkedHashMap<>();
        Map<Ledger, List<VoucherTransaction>> drLedgerTransactionMap = new LinkedHashMap<>();

        for (VoucherTransaction voucherTransaction : crTransactions) {

            List<VoucherTransaction> tempTxnList =
                    crLedgerTransactionMap.get(voucherTransaction.getLedger());

            if (tempTxnList == null) {
                tempTxnList = new ArrayList<>();
            }

            tempTxnList.add(voucherTransaction);
            crLedgerTransactionMap.put(
                    voucherTransaction.getLedger(),
                    tempTxnList);
        }

        for (VoucherTransaction voucherTransaction : drTransactions) {

            List<VoucherTransaction> tempTxnList =
                    drLedgerTransactionMap.get(voucherTransaction.getLedger());

            if (tempTxnList == null) {
                tempTxnList = new ArrayList<>();
            }

            tempTxnList.add(voucherTransaction);
            drLedgerTransactionMap.put(
                    voucherTransaction.getLedger(),
                    tempTxnList);
        }

        List<VoucherTransaction> finalCrList = new ArrayList<>();
        List<VoucherTransaction> finalDrList = new ArrayList<>();

        if (openingCr != null) {
            finalCrList.add(openingCr);
        }

        for (Ledger ledger : crLedgerTransactionMap.keySet()) {

            BigDecimal totalAmt = BigDecimal.ZERO;

            List<VoucherTransaction> tempTransaction =
                    crLedgerTransactionMap.get(ledger);

            for (VoucherTransaction vt : tempTransaction) {
                totalAmt = totalAmt.add(vt.getAmount());
            }

            VoucherTransaction header = new VoucherTransaction();
            header.setCode("tempLedger");
            header.setAmount(totalAmt);
            header.setNarration(ledger.toString());

            finalCrList.add(header);
            finalCrList.addAll(tempTransaction);
            VoucherTransaction emptyTransaction = new VoucherTransaction();
            emptyTransaction.setCode("");
            emptyTransaction.setNarration("");
            finalCrList.add(emptyTransaction);
        }

        if (closingCr != null) {
            finalCrList.add(closingCr);
        }

        if (openingDr != null) {
            finalDrList.add(openingDr);
        }

        for (Ledger ledger : drLedgerTransactionMap.keySet()) {

            BigDecimal totalAmt = BigDecimal.ZERO;

            List<VoucherTransaction> tempTransaction =
                    drLedgerTransactionMap.get(ledger);

            for (VoucherTransaction vt : tempTransaction) {
                totalAmt = totalAmt.add(vt.getAmount());
            }

            VoucherTransaction header = new VoucherTransaction();
            header.setCode("tempLedger");
            header.setAmount(totalAmt);
            header.setNarration(ledger.toString());

            finalDrList.add(header);
            finalDrList.addAll(tempTransaction);
            VoucherTransaction emptyTransaction = new VoucherTransaction();
            emptyTransaction.setCode("");
            emptyTransaction.setNarration("");
            finalDrList.add(emptyTransaction);

        }

        if (closingDr != null) {
            finalDrList.add(closingDr);
        }

        int maxRows = Math.max(
                finalCrList.size(),
                finalDrList.size()
        );


        for (int i = 0; i < maxRows; i++) {

            VoucherTransaction credit = i < finalCrList.size() ? finalCrList.get(i) : null;

            VoucherTransaction debit = i < finalDrList.size() ? finalDrList.get(i) : null;

            RojmedDto rojmedDto = new RojmedDto();

            if (credit != null) {

                if (credit.getCode() == null || credit.getCode().contains("tempLedger")) {
                    rojmedDto.setCreditAmount(credit.getAmount() == null ? null : credit.getAmount().doubleValue());
                } else {
                    rojmedDto.setCreditSubAmount(credit.getAmount() == null ? null : credit.getAmount().doubleValue());
                    rojmedDto.setCreditLedger("   ");
                }

                rojmedDto.setCreditLedger(rojmedDto.getCreditLedger() != null ? rojmedDto.getCreditLedger().concat(credit.getNarration()) : credit.getNarration());
            }

            if (debit != null) {
                if (debit.getCode() == null || debit.getCode().contains("tempLedger")) {
                    rojmedDto.setDebitAmount(debit.getAmount() == null ? null : debit.getAmount().doubleValue());
                } else {
                    rojmedDto.setDebitSubAmount(debit.getAmount() == null ? null : debit.getAmount().doubleValue());
                    rojmedDto.setDebitLedger("   ");
                }

                rojmedDto.setDebitLedger(rojmedDto.getDebitLedger() != null ? rojmedDto.getDebitLedger().concat(debit.getNarration()) : debit.getNarration());
            }

            rojmedDto.setDate(java.sql.Date.valueOf(processingDate));
            rojmedDto.setRojmedDate(processingDate.format(Formatter3));

            listRojmed.add(rojmedDto);
        }
    }


    public void loadDataBalanceSheetGrouping() {
        String localeStr = getLocaleString();
        BalanceSheetTask balanceSheetTask = new BalanceSheetTask(MainApp.identityDto.getSociety().getCode(),
                dpFromDate1.getValue(), dpToDate1.getValue(), localeStr);
        balanceSheetTask.setOnSucceeded(ee -> {
            try {
                List<LedgerBalance> list = balanceSheetTask.get();
                if (list == null) {
                    list = new ArrayList<>();
                }
                Map<String, Object> param = new HashMap<>();
                param.put("p_society_code", MainApp.identityDto.getSociety().getCodeEx());
                if (localeStr.equals("en")) {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                param.put("p_from_date", LocalDate.parse(dpFromDate1.getValue().toString()));
                param.put("p_to_date", LocalDate.parse(dpToDate1.getValue().toString()));
                param.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
                param.put("p_locale", localeStr);

                double diff = 0;
                if (listPLExpense != null && listPLIncome != null) {
                    diff = listPLIncome.stream().mapToDouble(m -> m.getBalance()).sum()
                            - Math.abs(listPLExpense.stream().mapToDouble(m -> m.getBalance()).sum());
                } else if (listPLIncome != null) {
                    diff = listPLIncome.stream().mapToDouble(m -> m.getBalance()).sum() - 0;
                } else if (listPLExpense != null) {
                    diff = 0 - Math.abs(listPLExpense.stream().mapToDouble(m -> m.getBalance()).sum());
                }

                listBSLiability = list.stream().filter(p -> p.getIncomeExpense() == 1).collect(Collectors.toList());
                if (listBSLiability != null) {
                    if (diff > 0) {
                        listBSLiability.add(new LedgerBalance("", "PL Ledger", 0, 0, Math.abs(diff), 1));
                    }
                }
                listBSAsset = list.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
                if (listBSAsset != null) {
                    if (diff < 0) {
                        listBSAsset.add(new LedgerBalance("", "PL Ledger", 0, 0, Math.abs(diff), 0));
                    }
                }
                Map<String, List<LedgerBalance>> liabilityGroupMap = listBSLiability.stream()
                        .collect(Collectors.groupingBy(
                                l -> l.getLedgerGroupCode() + " - " + l.getLedgerGroupName()
                        ));
                Map<String, List<LedgerBalance>> assetGroupMap = listBSAsset.stream()
                        .collect(Collectors.groupingBy(
                                l -> l.getLedgerGroupCode() + " - " + l.getLedgerGroupName()
                        ));
                List<RojmedDto> rojmedDtoList = generateSideBySideRojmed(assetGroupMap, liabilityGroupMap);
                for (RojmedDto rojmedDto : rojmedDtoList) {
                    System.out.println(rojmedDto.getCreditLedger() + " - " + rojmedDto.getCreditSubAmount() + " - " + rojmedDto.getCreditAmount() + " - " + rojmedDto.getDebitLedger() + " - " + rojmedDto.getDebitSubAmount() + " - " + rojmedDto.getDebitAmount());
                }

                // TODO - ANANT HERE MAKE CHANGES
                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_BALANCESHEET, param, new JRBeanCollectionDataSource(rojmedDtoList));
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(balanceSheetTask).start();
    }


    public List<RojmedDto> generateSideBySideRojmed(Map<String, List<LedgerBalance>> assetGroupMap, Map<String, List<LedgerBalance>> liabilityGroupMap) {
        List<RojmedDto> reportRows = new ArrayList<>();
        List<RojmedDto> assetRows = flattenToSideRows(assetGroupMap, true);
        List<RojmedDto> liabilityRows = flattenToSideRows(liabilityGroupMap, false);

        int maxRows = Math.max(assetRows.size(), liabilityRows.size());

        for (int i = 0; i < maxRows; i++) {
            RojmedDto combinedRow = new RojmedDto();

            if (i < assetRows.size()) {
                RojmedDto assetSource = assetRows.get(i);
                combinedRow.setDebitLedger(assetSource.getDebitLedger());
                combinedRow.setDebitAmount(assetSource.getDebitAmount());
                combinedRow.setDebitSubAmount(assetSource.getDebitSubAmount());
            }

            if (i < liabilityRows.size()) {
                RojmedDto liabilitySource = liabilityRows.get(i);
                combinedRow.setCreditLedger(liabilitySource.getCreditLedger());
                combinedRow.setCreditAmount(liabilitySource.getCreditAmount());
                combinedRow.setCreditSubAmount(liabilitySource.getCreditSubAmount());
            }

            reportRows.add(combinedRow);
        }

        return reportRows;
    }

    private List<RojmedDto> flattenToSideRows(Map<String, List<LedgerBalance>> groupMap, boolean isAsset) {
        List<RojmedDto> sideRows = new ArrayList<>();

        groupMap.forEach((groupName, balances) -> {
            RojmedDto header = new RojmedDto();
            double totalBalance = balances.stream().mapToDouble(LedgerBalance::getBalance).sum();

            if (isAsset) {
                header.setDebitLedger(groupName);
                header.setDebitAmount(totalBalance);
            } else {
                header.setCreditLedger(groupName);
                header.setCreditAmount(totalBalance);
            }
            sideRows.add(header);

            for (LedgerBalance ledger : balances) {
                RojmedDto child = new RojmedDto();
                String ledgerDisplay = ledger.getLedgerName() + " (" + ledger.getLedgerCode() + ")";

                if (isAsset) {
                    child.setDebitLedger(ledgerDisplay);
                    child.setDebitSubAmount(ledger.getBalance());
                } else {
                    child.setCreditLedger(ledgerDisplay);
                    child.setCreditSubAmount(ledger.getBalance());
                }
                sideRows.add(child);
            }
        });

        return sideRows;
    }
}

