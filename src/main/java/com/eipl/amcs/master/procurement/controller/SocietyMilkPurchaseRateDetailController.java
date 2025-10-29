package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateDetail;
import com.eipl.amcs.master.procurement.task.SocietyMilkPurchaseRateDetailLoadTask;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SocietyMilkPurchaseRateDetailController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<SocietyMilkPurchaseRateDetail> tableSocietyMilkPurchaseRateDetails;
    @FXML
    TableColumn<SocietyMilkPurchaseRateDetail, String> colCode;
    @FXML
    TableColumn<SocietyMilkPurchaseRateDetail, MilkQualityType> colMilkQualityType;
    @FXML
    TableColumn<SocietyMilkPurchaseRateDetail, MilkType> colMilkType;
    @FXML
    TableColumn<SocietyMilkPurchaseRateDetail, SocietyMilkPurchaseRate> colSocietyMilkPurchaseRate;
    @FXML
    TableColumn<SocietyMilkPurchaseRateDetail, Number> colFat, colSnf, colRate;
    @FXML
    Button btnClose;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFat()));
            colSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSnf()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colMilkQualityType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQualityType()));
            colSocietyMilkPurchaseRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSocietyMilkPurchaseRate()));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
        } catch (Exception e) {
            System.out.println("SocietyMilkPurchaseRateDetail setuptable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        var task = new SocietyMilkPurchaseRateDetailLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SocietyMilkPurchaseRateDetail> list = task.get();
                if (list != null)
                    tableSocietyMilkPurchaseRateDetails.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
