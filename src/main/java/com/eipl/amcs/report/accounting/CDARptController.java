package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberConvertor;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CDARptController implements MyInitialization {
    @FXML
    private Button btnClose;

    @FXML
    private Button btnGenerate;

    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    ComboBox<Shift> cboxShiftFrom, cboxShiftTo;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    private List<MilkType> listMilkType;
    @FXML
    private SwingNode reportNode;
    @FXML
    private ComboBox cboxQuantityMode;
    @FXML
    private ComboBox<String> cboxType1, cboxMode, cboxType2;

    @FXML
    private StackPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadShift();
        setupComboBox();
        loadData();
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        btnGenerate.setOnAction(e -> {
            validateAndGenerateReport();
        });


        List<String> list = new ArrayList<>();
        list.add("Litre");
        list.add("Kg");
        cboxQuantityMode.setItems(FXCollections.observableList(list));
        cboxQuantityMode.getSelectionModel().select(0);
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

    }

    @Override
    public void setupComboBox() {

        cboxType1.getItems().addAll("With MilkType", "WithOut MilkType");
        cboxType1.getSelectionModel().select(0);
        cboxMode.getItems().addAll("%Loss", "Kg Loss");
        cboxMode.getSelectionModel().select(0);
        cboxType2.getItems().addAll("Date Wise", "Date & Shift Wise", "Dispatch Wise");
        cboxType2.getSelectionModel().select(0);
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        if (MainApp.locale == "en") {
            params.put("society_code", MainApp.identityDto.getSociety().getName() + " -(" + MainApp.identityDto.getSociety().getCode() + ")");
            params.put("union_code", MainApp.identityDto.getUnion().getName() + " -(" + MainApp.identityDto.getUnion().getCode() + ")");
        } else {
            params.put("society_code", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal() + " -(" + MainApp.identityDto.getSociety().getCode() + ")");
            params.put("union_code", MainApp.identityDto.getUnion().getNameLocal() == null ? MainApp.identityDto.getUnion().getName() : MainApp.identityDto.getUnion().getNameLocal() + " -(" + MainApp.identityDto.getUnion().getCode() + ")");
        }
        params.put("from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-YY")) + " " + (cboxShiftFrom.getValue().getName().equals("Morning")?"- M":"- E"));
        params.put("to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-YY")) + " " + (cboxShiftTo.getValue().getName().equals("Morning")?"- M":"- E"));
        params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShiftFrom.getValue().getName().equals("Morning")?"06:00:00":"18:00:00"));
        params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxShiftTo.getValue().getName().equals("Morning")?"06:00:00":"18:00:00"));
        params.put("p_ltr_kg", cboxQuantityMode.getSelectionModel().getSelectedIndex());
        params.put("p_milk_type", cboxMilkType.getValue().getCode());
        params.put("p_locale", MainApp.locale);
        String rpt = "";
        if (cboxType2.getSelectionModel().getSelectedIndex() == 2) {
            JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RtpMilkCollectionProfitloss, params);
            JasperViewer.viewReport(print, false);
        } else if (cboxMode.getSelectionModel().getSelectedIndex() == 0) {
            if (cboxType1.getSelectionModel().getSelectedIndex() == 0) {
                if (cboxType2.getSelectionModel().getSelectedIndex() == 0) {
                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDADateWiseWithMilkType, params);
                    JasperViewer.viewReport(print, false);
                } else {

                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDAShiftWiseWithMilkType, params);
                    JasperViewer.viewReport(print, false);
                }
            } else {
                if (cboxType2.getSelectionModel().getSelectedIndex() == 0) {

                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDADateWiseWithoutMilkType, params);
                    JasperViewer.viewReport(print, false);
                } else {

                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDAShiftWiseWithoutMilkType, params);
                    JasperViewer.viewReport(print, false);
                }
            }
        } else {
            if (cboxType1.getSelectionModel().getSelectedIndex() == 0) {
                if (cboxType2.getSelectionModel().getSelectedIndex() == 0) {

                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDAKgFatKgSNFDateWiseWithMilkType, params);
                    JasperViewer.viewReport(print, false);
                } else {

                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDAKgFatKgSNFShiftWiseWithMilkType, params);
                    JasperViewer.viewReport(print, false);
                }
            } else {
                if (cboxType2.getSelectionModel().getSelectedIndex() == 0) {

                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDAKgFatKgSNFDateWiseWithoutMilkType, params);
                    JasperViewer.viewReport(print, false);
                } else {

                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RptCDAKgFatKgSNFShiftWiseWithoutMilkType, params);
                    JasperViewer.viewReport(print, false);
                }
            }
        }

    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    cboxShiftFrom.setItems(FXCollections.observableList(list1));
                    cboxShiftTo.setItems(FXCollections.observableList(list1));
                    cboxShiftFrom.getSelectionModel().select(0);
                    cboxShiftTo.getSelectionModel().select(list1.size() - 1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
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
                    cboxMilkType.setItems(FXCollections.observableList(listMilkType));
                    cboxMilkType.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }


}