package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.task.*;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class VoucherEntryController implements MyInitialization {
    private final ObjectProperty<VoucherTransaction> propDto;
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate, btnAdd, btnDelete;
    @FXML
    private E_DatePicker dpVoucherDate;
    @FXML
    private E_ComboBox<Ledger> cboxLedger;

    @FXML
    private E_TextField txtVoucherNo, txtBillNo;
    @FXML
    private E_NumericField txtAmount;
    @FXML
    private E_ComboBox<Narration> cboxNarration;
    @FXML
    private TableColumn<VoucherTransaction, String> colAmount, colLedger, colNarration;
    @FXML
    private TableView<VoucherTransaction> tableVoucherTransaction;

    @FXML
    private GridPane gridMaster;

    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;

    private Stage stage;
    private PopupCallback callback;

    private BigDecimal anotherSideAmt = BigDecimal.ZERO;

    private boolean credit_debit = true;

    VoucherTransaction anotherSideTxn = new VoucherTransaction();

    private Voucher voucher;
    private List<VoucherTransaction> voucherTransactionList;
    private VoucherType voucherType;

    private boolean isHavalo = false;

    public VoucherEntryController() {
        propDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setDate(LocalDate date) {
        dpVoucherDate.setValue(date);
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setVoucher(Voucher voucher, boolean credit_debit) {
        this.credit_debit = credit_debit;
        voucherTransactionList = new ArrayList<>();
        if (voucher != null) {
            if (voucher.getCode() == null || voucher.getCode().isBlank()) {
                getNextVoucherCode();
            }

            this.voucher = voucher;
            loadVoucherTransaction(voucher.getCode());
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            anotherSideTxn = new VoucherTransaction();
            getNextVoucherCode();
        }
        loadLedger();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        setupTable();
        loadVoucherType();
        loadNarration();
        dpVoucherDate.setValue(LocalDate.now());

        btnClose.setOnAction(e -> {
            this.callback.reloadData(true);
            this.stage.close();
//            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Ledger.fxml")));
        });

        btnAdd.setOnAction(e -> {
            addVoucherTransaction();
        });
        btnDelete.setOnAction(e -> {
            VoucherTransaction voucherTransaction = propDto.get();
            if (voucherTransaction != null)
                deleteVoucherTransaction(voucherTransaction);
        });

        btnSaveUpdate.setOnAction(e -> validateAndSave());
        dpVoucherDate.setConverter(new LocalDateConvertor());
        txtAmount.setOnAction(e -> {
            addVoucherTransaction();
            cboxNarration.getSelectionModel().clearSelection();
            cboxLedger.getSelectionModel().clearSelection();
            txtAmount.setText("0");
        });
    }

    public void loadControls() {
        txtVoucherNo.setText(voucher.getCode());
        txtBillNo.setText(voucher.getBillNo());
        dpVoucherDate.setValue(voucher.getVoucherDate());
    }

    @Override
    public void setupTable() {
        try {
            colAmount.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAmount().toString()));
            colLedger.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedger().toString()));
            colNarration.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNarration()));
            propDto.bind(tableVoucherTransaction.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (isHavalo) {
            this.callback.returnHavalaTransaction(voucherTransactionList, credit_debit);
            this.stage.close();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.voucher != null) {
                voucher = setValuesInObject();
                updateData();
            }
        } else {
            voucher = new Voucher();
            voucher = setValuesInObject();
            saveData();
        }
    }

    private void getNextVoucherCode() {
        var task = new VoucherNumberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty())
                    return;
                txtVoucherNo.setText(nextCode);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private Voucher setValuesInObject() {
        voucher.setCode(txtVoucherNo.getText());
        voucher.setVoucherDate(dpVoucherDate.getValue());
        voucher.setBillDate(dpVoucherDate.getValue());
        voucher.setBillNo(txtBillNo.getText());
        voucher.setAutoPosted(false);
        voucher.setCancelled(false);
        voucher.setSociety(MainApp.identityDto.getSociety());
        voucher.setUnionCode(MainApp.identityDto.getUnion().getCode());
        voucher.setDockCode(MainApp.identityDto.getDock().getDockNo());
        voucher.setMccPlantCode(MainApp.identityDto.getSociety().getMcc().getCode());
        voucher.setPlantCode(MainApp.identityDto.getSociety().getPlant().getCode());
        voucher.setVoucherType(voucherType);

        for (VoucherTransaction voucherTransaction : voucherTransactionList) {
            anotherSideAmt = anotherSideAmt.add(voucherTransaction.getAmount());
        }
        anotherSideTxn.setAmount(anotherSideAmt);
        anotherSideTxn.setNarration(cboxNarration.getFinalText());
        anotherSideTxn.setCreditDebit(!credit_debit);
        anotherSideTxn.setAutoPostedScreen(true);
        voucherTransactionList.add(anotherSideTxn);

        voucher.setVoucherTransactions(voucherTransactionList);
        return voucher;
    }

    private boolean validate() {
        if (dpVoucherDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("datenullerror") + "\n");
        if (voucherTransactionList == null || voucherTransactionList.isEmpty())
            errorMsg.append(resourceBundle.getString("voucher.transaction.null.error") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        VoucherDto voucherDto = new VoucherDto(voucher, voucherTransactionList, new ArrayList<>());

        var task = new VoucherSaveTask(voucherDto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                            sb.toString());
                    alert.createAlert();
                    return;


                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                        resourceBundle.getString("voucher.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
//                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Ledger.fxml")));

                clearControls();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        VoucherDto voucherDto = new VoucherDto(voucher, voucherTransactionList, new ArrayList<>());

        var task = new VoucherSaveTask(voucherDto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                        resourceBundle.getString("voucher.update.successful"));
                alert.createAlert();
                clearControls();
                this.callback.reloadData(true);
                this.stage.close();
//                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Ledger.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxLedger.setConverter(new LedgerConvertor(cboxLedger));
    }

    private void loadVoucherTransaction(String voucherCode) {
        var task = new VoucherTransactionLoadTask(voucherCode);
        task.setOnSucceeded(e -> {
            try {
                List<VoucherTransaction> list = task.get();
                if (list != null) {
                    voucherTransactionList = list;
                    anotherSideTxn = voucherTransactionList.stream().filter(vt -> vt.getCreditDebit() != credit_debit).findFirst().orElse(new VoucherTransaction());
                    voucherTransactionList.remove(anotherSideTxn);
                    tableVoucherTransaction.setItems(FXCollections.observableList(list));
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadLedger() {
        var task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Ledger> list = task.get();
                if (list != null) {
                    cboxLedger.setItems(FXCollections.observableList(list));

                    Ledger cashLedger = list.stream()
                            .filter(t -> t.getName().equalsIgnoreCase("CASH ON HAND AC"))
                            .findFirst()
                            .orElse(new Ledger());

                    anotherSideTxn.setLedger(cashLedger);
                    new AutoCompleteComboBoxListener<>(cboxLedger);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    @Override
    public void clearControls() {
        try {
            getNextVoucherCode();
            txtBillNo.setText("");
            voucherTransactionList = new ArrayList<>();
            cboxLedger.getSelectionModel().clearSelection();
            tableVoucherTransaction.setItems(FXCollections.observableList(voucherTransactionList));
            btnSaveUpdate.setText(resourceBundle.getString("save"));
            cboxNarration.getSelectionModel().clearSelection();
            txtAmount.setText("0");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addVoucherTransaction() {
        errorMsg = new StringBuilder();
        if (!validateTransaction()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        VoucherTransaction voucherTransaction = new VoucherTransaction();
        voucherTransaction.setLedger(cboxLedger.getValue());
        voucherTransaction.setNarration(cboxNarration.getFinalText());
        voucherTransaction.setAmount(new BigDecimal(txtAmount.getText()));
        voucherTransaction.setAutoPostedScreen(false);
        voucherTransaction.setCreditDebit(credit_debit);
        voucherTransactionList.add(voucherTransaction);
        tableVoucherTransaction.setItems(FXCollections.observableList(voucherTransactionList));
        clearTransaction();
    }

    private void deleteVoucherTransaction(VoucherTransaction voucherTransaction) {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {

                if (voucherTransaction.getCode() != null && !voucherTransaction.getCode().isBlank()) {
                    var task = new VoucherTransactionDeleteTask(voucherTransaction.getCode());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                                        resourceBundle.getString("error.occurred"));
                                alert1.createAlert();
                                return;
                            }
                            voucherTransactionList.remove(voucherTransaction);
                            this.callback.reloadData(true);

                            tableVoucherTransaction.setItems(FXCollections.observableList(voucherTransactionList));
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(task).start();
                } else {
                    voucherTransactionList.remove(voucherTransaction);
                    tableVoucherTransaction.setItems(FXCollections.observableList(voucherTransactionList));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setHavaloDetails() {
        for (int i = 0; i <= 2; i++) {
            if (gridMaster.getRowConstraints().size() <= i) {
                gridMaster.getRowConstraints().add(new RowConstraints());
            }
            RowConstraints rc = gridMaster.getRowConstraints().get(i);
            rc.setMinHeight(0);
            rc.setPrefHeight(0);
            rc.setMaxHeight(0);
        }

        gridMaster.getChildren().forEach(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            int actualRow = (rowIndex == null) ? 0 : rowIndex;

            if (actualRow >= 0 && actualRow <= 2) {
                node.setVisible(false);
                node.setManaged(false);
            }
        });
        isHavalo = true;

    }

    private void loadVoucherType() {
        var task = new VoucherTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<VoucherType> voucherTypeList = task.get();
                for (VoucherType vt : voucherTypeList) {
                    if (credit_debit && vt.getCode() == 6) { // CASH PAYMENT
                        voucherType = vt;
                    } else if (!credit_debit && vt.getCode() == 5) { // CASH RECEIPT
                        voucherType = vt;
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void loadNarration() {
        var task = new NarrationLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Narration> narrationList = task.get();
                if (narrationList == null || narrationList.isEmpty())
                    return;
                cboxNarration.setItems(FXCollections.observableList(narrationList));
                new AutoCompleteComboBoxListener<>(cboxNarration);


            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void clearTransaction() {
        try {
            cboxLedger.getSelectionModel().clearSelection();
            cboxNarration.getSelectionModel().clearSelection();
            txtAmount.setText("0");
            FocusUtils.requestFocus(cboxLedger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateTransaction() {
        try {
            if (cboxLedger.getSelectionModel().getSelectedItem() == null)
                errorMsg.append(resourceBundle.getString("ledgernullerror") + "\n");
            return errorMsg.length() == 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
