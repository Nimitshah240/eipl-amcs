package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.service.FinancialYearService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class FinancialYearController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<FinancialYear> tableFinancialYear;
    @FXML
    TableColumn<FinancialYear, String> colCode, colStatus;
    @FXML
    TableColumn<FinancialYear, LocalDate> colStartDate, colEndDate;
    @FXML
    Button btnClose;

    private ResourceBundle resourceBundle;
    private FinancialYearService financialYearService;

    @Override
    public Node getRoot() {
        return root;
    }

    public FinancialYearController() {
        financialYearService = MainApp.context.getBean(FinancialYearService.class);
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
            colStartDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartDate()));
            colEndDate.setCellFactory(new LocalDateCellFactory<>());

            colEndDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEndDate()));
            colStartDate.setCellFactory(new LocalDateCellFactory<>());

            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ?
                    resourceBundle.getString("active") : resourceBundle.getString("inactive")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            List<FinancialYear> list = financialYearService.findAll();
            if (list != null)
                tableFinancialYear.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
