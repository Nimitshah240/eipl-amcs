package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.task.LedgerTypeDeleteTask;
import com.eipl.amcs.master.account.task.LedgerTypeLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LedgerTypeController implements MyInitialization, PopupCallback {

    private final ObjectProperty<LedgerType> propLedgerType;
    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerType> tableLedgerType;
    @FXML
    TableColumn<LedgerType, String> colCode, colName, colLocalName, colStatus;
    @FXML
    TableColumn<LedgerType, String> colProfitAndLoss, colBalanceSheet;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete;
    private ResourceBundle resourceBundle;

    public LedgerTypeController() {
        propLedgerType = new SimpleObjectProperty<>();
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
        propLedgerType.addListener((observable, oldValue, newValue) -> {
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
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerTypeAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            LedgerType dto = propLedgerType.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerTypeAddEdit", dto, this);
        });
    }

    @Override
    public void setupTable() {
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
        colProfitAndLoss.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isProfitLoss() ?
                resourceBundle.getString("yes") : resourceBundle.getString("no")));
        colBalanceSheet.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isBalanceSheet() ?
                resourceBundle.getString("yes") : resourceBundle.getString("no")));
        propLedgerType.bind(tableLedgerType.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void loadData() {
        tableLedgerType.setItems(null);
        LedgerTypeLoadTask task = new LedgerTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<LedgerType> list = task.get();
                if (list != null)
                    tableLedgerType.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            LedgerType dto = propLedgerType.get();
            if (dto != null) {
                var task = new LedgerTypeDeleteTask(dto.getCode().toString());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
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
