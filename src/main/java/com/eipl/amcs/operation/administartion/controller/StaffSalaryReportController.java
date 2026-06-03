package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.operation.administartion.task.StaffMembersLoadTask;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class StaffSalaryReportController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private AutoSearchTextField<StaffMember> cboxStaff;
    @FXML
    private ComboBox<String> cboxMonth, cboxYear;

    private ResourceBundle resourceBundle;
    private MemberBillSummary dto = null;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setSummay(MemberBillSummary dto) {
        if (dto != null) {
            this.dto = dto;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadStaff();
        loadMonth();
        setupComboBox();
//        cboxStaff.getSelectionModel().select(0);
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> stage.close());
    }

    @Override
    public void setupComboBox() {
//        cboxStaff.setConverter(new StaffMemberConvertor(cboxStaff));
//        cboxStaff.getSelectionModel().select(0);
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_member_code", cboxStaff.getValue().getCode());
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_month", cboxMonth.getValue() + "-" + cboxYear.getValue());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.STAFF_SALARY, params);
        JasperViewer.viewReport(print, false);
    }

    private void loadStaff() {
        var task = new StaffMembersLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<StaffMember> list = task.get();
                if (list != null) {
                    List<StaffMember> list2 = new ArrayList<>();
                    StaffMember m = new StaffMember();
                    m.setCode("0");
                    m.setName(resourceBundle.getString("all"));
                    list2.add(m);
                    list2.addAll(list);
                    cboxStaff.setItems(FXCollections.observableList(list2));
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMonth() {
        String[] month = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] year = {"2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
        cboxMonth.setItems(FXCollections.observableList(Arrays.asList(month)));
        cboxYear.setItems(FXCollections.observableList(Arrays.asList(year)));
    }
}
