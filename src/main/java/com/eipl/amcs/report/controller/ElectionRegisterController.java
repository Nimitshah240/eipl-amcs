package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.org.convertor.DockConvertor;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ElectionRegisterController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    @FXML
    private DatePicker dpToDate,dpFromDate;
    @FXML
    private ComboBox<MilkType> cboxReportType;

    @FXML
    private ComboBox<String>  cboxQtyAmount;
    @FXML
    private ComboBox<Shift>  cboxFromShift,cboxToShift;
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
            if (!newValue){
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setValue(LocalDate.now());
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
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
    }

    @Override
    public void setupComboBox() {
        cboxReportType.setConverter(new MilkTypeConvertor(cboxReportType));
    }


    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_animal_type",cboxReportType.getValue().getCode());
        params.put("p_limit", Integer.parseInt(txtLimit.getText()));
        params.put("p_qty_amount", cboxQtyAmount.getSelectionModel().getSelectedIndex()==0?1:2);
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
       if(cboxReportType.getValue().getName().equalsIgnoreCase(MainApp.bundle.getString("all"))){
           print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.ELECTION_REGISTER, params);
       }else {
           print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.ELECTION_REGISTER_MILK_TYPE, params);
       }

        JasperViewer.viewReport(print, false);
    }

    private boolean validate() {
        return true;
    }


    public void loadData() {
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null&&!list.isEmpty()) {
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
                    cboxFromShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxToShift.getSelectionModel().select(1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }
}
