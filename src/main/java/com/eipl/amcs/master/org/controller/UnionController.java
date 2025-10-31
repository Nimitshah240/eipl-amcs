package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.task.UnionLoadTask;
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

public class UnionController implements MyInitialization {

    @FXML
    TableView<Union> tableUnion;
    @FXML
    TableColumn<Union, String> colCode, colCity, colName, colLocalName, colCodeEx, colStatus, colRegistrationCode, colPhoneNo, colContactPerson, colContactPersonMobileNo;
    @FXML
    TableColumn<Union, LocalDate> colRegistrationDate;
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
            colCodeEx.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx()));
            colRegistrationCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colRegistrationDate.setCellFactory(new LocalDateCellFactory<>());

            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colRegistrationDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRegistrationDate()));
            colPhoneNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));
            colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));
            colContactPerson.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));
            colContactPersonMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPersonMobileNo()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ?
                    resourceBundle.getString("active") : resourceBundle.getString("inactive")));
        } catch (Exception e) {
            System.out.println("Union setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new UnionLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Union> list = task.get();
                if (list != null)
                    tableUnion.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}


