package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.operation.administartion.task.DesignationLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class DesignationController implements MyInitialization, PopupCallback {
    @FXML
    StackPane root;
    @FXML
    TableView<Designation> tableTax;
    @FXML
    TableColumn<Designation, String> colCode;
    @FXML
    TableColumn<Designation, String> colName;
    @FXML
    Button btnClose;
    private Stage stage;

    private final ObjectProperty<Designation> propDesignation;

    public DesignationController() {
        propDesignation = new SimpleObjectProperty<>();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
        try{
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode().toString()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
    }catch (Exception e) {
            System.out.println("Designation setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new DesignationLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Designation> list = task.get();
                if (list != null) {
                    tableTax.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
