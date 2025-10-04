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
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.setting.dto.MilkCollectionMigration;
import com.eipl.amcs.setting.task.EMandaliMilkCollectionDbProcess;
import com.eipl.amcs.setting.task.EMandaliMilkCollectionDbSaveTask;
import com.eipl.amcs.setting.task.FriendsMilkCollectionDbProcess;
import com.eipl.amcs.setting.task.FriendsMilkCollectionDbSaveTask;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MilkCollectionDataMigrationEMandaliController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<MilkCollectionMigration> tableData;
    @FXML
    TableColumn<MilkCollectionMigration, String> colMonth, colCount;
    @FXML
    private TextField  txtCow, txtBuffalo,txtdatabase;
    @FXML
    private Button btnSave, btnClose, btnGenerate;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private Label lblStatus;

    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<MilkType> milkTypeList;
    private String selectedFilePath;


    private List<Member> memberList;
    String milkTypeStr = null;

    @Override
    public Node getRoot() {
        return root;
    }

    List<MilkCollection> list = new ArrayList<>();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");

    private Stage stage;

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

//        btnBrowse.setOnAction(e -> {
//            File file = CommonUtils.openFileDialog("Data");
//            if (file == null) {
//                MyAlert alert = new WarningAlert(MainApp.getStage(), "Data",
//                        resourceBundle.getString("select.file"));
//                alert.createAlert();
//                return;
//            }
//            txtFilePath.setText(file.getAbsolutePath());
//            selectedFilePath = file.getAbsolutePath();
//        });
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
        var task = new EMandaliMilkCollectionDbProcess(selectedFilePath,txtdatabase.getText(),dpFromDate.getValue(), dpToDate.getValue());
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
        var task = new EMandaliMilkCollectionDbSaveTask(milkTypeList, shiftList,
                txtCow.getText(), txtBuffalo.getText(),txtdatabase.getText(),dpFromDate.getValue(),dpToDate.getValue());
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

//    private void startImportProcess() {
//        var task = new MilkCollectionMigrationListSaveTask(listQueue, listQueue.size());
//        task.setOnSucceeded(e -> {
//            try {
//                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
//                        resourceBundle.getString("successful"));
//                alert.createAlert();
//                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/DataMigration.fxml")));
//                System.out.println("DONE");
//                this.stage.close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
//        task.valueProperty().addListener((ChangeListener<? super Integer>) (observable, oldValue, newValue) -> System.out.println("NEW Val: " + newValue));
//        new Thread(task).start();
//    }


//    private Queue<List<MilkCollection>> listQueue = new LinkedList<>();

//    public void setData() throws ClassNotFoundException, SQLException {
//        int i = 1;
////        if (type.equalsIgnoreCase("Prompt")) {
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection con = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3366/eipl_amcs_db", "root", "root");
//        Statement stmt = con.createStatement();
//        String rs = ("INSERT INTO `milk_collection`\n" +
//                "(`code`,\n" +
//                "`sample_no`,\n" +
//                "`collection_date`,\n" +
//                "`fat`,\n" +
//                "`snf`,\n" +
//                "`rtpl`,\n" +
//                "`qty`,\n" +
//                "`amount`,\n" +
//                "`is_weight_auto`,\n" +
//                "`is_quality_auto`,\n" +
//                "`is_avg_param`,\n" +
//                "`union_code`,\n" +
//                "`qty_mode`,\n" +
//                "`converted_qty`,\n" +
//                "`converted_qty_mode`,\n" +
//                "`member_code`,\n" +
//                "`shift_code`,\n" +
//                "`milk_type_code`,\n" +
//                "`milk_quality_type_code`,\n" +
//                "`society_code`,\n" +
//                "`dock_no`,\n" +
//                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//
//
//        String urlDb = "jdbc:ucanaccess://" + txtFilePath.getText();
//        String pwd = "PNM^$)&(%*";
//        try (Connection connection = DriverManager.getConnection(urlDb, "", pwd)) {
//            PreparedStatement stmnt = connection.prepareStatement(rs);
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("select * from tblILedger");
//            while (resultSet.next()) {
//                MilkCollection m = new MilkCollection();
//                m.setFat(new BigDecimal(resultSet.getString("Fat")));
//                m.setSnf(new BigDecimal(resultSet.getString("Snf")));
//                m.setAmount(new BigDecimal(resultSet.getString("Amount")));
//                m.setRtpl(new BigDecimal(resultSet.getString("Rate")));
//                m.setQty(new BigDecimal(resultSet.getString("Qty")));
//                String shiftStr = resultSet.getString("Shift");
//                Shift shift = null;
//                if (shiftStr == null || shiftStr.isEmpty())
//                    shift = shiftList.get(0);
//                else {
//                    shift = shiftList.stream().filter(p -> shiftStr.equalsIgnoreCase(p.getName()) || shiftStr.charAt(0) == p.getName().toLowerCase().charAt(0))
//                            .findAny().orElse(null);
//                    if (shift == null)
//                        shift = shiftList.get(0);
//                }
//                m.setShift(shift);
//
//                LocalDate date = resultSet.getDate("Date").toLocalDate();
//                m.setCollectionDate(CommonUtils.getLocalDateTimeFromDateAndShift(date, shift));
//
//                String invoice = resultSet.getString("InvNo");
//                if (invoice.contains("b"))
//                    milkTypeStr = "b";
//                else
//                    milkTypeStr = "c";
//                MilkType milkType = null;
//                if (milkTypeStr == null || milkTypeStr.isEmpty())
//                    milkType = milkTypeList.get(0);
//                else {
//                    milkType = milkTypeList.stream().filter(p -> milkTypeStr.equalsIgnoreCase(p.getName()) || milkTypeStr.charAt(0) == p.getName().charAt(0) || milkTypeStr.toLowerCase().charAt(0) == p.getName().toLowerCase().charAt(0))
//                            .findAny().orElse(null);
//                    if (milkType == null)
//                        milkType = milkTypeList.get(0);
//                }
//                m.setMilkType(milkType);
//                String code = MainApp.identityDto.getSociety().getCode() + resultSet.getString("SabhasadId");
//                if (code.equalsIgnoreCase(MainApp.identityDto.getSociety().getCode() + "0000")) {
//                    continue;
//                }
//                Member member = memberList.stream().filter(p -> code.equals(p.getCode())).findAny().orElse(null);
//                if (member == null)
//                    continue;
//                m.setMember(member);
//                m.setCode(MainApp.identityDto.getDock().getDockNo() + "-" + m.getCollectionDate().format(dateTimeFormatter) +
//                        m.getShift().getCode() + "-" + m.getSampleNo());
//
//                m.setDensity(new BigDecimal("0"));
//                m.setLectose(new BigDecimal("0"));
//                m.setProtein(new BigDecimal("0"));
//
//                m.setWeightAuto(false);
//                m.setWeightAt(null);
//                m.setQualityAuto(false);
//                m.setQualityAt(null);
//                m.setAvgParam(false);
//
//                m.setWsCode(null);
//                m.setAnalyserCode(null);
//
//                m.setUnionCode(MainApp.identityDto.getUnion().getCode());
//                m.setQtyMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MEMBER_COLLECTION_QTY_MODE, "0")));
//                m.setConvertedQtyMode(m.getQtyMode() == 0 ? 1 : 0);
//                m.setSociety(MainApp.identityDto.getSociety());
//                m.setDock(MainApp.identityDto.getDock());
//
//                m.setSampleNo(resultSet.getInt("SrNo"));
//                stmnt.setString(1, m.getCode());
//                stmnt.setInt(2, resultSet.getInt("SrNo"));
//                stmnt.setObject(3, m.getCollectionDate());
//                stmnt.setBigDecimal(4, new BigDecimal(resultSet.getString("Fat")));
//                stmnt.setBigDecimal(5, new BigDecimal(resultSet.getString("Snf")));
//                stmnt.setBigDecimal(6, new BigDecimal(resultSet.getString("Rate")));
//                stmnt.setBigDecimal(7, new BigDecimal(resultSet.getString("Qty")));
//                stmnt.setBigDecimal(8, new BigDecimal(resultSet.getString("Amount")));
//                stmnt.setBoolean(9, m.isWeightAuto());
//                stmnt.setBoolean(10, m.isQualityAuto());
//                stmnt.setBoolean(11, m.isAvgParam());
//                stmnt.setString(12, m.getUnionCode());
//                stmnt.setInt(13, m.getQtyMode());
//                stmnt.setBigDecimal(14, m.getConvertedQty());
//                stmnt.setInt(15, m.getConvertedQtyMode());
//                stmnt.setString(16, MainApp.identityDto.getSociety().getCode() + resultSet.getString("SabhasadId"));
//                stmnt.setInt(17, Integer.parseInt(resultSet.getString("Shift")));
//                stmnt.setInt(18, m.getMilkType().getCode());
//                stmnt.setInt(19, m.getMilkQualityType().getCode());
//                stmnt.setString(20, MainApp.identityDto.getSociety().getCode());
//                stmnt.setString(21, MainApp.identityDto.getDock().getDockNo());
//                list.add(m);
//                stmnt.addBatch();
//                i++;
//                if (i > 100) {
//                    listQueue.add(list);
//                    i = 1;
//                    stmnt.executeBatch();
//                    list = new ArrayList<>();
//                    save();
//                }
//            }
//            if (!list.isEmpty()) {
//                listQueue.add(list);
//            }
//            resultSet.close();
//            System.out.println(listQueue.size());
//            tableData.setItems(FXCollections.observableList(list));
//        } catch (SQLException ee) {
//            ee.printStackTrace();
//        }
//        } else if (type.equalsIgnoreCase("SkyWay")) {
//            try {
//                List<MilkCollection> list = new ArrayList<>();
//                List<String> lines = Files.readAllLines(new File(path).toPath(), Charset.forName("UTF-8"));
//                System.out.println("Lines: " + lines.size());
//                int count = 0;
//                for (String line : lines) {
//                    String[] arr = line.split(",");
//                    MilkCollection m = new MilkCollection();
//                    m.setFat(new BigDecimal(arr[4]));
//                    m.setSnf(new BigDecimal(9.5));
//                    m.setAmount(new BigDecimal(arr[7]));
////                    m.setRtpl(new BigDecimal(resultSet.getString("Rate")));
//                    m.setQty(new BigDecimal(arr[5]));
//                    BigDecimal rtpl = m.getAmount().divide(m.getQty(), RoundingMode.DOWN);
//                    m.setRtpl(rtpl);
//                    m.setSampleNo(Integer.parseInt(arr[10]));
////                    m.setCode(MainApp.identityDto.getDock().getDockNo()+"-"+m.getSampleNo());
//                    String shiftStr = arr[1];
//                    Shift shift = null;
//                    if (shiftStr == null || shiftStr.isEmpty())
//                        shift = shiftList.get(0);
//                    else {
//                        if (shiftStr.equalsIgnoreCase("1"))
//                            shift = shiftList.get(0);
//                        else
//                            shift = shiftList.get(1);
//                    }
//                    m.setShift(shift);
//
//                    LocalDate date = LocalDate.parse(arr[0].replaceAll("'",""));
//                    m.setCollectionDate(CommonUtils.getLocalDateTimeFromDateAndShift(date,shift));
//
//                    String invoice  = arr[3];
//                    if(invoice.contains("1"))
//                        milkTypeStr = "b";
//                    else
//                        milkTypeStr = "c";
//                    MilkType milkType = null;
//                    if (milkTypeStr == null || milkTypeStr.isEmpty())
//                        milkType = milkTypeList.get(0);
//                    else {
//                        milkType = milkTypeList.stream().filter(p -> milkTypeStr.equalsIgnoreCase(p.getName()) || milkTypeStr.charAt(0) == p.getName().charAt(0) || milkTypeStr.toLowerCase().charAt(0) == p.getName().toLowerCase().charAt(0))
//                                .findAny().orElse(null);
//                        if (milkType == null)
//                            milkType = milkTypeList.get(0);
//                    }
//                    m.setMilkType(milkType);
//                    String code = MainApp.identityDto.getSociety().getCode() +String.format("%04d", Integer.parseInt(arr[2]));
//                    if (code.equalsIgnoreCase(MainApp.identityDto.getSociety().getCode() + "0000")) {
//                        continue;
//                    }
//                    Member member = memberList.stream().filter(p -> code.equals(p.getCode())).findAny().orElse(null);
//                    if (member == null)
//                        continue;
//                    m.setMember(member);
//                    m.setCode(MainApp.identityDto.getDock().getDockNo()+"-"+m.getCollectionDate().format(dateTimeFormatter)+
//                            m.getShift().getCode() + "-" + m.getSampleNo());
//
//                    m.setDensity(new BigDecimal("0"));
//                    m.setLectose(new BigDecimal("0"));
//                    m.setProtein(new BigDecimal("0"));
//
//                    m.setWeightAuto(false);
//                    m.setWeightAt(null);
//                    m.setQualityAuto(false);
//                    m.setQualityAt(null);
//                    m.setAvgParam(false);
//
//                    m.setWsCode(null);
//                    m.setAnalyserCode(null);
//
//                    m.setUnionCode(MainApp.identityDto.getUnion().getCode());
//                    m.setQtyMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MEMBER_COLLECTION_QTY_MODE, "0")));
//                    m.setConvertedQtyMode(m.getQtyMode() == 0 ? 1 : 0);
//                    m.setSociety(MainApp.identityDto.getSociety());
//                    m.setDock(MainApp.identityDto.getDock());
//
//                    list.add(m);
//                    i++;
//                    if(i>100){
//                        listQueue.add(list);
//                        count += list.size();
//                        i=1;
//                        list = new ArrayList<>();
//                    }
//                }
//                if(!list.isEmpty()) {
//                   listQueue.add(list);
//                    count += list.size();
//                }
//                System.out.println("Rec: " + count);
//                tableData.setItems(FXCollections.observableList(list));
//            } catch (Exception ee) {
//                ee.printStackTrace();
//            }
//        }
//    }

//    private void save() throws SQLException, ClassNotFoundException {
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection con = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3366/eipl_amcs_db", "root", "root");
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("INSERT INTO `milk_collection`\n" +
//                "(`code`,\n" +
//                "`sample_no`,\n" +
//                "`collection_date`,\n" +
//                "`fat`,\n" +
//                "`snf`,\n" +
//                "`rtpl`,\n" +
//                "`qty`,\n" +
//                "`amount`,\n" +
//                "`is_weight_auto`,\n" +
//                "`is_quality_auto`,\n" +
//                "`is_avg_param`,\n" +
//                "`union_code`,\n" +
//                "`qty_mode`,\n" +
//                "`converted_qty`,\n" +
//                "`converted_qty_mode`,\n" +
//                "`member_code`,\n" +
//                "`shift_code`,\n" +
//                "`milk_type_code`,\n" +
//                "`milk_quality_type_code`,\n" +
//                "`society_code`,\n" +
//                "`dock_no`,\n" +
//                "VALUES"
//
//        );
//    }
}
