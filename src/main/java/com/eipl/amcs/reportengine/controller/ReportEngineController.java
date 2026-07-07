package com.eipl.amcs.reportengine.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.controller.FinancialYearClosingController;
import com.eipl.amcs.reportengine.dto.ParameterForm;
import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.reportengine.popup.ParameterFormBuilder;
import com.eipl.amcs.reportengine.service.ReportExecutionService;
import com.eipl.amcs.utils.TableExportUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.Collections.sort;

public class ReportEngineController implements MyInitialization, PopupCallback {


    @FXML
    private StackPane root;
    @FXML
    private TableView<Map> tblData;
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

        btnParameter.setOnAction(e -> onParameter());
        btnSetup.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ReportSetup", 2L, this);
        });
        btnExport.setOnAction(event -> {
            TableExportUtil.exportDataFromTableView(tblData, tblData.getId(), null);
        });
    }

    public void loadReport(Long reportId) {
        this.currentReportId = reportId;
        currentForm = popupBuilder.build(reportId);

    }

    private void openParameterPanel() {
        loadReport(3L);
        if (currentForm == null) {
            return;
        }
    }

    private void closeParameterPanel() {
    }

    private Long reportCode = 2L;
    private ReportResult result;

    private void onParameter() {
        tblData.getColumns().clear();

        Map<String, Object> values = parameterPopup.show(reportCode);

        if (values == null) {
            return;
        }
        result = reportExecutionService.execute(reportCode, values);

        List<RptTableResult> list = RptTableResult.buildForMilkCollectionReport(reportCode);
        list.sort(Comparator.comparing(RptTableResult::getDispSeq));
        list.stream()
                .forEach(row -> {
                    if (row.getVisible()) {
                        TableColumn<Map, Object> col = new TableColumn<>(row.getRespDispName());
                        col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(row.getRespFieldName())));
                        tblData.getColumns().add(col);
                    }
                });
        tblData.setItems(FXCollections.observableArrayList(result.getData()));
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag) {
            if (result != null) {
                tblData.getColumns().clear();
                List<RptTableResult> list = RptTableResult.buildForMilkCollectionReport(reportCode);
                list.sort(Comparator.comparing(RptTableResult::getDispSeq));
                list.stream()
                        .forEach(row -> {
                            if (row.getVisible()) {
                                TableColumn<Map, Object> col = new TableColumn<>(row.getRespDispName());
                                col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(row.getRespFieldName())));
                                tblData.getColumns().add(col);
                            }
                        });
                tblData.setItems(FXCollections.observableArrayList(result.getData()));
            }
        }
    }
}