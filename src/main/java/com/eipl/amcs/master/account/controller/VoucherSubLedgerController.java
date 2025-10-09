package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.converter.SubLedgerConvertor;
import com.eipl.amcs.master.account.dto.LedgerSubLedgerDto;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class VoucherSubLedgerController implements MyInitialization {
    @FXML
    VBox vbox;
    @FXML
    GridPane gridMaster;
    @FXML
    ComboBox<SubLedger> cboxSubLedger;
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnAdd, btnEdit, btnDelete, btnDone;
    @FXML
    private TextField txtDate, txtAmount, txtType, txtTotalAmount, txtVoucherNo, txtRemark, txtAmount1;
    @FXML
    private TableView<VoucherSubLedger> tableData;
    @FXML
    private TableColumn<VoucherSubLedger, String> colAmount, colSubLedgerCode, colSubLedger, colRemark;
    private BigDecimal total = BigDecimal.ZERO;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private final StringBuilder errorMsg = null;
    private PopupCallback callback;
    private List<VoucherSubLedger> voucherSubLedgerList;

    private SubLedgerService subLedgerService;
    private LedgerService ledgerService;

    private final ObjectProperty<VoucherSubLedger> propSubLedger;
    public VoucherTransaction voucherTransaction = new VoucherTransaction();

    public VoucherSubLedgerController() {
        subLedgerService = MainApp.context.getBean(SubLedgerService.class);
        ledgerService = MainApp.context.getBean(LedgerService.class);
        propSubLedger = new SimpleObjectProperty<>();
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vbox.getChildren().remove(gridMaster);
        this.resourceBundle = resourceBundle;
        voucherSubLedgerList = new ArrayList<>();

        loadData();
        setupComboBox();
        setupTable();
        propSubLedger.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnClose.setOnAction(e -> this.stage.close());
        btnDone.setOnAction(e -> this.stage.close());
        btnAdd.setOnAction(e -> {
            if (!vbox.getChildren().contains(gridMaster)) {
                FocusUtils.requestFocus(cboxSubLedger);
                btnAdd.setText(resourceBundle.getString("save"));
                vbox.getChildren().add(1, gridMaster);
            } else {
                setValuesInObject();
                clearControls();
                btnAdd.setText(resourceBundle.getString("add"));
                vbox.getChildren().remove(gridMaster);
            }
        });
        txtTotalAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equalsIgnoreCase(txtAmount.getText())) {
                btnDone.setDisable(false);
            }
        });
        txtRemark.setOnAction(e -> {
            FocusUtils.requestFocus(btnAdd);
        });
        btnEdit.setOnAction(e -> {
            if (propSubLedger != null) {
                if (!vbox.getChildren().contains(gridMaster)) {
                    vbox.getChildren().add(1, gridMaster);
                    btnEdit.setText(resourceBundle.getString("update"));
                    setControls();
                } else {
                    voucherSubLedgerList.remove(propSubLedger.get());
                    total = total.subtract(propSubLedger.get().getAmount());
                    txtTotalAmount.setText(total.toString());
                    tableData.setItems(FXCollections.observableList(voucherSubLedgerList));
                    setValuesInObject();
                    vbox.getChildren().remove(gridMaster);
                    btnEdit.setText(resourceBundle.getString("edit"));
                }
            }
        });
        btnDelete.setOnAction(e -> {
            if (propSubLedger != null)
                deleteData();
        });
    }

    private void setControls() {
        cboxSubLedger.setValue(propSubLedger.get().getSubLedger());
        txtAmount1.setText(propSubLedger.get().getAmount().toString());
        txtRemark.setText(propSubLedger.get().getNarration());
    }


    @Override
    public void deleteData() {
        voucherSubLedgerList.remove(propSubLedger.get());
        total = total.subtract(propSubLedger.get().getAmount());
        txtTotalAmount.setText(total.toString());
        tableData.setItems(FXCollections.observableList(voucherSubLedgerList));
    }

    @Override
    public void clearControls() {
        txtAmount1.setText("");
        txtRemark.setText("");
        cboxSubLedger.getSelectionModel().select(0);
    }

    public void setupTable() {
        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount().toString()));
        colRemark.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNarration()));
        colSubLedger.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSubLedger().getName()));
        colSubLedgerCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSubLedger().getName()));
        propSubLedger.bind(tableData.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void setupComboBox() {
        cboxSubLedger.setConverter(new SubLedgerConvertor(cboxSubLedger));
    }

    public void setVoucherTransaction(VoucherTransaction voucherTransaction) {
        this.voucherTransaction = voucherTransaction;
        txtAmount.setText(voucherTransaction.getAmount().toString());
        txtTotalAmount.setText("0");
        txtVoucherNo.setText(voucherTransaction.getVoucher().getCode());
        txtDate.setText(voucherTransaction.getVoucher().getVoucherDate().toString());
        txtType.setText(voucherTransaction.getCreditDebit() ? resourceBundle.getString("credit") : resourceBundle.getString("debit"));
        loadSubLedger();
        if (voucherTransaction.getVoucherSubLedgers() != null && !voucherTransaction.getVoucherSubLedgers().isEmpty()) {
            tableData.setItems(FXCollections.observableList(voucherTransaction.getVoucherSubLedgers() != null ? voucherTransaction.getVoucherSubLedgers() : null));
            voucherSubLedgerList.addAll(voucherTransaction.getVoucherSubLedgers());
            for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                total = total.add(voucherSubLedger.getAmount());
                if (total.compareTo(new BigDecimal(txtAmount.getText())) > 0) {
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("amount"), resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
                    total = total.subtract(voucherSubLedger.getAmount());
                    return;
                }
                txtTotalAmount.setText(total.toString());
            }
        }
    }

    public void loadSubLedger() {
        LedgerSubLedgerDto dto = new LedgerSubLedgerDto();
        CompletableFuture<List<SubLedger>> subLedgerListFuture = CompletableFuture.supplyAsync(() -> subLedgerService.findAll());
        CompletableFuture<List<LedgerSubLedgerMapping>> ledgerSubLedgerMappingListFuture = CompletableFuture.supplyAsync(() -> ledgerService.fetchMapping(MainApp.identityDto.getSociety().getCode(), voucherTransaction.getLedger().getCode(), null));
        CompletableFuture.allOf(subLedgerListFuture, ledgerSubLedgerMappingListFuture)
                .whenCompleteAsync((result, ex) -> {
                    try {
                        if (!subLedgerListFuture.get().isEmpty())
                            dto.setSubLedgerList(subLedgerListFuture.get());

                        if (!ledgerSubLedgerMappingListFuture.get().isEmpty())
                            dto.setLedgerSubLedgerMappingList(ledgerSubLedgerMappingListFuture.get());

                        for (SubLedger sbl : dto.getSubLedgerList()) {
                            if (dto.getLedgerSubLedgerMappingList().stream()
                                    .anyMatch(p -> p.getSubLedger().getCode().equals(sbl.getCode())))
                                sbl.selectedProperty().set(true);
                        }

                        if (dto != null) {
                            cboxSubLedger.setItems(FXCollections.observableList(dto.getSubLedgerList()));
                            cboxSubLedger.getSelectionModel().select(0);
                        }
                    } catch (Exception exs) {
                        System.out.println(exs);
                        throw new RuntimeException(exs);
                    }
                });
    }


    public void setValuesInObject() {
        VoucherSubLedger voucherSubLedger = new VoucherSubLedger();
        voucherSubLedger.setVoucher(voucherTransaction.getVoucher());
        voucherSubLedger.setVoucherTransaction(voucherTransaction);
        voucherSubLedger.setNarration(txtRemark.getText());
        voucherSubLedger.setSubLedger(cboxSubLedger.getValue());
        voucherSubLedger.setAmount(new BigDecimal(txtAmount1.getText()));
//        voucherSubLedger.setCreditDebit();

        total = total.add(voucherSubLedger.getAmount());
        if (total.compareTo(new BigDecimal(txtAmount.getText())) > 0) {
            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("amount"), resourceBundle.getString("error.occurred"));
            alert1.createAlert();
            total = total.subtract(voucherSubLedger.getAmount());
            return;
        }
        txtTotalAmount.setText(total.toString());
        voucherSubLedgerList.add(voucherSubLedger);
        tableData.setItems(FXCollections.observableList(voucherSubLedgerList));
        voucherTransaction.setVoucherSubLedgers(voucherSubLedgerList);
    }
}

