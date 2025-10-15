package com.eipl.amcs.master.global.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import javafx.beans.property.SimpleIntegerProperty;
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

import static com.eipl.amcs.MainApp.context;

public class MilkQualityTypeController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<MilkQualityType> tableMilkQualityTypes;
    @FXML
    TableColumn<MilkQualityType, Number> colCode;
    @FXML
    TableColumn<MilkQualityType, String> colName, colLocalName;
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
            colCode.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        } catch (Exception e) {
            System.out.println("MilkQuality setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new MilkQualityTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkQualityType> list = task.get();
                if (list != null)
                    tableMilkQualityTypes.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
