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
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.procurement.controller.MemberMilkPurchaseRateAddEditController;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.setting.task.HisaabMitraMilkCollection2DbSaveTask;
import com.eipl.amcs.setting.task.HisaabMitraMilkCollectionDbSaveTask;
import com.eipl.amcs.setting.task.HisaabMitraMilkCollectionImportTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MilkCollectionDataMigrationHisaabMitraController implements MyInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberMilkPurchaseRateAddEditController.class);
    @FXML
    StackPane root;
    @FXML
    TextField txtFilePath, txtCow, txtBuffalo;
    @FXML
    Button btnClose, btnBrowse, btnGenerate;
    @FXML
    CheckBox chkIsExcel, chkIsFormat2;
    @FXML
    private Label lblStatus;
    private File file;
    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<Member> memberList;
    private List<MilkType> milkTypeList;
    private Stage stage;
    private List<MilkCollection> listMilkCollection;

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
        loadData();

        btnBrowse.setOnAction(e -> {
            file = CommonUtils.openFileDialog("Data");
            if (file == null) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), "Data", resourceBundle.getString("select.file"));
                alert.createAlert();
                return;
            }
            txtFilePath.setText(file.getAbsolutePath());

        });


        btnGenerate.setOnAction(e -> {
            if (shiftList == null || shiftList.isEmpty() || milkTypeList == null || milkTypeList.isEmpty()) {
                lblStatus.setText("Milk type is not loaded yet!");
                return;
            }

            if (txtBuffalo.getText() == null || txtBuffalo.getText().equalsIgnoreCase("") || txtBuffalo.getText() == null || txtBuffalo.getText().equalsIgnoreCase("")) {
                startAmcsFileProcess();
            } else {
                if (chkIsExcel.isSelected()) {
                    startImportFileProcess();
                } else if (chkIsFormat2.isSelected()) {
                    startSkywayFileProcess2();
                } else {
                    startSkywayFileProcess();
                }
            }
        });
        btnClose.setOnAction(e -> this.stage.close());
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
        var task2 = new MemberLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                memberList = task2.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    private void startSkywayFileProcess() {
        var task = new HisaabMitraMilkCollectionDbSaveTask(milkTypeList, shiftList, txtFilePath.getText(), txtCow.getText(), txtBuffalo.getText());
        task.setOnSucceeded(e -> {
            try {
                Boolean res = task.get();
                if (res == null || !res) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("successful"));
                alert.createAlert();
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        lblStatus.textProperty().bind(task.messageProperty());
    }

    private void startSkywayFileProcess2() {
        var task = new HisaabMitraMilkCollection2DbSaveTask(milkTypeList, shiftList, txtFilePath.getText(), txtCow.getText(), txtBuffalo.getText());
        task.setOnSucceeded(e -> {
            try {
                Boolean res = task.get();
                if (res == null || !res) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("successful"));
                alert.createAlert();
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        lblStatus.textProperty().bind(task.messageProperty());
    }

    private void startImportFileProcess() {
        var task = new HisaabMitraMilkCollectionImportTask(file, milkTypeList, shiftList, memberList);
        task.setOnSucceeded(e -> {
            try {
                listMilkCollection = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("successful"));
                alert.createAlert();
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        lblStatus.textProperty().bind(task.messageProperty());
    }

    private void startImportFileProcess2() {
        var task = new HisaabMitraMilkCollectionImportTask(file, milkTypeList, shiftList, memberList);
        task.setOnSucceeded(e -> {
            try {
                listMilkCollection = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("successful"));
                alert.createAlert();
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        lblStatus.textProperty().bind(task.messageProperty());
    }

    private void startAmcsFileProcess() {
        var task = new HisaabMitraMilkCollectionDbSaveTask(milkTypeList, shiftList, txtFilePath.getText(), txtCow.getText(), txtBuffalo.getText());
        task.setOnSucceeded(e -> {

        });
        new Thread(task).start();
        lblStatus.textProperty().bind(task.messageProperty());
    }
}


