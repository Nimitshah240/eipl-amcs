package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.master.account.task.SubLedgerLedgerConfigBySubLedgerTypeLoadTask;
import com.eipl.amcs.master.account.task.SubLedgerLedgerConfigSaveTask;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SubLedgerLedgerConfigController implements MyInitialization, PopupCallback {

    private final StringBuilder errorMsg = null;
    @FXML
    StackPane root;
    @FXML
    TableView<Ledger> tableData;
    @FXML
    TableColumn<Ledger, Boolean> colSelect;
    @FXML
    TableColumn<Ledger, String> colLedger;
    @FXML
    Button btnClose, btnSave;
    @FXML
    ComboBox<CustomerTypeKeyValDto> cboxType;
    private List<Ledger> listLedger;
    private List<SubLedgerLedgerConfig> list;
    @FXML
    private CheckBox chkSelect;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        setupComboBox();
        loadData();
        btnSave.setOnAction(e -> {
            saveData();
        });
        chkSelect.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                listLedger.forEach(e -> e.selectedProperty().set(true));
            } else {
                listLedger.forEach(e -> e.selectedProperty().set(false));
            }
        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        cboxType.getItems().addAll(CommonUtils.getAllCustomerTypes());
        cboxType.setOnAction(e -> loadConfig());

    }

    private void loadConfig() {
        // TODO Auto-generated method stub
        chkSelect.setSelected(false);
        for (Ledger ledger : listLedger) {
            ledger.selectedProperty().set(false);
        }
        var task = new SubLedgerLedgerConfigBySubLedgerTypeLoadTask((int) cboxType.getValue().getKey());
        task.setOnSucceeded(e -> {
            try {
                list = task.get();
                if (list != null) {
                    for (Ledger ledger : listLedger) {
                        if (list.stream().anyMatch(ee -> ee.getLedger().getCode().equals(ledger.getCode())))
                            ledger.selectedProperty().set(true);
                    }
                } else {
                    for (Ledger ledger : listLedger) {
                        ledger.selectedProperty().set(false);
                    }
                }
                tableData.setItems(FXCollections.observableList(listLedger));
            } catch (InterruptedException | ExecutionException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        tableData.setEditable(true);
        colSelect.setEditable(true);
        colSelect.setCellValueFactory(data -> data.getValue().selectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
        colLedger.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
    }

    @Override
    public void setupComboBox() {
        cboxType.setConverter(new CustomerTypeConvertor(cboxType));
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }

    @Override
    public void loadData() {
        tableData.setItems(null);
        var task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                listLedger = task.get();
                if (listLedger != null) {
                    tableData.setItems(FXCollections.observableList(listLedger));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setValuesInObject() {
        list = new ArrayList<>();
        for (Ledger ledger : listLedger) {
            if (ledger.isSelected()) {
                SubLedgerLedgerConfig config = new SubLedgerLedgerConfig();
                config.setLedger(ledger);
                config.setSubLedgerType((int) cboxType.getValue().getKey());
                config.setSociety(MainApp.identityDto.getSociety());
                config.setUnionCode(MainApp.identityDto.getUnion().getCode());
                list.add(config);
            }
        }
    }

    @Override
    public void saveData() {
        if (cboxType.getValue() == null || (list == null || list.isEmpty())) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
            return;
        }
        setValuesInObject();
        var task = new SubLedgerLedgerConfigSaveTask(list, String.valueOf(cboxType.getValue().getKey()));
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("save.successful"));
            alert.createAlert();
        });
        new Thread(task).start();
    }
}
