package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductCellFactory;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductLocalCellFactory;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.report.dto.BalanceSheetRow;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.dto.ProductStockValuationWithSaleAndPurchase;
import com.eipl.amcs.report.task.*;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class RptStockValuationController implements MyInitialization {
    @FXML
    private Button btnClose;

    @FXML
    private Button btnGenerate, btnTrialBalance, btnTredingReport, btnProfitLoss, btnBalanceSheet, btnGenerate1;
    @FXML
    private Label lblAsOnDate;

    @FXML
    private DatePicker dpAsOnDate, dpFromDate, dpToDate, dpFromDate1, dpToDate1;
    @FXML
    private E_DatePicker dpStockValuation;
    @FXML
    private SwingNode reportNode;
    @FXML
    private ComboBox<Product> cboxProduct;
    @FXML
    private ComboBox<String> cboxFormat, cboxLanguage, cboxLanguage1;
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
        FocusUtils.requestFocus(btnGenerate);
        btnGenerate.setOnAction(e -> {
            validateAndGenerate();
        });
        btnProfitLoss.setOnAction(e -> {
            loadDataProfitLoss();
        });
        btnBalanceSheet.setOnAction(e -> {
            loadDataBalanceSheet();
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

    @Override
    public void setupComboBox() {
        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
        if (MainApp.locale.equalsIgnoreCase("gu")) {
            cboxProduct.setCellFactory(new ProductLocalCellFactory());
        } else {
            cboxProduct.setCellFactory(new ProductCellFactory());
        }
        new AutoCompleteComboBoxListener<>(cboxProduct);
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
                listPLIncome = list.stream().filter(p -> p != null && p.getIncomeExpense() == 1).collect(Collectors.toList());
                param.put("p_imcome_side", listPLIncome);
                listPLExpense = list.stream().filter(p -> p != null && p.getIncomeExpense() == 0).collect(Collectors.toList());
                param.put("p_expense_side", listPLExpense);

//                if (listPLExpense != null && listPLIncome != null) {
//                    param.put("p_balance", listPLIncome.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum()
//
//                            - Math.abs(listPLExpense.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum()));
//                } else if (listPLIncome != null) {
//                    param.put("p_balance",
//                            listPLIncome.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum() - 0);
//                } else if (listPLExpense != null) {
//                    param.put("p_balance",
//                            0 - listPLExpense.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum());
//                }

                double incomeTotal = listPLIncome != null
                        ? listPLIncome.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum() : 0;
                double expenseTotal = listPLExpense != null
                        ? Math.abs(listPLExpense.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum()) : 0;

                param.put("p_balance", incomeTotal - expenseTotal);

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

                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_PROFIT_LOSS_REPORT, param, new JRBeanCollectionDataSource(listPLRows));
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
                        JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_TRADINGREPORT, TradingTaskparams, new JRBeanCollectionDataSource(listtradingTask));
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

//                Change made by manoj - (12.05.2022)
//                if (listPLExpense == null || listPLExpense.isEmpty() || listPLIncome == null || listPLIncome.isEmpty())
//                    loadPL();
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
//                    listBSLiability.add(new LedgerBalance("", "Total", 0, 0,
//                            NumberUtil.round(listBSLiability.stream().mapToDouble(m -> m.getBalance()).sum(), 2), 1));

                }
                listBSAsset = list.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
                if (listBSAsset != null) {
                    if (diff < 0) {
                        listBSAsset.add(new LedgerBalance("", "PL Ledger", 0, 0, Math.abs(diff), 0));
                    }
//                    listBSAsset.add(new LedgerBalance("", "Total", 0, 0,
//                            NumberUtil.round(listBSAsset.stream().mapToDouble(m -> Math.abs(m.getBalance())).sum(), 2), 0));

                }


//                param.put("p_liability_side", listBSLiability);
//
//                param.put("p_asset_side", listBSAsset);


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

//                param.put("p_balance_sheet_rows", listBSRows);


                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_BALANCESHEET, param, new JRBeanCollectionDataSource(listBSRows));
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
}

