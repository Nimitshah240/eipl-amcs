package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.service.ProductGroupService;
import com.eipl.amcs.master.inventory.task.ProductGroupLoadTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static com.eipl.amcs.MainApp.context;

public class ProductGroupController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<ProductGroup> tableProductGroup;
    @FXML
    TableColumn<ProductGroup, String> colCode, colName, colLocalName, colIsActive;
    @FXML
    TableColumn<ProductGroup, Unit> colBaseUnit;
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
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode().toString()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colBaseUnit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getUnit()));
            colIsActive.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
        } catch (Exception e) {
            System.out.println("ProductGroup setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        ProductGroupLoadTask task = new ProductGroupLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<ProductGroup> list = task.get();
                if (list != null)
                    tableProductGroup.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
