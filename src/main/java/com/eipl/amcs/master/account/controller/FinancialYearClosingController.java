package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.dto.LedgerSubLedgerDto;
import com.eipl.amcs.master.account.dto.YearClosingDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.master.account.task.LedgerSubLedgerMappingDtoLoadTask;
import com.eipl.amcs.master.account.task.SubLedgerLoadTask;
import com.eipl.amcs.master.account.task.YearClosingDtoSaveTask;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.LedgerClose;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.task.*;
import com.eipl.amcs.utils.NumberUtil;
import com.eipl.amcs.utils.TableExportUtil;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FinancialYearClosingController implements MyInitialization, PopupCallback {

    private final List<LedgerOpeningBalance> ledgerOpeningBalanceList;
    private final ObjectProperty<LedgerClose> propObjLedger;
    TimeUnit time = TimeUnit.SECONDS;
    List<SubLedgerOpeningBalance> listSubLdgrOpening = new ArrayList<>();
    MyAlert alert;
    @FXML
    private AnchorPane root;
    @FXML
    private TableView<ProductStockValuation> tableStockValuation;
    @FXML
    private TableColumn<ProductStockValuation, String> colStockValProductCode, colStockValProductName,
            colStockValProductUnit;
    @FXML
    private TableColumn<ProductStockValuation, Number> colStockValProductStock, colStockValAmount;
    @FXML
    private Label lblStockValuation;
    @FXML
    private Button btnExportValuation, btnPrintValuation;
    @FXML
    private TableView<LedgerBalance> tableTrading;
    @FXML
    private TableColumn<LedgerBalance, String> colTradingLedgerCode, colTradingLedgerName;
    @FXML
    private TableColumn<LedgerBalance, Number> colTradingBalance, colTradingDebit, colTradingCredit;
    @FXML
    private Label lblTotalTrading;
    @FXML
    private Button btnExportTrading, btnPrintTrading;
    @FXML
    private TableView<LedgerBalance> tablePLIncome;
    @FXML
    private TableColumn<LedgerBalance, String> colPLLedgerIncome;
    @FXML
    private TableColumn<LedgerBalance, Number> colPLAmountIncome;
    @FXML
    private TableView<LedgerBalance> tablePLExpense;
    @FXML
    private TableColumn<LedgerBalance, String> colPLLedgerExpense;
    @FXML
    private TableColumn<LedgerBalance, Number> colPLAmountExpense;
    @FXML
    private Button btnExportPL, btnPrintPL;
    @FXML
    private ContextMenu contextMenuPLExport;
    @FXML
    private MenuItem menuItemPLIncome, menuItemPLExpense;
    @FXML
    private Label lblPl, lblPlBalance, lblTrading;
    @FXML
    private TableView<LedgerBalance> tableBSLiability, tableBSAsset;
    @FXML
    private TableColumn<LedgerBalance, String> colBSLiabilityLedgerName, colBSAssetLedgerName;
    @FXML
    private TableColumn<LedgerBalance, Number> colBSLiabilityAmount, colBSAssetAmount;
    @FXML
    private Button btnExportBS, btnPrintBS, btnSubLeder;
    @FXML
    private ContextMenu contextMenuBSExport;
    @FXML
    private MenuItem menuItemBSLiability, menuItemBSAsset;
    @FXML
    private TableView<LedgerClose> tableReview, tableReview1;
    @FXML
    private TableColumn<LedgerClose, String> colReviewLedgerName, colReviewLedgerName1, colReviewLedgerType, colReviewLedgerType1;
    @FXML
    private TableColumn<LedgerClose, Number> colReviewLedgerBalance, colReviewLedgerBalance1;
    @FXML
    private Button btnReviewSave, btnReviewClose, btnExportReview, btnFilter;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabStockValuation, tabTrading, tabPL, tabBS, tabReview;
    private List<ProductStockValuation> listStockValuation;
    private List<LedgerBalance> listTrading;
    private List<LedgerBalance> listPLExpense, listPLIncome;
    private List<LedgerBalance> listBSLiability, listBSAsset;
    private List<LedgerClose> ledgerCloses;
    private List<LedgerClose> subLedgerCloses;
    private Ledger ledgerFetchByCode;
    private SubLedger subLedgerFetchByCode;
    private ResourceBundle resources;
    private List<Ledger> listLedger;
    private List<SubLedger> listSubLedger;
    private LedgerSubLedgerDto ledgerSubLedgerDto;
    private List<Object[]> subLedgerOpeningBalanceList;
    private SocietyYearClosing societyYearClosingDto = null;

    private Stage stage;

    private Boolean flag = true;

    public FinancialYearClosingController() {
        propObjLedger = new SimpleObjectProperty<>();
        ledgerOpeningBalanceList = new ArrayList<>();
        subLedgerOpeningBalanceList = new ArrayList<>();

    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setData() {
        societyYearClosingDto = new SocietyYearClosing();
        societyYearClosingDto.setSociety(MainApp.identityDto.getSociety());
        societyYearClosingDto.setFinancialYear(MainApp.getFinancialYear());
        societyYearClosingDto.setClosingDate(LocalDate.now());
        societyYearClosingDto.setUnionCode(MainApp.identityDto.getUnion().getCode());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        loadStockValuation();
        setupTable();
        loadTrading();
        loadBS();

        loadLedger();
        loadSubLedger();
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                switch (newValue.getId()) {
                    case "tabStockValuation":
                        loadStockValuation();
                        break;
                    case "tabTrading":
                        loadTrading();
                        break;
                    case "tabPL":
                        loadPL();
                        break;
                    case "tabBS":
                        loadBS();
                        break;
                    case "tabReview":
                        loadReview();
                        break;
                    default:
                        break;
                }
            }
        });


        btnSubLeder.setOnAction(e -> MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SubLedgerView", null, this));
        btnReviewClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnReviewSave.setOnAction(event -> {
            closeReview();

        });

        btnExportReview.setOnAction(e -> {
            if (TableExportUtil.exportDataFrom2TableView(tableReview, tableReview1, tableReview.getId(),
                    FinancialYearClosingController.class.getSimpleName())) {
                alert = new InformationAlert(MainApp.stage, MainApp.getBundle().getString("review"),
                        MainApp.getBundle().getString("successful"));
            } else {
                alert = new ErrorAlert(MainApp.stage, MainApp.getBundle().getString("review"),
                        MainApp.getBundle().getString("error.occurred"));
            }
        });

        menuItemBSAsset = new MenuItem(resources.getString("yearend.bsassetside"));
        menuItemBSAsset.setOnAction(event -> {
            if (TableExportUtil.exportDataFromTableView(tableBSAsset, tableBSAsset.getId(),
                    FinancialYearClosingController.class.getSimpleName())) {
                MyAlert alert = new InformationAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message"));
                alert.createAlert();
            } else {
                MyAlert alert = new ErrorAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message.error"));
                alert.createAlert();
            }
        });
        menuItemBSLiability = new MenuItem(resources.getString("yearend.bsliabilityside"));
        menuItemBSLiability.setOnAction(event -> {
            if (TableExportUtil.exportDataFromTableView(tableBSLiability, tableBSLiability.getId(),
                    FinancialYearClosingController.class.getSimpleName())) {
                MyAlert alert = new InformationAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message"));
                alert.createAlert();
            } else {
                MyAlert alert = new ErrorAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message.error"));
                alert.createAlert();
            }
        });
        contextMenuBSExport = new ContextMenu(menuItemBSAsset, menuItemBSLiability);
        btnExportBS.setOnAction(event -> {
            contextMenuBSExport.show(btnExportBS, Side.TOP, 0, 0);
        });

        btnPrintValuation.setOnAction(event -> {
            if (listStockValuation != null && !listStockValuation.isEmpty()) {
                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (MainApp.locale.equals("en")) {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                params.put("p_from_date", MainApp.getFinancialYear().getStartDate());
                params.put("p_to_date", MainApp.getFinancialYear().getEndDate());
                params.put("p_as_on_date", MainApp.getFinancialYear().getEndDate());
                params.put("p_locale", MainApp.locale);
                JasperPrint jasperPrint;
                try {
                    jasperPrint = JasperFillManager.fillReport("resources/report/milkcollection/StockValuation.jasper",
                            params, new JRBeanCollectionDataSource(listStockValuation));
                    JasperViewer.viewReport(jasperPrint, false);
                } catch (JRException e) {
                    e.printStackTrace();
                }
            }
        });

        btnPrintTrading.setOnAction(event -> {
            if (listTrading != null && !listTrading.isEmpty()) {
                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (MainApp.locale.equals("en")) {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                params.put("p_from_date", MainApp.getFinancialYear().getStartDate());
                params.put("p_to_date", MainApp.getFinancialYear().getEndDate());
                params.put("p_locale", MainApp.locale);
                JasperPrint jasperPrint;
                try {
                    jasperPrint = JasperFillManager.fillReport("resources/report/milkcollection/TradingReport.jasper",
                            params, new JRBeanCollectionDataSource(listTrading));
                    JasperViewer.viewReport(jasperPrint, false);
                } catch (JRException e) {
                    e.printStackTrace();
                }
            }
        });

        btnExportValuation.setOnAction(event -> {
            if (TableExportUtil.exportDataFromTableView(tableStockValuation, tableStockValuation.getId(),
                    FinancialYearClosingController.class.getSimpleName())) {
                alert = new InformationAlert(MainApp.stage, MainApp.getBundle().getString("stockvaluation"),
                        MainApp.getBundle().getString("successful"));
            } else {
                alert = new ErrorAlert(MainApp.stage, MainApp.getBundle().getString("stockvaluation"),
                        MainApp.getBundle().getString("error.occurred"));
            }
        });

        btnExportTrading.setOnAction(event -> {
            if (TableExportUtil.exportDataFromTableView(tableTrading, tableTrading.getId(),
                    FinancialYearClosingController.class.getSimpleName())) {
                alert = new InformationAlert(MainApp.stage, MainApp.getBundle().getString("tradingreport"),
                        MainApp.getBundle().getString("successful"));
            } else {
                alert = new ErrorAlert(MainApp.stage, MainApp.getBundle().getString("tradingreport"),
                        MainApp.getBundle().getString("error.occurred"));
            }
        });
        menuItemPLIncome = new MenuItem(resources.getString("yearend.incomeside"));
        menuItemPLIncome.setOnAction(event -> {
            if (TableExportUtil.exportDataFromTableView(tablePLIncome, tablePLIncome.getId(),
                    FinancialYearClosingController.class.getSimpleName())) {
                MyAlert alert = new InformationAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message"));
                alert.createAlert();
            } else {
                MyAlert alert = new ErrorAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message.error"));
                alert.createAlert();
            }
        });
        menuItemPLExpense = new MenuItem(resources.getString("yearend.expenseside"));
        menuItemPLExpense.setOnAction(event -> {
            if (TableExportUtil.exportDataFromTableView(tablePLExpense, tablePLExpense.getId(),
                    FinancialYearClosingController.class.getSimpleName())) {
                MyAlert alert = new InformationAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message"));
                alert.createAlert();
            } else {
                MyAlert alert = new ErrorAlert(MainApp.stage, resources.getString("excel"),
                        resources.getString("excel.export.message.error"));
                alert.createAlert();
            }
        });
        contextMenuPLExport = new ContextMenu(menuItemPLIncome, menuItemPLExpense);
        btnExportPL.setOnAction(event -> {
            contextMenuPLExport.show(btnExportPL, Side.TOP, 0, 0);
        });

        btnPrintPL.setOnAction(data -> {
            Map<String, Object> param = new HashMap<>();
            param.put("p_society_code", MainApp.identityDto.getSociety().getCode());
            if (MainApp.locale.equals("en")) {
                param.put("p_society_name", MainApp.identityDto.getSociety().getName());
            } else {
                param.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
            }
            param.put("p_from_date", MainApp.getFinancialYear().getStartDate());
            param.put("p_to_date", MainApp.getFinancialYear().getEndDate());
            param.put("p_locale", MainApp.locale);
            listPLIncome.stream().filter(p -> p.getIncomeExpense() == 1).collect(Collectors.toList());
            param.put("p_imcome_side", listPLIncome);
            listPLIncome.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
            param.put("p_expense_side", listPLExpense);

            if (listPLExpense != null && listPLIncome != null) {
                param.put("p_balance", listPLIncome.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum()

                        - Math.abs(listPLExpense.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum()));
            } else if (listPLIncome != null) {
                param.put("p_balance",
                        listPLIncome.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum() - 0);
            } else if (listPLExpense != null) {
                param.put("p_balance",
                        0 - listPLExpense.stream().mapToDouble(m -> m != null ? m.getBalance() : 0).sum());
            }
            JasperPrint jasperPrint;
            try {
                jasperPrint = JasperFillManager.fillReport("resources/report/milkcollection/ProfitLoss.jasper", param,
                        new JREmptyDataSource());
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException e) {
                e.printStackTrace();
            }

        });

        btnPrintBS.setOnAction(data -> {
            if (listBSLiability != null && !listBSLiability.isEmpty()) {
                Map<String, Object> param = new HashMap<>();
                param.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (MainApp.locale.equals("en")) {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal());
                }
                param.put("p_from_date", MainApp.getFinancialYear().getStartDate());
                param.put("p_to_date", MainApp.getFinancialYear().getEndDate());
                param.put("p_locale", MainApp.locale);

                listBSLiability = listBSLiability.stream().filter(p -> p.getIncomeExpense() == 1).collect(Collectors.toList());
                param.put("p_liability_side", listBSLiability);
                listBSAsset = listBSAsset.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
                param.put("p_asset_side", listBSAsset);

                JasperPrint jasperPrint;
                try {
                    jasperPrint = JasperFillManager.fillReport("resources/report/milkcollection/BalanceSheet.jasper",
                            param, new JREmptyDataSource());
                    JasperViewer.viewReport(jasperPrint, false);
                } catch (JRException e) {
                    e.printStackTrace();
                }
            }
        });
        btnFilter.setOnAction(e -> {
            if (flag) {
                tableReview.setItems(FXCollections.observableArrayList(ledgerCloses.stream().
                        filter(e1 -> e1.getBalance() != 0.00).collect(Collectors.toList())));
                tableReview1.setItems(FXCollections.observableArrayList(ledgerCloses.stream().
                        filter(e1 -> e1.getBalance() != 0.00).collect(Collectors.toList())));
                flag = !flag;
            } else {
                tableReview.setItems(FXCollections.observableArrayList(ledgerCloses));
                tableReview1.setItems(FXCollections.observableArrayList(ledgerCloses));
                flag = !flag;
            }
        });

    }

    public void loadLedger() {
        var task = new LedgerLoadTask();
        if (listLedger != null)
            listLedger.clear();
        task.setOnSucceeded(ee -> {
            try {
                listLedger = task.get();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadSubLedger() {
        var task = new SubLedgerLoadTask();
        if (listSubLedger != null)
            listSubLedger.clear();
        task.setOnSucceeded(ee -> {
            try {
                listSubLedger = task.get();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadLedgerSubLedgerMapping(Ledger ledger) {
        ledgerSubLedgerDto = new LedgerSubLedgerDto();
        var task = new LedgerSubLedgerMappingDtoLoadTask(new Ledger(), null);
        if (ledgerSubLedgerDto != null)
            task.setOnSucceeded(ee -> {
                try {
                    ledgerSubLedgerDto = task.get();
                    listSubLedger = ledgerSubLedgerDto.getSubLedgerList();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
        new Thread(task).start();
    }

    public void closeReview() {
        NextFinYearDateTask financialYear = new NextFinYearDateTask(MainApp.getFinancialYear().getEndDate().plusDays(1));
        financialYear.setOnSucceeded(e -> {
            try {
                if (financialYear.get() == null) {
                    MyAlert alert = new WarningAlert(MainApp.stage, resources.getString("yearend.title"),
                            resources.getString("alert.yearclose.nextyearnotfound"));
                    alert.createAlert();
                } else {
                    FinancialYearsCodeTask nextFinYearDateTask = new FinancialYearsCodeTask(MainApp.getFinancialYear().getCode());
                    nextFinYearDateTask.setOnSucceeded(ee -> {
                        try {
                            Boolean financialYearCode = nextFinYearDateTask.get();
                            if (financialYearCode) {
                                ledgerOpeningBalanceList.clear();
                                for (LedgerClose arr : ledgerCloses) {
                                    LedgerOpeningBalance balance = new LedgerOpeningBalance();
                                    balance.setSociety(MainApp.identityDto.getSociety());
                                    balance.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                    balance.setBalance(BigDecimal.valueOf(arr.getBalance()));
                                    balance.setCreditDebit(arr.isCreditDebit());
                                    balance.setAutoManual(true);
                                    balance.setFinancialYearsCode(MainApp.getFinancialYear().getCode());

                                    balance.setLedger(listLedger.stream().filter(p -> p.getCode().equals(arr.getLedgerCode())).findAny().orElse(null));

                                    ledgerOpeningBalanceList.add(balance);
                                }

                                SubLedgerOpeningTask subLedgerOpeningTask = new SubLedgerOpeningTask(MainApp.identityDto.getSociety().getCode(), MainApp.getFinancialYear().getStartDate(), MainApp.getFinancialYear().getEndDate(), MainApp.locale);
                                subLedgerOpeningTask.setOnSucceeded(s -> {
                                    try {
                                        subLedgerOpeningBalanceList = subLedgerOpeningTask.get();
                                        for (Object[] arr : subLedgerOpeningBalanceList) {
                                            SubLedgerOpeningBalance subLedgerOpeningBalance = new SubLedgerOpeningBalance();
                                            subLedgerOpeningBalance.setBalance(BigDecimal.valueOf((Double) arr[2]));
                                            subLedgerOpeningBalance.setCreditDebit(!((double) arr[2] < 0));
                                            subLedgerOpeningBalance.setSociety(MainApp.identityDto.getSociety());
                                            subLedgerOpeningBalance.setFinancialYearsCode(MainApp.getFinancialYear().getCode());
                                            subLedgerOpeningBalance.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                            subLedgerOpeningBalance.setAutoManual(true);
                                            LedgerFetchByCodeLoadTask loadFetchByCodeLedgerTask = new LedgerFetchByCodeLoadTask((String) arr[3]);
                                            loadFetchByCodeLedgerTask.setOnSucceeded(eee -> {
                                                try {
                                                    ledgerFetchByCode = loadFetchByCodeLedgerTask.get();
                                                    SubLedgerFetchByCodeLoadTask loadFetchByCodeSubLedgerTask = new SubLedgerFetchByCodeLoadTask((String) arr[0]);
                                                    loadFetchByCodeSubLedgerTask.setOnSucceeded(eeq -> {
                                                        try {
                                                            subLedgerFetchByCode = loadFetchByCodeSubLedgerTask.get();
                                                            subLedgerOpeningBalance.setSubLedger(subLedgerFetchByCode);
                                                            subLedgerOpeningBalance.setLedger(ledgerFetchByCode);
                                                            listSubLdgrOpening.add(subLedgerOpeningBalance);
                                                        } catch (InterruptedException | ExecutionException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    });
                                                    new Thread(loadFetchByCodeSubLedgerTask).start();
                                                } catch (InterruptedException | ExecutionException ex) {
                                                    ex.printStackTrace();
                                                }
                                            });
                                            new Thread(loadFetchByCodeLedgerTask).start();
                                        }

                                    } catch (InterruptedException | ExecutionException ex) {
                                        ex.printStackTrace();
                                    }
                                });
                                new Thread(subLedgerOpeningTask).start();

                            }
                            saveData();
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(nextFinYearDateTask).start();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }

        });
        new Thread(financialYear).start();
    }

    protected void loadReview() {
        if (listBSLiability == null || listBSLiability.isEmpty() || listBSAsset == null || listBSAsset.isEmpty())
            loadBS();
        LedgerCloseTask ledgerCloseTask = new LedgerCloseTask(MainApp.identityDto.getSociety().getCode(), MainApp.getFinancialYear().getStartDate(), MainApp.getFinancialYear().getEndDate(), MainApp.locale);
        if (ledgerCloses != null)
            ledgerCloses.clear();
        ledgerCloseTask.setOnSucceeded(ee -> {
            try {
                ledgerCloses = ledgerCloseTask.get();

                if (ledgerCloses != null) {
                    tableReview.setItems(FXCollections.observableArrayList(ledgerCloses.stream().filter(e -> e.isCreditDebit()).collect(Collectors.toList())));
                    tableReview1.setItems(FXCollections.observableArrayList(ledgerCloses.stream().filter(e -> !e.isCreditDebit()).collect(Collectors.toList())));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(ledgerCloseTask).start();
    }

    protected void loadFetchByCodeLedger(String code) {
        LedgerFetchByCodeLoadTask loadFetchByCodeLedgerTask = new LedgerFetchByCodeLoadTask(code);
        loadFetchByCodeLedgerTask.setOnSucceeded(ee -> {
            try {
                ledgerFetchByCode = loadFetchByCodeLedgerTask.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(loadFetchByCodeLedgerTask).start();
    }

    protected void loadFetchByCodeSubLedger(String code) {
        SubLedgerFetchByCodeLoadTask loadFetchByCodeSubLedgerTask = new SubLedgerFetchByCodeLoadTask(code);
        if (subLedgerFetchByCode != null)
            loadFetchByCodeSubLedgerTask.setOnSucceeded(ee -> {
                try {
                    subLedgerFetchByCode = loadFetchByCodeSubLedgerTask.get();

                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
        new Thread(loadFetchByCodeSubLedgerTask).start();
    }

    protected void loadBS() {
        BalanceSheetTask balanceSheetTask = new BalanceSheetTask(MainApp.identityDto.getSociety().getCode(),
                MainApp.getFinancialYear().getStartDate(), MainApp.getFinancialYear().getEndDate(), MainApp.locale);

        balanceSheetTask.setOnFailed(e -> {
            Throwable cause = balanceSheetTask.getException();
            if (cause != null) {
                System.err.println("BalanceSheetTask failed:");
                cause.printStackTrace();
            }
        });

        balanceSheetTask.setOnSucceeded(e -> {
            listBSLiability = new ArrayList<>();
            listBSAsset = new ArrayList<>();

            List<LedgerBalance> fullBalanceSheetList = null;

            try {
                fullBalanceSheetList = balanceSheetTask.get();

            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Error retrieving balance sheet data:");
                ex.printStackTrace();
                return;
            }
            if (fullBalanceSheetList != null && !fullBalanceSheetList.isEmpty()) {
                listBSLiability = fullBalanceSheetList.stream()
                        .filter(java.util.Objects::nonNull)
                        .filter(p -> p.getIncomeExpense() == 1)
                        .collect(Collectors.toList());
                listBSAsset = fullBalanceSheetList.stream()
                        .filter(java.util.Objects::nonNull)
                        .filter(p -> p.getIncomeExpense() == 0)
                        .collect(Collectors.toList());
            }

            if (listPLExpense == null || listPLExpense.isEmpty() || listPLIncome == null || listPLIncome.isEmpty()) {
                loadPL();
            }

            double diff = 0;
            if (listPLExpense != null && listPLIncome != null) {
                diff = listPLIncome.stream().mapToDouble(m -> m.getBalance()).sum()
                        - Math.abs(listPLExpense.stream().mapToDouble(m -> m.getBalance()).sum());
            } else if (listPLIncome != null) {
                diff = listPLIncome.stream().mapToDouble(m -> m.getBalance()).sum();
            } else if (listPLExpense != null) {
                diff = -Math.abs(listPLExpense.stream().mapToDouble(m -> m.getBalance()).sum());
            }

            if (diff > 0) {
                listBSLiability.add(new LedgerBalance("", resources.getString("pl_ledger"), 0, 0, Math.abs(diff), 1));
            }
            listBSLiability.add(new LedgerBalance("", resources.getString("total"), 0, 0,
                    NumberUtil.round(listBSLiability.stream().mapToDouble(m -> m.getBalance()).sum(), 2), 1));
            tableBSLiability.setItems(FXCollections.observableArrayList(listBSLiability));

            if (diff < 0) {
                listBSAsset.add(new LedgerBalance("", resources.getString("pl_ledger"), 0, 0, Math.abs(diff), 0));
            }
            listBSAsset.add(new LedgerBalance("", resources.getString("total"), 0, 0,
                    NumberUtil.round(listBSAsset.stream().mapToDouble(m -> Math.abs(m.getBalance())).sum(), 2), 0));
            tableBSAsset.setItems(FXCollections.observableArrayList(listBSAsset));

        });
        new Thread(balanceSheetTask).start();
    }

    protected void loadPL() {
        ProfitLossTask profitLossTask = new ProfitLossTask(MainApp.identityDto.getSociety().getCode(),
                MainApp.getFinancialYear().getStartDate(), MainApp.getFinancialYear().getEndDate(), MainApp.locale);

        profitLossTask.setOnFailed(e -> {
            Throwable cause = profitLossTask.getException();
            if (cause != null) {
                System.err.println("ProfitLossTask failed:");
                cause.printStackTrace();
            }
        });

        profitLossTask.setOnSucceeded(e -> {
            listPLIncome = new ArrayList<>();
            listPLExpense = new ArrayList<>();
            List<LedgerBalance> fullProfitLossList = null;

            try {
                fullProfitLossList = profitLossTask.get();
                if (fullProfitLossList == null || fullProfitLossList.isEmpty()) {
                    System.out.println("Profit/Loss list was null or empty.");
                    return;
                }
                for (LedgerBalance ledgerBalance : fullProfitLossList) {
                    if (ledgerBalance != null) {
                        ledgerBalance.setLedgerName(ledgerBalance.getLedgerName());
                    }
                }
                listPLIncome = fullProfitLossList.stream()
                        .filter(java.util.Objects::nonNull)
                        .filter(p -> p.getIncomeExpense() == 1)
                        .collect(Collectors.toList());

                listPLExpense = fullProfitLossList.stream()
                        .filter(java.util.Objects::nonNull)
                        .filter(p -> p.getIncomeExpense() == 0)
                        .collect(Collectors.toList());

            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Error retrieving Profit/Loss data:");
                ex.printStackTrace();
                return;
            }
            tablePLExpense.setItems(FXCollections.observableArrayList(listPLExpense));
            tablePLIncome.setItems(FXCollections.observableArrayList(listPLIncome));
            double incomeSum = listPLIncome.stream().mapToDouble(LedgerBalance::getBalance).sum();
            double expenseSum = listPLExpense.stream().mapToDouble(m -> Math.abs(m.getBalance())).sum();
            double diff = incomeSum - expenseSum;

            lblPl.setText(diff > 0 ? MainApp.getBundle().getString("grossprofit") : MainApp.getBundle().getString("grossloss"));
            if (diff > 0) {
                lblPl.setStyle("-fx-text-fill: #006400;");
            } else {
                lblPl.setStyle("-fx-text-fill: #888c91;");
            }
            lblPlBalance.setText(NumberUtil.twoDecimal(Math.abs(diff)));
        });
        new Thread(profitLossTask).start();
    }

    protected void loadTrading() {
        TradingTask tradingTask = new TradingTask(MainApp.identityDto.getSociety().getCode(), MainApp.getFinancialYear().getStartDate(), MainApp.getFinancialYear().getEndDate(), MainApp.locale);
        tradingTask.setOnSucceeded(e -> {
            try {
                listTrading = tradingTask.get();
                for (LedgerBalance ledgerBalance : listTrading) {
                    ledgerBalance.setLedgerName((ledgerBalance.getLedgerName()));
                }
                if (listTrading != null && !listTrading.isEmpty()) {
                    tableTrading.setItems(FXCollections.observableArrayList(listTrading));
                    lblTotalTrading.setText(String.format("%.2f", (listTrading.stream().mapToDouble(m -> m.getBalance()).sum())));
                    lblTrading.setStyle("-fx-text-fill: #006400;");
                    tableTrading.setItems(FXCollections.observableArrayList(listTrading));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(tradingTask).start();
    }


    //stock valuation
    private void loadStockValuation() {
        LoadStockValuationTask stockValuationTask = new LoadStockValuationTask(MainApp.identityDto.getSociety().getCode(), MainApp.getFinancialYear().getEndDate(), MainApp.locale);
        stockValuationTask.setOnSucceeded(e -> {
            try {
                List<com.eipl.amcs.master.account.model.ProductStockValuation> list = stockValuationTask.get();

                listStockValuation = new ArrayList<>();
                if (list != null) {
                    for (com.eipl.amcs.master.account.model.ProductStockValuation psv : list) {
                        listStockValuation.add(new com.eipl.amcs.report.dto.ProductStockValuation(psv.getProductCode(), psv.getProductName(), psv.getStock(), psv.getValuation(), psv.getUnit()));
                    }
                }

                tableStockValuation.setItems(FXCollections.observableArrayList(listStockValuation));
                lblStockValuation.setText(String.format("%.2f", listStockValuation.stream().mapToDouble(m -> m.getValuation()).sum()));
                tableStockValuation.setItems(FXCollections.observableArrayList(listStockValuation));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(stockValuationTask).start();
    }

    @Override
    public void setupTable() {
        // Stock valuation
        colStockValProductCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductCode()));
        colStockValProductName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        colStockValProductUnit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnit()));
        colStockValProductStock.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getStock()));
        //   colStockValProductStock.setCellFactory(new RightAlignCellFactory<>());
        colStockValAmount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getValuation()));
        //    colStockValAmount.setCellFactory(new RightAlignCellFactory<>());

        // Trading
        colTradingLedgerCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerCode()));
        colTradingLedgerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colTradingLedgerName.setCellFactory(cell -> {
            return new TableCell<LedgerBalance, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals("stockvaluation")) {
                            setStyle("-fx-text-fill: #2E7D32 ; -fx-font-weight: bold;");
                            setText(MainApp.getBundle().getString("stockvaluation"));
                        } else {
                            setStyle("-fx-text-fill: #D32F2F ; -fx-font-weight: bold;");
                            setText(item);
                        }

                    }
                }
            };
        });
        colTradingDebit.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getDebit()));
        //     colTradingDebit.setCellFactory(new RightAlignCellFactory<>());
        colTradingCredit.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCredit()));
        //     colTradingCredit.setCellFactory(new RightAlignCellFactory<>());
        colTradingBalance.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getBalance()));
        //   colTradingBalance.setCellFactory(new LedgerBalanceCellFactory<>());

        //ProfitLoss
        colPLLedgerIncome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colPLLedgerIncome.setCellFactory(cell -> {
            return new TableCell<LedgerBalance, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals(MainApp.getBundle().getString("trading"))) {
                            setStyle("-fx-text-fill: #2E7D32 ; -fx-font-weight: bold;");
                        }
                        setText(item);
                    }
                }
            };
        });
        colPLAmountIncome.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getBalance()));
        //   colPLAmountIncome.setCellFactory(new RightAlignCellFactory<>());
        colPLLedgerExpense.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colPLLedgerExpense.setCellFactory(cell -> {
            return new TableCell<LedgerBalance, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals(MainApp.getBundle().getString("trading"))) {
                            setStyle("-fx-text-fill: #2E7D32 ; -fx-font-weight: bold;");
                        }
                        setText(item);
                    }
                }
            };
        });
        colPLAmountExpense.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getBalance()));
        // colPLAmountExpense.setCellFactory(new RightAlignCellFactory<>());


        // Balance Sheet
        colBSLiabilityLedgerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colBSLiabilityLedgerName.setCellFactory(cell -> {
            return new TableCell<LedgerBalance, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals(MainApp.getBundle().getString("pl_ledger"))) {
                            setStyle("-fx-text-fill: #2E7D32 ; -fx-font-weight: bold;");
                        }
                        setText(item);
                    }
                }
            };
        });
        colBSLiabilityAmount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getBalance()));
        //     colBSLiabilityAmount.setCellFactory(new RightAlignCellFactory<>());
        colBSAssetLedgerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colBSAssetLedgerName.setCellFactory(cell -> {
            return new TableCell<LedgerBalance, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals(MainApp.getBundle().getString("pl_ledger"))) {
                            setStyle("-fx-text-fill: #2E7D32 ; -fx-font-weight: bold;");
                        }
                        setText(item);
                    }
                }
            };
        });
        colBSAssetAmount.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getBalance()));
        //  colBSAssetAmount.setCellFactory(new RightAlignCellFactory<>());

        // Review
//        colReviewLedgerCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerCode()));
        colReviewLedgerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colReviewLedgerType.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isCreditDebit() ? MainApp.getBundle().getString("credit") : MainApp.getBundle().getString("debit")));
        colReviewLedgerType.setCellFactory(cell -> {
            return new TableCell<LedgerClose, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals(MainApp.getBundle().getString("credit"))) {
                            setStyle("-fx-text-fill: #2E7D32 ; -fx-font-weight: bold;");

                        } else {
                            setStyle("-fx-text-fill: #D32F2F ; -fx-font-weight: bold;");

                        }
                        setText(item);
                    }
                }
            };
        });
        colReviewLedgerBalance
                .setCellValueFactory(data -> new SimpleDoubleProperty(Math.abs(data.getValue().getBalance())));
        colReviewLedgerName1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colReviewLedgerType1.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isCreditDebit() ? MainApp.getBundle().getString("credit") : MainApp.getBundle().getString("debit")));
        colReviewLedgerType1.setCellFactory(cell -> {
            return new TableCell<LedgerClose, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals(MainApp.getBundle().getString("credit"))) {
                            setStyle("-fx-text-fill: #2E7D32 ; -fx-font-weight: bold;");

                        } else {
                            setStyle("-fx-text-fill: #D32F2F ; -fx-font-weight: bold;");

                        }
                        setText(item);
                    }
                }
            };
        });
        colReviewLedgerBalance1
                .setCellValueFactory(data -> new SimpleDoubleProperty(Math.abs(data.getValue().getBalance())));

        propObjLedger.bind(tableReview.getSelectionModel().selectedItemProperty());
        TableLocalizationUtil.localizeTable(tableStockValuation);
        TableLocalizationUtil.localizeTable(tableReview);
        TableLocalizationUtil.localizeTable(tablePLIncome);
        TableLocalizationUtil.localizeTable(tableReview1);
        TableLocalizationUtil.localizeTable(tableBSAsset);
        TableLocalizationUtil.localizeTable(tableBSLiability);
        TableLocalizationUtil.localizeTable(tablePLExpense);
        TableLocalizationUtil.localizeTable(tableTrading);

    }

    public void saveData() {
        setData();
        try {
            MainApp.paneDrop.setVisible(true);
            MainApp.lblMessage.setText("Processing...");
            time.sleep(20);
            MainApp.paneDrop.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        YearClosingDto yearClosingDto = new YearClosingDto(ledgerOpeningBalanceList, listSubLdgrOpening, societyYearClosingDto);

        var task = new YearClosingDtoSaveTask(yearClosingDto);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + MainApp.getBundle().getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), MainApp.getBundle().getString("yearclosing"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), MainApp.getBundle().getString("yearclosing"),
                        MainApp.getBundle().getString("yearclosing.insert.successful"));
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
