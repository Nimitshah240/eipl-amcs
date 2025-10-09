package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;

import static com.eipl.amcs.MainApp.context;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.cellfactory.RightAlignCellFactory;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.report.dto.LedgerClose;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.NumberUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubLedgerOpeningBalanceViewController implements MyInitialization {

    private ResourceBundle resourceBundle;
    @FXML
    private VBox vbox;

    @FXML
    private TableColumn<LedgerClose, String> colName, colSubLedgerCode, colCreditDebit;
    @FXML
    private TableColumn<LedgerClose, Number> colBalance;

    @FXML
    Button btnClose;
    @FXML
    private AnchorPane root;
    private Stage stage;
    @FXML
    private TableView<LedgerClose> tableSubLedger;
    private ObjectProperty<LedgerClose> propObjSubLedger;
    private PopupCallback callback;
    @FXML
    private Label lblTotalBalance;
    private LedgerRepository ledgerRepository;


    public SubLedgerOpeningBalanceViewController() {
        ledgerRepository = context.getBean(LedgerRepository.class);
        propObjSubLedger = new SimpleObjectProperty<>();
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
    public void initialize(URL location, ResourceBundle resourceBundle) {

        btnClose.setOnAction(e -> this.stage.close());
        this.resourceBundle = resourceBundle;
        setupTable();
    }

    public void setLedger(LedgerClose ledger) {
        loadReview(ledger);
    }

    public void loadReview(LedgerClose ledger) {
        try {
            List<LedgerClose> list = new ArrayList<>();
            List<Object[]> listLedger = ledgerRepository.fetchSubLedgerOpeningBalance(ledger.getLedgerCode(), CommonUtils.convertToSqlDate(MainApp.getFinancialYear().getStartDate()), CommonUtils.convertToSqlDate(MainApp.getFinancialYear().getEndDate()), MainApp.locale);
            if (list != null && !list.isEmpty()) {
                for (Object[] arr : listLedger) {
                    LedgerClose bal = new LedgerClose((String) arr[0], (String) arr[1], (((BigDecimal) arr[2]).doubleValue() < 0 ? false : true), ((BigDecimal) arr[2]).doubleValue());
                    list.add(bal);
                }
            }
            tableSubLedger.setItems(FXCollections.observableArrayList(list));
            lblTotalBalance.setText(NumberUtil.twoDecimal(list.stream().mapToDouble(m -> m.getBalance()).sum()));
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @Override
    public void setupTable() {
        // Review
        colSubLedgerCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerCode()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLedgerName()));
        colCreditDebit.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isCreditDebit() ? MainApp.getBundle().getString("credit") : MainApp.getBundle().getString("debit")));
        colCreditDebit.setCellFactory(cell -> {
            return new TableCell<LedgerClose, String>() {

                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setText(null);
                    else {
                        if (item.equals(MainApp.getBundle().getString("credit"))) {
                            setStyle("-fx-text-fill: #2E7D32");
                        } else {
                            setStyle("-fx-text-fill: #D32F2F");
                        }
                        setText(item);
                    }
                }
            };
        });
        colBalance.setCellValueFactory(data -> new SimpleDoubleProperty(Math.abs(data.getValue().getBalance())));
        colBalance.setCellFactory(new RightAlignCellFactory<>());
        propObjSubLedger.bind(tableSubLedger.getSelectionModel().selectedItemProperty());
    }
}
