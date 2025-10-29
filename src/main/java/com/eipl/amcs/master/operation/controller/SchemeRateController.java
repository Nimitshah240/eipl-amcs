package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.operation.model.SchemeRateApplicability;
import com.eipl.amcs.master.operation.task.SchemeRateApplicabilityLoadTask;
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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SchemeRateController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<SchemeRateApplicability> tableSchemeRateApplicability;
    @FXML
    TableColumn<SchemeRateApplicability, String> colRateAppCode, colDescription, colRtpl;
    @FXML
    TableColumn<SchemeRateApplicability, Integer> colFromShift, colToShift;
    @FXML
    TableColumn<SchemeRateApplicability, LocalDate> colFromDate, colToDate;

    @FXML
    Button btnClose;

    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return null;
    }

    /**
     * Method use to setup data for scheme rate screen.
     *
     * @param url
     * @param resourceBundle
     * @author Nimit Shah
     * @createdOn 23-07-2025
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        loadData();
        setupTable();
    }

    /**
     * Method use to set table with the data of SchemeRateApplicability.
     *
     * @author Nimit Shah
     * @createdOn 23-07-2025
     */
    @Override
    public void setupTable() {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            colRateAppCode.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getSchemeRateAppCode()));
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate().toLocalDate()));
            colFromShift.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getFromShift() == 1 ? resourceBundle.getString("Morning") : resourceBundle.getString("Evening")));
            colToShift.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getToShift() == 1 ? resourceBundle.getString("Morning") : resourceBundle.getString("Evening")));
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate().toLocalDate()));
            colRtpl.setCellValueFactory(data -> new SimpleObjectProperty(df.format(data.getValue().getRtpl())));
            colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method use to call load task to get data of SchemeRateApplicability.
     *
     * @author Nimit Shah
     * @createdOn 23-07-2025
     */
    @Override
    public void loadData() {
        tableSchemeRateApplicability.setItems(null);
        var task = new SchemeRateApplicabilityLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SchemeRateApplicability> list = task.get();
                if (list != null)
                    tableSchemeRateApplicability.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}