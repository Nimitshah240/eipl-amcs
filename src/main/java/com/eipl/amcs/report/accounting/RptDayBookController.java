package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RptDayBookController implements MyInitialization {
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
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        btnGenerate.setOnAction(e -> {
            validateAndGenerateReport();
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RPT_DAY_BOOK, params);
        JasperViewer.viewReport(print, false);
    }
}
