package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.task.LoadStockValuationTask;
import com.eipl.amcs.report.task.TradingTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
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
    @FXML
    private ComboBox<String> cboxLanguage;

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
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_as_on_date", MainApp.getFinancialYear().getEndDate());
                params.put("p_locale", localeStr);
                params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));

                double stockValuationTask = list.stream().mapToDouble(m -> m.getValuation()).sum();

                TradingTask tradingTask = new TradingTask(MainApp.identityDto.getSociety().getCode(), dpFromDate.getValue(), dpToDate.getValue(), localeStr);
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
                        TradingTaskparams.put("p_from_date", dpFromDate.getValue());
                        TradingTaskparams.put("p_to_date", dpToDate.getValue());


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
}
