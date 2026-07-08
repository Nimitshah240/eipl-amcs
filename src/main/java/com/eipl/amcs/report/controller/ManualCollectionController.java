package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.org.convertor.DockConvertor;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.task.DockLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
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

public class ManualCollectionController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate21, btnClose21;
    @FXML
    private DatePicker dpToDate11, dpFromDate11;
    @FXML
    private ComboBox<Dock> cboxDock1;
    @FXML
    private ComboBox<String> cboxLanguage3;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
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
        btnClose21.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate21.setOnAction(e -> validateAndGenerateReport3());

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage3.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage3.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage3.setValue("Hindi");
        } else {
            cboxLanguage3.setValue("English");
        }
    }

    @Override
    public void setupComboBox() {
        cboxDock1.setConverter(new DockConvertor(cboxDock1));
    }

    private void validateAndGenerateReport3() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        if (cboxDock1.getValue().getDockNo().equals("All"))
            params.put("p_dock_no", "0");
        else
            params.put("p_dock_no", cboxDock1.getValue().getDockNo());
        params.put("p_from_date", dpFromDate11.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("p_to_date", dpToDate11.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String localeStr = getLocaleString3();
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));

        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MANUAL_COLLECTION_REPORT, params);
        JasperViewer.viewReport(print, false);
    }

    private String getLocaleString3() {
        return cboxLanguage3.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    @Override
    public void loadData() {
        var task2 = new DockLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<DockMilkTypeDto> list = task2.get();
                if (list != null) {
                    List<Dock> listDock = list.stream()
                            .map(m -> m.getDock()).collect(Collectors.toList());
                    if (listDock.size() == 1) {
                        cboxDock1.setItems(FXCollections.observableList(listDock));
                        cboxDock1.getSelectionModel().select(0);
                    } else {
                        listDock.add(0, new Dock("All"));
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
