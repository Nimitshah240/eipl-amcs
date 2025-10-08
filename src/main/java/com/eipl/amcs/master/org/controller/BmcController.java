package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.service.BmcService;
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

public class BmcController implements MyInitialization {

    @FXML
    TableView<Bmc> tableBmc;
    @FXML
    TableColumn<Bmc, String> colCode, colName, colLocalName, colCodeEx, colAddress, colCity, colPhoneNo,
            colContactPerson, colContactPersonMobileNo;
    @FXML
    TableColumn<Bmc, Union> colUnion;
    @FXML
    TableColumn<Bmc, Mcc> colMcc;
    @FXML
    TableColumn<Bmc, State> colState;
    @FXML
    TableColumn<Bmc, District> colDistrict;
    @FXML
    TableColumn<Bmc, SubDistrict> colSubDistrict;
    @FXML
    TableColumn<Bmc, Village> colVillage;
    @FXML
    TableColumn<Bmc, Hamlet> colHamlet;
    @FXML
    Button btnClose;
    @FXML
    private StackPane root;


    private BmcService bmcService;

    public BmcController() {
        bmcService = MainApp.context.getBean(BmcService.class);
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
            colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));
            colPhoneNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));
            colContactPerson.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));
            colContactPersonMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPersonMobileNo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<Bmc> list = bmcService.findAll();
            if (list != null)
                tableBmc.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


