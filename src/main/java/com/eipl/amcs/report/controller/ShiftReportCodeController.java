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
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.org.convertor.DockConvertor;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.task.DockLoadTask;
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
import java.util.stream.Collectors;

public class ShiftReportCodeController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose, btnGenerate1, btnGenerate2, btnClose1, btnClose2, btnGenerate21, btnClose21;
    @FXML
    private DatePicker dpDate, dpToDate, dpFromDate, dpToDate1, dpFromDate1, dpToDate11, dpFromDate11;
    @FXML
    private ComboBox<Shift> cboxShift, cboxFromShift, cboxToShift, cboxFromShift1, cboxToShift1;
    @FXML
    private ComboBox<Member> cboxMember,cboxMember1;
    @FXML
    private ComboBox<MilkType> cboxMilkType,cboxMilkType1;

    @FXML
    private ComboBox<String> cboxReportType;
    @FXML
    private ComboBox<Dock> cboxDock, cboxDock1;
    @FXML
    private ComboBox<String> cboxLanguage2, cboxLanguage3;


    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpDate.setValue(LocalDate.now());
        dpDate.setConverter(new LocalDateConvertor());
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });
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

        dpFromDate11.setValue(LocalDate.now());
        dpToDate11.setValue(LocalDate.now());
        dpFromDate11.setConverter(new LocalDateConvertor());
        dpFromDate11.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate11.setValue(dpFromDate11.getConverter().fromString(dpFromDate11.getEditor().getText()));
            }
        });
        dpToDate11.setConverter(new LocalDateConvertor());
        dpToDate11.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate11.setValue(dpToDate11.getConverter().fromString(dpToDate11.getEditor().getText()));
            }
        });


        loadData();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnClose1.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnClose2.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnClose21.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        btnGenerate2.setOnAction(e -> validateAndGenerateReport2());
        btnGenerate21.setOnAction(e -> validateAndGenerateReport3());

        cboxReportType.getItems().addAll(resourceBundle.getString("codewise"), resourceBundle.getString("memberwise"));
        cboxReportType.getSelectionModel().select(0);
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage2.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLanguage3.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage2.setValue("Gujarati");
            cboxLanguage3.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage2.setValue("Hindi");
            cboxLanguage3.setValue("Hindi");
        } else {
            cboxLanguage2.setValue("English");
            cboxLanguage3.setValue("English");
        }
    }

    @Override
    public void setupComboBox() {
        cboxDock.setConverter(new DockConvertor(cboxDock));
        cboxDock1.setConverter(new DockConvertor(cboxDock1));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMember.setConverter(new MemberReportConvertor(cboxMember));
        cboxMember.setCellFactory(new MemberCellFactory());
        cboxFromShift1.setConverter(new ShiftConvertor(cboxFromShift1));
        cboxToShift1.setConverter(new ShiftConvertor(cboxToShift1));
        cboxMilkType1.setConverter(new MilkTypeConvertor(cboxMilkType1));
        cboxMember1.setConverter(new MemberReportConvertor(cboxMember1));
        cboxMember1.setCellFactory(new MemberCellFactory());
    }


    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_collection_date", dpDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        if (cboxDock.getValue().getDockNo().equals("All"))
            params.put("p_dock_no", "0");
        else
            params.put("p_dock_no", cboxDock.getValue().getDockNo());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        switch (cboxReportType.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SHIFT_REPORT_CODEWISE, params);
                break;
            case 2:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SHIFT_REPORT_MEMBERWISE, params);
                break;
        }
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReport1() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_member_code", cboxMember.getValue().getCode());
        params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_milk_type_code", cboxMilkType.getValue().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PRICE_DIFFERENCE, params);
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

    private void validateAndGenerateReport3() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        if (cboxDock.getValue().getDockNo().equals("All"))
            params.put("p_dock_no", "0");
        else
            params.put("p_dock_no", cboxDock.getValue().getDockNo());
        params.put("p_from_date", dpFromDate11.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("p_to_date", dpToDate11.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String localeStr = getLocaleString3();
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));

        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MANUAL_COLLECTION_REPORT, params);
        JasperViewer.viewReport(print, false);
    }

    private String getLocaleString2() {
       return cboxLanguage2.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }
    private String getLocaleString3() {
       return cboxLanguage3.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    @Override
    public void loadData() {
        var task1 = new ShiftLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Shift> list = task1.get();
                if (list != null) {
                    cboxShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxShift.getSelectionModel().select(0);
                    cboxFromShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxToShift.getSelectionModel().select(1);
                    cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
                    cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
                    cboxFromShift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxFromShift1.getSelectionModel().select(0);
                    cboxToShift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxToShift1.getSelectionModel().select(1);
                    cboxFromShift1.setConverter(new ShiftConvertor(cboxFromShift1));
                    cboxToShift1.setConverter(new ShiftConvertor(cboxToShift1));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();


        var task2 = new DockLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<DockMilkTypeDto> list = task2.get();
                if (list != null) {
                    List<Dock> listDock = list.stream()
                            .map(m -> m.getDock()).collect(Collectors.toList());
                    if (listDock.size() == 1) {
                        cboxDock.setItems(FXCollections.observableList(listDock));
                        cboxDock.getSelectionModel().select(0);
                        cboxDock1.setItems(FXCollections.observableList(listDock));
                        cboxDock1.getSelectionModel().select(0);
                    } else {
                        listDock.add(0, new Dock("All"));
                        cboxDock.setItems(FXCollections.observableList(listDock));
                        cboxDock.getSelectionModel().select(0);
                        cboxDock1.setItems(FXCollections.observableList(listDock));
                        cboxDock1.getSelectionModel().select(0);

                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

        var task3 = new MilkTypeLoadTask();
        task3.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task3.get();
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
                    cboxMilkType1.setItems(FXCollections.observableList(temp));
                    cboxMilkType1.getSelectionModel().select(0);
                    cboxMilkType1.setConverter(new MilkTypeConvertor(cboxMilkType1));
                    loadData1();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task3).start();
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
                    cboxMember.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxMember);
                    cboxMember.getSelectionModel().select(0);
                    cboxMember1.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxMember1);
                    cboxMember1.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task4).start();
    }
}
