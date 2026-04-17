package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.account.dto.DayBookDto;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.service.VoucherService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DayBookController implements MyInitialization {

    @FXML
    private StackPane root;

    @FXML
    private DatePicker dpFromdate, dpToDate;

    @FXML
    private Button btnSearch, btnClose;

    @FXML
    private TableView<DayBookDto> tableDayBook;

    @FXML
    private TableColumn<DayBookDto, LocalDate> colVoucherdate;
    @FXML
    private TableColumn<DayBookDto, String> colVoucherNo, colVoucherType, colLedgerName, colCredit, colDebit;

    private ResourceBundle resourceBundle;
    private VoucherService voucherService;
    private ObservableList<DayBookDto> dayBookData = FXCollections.observableArrayList();

    public DayBookController() {
        voucherService = EmcsAppContext.getContext().getBean(VoucherService.class);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFromdate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());

        setupTable();

        btnSearch.setOnAction(e -> loadData());
        btnSearch.setVisible(true); // Ensure it's visible now that logic is added

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/Voucher.fxml")));
        });

//        btnExport.setOnAction(e -> exportToExcel());
    }

    @Override
    public void setupTable() {
        colVoucherdate.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getVoucherDate()));
        colVoucherdate.setCellFactory(new LocalDateCellFactory<>());

        colVoucherNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucherNo()));

        colVoucherType.setCellValueFactory(data -> {
            var type = data.getValue().getVoucherType();
            return new SimpleStringProperty(MainApp.locale.equalsIgnoreCase("en") ? type.getName() : type.getNameLocal());
        });

        colLedgerName.setCellValueFactory(data -> {
            var ledger = data.getValue().getLedger();
            return new SimpleStringProperty(MainApp.locale.equalsIgnoreCase("en") ? ledger.getName() : ledger.getNameLocal());
        });

        colCredit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreditAmount().toString()));
        colDebit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDebitAmount().toString()));

        tableDayBook.setItems(dayBookData);
    }

    @Override
    public void loadData() {
        LocalDate fromDate = dpFromdate.getValue();
        LocalDate toDate = dpToDate.getValue();

        if (toDate.isBefore(fromDate)) {
            new InformationAlert(MainApp.getStage(), resourceBundle.getString("day.book"), "To Date cannot be before From Date").createAlert();
            return;
        }

        dayBookData.clear();
        tableDayBook.setPlaceholder(new Label("Fetching Data..."));

        List<VoucherDto> vouchers = voucherService.findAllBetweenDates(fromDate, toDate);
        List<DayBookDto> flattenedData = new ArrayList<>();

        for (VoucherDto vDto : vouchers) {
            List<VoucherTransaction> transactions = voucherService.findAllTransaction(vDto.getVoucher());
            if (transactions != null) {
                for (VoucherTransaction txn : transactions) {
                    flattenedData.add(new DayBookDto(vDto.getVoucher(), txn));
                }
            }
        }

        dayBookData.addAll(flattenedData);
        if (dayBookData.isEmpty()) {
            tableDayBook.setPlaceholder(new Label("No Data Available."));
        }
    }

//    private void exportToExcel() {
//
//    }
}

