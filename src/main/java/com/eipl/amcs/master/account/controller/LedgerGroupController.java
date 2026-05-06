package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.task.LedgerGroupDeleteTask;
import com.eipl.amcs.master.account.task.LedgerGroupLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class LedgerGroupController implements MyInitialization, PopupCallback {

    private final ObjectProperty<LedgerGroup> propLedgerGroup;
    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerGroup> tableLedgerGroup;
    @FXML
    TableColumn<LedgerGroup, String> colCode, colName, colLocalName, colStatus;
    @FXML
    TableColumn<LedgerGroup, LedgerType> colLedgerType;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete, btnClear;
    @FXML
    TextField txtSearch;
    private List<LedgerGroup> ledgerGroupList = new ArrayList<>();
    private ResourceBundle resourceBundle;

    public LedgerGroupController() {
        propLedgerGroup = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        propLedgerGroup.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        setupTable();
        loadData();
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LEDGER_GROUP_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerGroupAddEdit", null, this, resourceBundle.getString("ledgergroup"));
        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LEDGER_GROUP_EDIT"))
                throw new UnAuthorizedAccessException();
            LedgerGroup dto = propLedgerGroup.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerGroupAddEdit", dto, this, resourceBundle.getString("ledgergroup"));
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LEDGER_GROUP_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            search(oldValue, newValue);
        });
        btnClear.setOnAction(e -> {
            loadData();
            txtSearch.setText("");
        });

    }

    public void search(String oldVal, String newVal) {
        if (!newVal.equalsIgnoreCase("")) {
            tableLedgerGroup.setItems(FXCollections.observableList(ledgerGroupList.stream().
                    filter(
                            e1 ->
                                    e1.getCode().toString().contains(newVal.toLowerCase()) ||
                                            e1.getName().toLowerCase().contains(newVal.toLowerCase()) ||
                                            (e1.getNameLocal() != null && e1.getNameLocal().toLowerCase().contains(newVal.toLowerCase()))
                    ).collect(Collectors.toList())));
        }
    }


    @Override
    public void setupTable() {
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
        colLedgerType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerType()));
        propLedgerGroup.bind(tableLedgerGroup.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void loadData() {
        tableLedgerGroup.setItems(null);
        LedgerGroupLoadTask task = new LedgerGroupLoadTask();
        task.setOnSucceeded(e -> {
            try {
                ledgerGroupList = task.get();
                if (ledgerGroupList != null)
                    tableLedgerGroup.setItems(FXCollections.observableList(ledgerGroupList));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("ledgergroup"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            LedgerGroup dto = propLedgerGroup.get();
            if (dto != null) {
                var task = new LedgerGroupDeleteTask(dto.getCode().toString());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgergroup"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
