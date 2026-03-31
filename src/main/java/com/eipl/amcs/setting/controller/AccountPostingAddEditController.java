package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.task.EventsLoadTask;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.setting.dto.AccountPostingDto;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.task.AccountPostingLedgerMappingEventLoadTask;
import com.eipl.amcs.setting.task.AccountPostingSaveTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
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
    TableColumn<AccountPostingDto, String> colEvent, colLedgerCode, colLedgerName, colNarration, colType;
    @FXML
    TableColumn<AccountPostingDto, BigDecimal> colDebit, colCredit;
    @FXML
    TableColumn<AccountPostingDto, LocalDate> colDate;
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
    TableView<AccountPostingDto> tableAccountPosting;


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
        loadData();
        setupTable();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/AccountPosting.fxml")));
        });
        btnSearch.setOnAction(e -> loadAccountPostingLedgerMapping());
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
        colCredit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreditAmount()));
        colDebit.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDebitAmount()));
        colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
        colLedgerCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerMappingEvent() != null ? data.getValue().getLedger().getCode() : null));
        colLedgerName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerMappingEvent() != null ? data.getValue().getLedger().toString() : null));
        colNarration.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNarration()));
        colType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().isCredit_debit() ? resourceBundle.getString("credit") : resourceBundle.getString("debit")));
        colEvent.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerMappingEvent().getEvents().getEventName()));

    }

    public void savePosting() {
        var task = new AccountPostingSaveTask(accountPosting, accountPostingDtoList);
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

//            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/AccountPosting.fxml")));
        });
        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            while (exception.getCause() != null && exception.getMessage().contains("java.lang.RuntimeException")) {
                exception = exception.getCause();
            }
            String message = exception.getMessage();
            if (message == null || !message.equalsIgnoreCase("posting.already.exists")) {
                message = "milk.collection.account.posting.fail";
            }

            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("accountposting"),
                    resourceBundle.getString(message));
            alert.createAlert();
        });
        new Thread(task).start();
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

    private void setValuesInObject() {
        accountPosting = new AccountPosting(accountPosting != null ? accountPosting.getCode() : null, dpFromDate.getValue(), dpToDate.getValue(), cboxFromShift.getSelectionModel().getSelectedItem(),
                cboxToShift.getSelectionModel().getSelectedItem(), cboxPostingType.getSelectionModel().getSelectedItem().getValue(), cboxEvent.getSelectionModel().getSelectedItem().getEventCode());
        accountPostingDtoList.clear();
        accountPostingDtoList.addAll(new ArrayList<>(tableAccountPosting.getItems()));
    }

    private void loadShift() {
        try {
            var task = new ShiftLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Shift> list = task.get();
                    cboxFromShift.setItems(FXCollections.observableList(list));
                    cboxToShift.setItems(FXCollections.observableList(list));
                    if (accountPosting != null) {
                        cboxFromShift.getSelectionModel().select(accountPosting.getFromShift());
                        cboxToShift.getSelectionModel().select(accountPosting.getToShift());
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

    private void loadEvent() {
        try {
            var task = new EventsLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Events> list = task.get();

                    List<Events> eventList = list.stream()
                            .filter(eve -> Objects.equals(eve.getEventCode(), AppConstant.EventCode.MILK_COLLECTION)
                                    || Objects.equals(eve.getEventCode(), AppConstant.EventCode.LOCAL_MILK_SALE))
                            .collect(Collectors.toList());

                    cboxEvent.setItems(FXCollections.observableList(eventList));


                    if (accountPosting != null) {
                        Events event = list.stream()
                                .filter(eve -> Objects.equals(eve.getEventCode(), accountPosting.getEventType()))
                                .findFirst()
                                .orElse(null);

                        cboxEvent.getSelectionModel().select(event);
                        loadAccountPostingLedgerMapping();
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

    private void loadAccountPostingLedgerMapping() {
        var task = new AccountPostingLedgerMappingEventLoadTask(cboxPostingType.getSelectionModel().getSelectedItem().getValue(), CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getSelectionModel().getSelectedItem()), CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getSelectionModel().getSelectedItem()), cboxEvent.getSelectionModel().getSelectedIndex());
        task.setOnSucceeded(e -> {
            try {
                List<AccountPostingDto> list = task.get();
                if (list != null && !list.isEmpty()) {
                    tableAccountPosting.setItems(FXCollections.observableList(list));
                } else
                    tableAccountPosting.setItems(null);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    public void setAccountPostingDto(AccountPosting accountPosting) {
        this.accountPosting = accountPosting;
        AppConstant.PostingType type = AppConstant.PostingType.fromValue(accountPosting.getPostingType());
        cboxPostingType.setValue(type);
        dpFromDate.setValue(accountPosting.getFromDate());
        dpToDate.setValue(accountPosting.getToDate());
        cboxToShift.getSelectionModel().select(accountPosting.getToShift());
        cboxFromShift.getSelectionModel().select(accountPosting.getFromShift());
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
    }
}