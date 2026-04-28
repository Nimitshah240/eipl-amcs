package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
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

public class MilkCollectionMonthQuarterYearWiseReportController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose, btnGenerate1, btnClose1;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private ComboBox<Member> cboxStaff;
    @FXML
    private Label lblMonth;
    @FXML
    private ComboBox<String> cboxType;
    @FXML
    private DatePicker dpFromDate, dpToDate, dpFromDate1, dpToDate1;
    @FXML
    private ComboBox<String> cboxLanguage, cboxLanguage1;
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
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
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
        cboxType.getItems().addAll("Month", "Quarter", "Year");
        cboxType.getSelectionModel().select(0);
        cboxType.setOnAction(e -> {

        });
        loadStaff();
        setupComboBox();
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        btnClose1.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

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

    @Override
    public void setupComboBox() {
        cboxStaff.setConverter(new MemberReportConvertor(cboxStaff));
        cboxStaff.setCellFactory(new MemberCellFactory());
        cboxStaff.getSelectionModel().select(0);
        new AutoCompleteComboBoxListener<>(cboxStaff);
    }
    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private String getLocaleString1() {
        return cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        params.put("p_locale", localeStr);
        JasperPrint print = null;

        switch (cboxType.getSelectionModel().getSelectedIndex()) {
            case 0:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (!cboxStaff.getValue().getFirstName().equalsIgnoreCase(resourceBundle.getString("All"))) {
                    params.put("p_member_type", 1);
                    params.put("p_member_code", cboxStaff.getValue().getCode());
                } else if (cboxStaff.getValue().getFirstName().equalsIgnoreCase(resourceBundle.getString("All"))) {
                    params.put("p_member_type", 2);
                }
                params.put("p_member_code", cboxStaff.getValue().getCode());
                params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
                params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.Milk_Collection_Month_Wise, params);
                JasperViewer.viewReport(print, false);
                break;
            case 1:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (!cboxStaff.getValue().getFirstName().equalsIgnoreCase(resourceBundle.getString("All"))) {
                    params.put("p_member_type", 1);
                    params.put("p_member_code", cboxStaff.getValue().getCode());
                } else if (cboxStaff.getValue().getFirstName().equalsIgnoreCase(resourceBundle.getString("All"))) {
                    params.put("p_member_type", 2);
                }
                params.put("p_member_code", cboxStaff.getValue().getCode());

                params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
                params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.Milk_Collection_Quarter_Wise, params);
                JasperViewer.viewReport(print, false);
                break;
            case 2:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (!cboxStaff.getValue().getFirstName().equalsIgnoreCase(resourceBundle.getString("All"))) {
                    params.put("p_member_type", 1);
                    params.put("p_member_code", cboxStaff.getValue().getCode());
                } else if (cboxStaff.getValue().getFirstName().equalsIgnoreCase(resourceBundle.getString("All"))) {
                    params.put("p_member_type", 2);
                }
                params.put("p_member_code", cboxStaff.getValue().getCode());
                params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
                params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.Milk_Collection_Year_Wise, params);
                JasperViewer.viewReport(print, false);
                break;
        }

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
                    cboxStaff.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxStaff);
                    cboxStaff.getSelectionModel().select(0);
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void validateAndGenerateReport1() {
        try {
            Map<String, Object> params = new HashMap<>();
            String localeStr = getLocaleString1();
            params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
            params.put("p_locale", localeStr);
            JasperPrint print = null;
            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
            params.put("p_from_collection_date", java.sql.Date.valueOf(dpFromDate1.getValue()) + " 06:00:00");
            params.put("p_to_collection_date", java.sql.Date.valueOf(dpToDate1.getValue()) + " 18:00:00");
            print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PURCHASE_REGISTER_MONTH_WISE, params);
            JasperViewer.viewReport(print, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
