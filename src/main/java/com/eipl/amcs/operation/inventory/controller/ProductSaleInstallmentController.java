package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ProductSaleInstallmentController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<ProductSaleInstallment> tableProductInstallment;
    @FXML
    TableColumn<ProductSaleInstallment, String> colTrDate;
    @FXML
    TableColumn<ProductSaleInstallment, Number> colAmount;
    @FXML
    TableColumn<ProductSaleInstallment, String> colStatus, colSrNo;
    @FXML
    Button btnClose;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yy");
    private List<ProductSaleInstallment> list = null;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setInstallments(List<ProductSaleInstallment> list) {
        if (list != null) {
            this.list = list;
            setupTable();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        btnClose.setOnAction(e -> this.stage.close());
    }

    @Override
    public void setupTable() {
        try {
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getInstallmentAmount()));
            colTrDate.setText(resourceBundle.getString("period"));
            colTrDate.setCellValueFactory(data -> new SimpleStringProperty(dtf.format(data.getValue().getSocietyPaymentCycle().getFromDate().toLocalDate()) + " To " + dtf.format(data.getValue().getSocietyPaymentCycle().getToDate().toLocalDate())));
            colSrNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode().replace(data.getValue().getInvoiceNo() + "-", "")));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBilling() ? "Done" : "Pending"));
            tableProductInstallment.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            System.out.println("ProductSaleInstallment setuptable Exception");
            e.printStackTrace();
        }
    }

}
