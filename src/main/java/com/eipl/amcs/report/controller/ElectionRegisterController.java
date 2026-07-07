package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.GenderConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ElectionRegisterController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    @FXML
    private DatePicker dpToDate, dpFromDate;
    @FXML
    private ComboBox<MilkType> cboxReportType;
    @FXML
    private ComboBox<Gender> cboxGender;
    private List<Gender> genderList;
    @FXML
    private ComboBox<String> cboxQtyAmount, cboxLanguage;
    @FXML
    private ComboBox<Shift> cboxFromShift1, cboxToShift1;
    @FXML
    private E_NumericField txtLimit;

    private ResourceBundle resourceBundle;
    private List<MilkType> listMilkType;

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
        txtLimit.setText("10");
        loadData();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        cboxQtyAmount.getItems().addAll(resourceBundle.getString("qty"), resourceBundle.getString("amount"));
        cboxQtyAmount.getSelectionModel().select(0);

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
        cboxReportType.setConverter(new MilkTypeConvertor(cboxReportType));
        cboxGender.setConverter(new GenderConvertor(cboxGender));
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }


    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_from_date", Timestamp.valueOf(dpFromDate.getValue().atTime(cboxFromShift1.getValue().getName().equals("Morning") ? 6 : 18, 0)));
        params.put("p_to_date", Timestamp.valueOf(dpToDate.getValue().atTime(cboxToShift1.getValue().getName().equals("Morning") ? 6 : 18, 0)));
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_animal_type", cboxReportType.getValue().getCode());
        params.put("p_gender_code", cboxGender.getValue().getCode());
        params.put("p_limit", Integer.parseInt(txtLimit.getInputText()));
        params.put("p_qty_amount", cboxQtyAmount.getSelectionModel().getSelectedIndex() == 0 ? 1 : 2);
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;
        if (cboxReportType.getValue().getName().equalsIgnoreCase(MainApp.bundle.getString("all"))) {
            print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.ELECTION_REGISTER, params);
        } else {
            print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.ELECTION_REGISTER_MILK_TYPE, params);
        }

        JasperViewer.viewReport(print, false);
    }
    public void onCloseElectionRegister1ReportClicked() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
    }


    public void loadData() {
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null && !list.isEmpty()) {
                    listMilkType = new ArrayList<>();
                    listMilkType.add(0, new MilkType(0, MainApp.bundle.getString("all")));
                    listMilkType.addAll(list);
                    cboxReportType.setItems(FXCollections.observableList(listMilkType));
                    cboxReportType.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task2 = new ShiftLoadTask();
        task2.setOnSucceeded(e -> {

            try {
                List<Shift> list = task2.get();
                if (list != null) {

                    cboxFromShift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxFromShift1.getSelectionModel().select(0);
                    cboxToShift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxToShift1.getSelectionModel().select(1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

        GenderLoadTask task = new GenderLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Gender> list = task.get();
                if (list != null) {
                    genderList = new ArrayList<>();
                    Gender allGender = new Gender();
                    allGender.setCode(0);
                    allGender.setName(MainApp.bundle.getString("all"));
                    genderList.add(allGender);
                    genderList.addAll(list);
                    cboxGender.setItems(FXCollections.observableList(genderList));
                    cboxGender.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
