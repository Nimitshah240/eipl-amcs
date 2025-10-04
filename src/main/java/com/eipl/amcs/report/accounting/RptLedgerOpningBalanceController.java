package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.FinancialYearConvertor;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.task.FinancialYearLoadTask;
import com.eipl.amcs.master.operation.convertor.LedgerCellFactory;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class RptLedgerOpningBalanceController implements MyInitialization {

    @FXML
    private Button btnGenerate, btnClose;

    @FXML
    private ComboBox<FinancialYear> cboxFinancialYear;

    private List<FinancialYear> financialYearList;

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadData();
        setupComboBox();
        btnGenerate.setOnAction(e -> {
            validateAndGenerateReport();
        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
    }

    @Override
    public void setupComboBox() {
        cboxFinancialYear.setConverter(new FinancialYearConvertor(cboxFinancialYear));
        cboxFinancialYear.getSelectionModel().select(0);
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_financial_year", cboxFinancialYear.getValue().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RPT_LEDGER_OPENING_BALANCE, params);
        JasperViewer.viewReport(print, false);
    }

    public void loadData() {
        var task = new FinancialYearLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<FinancialYear> list = task.get();
                if (list != null) {
                    cboxFinancialYear.getItems().addAll(FXCollections.observableList(list));
                    cboxFinancialYear.getSelectionModel().select(0);
                    new AutoCompleteComboBoxListener<>(cboxFinancialYear);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


}
