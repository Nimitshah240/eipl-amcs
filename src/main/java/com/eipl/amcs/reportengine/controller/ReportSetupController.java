package com.eipl.amcs.reportengine.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.reportengine.model.RptTableResult;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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


    public void setReportCode(Long reportCode) {
        this.reportCode = reportCode;
        rptTableResultList = RptTableResult.buildForMilkCollectionReport(reportCode);
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
            rptTableResultList.forEach(item -> item.setVisible(item.isSelected()));
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("resources/report_" + this.reportCode + ".ser")))) {
                oos.writeObject(rptTableResultList);
                this.callback.reloadData(true);
                this.stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btnClose.setOnAction(event -> {
            this.stage.close();
        });
    }
}
