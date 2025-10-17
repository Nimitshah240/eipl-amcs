package com.eipl.amcs.master.geo.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.task.SubDistrictLoadTask;
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

public class SubDistrictController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<SubDistrict> tableSubDistricts;
    @FXML
    TableColumn<SubDistrict, String> colCode;
    @FXML
    TableColumn<SubDistrict, String> colName, colLocalName;
    @FXML
    TableColumn<SubDistrict, District> colDistrict;
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
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colDistrict.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDistrict()));
        } catch (Exception e) {
            System.out.println("SubDistrict setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new SubDistrictLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SubDistrict> list = task.get();
                if (list != null)
                    tableSubDistricts.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
