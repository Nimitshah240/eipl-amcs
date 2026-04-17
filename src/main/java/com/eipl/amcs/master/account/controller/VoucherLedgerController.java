package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.task.VoucherTransactionLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class VoucherLedgerController implements MyInitialization, PopupCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherLedgerController.class);
    private final ObjectProperty<VoucherTransaction> propTransaction;

    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSubLedger;
    @FXML
    private TableView<VoucherTransaction> tblVoucherLedger;
    @FXML
    private TableColumn<VoucherTransaction, String> colName, colType, colLedgerName, colLedgerCode, colNarration;
    @FXML
    private TableColumn<VoucherTransaction, BigDecimal> colAmountCredit, colAmountDebit;

    private Stage stage;
    private ResourceBundle resourceBundle;
    private PopupCallback callback;
    private List<VoucherTransaction> listVoucherTransaction;
    private VoucherDto voucherDto;

    public VoucherLedgerController() {
        propTransaction = new SimpleObjectProperty<>();
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

    public void setVoucher(VoucherDto voucherDto) {
        this.voucherDto = voucherDto;
        loadData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        colAmountDebit.setStyle("-fx-text-fill: red;");
        colAmountCredit.setStyle("-fx-text-fill: green;");
        setupTable();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.
                getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Voucher.fxml"))));
        btnSubLedger.setOnAction(e -> {
            if (propTransaction.get() != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"),
                        "VoucherSubLedgerPopup", propTransaction.get(), this);
            }
        });
    }

    @Override
    public void loadData() {
        var task = new VoucherTransactionLoadTask(voucherDto.getVoucher().getCode());
        task.setOnSucceeded(e -> {
            try {
                listVoucherTransaction = new ArrayList<>(task.get());
                tblVoucherLedger.setItems(FXCollections.observableList(listVoucherTransaction));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        colAmountCredit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ? data.getValue().getAmount() : BigDecimal.ZERO));
        colAmountDebit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ? BigDecimal.ZERO : data.getValue().getAmount()));
        colLedgerCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().getCode()));
        colLedgerName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().getName()));
//        colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().toString()));
        colNarration.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNarration()));
        colType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ? resourceBundle.getString("credit") : resourceBundle.getString("debit")));

        propTransaction.bind(tblVoucherLedger.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void reloadData(boolean flag) {
        tblVoucherLedger.getSelectionModel().clearSelection();
    }
}
