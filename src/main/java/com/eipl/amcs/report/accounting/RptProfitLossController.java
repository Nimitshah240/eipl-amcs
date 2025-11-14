package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.task.ProfitLossTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;
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
    private DatePicker dpFromDate;

    @FXML
    private DatePicker dpToDate;

    @FXML
    private SwingNode reportNode;

    @FXML
    private AnchorPane root;

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
    }

    @Override
    public void loadData() {
        ProfitLossTask profitLossTask = new ProfitLossTask(MainApp.identityDto.getSociety().getCode(),
                dpFromDate.getValue(), dpToDate.getValue(), MainApp.locale);
        profitLossTask.setOnSucceeded(ee -> {
            try {
                List<LedgerBalance> list = profitLossTask.get();
                if (list == null) {
                    list = Collections.emptyList();
                }
                Map<String, Object> param = new HashMap<>();
                param.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (MainApp.locale.equals("en")) {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    param.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                param.put("p_from_date", dpFromDate.getValue());
                param.put("p_to_date", dpToDate.getValue());
                param.put("p_locale", MainApp.locale);
//                listPLIncome = list.stream().filter(p -> p.getIncomeExpense() == 1).collect(Collectors.toList());
                listPLIncome = list.stream().filter(p -> p != null && p.getIncomeExpense() == 1).collect(Collectors.toList());
                param.put("p_imcome_side", listPLIncome);
//                listPLExpense = list.stream().filter(p -> p.getIncomeExpense() == 0).collect(Collectors.toList());
                listPLExpense = list.stream().filter(p -> p != null && p.getIncomeExpense() == 0).collect(Collectors.toList());
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

                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_PROFIT_LOSS_REPORT, param, new JREmptyDataSource());
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(profitLossTask).start();
    }
}
