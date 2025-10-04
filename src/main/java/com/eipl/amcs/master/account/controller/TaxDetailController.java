package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.account.model.BasicTax;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.task.TaxLoadTask;
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

public class TaxDetailController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<TaxDetail> tableTaxDetail;
    @FXML
    TableColumn<TaxDetail, BasicTax> colBasicTax;
    @FXML
    TableColumn<TaxDetail, Number> colPercentage;
    @FXML
    TableColumn<TaxDetail, String> colType;
    @FXML
    Button btnClose;
    private Stage stage;
    private TaxDto dto;
    private ResourceBundle resourceBundle;

    public void setDto(TaxDto dto) {
        this.dto = dto;
        if(dto != null)
            tableTaxDetail.setItems(FXCollections.observableList(dto.getTaxDetails()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        btnClose.setOnAction(e -> this.stage.close());
    }

    @Override
    public void setupTable() {
        colPercentage.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPercentage()));
        colBasicTax.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBasicTax()));
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()==1? resourceBundle.getString("addition"):resourceBundle.getString("deduction")));
//        colIsDefault.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDock().getIsDefault() != 0 ? resourceBundle.getString("yes") : resourceBundle.getString("no")));

    }
}
