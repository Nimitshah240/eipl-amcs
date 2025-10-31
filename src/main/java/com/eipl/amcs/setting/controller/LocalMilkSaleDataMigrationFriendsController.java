package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LocalMilkSaleDataMigrationFriendsController implements MyInitialization {
    private final String selectedFilePath = null;
    @FXML
    StackPane root;
    @FXML
    TableView<LocalMilkSale> tableData;
    @FXML
    TableColumn<LocalMilkSale, Number> colQty, colRate, colAmount;
    @FXML
    TableColumn<LocalMilkSale, LocalDate> colSaleDate;
    @FXML
    TextField txtFilePath;
    @FXML
    Button btnSave, btnClose, btnBrowse, btnGenerate;
    List<LocalMilkSale> list = new ArrayList<>();
    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<MilkType> milkTypeList;
    private List<MilkClass> classList;
    private List<Customer> customerList;
    private Stage stage;

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
        btnSave.setOnAction(e -> startImportProcess());
        btnClose.setOnAction(e -> this.stage.close());
        btnGenerate.setOnAction(e -> {
            loadImportPreReq("SkyWay", txtFilePath.getText());
        });
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


    public void loadImportPreReq(String from, String path) {
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
                                            setData(from, path);
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
                                                    setData(from, path);
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


    public void setData(String type, String path) {
        try {
            String urlDb = "jdbc:ucanaccess://" + path;

            try (Connection connection = DriverManager.getConnection(urlDb, "", "")) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from localsale");

                while (resultSet.next()) {
                    // Member
                    LocalMilkSale m = new LocalMilkSale();
                    int codeEx = CommonUtils.strToInteger(resultSet.getString("memCode"));
                    m.setInvoiceNo(String.format("%04d", codeEx));
                    String[] nameArr = resultSet.getString("memName") != null ?
                            resultSet.getString("memName").split("\\s+") : null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
