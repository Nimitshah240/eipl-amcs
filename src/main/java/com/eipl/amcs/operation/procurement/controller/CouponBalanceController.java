package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.task.CouponBalanceLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class CouponBalanceController implements MyInitialization, PopupCallback {


    private ObservableList<CouponBalance> listCouponBalance;
    @FXML
    private Button btnCancel;
    @FXML
    private TableView<CouponBalance> tableIssueCoupon;
    @FXML
    private TableColumn<CouponBalance, String> colCode;
//    @FXML
//    private TableColumn<CouponIssue, String>  colName;
    @FXML
    private TableColumn<CouponBalance, MilkType> colMilkType;

    @FXML
    private TableColumn<CouponBalance, Double> colBalance;
    @FXML
    private TableColumn<CouponBalance, LocalDate> colDate;
    private ObjectProperty<CouponBalance> propertyCouponIssue = new SimpleObjectProperty<>();
    @FXML
    private StackPane root;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadData();
        btnCancel.setOnAction(e -> this.stage.close());
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
//            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsumerName()));
            colBalance.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBalance()));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            tableIssueCoupon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            propertyCouponIssue.bind(tableIssueCoupon.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("LocalMilkSake setuptable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        if (listCouponBalance != null)
            listCouponBalance.clear();
        tableIssueCoupon.setPlaceholder(new Label("Fetching Data..."));

        CouponBalanceLoadTask stateTask = new CouponBalanceLoadTask();
        stateTask.setOnSucceeded(event -> {
            try {
                if (listCouponBalance == null)
                    listCouponBalance = FXCollections.observableArrayList(stateTask.get());
                else {
                    listCouponBalance.clear();
                    listCouponBalance.addAll(stateTask.get());
                }
                tableIssueCoupon.setItems(listCouponBalance);
                if (listCouponBalance != null && listCouponBalance.isEmpty())
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
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }


}
