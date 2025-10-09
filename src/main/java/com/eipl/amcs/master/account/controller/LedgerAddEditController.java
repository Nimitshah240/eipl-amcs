package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.converter.LedgerGroupConvertor;
import com.eipl.amcs.master.account.converter.LedgerTypeConvertor;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.service.LedgerGroupService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.LedgerTypeService;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.util.CommonUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class LedgerAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<LedgerGroup> cboxLedgerGroup;
    @FXML
    private ComboBox<LedgerType> cboxLedgerType;
    @FXML
    private CheckBox chkBoxSubLedger, chkBoxSelectAll;
    @FXML
    private E_TextField txtCode, txtName, txtLocalName;

    @FXML
    private TableColumn<SubLedger, Boolean> colSelect;
    @FXML
    private TableColumn<SubLedger, String> colSubLedger, colCode, colName, colLocalName;

    @FXML
    private TableView<SubLedger> tableSubLedgerData;
    private List<SubLedger> listSubLedger;
    private List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private Ledger ledger = null;

    private List<LedgerGroup> ledgerGroupList;
    private LedgerTypeService ledgerTypeService;
    private LedgerService ledgerService;
    private NextCodeService nextCodeService;
    private SubLedgerService subLedgerService;
    private LedgerGroupService ledgerGroupService;

    private ObjectProperty<SubLedger> propDto;


    public LedgerAddEditController() {
        ledgerTypeService = MainApp.context.getBean(LedgerTypeService.class);
        ledgerService = MainApp.context.getBean(LedgerService.class);
        nextCodeService = MainApp.context.getBean(NextCodeService.class);
        ledgerGroupService = MainApp.context.getBean(LedgerGroupService.class);
        subLedgerService = MainApp.context.getBean(SubLedgerService.class);
        propDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    public void setLedger(Ledger ledger) {
        loadLedgerType();
        if (ledger != null) {
            this.ledger = ledger;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadLedgerGroup();
        } else {
            getNextLedgerCode();
        }

    }

    public void loadSubLedger() {
        try {
            listSubLedger = subLedgerService.findAll();
            if (listSubLedger != null) {
                tableSubLedgerData.setItems(FXCollections.observableList(listSubLedger));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        loadLedgerType();
        setupSubLedgerTable();
        loadSubLedger();
        ledgerSubLedgerMappingList = new ArrayList<>();
        chkBoxSelectAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                listSubLedger.forEach(e -> e.selectedProperty().set(true));
            } else {
                listSubLedger.forEach(e -> e.selectedProperty().set(false));
            }
        });

        cboxLedgerType.selectionModelProperty().addListener(e -> {
            if (cboxLedgerType.getValue() != null)
                loadLedgerGroupByType();
        });
        cboxLedgerType.setOnAction(e -> {
            if (cboxLedgerType.getValue() != null)
                loadLedgerGroupByType();
        });
//        loadLedgerGroup();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Ledger.fxml")));
        });
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        chkBoxSubLedger.setOnAction(e -> {
            if (chkBoxSubLedger.isSelected()) {
                tableSubLedgerData.setDisable(false);
                chkBoxSelectAll.setDisable(false);
            } else {
                tableSubLedgerData.setDisable(true);
                chkBoxSelectAll.setDisable(true);
            }
        });
    }

    public void loadControls() {
        txtCode.setText(ledger.getCode());
        txtName.setText(ledger.getName());
        txtLocalName.setText(ledger.getNameLocal());
        LedgerType ledgerType = ledgerGroupList.stream().filter(p -> p.getCode().intValue() ==
                ledger.getLedgerGroup().getCode().intValue()).findAny().get().getLedgerType();
        cboxLedgerType.getSelectionModel().select(ledgerType);
        cboxLedgerGroup.getSelectionModel().select(ledger.getLedgerGroup());
        chkBoxSubLedger.setSelected(ledger.getHasSubLedger());
        if (ledger.getHasSubLedger())
            tableSubLedgerData.setDisable(false);
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.ledger != null) {
                ledger = setValuesInObject();
                updateData();
            }
        } else {
            ledger = new Ledger();
            ledger = setValuesInObject();
            saveData();
        }
    }


    private void setupSubLedgerTable() {

        tableSubLedgerData.setEditable(true);
        colSelect.setEditable(true);
        colSelect.setCellValueFactory(data -> data.getValue().selectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
        colSubLedger.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getType()));
        colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNameLocal()));
        colCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCode()));
        propDto.bind(tableSubLedgerData.getSelectionModel().selectedItemProperty());

    }

    private void getNextLedgerCode() {
        try {
            String nextCode = nextCodeService.getNextCode("Ledger", "code", MainApp.identityDto.getSociety().getCode(), 0);
            if (nextCode == null || nextCode.isEmpty())
                return;
            txtCode.setText(nextCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Ledger setValuesInObject() {
        ledger.setCode(txtCode.getText());
        ledger.setName(txtName.getText());
        ledger.setNameLocal(txtLocalName.getText());
        ledger.setLedgerGroup(cboxLedgerGroup.getValue());
        ledger.setSociety(MainApp.identityDto.getSociety());
        ledger.setUnionCode(MainApp.identityDto.getUnion().getCode());
        ledger.setHasSubLedger(chkBoxSubLedger.isSelected());
        ledger.setActive(true);
        return ledger;
    }

    private boolean validate() {
        if (cboxLedgerGroup.getValue() == null)
            errorMsg.append(resourceBundle.getString("ledgergroupnullerror") + "\n");

        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        try {
            ledgerService.save(ledger, CommonUtil.setIdentityHeader());
            if (listSubLedger != null)
                saveMapping();
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                    resourceBundle.getString("ledger.insert.successful"));
            alert.createAlert();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Ledger.fxml")));

        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                    "Error");
            alert.createAlert();
        }
    }

    private void saveMapping() {
        for (SubLedger subLedger : listSubLedger) {
            if (subLedger.selectedProperty().get()) {
                LedgerSubLedgerMapping o = new LedgerSubLedgerMapping();
                o.setSubLedger(subLedger);
                o.setLedger(ledger);
                o.setCode(ledger.getCode() + subLedger.getCode());
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
            ledgerService.update(ledger, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                    resourceBundle.getString("ledger.update.successful"));
            alert.createAlert();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Ledger.fxml")));
        } catch (Exception ex) {
            ex.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                    "Error");
            alert.createAlert();
        }
    }

    @Override
    public void setupComboBox() {
        cboxLedgerGroup.setConverter(new LedgerGroupConvertor(cboxLedgerGroup));
        cboxLedgerType.setConverter(new LedgerTypeConvertor(cboxLedgerType));
    }

    private void loadLedgerGroup() {
        try {
            ledgerGroupList = ledgerGroupService.findAll();
            loadControls();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void loadLedgerType() {
        try {
            List<LedgerType> list = ledgerTypeService.findAll();
            if (list != null) {
                cboxLedgerType.setItems(FXCollections.observableList(list));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLedgerGroupByType() {
        try {
            List<LedgerGroup> list = ledgerGroupService.findByLedgerType(cboxLedgerType.getValue().getCode());
            if (list != null) {
                cboxLedgerGroup.setItems(FXCollections.observableList(list));
                cboxLedgerGroup.getSelectionModel().select(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
