package com.eipl.amcs.master.global.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.global.service.MemberTypeService;
import com.eipl.amcs.master.global.task.MemberTypeLoadTask;
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

public class MemberTypeController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<MemberType> tableMemberTypes;
    @FXML
    TableColumn<MemberType, Number> colCode;
    @FXML
    TableColumn<MemberType, String> colName, colLocalName;
    @FXML
    Button btnClose;

    private MemberTypeService memberTypeService;

    @Override
    public Node getRoot() {
        return root;
    }

    public MemberTypeController() {
        memberTypeService = context.getBean(MemberTypeService.class);
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
            List<MemberType> list = memberTypeService.findAll();
            if (list != null)
                tableMemberTypes.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        var task = new MemberTypeLoadTask();
//        task.setOnSucceeded(e -> {
//            try {
//                List<MemberType> list = task.get();
//                if (list != null)
//                    tableMemberTypes.setItems(FXCollections.observableList(list));
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
    }
}
