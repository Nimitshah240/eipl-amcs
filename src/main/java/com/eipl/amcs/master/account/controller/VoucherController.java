package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.task.VoucherDeleteTask;
import com.eipl.amcs.master.account.task.VoucherLoadTask;
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

public class VoucherController implements MyInitialization, PopupCallback {
    private final ObjectProperty<VoucherDto> propVoucherDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<VoucherDto> tableVoucher;
    @FXML
    TableColumn<VoucherDto, String> colType, colVoucherDate, colVoucherNo, colRefNo, colRemarks;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnLedger;
    private ResourceBundle resourceBundle;

    public VoucherController() {
        propVoucherDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propVoucherDto.addListener((observable, oldValue, newValue) -> {
            //                btnEdit.setDisable(false);
            //                btnEdit.setDisable(true);
            btnDelete.setDisable(newValue == null);
        });
        setupTable();
        loadData();
        btnAdd.setOnAction(e -> {
            VoucherAddEditController controller = (VoucherAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/VoucherAddEdit.fxml"));
            controller.setVoucher(null);
            MainApp.getContentPane().setCenter(controller.getRoot());
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            if (propVoucherDto.get().getVoucher().getCancelled()) {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                        resourceBundle.getString("cancelled.can.not.delete"));
                alert.createAlert();
            } else {
                if (!propVoucherDto.get().getVoucher().getCancelled()) {
                    if (!propVoucherDto.get().getVoucher().getAutoPosted())
                        deleteData();
                    else {
                        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                                resourceBundle.getString("autoposted.can.not.delete"));
                        alert.createAlert();
                    }
                }
            }
        });
        btnLedger.setOnAction(e -> {
            if (propVoucherDto.get() != null) {
                VoucherLedgerController controller = (VoucherLedgerController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/master/account/VoucherLedger.fxml"));
                controller.setVoucher(propVoucherDto.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            }
        });
//        btnEdit.setOnAction(e -> {
//            Voucher dto = propVoucherDto.get().getVoucher();
//            if (dto != null) {
//                VoucherAddEditController controller = (VoucherAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/VoucherAddEdit.fxml"));
//                controller.setVoucher(dto);
//                MainApp.getContentPane().setCenter(controller.getRoot());
//            }
//        });

    }


    @Override
    public void setupTable() {
        colType.setCellValueFactory(data -> new SimpleStringProperty(
                MainApp.locale.equalsIgnoreCase("en") ?
                        data.getValue().getVoucher().getVoucherType().getName() :
                        data.getValue().getVoucher().getVoucherType().getNameLocal()));
        colVoucherNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getCode()));
        colVoucherDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getVoucherDate().toString()));
        colRefNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getBillNo()));
        colRemarks.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getRemarks()));
        propVoucherDto.bind(tableVoucher.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void loadData() {
        tableVoucher.setItems(null);
        var task = new VoucherLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<VoucherDto> list = task.get();
                if (list != null) tableVoucher.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("voucher"), resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Voucher dto = propVoucherDto.get().getVoucher();
            if (dto != null) {
                var task = new VoucherDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"), resourceBundle.getString("error.occurred"));
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

}
