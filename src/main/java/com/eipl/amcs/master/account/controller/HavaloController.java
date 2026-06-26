package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_Label;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.task.VoucherNumberLoadTask;
import com.eipl.amcs.master.account.task.VoucherSaveTask;
import com.eipl.amcs.master.account.task.VoucherTypeLoadTask;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

public class HavaloController implements MyInitialization, PopupCallback {
    private final ObjectProperty<VoucherTransaction> propDto;
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate, btnDelete;
    @FXML
    private E_Button btnCredit, btnDebit;
    @FXML
    private E_DatePicker dpVoucherDate;
    @FXML
    private E_TextField txtBillRefNo, txtVoucherNo;
    @FXML
    private E_Label lblCredit, lblDebit;
    @FXML
    private TableColumn<VoucherTransaction, String> colAmount, colLedger, colAmount1, colLedger1;
    @FXML
    private TableView<VoucherTransaction> tableDebit, tableCredit;
    BigDecimal crAmt = BigDecimal.ZERO;
    BigDecimal drAmt = BigDecimal.ZERO;


    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;

    private Stage stage;
    private PopupCallback callback;

    private BigDecimal anotherSideAmt = BigDecimal.ZERO;

    private boolean credit_debit = true;

    VoucherTransaction anotherSideTxn = new VoucherTransaction();

    private Voucher voucher;
    private List<VoucherTransaction> debitVoucherTransactionList = new ArrayList<>();
    private List<VoucherTransaction> creditVoucherTransactionList = new ArrayList<>();
    private List<VoucherTransaction> voucherTransactionList = new ArrayList<>();
    private VoucherType voucherType;


    public HavaloController() {
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
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            anotherSideTxn = new VoucherTransaction();
            getNextVoucherCode();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadVoucherType();
        btnCredit.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "HavaloVoucherEntryCredit", null, this, resourceBundle.getString("receipt.credit"));
        });
        btnDebit.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "HavaloVoucherEntryDebit", null, this, resourceBundle.getString("receipt.debit"));
        });
        dpVoucherDate.setValue(LocalDate.now());
        getNextVoucherCode();
        btnSaveUpdate.setOnAction(e -> {
            validateAndSave();
        });
        btnClose.setOnAction(e -> {
            this.callback.reloadData(true);
            this.stage.close();
        });
        dpVoucherDate.setConverter(new LocalDateConvertor());
        FocusUtils.requestFocus(dpVoucherDate);

        dpVoucherDate.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                FocusUtils.requestFocus(txtBillRefNo);
                e.consume();
            }
        });

        txtBillRefNo.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                FocusUtils.requestFocus(btnCredit);
                e.consume();
            }
        });


        root.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if ((tableCredit.getItems() != null && !tableCredit.getItems().isEmpty()) || (tableDebit.getItems() != null && !tableDebit.getItems().isEmpty())) {
                    MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("voucher"),
                            resourceBundle.getString("want.to.save"));
                    Optional<ButtonType> result = alert.createConfirmationAlert();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        validateAndSave();
                        if (!validate())
                            return;
                    }
                    if (callback != null) {
                        callback.reloadData(true);
                    }
                    if (stage != null) {
                        stage.close();
                    }
                } else {
                    if (callback != null) {
                        callback.reloadData(true);
                    }
                    if (stage != null) {
                        stage.close();
                    }
                }
            }
        });

    }

    public void loadControls() {
        txtVoucherNo.setText(voucher.getCode());
        txtBillRefNo.setText(voucher.getBillNo());
        dpVoucherDate.setValue(voucher.getVoucherDate());
    }

    @Override
    public void setupTable() {
        try {
            colAmount.setCellValueFactory(data -> {
                if (data.getValue().getCreditDebit())
                    return new SimpleStringProperty(data.getValue().getAmount().toString());
                else
                    return null;
            });
            colLedger.setCellValueFactory(data -> {
                if (data.getValue().getCreditDebit())
                    return new SimpleStringProperty(data.getValue().getLedger().toString());
                else
                    return null;
            });
            colAmount1.setCellValueFactory(data -> {
                if (!data.getValue().getCreditDebit())
                    return new SimpleStringProperty(data.getValue().getAmount().toString());
                else
                    return null;
            });
            colLedger1.setCellValueFactory(data -> {
                if (!data.getValue().getCreditDebit())
                    return new SimpleStringProperty(data.getValue().getLedger().toString());
                else
                    return null;
            });
//            colNarration.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNarration()));
//            propDto.bind(tableVoucherTransaction.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableCredit);
            TableLocalizationUtil.localizeTable(tableDebit);
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
        voucher.setBillNo(txtBillRefNo.getText());
        voucher.setBillDate(dpVoucherDate.getValue());
        voucher.setAutoPosted(false);
        voucher.setCancelled(false);
        voucher.setVoucherType(voucherType);
        voucher.setSociety(MainApp.identityDto.getSociety());
        voucher.setUnionCode(MainApp.identityDto.getUnion().getCode());
        voucher.setDockCode(MainApp.identityDto.getDock().getDockNo());
        voucher.setMccPlantCode(MainApp.identityDto.getSociety().getMcc().getCode());
        voucher.setPlantCode(MainApp.identityDto.getSociety().getPlant().getCode());
        voucherTransactionList.addAll(creditVoucherTransactionList);
        voucherTransactionList.addAll(debitVoucherTransactionList);
        voucher.setVoucherTransactions(voucherTransactionList);
        return voucher;
    }

    private boolean validate() {
        if (dpVoucherDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("datenullerror") + "\n");
        if (crAmt.compareTo(drAmt) != 0)
            errorMsg.append(resourceBundle.getString("cr.dr.not.match") + "\n");
        if ((debitVoucherTransactionList == null || debitVoucherTransactionList.isEmpty()) || (creditVoucherTransactionList == null || creditVoucherTransactionList.isEmpty()))
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
                        resourceBundle.getString("voucher.insert.successful"));
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
//        cboxLedger.setConverter(new LedgerConvertor(cboxLedger));
    }


    @Override
    public void clearControls() {
        try {
            getNextVoucherCode();
//            txtBillNo.setText("");
            voucherTransactionList = new ArrayList<>();
//            cboxLedger.getSelectionModel().clearSelection();
//            tableVoucherTransaction.setItems(FXCollections.observableList(voucherTransactionList));
            btnSaveUpdate.setText(resourceBundle.getString("save"));
//            txtNarration.setText("");
//            txtAmount.setText("0");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void returnHavalaTransaction(List<VoucherTransaction> voucherTransactionList, boolean credit_debit) {
        try {
            if (credit_debit) {
                creditVoucherTransactionList.addAll(voucherTransactionList);
                tableCredit.setItems(FXCollections.observableList(creditVoucherTransactionList));

                for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                    crAmt = crAmt.add(voucherTransaction.getAmount());
                }
                lblCredit.setText(crAmt.toString());

            } else {
                debitVoucherTransactionList.addAll(voucherTransactionList);
                tableDebit.setItems(FXCollections.observableList(debitVoucherTransactionList));
                for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                    drAmt = drAmt.add(voucherTransaction.getAmount());
                }
                lblDebit.setText(drAmt.toString());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void loadVoucherType() {
        var task = new VoucherTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<VoucherType> voucherTypeList = task.get();
                for (VoucherType vt : voucherTypeList) {
                    if (vt.getCode() == 8) // JOURNAL
                        voucherType = vt;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

}
