package com.eipl.amcs.master.geo.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.service.UserService;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.service.StateService;
import com.eipl.amcs.master.geo.task.StateLoadTask;
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

public class StateController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<State> tableStates;
    @FXML
    TableColumn<State, String> colCode;
    @FXML
    TableColumn<State, String> colName, colLocalName;
    @FXML
    Button btnClose;

    private StateService stateService;

    @Override
    public Node getRoot() {
        return root;
    }

    public StateController() {
        stateService = context.getBean(StateService.class);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<State> list = stateService.findAll();
            if (list != null)
                tableStates.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        .start();var task = new StateLoadTask();
////        task.setOnSucceeded(e -> {
////            try {
////                List<State> list = task.get();
////                if (list != null)
////                    tableStates.setItems(FXCollections.observableList(list));
////            } catch (InterruptedException | ExecutionException ex) {
////                ex.printStackTrace();
////            }
////        });
////        new Thread(task)
    }
}
