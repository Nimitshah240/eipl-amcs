package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.task.ShareRateDeleteTask;
import com.eipl.amcs.operation.share.task.ShareRateLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ShareRateController implements MyInitialization, PopupCallback {
    private final ObjectProperty<ShareRate> propLocalMilkSaleRate;
    @FXML
    StackPane root;
    @FXML
    TableView<ShareRate> tableLocalMilkSaleRates;
    @FXML
    TableColumn<ShareRate, Number> colRate;
    @FXML
    TableColumn<ShareRate, LocalDate> colWefDate;
    @FXML
    Button btnClose, btnAdd, btnDelete;
    private ResourceBundle resourceBundle;

    public ShareRateController() {
        propLocalMilkSaleRate = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadData();
        btnDelete.setDisable(true);
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ShareRateAddEdit", null, this,resourceBundle.getString("sharerate"));
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        propLocalMilkSaleRate.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
        });
    }

    @Override
    public void setupTable() {
        try {
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate()));
            colWefDate.setCellFactory(new LocalDateCellFactory<>());
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShareAmount()));
            propLocalMilkSaleRate.bind(tableLocalMilkSaleRates.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("ShareRate setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableLocalMilkSaleRates.setItems(null);
        var task = new ShareRateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<ShareRate> list = task.get();
                if (list != null)
                    tableLocalMilkSaleRates.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            ShareRate dto = propLocalMilkSaleRate.get();
            if (dto != null) {
                var task = new ShareRateDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
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
