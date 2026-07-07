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

public class MilkCollectionPendingListController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate1, btnClose1;
    @FXML
    private DatePicker dpPreviousDate, dpCompareDate;
    @FXML
    private ComboBox<Shift> cboxPreviousShift, cboxCompareShift;
    @FXML
    private ComboBox<String> cboxLanguage1;

    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        loadData();
        loadShift();
        setupComboBox();

        dpPreviousDate.setValue(LocalDate.now());
        dpCompareDate.setValue(LocalDate.now());
        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
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

    }

    @Override
    public void setupComboBox() {

        cboxPreviousShift.setConverter(new ShiftConvertor(cboxPreviousShift));
        cboxCompareShift.setConverter(new ShiftConvertor(cboxCompareShift));
    }

    private String getFarmerPendingListLocaleString() {
        return cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
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

    public void onCloseMemberRegisterFarmerMappingReportClicked() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
    }


    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
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