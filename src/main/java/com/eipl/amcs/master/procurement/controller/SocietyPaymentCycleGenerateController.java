package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SocietyPaymentCycleGenerateController implements MyInitialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyPaymentCycleGenerateController.class);
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate, btnGenerate;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private TextField txtInterval;
    @FXML
    private CheckBox chkCheckMonth;
    @FXML
    private TableView<SocietyPaymentCycle> tablePaymentCycle;
    @FXML
    private TableColumn<SocietyPaymentCycle, Number> colInterval;
    @FXML
    private TableColumn<SocietyPaymentCycle, LocalDate> colFromDate, colToDate;
    @FXML
    private TableColumn<SocietyPaymentCycle, Shift> colToShift, colFromShift;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private PopupCallback callback;
    private List<SocietyPaymentCycle> listPaymentCycle;

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
        this.resourceBundle = resourceBundle;

        loadData();
        setupComboBox();
        setupTable();

        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> saveData());
        btnGenerate.setOnAction(event -> {
            if (!MainApp.user.getPermissions().contains("ACTION_SOCIETY_PAYMENT_CYCLE_GENERATE"))
                throw new UnAuthorizedAccessException();
            generatePaymentCycles();
        });
        txtInterval.textProperty().addListener((observableValue, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                int val = CommonUtils.strToInteger(newVal);
                chkCheckMonth.setDisable(val % 5 != 0);
                chkCheckMonth.setDisable(cboxFromShift.getValue().getCode() == 2);
            }
        });
    }

    private void generatePaymentCycles() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (listPaymentCycle == null)
            listPaymentCycle = new ArrayList<>();
        else
            listPaymentCycle.clear();

        LocalDate fromDate = dpFromDate.getValue();
        LocalDate toDate = dpToDate.getValue();
        LocalDate toDateTemp = null;

        while (fromDate.isBefore(toDate)) {
            int days = CommonUtils.strToInteger(txtInterval.getText());
            toDateTemp = fromDate.plusDays(cboxFromShift.getValue().getCode() == 1 ? days - 1 : days);
            if (chkCheckMonth.isSelected()) {
                if (fromDate.getMonth() != toDateTemp.getMonth())
                    toDateTemp = fromDate.withDayOfMonth(fromDate.lengthOfMonth());
                if (toDateTemp.getDayOfMonth() == 30)
                    toDateTemp = fromDate.withDayOfMonth(fromDate.lengthOfMonth());
            }

            // Create payment cycle
            SocietyPaymentCycle paymentCycle = new SocietyPaymentCycle();
            paymentCycle.setFromDate(CommonUtils.getLocalDateTimeFromDateAndShift(fromDate, cboxFromShift.getValue()));
            paymentCycle.setToDate(CommonUtils.getLocalDateTimeFromDateAndShift(toDateTemp, cboxToShift.getValue()));
            paymentCycle.setFromShift(cboxFromShift.getValue());
            paymentCycle.setToShift(cboxToShift.getValue());
            paymentCycle.setSociety(MainApp.getUser().getSociety());
            paymentCycle.setIntervalValue((int) (Duration.between(paymentCycle.getFromDate(), paymentCycle.getToDate()).toDays() + 1));

            listPaymentCycle.add(paymentCycle);
            LOGGER.info("Payment cycle from {} to {}", paymentCycle.getFromDate(), paymentCycle.getToDate());
            if (cboxFromShift.getValue().getCode() == 1)
                fromDate = toDateTemp.plusDays(1);
            else
                fromDate = toDateTemp;
        }

        tablePaymentCycle.setItems(FXCollections.observableList(listPaymentCycle));
    }

    @Override
    public void setupComboBox() {
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));

        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
    }

    @Override
    public void loadData() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = new ArrayList<>(task.get());
                if (list != null || list.isEmpty()) {
                    list.removeIf(p -> p.getName().equalsIgnoreCase("all"));
                    cboxFromShift.setItems(FXCollections.observableList(list));
                    cboxToShift.setItems(FXCollections.observableList(list));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.getSelectionModel().select(list.size() - 1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setupTable() {
        try {
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate().toLocalDate()));
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate().toLocalDate()));
            colFromShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromShift()));
            colToShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToShift()));
            colInterval.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIntervalValue()));
        } catch (Exception e) {
            System.out.println("SocietyPaymentCycleGenerate setuptable Exception");
            e.printStackTrace();
        }
    }


    public void clearControls() {
        dpFromDate.setValue(null);
        dpToDate.setValue(null);
        cboxFromShift.valueProperty().set(null);
        cboxToShift.valueProperty().set(null);
    }

    private boolean validate() {
        if (dpFromDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("fromdatenullerror") + "\n");
        if (dpFromDate.getValue() != null && dpToDate.getValue() != null) {
            if (dpFromDate.getValue().isAfter(dpToDate.getValue()))
                errorMsg.append(resourceBundle.getString("fromdate.isnotvalid") + "\n");
        }
        if (cboxFromShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("fromshiftnullerror") + "\n");
        if (dpToDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("todatenullerror") + "\n");
        if (cboxToShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("toshiftnullerror") + "\n");
        if (txtInterval.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("intervalnullerror") + "\n");
        if (!CommonUtils.isNumeric(txtInterval.getText()))
            errorMsg.append(resourceBundle.getString("interval.isnotnumeric") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new SocietyPaymentCycleSaveTask(listPaymentCycle, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof RuntimeException) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                        resourceBundle.getString("societypaymentcycle.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}

