package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.Plant;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.service.PlantService;
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

import static com.eipl.amcs.MainApp.context;

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

    private PlantService plantService;

    public PlantController() {
        plantService = context.getBean(PlantService.class);
    }

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
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<Plant> list = plantService.findAll();
            if (list != null)
                tablePlant.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


