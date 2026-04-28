package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.procurement.converter.SocietyPaymentCycleConvertor;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MemberWiseHeadPivotingController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate;
    @FXML
    private ComboBox<Member> cboxMemberCode;
    @FXML
    private ComboBox<SocietyPaymentCycle> cboxSocietyPaymentCycleCode;
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
        loadData();
        setupComboBox();

        btnGenerate.setOnAction(e -> validateAndGenerateReport());

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
        cboxMemberCode.setConverter(new MemberReportConvertor(cboxMemberCode));
        cboxMemberCode.setCellFactory(new MemberCellFactory());
        cboxSocietyPaymentCycleCode.setConverter(new SocietyPaymentCycleConvertor(cboxSocietyPaymentCycleCode));
        cboxSocietyPaymentCycleCode.getSelectionModel().select(0);
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_member_code", cboxMemberCode.getValue().getCode());
        params.put("p_society_payment_cycle_code", cboxSocietyPaymentCycleCode.getValue().getCode());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_WISE_HEAD_PIVOTING, params);
        JasperViewer.viewReport(print, false);
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


        var task1 = new SocietyPaymentCycleLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<SocietyPaymentCycle> list = task1.get();
                if (list != null) {
                    cboxSocietyPaymentCycleCode.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxSocietyPaymentCycleCode);
                    cboxSocietyPaymentCycleCode.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }
}
