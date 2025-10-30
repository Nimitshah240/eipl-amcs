package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.task.RouteLoadTask;
import com.eipl.amcs.master.org.task.RouteSaveTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class RouteController implements MyInitialization {

    @FXML
    TableView<Route> tableRoute;
    @FXML
    TableColumn<Route, String> colCode, colName, colLocalName, colCodeEx;
    @FXML
    TableColumn<Route, Bmc> colBmc;
    @FXML
    TableColumn<Route, String> colStartTime, colReturnTime;
    @FXML
    TableColumn<Route, String> colCapacity, colLengthKms;
    @FXML
    Button btnClose, btnSave;
    @FXML
    private StackPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnSave.setOnAction(e -> {
            saveData();
        });
    }

    @Override
    public void setupTable() {
        try {
            tableRoute.setEditable(true);
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colCodeEx.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx()));
            colBmc.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBmc()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colName.setCellFactory(TextFieldTableCell.forTableColumn());
            colName.setOnEditCommit(e -> {
                Route r = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    r.setName(e.getNewValue());
            });
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colLocalName.setCellFactory(TextFieldTableCell.forTableColumn());
            colLocalName.setOnEditCommit(e -> {
                Route r = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    r.setNameLocal(e.getNewValue());
            });
            colCapacity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCapacity().toString()));
            colCapacity.setCellFactory(TextFieldTableCell.forTableColumn());
            colCapacity.setOnEditCommit(e -> {
                Route r = e.getRowValue();
                try {
                    if (e.getNewValue() != null)
                        r.setCapacity(Integer.valueOf(e.getNewValue()));
                } catch (Exception ex) {

                }
            });
            colLengthKms.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLengthKms().toString()));
            colLengthKms.setCellFactory(TextFieldTableCell.forTableColumn());
            colLengthKms.setOnEditCommit(e -> {
                Route r = e.getRowValue();
                try {
                    if (e.getNewValue() != null)
                        r.setLengthKms(Integer.valueOf(e.getNewValue()));
                } catch (Exception ex) {

                }
            });


            colStartTime.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartTime() != null ? data.getValue().getStartTime().toString() : ""));
            colStartTime.setCellFactory(TextFieldTableCell.forTableColumn());
            colStartTime.setOnEditCommit(e -> {
                Route r = e.getRowValue();
                try {
                    if (e.getNewValue() != null)
                        r.setStartTime(LocalTime.parse(e.getNewValue()));
                } catch (Exception ex) {

                }
            });


            colReturnTime.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getReturnTime() != null ? data.getValue().getReturnTime().toString() : ""));
            colReturnTime.setCellFactory(TextFieldTableCell.forTableColumn());
            colReturnTime.setOnEditCommit(e -> {
                Route r = e.getRowValue();
                try {
                    if (e.getNewValue() != null)
                        r.setReturnTime(LocalTime.parse(e.getNewValue()));
                } catch (Exception ex) {

                }
            });
        } catch (Exception e) {
            System.out.println("Route setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new RouteLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Route> list = task.get();
                if (list != null)
                    tableRoute.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void saveData() {
        RouteSaveTask task = new RouteSaveTask(tableRoute.getItems().get(0));
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }
}


