package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.service.BankService;
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
import java.util.List;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class BankController implements MyInitialization {

    @FXML
    TableView<Bank> tableBank;
    @FXML
    TableColumn<Bank, String> colCode, colName, colLocalName, colStatus;
    @FXML
    TableColumn<Bank, Number> colAcNoLength;
    @FXML
    TableColumn<Bank, String> colCheckedAcNo, colNationalizedBank;
    @FXML
    Button btnClose;
    @FXML
    private StackPane root;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    private BankService bankService;

    public BankController() {
        bankService = context.getBean(BankService.class);
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
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colAcNoLength.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAcNoLength()));
            colCheckedAcNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isCheckedAcNoLength() ?
                    resourceBundle.getString("yes") : resourceBundle.getString("no")));
            colNationalizedBank.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isNationalizedBank() ?
                    resourceBundle.getString("yes") : resourceBundle.getString("no")));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ?
                    resourceBundle.getString("active") : resourceBundle.getString("inactive")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<Bank> list = bankService.findAll();
            if (list != null)
                tableBank.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


