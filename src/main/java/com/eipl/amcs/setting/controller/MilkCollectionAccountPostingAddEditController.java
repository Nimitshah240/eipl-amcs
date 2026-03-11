package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.setting.dto.MilkCollectionAccountPostingDto;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;
import com.eipl.amcs.setting.task.MilkCollectionAccountPostingSaveTask;
import com.eipl.amcs.setting.task.MilkCollectionLedgerMappingEventLoadTask;
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
import java.util.ResourceBundle;

public class MilkCollectionAccountPostingAddEditController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    Button btnClose, btnPosting;
    @FXML
    TableColumn<MilkCollectionAccountPostingDto, String> colNarration, colLedger, colNarration1, colLedger1;
    @FXML
    TableColumn<MilkCollectionAccountPostingDto, BigDecimal> colAmount1, colAmount;
    @FXML
    TableColumn<MilkCollectionAccountPostingDto, LocalDate> colDate, colDate1;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    ComboBox<Shift> cboxToShift, cboxFromShift;
    @FXML
    ComboBox<String> cboxPostingType;
    @FXML
    Button btnSearch;
    @FXML
    TableView<MilkCollectionAccountPostingDto> tableCredit, tableDebit;


    List<MilkCollectionAccountPostingDto> creditMilkCollectionAccountPostingDtoList = new ArrayList<>();
    List<MilkCollectionAccountPostingDto> debitMilkCollectionAccountPostingDtoList = new ArrayList<>();
    MilkCollectionAccountPosting milkCollectionAccountPosting = null;


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
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/MilkCollectionAccountPosting.fxml")));
        });
        btnSearch.setOnAction(e -> loadMilkCollectionLedgerMapping());

        btnPosting.setOnAction(e -> {
            validateAndSave();
        });
    }

    @Override
    public void loadData() {
        cboxPostingType.getItems().addAll("Consolidate", "Day");
        loadShift();
    }

    @Override
    public void setupTable() {
        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
        colAmount1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
        colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
        colDate1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
        colLedger.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerMappingEvent().getCreditLedger().toString()));
        colLedger1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerMappingEvent().getDebitLedger().toString()));
    }

    public void saveData() {
        var task = new MilkCollectionAccountPostingSaveTask(milkCollectionAccountPosting, creditMilkCollectionAccountPostingDtoList, debitMilkCollectionAccountPostingDtoList);
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionaccountposting"),
                    resourceBundle.getString("milk.collection.account.posting.saved"));
            alert.createAlert();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/MilkCollectionAccountPosting.fxml")));
        });
        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            while (exception.getCause() != null && exception.getMessage().contains("java.lang.RuntimeException")) {
                exception = exception.getCause();
            }
            String message = exception.getMessage();
            if (!message.equalsIgnoreCase("posting.already.exists")) {
                message = "milk.collection.account.posting.fail";
            }

            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionaccountposting"),
                    resourceBundle.getString(message));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionaccountposting"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        setValuesInObject();
        saveData();
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
        milkCollectionAccountPosting = new MilkCollectionAccountPosting();
        milkCollectionAccountPosting.setFromDate(dpFromDate.getValue());
        milkCollectionAccountPosting.setToDate(dpToDate.getValue());
        milkCollectionAccountPosting.setFromShift(cboxFromShift.getSelectionModel().getSelectedItem().getCode());
        milkCollectionAccountPosting.setToShift(cboxToShift.getSelectionModel().getSelectedItem().getCode());
        milkCollectionAccountPosting.setPostingType(cboxPostingType.getSelectionModel().getSelectedIndex() + 1);
        creditMilkCollectionAccountPostingDtoList.clear();
        debitMilkCollectionAccountPostingDtoList.clear();
        creditMilkCollectionAccountPostingDtoList.addAll(new ArrayList<>(tableCredit.getItems()));
        debitMilkCollectionAccountPostingDtoList.addAll(new ArrayList<>(tableDebit.getItems()));
    }

    private void loadShift() {
        try {
            var task = new ShiftLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Shift> list = task.get();
                    cboxFromShift.setItems(FXCollections.observableList(list));
                    cboxToShift.setItems(FXCollections.observableList(list));
                    if (milkCollectionAccountPosting != null) {
                        cboxFromShift.getSelectionModel().select(milkCollectionAccountPosting.getFromShift() - 1);
                        cboxToShift.getSelectionModel().select(milkCollectionAccountPosting.getToShift() - 1);
                        loadMilkCollectionLedgerMapping();
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

    private void loadMilkCollectionLedgerMapping() {
        var task = new MilkCollectionLedgerMappingEventLoadTask(cboxPostingType.getSelectionModel().getSelectedIndex() + 1, CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getSelectionModel().getSelectedItem()), CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getSelectionModel().getSelectedItem()));
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollectionAccountPostingDto> list = task.get();
                if (list != null || !list.isEmpty()) {
                    tableCredit.setItems(FXCollections.observableList(list));
                    tableDebit.setItems(FXCollections.observableList(list));
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    public void setMilkCollectionAccountPostingDto(MilkCollectionAccountPosting milkCollectionAccountPosting) {
        this.milkCollectionAccountPosting = milkCollectionAccountPosting;
        cboxPostingType.getSelectionModel().select(milkCollectionAccountPosting.getPostingType() - 1);
        dpFromDate.setValue(milkCollectionAccountPosting.getFromDate());
        dpToDate.setValue(milkCollectionAccountPosting.getToDate());
        btnPosting.setDisable(true);
    }
}