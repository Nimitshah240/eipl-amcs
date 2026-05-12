package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.task.BalanceSheetTask;
import com.eipl.amcs.report.task.ProfitLossTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.NumberUtil;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class RptBalanceSheetController implements MyInitialization {
    @FXML
    private Button btnClose;

    @FXML
    private Button btnGenerate;

    @FXML
    private DatePicker dpFromDate, dpToDate;

    @FXML
    private SwingNode reportNode;

    @FXML
    private AnchorPane root;

    @FXML
    private ComboBox<String> cboxLanguage;
    private List<LedgerBalance> listBSLiability, listBSAsset;
    private List<LedgerBalance> listPLExpense, listPLIncome;

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        btnGenerate.setOnAction(e -> {
            loadPL();
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
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    @Override
    public void loadData() {
        String localeStr = getLocaleString();
        BalanceSheetTask balanceSheetTask = new BalanceSheetTask(MainApp.identityDto.getSociety().getCode(),
                dpFromDate.getValue(), dpToDate.getValue(), localeStr);
        balanceSheetTask.setOnSucceeded(ee -> {
            try {
                List<LedgerBalance> list = balanceSheetTask.get();
                if (list == null) {
                    list = new ArrayList<>();
                }
                Map<String, Object> param = new HashMap<>();
                param.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (localeStr.equals("en")) {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                param.put("p_from_date", LocalDate.parse(dpFromDate.getValue().toString()));
                param.put("p_to_date", LocalDate.parse(dpToDate.getValue().toString()));
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
                    listBSLiability.add(new LedgerBalance("", "Total", 0, 0,
                            NumberUtil.round(listBSLiability.stream().mapToDouble(m -> m.getBalance()).sum(), 2), 1));

                }
                listBSAsset = list.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
                if (listBSAsset != null) {
                    if (diff < 0) {
                        listBSAsset.add(new LedgerBalance("", "PL Ledger", 0, 0, Math.abs(diff), 0));
                    }
                    listBSAsset.add(new LedgerBalance("", "Total", 0, 0,
                            NumberUtil.round(listBSAsset.stream().mapToDouble(m -> Math.abs(m.getBalance())).sum(), 2), 0));

                }


                param.put("p_liability_side", listBSLiability);

                param.put("p_asset_side", listBSAsset);

                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_BALANCESHEET, param, new JREmptyDataSource());
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(balanceSheetTask).start();
    }

    protected void loadPL() {
        ProfitLossTask profitLossTask = new ProfitLossTask(MainApp.identityDto.getSociety().getCode(), MainApp.getFinancialYear().getStartDate(), MainApp.getFinancialYear().getEndDate(), getLocaleString());
        profitLossTask.setOnSucceeded(e -> {
            try {
                listPLIncome = profitLossTask.get();
                listPLExpense = profitLossTask.get();
                listPLIncome = listPLIncome.stream().filter(p -> p.getIncomeExpense() == 1).collect(Collectors.toList());
                listPLExpense = listPLExpense.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
                loadData();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        profitLossTask.setOnFailed(e -> {
            loadData();
        });
        new Thread(profitLossTask).start();
    }
}
