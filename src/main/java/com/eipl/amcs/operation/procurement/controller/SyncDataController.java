package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.procurement.task.MilkCollectionLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SyncDataController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnSync, btnClose;
    private Stage stage;
    @FXML
    private Label lbl;
    private PopupCallback callback;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadShift();
        setupComboBox();

        btnSync.setOnAction(e -> syncData1());
        btnClose.setOnAction(e -> stage.close());
    }

    @Override
    public void setupComboBox() {
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
    }

    private void syncData1() {
        var task = new MilkCollectionLoadTask(
                CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()),
                CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()), 1);
        lbl.setVisible(true);
        task.setOnSucceeded(e -> {
            System.out.println("Done");
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("sync"),
                    resourceBundle.getString("successful"));
            alert.createAlert();
            this.stage.close();
        });
        new Thread(task).start();
    }


    private void syncData() {
        String mysqlUrl = "jdbc:mysql://localhost:3366/" + AppConstant.EIPL_DB_NAME;
        try {
            Connection connMySql = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS);
            Statement stmt = connMySql.createStatement();
            String sql = "INSERT INTO broadcasted(`source_type`,\n" +
                    "`source_code`,\n" +
                    "`dest_type`,\n" +
                    "`dest_code`,\n" +
                    "`table_name`,\n" +
                    "`operation`,\n" +
                    "`data_text`,\n" +
                    "`error_text`,\n" +
                    "`created_at`,\n" +
                    "`processed`,\n" +
                    "`sequence`,\n" +
                    "`processed_at`,\n" +
                    "`language`,\n" +
                    "`source_system_id`,\n" +
                    "`version`) " +
                    "select " +
                    "`source_type`,\n" +
                    "`source_code`,\n" +
                    "`dest_type`,\n" +
                    "`dest_code`,\n" +
                    "`table_name`,\n" +
                    "`operation`,\n" +
                    "`data_text`,\n" +
                    "`error_text`,\n" +
                    "`created_at`,\n" +
                    "`processed`,\n" +
                    "`sequence`,\n" +
                    "`processed_at`,\n" +
                    "`language`,\n" +
                    "`source_system_id`,\n" +
                    "`version`" +
                    " from broadcasted_log where processed_at between '" +
                    dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " +
                    (cboxFromShift.getValue().getName().equalsIgnoreCase(resourceBundle.getString("Morning")) ? "06:00:00" : "18:00:00") + "' and '" +
                    dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " +
                    (cboxToShift.getValue().getName().equalsIgnoreCase(resourceBundle.getString("Morning")) ? "06:00:00" : "18:00:00") + "';";

            Boolean rs = stmt.execute(sql);
            if (rs) {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("successful"));
                alert.createAlert();
                this.stage.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    cboxFromShift.setItems(FXCollections.observableList(list));
                    cboxToShift.setItems(FXCollections.observableList(list));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.getSelectionModel().select(1);
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


}