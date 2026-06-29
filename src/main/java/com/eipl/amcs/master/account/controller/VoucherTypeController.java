package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.task.VoucherTypeDeleteTask;
import com.eipl.amcs.master.account.task.VoucherTypeLoadTask;
import com.eipl.amcs.utils.TableLocalizationUtil;
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

public class VoucherTypeController implements MyInitialization, PopupCallback {

    private final ObjectProperty<VoucherType> propVoucherType;
    @FXML
    AnchorPane root;
    @FXML
    TableView<VoucherType> tableVoucherType;
    @FXML
    TableColumn<VoucherType, String> colCode, colName, colLocalName, colStatus, colVoucherType, colCreditDebit;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete;
    private ResourceBundle resourceBundle;

    public VoucherTypeController() {
        propVoucherType = new SimpleObjectProperty<>();
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
        propVoucherType.addListener((observable, oldValue, newValue) -> {
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
            if (!MainApp.user.getPermissions().contains("ACTION_VOUCHER_TYPE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherTypeAddEdit", null, this, resourceBundle.getString("vouchertype"));
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_VOUCHER_TYPE_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_VOUCHER_TYPE_EDIT"))
                throw new UnAuthorizedAccessException();
            VoucherType dto = propVoucherType.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherTypeAddEdit", dto, this, resourceBundle.getString("vouchertype"));
        });
    }

    @Override
    public void setupTable() {
        tableVoucherType.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colCode.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCode())));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ?
                resourceBundle.getString("active") : resourceBundle.getString("inactive")));
        colVoucherType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucherType() == 0 ?
                resourceBundle.getString("cash") : resourceBundle.getString("bank")));
        colCreditDebit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreditDebit() ?
                resourceBundle.getString("credit") : resourceBundle.getString("debit")));
        propVoucherType.bind(tableVoucherType.getSelectionModel().selectedItemProperty());
        TableLocalizationUtil.localizeTable(tableVoucherType);
    }

    @Override
    public void loadData() {
        tableVoucherType.setItems(null);
        VoucherTypeLoadTask task = new VoucherTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<VoucherType> list = task.get();
                if (list != null)
                    tableVoucherType.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            VoucherType dto = propVoucherType.get();
            if (dto != null) {
                var task = new VoucherTypeDeleteTask(dto.getCode().toString());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
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
