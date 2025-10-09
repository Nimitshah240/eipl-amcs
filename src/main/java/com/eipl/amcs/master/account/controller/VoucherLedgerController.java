package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.VoucherRepository;
import com.eipl.amcs.master.account.service.VoucherService;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.eipl.amcs.MainApp.context;

public class VoucherLedgerController implements MyInitialization, PopupCallback {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSubLedger;
    @FXML
    private TableView<VoucherTransaction> table1, table2;
    @FXML
    private TableColumn<VoucherTransaction, String> colCode, colName, colAmount, colType;
    @FXML
    private TableColumn<VoucherTransaction, String> colCode1, colName1, colAmount1, colType1;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private PopupCallback callback;
    private List<VoucherTransaction> listVoucherTransaction;
    private VoucherDto voucherDto;
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherLedgerController.class);
    private ObjectProperty<VoucherTransaction> propTransaction1;
    private ObjectProperty<VoucherTransaction> propTransaction2;
    private VoucherRepository repository;
    private VoucherService service;

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

    public VoucherLedgerController() {
        propTransaction1 = new SimpleObjectProperty<>();
        propTransaction2 = new SimpleObjectProperty<>();
        service = context.getBean(VoucherService.class);
        repository = context.getBean(VoucherRepository.class);
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
        try {
            Optional<Voucher> voucher = repository.findById(voucherDto.getVoucher().getCode());
            if (voucher.isPresent()) {
                List<VoucherTransaction> list = service.findAllTransaction(voucher.get());
                if (list == null || list.isEmpty())
                    return;
                listVoucherTransaction = new ArrayList<>(list);
                table1.setItems(FXCollections.observableList(listVoucherTransaction.stream().filter
                        (e1 -> !e1.getCreditDebit()).collect(Collectors.toList())));
                table2.setItems(FXCollections.observableList(listVoucherTransaction.stream().filter
                        (VoucherTransaction::getCreditDebit).collect(Collectors.toList())));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setupTable() {
        colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().toString()));
        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount().toString()));
        colType.setCellValueFactory(data -> new SimpleObjectProperty<>(resourceBundle.getString("debit")));
        propTransaction1.bind(table1.getSelectionModel().selectedItemProperty());
    }

    public void setupTable1() {
        colName1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().toString()));
        colAmount1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount().toString()));
        colType1.setCellValueFactory(data -> new SimpleObjectProperty<>(resourceBundle.getString("credit")));
        propTransaction2.bind(table2.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void reloadData(boolean flag) {
        table1.getSelectionModel().clearSelection();
        table2.getSelectionModel().clearSelection();
    }
}

