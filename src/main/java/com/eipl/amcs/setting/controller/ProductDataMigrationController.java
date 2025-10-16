package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.task.TaxLoadTask;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.task.MilkClassLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.global.task.UnitLoadTask;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.task.ProductGroupLoadTask;
import com.eipl.amcs.master.inventory.task.ProductSaleRateSaveTask;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.master.operation.task.CustomerSaveTask;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.master.inventory.model.ProductAndSaleRateDto;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleMigrationListSaveTask;
import com.eipl.amcs.operation.procurement.task.ProductMigrationListSaveTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProductDataMigrationController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<Product> tableData;
    @FXML
    TableColumn<Product, String> colCode, colName, colGroup;
    @FXML
    TextField txtFilePath;
    @FXML
    Button btnSave, btnClose, btnGenerate, btnBrowse;
    private List<ProductGroup> groupList;
    private List<Unit> unitList;
    private List<Tax> taxList;
    String milkTypeStr = null;
    private String selectedFilePath = null;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    List<ProductAndSaleRateDto> list = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    private ResourceBundle resourceBundle;

//    List<ProductSaleRate> listSaleRate = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        btnSave.setOnAction(e -> startImportProcess());
        btnClose.setOnAction(e -> this.stage.close());
        btnGenerate.setOnAction(e -> {
            loadImportPreReq("SkyWay", txtFilePath.getText());
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


    @Override
    public void setupTable() {
        colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getName()));
        colCode.setCellValueFactory(data -> new SimpleObjectProperty<>((data.getValue().getCode())));
        colGroup.setCellValueFactory(data -> new SimpleObjectProperty<>((data.getValue().getProductGroup().getName())));
    }


    private void startImportProcess() {
        var task = new ProductMigrationListSaveTask(
                list, list.size());
        task.setOnSucceeded(e -> {
            try {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("product"),
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
        var task1 = new ProductGroupLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                groupList = task1.get();
                var task = new UnitLoadTask();
                task.setOnSucceeded(e1 -> {
                    try {
                        unitList = task.get();
                        var task2 = new TaxLoadTask();
                        task2.setOnSucceeded(e2 -> {
                            try {
                                taxList = CommonUtils.getTaxFromDto(task2.get());
                                var task3 = new CustomerLoadTask();
                                task3.setOnSucceeded(e3 ->
                                        {
                                            setData(from, path);
                                        }
                                );
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
        int i = 1;
        if (type.equalsIgnoreCase("Prompt")) {
//            String urlDb = "jdbc:ucanaccess://" + path;
//            String pwd = "PNM^$)&(%*";
//            try (Connection connection = DriverManager.getConnection(urlDb, "", pwd)) {
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("select * from tblILedger");
//                while (resultSet.next()) {
        } else if (type.equalsIgnoreCase("SkyWay")) {
            try {
                List<String> lines = Files.readAllLines(new File(path).toPath(), Charset.forName("UTF-8"));
                for (String line : lines) {
                    String[] arr = line.split(",");
                    Product m = new Product();
                    String name = arr[1].replaceAll("'", "");
                    if (name == null && name.equals(""))
                        System.out.println();
                    else
                        m.setName(name);
                    m.setCode(MainApp.identityDto.getSociety().getCode() + arr[0]);
                    m.setUnion(MainApp.identityDto.getUnion());
                    m.setSociety(MainApp.identityDto.getSociety());
                    m.setProductGroup(groupList.get(0));
                    m.setTax(taxList.get(2));
                    m.setConversionUnit(unitList.get(0));
                    m.setPrimaryUom(unitList.get(0));
                    ProductSaleRate psr = new ProductSaleRate();
                    psr.setCode(m.getCode());
                    psr.setProduct(m);
                    psr.setUnion(MainApp.identityDto.getUnion());
                    psr.setSociety(MainApp.identityDto.getSociety());
                    productList.add(m);
                    list.add(new ProductAndSaleRateDto(m, psr));
                    i++;
                }
                tableData.setItems(FXCollections.observableList(productList));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
}
