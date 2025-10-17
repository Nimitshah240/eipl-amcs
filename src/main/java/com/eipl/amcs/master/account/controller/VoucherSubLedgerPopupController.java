package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.task.VoucherSubLedgerLoadTask;
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

public class VoucherSubLedgerPopupController implements MyInitialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherSubLedgerPopupController.class);
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose;
    @FXML
    private TableView<VoucherSubLedger> tableData;
    @FXML
    private TableColumn<VoucherSubLedger, String> colCode, colSubLedger, colAmount;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private final StringBuilder errorMsg = null;
    private PopupCallback callback;
    private List<VoucherSubLedger> listVoucheSubLedger;
    private VoucherTransaction voucherTransaction;

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

    public void setVoucherTransaction(VoucherTransaction voucherTransaction) {
        this.voucherTransaction = voucherTransaction;
        loadData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            this.callback.reloadData(true);
            this.stage.close();
        });
    }


    @Override
    public void loadData() {
        if (voucherTransaction != null) {
            var task = new VoucherSubLedgerLoadTask(voucherTransaction.getCode());
            task.setOnSucceeded(e -> {
                try {
                    listVoucheSubLedger = new ArrayList<>();
                    listVoucheSubLedger = task.get();
                    if (listVoucheSubLedger != null && !listVoucheSubLedger.isEmpty()) {
                        tableData.setItems(FXCollections.observableList(listVoucheSubLedger));
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    public void setupTable() {
        colSubLedger.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSubLedger().getName()));
        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount().toString()));
        colCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSubLedger().getCode()));
    }


}

