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
import com.eipl.amcs.setting.task.SkyWayVersionOneMemberFileProcess;
import com.eipl.amcs.setting.task.SkyWayVersionTwoMemberFileProcess;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MemberDataMigrationSkyWayController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<Member> tableData;
    @FXML
    TableColumn<Member, Number> colNoOfCow, colNoOfBuffalo;
    @FXML
    TableColumn<Member, String> colFirstName, colMiddleName, colLastName, colGender, colMobileNo, colCode, colLocalName;
    @FXML
    Button btnSave, btnClose, btnBrowse, btnGenerate;
    @FXML
    TextField txtFilePath, txtCow, txtBuffalo;
    @FXML
    ComboBox<String> cboxVersion;
    @FXML
    private Label lblStatus;


//    private List<MilkType> milkTypeList;
//    private List<Gender> genderList;
//    private List<MemberType> memberTypeList;
//    String milkTypeStr = null;


    private Stage stage;
    private ResourceBundle resourceBundle;
    private List<MemberDto> listDto = new ArrayList<>();
    private final Map<String, MilkType> mapMilkType = new HashMap<>();
    private final Map<String, Gender> mapGender = new HashMap<>();
    private MemberType memberType;
    private String selectedFilePath = null;

    public MemberDataMigrationSkyWayController() {
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
        cboxVersion.getItems().add("Version 1");
        cboxVersion.getItems().add("Version 2");
        cboxVersion.getSelectionModel().select(0);

        btnSave.setOnAction(e -> startImportProcess());
        btnClose.setOnAction(e -> this.stage.close());
        btnGenerate.setOnAction(e -> {
            if (mapMilkType.isEmpty() || mapGender.isEmpty() || memberType == null) {
                lblStatus.setText("Milk type is not loaded yet!");
                return;
            }
            if (cboxVersion.getValue().equalsIgnoreCase("Version 1"))
                startVersionOneProcess();
            else if (cboxVersion.getValue().equalsIgnoreCase("Version 2"))
                startVersionTwoProcess();

        });
        btnBrowse.setOnAction(e -> {
            File file = CommonUtils.openFileDialog("Data");
            if (file == null) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), "Data",
                        resourceBundle.getString("select.file"));
                alert.createAlert();
                return;
            }
            selectedFilePath = file.getAbsolutePath();
            txtFilePath.setText(selectedFilePath);
        });
    }

    private void startVersionOneProcess() {
        lblStatus.setText("Preparing data...");
        var task = new SkyWayVersionOneMemberFileProcess(mapMilkType, mapGender, memberType, txtFilePath.getText(),
                txtCow.getText(), txtBuffalo.getText());
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

    private void startVersionTwoProcess() {
        lblStatus.setText("Preparing data...");
        var task = new SkyWayVersionTwoMemberFileProcess(mapMilkType, mapGender, memberType, txtFilePath.getText(),
                txtCow.getText(), txtBuffalo.getText());
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
        colCode.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getMemberShortCode(data.getValue().getCodeEx())));
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

//    public void setData() {
//        try {
//            List<Member> list = new ArrayList<>();
//            List<String> lines = Files.readAllLines(new File(path).toPath(), Charset.forName("UTF-8"));
//            String milkCow[] = txtCow.getText().split("-");
//            String milkBuf[] = txtBuffalo.getText().split("-");
//            for (String line : lines) {
//                String[] arr = line.split(",");
//                System.out.println(arr[0] + "-" + arr[1] + "-" + arr[2]);
//
//                Member m = new Member();
//                MemberDetail md = new MemberDetail();
//                m.setCodeEx(String.format("%04d", Integer.parseInt(arr[0])));
//                m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());
//                m.setFirstName("Member");
//                m.setMiddleName("");
//                m.setLastName(arr[0]);
//                String[] arrNameLocal = arr[1].trim().replace("'", "").split("\\s+");
//                m.setFirstNameLocal(arrNameLocal.length >  1 ? arrNameLocal[1] : "");
//                m.setLastNameLocal(arrNameLocal[0]);
//                m.setMiddleNameLocal(arrNameLocal.length > 2 ? arrNameLocal[2] : "");
//                if(Integer.parseInt(m.getCodeEx())>=Integer.parseInt(milkCow[0]) &&(Integer.parseInt(m.getCodeEx())<=Integer.parseInt(milkCow[1])))
//                    milkTypeStr="c";
//                else if (Integer.parseInt(m.getCodeEx())>=Integer.parseInt(milkBuf[0]) &&(Integer.parseInt(m.getCodeEx())<=Integer.parseInt(milkBuf[1])))
//                    milkTypeStr="b";
//                else
//                    continue;
//                m.setMilkType(mapMilkType.get(milkTypeStr) != null ? mapMilkType.get(milkTypeStr) : milkTypeList.get(0));
//                m.setMilkType(mapMilkType.get(milkTypeStr) != null ? mapMilkType.get(milkTypeStr) : milkTypeList.get(0));
//                md.setGender(genderList.get(0));
//                MemberType memberType = memberTypeList.get(0);
//                m.setMemberType(memberType);
//                m.setSociety(MainApp.identityDto.getSociety());
//                md.setUnionCode(MainApp.identityDto.getUnion().getCode());
//                m.setMobileNo("0000000000");
//                md.setMember(m);
//                md.setNumberOfBuffalo((short) 0);
//                md.setNumberOfCow((short) 0);
//                list.add(m);
//                listDto.add(new MemberDto(m, md));
//            }
//            tableData.setItems(FXCollections.observableList(list));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
