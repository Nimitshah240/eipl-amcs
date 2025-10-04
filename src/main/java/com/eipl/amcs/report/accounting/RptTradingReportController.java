package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;


import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.task.StockValuationTask;
import com.eipl.amcs.report.task.TradingTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class RptTradingReportController implements MyInitialization {
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

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnGenerate.setOnAction(e -> {
            loadData();
        });
    }

    @Override
    public void loadData() {

        StockValuationTask task = new StockValuationTask(MainApp.identityDto.getSociety().getCode(), MainApp.getFinancialYear().getEndDate(), MainApp.locale);
        task.setOnSucceeded(e -> {
            try {
                List<ProductStockValuation> list = task.get();
                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_as_on_date", MainApp.getFinancialYear().getEndDate());
                params.put("p_locale", MainApp.locale);

                double stockValuationTask = list.stream().mapToDouble(m -> m.getValuation()).sum();

                TradingTask tradingTask = new TradingTask(MainApp.identityDto.getSociety().getCode(), dpFromDate.getValue(), dpToDate.getValue(), MainApp.locale);
                tradingTask.setOnSucceeded(ee -> {
                    try {
                        List<LedgerBalance> listtradingTask = tradingTask.get();
                        Map<String, Object> TradingTaskparams = new HashMap<>();
                        TradingTaskparams.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                        if (MainApp.locale.equals("en")) {
                            TradingTaskparams.put("p_society_name", MainApp.identityDto.getSociety().getName());
                        } else {
                            TradingTaskparams.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName(): MainApp.identityDto.getSociety().getNameLocal());
                        }
                        TradingTaskparams.put("p_from_date", dpFromDate.getValue());
                        TradingTaskparams.put("p_to_date", dpToDate.getValue());


                        TradingTaskparams.put("p_locale", MainApp.locale);
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
}
