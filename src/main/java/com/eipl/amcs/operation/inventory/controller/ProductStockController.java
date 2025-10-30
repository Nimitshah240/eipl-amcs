package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import com.eipl.amcs.operation.inventory.task.ProductStockLoadTask;
import javafx.beans.property.SimpleDoubleProperty;
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

public class ProductStockController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<ProductStock> tableProductStock;
    @FXML
    TableColumn<ProductStock, Product> colProduct;
    @FXML
    TableColumn<ProductStock, Number> colStock;
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
            colProduct.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduct()));
            colStock.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getStock().toBigInteger().doubleValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new ProductStockLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<ProductStock> list = task.get();
                if (list != null)
                    tableProductStock.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
