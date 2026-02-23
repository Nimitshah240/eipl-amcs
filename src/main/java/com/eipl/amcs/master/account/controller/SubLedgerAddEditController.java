package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.dto.LedgerSubLedgerDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.task.*;
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
import java.util.concurrent.ExecutionException;

public class SubLedgerAddEditController implements MyInitialization {
    private final ObjectProperty<Ledger> propDto;
    @FXML
    TableColumn<Ledger, Boolean> colSelect;
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxType;
    @FXML
    private CheckBox chkSelectAll;
    @FXML
    private E_TextField txtCode, txtName, txtLocalName;
    private List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList;
    @FXML
    private TableView<Ledger> tableLedgerData;
    @FXML
    private TableColumn<Ledger, String> colCode, colName, colLocalName, colIsActive;
    private List<Ledger> listLedger;
    private Stage stage;

    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private SubLedger subLedger = null;


    public SubLedgerAddEditController() {
        propDto = new SimpleObjectProperty<>();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
        var task = new LedgerLoadTask();
        task.setOnSucceeded(ee -> {
            try {
                listLedger = task.get();
                if (listLedger != null) {
                    tableLedgerData.setItems(FXCollections.observableList(listLedger));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadLedgerDetail() {
        var task = new LedgerSubLedgerMappingDtoLoadTask(null, subLedger);
        task.setOnSucceeded(e -> {
            try {
                LedgerSubLedgerDto dto = task.get();
                if (dto != null) {
                    listLedger = dto.getLedgerList();
                    tableLedgerData.setItems(FXCollections.observableList(dto.getLedgerList()));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
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
        var task = new SubLedgerNumberLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty()) return;
                txtCode.setText(nextCode);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private SubLedger setValuesInObject() {
        subLedger.setCode(txtCode.getText());
        subLedger.setName(txtName.getText());
        subLedger.setNameLocal(txtLocalName.getText());
        subLedger.setSociety(MainApp.identityDto.getSociety());
        subLedger.setUnionCode(MainApp.identityDto.getUnion().getCode());
        subLedger.setType(cboxType.getValue().getKey());
        subLedger.setActive(true);

        return subLedger;
    }

    private boolean validate() {
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new SubLedgerSaveTask(subLedger, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("subledger"), sb.toString());
                    alert.createAlert();
                    return;
                }
                if (listLedger != null) saveMapping();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("subledger"), resourceBundle.getString("subledger.insert.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/SubLedger.fxml")));

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void saveMapping() {
        System.out.println(listLedger);
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
        var task = new LedgerSubLedgerMappingSaveTask(ledgerSubLedgerMappingList, (short) 0);
        task.setOnSucceeded(e -> {

        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new SubLedgerSaveTask(subLedger, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledger"), sb.toString());
                    alert.createAlert();
                    return;
                }
                if (listLedger != null) saveMapping();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledger"), resourceBundle.getString("ledger.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/SubLedger.fxml")));


            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
    }


}
