package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.service.FinancialYearService;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.task.LedgerOpeningBalanceImportTask;
import com.eipl.amcs.util.CommonUtil;
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

import static com.eipl.amcs.MainApp.context;

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
    private FinancialYearService financialYearService;
    private LedgerService ledgerService;
    private LedgerOpeningBalanceService ledgerOpeningBalanceService;


    private final ObjectProperty<LedgerOpeningBalance> propLedgerOpeningBalance;

    public LedgerOpeningBalanceController() {
        financialYearService = context.getBean(FinancialYearService.class);
        ledgerService = context.getBean(LedgerService.class);
        ledgerOpeningBalanceService = context.getBean(LedgerOpeningBalanceService.class);
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
        try {
            financialYearList = financialYearService.findAll();
            ledgerList = ledgerService.findAllByIsActive();
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
        try {
            MainApp.paneDrop.setVisible(false);
            List<LedgerOpeningBalance> list = ledgerOpeningBalanceService.importLedgerBalance(listLedgerOpeningBalance, CommonUtil.setIdentityHeader());
            if (list == null || list.isEmpty()) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                        resourceBundle.getString("error.occurred"));
                alert.createAlert();
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Import success: ");
            builder.append("\n");
            builder.append("Import fail: ");
            builder.append("\n");

            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                    builder.toString());
            alert.createAlert();
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveData() {
        try {
            setValuesInObject();
            ledgerOpeningBalanceService.save(ledgerOpeningBalance, CommonUtil.setIdentityHeader());
            loadData();
            clearControls();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        try {
            tableData.setItems(null);
            List<LedgerOpeningBalance> list = ledgerOpeningBalanceService.findAll();
            if (list != null)
                tableData.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadFinancialYear() {
        try {
            List<FinancialYear> list = financialYearService.findAll();
            if (list != null) {
                cboxFinancialYear.getItems().addAll(FXCollections.observableList(list));
                cboxFinancialYear.getSelectionModel().select(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLedger() {
        try {
            List<Ledger> list = ledgerService.findAllByIsActive();
            if (list != null) {
                cboxLedger.getItems().addAll(FXCollections.observableList(list));
                cboxLedger.getSelectionModel().select(0);
                new AutoCompleteComboBoxListener<>(cboxLedger);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                try {
                    ledgerOpeningBalanceService.delete(ledgerOpeningBalance.getCode(), CommonUtil.setIdentityHeader());
                    loadData();
                } catch (Exception ex) {
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                            resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
                    ex.printStackTrace();
                }
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
