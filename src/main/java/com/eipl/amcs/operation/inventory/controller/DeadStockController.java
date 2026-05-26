package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.operation.inventory.model.DeadStock;
import com.eipl.amcs.operation.inventory.task.DeadStockDeleteTask;
import com.eipl.amcs.operation.inventory.task.DeadStockLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class DeadStockController implements MyInitialization, PopupCallback {

    private final ObjectProperty<DeadStock> propDeadStockDto;
    @FXML
    StackPane root;
    @FXML
    TableView<DeadStock> tableDeadStock;
    @FXML
    TableColumn<DeadStock, String> colCode;
    @FXML
    TableColumn<DeadStock, BigDecimal> colAmount, colQty;
    @FXML
    TableColumn<DeadStock, LocalDate> colPurchaseDate;
    @FXML
    TableColumn<DeadStock, String> colLedgerAccount;
    @FXML
    TableColumn<DeadStock, String> colName, colNameLocal;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnExport, btnSearch;
    private ResourceBundle resourceBundle;

    public DeadStockController() {
        propDeadStockDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        propDeadStockDto.addListener((observable, oldValue, newValue) -> {
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
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "DeadStockAddEdit", null, this, resourceBundle.getString("deadstock"));
        });

        btnEdit.setOnAction(e -> {
            DeadStock dto = propDeadStockDto.get();
            if (dto != null)
                editDeadStock(dto);
        });

        btnDelete.setOnAction(e -> {
            deleteData();
        });

        tableDeadStock.setRowFactory(tv -> {
            TableRow<DeadStock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    DeadStock data = row.getItem();
                    editDeadStock(data);
                }
            });
            return row;
        });

        tableDeadStock.setOnKeyPressed(event -> {
            DeadStock dto = tableDeadStock.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propDeadStockDto.get();
                    if (dto != null)
                        deleteData();
                    break;
                case ENTER:
                    editDeadStock(dto);
                    break;
            }
        });
    }

    @Override
    public void setupTable() {
        colCode.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCode()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colNameLocal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNameLocal()));
        colQty.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQty()));
        colAmount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        colPurchaseDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPurchaseDate()));
        colLedgerAccount.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLedger().getName()));

        propDeadStockDto.bind(tableDeadStock.getSelectionModel().selectedItemProperty());
    }

    private void editDeadStock(DeadStock deadStock) {
        if (deadStock != null)
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "DeadStockAddEdit", deadStock, this, resourceBundle.getString("deadstock"));
    }

    @Override
    public void loadData() {
        var task = new DeadStockLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<DeadStock> deadStocks = task.get();
                if (deadStocks != null) {
                    tableDeadStock.setItems(FXCollections.observableArrayList(deadStocks));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        DeadStock deadStock = propDeadStockDto.get();
        if (deadStock != null) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("deadstock"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                var task = new DeadStockDeleteTask(deadStock.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean result = task.get();
                        if (result != null && result) {
                            MyAlert infoAlert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("deadstock"),
                                    resourceBundle.getString("data.delete.success"));
                            infoAlert.createAlert();
                            loadData();
                        } else {
                            MyAlert errorAlert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("deadstock"),
                                    resourceBundle.getString("data.delete.fail"));
                            errorAlert.createAlert();
                        }
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