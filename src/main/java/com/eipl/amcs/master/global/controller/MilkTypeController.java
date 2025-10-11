package com.eipl.amcs.master.global.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.service.MilkTypeService;
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

import static com.eipl.amcs.MainApp.context;

public class MilkTypeController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<MilkType> tableMilkTypes;
    @FXML
    TableColumn<MilkType, Number> colCode;
    @FXML
    TableColumn<MilkType, String> colName, colLocalName;
    @FXML
    Button btnClose;

    private MilkTypeService milkTypeService;


    @Override
    public Node getRoot() {
        return root;
    }

    public MilkTypeController() {
        milkTypeService = context.getBean(MilkTypeService.class);
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
            List<MilkType> list = milkTypeService.findAll();
            if (list != null)
                tableMilkTypes.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        var task = new MilkTypeLoadTask();
//        task.setOnSucceeded(e -> {
//            try {
//                List<MilkType> list = task.get();
//                if (list != null)
//                    tableMilkTypes.setItems(FXCollections.observableList(list));
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
    }
}
