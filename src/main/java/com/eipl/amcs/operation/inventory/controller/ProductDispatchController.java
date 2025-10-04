package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.task.ProductDispatchLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProductDispatchController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<ProductDispatchTransaction> tableProductDispatch;
    @FXML
    TableColumn<ProductDispatchTransaction, String> colChallanNo, colStatus, colQty, colProduct;
    @FXML
    TableColumn<ProductDispatchTransaction, LocalDate> colChallanDate;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    private ResourceBundle resourceBundle;
    @FXML
    Button btnClose, btnSearch;
    private final ObjectProperty<ProductDispatchTransaction> propProductDispatchTransactionDto;

    @Override
    public Node getRoot() {
        return root;
    }

    public ProductDispatchController() {
        propProductDispatchTransactionDto = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });

        this.resourceBundle = resourceBundle;


        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        loadData();
        setupTable();
        btnSearch.setOnAction(e -> loadData());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

    @Override
    public void setupTable() {
        try {
            colChallanDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDispatchDate()));
            colChallanDate.setCellFactory(new LocalDateCellFactory<>());
            colChallanNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductDispatch().getChallanNo()));
            colQty.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDispatchQty())));
//            colRate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getRate())));
//            colAmount.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAmount())));
            colProduct.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getProduct())));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStatus())));

            propProductDispatchTransactionDto.bind(tableProductDispatch.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("ProductDispatchTransaction setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableProductDispatch.setItems(null);
        ProductDispatchLoadTask task = new ProductDispatchLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<ProductDispatchTransaction> list = task.get();
                if (list != null)
                    tableProductDispatch.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {

    }
}
