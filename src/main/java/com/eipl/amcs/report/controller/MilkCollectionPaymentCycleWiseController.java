package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberConvertor;
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
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MilkCollectionPaymentCycleWiseController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate1, btnClose1, btnGenerate11, btnClose11;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private Label lblMonth;
    @FXML
    private DatePicker dpFromDate1, dpToDate1, dpFromDate11, dpToDate11;
    @FXML
    private AutoSearchTextField<Member> cboxMember;
    @FXML
    private AutoSearchTextField<String> cboxPeriod, cboxPeriod1;
    @FXML
    private AutoSearchTextField<String> cboxLanguage1, cboxLanguage11;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        dpFromDate11.setValue(LocalDate.now());
        dpToDate11.setValue(LocalDate.now());

        loadData();
        loadStaff();
        setupComboBox();
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        btnGenerate11.setOnAction(e -> validateAndGenerateReport2());
        btnClose1.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnClose11.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        cboxPeriod.getItems().addAll("૧૦-દિવસ", "૧૫-દિવસ");
        cboxPeriod1.getItems().addAll("૧૦-દિવસ", "૧૫-દિવસ");
        cboxPeriod.getSelectionModel().select(0);
        cboxPeriod1.getSelectionModel().select(0);

        cboxPeriod.setOnAction(e -> {

        });
        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage1.setValue("English");
        }


    cboxPeriod1.setOnAction(e -> {

    });
    String[] arr1 = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage11.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
        cboxLanguage11.setValue("Gujarati");
    } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
        cboxLanguage11.setValue("Hindi");
    } else {
        cboxLanguage11.setValue("English");
    }
}


    private String getLocaleString1() {
        return cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private String getLocaleString2() {
        return cboxLanguage11.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }


    private void loadStaff() {
        var task = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Member> list = task.get();
                if (list != null) {
                    List<Member> list2 = new ArrayList<>();
                    Member m = new Member();
                    m.setCode("0");
                    m.setCodeEx("0");
                    m.setFirstName(resourceBundle.getString("all"));
                    list2.add(m);
                    list2.addAll(list);
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
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
                    cboxMember.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxMember);
//                    cboxMember.setConverter(new MemberConvertor(cboxMember));
                    cboxMember.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
//    private void validateAndGenerateReport1() {
//        try {
//            Map<String, Object> params = new HashMap<>();
//            String localeStr = getLocaleString1();
//            params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
//            params.put("p_locale", localeStr);
//            JasperPrint print = null;
//            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
//            params.put("p_from_collection_date", java.sql.Date.valueOf(dpFromDate1.getValue()) + " 06:00:00");
//            params.put("p_to_collection_date", java.sql.Date.valueOf(dpToDate1.getValue()) + " 18:00:00");
//            params.put("p_member_code", cboxMember.getValue().getCode());
//            print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PURCHASE_REGISTER_MONTH_WISE, params);
//            JasperViewer.viewReport(print, false);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
private void validateAndGenerateReport1() {
    try {
        Map<String, Object> params = new HashMap<>();

        String localeStr = getLocaleString1();
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        params.put("p_locale", localeStr);

        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate1.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate1.getValue()));
        params.put("p_member_code", cboxMember.getValue().getCode());

        JasperPrint print = null;

        switch (cboxPeriod.getSelectionModel().getSelectedIndex()) {

            case 0: // 10 Days
                print = ReportGenerate.getReportDataSourceJasperPrint(
                        AppConstant.ReportPath.PURCHASE_REGISTER_MONTH_WISE,
                        params);
                break;

            case 1: // 15 Days
                print = ReportGenerate.getReportDataSourceJasperPrint(
                        AppConstant.ReportPath.PURCHASE_REGISTER_MONTH_WISE2,
                        params);
                break;
        }

        if (print != null) {
            JasperViewer.viewReport(print, false);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void validateAndGenerateReport2() {
        try {
            Map<String, Object> params = new HashMap<>();

            String localeStr = getLocaleString2();
            params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
            params.put("p_locale", localeStr);

            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
            params.put("p_from_date", java.sql.Date.valueOf(dpFromDate11.getValue()));
            params.put("p_to_date", java.sql.Date.valueOf(dpToDate11.getValue()));
            JasperPrint print = null;

            switch (cboxPeriod1.getSelectionModel().getSelectedIndex()) {

                case 0: // 10 Days
                    print = ReportGenerate.getReportDataSourceJasperPrint(
                            AppConstant.ReportPath.PURCHASE_REGISTER_MONTH_WISE_DCS,
                            params);
                    break;

                case 1: // 15 Days
                    print = ReportGenerate.getReportDataSourceJasperPrint(
                            AppConstant.ReportPath.PURCHASE_REGISTER_MONTH_WISE2_DCS,
                            params);
                    break;
            }

            if (print != null) {
                JasperViewer.viewReport(print, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
