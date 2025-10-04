package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkClassLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.master.operation.task.CustomerSaveTask;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleMigrationListSaveTask;
import com.eipl.amcs.setting.dto.MilkCollectionMigration;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.collections4.ListUtils;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class LocalMilkSaleDataMigrationEMandliController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<LocalMilkSale> tableData;
    @FXML
    TableColumn<LocalMilkSale, Number> colQty, colRate, colAmount;
    @FXML
    TableColumn<LocalMilkSale, LocalDate> colSaleDate;
    private ResourceBundle resourceBundle;
    @FXML
    TextField txtDatabase;
    @FXML
    Button btnSave, btnClose, btnGenerate;

    private String selectedFilePath = null;
    private List<Shift> shiftList;
    private List<MilkType> milkTypeList;
    private List<MilkClass> classList;
    private List<Customer> customerList;
    String milkTypeStr = null;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    List<LocalMilkSale> list = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        btnSave.setOnAction(e -> startImportProcess());
        btnClose.setOnAction(e -> this.stage.close());
        btnGenerate.setOnAction(e -> {
            loadImportPreReq(txtDatabase.getText());
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
    public void setupTable() {
        colSaleDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSaleDate().toLocalDate()));
        colSaleDate.setCellFactory(new LocalDateCellFactory<>());
        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>((data.getValue().getAmount())));
        colRate.setCellValueFactory(data -> new SimpleObjectProperty<>((data.getValue().getRate())));
        colQty.setCellValueFactory(data -> new SimpleObjectProperty<>((data.getValue().getQuantity())));
    }


    private void startImportProcess() {
        var task = new LocalMilkSaleMigrationListSaveTask(
                list, list.size());
        task.setOnSucceeded(e -> {
            try {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                        resourceBundle.getString("successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/DataMigration.fxml")));
                this.stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        task.valueProperty().addListener((ChangeListener<? super Integer>) (observable, oldValue, newValue) -> System.out.println("NEW Val: " + newValue));
        new Thread(task).start();
    }


    public void loadImportPreReq(String from) {
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                milkTypeList = task1.get();
                var task = new ShiftLoadTask();
                task.setOnSucceeded(e1 -> {
                    try {
                        shiftList = task.get();
                        var task2 = new MilkClassLoadTask();
                        task2.setOnSucceeded(e2 -> {
                            try {
                                classList = task2.get();
                                var task3 = new CustomerLoadTask();
                                task3.setOnSucceeded(e3 -> {
                                    try {
                                        customerList = new ArrayList<>();
                                        customerList = task3.get();
                                        if (customerList != null && !customerList.isEmpty()) {
                                            setData(from);
                                        } else {
                                            Customer c = new Customer();
                                            c.setCode(MainApp.identityDto.getSociety().getCode() + "0001");
                                            c.setUnion(MainApp.identityDto.getUnion());
                                            c.setSociety(MainApp.identityDto.getSociety());
                                            c.setType(6);
                                            c.setName("Conusmer");
                                            c.setActive(true);
                                            CustomerDetails cd = new CustomerDetails();
                                            CustomerDto dto = new CustomerDto(c, cd);
                                            var task5 = new CustomerSaveTask(dto, (short) 0);
                                            task5.setOnSucceeded(e5 -> {
                                                var task6 = new CustomerLoadTask();
                                                task6.setOnSucceeded(e6 -> {
                                                    try {
                                                        customerList = task6.get();
                                                    } catch (InterruptedException ex) {
                                                        ex.printStackTrace();
                                                    } catch (ExecutionException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                    setData(from);
                                                });
                                                new Thread(task6).start();

                                            });
                                            new Thread(task5).start();
                                        }
                                    } catch (Exception exception) {

                                    }
                                });
                                new Thread(task3).start();
                            } catch (InterruptedException | ExecutionException ex) {
                                ex.printStackTrace();
                            }
                        });
                        new Thread(task2).start();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();


    }


    public void setData(String dbName) {
        {
            List<LocalMilkSale> list = new ArrayList<>();
            try {
                String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";user=sa;password=everest;integretedSecurity=false";
                Map<String, Integer> mapShift = new HashMap<>();
                for (Shift shift : shiftList) {
                    mapShift.put(shift.getName().substring(0, 1).toUpperCase(), shift.getCode());
                }
                Map<String, Integer> mapMilkType = new HashMap<>();
                for (MilkType milkType : milkTypeList) {
                    mapMilkType.put(milkType.getName().toUpperCase().substring(0, 1), milkType.getCode());
                }


                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");

                LocalTime morningTime = LocalTime.of(6, 0);
                LocalTime eveningTime = LocalTime.of(18, 0);
                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     Statement stmt = connection.createStatement();) {

//            try (Connection connection = DriverManager.getConnection(connectionUrl, "", AppConstant.PROMPT_DB_PASS)) {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select format(NDate, 'MMM yyyy') as month, count(*) as count from LocalMilkSale group by format(NDate, 'MMM yyyy')");
                    List<String> listMonth = new ArrayList<>();
                    while (resultSet.next()) {
                        listMonth.add(resultSet.getString("month"));
                    }
                    resultSet.close();
                    statement.close();

                    // select data
                    for (String month : listMonth) {
                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("select * from LocalMilkSale where format(NDate, 'MMM yyyy') = '" + month + "'");
                        List<Map<String, Object>> mapCollection = new ArrayList<>();
                        String shift = null;
                        while (resultSet.next()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("membercode", customerList.get(0).getCode());
                            map.put("amount", new BigDecimal(resultSet.getString("Amount")));
                            map.put("rate", new BigDecimal(resultSet.getString("Rate")));
                            map.put("qty", new BigDecimal(resultSet.getString("Qty")));
                            map.put("unioncode", MainApp.identityDto.getUnion().getCode());
                            shift = resultSet.getString("Shift");
                            LocalDate date = resultSet.getDate("NDate").toLocalDate();
                            if (shift == null || shift.isEmpty() || shift.equalsIgnoreCase("M")) {
                                map.put("shift", mapShift.get("M"));
                                map.put("collectiondate", LocalDateTime.of(date, morningTime));
                            } else {
                                map.put("shift", mapShift.get(shift.toUpperCase()));
                                map.put("collectiondate", LocalDateTime.of(date, eveningTime));
                            }
                            map.put("milktype", mapMilkType.get(resultSet.getString("MilkType")));
                            map.put("code", MainApp.identityDto.getDock().getDockNo() + "-" + ((LocalDateTime) map.get("collectiondate")).format(dateTimeFormatter) + map.get("shift")  + "-" +resultSet.getString("Id"));
                            mapCollection.add(map);
                        }
                        resultSet.close();
                        statement.close();

                        // split map and save in mysql
                        List<List<Map<String, Object>>> listTemp = ListUtils.partition(mapCollection, AppConstant.MIGRATION_LIST_SIZE);
                        String mysqlUrl = "jdbc:mysql://localhost:3366/" + AppConstant.EIPL_DB_NAME;
                        try (Connection connMySql = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
                            String sql = "INSERT INTO local_milk_sale(code,invoice_no,consumer_type,consumer_code,sale_date,shift_code,entry_type,payment_mode," +
                                    "quantity,rate,amount,cash,milk_type_code,milk_class_code,union_code," +
                                    "quantity_mode,converted_quantity,converted_quantity_mode," +
                                    "society_code,dock_no,created_at,created_by) " +
                                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            int current = 1;
                            connMySql.setAutoCommit(false);
                            for (List<Map<String, Object>> maps : listTemp) {
                                PreparedStatement pstmt = connMySql.prepareStatement(sql);
                                for (Map<String, Object> map : maps) {
                                    pstmt.setString(1, map.get("code").toString());
                                    pstmt.setString(2, map.get("code").toString());
                                    pstmt.setObject(3, (short)1);
                                    pstmt.setString(4, map.get("membercode").toString());
                                    pstmt.setObject(5, map.get("collectiondate"));
                                    pstmt.setInt(6,  (int)map.get("shift"));
                                    pstmt.setShort(7, (short) 1);
                                    pstmt.setShort(8, (short) 1);
                                    pstmt.setBigDecimal(9, (BigDecimal) map.get("qty"));
                                    pstmt.setBigDecimal(10, (BigDecimal) map.get("rate"));
                                    pstmt.setBigDecimal(11, (BigDecimal) map.get("amount"));
                                    pstmt.setBigDecimal(12, (BigDecimal) map.get("cash"));
                                    pstmt.setInt(13, (int) map.get("milktype"));
                                    pstmt.setInt(14, 1);
                                    pstmt.setString(15, map.get("unioncode").toString());
                                    pstmt.setInt(16, (int) 1);
                                    pstmt.setBigDecimal(17, (BigDecimal) map.get("cash"));
                                    pstmt.setInt(18, (int) 1);
                                    pstmt.setString(19, MainApp.identityDto.getSociety().getCode());
                                    pstmt.setString(20, MainApp.identityDto.getDock().getDockNo());
                                    pstmt.setObject(21, LocalDateTime.now());
                                    pstmt.setString(22, "MIGR");
                                    pstmt.addBatch();
                                }
                                pstmt.executeBatch();
                                pstmt.close();
                                current++;
                            }
                            connMySql.commit();
                            connMySql.setAutoCommit(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
