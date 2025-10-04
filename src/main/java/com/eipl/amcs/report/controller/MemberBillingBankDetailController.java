package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MemberBillingBankDetailController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate;
    @FXML
    private ComboBox<SocietyPaymentCycle> cboxSocietyPaymentCycleCode;

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
    }

    @Override
    public void setupComboBox() {
        cboxSocietyPaymentCycleCode.setConverter(new SocietyPaymentCycleConvertor(cboxSocietyPaymentCycleCode));
        cboxSocietyPaymentCycleCode.getSelectionModel().select(0);
    }

    private StringBuilder errorMsg;

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_society_payment_cycle_code", cboxSocietyPaymentCycleCode.getValue().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_BILL_BANK_DETAIL, params);
        JasperViewer.viewReport(print, false);
    }

    private boolean validate() {
        return true;
    }

    @Override
    public void loadData() {
        var task = new SocietyPaymentCycleLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SocietyPaymentCycle> list = task.get();
                if (list != null) {
                    cboxSocietyPaymentCycleCode.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxSocietyPaymentCycleCode);
                    cboxSocietyPaymentCycleCode.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
