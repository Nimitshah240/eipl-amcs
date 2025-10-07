package com.eipl.amcs.master.geo.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.service.VillageService;
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

public class VillageController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<Village> tableVillages;
    @FXML
    TableColumn<Village, String> colCode;
    @FXML
    TableColumn<Village, String> colName, colLocalName;
    @FXML
    TableColumn<Village, SubDistrict> colSubDistrict;
    @FXML
    Button btnClose;

    @Autowired
    private VillageService villageService;

    @Override
    public Node getRoot() {
        return root;
    }

    public VillageController() {
        villageService = context.getBean(VillageService.class);
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
            colSubDistrict.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSubDistrict()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<Village> list = villageService.findAll();
            if (list != null)
                tableVillages.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
