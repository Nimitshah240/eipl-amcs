package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
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
    private Button btnGenerate, btnClose, btnGenerate2, btnClose2;
    @FXML
    private E_DatePicker dpFromDate, dpToDate,dpToDate1, dpFromDate1;
    @FXML
    private AutoSearchTextField<Shift> cboxFromShift, cboxToShift, cboxFromShift1, cboxToShift1;
     @FXML
    private AutoSearchTextField<Member> cboxMember1;
    @FXML
    private AutoSearchTextField<MilkType> cboxMilkType1;
    @FXML
    private AutoSearchTextField<String> cboxLanguage, cboxLanguage2;

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
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        dpFromDate1.setConverter(new LocalDateConvertor());
        dpFromDate1.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate1.setValue(dpFromDate1.getConverter().fromString(dpFromDate1.getEditor().getText()));
            }
        });
        dpToDate1.setConverter(new LocalDateConvertor());
        dpToDate1.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate1.setValue(dpToDate1.getConverter().fromString(dpToDate1.getEditor().getText()));
            }
        });
        loadData();
        loadShift();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose2.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate2.setOnAction(e -> validateAndGenerateReport2());

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLanguage2.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage.setValue("Gujarati");
            cboxLanguage2.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage.setValue("Hindi");
            cboxLanguage2.setValue("Hindi");

        } else {
            cboxLanguage.setValue("English");
            cboxLanguage2.setValue("English");
        }

    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
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

    private void validateAndGenerateReport2() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString2();
        params.put("p_member_code", cboxMember1.getValue().getCode());
        params.put("p_from_date", dpFromDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_date", dpToDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_milk_type_code", cboxMilkType1.getValue().getCode());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.EDIT_COLLECTION_REPORT, params);
        JasperViewer.viewReport(print, false);
    }

    private String getLocaleString2() {
        return cboxLanguage2.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
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
                cboxFromShift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                cboxFromShift1.getSelectionModel().select(0);
                cboxToShift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                cboxToShift1.getSelectionModel().select(1);
//                cboxFromShift1.setConverter(new ShiftConvertor(cboxFromShift1));
//                cboxToShift1.setConverter(new ShiftConvertor(cboxToShift1));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task2 = new MilkTypeLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task2.get();
                if (list != null) {
                    MilkType milkType = new MilkType();
                    milkType.setCode(0);
                    milkType.setName("All");
                    List<MilkType> temp = new ArrayList<>();
                    temp.add(0, milkType);
                    temp.addAll(list);
                    cboxMilkType1.setItems(FXCollections.observableList(temp));
                    cboxMilkType1.getSelectionModel().select(0);
//                    cboxMilkType1.setConverter(new MilkTypeConvertor(cboxMilkType1));
                    loadData1();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    public void loadData1() {
        MemberLoadTask task4 = new MemberLoadTask();
        task4.setOnSucceeded(e -> {
            try {
                List<Member> list = task4.get();
                if (list != null) {
                    List<Member> list2 = new ArrayList<>();
                    Member m = new Member();
                    m.setCode("0");
                    m.setCodeEx("0");
                    m.setFirstName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxMember1.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxMember1);
                    cboxMember1.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task4).start();
    }
}