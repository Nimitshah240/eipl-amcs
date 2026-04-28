package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import net.sf.jasperreports.engine.JRParameter;

public class MemberWiseLocalMilkSaleController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate;
    @FXML
    private DatePicker dpToDate, dpFromDate;
    @FXML
    private ComboBox<String> cboxReportType, cboxLanguage;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;

    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

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
        loadData();
        setupComboBox();

        btnGenerate.setOnAction(e -> validateAndGenerateReport());

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

    @Override
    public void setupComboBox() {
        cboxReportType.getItems().addAll("Date & Shift Wise", "Date Wise");
        cboxReportType.getSelectionModel().select(0);
    }
    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_report_type", cboxReportType.getSelectionModel().getSelectedIndex() + 1);
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;
        switch (cboxReportType.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_LOCAL_MILK_SALE_DATE_SHIFT_WISE, params);
                break;
            case 2:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_LOCAL_MILK_SALE_DATE_WISE, params);
                break;
        }
        JasperViewer.viewReport(print, false);

    }

    @Override
    public void loadData() {
        var task1 = new ShiftLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Shift> list = task1.get();
                if (list != null) {
                    cboxFromShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxToShift.getSelectionModel().select(1);

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }
}
