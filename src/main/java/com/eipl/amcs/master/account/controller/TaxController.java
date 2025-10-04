package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.account.model.BasicTax;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.task.TaxLoadTask;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

public class TaxController implements MyInitialization, PopupCallback {
    @FXML
    StackPane root;
    @FXML
    TableView<TaxDto> tableTax;
    @FXML
    TableColumn<TaxDto, String> colCode;
    @FXML
    TableColumn<TaxDto, String> colName, colLocalName;
    @FXML
    Button btnClose, btnTaxDetail;
    private Stage stage;

    private final ObjectProperty<TaxDto> propTaxDto;

    public TaxController() {
        propTaxDto = new SimpleObjectProperty<>();
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
        propTaxDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnTaxDetail.setDisable(false);
            } else {
                btnTaxDetail.setDisable(true);
            }
        });

        btnTaxDetail.setOnAction(e -> {
            TaxDto dto = propTaxDto.get();
            if (dto != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "TaxDetail", dto, this);
            }
        });
    }

    @Override
    public void setupTable() {
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTax().getCode()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTax().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTax().getNameLocal()));
        propTaxDto.bind(tableTax.getSelectionModel().selectedItemProperty());

    }

    @Override
    public void loadData() {
        var task = new TaxLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<TaxDto> list = task.get();
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
