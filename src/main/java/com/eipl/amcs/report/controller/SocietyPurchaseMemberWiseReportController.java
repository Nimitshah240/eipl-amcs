package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
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
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class SocietyPurchaseMemberWiseReportController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private ComboBox<Member> cboxMember;
    @FXML
    private ComboBox<String>  cboxLanguage1;

    private ResourceBundle resourceBundle;


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
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });



        loadData();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate.setOnAction(e -> validateAndGenerateReport());

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage1.setValue("English");
        }
    }

    private String getLocaleString() {
        return cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }


    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_collection_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_collection_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        params.put("p_milk_type_code", cboxMilkType.getValue().getCode());
        params.put("p_member_code", cboxMember.getValue().getCode());
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SOCIETY_PURCHASE_MEMBER_WISE, params);
        JasperViewer.viewReport(print, false);
    }

    @Override
    public void loadData() {
        var task1 = new ShiftLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Shift> list = task1.get();
                if (list != null) {
                    cboxFromShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxToShift.getSelectionModel().select(1);
                    cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
                    cboxToShift.setConverter(new ShiftConvertor(cboxToShift));

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

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
                    cboxMilkType.setItems(FXCollections.observableList(temp));
                    cboxMilkType.getSelectionModel().select(0);
                    cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
                    loadData1();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();


    }

    @Override
    public void setupComboBox() {
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMember.setConverter(new MemberReportConvertor(cboxMember));
        cboxMember.setCellFactory(new MemberCellFactory());
    }


    public void loadData1() {
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
                    new AutoCompleteComboBoxListener<>(cboxMember);
                    cboxMember.setConverter(new MemberConvertor(cboxMember));
                    cboxMember.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

}
