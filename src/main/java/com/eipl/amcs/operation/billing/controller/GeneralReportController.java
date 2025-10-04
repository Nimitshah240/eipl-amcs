package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.report.dto.PaymentForBank;
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

public class GeneralReportController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private ComboBox<MilkType> cboxMilkType;


    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    private MemberBillSummary dto = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    private MemberBillSummary propSummary;


    public void setSummay(MemberBillSummary dto) {
        if (dto != null) {
            this.dto = dto;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadMilkType();
        setupComboBox();
        cboxMilkType.getSelectionModel().select(0);
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> stage.close());
    }

    @Override
    public void setupComboBox() {
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkType.getSelectionModel().select(0);
    }

    private StringBuilder errorMsg;

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_society_payment_cycle_code", dto.getPaymentCycle().getCode());
        params.put("p_payment_mode", 1);
        params.put("p_milk_type", cboxMilkType.getSelectionModel().getSelectedIndex());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PAYMENT_REGISTER_ALL, params);
        JasperViewer.viewReport(print, false);
        

    }

    private boolean validate() {
        return true;
    }

    private void loadMilkType() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    List<MilkType> list2 = new ArrayList<>();
                    MilkType m = new MilkType();
                    m.setCode(0);
                    m.setName(resourceBundle.getString("all"));
                    list2.add(m);
                    list2.addAll(list);
                    cboxMilkType.setItems(FXCollections.observableList(list2));
                    cboxMilkType.getSelectionModel().select(0);
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


}