package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.converter.VoucherTypeConvertor;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.VoucherRepository;
import com.eipl.amcs.master.account.repository.VoucherTransactionRepository;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.VoucherService;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import com.eipl.amcs.master.operation.convertor.LedgerCellFactory;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.FocusUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.eipl.amcs.MainApp.context;

public class VoucherAddEditController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<VoucherTransaction> tableData, tableData1;
    @FXML
    TableColumn<VoucherTransaction, String> colLedger, colType, colAmount;
    @FXML
    TableColumn<VoucherTransaction, String> colLedger1, colType1, colAmount1;
    @FXML
    TextField txtVoucherNo, txtBillRefNo, txtRemark, txtAmount, txtNarration;
    @FXML
    DatePicker dpVoucherDate, dpRefDate;
    @FXML
    GridPane gridMaster, gridTransaction;
    @FXML
    VBox vbox;
    @FXML
    HBox hboxMaster;
    @FXML
    ComboBox<Ledger> cboxLedger;
    @FXML
    ComboBox<String> cboxType;
    @FXML
    Label lblCredit, lblDebit;
    @FXML
    ComboBox<VoucherType> cboxVoucherType;

    @FXML
    Button btnClose, btnSave, btnDelete, btnSubLedger, btnAdd;

    private Stage stage;
    private PopupCallback callback;

    private ResourceBundle resourceBundle;

    private ObjectProperty<VoucherTransaction> propVoucherTransactionDto;
    private ObjectProperty<VoucherTransaction> propVoucherTransactionDto1;

    private static BigDecimal totalCredit = BigDecimal.ZERO;
    private static BigDecimal totalDebit = BigDecimal.ZERO;

    private VoucherService voucherService;
    private VoucherRepository voucherRepository;
    private VoucherTransactionRepository voucherTransactionRepository;
    private VoucherTypeService voucherTypeService;
    private NextCodeService nextCodeService;
    private LedgerService ledgerService;


    public VoucherAddEditController() {
        propVoucherTransactionDto = new SimpleObjectProperty<>();
        propVoucherTransactionDto1 = new SimpleObjectProperty<>();
        voucherService = context.getBean(VoucherService.class);
        voucherRepository = context.getBean(VoucherRepository.class);
        voucherTransactionRepository = context.getBean(VoucherTransactionRepository.class);
        voucherTypeService = context.getBean(VoucherTypeService.class);
        nextCodeService = context.getBean(NextCodeService.class);
        ledgerService = context.getBean(LedgerService.class);
    }

    private Voucher voucher;
    private List<VoucherTransaction> voucherTransactionList = new ArrayList<>();
    private List<VoucherSubLedger> voucherSubLedgerList = new ArrayList<>();
    private VoucherDto dto;
    private VoucherTransaction voucherTransaction;


    @Override
    public Node getRoot() {
        return root;
    }

    public void setVoucher(Voucher voucher) {
        if (voucher != null) {
            this.voucher = voucher;
            loadControls();
            loadTransaction(voucher);
            setupTable();
        } else {
            setVoucherNo();
        }
    }

    private void loadTransaction(Voucher voucher) {
        try {
            Optional<Voucher> voucher1 = voucherRepository.findById(voucher.getCode());
            if (voucher1.isPresent()) {
                List<VoucherTransaction> list = voucherService.findAllTransaction(voucher1.get());
                if (list == null || list.isEmpty())
                    return;

                voucherTransactionList.addAll(list);
            }
            for (VoucherTransaction transaction : voucherTransactionList) {
                try {
                    VoucherTransaction voucherTransaction = voucherTransactionRepository.findById(transaction.getCode()).orElse(null);
                    if (voucherTransaction != null)
                        voucherSubLedgerList = voucherService.findAllVoucherSubLedger(voucherTransaction);

                    if (voucherSubLedgerList == null || voucherSubLedgerList.isEmpty())
                        return;
                    voucherTransaction.setVoucherSubLedgers(voucherSubLedgerList);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            voucher.setVoucherTransactions(voucherTransactionList);
            tableData.setItems(FXCollections.observableList(voucherTransactionList.stream().filter(VoucherTransaction::getCreditDebit).collect(Collectors.toList())));
            tableData1.setItems(FXCollections.observableList(voucherTransactionList.stream().filter(e1 -> !e1.getCreditDebit()).collect(Collectors.toList())));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void loadControls() {
        txtBillRefNo.setText(voucher.getBillNo());
        txtRemark.setText(voucher.getRemarks());
        txtVoucherNo.setText(voucher.getCode());
        cboxVoucherType.setValue(voucher.getVoucherType());
        dpVoucherDate.setValue(dpVoucherDate.getValue());
        dpRefDate.setValue(dpRefDate.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpRefDate.setValue(LocalDate.now());
        dpVoucherDate.setValue(LocalDate.now());
        this.resourceBundle = resourceBundle;
        cboxType.getItems().addAll(resourceBundle.getString("credit"), resourceBundle.getString("debit"));
        cboxType.getSelectionModel().select(0);
        loadLedger();
        loadVoucherType();
        setupTable();
        loadData();
        setupComboBox();
        totalCredit = BigDecimal.ZERO;
        totalDebit = BigDecimal.ZERO;
        Platform.runLater(() -> {
            lblCredit.setText(String.valueOf(totalCredit));
            lblDebit.setText(String.valueOf(totalDebit));
        });
//        voucherTransactionList = new ArrayList<>();
        propVoucherTransactionDto.addListener((observable, oldValue, newValue) -> {
            try {
                propVoucherTransactionDto1 = new SimpleObjectProperty<>();
                propVoucherTransactionDto1.bind(tableData1.getSelectionModel().selectedItemProperty());
            } catch (Exception w) {

            }
            if (newValue != null) {
//                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
//                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        propVoucherTransactionDto1.addListener((observable, oldValue, newValue) -> {
            try {

                propVoucherTransactionDto = new SimpleObjectProperty<>();
                propVoucherTransactionDto.bind(tableData.getSelectionModel().selectedItemProperty());
//                Platform.runLater(()->{
//                    tableData.getSelectionModel().clearSelection();
//                });

            } catch (Exception w) {

            }
            if (newValue != null) {
//                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
//                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        cboxLedger.setOnAction(e -> {
            if (cboxLedger.getValue().getHasSubLedger() != null && cboxLedger.getValue().getHasSubLedger())
                btnSubLedger.setDisable(false);
            else
                btnSubLedger.setDisable(false);
        });
        btnSubLedger.setOnAction(e -> {
            try {
                if (txtAmount.getText() != null && !txtAmount.getText().equalsIgnoreCase("") && new BigDecimal(txtAmount.getText()).compareTo(BigDecimal.ZERO) >= 0) {
                    if (voucherTransaction == null) {
                        voucherTransaction = new VoucherTransaction();
                    }
                    if (voucher == null) {
                        voucher = new Voucher();
                        voucher.setCode(txtVoucherNo.getText());
                        voucher.setVoucherDate(dpVoucherDate.getValue());
                    }
                    if (voucher.getVoucherTransactions() == null) voucher.setVoucherTransactions(new ArrayList<>());
                    voucherTransaction.setAmount(new BigDecimal(txtAmount.getText()));
                    voucherTransaction.setCreditDebit(cboxType.getValue().equals(resourceBundle.getString("credit")) ? true : false);
                    voucherTransaction.setLedger(cboxLedger.getValue());
                    voucherTransaction.setNarration(txtNarration.getText());
                    voucherTransaction.setVoucher(voucher);
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherSubLedger", voucherTransaction, this);
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                            resourceBundle.getString("entervalid"));
                    alert.createAlert();
                }
            } catch (NumberFormatException ee) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                        resourceBundle.getString("entervalid"));
                alert.createAlert();
            }
        });
        btnSave.setOnAction(e -> {
            saveData();
        });
        btnAdd.setOnAction(e -> {
            if (!gridTransaction.isDisable()) {
                btnAdd.setText(resourceBundle.getString("add"));
                if (txtAmount.getText() != null) {
                    addTransaction();
                }
            } else {
                gridTransaction.setDisable(false);
                btnAdd.setText(resourceBundle.getString("save"));
            }
        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Voucher.fxml")));
        });
        propVoucherTransactionDto1.bind(tableData1.getSelectionModel().selectedItemProperty());
        propVoucherTransactionDto.bind(tableData.getSelectionModel().selectedItemProperty());
        btnDelete.setOnAction(e -> {
            deleteData();
//            Platform.runLater(() -> {
            lblCredit.setText(String.valueOf(totalCredit));
            lblDebit.setText(String.valueOf(totalDebit));
//            });
            tableData.setItems(FXCollections.observableArrayList(voucherTransactionList.stream().filter(VoucherTransaction::getCreditDebit).collect(Collectors.toList())));
            tableData1.setItems(FXCollections.observableArrayList(voucherTransactionList.stream().filter(e1 -> !e1.getCreditDebit()).collect(Collectors.toList())));
        });
        tableData.setOnMouseClicked(e -> {
            tableData1.getSelectionModel().clearSelection();
            propVoucherTransactionDto1 = new SimpleObjectProperty<>();
            propVoucherTransactionDto1.bind(tableData1.getSelectionModel().selectedItemProperty());
        });
        tableData1.setOnMouseClicked(e -> {
            tableData.getSelectionModel().clearSelection();
            propVoucherTransactionDto = new SimpleObjectProperty<>();
            propVoucherTransactionDto.bind(tableData.getSelectionModel().selectedItemProperty());
        });

    }

    @Override
    public void deleteData() {
        if (propVoucherTransactionDto.get() != null) {
            voucherTransactionList.remove(propVoucherTransactionDto.get());
            setupTable();
            if (propVoucherTransactionDto.get().getCreditDebit())
                totalCredit = totalCredit.subtract(propVoucherTransactionDto.get().getAmount());
            else
                totalDebit = totalDebit.subtract(propVoucherTransactionDto.get().getAmount());
        }
        if (propVoucherTransactionDto1.get() != null) {
            voucherTransactionList.remove(propVoucherTransactionDto1.get());
            setupTable();
            if (propVoucherTransactionDto1.get().getCreditDebit())
                totalCredit = totalCredit.subtract(propVoucherTransactionDto1.get().getAmount());
            else
                totalDebit = totalDebit.subtract(propVoucherTransactionDto1.get().getAmount());
        }
    }

    private void addTransaction() {
        BigDecimal amount = BigDecimal.ZERO;
        if (!cboxLedger.getValue().getHasSubLedger())
            voucherTransaction = new VoucherTransaction();
        voucherTransaction.setCreditDebit(cboxType.getValue().equalsIgnoreCase(resourceBundle.getString("credit")));
        voucherTransaction.setLedger(cboxLedger.getValue());
        try {
            if (new BigDecimal(txtAmount.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                        resourceBundle.getString("entervalid"));
                alert.createAlert();
                return;
            }
            voucherTransaction.setAmount(new BigDecimal(txtAmount.getText()));
        } catch (NumberFormatException ee) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                    resourceBundle.getString("entervalid"));
            alert.createAlert();
            return;
        }

        voucherTransaction.setNarration(txtNarration.getText() != null ? txtNarration.getText() : "");
        if (voucherTransaction.getVoucherSubLedgers() != null) {
            voucherSubLedgerList.addAll(voucherTransaction.getVoucherSubLedgers());
        }

        if (voucherTransaction.getLedger().getHasSubLedger()) {
            for (VoucherSubLedger voucherSubLedger : voucherTransaction.getVoucherSubLedgers()) {
                amount = amount.add(voucherSubLedger.getAmount());
            }
            if (checkAmount(amount)) {
                if (voucherTransaction.getCreditDebit())
                    totalCredit = totalCredit.add(voucherTransaction.getAmount());
                else
                    totalDebit = totalDebit.add(voucherTransaction.getAmount());
                Platform.runLater(() -> {
                    lblCredit.setText(String.valueOf(totalCredit));
                    lblDebit.setText(String.valueOf(totalDebit));
                });
                voucherTransactionList.add(voucherTransaction);

                tableData.setItems(FXCollections.observableArrayList(voucherTransactionList.stream().filter(VoucherTransaction::getCreditDebit).collect(Collectors.toList())));
                tableData1.setItems(FXCollections.observableArrayList(voucherTransactionList.stream().filter(e1 -> !e1.getCreditDebit()).collect(Collectors.toList())));
                setupTable();
                gridTransaction.setDisable(true);
                FocusUtils.requestFocus(cboxLedger);
                clearTransactionControls();
                voucherTransaction = null;
            } else {
                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("amount"), resourceBundle.getString("error.occurred"));
                alert1.createAlert();
            }
        } else {
            if (voucherTransaction.getCreditDebit())
                totalCredit = totalCredit.add(voucherTransaction.getAmount());
            else
                totalDebit = totalDebit.add(voucherTransaction.getAmount());
            Platform.runLater(() -> {
                lblCredit.setText(String.valueOf(totalCredit));
                lblDebit.setText(String.valueOf(totalDebit));
            });
            voucherTransactionList.add(voucherTransaction);

            tableData.setItems(FXCollections.observableArrayList(voucherTransactionList.stream().filter(VoucherTransaction::getCreditDebit).collect(Collectors.toList())));
            tableData1.setItems(FXCollections.observableArrayList(voucherTransactionList.stream().filter(e1 -> !e1.getCreditDebit()).collect(Collectors.toList())));
            setupTable();

            gridTransaction.setDisable(true);
            FocusUtils.requestFocus(cboxLedger);
            clearTransactionControls();
            voucherTransaction = null;
        }

    }

    public Boolean checkAmount(BigDecimal total) {
        if (total.compareTo(new BigDecimal(txtAmount.getText())) != 0) {
            return false;
        }
        return true;
    }

    private void setVoucherNo() {
        try {
            String no = nextCodeService.getNextCode("Voucher", "code", MainApp.identityDto.getSociety().getCode() + "/" + MainApp.getFinancialYear().getCode() + "/", 6);
            if (no != null) {
                txtVoucherNo.setText(no);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setupComboBox() {
        cboxVoucherType.setConverter(new VoucherTypeConvertor(cboxVoucherType));
        cboxLedger.setConverter(new LedgerConvertor(cboxLedger));
        cboxLedger.setCellFactory(new LedgerCellFactory());
        dpVoucherDate.setConverter(new LocalDateConvertor());
        dpRefDate.setConverter(new LocalDateConvertor());
    }

    @Override
    public void saveData() {
        setValuesInObject();

        dto = new VoucherDto();
        dto.setVoucher(voucher);
        int i = 1;
        for (VoucherTransaction transaction : voucherTransactionList) {
            transaction.setVoucher(voucher);
            transaction.setCode(transaction.getVoucher().getCode() + "T" + i);
            int j = 1;
            voucherSubLedgerList.clear();
            if (transaction.getVoucherSubLedgers() != null) {
                for (VoucherSubLedger voucherSubLedger : transaction.getVoucherSubLedgers()) {
                    voucherSubLedger.setCode(transaction.getCode() + "T" + j);
                    voucherSubLedgerList.add(voucherSubLedger);
                    j += 1;
                }
            }
            i += 1;
        }
        dto.setVoucherTransactions(voucherTransactionList);
        dto.setVoucherSubLedgers(voucherSubLedgerList);

        voucherService.save(dto, CommonUtil.setIdentityHeader());
        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                resourceBundle.getString("insert.successful"));
        alert.createAlert();
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Voucher.fxml")));
    }

    private void setValuesInObject() {
        voucher = new Voucher();
        voucher.setCode(txtVoucherNo.getText());
        voucher.setCancelled(false);
        voucher.setVoucherDate(dpVoucherDate.getValue());
        voucher.setBillNo(txtBillRefNo.getText());
        voucher.setRemarks(txtRemark.getText());
        voucher.setBillDate(dpRefDate.getValue());
        voucher.setVoucherType(cboxVoucherType.getValue());
        voucher.setSociety(MainApp.identityDto.getSociety());
        voucher.setUnionCode(MainApp.identityDto.getUnion().getCode());
        voucher.setFinancialYearsCode(MainApp.getFinancialYear().getCode());
        voucher.setAutoPosted(false);
    }

    @Override
    public void setupTable() {
        colLedger.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedger().getName()));
        colAmount.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAmount().toString()));
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreditDebit() ? resourceBundle.getString("credit") : resourceBundle.getString("debit")));
        colLedger1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedger().getName()));
        colAmount1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAmount().toString()));
        colType1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreditDebit() ? resourceBundle.getString("credit") : resourceBundle.getString("debit")));
    }

    @Override
    public void loadData() {
        tableData.setItems(null);
        try {
            Optional<Voucher> vouchers = voucherRepository.findById(voucher != null ? voucher.getCode() : null);
            if (vouchers.isPresent()) {
                List<VoucherTransaction> voucherTransactionList = voucherService.findAllTransaction(vouchers.get());
                if (voucherTransactionList != null)
                    tableData.setItems(FXCollections.observableList(voucherTransactionList));
                else {
                    voucherTransactionList = new ArrayList<>();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadLedger() {
        try {
            List<Ledger> list = ledgerService.findAllByIsActive();
            if (list != null) {
                cboxLedger.setItems(FXCollections.observableList(list));
                cboxLedger.getSelectionModel().select(0);
                if (cboxLedger.getValue().getHasSubLedger())
                    btnSubLedger.setDisable(false);
                new AutoCompleteComboBoxListener<>(cboxLedger);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadVoucherType() {
        try {
            List<VoucherType> list = voucherTypeService.findAll();
            cboxVoucherType.setItems(FXCollections.observableList(list));
            cboxVoucherType.getSelectionModel().select(7);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }


    @Override
    public void clearControls() {
        txtBillRefNo.setText("");
        txtRemark.setText("");
        dpVoucherDate.setValue(LocalDate.now());
        dpRefDate.setValue(LocalDate.now());
        setVoucherNo();
    }

    public void clearTransactionControls() {
        txtAmount.setText("0");
        txtNarration.setText("");
    }


}