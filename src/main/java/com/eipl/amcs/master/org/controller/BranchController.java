package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.task.BranchLoadTask;
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

public class BranchController implements MyInitialization {

    @FXML
    TableView<Branch> tableBranch;
    @FXML
    TableColumn<Branch, String> colCode, colName, colLocalName, colStatus, colAddress, colPinCode, colIfsc;
    @FXML
    TableColumn<Branch, Bank> colBank;
    @FXML
    TableColumn<Branch, State> colState;
    @FXML
    TableColumn<Branch, District> colDistrict;
    @FXML
    TableColumn<Branch, SubDistrict> colSubDistrict;
    @FXML
    TableColumn<Branch, Village> colVillage;
    @FXML
    Button btnClose;
    @FXML
    private StackPane root;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
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
            colBank.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBank()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colIfsc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIfsc()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ?
                    resourceBundle.getString("active") : resourceBundle.getString("inactive")));
        } catch (Exception e) {
            System.out.println("Branch setuptable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        var task = new BranchLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Branch> list = task.get();
                if (list != null)
                    tableBranch.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}


