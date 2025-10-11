package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.LedgerSubLedgerDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.concurrent.CompletableFuture;

public class SubLedgerAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxType;
    @FXML
    TableColumn<Ledger, Boolean> colSelect;
    @FXML
    private CheckBox chkSelectAll;
    @FXML
    private E_TextField txtCode, txtName, txtLocalName;
    private List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList;

    @FXML
    private TableView<Ledger> tableLedgerData;
    @FXML
    private TableColumn<Ledger, String> colCode, colName, colLocalName, colIsActive;
    private ObjectProperty<Ledger> propDto;
    private List<Ledger> listLedger;
    private Stage stage;

    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private SubLedger subLedger = null;

    private NextCodeService nextCodeService;
    private LedgerService ledgerService;
    private SubLedgerService subLedgerService;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public SubLedgerAddEditController() {
        nextCodeService = MainApp.context.getBean(NextCodeService.class);
        ledgerService = MainApp.context.getBean(LedgerService.class);
        subLedgerService = MainApp.context.getBean(SubLedgerService.class);
        propDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    public void setSubLedger(SubLedger subLedger) {
        if (subLedger != null) {
            this.subLedger = subLedger;
            loadLedgerDetail();
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextSubLedgerCode();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        setupLedgerTable();
        loadLedger();
        ledgerSubLedgerMappingList = new ArrayList<>();
        cboxType.getItems().addAll(FXCollections.observableList(CommonUtils.getAllCustomerTypes()));
        cboxType.getSelectionModel().select(6);
        chkSelectAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                listLedger.forEach(e -> e.selectedProperty().set(true));
            } else {
                listLedger.forEach(e -> e.selectedProperty().set(false));
            }
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/SubLedger.fxml")));
        });
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    private void setupLedgerTable() {

//        tableLedgerData.setEditable(true);
        colSelect.setEditable(true);
        colSelect.setCellValueFactory(data -> data.getValue().selectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
        colCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCode()));
        colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNameLocal()));

        propDto.bind(tableLedgerData.getSelectionModel().selectedItemProperty());

    }

    public void loadControls() {
        cboxType.getSelectionModel().select(subLedger.getType() - 1);
        txtCode.setText(subLedger.getCode());
        txtName.setText(subLedger.getName());
        txtLocalName.setText(subLedger.getNameLocal());
    }

    public void loadLedger() {
        try {
            listLedger = ledgerService.findAllByIsActive();
            if (listLedger != null) {
                tableLedgerData.setItems(FXCollections.observableList(listLedger));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadLedgerDetail() {
        try {
            LedgerSubLedgerDto dto = new LedgerSubLedgerDto();
            CompletableFuture<List<Ledger>> ledgerListFuture = CompletableFuture.supplyAsync(() -> ledgerService.findAllByIsActive());
            CompletableFuture<List<LedgerSubLedgerMapping>> ledgerSubLedgerMappingListFuture = CompletableFuture.supplyAsync(() -> ledgerService.fetchMapping(MainApp.identityDto.getSociety().getCode(), null, subLedger.getCode()));
            CompletableFuture.allOf(ledgerListFuture, ledgerSubLedgerMappingListFuture)
                    .whenCompleteAsync((result, ex) -> {
                        try {
                            if (!ledgerListFuture.get().isEmpty())
                                dto.setLedgerList(ledgerListFuture.get());

                            if (!ledgerSubLedgerMappingListFuture.get().isEmpty())
                                dto.setLedgerSubLedgerMappingList(ledgerSubLedgerMappingListFuture.get());

                            for (Ledger ldr : dto.getLedgerList()) {
                                if (dto.getLedgerSubLedgerMappingList().stream()
                                        .anyMatch(p -> p.getLedger().getCode().equals(ldr.getCode())))
                                    ldr.selectedProperty().set(true);
                            }

                            if (dto != null) {
                                listLedger = dto.getLedgerList();
                                tableLedgerData.setItems(FXCollections.observableList(dto.getLedgerList()));
                            }
                        } catch (Exception exs) {
                            System.out.println(exs);
                            throw new RuntimeException(exs);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("subledger"), errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.subLedger != null) {
                subLedger = setValuesInObject();
                updateData();
            }
        } else {
            subLedger = new SubLedger();
            subLedger = setValuesInObject();
            saveData();
        }
    }

    private void getNextSubLedgerCode() {
        try {
            String nextCode = nextCodeService.getNextCode("SubLedger", "code", MainApp.identityDto.getSociety().getCode(), 0);
            if (nextCode == null || nextCode.isEmpty()) return;
            txtCode.setText(nextCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SubLedger setValuesInObject() {
        subLedger.setCode(cboxType.getValue().getKey() <= 2 ? "M" + txtCode.getText() : "C" + txtCode.getText());
        subLedger.setName(txtName.getText());
        subLedger.setNameLocal(txtLocalName.getText());
        subLedger.setSociety(MainApp.identityDto.getSociety());
        subLedger.setUnionCode(MainApp.identityDto.getUnion().getCode());
        subLedger.setType(cboxType.getValue().getKey());
        subLedger.setActive(true);

        return subLedger;
    }

    private boolean validate() {
//        if (cboxType.getValue() == null)
//            errorMsg.append(resourceBundle.getString("typenullerror") + "\n");

        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        try {
            subLedgerService.save(subLedger, CommonUtil.setIdentityHeader());
            if (listLedger != null) saveMapping();
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("subledger"), resourceBundle.getString("subledger.insert.successful"));
            alert.createAlert();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/SubLedger.fxml")));
        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("subledger"), "Error");
            alert.createAlert();
        }
    }

    private void saveMapping() {
        for (Ledger ledger : listLedger) {
            if (ledger.selectedProperty().get()) {
                LedgerSubLedgerMapping o = new LedgerSubLedgerMapping();
                o.setLedger(ledger);
                o.setSubLedger(subLedger);
                o.setSociety(MainApp.identityDto.getSociety());
                o.setUnionCode(MainApp.identityDto.getUnion().getCode());

                ledgerSubLedgerMappingList.add(o);
            }
        }
        ledgerService.save(ledgerSubLedgerMappingList, CommonUtil.setIdentityHeader());
    }


    @Override
    public void updateData() {
        try {
            subLedgerService.update(subLedger, CommonUtil.setIdentityHeader());
            if (listLedger != null) saveMapping();
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledger"), resourceBundle.getString("ledger.update.successful"));
            alert.createAlert();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/SubLedger.fxml")));
        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledger"), "Error");
            alert.createAlert();
        }
    }
}
