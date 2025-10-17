package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.base.task.RateTask;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.procurement.dto.RateViewDto;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.task.MemberMilkPurchaseRateLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MemberMilkPurchaseRateController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<MemberMilkPurchaseRate> tableMemberMilkPurchaseRates;
    @FXML
    TableColumn<MemberMilkPurchaseRate, String> colCode, colDescription;
    @FXML
    TableColumn<MemberMilkPurchaseRate, LocalDate> colWefDate;
    @FXML
    TableColumn<MemberMilkPurchaseRate, RateType> colRateType;
    @FXML
    TableColumn<MemberMilkPurchaseRate, Shift> colShift, colShiftApplicable;
    @FXML
    TableColumn<MemberMilkPurchaseRate, String> colRateGenMethodCode;
    @FXML
    Button btnAdd, btnClose, btnView, btnSync;
    private ResourceBundle resourceBundle;

    private final ObjectProperty<MemberMilkPurchaseRate> propRate;

    public MemberMilkPurchaseRateController() {
        propRate = new SimpleObjectProperty<>();
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

        propRate.addListener((observable, oldValue, newValue) -> {
            btnView.setDisable(newValue == null);
        });

        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_MILK_PURCHASE_RATE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/procurement/MemberMilkPurchaseRateAddEdit.fxml")));
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnView.setOnAction(e -> {
            RateViewDto dto = new RateViewDto();
            dto.setRateType((short) 0);
            dto.setMemberRate(propRate.get());

            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "RateView", dto, null);
        });
        btnSync.setOnAction(e -> {
            syncRate();
        });
    }

    private void syncRate() {
        RateTask rateTask = new RateTask();
        MainApp.paneDrop.setVisible(true);
        MainApp.lblMessage.setText("Downloading...");
        rateTask.setOnSucceeded(e -> {
            loadData();
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("rate"),
                    "Successful");
            alert.createAlert();
            MainApp.paneDrop.setVisible(false);
        });
        rateTask.setOnFailed(e -> {
            loadData();
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productpurchaserate"),
                    "Failed");
            alert.createAlert();
            MainApp.paneDrop.setVisible(false);
        });
        new Thread(rateTask).start();
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
            colRateGenMethodCode.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getRateGenerationMethod(data.getValue().getRateGenMethodCode())));
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate().toLocalDate()));
            colWefDate.setCellFactory(new LocalDateCellFactory<>());
            colShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShift()));
            colShiftApplicable.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShiftApplicable()));
            colRateType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRateType()));

            propRate.bind(tableMemberMilkPurchaseRates.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("MemberMilkPurchaseRate setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableMemberMilkPurchaseRates.setItems(null);
        var task = new MemberMilkPurchaseRateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MemberMilkPurchaseRate> list = task.get();
                if (list != null)
                    tableMemberMilkPurchaseRates.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
