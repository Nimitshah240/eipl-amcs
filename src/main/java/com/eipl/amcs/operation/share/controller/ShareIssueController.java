package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.task.ShareIssueDeleteTask;
import com.eipl.amcs.operation.share.task.ShareIssueLoadTask;
import com.eipl.amcs.operation.share.task.ShareIssuePermanantDeleteTask;
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

public class ShareIssueController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Share> propShareIssue;
    @FXML
    private StackPane root;
    @FXML
    private TableView<Share> tableShareIssue;
    @FXML
    private TableColumn<Share, String> colVoucherNo, colNoOfShare, colMemberCode, colMemberName;
    @FXML
    private TableColumn<Share, LocalDate> colDate;
    @FXML
    private TableColumn<Share, BigDecimal> colAmount;
    @FXML
    private Button btnAdd, btnCancel, btnClose, btnDelete, btnReport;
    private ResourceBundle resourceBundle;

    private List<Member> listMembers;
    private List<Customer> listCustomers;

    public ShareIssueController() {
        propShareIssue = new SimpleObjectProperty<>();
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
        propShareIssue.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDelete.setDisable(false);
                btnCancel.setDisable(false);
            } else {
                btnDelete.setDisable(true);
                btnCancel.setDisable(true);
            }
        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ShareIssueAddEdit", null, this, resourceBundle.getString("shareissued"));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnCancel.setOnAction(e -> {
            cancelData();
        });

        btnReport.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "IssueReportPopup", null, this, resourceBundle.getString("shareissued"));
        });
    }

    public void cancelData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("shareissue"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Share dto = propShareIssue.get();
            if (dto != null) {
                var task = new ShareIssueDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("shareissue"),
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
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("shareissue"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Share dto = propShareIssue.get();
            if (dto != null) {
                var task = new ShareIssuePermanantDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("shareissue"),
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

    private void loadMember() {
        var task = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                listMembers = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadCustomer() {
        var task = new CustomerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                listCustomers = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        try {
            colVoucherNo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCode()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIssueDate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShareAmount()));
            colNoOfShare.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNoOfShare().toString()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colMemberCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShareCode()));

            propShareIssue.bind(tableShareIssue.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("ShareIssue setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableShareIssue.setItems(null);
        var task = new ShareIssueLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Share> list = task.get();
                if (list != null)
                    tableShareIssue.setItems(FXCollections.observableList(list.stream().filter(e1 -> !e1.getCancelled()
                            && !e1.getTransferred()).collect(Collectors.toList())));
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
