package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.task.*;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
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
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LedgerOpeningBalanceController implements MyInitialization {

    @FXML
    StackPane root;
    @FXML
    TableView<LedgerOpeningBalance> tableData;
    @FXML
    TableColumn<LedgerOpeningBalance, String> colFinancialYear, colLedger, colType;
    @FXML
    TableColumn<LedgerOpeningBalance, BigDecimal> colBalance;
    @FXML
    private TextField txtBalance;
    @FXML
    private ComboBox<String> cboxType;
    @FXML
    private ComboBox<Ledger> cboxLedger;
    @FXML
    private ComboBox<FinancialYear> cboxFinancialYear;
    @FXML
    GridPane gridMaster;
    @FXML
    VBox vbox;

    @FXML
    Button btnClose, btnSave, btnDelete, btnImport;

    private Stage stage;

    private List<Ledger> ledgerList;
    private List<FinancialYear> financialYearList;

    private ResourceBundle resourceBundle;

    private final ObjectProperty<LedgerOpeningBalance> propLedgerOpeningBalance;

    public LedgerOpeningBalanceController() {
        propLedgerOpeningBalance = new SimpleObjectProperty<>();
    }

    private LedgerOpeningBalance ledgerOpeningBalance;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSave.setText(resourceBundle.getString("add"));
        vbox.getChildren().remove(gridMaster);
        this.resourceBundle = resourceBundle;
        cboxType.getItems().addAll(resourceBundle.getString("debit"), resourceBundle.getString("credit"));
        cboxType.getSelectionModel().select(0);
        loadData();
        loadLedger();
        setupComboBox();
        loadFinancialYear();
        propLedgerOpeningBalance.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDelete.setDisable(false);
            } else {
                btnDelete.setDisable(true);
            }
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

    private void loadImportPreReq() {
        var task = new FinancialYearLoadTask();
        task.setOnSucceeded(e -> {
            try {
                financialYearList = task.get();
                var task1 = new LedgerLoadTask();
                task1.setOnSucceeded(ew -> {
                    try {
                        ledgerList = task1.get();
                        File file = CommonUtils.openExcelFileDialog(resourceBundle.getString("ledgeropeningbalance"));
                        if (file == null) {
                            MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                                    resourceBundle.getString("select.file"));
                            alert.createAlert();
                            return;
                        }

                        MainApp.paneDrop.setVisible(true);
                        MainApp.lblMessage.setText("Preparing LedgerOpeningBalance...");
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

    private List<LedgerOpeningBalance> listLedgerOpeningBalance;

    private void startImport(File file) {
        var task = new LedgerOpeningBalanceImportTask(file, ledgerList, financialYearList);
        task.setOnSucceeded(e -> {
            try {
                listLedgerOpeningBalance = task.get();
                if (listLedgerOpeningBalance == null || listLedgerOpeningBalance.isEmpty()) {
                    MainApp.paneDrop.setVisible(false);
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                            resourceBundle.getString("invalid.file"));
                    alert.createAlert();
                    return;
                }
                MainApp.lblMessage.setText("Importing LedgerOpeningBalance...");
                startImportProcess();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void startImportProcess() {
        var task = new LedgerOpeningBalanceListSaveTask(listLedgerOpeningBalance);
        task.setOnSucceeded(e -> {
            try {
                MainApp.paneDrop.setVisible(false);
                List<LedgerOpeningBalance> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("Import success: ");
//                builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("success")).count());
                builder.append("\n");
                builder.append("Import fail: ");
//               builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("error")).count());
                builder.append("\n");

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                        builder.toString());
                alert.createAlert();
                loadData();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void saveData() {
        setValuesInObject();
        var task = new LedgerOpeningBalanceSaveTask(ledgerOpeningBalance, (short) 0);
        task.setOnSucceeded(e -> {
            loadData();
            clearControls();
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxLedger.setConverter(new LedgerConvertor(cboxLedger));
        new AutoCompleteComboBoxListener<>(cboxLedger);

    }

    private void setValuesInObject() {
        ledgerOpeningBalance = new LedgerOpeningBalance();
        ledgerOpeningBalance.setSociety(MainApp.identityDto.getSociety());
        ledgerOpeningBalance.setUnionCode(MainApp.identityDto.getUnion().getCode());
        ledgerOpeningBalance.setBalance(new BigDecimal(txtBalance.getText()));
        ledgerOpeningBalance.setFinancialYearsCode(cboxFinancialYear.getValue().getCode());
        ledgerOpeningBalance.setLedger(cboxLedger.getValue());
        ledgerOpeningBalance.setAutoManual(Boolean.FALSE);
        ledgerOpeningBalance.setCreditDebit(cboxType.getValue().equalsIgnoreCase(resourceBundle.getString("debit")) ? false : true);

    }

    @Override
    public void setupTable() {
        try {
            colFinancialYear.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFinancialYearsCode()));
            colBalance.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBalance()));
            colLedger.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().getName()));
            colType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ?
                    resourceBundle.getString("credit") : resourceBundle.getString("debit")));

            propLedgerOpeningBalance.bind(tableData.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        tableData.setItems(null);
        LedgerOpeningBalanceLoadTask task = new LedgerOpeningBalanceLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<LedgerOpeningBalance> list = task.get();
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

    public void loadLedger() {
        var task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Ledger> list = task.get();
                if (list != null) {
                    cboxLedger.getItems().addAll(FXCollections.observableList(list));
                    cboxLedger.getSelectionModel().select(0);
                    new AutoCompleteComboBoxListener<>(cboxLedger);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            LedgerOpeningBalance ledgerOpeningBalance = propLedgerOpeningBalance.get();
            if (ledgerOpeningBalance != null) {
                var task = new LedgerOpeningBalanceDeleteTask(ledgerOpeningBalance.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || respDelete.booleanValue() == false) {
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
        cboxFinancialYear.getSelectionModel().select(0);
        cboxLedger.getSelectionModel().select(0);
        cboxType.getSelectionModel().select(0);
    }
}
