package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.task.ShareCancelledLoadTask;
import com.eipl.amcs.operation.share.task.ShareIssueRevertTask;
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
import java.util.stream.Collectors;

public class ShareCancelledController implements MyInitialization, PopupCallback {

    @FXML
    private StackPane root;

    @FXML
    private TableView<Share> tableShareCancelled;
    @FXML
    private TableColumn<Share, String> colVoucherNo, colNoOfShare, colMemberCode, colMemberName, colCancelledDate;
    @FXML
    private TableColumn<Share, LocalDate> colDate;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private TableColumn<Share, BigDecimal> colAmount;
    @FXML
    private Button btnClose, btnSearch, btnReport, btnRevert;
    private final ObjectProperty<Share> propShareIssue;
    private ResourceBundle resourceBundle;

    private String name;
    private List<Member> listMembers;
    private List<Customer> listCustomers;

    public ShareCancelledController() {
        propShareIssue = new SimpleObjectProperty<>();
    }


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propShareIssue.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnRevert.setDisable(false);
            } else {
                btnRevert.setDisable(true);
            }
        });
        setupTable();
        loadMember();
        loadCustomer();

        btnSearch.setOnAction(e -> loadData());
        dpFromDate.setValue(LocalDate.now());
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setValue(LocalDate.now());
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
        loadData();
        propShareIssue.addListener((observable, oldValue, newValue) -> {

        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnReport.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ShareCancelReportPopup", null, this);
        });
        btnRevert.setOnAction(e -> {
            revertData();
        });
    }

    private void revertData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("sharecancel"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Share dto = propShareIssue.get();
            if (dto != null) {
                var task = new ShareIssueRevertTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || respDelete.booleanValue() == false) {
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
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colMemberCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShareCode()));
            colNoOfShare.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNoOfShare().toString()));
            colCancelledDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCancelDate() != null ? data.getValue().getCancelDate().toString() : ""));
            propShareIssue.bind(tableShareCancelled.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("ShareCancelled setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableShareCancelled.setItems(null);
        var task = new ShareCancelledLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<Share> list = task.get();
                if (list != null)
                    tableShareCancelled.setItems(FXCollections.observableList(list.stream().filter(e1 -> e1.getCancelled() && !e1.getTransferred()).collect(Collectors.toList())));
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
