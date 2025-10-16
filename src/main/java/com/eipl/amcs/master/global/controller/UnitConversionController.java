package com.eipl.amcs.master.global.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.model.UnitConversion;
import com.eipl.amcs.master.global.task.UnitConversionLoadTask;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class UnitConversionController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<UnitConversion> tableUnitConversions;
    @FXML
    TableColumn<UnitConversion, Number> colCode, colConversionFactor;
    @FXML
    TableColumn<UnitConversion, Unit> colFromUnit, colToUnit;
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
            colConversionFactor.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getConversionFactor()));
            colFromUnit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromUnit()));
            colToUnit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToUnit()));
        } catch (Exception e) {
            System.out.println("UnitConversion setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new UnitConversionLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<UnitConversion> list = task.get();
                if (list != null)
                    tableUnitConversions.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}

