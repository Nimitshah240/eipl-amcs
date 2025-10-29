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
    private final Map<String, MilkType> mapMilkType = new HashMap<>();
    private final Map<String, Gender> mapGender = new HashMap<>();
    private MemberType memberType;
    private final String selectedFilePath = null;


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

//            if (txtFilePath.getText().endsWith(".txt"))
//                startFileProcess();
//            else if (txtFilePath.getText().endsWith(".mdb"))
            startAccessDbProcess();
        });
//        btnBrowse.setOnAction(e -> {
//            File file = CommonUtils.openFileDialog("Data");
//            if (file == null) {
//                MyAlert alert = new WarningAlert(MainApp.getStage(), "Data",
//                        resourceBundle.getString("select.file"));
//                alert.createAlert();
//                return;
//            }
//            selectedFilePath = file.getAbsolutePath();
//            txtFilePath.setText(selectedFilePath);
//        });
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

//        var task = new MemberListSaveTask(listDto);
//        final double wndwWidth = 300.0d;
//        Label updateLabel = new Label("Running tasks...");
//        updateLabel.setPrefWidth(wndwWidth);
//        Task longTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                updateMessage("Task processing");
//                Thread.sleep(100);
//                return null;
//            }
//        };
//        updateLabel.textProperty().bind(longTask.messageProperty());
//        Stage taskUpdateStage = new Stage(StageStyle.UTILITY);
//        VBox updatePane = new VBox();
//        updatePane.setPadding(new Insets(10));
//        updatePane.setSpacing(5.0d);
//        ProgressBar progress = new ProgressBar();
//        progress.setPrefWidth(wndwWidth);
//        updatePane.getChildren().addAll(updateLabel, progress);
//        taskUpdateStage.setScene(new Scene(updatePane));
//        taskUpdateStage.show();
//        new Thread(longTask).start();
//        updateLabel.textProperty().bind(longTask.messageProperty());
//        task.setOnSucceeded(e -> {
//            try {
//                List<MemberImportDto> list = task.get();
//                if (list == null || list.isEmpty()) {
//                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("member"),
//                            resourceBundle.getString("error.occurred"));
//                    alert.createAlert();
//                    return;
//                }
//
//                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
//                        resourceBundle.getString("successful"));
//                alert.createAlert();
//                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/DataMigration.fxml")));
//                System.out.println("DONE");
//                this.stage.close();
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
    }

//    private void runTask() {
//
//        final double wndwWidth = 300.0d;
//        Label updateLabel = new Label("Running tasks...");
//        updateLabel.setPrefWidth(wndwWidth);
//        ProgressBar progress = new ProgressBar();
//        progress.setPrefWidth(wndwWidth);
//
//        VBox updatePane = new VBox();
//        updatePane.setPadding(new Insets(10));
//        updatePane.setSpacing(5.0d);
//        updatePane.getChildren().addAll(updateLabel, progress);
//
//        Stage taskUpdateStage = new Stage(StageStyle.UTILITY);
//        taskUpdateStage.setScene(new Scene(updatePane));
//        taskUpdateStage.show();
//
//        Task longTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                int max = 50;
//                for (int i = 1; i <= 100000000000L; i++) {
//                    if (isCancelled()) {
//                        break;
//                    }
//                    updateProgress(i, max);
//                    updateMessage("Task part " + String.valueOf(i) + " complete");
//                    Thread.sleep(100);
//                }
//                return null;
//            }
//        };
//
//        longTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent t) {
//                taskUpdateStage.hide();
//            }
//        });
//        progress.progressProperty().bind(longTask.progressProperty());
//        updateLabel.textProperty().bind(longTask.messageProperty());
//
//        taskUpdateStage.show();
//        new Thread(longTask).start();
//    }

//    private void startFileProcess() {
//        lblStatus.setText("Preparing data...");
//        var task = new EMandaliMemberFileProcess(mapMilkType, mapGender, memberType, txtFilePath.getText(),
//                txtCow.getText(), txtBuffalo.getText());
//        task.setOnSucceeded(e -> {
//            lblStatus.setText("");
//            try {
//                listDto = task.get();
//                if (listDto == null) {
//                    lblStatus.setText("An error occurred!");
//                    return;
//                }
//
//                List<Member> memberList = listDto.stream().map(m -> m.getMember()).collect(Collectors.toList());
//                tableData.setItems(FXCollections.observableList(memberList));
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
//    }

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

//    public void setData() {
//        if (txtFilePath.getText().contains(".txt")) {
//            try {
//                List<Member> list = new ArrayList<>();
//                List<String> lines = Files.readAllLines(new File(selectedFilePath).toPath(), Charset.forName("UTF-8"));
//                String milkCow[] = txtCow.getText().split("-");
//                String milkBuf[] = txtBuffalo.getText().split("-");
//                for (String line : lines) {
//                    String[] arr = line.split(",");
//                    String[] arr1 = arr[1].split("\\s+");
//                    String[] arr2 = arr[2].split("\\s+");
//                    System.out.println(arr[0] + "-" + arr[1] + "-" + arr[2]);
//
//                    Member m = new Member();
//                    MemberDetail md = new MemberDetail();
//                    m.setCodeEx(String.format("%04d", Integer.parseInt(arr[0])));
//                    m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());
//                    m.setLastName(arr1[0]);
//                    m.setFirstName(arr1.length > 1 ? arr1[1] : "Member");
//                    m.setMiddleName(arr1.length > 2 ? arr1[2] : "");
//                    m.setLastNameLocal(arr2[0]);
//                    m.setFirstNameLocal(arr2.length > 1 ? arr2[1] : "");
//                    m.setMiddleNameLocal(arr2.length > 2 ? arr2[2] : "");
//                    if (Integer.parseInt(m.getCodeEx()) >= Integer.parseInt(milkCow[0]) && (Integer.parseInt(m.getCodeEx()) <= Integer.parseInt(milkCow[1])))
//                        milkTypeStr = "c";
//                    else if (Integer.parseInt(m.getCodeEx()) >= Integer.parseInt(milkBuf[0]) && (Integer.parseInt(m.getCodeEx()) <= Integer.parseInt(milkBuf[1])))
//                        milkTypeStr = "b";
//                    else
//                        continue;
//                    m.setMilkType(mapMilkType.get(milkTypeStr) != null ? mapMilkType.get(milkTypeStr) : milkTypeList.get(0));
//                    m.setMilkType(mapMilkType.get(milkTypeStr) != null ? mapMilkType.get(milkTypeStr) : milkTypeList.get(0));
//                    String genderStr = arr[4];
//                    md.setGender(mapGender.get(genderStr) != null ? mapGender.get(genderStr) : mapGender.get(0));
////                    md.setGender(genderList.get(0));
//                    MemberType memberType = memberTypeList.get(0);
//                    m.setMemberType(memberType);
//                    m.setSociety(MainApp.identityDto.getSociety());
//                    md.setUnionCode(MainApp.identityDto.getUnion().getCode());
//                    m.setMobileNo(arr[3].isEmpty() || arr[3].equalsIgnoreCase("0") ? "0000000000" : arr[3]);
//                    md.setMember(m);
//                    md.setNumberOfBuffalo(Short.parseShort(arr[6]));
//                    md.setNumberOfCow(Short.parseShort(arr[5]));
//                    list.add(m);
//                    listDto.add(new MemberDto(m, md));
//                }
//                tableData.setItems(FXCollections.observableList(list));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            mapMilkType.put("b", milkTypeList.get(1));
//            mapMilkType.put("c", milkTypeList.get(0));
//            mapGender.put("M", genderList.get(0));
//            mapGender.put("F", genderList.get(1));
//            String urlDb = "jdbc:ucanaccess://" + txtFilePath.getText();
//            String pwd = "PNM^$)&(%*";
//            try (Connection connection = DriverManager.getConnection(urlDb, "", pwd)) {
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("select * from tblSabhasad");
//                List<Member> list = new ArrayList<>();
//                String milkCow[] = txtCow.getText().split("-");
//                String milkBuf[] = txtBuffalo.getText().split("-");
//                while (resultSet.next()) {
//                    Member m = new Member();
//                    MemberDetail md = new MemberDetail();
//                    m.setCodeEx(resultSet.getString("SabhasadId"));
//                    m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());
//                    String name = resultSet.getString("SName");
//                    String[] arr = name.split(" ");
//                    m.setFirstName(arr.length > 1 ? arr[1] : arr[0]);
//                    m.setMiddleName(arr.length > 2 ? arr[2] : "");
//                    m.setLastName(arr[0]);
//                    if (Integer.parseInt(m.getCodeEx()) >= Integer.parseInt(milkCow[0]) && (Integer.parseInt(m.getCodeEx()) <= Integer.parseInt(milkCow[1])))
//                        milkTypeStr = "c";
//                    else if (Integer.parseInt(m.getCodeEx()) >= Integer.parseInt(milkBuf[0]) && (Integer.parseInt(m.getCodeEx()) <= Integer.parseInt(milkBuf[1])))
//                        milkTypeStr = "b";
//                    else
//                        continue;
//                    m.setMilkType(mapMilkType.get(milkTypeStr) != null ? mapMilkType.get(milkTypeStr) : milkTypeList.get(0));
//                    String genderStr = resultSet.getString("sex");
//                    md.setGender(mapGender.get(genderStr) != null ? mapGender.get(genderStr) : mapGender.get(0));
//                    m.setMemberType(memberTypeList.get(0));
//                    m.setSociety(MainApp.identityDto.getSociety());
//                    md.setUnionCode(MainApp.identityDto.getUnion().getCode());
//                    m.setMobileNo(resultSet.getString("Phone"));
//                    list.add(m);
//                    md.setMember(m);
//                    md.setNumberOfBuffalo(resultSet.getShort("NoOfBuff"));
//                    md.setNumberOfCow(resultSet.getShort("NoOfCow"));
//                    md.setAccountNo(resultSet.getString("BankAcNo"));
//                    listDto.add(new MemberDto(m, md));
//                }
//                resultSet.close();
//                tableData.setItems(FXCollections.observableList(list));
//            } catch (SQLException ee) {
//                System.out.println("Exception in member migrate");
//                ee.printStackTrace();
//            }
//        }
//
//    }
}
