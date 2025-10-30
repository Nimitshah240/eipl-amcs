package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.global.task.MemberTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.task.MemberListSaveTask;
import com.eipl.amcs.setting.task.PromptSqlMemberDbProcess;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MemberDataMigrationPromptController implements MyInitialization {
    private final Map<String, MilkType> mapMilkType = new HashMap<>();
    private final Map<String, Gender> mapGender = new HashMap<>();
    private final String selectedFilePath = null;
    @FXML
    private StackPane root;
    @FXML
    private TableView<Member> tableData;
    @FXML
    private TableColumn<Member, String> colFirstName, colMiddleName, colLastName, colMobileNo, colCode, colLocalName;
    @FXML
    private Button btnSave, btnClose, btnGenerate;
    @FXML
    private TextField txtCow, txtBuffalo, txtdatabase;
    @FXML
    private Label lblStatus;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private List<MemberDto> listDto = new ArrayList<>();
    private MemberType memberType;


    public MemberDataMigrationPromptController() {
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
        loadData();
        setupTable();

        btnSave.setOnAction(e -> startImportProcess());
        btnClose.setOnAction(e -> this.stage.close());
        btnGenerate.setOnAction(e -> {
            if (mapMilkType.isEmpty() || mapGender.isEmpty() || memberType == null) {
                lblStatus.setText("Milk type is not loaded yet!");
                return;
            }
            startAccessDbProcess();
        });
    }

    @Override
    public void loadData() {
        // Gender load task
        var task = new GenderLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Gender> list = task.get();
                if (list == null)
                    return;
                list.forEach(item -> {
                    mapGender.put(item.getName(), item);
                    mapGender.put(item.getName().toLowerCase().substring(0, 1), item);
                    mapGender.put(item.getName().toUpperCase().substring(0, 1), item);
                    mapGender.put(item.getCode().toString(), item);
                });
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        // Milk type load task
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list == null)
                    return;
                list.forEach(item -> {
                    mapMilkType.put(item.getName().toLowerCase().substring(0, 1), item);
                });
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        // Member type load task
        var task2 = new MemberTypeLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<MemberType> list = task2.get();
                if (list == null || list.isEmpty())
                    return;
                memberType = list.stream().filter(p -> p.getName().equalsIgnoreCase("member")).findFirst().orElse(null);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    @Override
    public void setupTable() {
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx()));
        colFirstName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        colMiddleName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMiddleName()));
        colLastName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        colMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMobileNo()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastNameLocal() + " " + data.getValue().getFirstNameLocal()));
    }

    private void startImportProcess() {
        var task = new MemberListSaveTask(listDto, true);
        task.setOnSucceeded(e -> {
            try {
                List<MemberImportDto> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("member"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
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

    private void startAccessDbProcess() {
        lblStatus.setText("Preparing data...");
        var task = new PromptSqlMemberDbProcess(mapMilkType, mapGender, memberType,
                txtCow.getText(), txtBuffalo.getText(), txtdatabase.getText());
        task.setOnSucceeded(e -> {
            lblStatus.setText("");
            try {
                listDto = task.get();
                if (listDto == null) {
                    lblStatus.setText("An error occurred!");
                    return;
                }

                List<Member> memberList = listDto.stream().map(m -> m.getMember()).collect(Collectors.toList());
                tableData.setItems(FXCollections.observableList(memberList));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
