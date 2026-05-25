package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
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
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MilkCollectionAuditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private ComboBox<String> cboxLanguage;

    @FXML
    private Button btnGenerate1, btnClose1;
    @FXML
    private DatePicker dpPreviousDate, dpCompareDate;
    @FXML
    private ComboBox<Shift> cboxPreviousShift, cboxCompareShift;
    @FXML
    private ComboBox<String> cboxLanguage1;


    @FXML
    private Button btnGenerate12, btnClose12;
    @FXML
    private ComboBox<String> cboxReportType;
    @FXML
    private ComboBox<String> cboxLanguage12;


    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
        loadData();
        loadShift();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
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

        dpPreviousDate.setValue(LocalDate.now());
        dpCompareDate.setValue(LocalDate.now());
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage1.setValue("English");
        }

        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        btnClose1.setOnAction(e -> onCloseFarmerPendingListReportClicked());

        cboxReportType.setItems(FXCollections.observableArrayList("All", "Parent", "Child"));
        cboxReportType.getSelectionModel().selectFirst();
        cboxLanguage12.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage12.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage12.setValue("Hindi");
        } else {
            cboxLanguage12.setValue("English");
        }

        btnGenerate12.setOnAction(e -> validateAndGenerateReport2());
        btnClose12.setOnAction(e -> onCloseMemberRegisterFarmerMappingReportClicked());
    }

    @Override
    public void setupComboBox() {
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxPreviousShift.setConverter(new ShiftConvertor(cboxPreviousShift));
        cboxCompareShift.setConverter(new ShiftConvertor(cboxCompareShift));
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private String getFarmerPendingListLocaleString() {
        return cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private String getMemberRegisterFarmerMappingLocaleString() {
        return cboxLanguage12.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase(); // Corrected fx:id
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_from_date", CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
        params.put("p_to_date", CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()));
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;
        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_COLLECTION_AUDIT, params);
        JasperViewer.viewReport(print, false);
    }

    public void validateAndGenerateReport1() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getFarmerPendingListLocaleString();
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        params.put("p_locale", localeStr);
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        java.time.LocalDateTime previousDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(dpPreviousDate.getValue(), cboxPreviousShift.getValue());
        java.time.LocalDateTime compareDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(dpCompareDate.getValue(), cboxCompareShift.getValue());
        DateTimeFormatter dbDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedPreviousDate = "";
        if (previousDateTime != null) {
            formattedPreviousDate = previousDateTime.format(dbDateTimeFormatter);
        }
        params.put("p_previous_date", formattedPreviousDate);

        String formattedCompareDate = "";
        if (compareDateTime != null) {
            formattedCompareDate = compareDateTime.format(dbDateTimeFormatter);
        }
        params.put("p_compare_date", formattedCompareDate);

        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.FARMER_PENDING_LIST, params);
        JasperViewer.viewReport(print, false);
    }

    public void onCloseFarmerPendingListReportClicked() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
    }

    public void validateAndGenerateReport2() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getMemberRegisterFarmerMappingLocaleString();
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        params.put("p_locale", localeStr);
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());

        int reportTypeInt;
        switch (cboxReportType.getValue()) {
            case "All":
                reportTypeInt = 0;
                break;
            case "Parent":
                reportTypeInt = 1;
                break;
            case "Child":
                reportTypeInt = 2;
                break;
            default:
                reportTypeInt = 0;
        }
        params.put("p_report_type", reportTypeInt);

        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.Farmer_MAPPING, params);
        JasperViewer.viewReport(print, false);
    }

    public void onCloseMemberRegisterFarmerMappingReportClicked() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
    }


    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                cboxFromShift.setItems(FXCollections.observableList(list));
                cboxToShift.setItems(FXCollections.observableList(list));
                cboxToShift.getSelectionModel().select(1);
                cboxFromShift.getSelectionModel().select(0);
                cboxPreviousShift.setItems(FXCollections.observableList(list));
                cboxCompareShift.setItems(FXCollections.observableList(list));
                cboxPreviousShift.getSelectionModel().select(0);
                cboxCompareShift.getSelectionModel().select(1);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}