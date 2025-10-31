package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.setting.dto.MilkCollectionMigration;
import com.eipl.amcs.setting.task.PromptSqlMilkCollectionDbProcess;
import com.eipl.amcs.setting.task.PromptSqlMilkCollectionDbSaveTask;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MilkCollectionDataMigrationPromptController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<MilkCollectionMigration> tableData;
    @FXML
    TableColumn<MilkCollectionMigration, String> colMonth, colCount;
    @FXML
    private TextField txtCow, txtBuffalo, txtdatabase;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private Button btnSave, btnClose, btnGenerate;
    @FXML
    private Label lblStatus;
    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<MilkType> milkTypeList;
    private String selectedFilePath;
    private Stage stage;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        loadData();
        setupTable();
        btnGenerate.setOnAction(e -> {
            if (shiftList == null || shiftList.isEmpty() || milkTypeList == null || milkTypeList.isEmpty()) {
                lblStatus.setText("Milk type is not loaded yet!");
                return;
            }

            startAccessDbProcess();
        });
        btnSave.setOnAction(e -> startImportProcess());
        btnClose.setOnAction(e -> this.stage.close());
    }

    private void startAccessDbProcess() {
        lblStatus.setText("Preparing data...");
        var task = new PromptSqlMilkCollectionDbProcess(selectedFilePath, txtdatabase.getText(), dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                lblStatus.setText("");
                List<MilkCollectionMigration> list = task.get();
                if (list == null)
                    return;
                tableData.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void loadData() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e1 -> {
            try {
                shiftList = task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                milkTypeList = task1.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

    @Override
    public void setupTable() {
        colMonth.setCellValueFactory(data -> new SimpleStringProperty((data.getValue().getMonth())));
        colCount.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCount().toString()));
    }

    private void startImportProcess() {
        var task = new PromptSqlMilkCollectionDbSaveTask(milkTypeList, shiftList,
                txtCow.getText(), txtBuffalo.getText(), txtdatabase.getText(), dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                Boolean res = task.get();
                if (res == null || !res) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("successful"));
                alert.createAlert();
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        lblStatus.textProperty().bind(task.messageProperty());
    }
}
