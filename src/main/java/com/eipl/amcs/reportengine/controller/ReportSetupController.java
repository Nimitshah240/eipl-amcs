package com.eipl.amcs.reportengine.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.reportengine.service.RptTableResultService;
import com.eipl.amcs.reportengine.task.RptTableResultSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
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
    private TableColumn<RptTableResult, String> colSequence;
    @FXML
    private TableColumn<RptTableResult, Boolean> colVisible;
    @FXML
    private Button btnSave, btnClose;

    @Setter
    private Stage stage;
    @Setter
    private PopupCallback callback;
    private Long reportCode;
    private List<RptTableResult> rptTableResultList;
    private RptTableResultService rptTableResultService;


    public void setReportCode(Long reportCode) {
        rptTableResultService = EmcsAppContext.getContext().getBean(RptTableResultService.class);
        this.reportCode = reportCode;
        rptTableResultList = rptTableResultService.findByReportCode(reportCode);
        tblData.setItems(FXCollections.observableList(rptTableResultList));
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colVisible.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        colVisible.setCellFactory(CheckBoxTableCell.forTableColumn(colVisible));
        colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRespDispName()));
        colSequence.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDispSeq().toString()));
        colSequence.setCellFactory(TextFieldTableCell.forTableColumn());
        colSequence.setOnEditCommit(event -> {
            RptTableResult rowValue = event.getRowValue();
            rowValue.setDispSeq(CommonUtils.strToInteger(event.getNewValue()));
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

    @Override
    public void saveData() {
        try {
            var task = new RptTableResultSaveTask(rptTableResultList, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    rptTableResultList = task.get();
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
