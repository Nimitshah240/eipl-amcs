package com.eipl.amcs.reportengine.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.reportengine.dto.ParameterForm;
import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.reportengine.popup.ParameterFormBuilder;
import com.eipl.amcs.reportengine.service.ReportExecutionService;
import com.eipl.amcs.reportengine.service.RptTableResultService;
import com.eipl.amcs.reportengine.util.PdfExportUtil;
import com.eipl.amcs.utils.TableExportUtil;
import com.lowagie.text.PageSize;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
    private RptTableResultService rptTableResultService;

    private Long reportCode = 2L;
    private ReportResult result;
    private List<RptTableResult> listRptTableResult = null;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        popupBuilder = EmcsAppContext.getContext().getBean(ParameterFormBuilder.class);
        parameterPopup = EmcsAppContext.getContext().getBean(ParameterPopup.class);
        reportExecutionService = EmcsAppContext.getContext().getBean(ReportExecutionService.class);
        rptTableResultService = EmcsAppContext.getContext().getBean(RptTableResultService.class);

        btnParameter.setOnAction(e -> onParameter());
        btnSetup.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ReportSetup", reportCode, this);
        });
        btnExport.setOnAction(event -> {
            TableExportUtil.exportDataFromTableView(tblData, tblData.getId(), null);
        });
        btnPrint.setOnAction(event -> {
            if (listRptTableResult == null) {
                return;
            }
            double totalWidth = listRptTableResult.stream().filter(RptTableResult::getVisible)
                    .mapToDouble(RptTableResult::getWidth).sum();
            double allowedWidth = 0;
            if (listRptTableResult.get(0).getReportOrientation() == 1) {
                allowedWidth = PageSize.A4.getWidth() - 52;
            } else {
                allowedWidth = PageSize.A4.rotate().getWidth() - 52;
            }
            if (totalWidth > allowedWidth) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), "Report Page Setup", "Page setup A4 width exceed! Please re-arrange column visibility and width in setup option.");
                alert.createAlert();
                return;
            }
            PdfExportUtil.exportTableToPdf(tblData, new File("resources/report_pdf_" + reportCode + ".pdf").getAbsolutePath(),listRptTableResult);
        });
        btnRefresh.setOnAction(event -> renderTableData(false));
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

    private void onParameter() {
        Map<String, Object> values = parameterPopup.show(reportCode);
        if (values == null) {
            return;
        }
        result = reportExecutionService.execute(reportCode, values);
        renderTableData(false);
    }

    private void renderTableData(boolean refresh) {
        tblData.getColumns().clear();
        if (refresh || listRptTableResult == null || listRptTableResult.isEmpty())
            listRptTableResult = rptTableResultService.findByReportCode(reportCode);
        listRptTableResult.sort(Comparator.comparing(RptTableResult::getDispSeq));
        listRptTableResult.forEach(row -> {
            if (row.getVisible()) {
                TableColumn<Map, Object> col = new TableColumn<>(row.getRespDispName());
                col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(row.getRespFieldName())));
                col.setMinWidth(row.getWidth());
                String styleClass = "";
                switch (row.getCellAlignment()) {
                    case 1:
                        break;
                    case 2:
                        styleClass = "cell-right-aligned";
                        break;
                    default:
                        styleClass = "cell-center-aligned";
                        break;
                }
                if (!styleClass.isEmpty())
                    col.getStyleClass().add(styleClass);
                tblData.getColumns().add(col);
            }
        });
        tblData.setItems(FXCollections.observableArrayList(result.getData()));
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag) {
            if (result != null) {
                renderTableData(true);
            }
        }
    }
}