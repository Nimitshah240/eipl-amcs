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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class VoucherLedgerController implements MyInitialization, PopupCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherLedgerController.class);
    private final ObjectProperty<VoucherTransaction> propTransaction1;
    private final ObjectProperty<VoucherTransaction> propTransaction2;
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSubLedger;
    @FXML
    private TableView<VoucherTransaction> table1, table2;
    @FXML
    private TableColumn<VoucherTransaction, String> colName, colAmount, colType;
    @FXML
    private TableColumn<VoucherTransaction, String> colName1, colAmount1, colType1;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private PopupCallback callback;
    private List<VoucherTransaction> listVoucherTransaction;
    private VoucherDto voucherDto;

    public VoucherLedgerController() {
        propTransaction1 = new SimpleObjectProperty<>();
        propTransaction2 = new SimpleObjectProperty<>();
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
        setupComboBox();
        setupTable();
        setupTable1();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.
                getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Voucher.fxml"))));
        btnSubLedger.setOnAction(e -> {
            if (propTransaction1.get() != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"),
                        "VoucherSubLedgerPopup", propTransaction1.get(), this);
            } else if (propTransaction2.get() != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"),
                        "VoucherSubLedgerPopup", propTransaction2.get(), this);
            }
        });

        table2.setOnMouseClicked(e -> {
            table1.getSelectionModel().clearSelection();
        });
        table1.setOnMouseClicked(e -> {
            table2.getSelectionModel().clearSelection();
        });

    }


    @Override
    public void loadData() {
        var task = new VoucherTransactionLoadTask(voucherDto.getVoucher().getCode());
        task.setOnSucceeded(e -> {
            try {
                listVoucherTransaction = new ArrayList<>(task.get());
                table1.setItems(FXCollections.observableList(listVoucherTransaction.stream().filter
                        (e1 -> !e1.getCreditDebit()).collect(Collectors.toList())));
                table2.setItems(FXCollections.observableList(listVoucherTransaction.stream().filter
                        (VoucherTransaction::getCreditDebit).collect(Collectors.toList())));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setupTable() {
        colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().toString()));
        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount().toString()));
        colType.setCellValueFactory(data -> new SimpleObjectProperty<>(resourceBundle.getString("debit")));
        colAmount.setStyle("-fx-text-fill: red;");
        propTransaction1.bind(table1.getSelectionModel().selectedItemProperty());
    }

    public void setupTable1() {
        colName1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().toString()));
        colAmount1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount().toString()));
        colAmount1.setStyle("-fx-text-fill: green;");

        colType1.setCellValueFactory(data -> new SimpleObjectProperty<>(resourceBundle.getString("credit")));
        propTransaction2.bind(table2.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void reloadData(boolean flag) {
        table1.getSelectionModel().clearSelection();
        table2.getSelectionModel().clearSelection();
    }
}

