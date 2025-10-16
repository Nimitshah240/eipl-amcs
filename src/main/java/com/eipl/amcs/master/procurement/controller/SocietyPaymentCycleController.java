package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleDeleteTask;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleLoadByDateTask;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SocietyPaymentCycleController implements MyInitialization, PopupCallback {
    @FXML
    StackPane root;
    @FXML
    TableView<SocietyPaymentCycle> tableSocietyPaymentCycles;
    @FXML
    TableColumn<SocietyPaymentCycle, String> colCode;
    @FXML
    TableColumn<SocietyPaymentCycle, Shift> colFromShift, colToShift;
    @FXML
    TableColumn<SocietyPaymentCycle, Number> colIntervalValue;
    @FXML
    TableColumn<SocietyPaymentCycle, LocalDate> colFromDate, colToDate;
    @FXML
    TableColumn<SocietyPaymentCycle, String> colIsBilling, colLockBillingProcess;
    @FXML
    Button btnClose, btnEdit, btnGenerate, btnDelete, btnSearch;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    private ResourceBundle resourceBundle;
    private final ObjectProperty<SocietyPaymentCycle> propPaymentCycle;

    public SocietyPaymentCycleController() {
        propPaymentCycle = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;


        loadData();
        setupTable();
//        dpFromDate.setValue(LocalDate.now());
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
//        dpToDate.setValue(LocalDate.now());
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
        btnSearch.setOnAction(e -> loadData());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_SOCIETY_PAYMENT_CYCLE_EDIT"))
                throw new UnAuthorizedAccessException();
            SocietyPaymentCycle paymentCycle = propPaymentCycle.get();
            if (paymentCycle != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SocietyPaymentCycleEdit", paymentCycle, this);
        });
        btnGenerate.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_SOCIETY_PAYMENT_CYCLE_GENERATE"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SocietyPaymentCycleGenerate", null, this);
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_SOCIETY_PAYMENT_CYCLE_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });

        propPaymentCycle.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colIntervalValue.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIntervalValue()));
            colFromShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromShift()));
            colToShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToShift()));
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate().toLocalDate()));
            colFromDate.setCellFactory(new LocalDateCellFactory<>());
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate().toLocalDate()));
            colToDate.setCellFactory(new LocalDateCellFactory<>());
            colIsBilling.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBilling() ? "YES" : "No"));
            colLockBillingProcess.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLockBillingProcess() ? "YES" : "No"));

            propPaymentCycle.bind(tableSocietyPaymentCycles.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("SocietyPaymentCycle setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableSocietyPaymentCycles.setItems(null);
        if (dpFromDate.getValue() == null) {
            var task = new SocietyPaymentCycleLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<SocietyPaymentCycle> list = task.get();
                    if (list != null)
                        tableSocietyPaymentCycles.setItems(FXCollections.observableList(list));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else {
            var task = new SocietyPaymentCycleLoadByDateTask(dpFromDate.getValue(), dpToDate.getValue());
            task.setOnSucceeded(e -> {
                try {
                    List<SocietyPaymentCycle> list = task.get();
                    if (list != null)
                        tableSocietyPaymentCycles.setItems(FXCollections.observableList(list));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    @Override
    public void deleteData() {
        SocietyPaymentCycle dto = propPaymentCycle.get();
        if (dto == null)
            return;

        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            var task = new SocietyPaymentCycleDeleteTask(dto.getCode());
            task.setOnSucceeded(e -> {
                try {
                    Boolean respDelete = task.get();
                    if (respDelete == null || !respDelete.booleanValue()) {
                        MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
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

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
