package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.service.UnionService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class UnionController implements MyInitialization {

    @FXML
    TableView<Union> tableUnion;
    @FXML
    TableColumn<Union, String> colCode, colCity, colName, colLocalName, colCodeEx, colStatus, colRegistrationCode, colPhoneNo, colPinCode, colContactPerson, colEmail, colContactPersonMobileNo, colAcNo, colIfsc;
    @FXML
    TableColumn<Union, Branch> colBranch;
    @FXML
    TableColumn<Union, Bank> colBank;
    @FXML
    TableColumn<Union, LocalDate> colRegistrationDate;
    @FXML
    Button btnClose;
    @FXML
    private StackPane root;
    private ResourceBundle resourceBundle;

    private UnionService service;

    public UnionController() {
        service = context.getBean(UnionService.class);
    }

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
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colCodeEx.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx()));
            colRegistrationCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colRegistrationDate.setCellFactory(new LocalDateCellFactory<>());
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colRegistrationDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRegistrationDate()));
            colPhoneNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));
            colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));
            colContactPerson.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));
            colContactPersonMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPersonMobileNo()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ?
                    resourceBundle.getString("active") : resourceBundle.getString("inactive")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<Union> list = service.findAll();
            if (list != null)
                tableUnion.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


