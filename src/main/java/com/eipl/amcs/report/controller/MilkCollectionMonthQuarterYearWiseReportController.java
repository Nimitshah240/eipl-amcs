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
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MilkCollectionMonthQuarterYearWiseReportController implements MyInitialization {

    String[] month = {"All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String[] quarter = {"All", "1", "2", "3", "4"};
    String[] year = {"All", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private ComboBox<Member> cboxStaff;
    @FXML
    private Label lblMonth;
    @FXML
    private ComboBox<String> cboxType;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    private ResourceBundle resourceBundle;
    private final MemberBillSummary dto = null;
    private StringBuilder errorMsg;

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
//        cboxStaff.getSelectionModel().select(0);
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> stage.close());
    }

    @Override
    public void setupComboBox() {
        cboxStaff.setConverter(new MemberReportConvertor(cboxStaff));
        cboxStaff.setCellFactory(new MemberCellFactory());
        cboxStaff.getSelectionModel().select(0);
        new AutoCompleteComboBoxListener<>(cboxStaff);
    }

    private void validateAndGenerateReport() {


        Map<String, Object> params = new HashMap<>();
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
                params.put("p_locale", MainApp.locale);
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
                params.put("p_locale", MainApp.locale);
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
                params.put("p_locale", MainApp.locale);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.Milk_Collection_Year_Wise, params);
                JasperViewer.viewReport(print, false);
                break;


        }

    }

    private boolean validate() {
        return true;
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


}
