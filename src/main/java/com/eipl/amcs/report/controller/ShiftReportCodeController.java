package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
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
    private Button btnGenerate, btnClose;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<Shift> cboxShift;
    @FXML
    private ComboBox<String> cboxReportType;
    @FXML
    private ComboBox<Dock> cboxDock;
    @FXML
    private ComboBox<String> cboxLanguage;

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
        loadData();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        cboxReportType.getItems().addAll(resourceBundle.getString("codewise"), resourceBundle.getString("memberwise"), resourceBundle.getString("timewise"));
        cboxReportType.getSelectionModel().select(0);
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });

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
        cboxDock.setConverter(new DockConvertor(cboxDock));
    }


    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_collection_date", dpDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        if (cboxDock.getValue().getDockNo().equals("All"))
            params.put("p_dock_no", "0");
        else
            params.put("p_dock_no", cboxDock.getValue().getDockNo());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;
        switch (cboxReportType.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SHIFT_REPORT_CODEWISE, params);
                break;
            case 2:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SHIFT_REPORT_MEMBERWISE, params);
                break;
            case 3:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SHIFT_REPORT_TIMEWISE, params);
                break;
        }
        JasperViewer.viewReport(print, false);
    }


    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
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
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task4).start();
    }
}
