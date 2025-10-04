package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.operation.convertor.BillHeadCellFactory;
import com.eipl.amcs.master.operation.convertor.BillHeadConvertor;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.BillHeadLoadTask;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
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
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MemberBillHeadController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate;
    @FXML
    private DatePicker dpToDate, dpFromDate;
    @FXML
    private ComboBox<Member> cboxMemberCode;
    @FXML
    private ComboBox<BillHead> cboxHeadType;
    @FXML
    private ComboBox<String> cboxReportType;

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

        loadData();
        setupComboBox();

        btnGenerate.setOnAction(e -> validateAndGenerateReport());
    }

    @Override
    public void setupComboBox() {
        cboxMemberCode.setConverter(new MemberReportConvertor(cboxMemberCode));
        cboxMemberCode.setCellFactory(new MemberCellFactory());
        cboxMemberCode.getSelectionModel().select(0);

        cboxHeadType.setConverter(new BillHeadConvertor(cboxHeadType));
        cboxHeadType.setCellFactory(new BillHeadCellFactory());


        cboxReportType.getItems().addAll("Summary", "Consolidate");
        cboxReportType.getSelectionModel().select(0);
    }

    private StringBuilder errorMsg;

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_member_code", cboxMemberCode.getValue().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_head_code", cboxHeadType.getValue().getCode());
        params.put("p_report_type", cboxReportType.getSelectionModel().getSelectedIndex() + 1);
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        switch (cboxReportType.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_BILL_HEAD_DETAIL, params);
                break;
            case 2:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_BILL_HEAD_SUMMARY, params);
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
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        BillHeadLoadTask task1 = new BillHeadLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<BillHead> list = task1.get();
                if (list != null) {
                    List<BillHead> list1 = new ArrayList<>();
                    for (BillHead billHead : list) {
                        if (billHead.getCode().equals("101") || billHead.getCode().equals("105"))
                            continue;
                        list1.add(billHead);
                    }
                    cboxHeadType.setItems(FXCollections.observableList(list1));
                    new AutoCompleteComboBoxListener<>(cboxHeadType);
                    cboxHeadType.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

}
