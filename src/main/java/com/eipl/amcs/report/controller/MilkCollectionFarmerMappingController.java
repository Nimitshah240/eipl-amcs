package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
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

public class MilkCollectionFarmerMappingController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate12, btnClose12;
    @FXML
    private AutoSearchTextField<String> cboxReportType;
    @FXML
    private AutoSearchTextField<String> cboxLanguage12;

    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        loadData();
        setupComboBox();

        cboxReportType.setItems(FXCollections.observableArrayList("બધા", "મુખ્ય", "પેટા"));
        cboxReportType.getSelectionModel().selectFirst();
        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
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


    private String getMemberRegisterFarmerMappingLocaleString() {
        return cboxLanguage12.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase(); // Corrected fx:id
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
            case "બધા":
                reportTypeInt = 0;
                break;
            case "મુખ્ય":
                reportTypeInt = 1;
                break;
            case "પેટા":
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

}