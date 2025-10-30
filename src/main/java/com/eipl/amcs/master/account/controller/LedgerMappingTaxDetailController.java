package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.TaxDetailMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.task.LedgerMappingTaxDetailLoadTask;
import com.eipl.amcs.master.account.task.LedgerMappingTaxDetailSaveTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LedgerMappingTaxDetailController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerMappingTaxDetail> tableTaxDetailData;
    @FXML
    TableColumn<LedgerMappingTaxDetail, Tax> colTax, colTaxDetail;
    @FXML
    TableColumn<LedgerMappingTaxDetail, Ledger> colLedger;

    @FXML
    Button btnSave, btnClose;

    private ResourceBundle resourceBundle;
    private ObservableList<Ledger> ledgerList;
    private final StringConverter<Ledger> converter = new StringConverter<>() {
        @Override
        public String toString(Ledger object) {
            if (object == null)
                return null;
            return object.toString();
        }

        @Override
        public Ledger fromString(String string) {
            if (string == null || string.isEmpty())
                return null;
            return ledgerList.stream().filter(p -> p.toString().equalsIgnoreCase(string))
                    .findFirst().orElse(null);
        }
    };

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        loadData();

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        btnSave.setOnAction(e -> {
            saveData();
        });
    }

    @Override
    public void saveData() {
        for (LedgerMappingTaxDetail item : tableTaxDetailData.getItems()) {
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            item.setSociety(MainApp.identityDto.getSociety());
        }
        for (LedgerMappingTaxDetail item : tableTaxDetailData.getItems()) {
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            item.setSociety(MainApp.identityDto.getSociety());
        }
        LedgerMappingTaxDetailSaveTask task = new LedgerMappingTaxDetailSaveTask(tableTaxDetailData.getItems());
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("save.successful"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        colTax.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getTaxDetail().getTax()));
        colTaxDetail.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getTaxDetail() != null ? cell.getValue().getTaxDetail().getBasicTax() : ""));
        colLedger.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getLedger()));
        colLedger.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedger.setOnEditCommit(event -> {
            LedgerMappingTaxDetail obj = event.getRowValue();
            obj.setLedger(event.getNewValue());
        });
    }

    @Override
    public void loadData() {
        LedgerMappingTaxDetailLoadTask task = new LedgerMappingTaxDetailLoadTask();
        task.setOnSucceeded(e -> {
            try {
                TaxDetailMappingDto dto = task.get();
                if (dto == null)
                    return;

                ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                setupTable();
                tableTaxDetailData.setItems(FXCollections.observableList(dto.getListMapping()));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        new Thread(task).start();
    }

}
