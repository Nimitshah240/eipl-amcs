package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.task.LocalMilkSaleRateDeleteTask;
import com.eipl.amcs.master.procurement.task.LocalMilkSaleRateLoadTask;
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

public class LocalMilkSaleRateController implements MyInitialization, PopupCallback {
    private final ObjectProperty<LocalMilkSaleRate> propLocalMilkSaleRate;
    @FXML
    StackPane root;
    @FXML
    TableView<LocalMilkSaleRate> tableLocalMilkSaleRates;
    @FXML
    TableColumn<LocalMilkSaleRate, MilkType> colMilkType;
    @FXML
    TableColumn<LocalMilkSaleRate, MilkClass> colMilkClass;
    @FXML
    TableColumn<LocalMilkSaleRate, Number> colRate;
    @FXML
    TableColumn<LocalMilkSaleRate, LocalDate> colWefDate;
    @FXML
    Button btnClose, btnAdd, btnDelete;
    private ResourceBundle resourceBundle;

    public LocalMilkSaleRateController() {
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
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LOCAL_MILK_SALE_RATE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LocalMilkSaleRateAddEdit", null, this, "Local Milk Sale Rate");
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LOCAL_MILK_SALE_RATE_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
    }

    @Override
    public void setupTable() {
        try {
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate()));
            colWefDate.setCellFactory(new LocalDateCellFactory<>());
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colMilkClass.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkClass()));
            propLocalMilkSaleRate.bind(tableLocalMilkSaleRates.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("LocalMilksaleRate setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableLocalMilkSaleRates.setItems(null);
        var task = new LocalMilkSaleRateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<LocalMilkSaleRate> list = task.get();
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
            LocalMilkSaleRate dto = propLocalMilkSaleRate.get();
            if (dto != null) {
                var task = new LocalMilkSaleRateDeleteTask(dto.getCode());
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
