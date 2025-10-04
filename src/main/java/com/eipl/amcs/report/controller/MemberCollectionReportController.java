package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberConvertor;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.report.dto.MemberCollection;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MemberCollectionReportController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose, btnGenerate1, btnClose1;
    @FXML
    private SwingNode reportNode;
    @FXML
    private DatePicker dpFromDate, dpToDate, dpFromDate1, dpToDate1;
    @FXML
    private TextField txtsocietyCode;
    @FXML
    private ComboBox<Member> cboxMemberCode;
    @FXML
    private ComboBox<Shift> cboxfromshift, cboxtoshift, cboxfromshift1, cboxtoshift1;
    @FXML
    private ComboBox cboxqty, cboxqty1;


    private ResourceBundle resourceBundle;
    private MemberCollection memberCollection;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());

        loadData();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnClose1.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        List<String> list = new ArrayList<>();
        list.add("Litre");
        list.add("Kg");
        cboxqty.setItems(FXCollections.observableList(list));
        cboxqty.getSelectionModel().select(0);
        cboxqty1.setItems(FXCollections.observableList(list));
        cboxqty1.getSelectionModel().select(0);
        txtsocietyCode.setText(MainApp.identityDto.getSociety().getCode());
        txtsocietyCode.setDisable(true);

    }

    @Override
    public void setupComboBox() {
        cboxfromshift.setConverter(new ShiftConvertor(cboxfromshift));
        cboxtoshift.setConverter(new ShiftConvertor(cboxtoshift));
        cboxfromshift1.setConverter(new ShiftConvertor(cboxfromshift1));
        cboxtoshift1.setConverter(new ShiftConvertor(cboxtoshift1));
        cboxMemberCode.setConverter(new MemberReportConvertor(cboxMemberCode));
        cboxMemberCode.setCellFactory(new MemberCellFactory());
    }

    private StringBuilder errorMsg;

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_collection_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxfromshift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_collection_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxtoshift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_ltr_kg", cboxqty.getSelectionModel().getSelectedIndex());
        //    params.put("p_milk_type", c.getSelectionModel().getSelectedIndex());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_COLLECTION_SUMMARY, params);
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReport1() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_collection_date", dpFromDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxfromshift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_to_collection_date", dpToDate1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + (cboxtoshift1.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
        params.put("p_ltr_kg", cboxqty1.getSelectionModel().getSelectedIndex());
        params.put("p_member_code", cboxMemberCode.getValue().getCode());
        //    params.put("p_milk_type", c.getSelectionModel().getSelectedIndex());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_COLLECTION_SUMMARY1, params);
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
                    cboxfromshift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxfromshift.getSelectionModel().select(0);
                    cboxtoshift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxtoshift.getSelectionModel().select(1);
                    cboxfromshift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxfromshift1.getSelectionModel().select(0);
                    cboxtoshift1.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxtoshift1.getSelectionModel().select(1);
                    loadData1();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
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
                    cboxMemberCode.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxMemberCode);
                    cboxMemberCode.setConverter(new MemberConvertor(cboxMemberCode));
                    cboxMemberCode.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

}
