package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ShareCancelReportController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;

    @FXML
    private DatePicker dpFromDate, dpToDate;

    public PopupCallback callback;


    private ResourceBundle resourceBundle;


    @Override
    public Node getRoot() {
        return root;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    List<Bonus> bonusList = new ArrayList<>();
    public Bonus bonus;
    public Map<String, Object> map;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> stage.close());
        dpFromDate.setValue(LocalDate.now());
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setValue(LocalDate.now());
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
    }


    @Override
    public void setupComboBox() {

    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", Date.valueOf(dpToDate.getValue()));
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        params.put("p_payment_type", 0);
        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SHARE_CANCEL, params);
        JasperViewer.viewReport(print, false);
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }
}