package com.eipl.amcs.reportengine.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.reportengine.dto.ParameterForm;
import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.popup.ParameterFormBuilder;
import com.eipl.amcs.reportengine.service.ReportExecutionService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportEngineController implements MyInitialization {


    @FXML
    private StackPane root;
    @FXML
    private StackPane parameterContainer;
    private Long currentReportId;
    private ParameterFormBuilder popupBuilder;

    @FXML
    private Button btnPrint, btnExport, btnParameter, btnRefresh, btnSetup, btnClose;

    ParameterForm currentForm;
    private ParameterPopup parameterPopup;
    private ReportExecutionService reportExecutionService;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        popupBuilder = EmcsAppContext.getContext().getBean(ParameterFormBuilder.class);
        parameterPopup = EmcsAppContext.getContext().getBean(ParameterPopup.class);
        reportExecutionService = EmcsAppContext.getContext().getBean(ReportExecutionService.class);
        parameterContainer.setVisible(false);
        parameterContainer.setManaged(false);

        btnParameter.setOnAction(e -> onParameter());

    }

    public void loadReport(Long reportId) {
        this.currentReportId = reportId;
        currentForm = popupBuilder.build(reportId);
        parameterContainer.getChildren().setAll(currentForm.getRoot());
    }

    private void openParameterPanel() {
        loadReport(3L);
        if (currentForm == null) {
            return;
        }
        parameterContainer.setManaged(true);
        parameterContainer.setVisible(true);
    }

    private void closeParameterPanel() {

        parameterContainer.setVisible(false);
        parameterContainer.setManaged(false);
    }

    private void onParameter() {

        Map<String, Object> values = parameterPopup.show(1L);

        if (values == null) {
            return;
        }
        ReportResult result = reportExecutionService.execute(1L, values);

        System.out.println(result.getData());

    }

}