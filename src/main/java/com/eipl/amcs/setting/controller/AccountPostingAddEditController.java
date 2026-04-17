package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.task.EventsLoadTask;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.setting.dto.AccountPostingDto;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.task.AccountPostingSaveTask;
import com.eipl.amcs.setting.task.AccountPostingTxnDataLoadTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AccountPostingAddEditController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    Button btnClose, btnPosting, btnSave;
    @FXML
    TableColumn<VoucherTransaction, String> colEvent, colLedgerCode, colLedgerName, colNarration, colType;
    @FXML
    TableColumn<VoucherTransaction, BigDecimal> colDebit, colCredit;
    @FXML
    TableColumn<VoucherTransaction, LocalDate> colDate;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    ComboBox<Shift> cboxToShift, cboxFromShift;
    @FXML
    ComboBox<AppConstant.PostingType> cboxPostingType;
    @FXML
    ComboBox<Events> cboxEvent;
    @FXML
    Button btnSearch;
    @FXML
    TableView<VoucherTransaction> tableAccountPosting;

    List<Voucher> voucherList = new ArrayList<>();


    List<AccountPostingDto> accountPostingDtoList = new ArrayList<>();
    AccountPosting accountPosting = null;


    private StringBuilder errorMsg = null;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        colDebit.setStyle("-fx-text-fill: red;");
        colCredit.setStyle("-fx-text-fill: green;");
        loadData();
        setupTable();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/AccountPosting.fxml")));
        });
        btnSearch.setOnAction(e -> validateAndLoad());
//
        btnPosting.setOnAction(e -> {
            validateAndSave();
            accountPosting.setStatus((short) 2);
            savePosting();
        });
        btnSave.setOnAction(e -> {
            validateAndSave();
            accountPosting.setStatus((short) 1);
            savePosting();
        });


    }

    @Override
    public void loadData() {
        cboxPostingType.getItems().addAll(AppConstant.PostingType.values());
        loadShift();
        loadEvent();

    }

    @Override
    public void setupTable() {
        colCredit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ? data.getValue().getAmount() : BigDecimal.ZERO));
        colDebit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ? BigDecimal.ZERO : data.getValue().getAmount()));
        colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getVoucher().getBillDate()));
        colLedgerCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().getCode()));
        colLedgerName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedger().getName()));
//        colLedgerName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerMappingEvent() != null ? data.getValue().getLedger().toString() : null));
        colNarration.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNarration()));
        colType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditDebit() ? resourceBundle.getString("credit") : resourceBundle.getString("debit")));
        colEvent.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEvents().getEventName()));

    }

    //    LOAD REQUIRED DATA
    private void loadShift() {
        try {
            var task = new ShiftLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Shift> list = task.get();
                    list = list.stream().filter(s -> s.getCode() != 3).collect(Collectors.toList());
                    cboxFromShift.setItems(FXCollections.observableList(list));
                    cboxToShift.setItems(FXCollections.observableList(list));
//                    if (accountPosting != null) {
//                        cboxFromShift.getSelectionModel().select(accountPosting.getFromShift());
//                        cboxToShift.getSelectionModel().select(accountPosting.getToShift());
//                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEvent() {
        try {
            var task = new EventsLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Events> list = task.get();

                    List<Events> eventList = list.stream()
                            .filter(eve -> Objects.equals(eve.getEventCode(), AppConstant.EventCode.MILK_COLLECTION)
                                    || Objects.equals(eve.getEventCode(), AppConstant.EventCode.LOCAL_MILK_SALE_CASH)
                                    || Objects.equals(eve.getEventCode(), AppConstant.EventCode.LOCAL_MILK_SALE_CREDIT)
                                    || Objects.equals(eve.getEventCode(), AppConstant.EventCode.LOCAL_MILK_SALE_COUPON))
                            .collect(Collectors.toList());
//
//                    for (Events events : eventList) {
//                        if (events.getEventName().contains("Local Milk Sale")) {
//                            events.setEventName("Local Milk Sale");
//                        }
//                    }
                    cboxEvent.setItems(FXCollections.observableList(eventList));


                    if (accountPosting != null) {
                        Events event = list.stream()
                                .filter(eve -> Objects.equals(eve.getEventCode(), accountPosting.getEventType()))
                                .findFirst()
                                .orElse(null);

                        cboxEvent.getSelectionModel().select(event);
                        validateAndLoad();
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//    ------------------------------

    //    Validation
    private void validateAndLoad() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("accountposting"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        loadTxnData();
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("accountposting"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        setValuesInObject();
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (dpFromDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("fromdatenullerror") + " \n");
        if (dpToDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("todatenullerror") + "\n");
        if (cboxFromShift.getSelectionModel().getSelectedItem() == null)
            errorMsg.append(resourceBundle.getString("fromshiftnullerror") + "\n");
        if (cboxToShift.getSelectionModel().getSelectedItem() == null)
            errorMsg.append(resourceBundle.getString("toshiftnullerror") + "\n");
        if (cboxPostingType.getSelectionModel().getSelectedItem() == null)
            errorMsg.append(resourceBundle.getString("postingtypenullerror") + "\n");

        return errorMsg.length() == 0;
    }
//    ------------------------------

    //    LOAD TXN DATA
    private void loadTxnData() {
        AccountPosting accountPosting = new AccountPosting();
        accountPosting.setFromDate(dpFromDate.getValue());
        accountPosting.setToDate(dpToDate.getValue());
        accountPosting.setFromShift(cboxFromShift.getValue());
        accountPosting.setToShift(cboxToShift.getValue());
        accountPosting.setEventType(cboxEvent.getSelectionModel().getSelectedItem().getEventCode());
        accountPosting.setPostingType(cboxPostingType.getSelectionModel().getSelectedItem().getValue());

        var task = new AccountPostingTxnDataLoadTask(accountPosting);
        task.setOnSucceeded(e -> {
            try {
                voucherList = task.get();
                if (voucherList.isEmpty())
                    return;
                List<VoucherTransaction> list =
                        voucherList.stream()
                                .flatMap(dto -> dto.getVoucherTransactions().stream())
                                .collect(Collectors.toList());
                tableAccountPosting.setItems(FXCollections.observableList(list));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    public void setTxnData(AccountPosting accountPosting) {
        try {
            this.accountPosting = accountPosting;
            dpToDate.setValue(accountPosting.getToDate());
            dpFromDate.setValue(accountPosting.getFromDate());
            cboxFromShift.setValue(accountPosting.getFromShift());
            cboxToShift.setValue(accountPosting.getToShift());
            AppConstant.PostingType type = AppConstant.PostingType.fromValue(accountPosting.getPostingType());
            cboxPostingType.setValue(type);
            if (accountPosting.getStatus() == 2) {
                btnPosting.setDisable(true);
                cboxEvent.setDisable(true);
                cboxPostingType.setDisable(true);
                cboxFromShift.setDisable(true);
                cboxToShift.setDisable(true);
                dpFromDate.setDisable(true);
                dpToDate.setDisable(true);
                btnSave.setDisable(true);
                btnSearch.setDisable(true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    ------------------------------

    //    Save Data
    private void setValuesInObject() {
        accountPosting = new AccountPosting(accountPosting != null ? accountPosting.getCode() : null, dpFromDate.getValue(), dpToDate.getValue(), cboxFromShift.getSelectionModel().getSelectedItem(),
                cboxToShift.getSelectionModel().getSelectedItem(), cboxPostingType.getSelectionModel().getSelectedItem().getValue(), cboxEvent.getSelectionModel().getSelectedItem().getEventCode());
        accountPostingDtoList.clear();
//        accountPostingDtoList.addAll(new ArrayList<>(tableAccountPosting.getItems()));
    }

    public void savePosting() {
        if (voucherList.isEmpty())
            return;
        var task = new AccountPostingSaveTask(accountPosting, voucherList);
        task.setOnSucceeded(e -> {
            try {
                AccountPosting accountPosting1 = task.get();
                String message = null;
                if (cboxEvent.getSelectionModel().getSelectedIndex() == 0)
                    message = "milk.collection.account.posting.";
                else
                    message = "local.sale.account.posting.";

                if (accountPosting1 == null) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("accountposting"), resourceBundle.getString(message.concat("fail")));
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("accountposting"),
                        resourceBundle.getString(message.concat("saved")));
                alert.createAlert();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/AccountPosting.fxml")));
        });
        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            while (exception.getCause() != null && exception.getMessage().contains("java.lang.RuntimeException")) {
                exception = exception.getCause();
            }
            String message = exception.getMessage();

            if ((message != null && (message.equalsIgnoreCase("posting.already.exists") || message.equalsIgnoreCase("one.draft.posting.exist")))) {
                message = exception.getMessage();
            } else {
                message = "milk.collection.account.posting.fail";
            }

            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("accountposting"),
                    resourceBundle.getString(message));
            alert.createAlert();
        });
        new Thread(task).start();
    }
//    ------------------------------

}