package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.master.account.converter.SubLedgerConvertor;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.master.account.service.FinancialYearService;
import com.eipl.amcs.master.account.service.SubLedgerOpeningBalanceService;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.master.account.task.SubLedgerOpeningBalanceImportTask;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
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
import java.util.stream.Collectors;

public class SubLedgerOpeningBalanceController implements MyInitialization {

    @FXML
    StackPane root;
    @FXML
    TableView<SubLedgerOpeningBalance> tableData;
    @FXML
    TableColumn<SubLedgerOpeningBalance, String> colFinancialYear, colType, colSubLedger;
    @FXML
    TableColumn<SubLedgerOpeningBalance, BigDecimal> colBalance;
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
    @FXML
    GridPane gridMaster;
    @FXML
    VBox vbox;
    private List<SubLedger> subledgerList;
    private List<FinancialYear> financialYearList;
    private List<LedgerType> ledgerTypeList;

    //    private List<LedgerSubLedgerDto> listLedgerSubLedgerDto;
    @FXML
    Button btnClose, btnSave, btnDelete, btnImport;

    private Stage stage;

    private ResourceBundle resourceBundle;

    private final ObjectProperty<SubLedgerOpeningBalance> propSubLedgerOpeningBalance;
    private FinancialYearService financialYearService;
    private SubLedgerService subLedgerService;
    private SubLedgerOpeningBalanceService subLedgerOpeningBalanceService;

    public SubLedgerOpeningBalanceController() {
        financialYearService = MainApp.context.getBean(FinancialYearService.class);
        subLedgerOpeningBalanceService = MainApp.context.getBean(SubLedgerOpeningBalanceService.class);
        subLedgerService = MainApp.context.getBean(SubLedgerService.class);
        propSubLedgerOpeningBalance = new SimpleObjectProperty<>();
    }

    private SubLedgerOpeningBalance subLedgerOpeningBalance;

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
            if (newValue != null) {
                btnDelete.setDisable(false);
            } else {
                btnDelete.setDisable(true);
            }
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
        try {
            setValuesInObject();
            subLedgerOpeningBalanceService.save(subLedgerOpeningBalance, CommonUtil.setIdentityHeader());
            loadData();
            clearControls();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        subLedgerOpeningBalance.setCreditDebit(cboxType.getValue().equalsIgnoreCase(resourceBundle.getString("debit")) ? false : true);

    }

    private void loadImportPreReq() {
        try {
//            NIMIT ASYNC HERE
            financialYearList = financialYearService.findAll();
            subledgerList = subLedgerService.findAll();
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private List<SubLedgerOpeningBalance> listSubLedgerOpeningBalance;

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
        try {
            MainApp.paneDrop.setVisible(false);
            List<SubLedgerOpeningBalance> list = subLedgerOpeningBalanceService.importSubLedgerBalance(listSubLedgerOpeningBalance, CommonUtil.setIdentityHeader());
            if (list == null || list.isEmpty()) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("subledgeropeningbalance"),
                        resourceBundle.getString("error.occurred"));
                alert.createAlert();
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Import success: ");
            builder.append("\n");
            builder.append("Import fail: ");
            builder.append("\n");

            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("subledgeropeningbalance"),
                    builder.toString());
            alert.createAlert();
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setupTable() {
        colFinancialYear.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFinancialYearsCode()));
        colBalance.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBalance()));
        colSubLedger.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSubLedger().getName()));
        colType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ?
                resourceBundle.getString("credit") : resourceBundle.getString("debit")));

        propSubLedgerOpeningBalance.bind(tableData.getSelectionModel().selectedItemProperty());
    }


    @Override
    public void loadData() {
        tableData.setItems(null);
        try {
            List<SubLedgerOpeningBalance> list = subLedgerOpeningBalanceService.findAll();
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


    public void loadSubLedger() {
        try {
            subledgerList = subLedgerService.findAll();
            if (subledgerList != null) {
                if (cboxSubLedger.getItems() != null) {
                    cboxSubLedger.getItems().clear();
                    cboxSubLedger.getItems().addAll(FXCollections.observableList(
                            subledgerList.stream().filter(p -> p.getType() == cboxSubLedgerType.getValue().getKey()).collect(Collectors.toList())));
                } else {
                    cboxSubLedger.getItems().addAll(FXCollections.observableList(
                            subledgerList.stream().filter(p -> p.getType() == cboxSubLedgerType.getValue().getKey()).collect(Collectors.toList())));
                }
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
            SubLedgerOpeningBalance subLedgerOpeningBalance = propSubLedgerOpeningBalance.get();
            if (subLedgerOpeningBalance != null) {
                try {
                    subLedgerOpeningBalanceService.delete(subLedgerOpeningBalance.getCode(), CommonUtil.setIdentityHeader());
                    loadData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgeropeningbalance"),
                            resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
                }
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
