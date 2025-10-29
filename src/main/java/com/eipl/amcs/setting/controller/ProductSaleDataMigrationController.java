package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.task.UnitLoadTask;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.master.operation.task.CustomerSaveTask;
import com.eipl.amcs.operation.inventory.dto.ProductSaleMigrateDto;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.procurement.task.ProductSaleMigrationListSaveTask;
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
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductSaleDataMigrationController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<ProductSale> tableData;
    @FXML
    TableColumn<ProductSale, String> colConsumerCode, colPaymentMode;
    @FXML
    TableColumn<ProductSale, Number> colAmount;
    @FXML
    TableColumn<ProductSale, LocalDate> colSaleDate;
    @FXML
    Button btnSave, btnClose, btnBrowse, btnGenerate;
    @FXML
    TextField txtFilePath;
    String milkTypeStr = null;
    List<ProductSaleMigrateDto> listDto = new ArrayList<>();
    List<ProductSale> list = new ArrayList<>();
    private ResourceBundle resourceBundle;
    private List<Unit> unitList;
    private List<Customer> customerList;
    private Stage stage;
    private String selectedFilePath = null;

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
        colSaleDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getInvoiceDate()));
        colSaleDate.setCellFactory(new LocalDateCellFactory<>());
        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>((data.getValue().getAmount())));
        colPaymentMode.setCellValueFactory(data -> new SimpleObjectProperty<>(("CASH")));
        colConsumerCode.setCellValueFactory(data -> new SimpleObjectProperty<>((data.getValue().getConsumerCode())));
    }


    private void startImportProcess() {
        var task = new ProductSaleMigrationListSaveTask(
                listDto, listDto.size());
        task.setOnSucceeded(e -> {
            try {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("productsale"),
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
        var task1 = new UnitLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                unitList = task1.get();
                var task2 = new CustomerLoadTask();
                task2.setOnSucceeded(exx -> {
                    try {
                        customerList = task2.get();
                        if (customerList != null && !customerList.isEmpty()) {
                            setData(from, path);
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
                                    setData(from, path);
                                });
                                new Thread(task6).start();
                            });
                            new Thread(task5).start();
                        }
                    } catch (Exception exceptionx) {
                    }
                });
                new Thread(task2).start();
            } catch (Exception ef) {
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
//                List<LocalMilkSale> list = new ArrayList<>();
                List<String> lines = Files.readAllLines(new File(path).toPath(), StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] arr = line.split(",");
                    ProductSale m = new ProductSale();
                    ProductSaleTransaction pst = new ProductSaleTransaction();
                    m.setInvoiceNo(MainApp.identityDto.getSociety().getCode() + arr[0].replaceAll("'", ""));
                    pst.setInvoiceTxnNo(m.getInvoiceNo());
                    m.setInvoiceDate(LocalDate.parse(arr[1].replaceAll("'", "")));
                    m.setConsumerType((short) 6);
                    m.setConsumerCode(customerList.get(0).getCode());
                    m.setNoOfInstallments((short) 0);
                    m.setTransactionType((short) 0);
                    m.setPaymentMode((short) 0);
                    m.setAmount(new BigDecimal(arr[6]));
                    m.setNetAmount(m.getAmount());
                    m.setDock(MainApp.identityDto.getDock());
                    m.setSociety(MainApp.identityDto.getSociety());
                    m.setUnion(MainApp.identityDto.getUnion());
                    pst.setProductSaleToMember(m);
                    pst.setAmount(m.getAmount());
                    pst.setNetAmount(m.getAmount());
                    pst.setLooseSale(true);
                    pst.setQuantity(BigDecimal.valueOf((int) Double.parseDouble(arr[5])));
                    pst.setRate(new BigDecimal(arr[4]));
                    pst.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    pst.setSocietyCode(MainApp.identityDto.getSociety().getCode());
                    pst.setUnitCode(unitList.get(0).getCode());
                    m.setSociety(MainApp.identityDto.getSociety());
                    m.setDock(MainApp.identityDto.getDock());
                    ProductSaleMigrateDto dto = new ProductSaleMigrateDto(m, pst);
                    list.add(m);
                    listDto.add(dto);
                    i++;
                }
                tableData.setItems(FXCollections.observableList(list));
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
}
