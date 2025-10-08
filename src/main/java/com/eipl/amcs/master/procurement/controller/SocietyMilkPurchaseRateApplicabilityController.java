package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.task.SocietyMilkPurchaseRateApplicabilityLoadTask;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SocietyMilkPurchaseRateApplicabilityController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<SocietyMilkPurchaseRateApplicability> tableSocietyMilkPurchaseRateApplicability;
    @FXML
    TableColumn<SocietyMilkPurchaseRateApplicability, String> colCode, colUnionCode;
    @FXML
    TableColumn<SocietyMilkPurchaseRateApplicability, LocalDateTime> colWefDate;
    @FXML
    TableColumn<SocietyMilkPurchaseRateApplicability, Society> colSociety;
    @FXML
    TableColumn<SocietyMilkPurchaseRateApplicability, Shift> colShift;
    @FXML
    TableColumn<SocietyMilkPurchaseRateApplicability, SocietyMilkPurchaseRate> colSocietyMilkPurchaseRate;
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
            colUnionCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnionCode()));
            colSociety.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSociety()));
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate()));
            colShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShift()));
            colSocietyMilkPurchaseRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSocietyMilkPurchaseRate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// task API not available
    @Override
    public void loadData() {
        var task = new SocietyMilkPurchaseRateApplicabilityLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SocietyMilkPurchaseRateApplicability> list = task.get();
                if (list != null)
                    tableSocietyMilkPurchaseRateApplicability.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
