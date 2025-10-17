package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
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

public class MemberWiseConsolidateCollectionController implements MyInitialization {

    @FXML
    ComboBox<String> cboxType, cboxType1;
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnGenerate1;
    @FXML
    private ComboBox<Member> cboxMemberCode, cboxMemberCode1;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift, cboxFromShift1, cboxToShift1;
    @FXML
    private DatePicker dpFromDate, dpToDate, dpFromDate1, dpToDate1;
    @FXML
    private ComboBox<MilkType> cboxMilkType, cboxMilkType1;
    private List<MilkType> listMilkType;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboxMilkType.setDisable(true);
        cboxMilkType1.setDisable(true);

        this.resourceBundle = resourceBundle;
        loadData();
        setupComboBox();
        cboxType.getItems().addAll("Member Wise", "Member and MIlkType Wise", "Member Code Wise");
        cboxType.getSelectionModel().select(0);
        cboxType.setOnAction(e -> {
            cboxMilkType.setDisable(cboxType.getSelectionModel().getSelectedIndex() != 1);

        });
        cboxType1.getItems().addAll("Member Wise", "Member and MIlkType Wise", "Member Code Wise");
        cboxType1.getSelectionModel().select(0);
        cboxType1.setOnAction(e -> {
            cboxMilkType1.setDisable(cboxType1.getSelectionModel().getSelectedIndex() != 1);

        });

        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
    }

    @Override
    public void setupComboBox() {
        cboxMemberCode.setConverter(new MemberReportConvertor(cboxMemberCode));
        cboxMemberCode.setCellFactory(new MemberCellFactory());
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxMemberCode1.setConverter(new MemberReportConvertor(cboxMemberCode1));
        cboxMemberCode1.setCellFactory(new MemberCellFactory());
        cboxMilkType1.setConverter(new MilkTypeConvertor(cboxMilkType1));
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        cboxFromShift1.setConverter(new ShiftConvertor(cboxFromShift1));
        cboxToShift1.setConverter(new ShiftConvertor(cboxToShift1));
    }

    private void validateAndGenerateReport() {
        //(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date date,IN p_to_date date,IN p_milk_type_code INT,IN p_locale VARCHAR(20))
        Map<String, Object> params = new HashMap<>();
        JasperPrint print = null;
        switch (cboxType.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_member_code", cboxMemberCode.getValue().getCode());
                params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_locale", MainApp.locale);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION, params);
                break;
            case 2:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_member_code", cboxMemberCode.getValue().getCode());
                params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_milk_type_code", cboxMilkType.getValue().getCode());
                params.put("p_locale", MainApp.locale);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_WISE_MILK_TYPE_WISE_CONSOLIDATE_COLLECTION, params);
                break;
            case 3:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_member_code", cboxMemberCode.getValue().getCode());
                params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_locale", MainApp.locale);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION2, params);
                break;
        }
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReport1() {
        //(IN p_society_code varchar(25),IN p_member_code varchar(25),IN p_from_date date,IN p_to_date date,IN p_milk_type_code INT,IN p_locale VARCHAR(20))
        Map<String, Object> params = new HashMap<>();
        JasperPrint print = null;
        switch (cboxType1.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_member_code", cboxMemberCode1.getValue().getCode());
                params.put("p_from_date", dpFromDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_to_date", dpToDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_locale", MainApp.locale);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION_WITH_DEDUCTION, params);
                break;
            case 2:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_member_code", cboxMemberCode1.getValue().getCode());
                params.put("p_from_date", dpFromDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_to_date", dpToDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_milk_type_code", cboxMilkType1.getValue().getCode());
                params.put("p_locale", MainApp.locale);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_WISE_MILK_TYPE_WISE_CONSOLIDATE_COLLECTION_WITH_DEDUCTION, params);
                break;
            case 3:
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                params.put("p_member_code", cboxMemberCode1.getValue().getCode());
                params.put("p_from_date", dpFromDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxFromShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_to_date", dpToDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxToShift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
                params.put("p_locale", MainApp.locale);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION2_WITH_DEDUCTION, params);
                break;
        }
        JasperViewer.viewReport(print, false);
    }

    private boolean validate() {
        return true;
    }

    @Override
    public void loadData() {
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
                    cboxMemberCode.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxMemberCode);
                    cboxMemberCode.getSelectionModel().select(0);
                    cboxMemberCode1.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxMemberCode1);
                    cboxMemberCode1.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

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
                    cboxMilkType1.setItems(FXCollections.observableList(listMilkType));
                    cboxMilkType1.getSelectionModel().select(0);
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
    }

}
