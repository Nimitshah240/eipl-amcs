package com.eipl.amcs.reportengine.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.reportengine.service.RptTableResultService;
import com.eipl.amcs.reportengine.task.RptTableResultSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import com.lowagie.text.PageSize;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReportSetupController implements MyInitialization {
    @FXML
    private AnchorPane root;
    @FXML
    private TableView<RptTableResult> tblData;
    @FXML
    private TableColumn<RptTableResult, String> colTitle;
    @FXML
    private TableColumn<RptTableResult, String> colSequence, colWidth, colAlignment;
    @FXML
    private TableColumn<RptTableResult, Boolean> colVisible;
    @FXML
    private Button btnSave, btnClose;
    @FXML
    private RadioButton rbtnPortrait, rbtnLandscape;
    @FXML
    private Label lblWidth, lblHeight;
    @FXML
    private ToggleGroup rptLayout;

    @Setter
    private Stage stage;
    @Setter
    private PopupCallback callback;
    private Long reportCode;
    private List<RptTableResult> rptTableResultList;
    private RptTableResultService rptTableResultService;
    private ObservableList<String> listCellAlignment = FXCollections.observableArrayList();

    public void setReportCode(Long reportCode) {
        rptTableResultService = EmcsAppContext.getContext().getBean(RptTableResultService.class);
        this.reportCode = reportCode;
        rptTableResultList = rptTableResultService.findByReportCode(reportCode);
        tblData.setItems(FXCollections.observableList(rptTableResultList));
        if (rptTableResultList != null && !rptTableResultList.isEmpty()) {
            RptTableResult rptTableResult = rptTableResultList.get(0);
            if (rptTableResult.getReportOrientation() == 1) {
                rbtnPortrait.setSelected(true);
                lblWidth.setText(String.valueOf(PageSize.A4.getWidth() - 52));
                lblHeight.setText(String.valueOf(PageSize.A4.getHeight() - 52));
            } else {
                rbtnLandscape.setSelected(true);
                lblWidth.setText(String.valueOf(PageSize.A4.rotate().getWidth() - 52));
                lblHeight.setText(String.valueOf(PageSize.A4.rotate().getHeight() - 52));
            }
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listCellAlignment.add("Left");
        listCellAlignment.add("Right");
        listCellAlignment.add("Center");

        rptLayout.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(rbtnPortrait)) {
                rbtnPortrait.setSelected(true);
                lblWidth.setText(String.valueOf(PageSize.A4.getWidth() - 52));
                lblHeight.setText(String.valueOf(PageSize.A4.getHeight() - 52));
            } else {
                rbtnPortrait.setSelected(false);
                lblWidth.setText(String.valueOf(PageSize.A4.rotate().getWidth() - 52));
                lblHeight.setText(String.valueOf(PageSize.A4.rotate().getHeight() - 52));
            }
        });

        colVisible.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        colVisible.setCellFactory(CheckBoxTableCell.forTableColumn(colVisible));
        colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRespDispName()));
        colSequence.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDispSeq().toString()));
        colSequence.setCellFactory(TextFieldTableCell.forTableColumn());
        colSequence.setOnEditCommit(event -> {
            RptTableResult rowValue = event.getRowValue();
            rowValue.setDispSeq(CommonUtils.strToInteger(event.getNewValue()));
        });
        colWidth.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWidth().toString()));
        colWidth.setCellFactory(TextFieldTableCell.forTableColumn());
        colWidth.setOnEditCommit(event -> {
            RptTableResult rowValue = event.getRowValue();
            rowValue.setWidth(CommonUtils.strToInteger(event.getNewValue()));
        });
        colAlignment.setCellValueFactory(cellData -> new SimpleStringProperty(getCellAlignmentText(cellData.getValue().getCellAlignment())));
        colAlignment.setCellFactory(ComboBoxTableCell.forTableColumn(listCellAlignment));
        colAlignment.setOnEditCommit(event -> {
            RptTableResult rowValue = event.getRowValue();
            rowValue.setCellAlignment(getCellAlignmentVal(event.getNewValue()));
        });

        colVisible.setEditable(true);
        colSequence.setEditable(true);

        btnSave.setOnAction(event -> {
            saveData();
        });
        btnClose.setOnAction(event -> {
            this.stage.close();
        });
    }

    private Integer getCellAlignmentVal(String newValue) {
        int cellAlignment = 0;
        switch (newValue) {
            case "Left":
                cellAlignment = 1;
                break;
            case "Right":
                cellAlignment = 2;
                break;
            default:
                cellAlignment = 3;
                break;
        }
        return cellAlignment;
    }

    private String getCellAlignmentText(Integer cellAlignment) {
        String res = "";
        switch (cellAlignment) {
            case 1:
                res = "Left";
                break;
            case 2:
                res = "Right";
                break;
            default:
                res = "Center";
        }
        return res;
    }

    @Override
    public void saveData() {
        try {
            rptTableResultList.forEach(item -> {
                item.setVisible(item.isSelected());
                item.setReportOrientation(rbtnPortrait.isSelected() ? 1 : 2);
            });
            var task = new RptTableResultSaveTask(rptTableResultList, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    rptTableResultList = task.get();
                    this.callback.reloadData(true);
                    this.stage.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
