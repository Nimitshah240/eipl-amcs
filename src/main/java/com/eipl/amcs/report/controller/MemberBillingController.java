package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.Shift;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import net.sf.jasperreports.engine.JRParameter;

public class MemberBillingController implements MyInitialization {

    @FXML
    AutoSearchTextField<Shift> cboxShiftFrom, cboxShiftTo;
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnSummary;
    @FXML
    private E_DatePicker dpToDate, dpFromDate, dpToDateSummary, dpFromDateSummary;
    @FXML
    private AutoSearchTextField<Member> cboxMemberCode, cboxMemberCodeSummary;
    @FXML
    private AutoSearchTextField<String> cboxType;
    private ResourceBundle resourceBundle;
    @FXML
    private AutoSearchTextField<String> cboxLanguage, cboxLanguage1;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadShift();
        loadData();

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

        dpFromDateSummary.setValue(LocalDate.now());
        dpFromDateSummary.setConverter(new LocalDateConvertor());
        dpFromDateSummary.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDateSummary.setValue(dpFromDateSummary.getConverter().fromString(dpFromDateSummary.getEditor().getText()));
            }
        });
        dpToDateSummary.setValue(LocalDate.now());
        dpToDateSummary.setConverter(new LocalDateConvertor());
        dpToDateSummary.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDateSummary.setValue(dpToDateSummary.getConverter().fromString(dpToDateSummary.getEditor().getText()));
            }
        });

        setupComboBox();

        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnSummary.setOnAction(e -> validateAndGenerateReport1());

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage.setValue("Gujarati");
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage.setValue("Hindi");
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage.setValue("English");
            cboxLanguage1.setValue("English");
        }
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private String getLocaleString1() {
        return cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    @Override
    public void setupComboBox() {
//        cboxMemberCode.setConverter(new MemberReportConvertor(cboxMemberCode));
//        cboxMemberCode.setCellFactory(new MemberCellFactory());
//        cboxMemberCodeSummary.setConverter(new MemberReportConvertor(cboxMemberCodeSummary));
//        cboxMemberCodeSummary.setCellFactory(new MemberCellFactory());
        cboxType.getItems().addAll("Month & Year", "Payment Cycle Wise", "Consolidate");
        cboxMemberCode.getSelectionModel().select(0);
        cboxMemberCodeSummary.getSelectionModel().select(0);
        cboxType.getSelectionModel().select(0);
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_member_code", cboxMemberCode.getValue().getCode());
        params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShiftFrom.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShiftTo.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_report_type", cboxType.getSelectionModel().getSelectedIndex() + 1);
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;
        switch (cboxType.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_BILL_MONTH_YEAR, params);
                break;
            case 2:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_BILL_PAYMENT_CYCLE, params);
                break;
            case 3:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_BILL_CONSOLIDATE, params);
                break;
        }
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReport1() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString1();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_member_code", cboxMemberCodeSummary.getValue().getCode());
        params.put("from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-YY")) + " " + (cboxShiftFrom.getValue().getName().equals("Morning") ? "- M" : "- E"));
        params.put("to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-YY")) + " " + (cboxShiftTo.getValue().getName().equals("Morning") ? "- M" : "- E"));
        params.put("p_from_date", dpFromDateSummary.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShiftFrom.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_date", dpToDateSummary.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShiftTo.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SUMMARY_2, params);
        JasperViewer.viewReport(print, false);
    }

    private boolean validate() {
        return true;
    }

    @Override
    public void loadData() {
        MemberLoadTask task = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Member> list = task.get();
                if (list != null) {
                    List<Member> list2 = new ArrayList<>();
                    Member m = new Member();
                    m.setCode("0");
                    m.setCodeEx("0");
                    m.setFirstName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxMemberCode.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxMemberCode);
                    cboxMemberCode.getSelectionModel().select(0);
                    cboxMemberCodeSummary.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxMemberCodeSummary);
                    cboxMemberCodeSummary.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();


    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    cboxShiftFrom.setItems(FXCollections.observableList(list1));
                    cboxShiftTo.setItems(FXCollections.observableList(list1));
                    cboxShiftFrom.getSelectionModel().select(0);
                    cboxShiftTo.getSelectionModel().select(list1.size() - 1);
//                    cboxShiftFrom.setConverter(new ShiftConvertor(cboxShiftFrom));
//                    cboxShiftTo.setConverter(new ShiftConvertor(cboxShiftTo));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
