package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.master.operation.convertor.LedgerCellFactory;
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
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class RptLedgerBookController implements MyInitialization {

    @FXML
    private Button btnGenerate, btnGenerate1, btnClose,btnGenerateSubLedger;
    @FXML
    private ComboBox<Ledger> cboxLedgerName;
    @FXML
    private DatePicker dpFromDate, dpToDate, dpFromDate1, dpToDate1;
    @FXML
    private SwingNode reportNode;
    @FXML
    private AnchorPane root;
    @FXML
    private ComboBox<String> cboxLanguage, cboxLanguage1;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        loadData();
        setupComboBox();
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnGenerateSubLedger.setOnAction(e -> validateAndGenerateReport2());
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());

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
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage1.setValue("English");
        }
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        if (localeStr.equals("en")) {
            params.put("p_society_name", MainApp.identityDto.getSociety().getName());
        } else {
            params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
        }
        params.put("p_ledger_code", cboxLedgerName.getValue().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RPT_LEDGER_BOOK_SUB_LEDGER, params);
        JasperViewer.viewReport(print, false);
    }
    private void validateAndGenerateReport2() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        if (localeStr.equals("en")) {
            params.put("p_society_name", MainApp.identityDto.getSociety().getName());
        } else {
            params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
        }
        params.put("p_ledger_code", cboxLedgerName.getValue().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RPT_LEDGER_BOOK, params);
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReport1() {
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

    @Override
    public void setupComboBox() {
        cboxLedgerName.setConverter(new LedgerConvertor(cboxLedgerName));
        cboxLedgerName.setCellFactory(new LedgerCellFactory());
        cboxLedgerName.getSelectionModel().select(0);
    }

    @Override
    public void loadData() {
        LedgerLoadTask task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Ledger> list = task.get();
                if (list != null) {
                    List<Ledger> list2 = new ArrayList<>();
                    Ledger m = new Ledger();
                    m.setCode("0");
                    m.setName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxLedgerName.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxLedgerName);
                    cboxLedgerName.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
