package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.report.dto.BalanceSheetRow;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.task.ProfitLossTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

public class RptProfitLossController implements MyInitialization {
    @FXML
    private Button btnClose;

    @FXML
    private Button btnGenerate;

    @FXML
    private E_DatePicker dpFromDate;

    @FXML
    private E_DatePicker dpToDate;

    @FXML
    private SwingNode reportNode;

    @FXML
    private AnchorPane root;
    @FXML
    private AutoSearchTextField<String> cboxLanguage;

    private List<LedgerBalance> listPLExpense, listPLIncome;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        btnGenerate.setOnAction(e -> {
            loadData();
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
        ProfitLossTask profitLossTask = new ProfitLossTask(MainApp.identityDto.getSociety().getCode(),
                dpFromDate.getValue(), dpToDate.getValue(), localeStr);
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
                param.put("p_from_date", dpFromDate.getValue());
                param.put("p_to_date", dpToDate.getValue());
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
}
