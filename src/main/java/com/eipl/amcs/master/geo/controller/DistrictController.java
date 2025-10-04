package com.eipl.amcs.master.geo.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.service.DistrictService;
import com.eipl.amcs.master.geo.service.StateService;
import com.eipl.amcs.master.geo.task.DistrictLoadTask;
import javafx.beans.property.SimpleObjectProperty;
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

public class DistrictController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<District> tableDistricts;
    @FXML
    TableColumn<District, String> colCode;
    @FXML
    TableColumn<District, String> colName, colLocalName;
    @FXML
    TableColumn<District, State> colState;
    @FXML
    Button btnClose;

    private DistrictService districtService;

    @Override
    public Node getRoot() {
        return root;
    }

    public DistrictController(){
        districtService = context.getBean(DistrictService.class);
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
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colState.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getState()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<District> list = districtService.findAll();
            if (list != null)
                tableDistricts.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        var task = new DistrictLoadTask();
//        task.setOnSucceeded(e -> {
//            try {
//                List<District> list = task.get();
//                if (list != null)
//                    tableDistricts.setItems(FXCollections.observableList(list));
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
    }
}
