package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.task.CouponIssueDeleteTask;
import com.eipl.amcs.operation.procurement.task.CouponIssueLoadTask;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class CouponIssueController implements MyInitialization, PopupCallback {


    private ObservableList<CouponIssue> listCouponIssue;
    @FXML
    private Button btnCancel, btnAddSave, btnEditUpdate, btnDelete,btnView;
    @FXML
    private TableView<CouponIssue> tableIssueCoupon;
    @FXML
    private TableColumn<CouponIssue, String> colName, colCode, colConsumerType;
    @FXML
    private TableColumn<CouponIssue, MilkType> colMilkType;
    @FXML
    private TableColumn<CouponIssue, Number> colAmount;
    @FXML
    private TableColumn<CouponIssue, Double> colBalance;
    @FXML
    private TableColumn<CouponIssue, LocalDate> colDate;
    private ObjectProperty<CouponIssue> propertyCouponIssue = new SimpleObjectProperty<>();

    private ResourceBundle resourceBundle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadData();
        propertyCouponIssue.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEditUpdate.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEditUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        loadData();
        btnCancel.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAddSave.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CouponIssueAddEdit", null, this);
        });
        btnView.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CouponBalance", null, this);
        });
        btnEditUpdate.setOnAction(e -> {
            CouponIssue dto = propertyCouponIssue.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CouponIssueAddEdit", dto, this);
        });

        btnDelete.setOnAction(e -> {
            deleteData();
        });
        FocusUtils.requestFocus(btnAddSave);
    }


    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> {
                String code = data.getValue().getConsumerCode();
                String lastFour = (code != null && code.length() >= 4)
                        ? code.substring(code.length() - 4)
                        : code;
                return new SimpleObjectProperty<>(lastFour);
            });
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIssueDate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colConsumerType.setCellValueFactory(data -> new SimpleStringProperty(resourceBundle.getString(data.getValue().getConsumerTypeString().toLowerCase())));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsumerName()));
            tableIssueCoupon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            propertyCouponIssue.bind(tableIssueCoupon.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("LocalMilkSake setuptable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }

    @Override
    public void loadData() {
        if (listCouponIssue != null)
            listCouponIssue.clear();
        tableIssueCoupon.setPlaceholder(new Label("Fetching Data..."));

        CouponIssueLoadTask stateTask = new CouponIssueLoadTask();
        stateTask.setOnSucceeded(event -> {
            try {
                if (listCouponIssue == null)
                    listCouponIssue = FXCollections.observableArrayList(stateTask.get());
                else {
                    listCouponIssue.clear();
                    listCouponIssue.addAll(stateTask.get());
                }
                tableIssueCoupon.setItems(listCouponIssue);
                if (listCouponIssue != null && listCouponIssue.isEmpty())
                    tableIssueCoupon.setPlaceholder(new Label("No Data Available."));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(stateTask).start();

    }

    @Override
    public void deleteData() {
        CouponIssue couponIssue = propertyCouponIssue.get();
        if (couponIssue != null) {
            MyAlert alert1 = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert1.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                CouponIssueDeleteTask deleteTask = new CouponIssueDeleteTask(couponIssue);
                deleteTask.setOnSucceeded(e -> {
                    boolean success = deleteTask.getValue();
                    if (success) {
                        new InformationAlert(MainApp.getStage(),
                                resourceBundle.getString("couponissue.title"),
                                resourceBundle.getString("alert.delete.success")).createAlert();
                        loadData();
                    } else {
                        new ErrorAlert(MainApp.getStage(),
                                resourceBundle.getString("couponissue.title"),
                                resourceBundle.getString("couponissue.alert.delete.message")).createAlert();
                    }
                });
                deleteTask.setOnFailed(ev -> {
                    Throwable ex = deleteTask.getException();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString(ex.getMessage()));
                    alert.createAlert();
                });
                new Thread(deleteTask).start();
            }
        }
    }
}
