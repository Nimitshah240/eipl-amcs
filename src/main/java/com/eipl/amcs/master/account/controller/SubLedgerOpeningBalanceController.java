package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.master.account.converter.SubLedgerConvertor;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.master.account.task.*;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SubLedgerOpeningBalanceController implements MyInitialization {

    private final ObjectProperty<SubLedgerOpeningBalance> propSubLedgerOpeningBalance;
    @FXML
    StackPane root;
    @FXML
    TableView<SubLedgerOpeningBalance> tableData;
    @FXML
    TableColumn<SubLedgerOpeningBalance, String> colFinancialYear, colType, colSubLedger;
    @FXML
    TableColumn<SubLedgerOpeningBalance, BigDecimal> colBalance;
    @FXML
    GridPane gridMaster;
    @FXML
    VBox vbox;
    @FXML
    Button btnClose, btnSave, btnDelete, btnImport;
    @FXML
    private TextField txtBalance;
    @FXML
    private ComboBox<String> cboxType;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxSubLedgerType;
    @FXML
    private ComboBox<SubLedger> cboxSubLedger;
    @FXML
    private ComboBox<FinancialYear> cboxFinancialYear;
    private List<SubLedger> subledgerList;
    private List<FinancialYear> financialYearList;
    private ResourceBundle resourceBundle;
    private SubLedgerOpeningBalance subLedgerOpeningBalance;
    private List<SubLedgerOpeningBalance> listSubLedgerOpeningBalance;

    public SubLedgerOpeningBalanceController() {
        propSubLedgerOpeningBalance = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSave.setText(resourceBundle.getString("add"));
        vbox.getChildren().remove(gridMaster);
        this.resourceBundle = resourceBundle;
        cboxType.getItems().addAll("Debit", "Credit");
        cboxType.getSelectionModel().select(0);
        cboxSubLedgerType.getItems().addAll(CommonUtils.getAllCustomerTypes());
        loadData();

        loadFinancialYear();
        setupComboBox();
        propSubLedgerOpeningBalance.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
        });
        cboxSubLedgerType.setOnAction(e -> {
            loadSubLedger();
        });
        btnSave.setOnAction(e -> {
            if (btnSave.getText().equalsIgnoreCase(resourceBundle.getString("add"))) {
                vbox.getChildren().add(1, gridMaster);
                FocusUtils.requestFocus(cboxFinancialYear);
                btnSave.setText(resourceBundle.getString("save"));
            } else {
                saveData();
                btnSave.setText(resourceBundle.getString("add"));
                vbox.getChildren().remove(gridMaster);
            }
        });
        btnImport.setOnAction(e -> {
            loadImportPreReq();

        });
        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnDelete.setOnAction(e -> {
            deleteData();
        });

    }

    @Override
    public void saveData() {
        setValuesInObject();
        var task = new SubLedgerOpeningBalanceSaveTask(subLedgerOpeningBalance, (short) 0);
        task.setOnSucceeded(e -> {
            loadData();
            clearControls();
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxSubLedger.setConverter(new SubLedgerConvertor(cboxSubLedger));
        cboxSubLedgerType.setConverter(new CustomerTypeConvertor(cboxSubLedgerType));

    }

    private void setValuesInObject() {
        subLedgerOpeningBalance = new SubLedgerOpeningBalance();
        subLedgerOpeningBalance.setSociety(MainApp.identityDto.getSociety());
        subLedgerOpeningBalance.setUnionCode(MainApp.identityDto.getUnion().getCode());
        subLedgerOpeningBalance.setBalance(new BigDecimal(txtBalance.getText()));
        subLedgerOpeningBalance.setFinancialYearsCode(cboxFinancialYear.getValue().getCode());
        subLedgerOpeningBalance.setSubLedger(cboxSubLedger.getValue());
        subLedgerOpeningBalance.setCreditDebit(!cboxType.getValue().equalsIgnoreCase(resourceBundle.getString("debit")));

    }

    private void loadImportPreReq() {
        var task = new FinancialYearLoadTask();
        task.setOnSucceeded(e -> {
            try {
                financialYearList = task.get();
                var task1 = new SubLedgerLoadTask();
                task1.setOnSucceeded(ew -> {
                    try {
                        subledgerList = task1.get();
                        File file = CommonUtils.openExcelFileDialog(resourceBundle.getString("subledgeropeningbalance"));
                        if (file == null) {
                            MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("subledgeropeningbalance"),
                                    resourceBundle.getString("select.file"));
                            alert.createAlert();
                            return;
                        }
                        MainApp.paneDrop.setVisible(true);
                        MainApp.lblMessage.setText("Preparing SubLedgerOpeningBalance...");
                        startImport(file);

                        new Thread(task1).start();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task1).start();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();


    }

    private void startImport(File file) {
        var task = new SubLedgerOpeningBalanceImportTask(file, subledgerList, financialYearList);
        task.setOnSucceeded(e -> {
            try {
                listSubLedgerOpeningBalance = task.get();
                if (listSubLedgerOpeningBalance == null || listSubLedgerOpeningBalance.isEmpty()) {
                    MainApp.paneDrop.setVisible(false);
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("subledgeropeningbalance"),
                            resourceBundle.getString("invalid.file"));
                    alert.createAlert();
                    return;
                }
                MainApp.lblMessage.setText("Importing SubLedgerOpeningBalance...");
                startImportProcess();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void startImportProcess() {
        var task = new SubLedgerOpeningBalanceListSaveTask(listSubLedgerOpeningBalance);
        task.setOnSucceeded(e -> {
            try {
                MainApp.paneDrop.setVisible(false);
                List<SubLedgerOpeningBalance> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("subledgeropeningbalance"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
                String builder = "Import success: " +
                        "\n" +
                        "Import fail: " +
                        "\n";

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("subledgeropeningbalance"),
                        builder);
                alert.createAlert();
                loadData();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        colFinancialYear.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFinancialYearsCode()));
        colBalance.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBalance()));
        colSubLedger.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSubLedger().getName()));
        colType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ?
                resourceBundle.getString("credit") : resourceBundle.getString("debit")));

        propSubLedgerOpeningBalance.bind(tableData.getSelectionModel().selectedItemProperty());
        TableLocalizationUtil.localizeTable(tableData);

    }


    @Override
    public void loadData() {
        tableData.setItems(null);
        SubLedgerOpeningBalanceLoadTask task = new SubLedgerOpeningBalanceLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SubLedgerOpeningBalance> list = task.get();
                if (list != null)
                    tableData.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadFinancialYear() {
        var task = new FinancialYearLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<FinancialYear> list = task.get();
                if (list != null) {
                    cboxFinancialYear.getItems().addAll(FXCollections.observableList(list));
                    cboxFinancialYear.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadSubLedger() {
        var task = new SubLedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                subledgerList = task.get();
                if (subledgerList != null) {
                    if (cboxSubLedger.getItems() != null) {
                        cboxSubLedger.getItems().clear();
                        cboxSubLedger.getItems().addAll(FXCollections.observableList(
                                subledgerList.stream().filter(p -> p.getType() == cboxSubLedgerType.getValue().getKey()).collect(Collectors.toList())));
                    } else {
                        cboxSubLedger.getItems().addAll(FXCollections.observableList(
                                subledgerList.stream().filter(p -> p.getType() == cboxSubLedgerType.getValue().getKey()).collect(Collectors.toList())));
                    }
                } else {

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            SubLedgerOpeningBalance subLedgerOpeningBalance = propSubLedgerOpeningBalance.get();
            if (subLedgerOpeningBalance != null) {
                var task = new SubLedgerOpeningBalanceDeleteTask(subLedgerOpeningBalance.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void clearControls() {
        txtBalance.setText("");
        cboxFinancialYear.setValue(MainApp.getFinancialYear());
        cboxSubLedger.getSelectionModel().select(0);
        cboxType.getSelectionModel().select(0);
    }
}
