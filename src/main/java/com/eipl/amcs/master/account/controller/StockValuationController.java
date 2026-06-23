package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.account.model.ProductStockValuation;
import com.eipl.amcs.report.task.LoadStockValuationTask;
import com.eipl.amcs.report.task.StockValuationTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class StockValuationController implements MyInitialization, PopupCallback {
    private final ObjectProperty<ProductStockValuation> propStockValuationDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<ProductStockValuation> tableStockValuation;
    @FXML
    TableColumn<ProductStockValuation, String> colName, colUnit, colQuantity, colRate, colAmount;
    @FXML
    Button btnClose, btnGenerate;
    private ResourceBundle resourceBundle;
    @FXML
    private E_DatePicker dpGenerate;
    @FXML
    private Label lblTotal;

    public StockValuationController() {
        propStockValuationDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpGenerate.setValue(LocalDate.now());
        setupTable();
        loadData();
        btnGenerate.setOnAction(e -> {
            loadProductStockValuation();
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        dpGenerate.setConverter(new LocalDateConvertor());
    }

    @Override
    public void loadData() {
        try {
            var task = new LoadStockValuationTask(MainApp.identityDto.getSociety().getCode(), dpGenerate.getValue(), MainApp.getLocale());
            task.setOnSucceeded(e -> {
                try {
                    List<ProductStockValuation> list = task.get();
                    if (list != null) {
                        tableStockValuation.setItems(FXCollections.observableList(list));
                        calculateTotal(list);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupTable() {
        colQuantity.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStock())));
        colAmount.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getValuation())));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        colRate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStock() != 0D ? String.format("%.2f", data.getValue().getValuation() / data.getValue().getStock()) : "0"));
        colUnit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnit().toString()));
        propStockValuationDto.bind(tableStockValuation.getSelectionModel().selectedItemProperty());
    }


    public void loadProductStockValuation() {
        var task = new StockValuationTask(MainApp.identityDto.getSociety().getCode(), dpGenerate.getValue(), MainApp.getLocale());
        if (MainApp.getProperty("fifo.process", "FIFO").equalsIgnoreCase("FIFO"))
            task = new StockValuationTask(MainApp.identityDto.getSociety().getCode(), dpGenerate.getValue(), MainApp.getLocale(), "0");
        StockValuationTask finalTask = task;
        task.setOnSucceeded(e -> {
            try {
                List<ProductStockValuation> list = finalTask.get();
                if (list != null) {
                    tableStockValuation.setItems(FXCollections.observableList(list));
                    calculateTotal(list);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void calculateTotal(List<ProductStockValuation> list) {
        try {
            Double total = list.stream()
                    .map(ProductStockValuation::getValuation)
                    .filter(Objects::nonNull)
                    .reduce(0.0, Double::sum);

            lblTotal.setText(total.toString());
        } catch (RuntimeException e) {

        }
    }
}
