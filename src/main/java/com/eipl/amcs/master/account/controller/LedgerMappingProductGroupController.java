package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.ProductGroupMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.task.LedgerMappingProductGroupLoadTask;
import com.eipl.amcs.master.account.task.LedgerMappingProductGroupSaveTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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

public class LedgerMappingProductGroupController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerMappingProductGroup> tableData;
    @FXML
    TableColumn<LedgerMappingProductGroup, String> colProductGroupCode, colProductGroupName;
    @FXML
    TableColumn<LedgerMappingProductGroup, Ledger> colLedgerPurchase, colLedgerSale;

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
        for (LedgerMappingProductGroup item : tableData.getItems()) {
            if (item.getLedgerSaleCode() != null && item.getLedgerSaleCode().getName().equalsIgnoreCase("None"))
                item.setLedgerSaleCode(null);
            if (item.getLedgerPurchaseCode() != null && item.getLedgerPurchaseCode().getName().equalsIgnoreCase("None"))
                item.setLedgerPurchaseCode(null);
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            item.setSociety(MainApp.identityDto.getSociety());
        }
        LedgerMappingProductGroupSaveTask task = new LedgerMappingProductGroupSaveTask(tableData.getItems());
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("save.successful"));
            alert.createAlert();
            loadData();
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        colProductGroupCode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductGroup().getCode().toString()));
        colProductGroupName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductGroup().getName()));

        colLedgerSale.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLedgerSaleCode()));
        colLedgerSale.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedgerSale.setOnEditCommit(event -> {
            LedgerMappingProductGroup obj = event.getRowValue();
            obj.setLedgerSaleCode(event.getNewValue());
        });
        colLedgerPurchase.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLedgerPurchaseCode()));
        colLedgerPurchase.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedgerPurchase.setOnEditCommit(event -> {
            LedgerMappingProductGroup obj = event.getRowValue();
            obj.setLedgerPurchaseCode(event.getNewValue());
        });
    }

    @Override
    public void loadData() {
        LedgerMappingProductGroupLoadTask task = new LedgerMappingProductGroupLoadTask();
        task.setOnSucceeded(e -> {
            try {
                ProductGroupMappingDto dto = task.get();
                if (dto == null)
                    return;

                ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                setupTable();
                tableData.setItems(FXCollections.observableList(dto.getListMapping()));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        new Thread(task).start();
    }

}
