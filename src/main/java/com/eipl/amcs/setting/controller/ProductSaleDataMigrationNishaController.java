package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
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
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.collections4.ListUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.eipl.amcs.utils.AppConstant.DB_LOC;

public class ProductSaleDataMigrationNishaController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TextField txtFilePath;
    @FXML
    Button btnClose, btnGenerate, btnBrowse;
    Random r = new Random();
    int result;
    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<MilkType> milkTypeList;
    private List<MilkClass> classList;
    private List<Customer> customerList;
    private Stage stage;
    private String selectedFilePath;

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
        setupTable();
        btnClose.setOnAction(e -> this.stage.close());
        btnGenerate.setOnAction(e -> {
            loadImportPreReq(txtFilePath.getText());
        });


        btnBrowse.setOnAction(e -> {
            File file = CommonUtils.openFileDialog("Data");
            if (file == null) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), "Data",
                        resourceBundle.getString("select.file"));
                alert.createAlert();
                return;
            }
            txtFilePath.setText(file.getAbsolutePath());
            selectedFilePath = file.getAbsolutePath();
        });
    }


    public void loadImportPreReq(String path) {

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
                                            setData(path);
                                            System.out.println("done");
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
                                                    System.out.println("done");
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


    public void setData(String path) {

        {
            List<ProductSale> list = new ArrayList<>();
            try {
                String urlDb = "jdbc:ucanaccess://" + path;
                //    String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";user=sa;password=everest;integretedSecurity=false";
                Map<String, Integer> mapShift = new HashMap<>();
                for (Shift shift : shiftList) {
                    mapShift.put(shift.getName().substring(0, 1).toUpperCase(), shift.getCode());
                }
                Map<String, Integer> mapMilkType = new HashMap<>();
                for (MilkType milkType : milkTypeList) {
                    mapMilkType.put(milkType.getName().toUpperCase().substring(0, 1), milkType.getCode());
                }

                LocalTime morningTime = LocalTime.of(6, 0);
                LocalTime eveningTime = LocalTime.of(18, 0);

                try (Connection connection = DriverManager.getConnection(urlDb, "", "Oracle8.0")) {
                    Statement statement = connection.createStatement();

                    ResultSet resultSet = statement.executeQuery("select * from Kapat");
                    List<Map<String, Object>> mapCollection = new ArrayList<>();
                    String shift = null;
                    int i = 0;
                    while (resultSet.next()) {
                        i += 1;
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", resultSet.getString("Memcode") + "-" + (resultSet.getString("Payment")));
                        map.put("amount", new BigDecimal(resultSet.getString("Payment")));
                        map.put("qty", new BigDecimal(resultSet.getString("Liters")));
                        // map.put("rate",((BigDecimal)map.get("amount")).divide((BigDecimal) map.get("qty")));
                        map.put("rate", new BigDecimal(resultSet.getString("Kapat1")));
                        map.put("unioncode", MainApp.identityDto.getUnion().getCode());
                        shift = resultSet.getString("shift");
                        LocalDate date = resultSet.getDate("Date").toLocalDate();
                        map.put("Date", date);
                        if (shift == null || shift.isEmpty() || shift.equalsIgnoreCase("M")) {
                            map.put("shift", mapShift.get("M"));
                            map.put("collectiondate", LocalDateTime.of(date, morningTime));
                        } else {
                            map.put("shift", mapShift.get(shift.toUpperCase()));
                            map.put("collectiondate", LocalDateTime.of(date, eveningTime));
                        }

                        result = r.nextInt(100 - 10) + 10;
                        int result2 = r.nextInt(10 - 1) + 1;
                        int result3 = r.nextInt(10 - 1) + 1;
                        map.put("code", resultSet.getString("Date").substring(0, 10) + "-" + ((BigDecimal) map.get("qty")).intValue() + "-" + result2 + "-" + result3 + "-" + result);
                        System.out.println(map.get("code"));
                        mapCollection.add(map);
                    }
                    resultSet.close();
                    statement.close();


                    // split map and save in mysql
                    List<List<Map<String, Object>>> listTemp = ListUtils.partition(mapCollection, AppConstant.MIGRATION_LIST_SIZE);
                    String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
                    try (Connection connMySql = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
                        String sql = "INSERT INTO product_sale(invoice_no,invoice_date,consumer_code,consumer_type,transaction_type," +
                                "amount,net_amount)" +
                                "VALUES (?,?,?,?,?,?,?)";
                        int current = 1;
                        connMySql.setAutoCommit(false);
                        for (List<Map<String, Object>> maps : listTemp) {
                            PreparedStatement pstmt = connMySql.prepareStatement(sql);
                            for (Map<String, Object> map : maps) {

                                pstmt.setString(1, map.get("code").toString());
                                pstmt.setObject(2, map.get("date"));
                                pstmt.setObject(3, map.get(""));
                                pstmt.setString(4, String.valueOf(1));
                                pstmt.setInt(5, 2);

                                pstmt.setBigDecimal(6, ((BigDecimal) map.get("rate")).setScale(2, RoundingMode.HALF_UP));
                                pstmt.setBigDecimal(7, (BigDecimal) map.get("amount"));
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
