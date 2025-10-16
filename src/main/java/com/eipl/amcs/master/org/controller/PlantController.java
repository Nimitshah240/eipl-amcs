package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.Plant;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.task.PlantLoadTask;
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

public class PlantController implements MyInitialization {

    @FXML
    TableView<Plant> tablePlant;
    @FXML
    TableColumn<Plant, String> colCode, colName, colLocalName, colCodeEx, colAddress, colCity, colPhoneNo, colContactPerson, colContactPersonMobileNo;
    @FXML
    TableColumn<Plant, Union> colUnion;
    @FXML
    TableColumn<Plant, State> colState;
    @FXML
    TableColumn<Plant, District> colDistrict;
    @FXML
    TableColumn<Plant, SubDistrict> colSubDistrict;
    @FXML
    TableColumn<Plant, Village> colVillage;
    @FXML
    TableColumn<Plant, Hamlet> colHamlet;
    @FXML
    Button btnClose;
    @FXML
    private StackPane root;

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
            colCodeEx.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colPhoneNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));
            colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));
            colContactPerson.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));
            colContactPersonMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPersonMobileNo()));
        } catch (Exception e) {
            System.out.println("Plant setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new PlantLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Plant> list = task.get();
                if (list != null)
                    tablePlant.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}


