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
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.task.DockLoadTask;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
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
import java.util.stream.Collectors;

public class ShiftReportCodeController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose, btnGenerate1, btnClose1;
    @FXML
    private DatePicker dpDate, dpToDate, dpFromDate;
    @FXML
    private ComboBox<Shift> cboxShift, cboxFromShift, cboxToShift;
    @FXML
    private ComboBox<Member> cboxMember;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    private List<MilkType> listMilkType;

    @FXML
    private ComboBox<String> cboxReportType;
    @FXML
    private ComboBox<Dock> cboxDock;


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
        loadData();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnClose1.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        cboxReportType.getItems().addAll(resourceBundle.getString("codewise"), resourceBundle.getString("memberwise"));
        cboxReportType.getSelectionModel().select(0);
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });
    }

    @Override
    public void setupComboBox() {
        cboxDock.setConverter(new DockConvertor(cboxDock));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMember.setConverter(new MemberReportConvertor(cboxMember));
        cboxMember.setCellFactory(new MemberCellFactory());
    }


    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_collection_date", dpDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
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

    private boolean validate() {
        return true;
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
                    } else {
                        listDock.add(0, new Dock("All"));
                        cboxDock.setItems(FXCollections.observableList(listDock));
                        cboxDock.getSelectionModel().select(0);

                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

//        var task3 = new MilkTypeLoadTask();
//        task3.setOnSucceeded(e -> {
//            try {
//                List<MilkType> list = task3.get();
//                if (list != null && !list.isEmpty()) {
//                    listMilkType = new ArrayList<>();
//                    listMilkType.add(0, new MilkType(0, MainApp.bundle.getString("all")));
//                    listMilkType.addAll(list);
//                    cboxMilkType.setItems(FXCollections.observableList(listMilkType));
//                    cboxMilkType.getSelectionModel().select(0);
//                }
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task3).start();
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
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task4).start();
    }
}
