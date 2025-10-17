package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.task.ShareIssueDeleteTask;
import com.eipl.amcs.operation.share.task.ShareIssueLoadTask;
import com.eipl.amcs.operation.share.task.ShareTranferRevertTask;
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
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ShareTransferController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Share> propShareTransfer;
    @FXML
    private StackPane root;
    @FXML
    private TableView<Share> tableShareTransfer;
    @FXML
    private TableColumn<Share, String> colVoucherNo, colNoOfShare, colOldMemberName, colNewMemberName, colNewMemberCode, colOldMemberCode;
    @FXML
    private TableColumn<Share, LocalDate> colDate;
    @FXML
    private TableColumn<Share, BigDecimal> colAmount;
    @FXML
    private Button btnAdd, btnClose, btnReport, btnRevert;
    private ResourceBundle resourceBundle;
    private String name;
    private List<Member> listMembers;
    private List<Customer> listCustomers;

    public ShareTransferController() {
        propShareTransfer = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propShareTransfer.addListener((observable, oldValue, newValue) -> {
            btnRevert.setDisable(newValue == null);
        });
        setupTable();

        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ShareTransferAddEdit", null, this);
        });

//        btnDelete.setOnAction(e -> {
//            deleteData();
//        });
        btnRevert.setOnAction(e -> {
            revertData();
        });
        btnReport.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ShareTransferReportPopup", null, this);
        });
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("sharetransfer"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Share dto = propShareTransfer.get();
            if (dto != null) {
                var task = new ShareIssueDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharetransfer"),
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
//    public void revertData() {
//        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("shareissue"),
//                resourceBundle.getString("alert.delete"));
//        Optional<ButtonType> resp = alert.createConfirmationAlert();
//        if (resp.isPresent() && resp.get() == ButtonType.OK) {
//            Share dto = propShareTransfer.get();
//            if (dto != null) {
//                ShareTranferRevertTask task = new ShareTranferRevertTask(dto.getCode());
//                task.setOnSucceeded(e -> {
//                    try {
//                        Boolean respDelete = task.get();
//                        if (respDelete == null || respDelete.booleanValue() == false) {
//                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("shareissue"),
//                                    resourceBundle.getString("error.occurred"));
//                            alert1.createAlert();
//                            return;
//                        }
//                        loadData();
//                    } catch (InterruptedException | ExecutionException ex) {
//                        ex.printStackTrace();
//                    }
//                });
//                new Thread(task).start();
//            }
//        }
//    }

    private void revertData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("sharetransfer"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Share dto = propShareTransfer.get();
            if (dto != null) {
                ShareTranferRevertTask task = new ShareTranferRevertTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharetransfer"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
//                deleteData();
                new Thread(task).start();
            }
        }
    }

    @Override
    public void setupTable() {
        try {
            colVoucherNo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCode()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShareAmount()));
            colNoOfShare.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNoOfShare().toString()));
            colNewMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIssueDate()));
            propShareTransfer.bind(tableShareTransfer.getSelectionModel().selectedItemProperty());
            colNewMemberCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShareCode()));
            colOldMemberCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTransferredFromCode()));
            colOldMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransferredFrom().toMemberName()));
        } catch (Exception e) {
            System.out.println("ShareTransfer setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableShareTransfer.setItems(null);
        var task = new ShareIssueLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Share> list = task.get();
                if (list != null)
                    tableShareTransfer.setItems(FXCollections.observableList(list.stream().filter(e1 -> !e1.getCancelled()
                            && e1.getTransferredFrom() != null).collect(Collectors.toList())));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }


}
