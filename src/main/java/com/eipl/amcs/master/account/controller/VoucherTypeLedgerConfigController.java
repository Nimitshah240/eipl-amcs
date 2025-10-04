package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import com.eipl.amcs.master.account.dto.ProductGroupMappingDto;
import com.eipl.amcs.master.account.dto.VoucherTypeMappingDto;
import com.eipl.amcs.master.account.task.VoucherTypeLedgerConfigLoadTask;
import com.eipl.amcs.master.account.task.VoucherTypeLedgerConfigSaveTask;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
//import net.ucanaccess.console.Main;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class VoucherTypeLedgerConfigController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<VoucherTypeLedgerConfig> tableData;
    @FXML
    TableColumn<VoucherTypeLedgerConfig, String> colType, colVoucherType;
    @FXML
    TableColumn<VoucherTypeLedgerConfig, Ledger> colLedger;
    @FXML
    Button btnSave, btnClose;

    private ResourceBundle resourceBundle;

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
        for (VoucherTypeLedgerConfig item : tableData.getItems()) {
            item.setSociety(MainApp.identityDto.getSociety());
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
        }
        VoucherTypeLedgerConfigSaveTask task = new VoucherTypeLedgerConfigSaveTask(tableData.getItems());
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("save.successful"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        colVoucherType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherType().getName()));

        colLedger.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLedger()));
        colLedger.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedger.setOnEditCommit(event -> {
            VoucherTypeLedgerConfig obj = event.getRowValue();
            obj.setLedger(event.getNewValue());
        });
        colType.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCreditDebit()!=null?
                cell.getValue().getCreditDebit()==false?resourceBundle.getString("debit"):resourceBundle.getString("credit"):""));
        colType.setCellFactory(ComboBoxTableCell.forTableColumn(converterString, FXCollections.observableList(typeList)));
        colType.setOnEditCommit(event -> {
            VoucherTypeLedgerConfig obj = event.getRowValue();
            obj.setCreditDebit(event.getNewValue().equalsIgnoreCase(resourceBundle.getString("debit"))?false:true);
        });
    }

    private StringConverter<Ledger> converter = new StringConverter<>() {
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
    private StringConverter<String> converterString = new StringConverter<>() {
        @Override
        public String toString(String  object) {
            if (object == null)
                return null;
            return object;
        }

        @Override
        public String fromString(String string) {
            if (string == null || string.isEmpty())
                return null;
            return typeList.stream().filter(p -> p.equalsIgnoreCase(string))
                    .findFirst().orElse(null);
        }
    };

    private ObservableList<Ledger> ledgerList;
    private List<String> typeList;

    @Override
    public void loadData() {
        VoucherTypeLedgerConfigLoadTask task = new VoucherTypeLedgerConfigLoadTask();
        task.setOnSucceeded(e -> {
            try {
                VoucherTypeMappingDto dto = task.get();
                if (dto == null)
                    return;

                ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                typeList = new ArrayList<>();
                typeList.add(resourceBundle.getString("debit"));
                typeList.add(resourceBundle.getString("credit"));
                setupTable();
                tableData.setItems(FXCollections.observableList(dto.getListMapping()));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        new Thread(task).start();
    }

}
