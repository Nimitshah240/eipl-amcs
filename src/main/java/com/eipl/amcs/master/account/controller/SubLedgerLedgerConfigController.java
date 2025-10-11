package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.SubLedgerLedgerConfigService;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class SubLedgerLedgerConfigController implements MyInitialization, PopupCallback {

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
    private Stage stage;
    private List<Ledger> listLedger;
    private List<SubLedgerLedgerConfig> list;

    @FXML
    private CheckBox chkSelect;

    private StringBuilder errorMsg = null;

    private ResourceBundle resourceBundle;
    private LedgerService ledgerService;
    private SubLedgerLedgerConfigService subLedgerLedgerConfigService;

    public SubLedgerLedgerConfigController() {
        ledgerService = context.getBean(LedgerService.class);
        subLedgerLedgerConfigService = context.getBean(SubLedgerLedgerConfigService.class);
    }

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
        try {
            chkSelect.setSelected(false);
            for (Ledger ledger : listLedger) {
                ledger.selectedProperty().set(false);
            }

            list = subLedgerLedgerConfigService.findBySubLedgerType((int) cboxType.getValue().getKey());
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
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void loadData() {
        try {
            tableData.setItems(null);
            listLedger = ledgerService.findAllByIsActive();
            if (listLedger != null) {
                tableData.setItems(FXCollections.observableList(listLedger));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        setValuesInObject();
        subLedgerLedgerConfigService.save(list, String.valueOf(cboxType.getValue().getKey()), CommonUtil.setIdentityHeader());
        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                resourceBundle.getString("save.successful"));
        alert.createAlert();
    }
}
