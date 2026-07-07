package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
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
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class PaymentRegisterController implements MyInitialization {

    @FXML
    private StackPane root;
//    @FXML
//    private Button btnGenerate, btnClose;
    private Stage stage;
    private PopupCallback callback;
//    @FXML
//    private DatePicker dpFromDate, dpToDate;
    @FXML
    private E_TextField txtSampleNo;
    @FXML
    private ComboBox<String> cboxLanguage;

    private ResourceBundle resourceBundle;

//    @FXML
//    private ComboBox<Shift> cboxfromshift, cboxtoshift;

    @FXML
    private Button btnGenerate1, btnClose1;
    @FXML
    private DatePicker dpFromDate1, dpToDate1;
    @FXML
    private ComboBox<Shift> cboxfromshift1, cboxtoshift1;
    @FXML
    private ComboBox<String> cboxLanguage1;
    @FXML
    private ComboBox<String> cboxStatusType, cboxReportType;


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
        loadData();
        setupComboBox();
//        dpFromDate.setValue(LocalDate.now());
//        dpToDate.setValue(LocalDate.now());
//        btnGenerate.setOnAction(e -> validateAndGenerateReport());
//        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage.setValue("Hindi");
        } else {
            cboxLanguage.setValue("English");
        }

        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage1.setValue("English");
        }

        cboxStatusType.setItems(FXCollections.observableArrayList("All", "Active", "InActive"));
        cboxStatusType.getSelectionModel().selectFirst();
        cboxReportType.setItems(FXCollections.observableArrayList("All", "Parent", "Child"));
        cboxReportType.getSelectionModel().selectFirst();
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        btnClose1.setOnAction(e -> onCloseFarmerRegisterReportClicked());
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private String getFarmerRegisterLocaleString() {
        return cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

//    private void validateAndGenerateReport() {
//        try {
//            if (txtSampleNo.getText() == null || txtSampleNo.getText().trim().equals("")) {
//                MyAlert alert = new ErrorAlert(MainApp.getStage(),
//                        resourceBundle.getString("paymentregister"),
//                        resourceBundle.getString("sampleno.cannot.be.null"));
//                alert.createAlert();
//                return;
//            }
//            if (txtSampleNo.getText().length() > 4) {
//                MyAlert alert = new ErrorAlert(MainApp.getStage(),
//                        resourceBundle.getString("paymentregister"),
//                        resourceBundle.getString("samplenovalidation"));
//                alert.createAlert();
//                return;
//            }
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            Map<String, Object> params = new HashMap<>();
//            String localeStr = getLocaleString();
//            params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
//            params.put("p_locale", localeStr);
//            JasperPrint print = null;
//            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
//            params.put("p_member_code", MainApp.identityDto.getSociety().getCode() + String.format("%04d", Integer.parseInt(txtSampleNo.getText().trim())));
//            params.put("p_from_date", dpFromDate.getValue().format(formatter) + " " + (cboxfromshift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
//            params.put("p_to_date", dpToDate.getValue().format(formatter) + " " + (cboxtoshift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
//            print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PAYMENT_REGISTER_WITH_DEDUCTION, params);
//            JasperViewer.viewReport(print, false);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    public void validateAndGenerateReport1() {
        try {
            if (dpFromDate1.getValue() == null) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("farmer_register"),
                        resourceBundle.getString("fromdate.cannot.be.null"));
                alert.createAlert();
                return;
            }
            if (dpToDate1.getValue() == null) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("farmer_register"),
                        resourceBundle.getString("todate.cannot.be.null"));
                alert.createAlert();
                return;
            }
            if (cboxfromshift1.getValue() == null) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("farmer_register"),
                        resourceBundle.getString("fromshift.cannot.be.null"));
                alert.createAlert();
                return;
            }
            if (cboxtoshift1.getValue() == null) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("farmer_register"),
                        resourceBundle.getString("toshift.cannot.be.null"));
                alert.createAlert();
                return;
            }
            if (cboxStatusType.getValue() == null || cboxStatusType.getValue().trim().isEmpty()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("farmer_register"),
                        resourceBundle.getString("status.cannot.be.null"));
                alert.createAlert();
                return;
            }
            if (cboxReportType.getValue() == null || cboxReportType.getValue().trim().isEmpty()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("farmer_register"),
                        resourceBundle.getString("reporttype.cannot.be.null"));
                alert.createAlert();
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Map<String, Object> params = new HashMap<>();
            String localeStr = getFarmerRegisterLocaleString();
            params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
            params.put("p_locale", localeStr);
            JasperPrint print = null;
            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
            params.put("p_from_date", dpFromDate1.getValue().format(formatter) + " " + (cboxfromshift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
            params.put("p_to_date", dpToDate1.getValue().format(formatter) + " " + (cboxtoshift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));

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
            params.put("p_status_type", cboxStatusType.getValue());

            print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.FARMER_REGISTER, params);
            JasperViewer.viewReport(print, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onCloseFarmerRegisterReportClicked() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
    }

    @Override
    public void setupComboBox() {
        try {
//            cboxfromshift.setConverter(new ShiftConvertor(cboxfromshift));
//            cboxtoshift.setConverter(new ShiftConvertor(cboxtoshift));
            cboxfromshift1.setConverter(new ShiftConvertor(cboxfromshift1));
            cboxtoshift1.setConverter(new ShiftConvertor(cboxtoshift1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void loadData() {
        var task1 = new ShiftLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Shift> list = task1.get();
                if (list != null) {
//                    cboxfromshift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
//                    cboxfromshift.getSelectionModel().select(0);
//                    cboxtoshift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
//                    cboxtoshift.getSelectionModel().select(1);
                    cboxfromshift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxfromshift1.getSelectionModel().select(0);
                    cboxtoshift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxtoshift1.getSelectionModel().select(1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }
}