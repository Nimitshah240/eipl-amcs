package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.report.dto.PaymentForBank;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MilkDispatchReportController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private ComboBox<String> cboxFormat;
    private ResourceBundle resourceBundle;
    private MilkDispatch dto = null;

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

    public void setDispatch(MilkDispatch dto) {
        if (dto != null) {
            this.dto = dto;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        cboxFormat.getItems().addAll("Format 1", "Format 2");
        setupComboBox();
        cboxFormat.getSelectionModel().select(0);
        btnGenerate.setOnAction(e -> validateAndGenerate());
        btnClose.setOnAction(e -> stage.close());
    }

    private void validateAndGenerate() {
        switch (cboxFormat.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                validateAndGenerateReportTwo();
                break;
            case 2:
                validateAndGenerateReport();
                break;
        }
    }

    @Override
    public void setupComboBox() {
    }

    private boolean validate() {
        return true;
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_invoice_no", dto.getChallanNo());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_DISPATCH_CHALLAN, params);
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReportTwo() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_challan_no", dto.getChallanNo());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_DISPATCH_CHALLAN_FORMAT_TWO, params);
        JasperViewer.viewReport(print, false);
    }
}
