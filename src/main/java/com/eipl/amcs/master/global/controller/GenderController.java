package com.eipl.amcs.master.global.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.service.DistrictService;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static com.eipl.amcs.MainApp.context;

public class GenderController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<Gender> tableGenders;
    @FXML
    TableColumn<Gender, Number> colCode;
    @FXML
    TableColumn<Gender, String> colName, colLocalName;
    @FXML
    Button btnClose;

    private GenderService genderService;

    @Override
    public Node getRoot() {
        return root;
    }

    public GenderController() {
        genderService = context.getBean(GenderService.class);
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
            colCode.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<Gender> list = genderService.findAll();
            if (list != null)
                tableGenders.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        var task = new GenderLoadTask();
//        task.setOnSucceeded(e -> {
//            try {
//                List<Gender> list = task.get();
//                if (list != null)
//                    tableGenders.setItems(FXCollections.observableList(list));
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
    }
}
