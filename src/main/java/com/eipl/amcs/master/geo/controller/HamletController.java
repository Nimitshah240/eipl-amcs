package com.eipl.amcs.master.geo.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.Hamlet;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.service.HamletService;
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

import static com.eipl.amcs.MainApp.context;

public class HamletController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<Hamlet> tableHamlets;
    @FXML
    TableColumn<Hamlet, String> colCode;
    @FXML
    TableColumn<Hamlet, String> colName, colLocalName;
    @FXML
    TableColumn<Hamlet, Village> colVillage;
    @FXML
    Button btnClose;

    @Autowired
    private HamletService hamletService;

    @Override
    public Node getRoot() {
        return root;
    }

    public HamletController() {
        hamletService = context.getBean(HamletService.class);
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
            colVillage.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getVillage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<Hamlet> list = hamletService.findAll();
            if (list != null)
                tableHamlets.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
