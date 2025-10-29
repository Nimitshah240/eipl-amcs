package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.task.MemberMilkPurchaseRateLoadTask;
import com.eipl.amcs.operation.procurement.convertor.MemberMilkPurchaseRateConvertor;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.*;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class RateRecalculateController implements MyInitialization, PopupCallback {
    protected final int SCALE = 2;
    protected final RoundingMode ROUND = RoundingMode.HALF_UP;
    protected Map<String, BigDecimal> mapRateDetails;
    protected MemberMilkPurchaseRate memberMilkPurchaseRate;
    protected MilkCollectionPreReqDto collectionPreReqDto;
    protected List<MemberMilkPurchaseRateBased> memberRateBasedList;
    @FXML
    StackPane root;
    @FXML
    TableView<MilkCollection> tableCollection;
    @FXML
    TableColumn<MilkCollection, Number> colSampleNo, colMemberCode, colQty, colFat, colSnf, colRate, colAmount, colNewRate, colNewAmount;
    @FXML
    TableColumn<MilkCollection, MilkType> colMilkType;
    @FXML
    TableColumn<MilkCollection, Shift> colShift;
    @FXML
    TableColumn<MilkCollection, LocalDate> colDate;
    @FXML
    TableColumn<MilkCollection, String> colMemberName;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    Button btnSearch, btnRecalculate, btnSubmit, btnClose;
    int index = 0;
    String mapKey = null;
    @FXML
    private ComboBox<MemberMilkPurchaseRate> cboxRateChart;
    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<MilkType> milkTypeList;
    private List<Member> memberList;
    private List<MilkCollection> listMilkCollection;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        setupTable();
        loadShift();
        setupComboBox();
        btnSearch.setOnAction(e -> loadData());
        btnSubmit.setOnAction(e -> {
            changeData();
        });
        btnRecalculate.setOnAction(e -> {
            fetchRateDetails();

        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

    }

    private void changeData() {
        for (MilkCollection milkCollection : listMilkCollection) {
            milkCollection.setRtpl(milkCollection.getNewRate());
            milkCollection.setAmount(milkCollection.getNewAmount());
        }
        startImportProcess();
    }

    private void calculateData() {
        for (MilkCollection milkCollection : listMilkCollection) {
            milkCollection.setNewRate(new BigDecimal(fetchRate(milkCollection.getFat().toString(), milkCollection.getSnf().toString(),
                    milkCollection.getMilkType(), milkCollection.getMilkQualityType())));
            milkCollection.setNewAmount(new BigDecimal(calculateAmount(milkCollection.getNewRate().toString(), milkCollection.getQty().toString())));
        }
        tableCollection.setItems(FXCollections.observableList(listMilkCollection));
        setupTable();
        tableCollection.refresh();
    }

    protected String calculateAmount(String rate, String qty) {
        mapKey = null;
        return new BigDecimal(rate).multiply(new BigDecimal(qty)).setScale(SCALE, ROUND).toString();
    }

    protected String fetchRate(String fat, String snf, MilkType milkType, MilkQualityType milkQualityType) {
//        if (!fat.isEmpty() && !snf.isEmpty() && milkType != null && milkQualityType != null) {
        BigDecimal snfVal = null;
        if (memberMilkPurchaseRate.getRateType().getCode().intValue() == 1) {
            snfVal = new BigDecimal(0).setScale(SCALE, ROUND);
        } else if (memberMilkPurchaseRate.getRateType().getCode().intValue() == 2) {
            snfVal = new BigDecimal(snf).setScale(SCALE, ROUND);
        }
        if (milkQualityType != null) {
            mapKey = new BigDecimal(fat).setScale(SCALE, ROUND) + "#" +
                    snfVal + "#" +
                    milkType.getCode() + "#" + milkQualityType.getCode();
        } else {
            mapKey = new BigDecimal(fat).setScale(SCALE, ROUND) + "#" +
                    snfVal + "#" +
                    milkType.getCode() + "#" + 1;

        }
        BigDecimal val = mapRateDetails.get(mapKey);


        return (val != null ? val.setScale(SCALE, ROUND).toString() : "0");
    }

    protected void fetchRateDetails() {
        if (cboxRateChart.getValue() == null)
            return;
        fetchRateBased();
        var task = new MilkRateAndDetailsLoadTask(cboxRateChart.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                MilkRateAndDetailsDto dto = task.get();
                if (dto != null) {
                    memberMilkPurchaseRate = dto.getMemberPurchaseRate();
                    mapRateDetails = dto.getDetails();
                    calculateData();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchRateBased() {
        if (cboxRateChart.getValue() == null)
            return;
        var task = new MilkRateBasedLoadTask(cboxRateChart.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                memberRateBasedList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void startImport(File file) {
        var task = new MilkCollectionImportTask(file, milkTypeList, shiftList, memberList);
        task.setOnSucceeded(e -> {
            try {
                listMilkCollection = task.get();
                if (listMilkCollection == null || listMilkCollection.isEmpty()) {
                    MainApp.paneDrop.setVisible(false);
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                            resourceBundle.getString("invalid.file"));
                    alert.createAlert();
                    return;
                }
                MainApp.lblMessage.setText("Importing Milk Collections...");
                startImportProcess();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void startImportProcess() {
        MainApp.paneDrop.setVisible(true);
        MainApp.lblMessage.setText("ReCalculating Milk Collections...");
        var task = new MilkCollectionListSaveTask(listMilkCollection);
        task.setOnSucceeded(e -> {
            try {
                MainApp.paneDrop.setVisible(false);
                List<CollectionImportDto> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
                String builder = "Import success: " +
                        list.stream().filter(p -> p.getStatus().equalsIgnoreCase("success")).count() +
                        "\n" +
                        "Import fail: " +
                        list.stream().filter(p -> p.getStatus().equalsIgnoreCase("error")).count() +
                        "\n";


                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                        builder);
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void loadImportPreReq() {

        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                milkTypeList = task1.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                shiftList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task2 = new MemberLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                memberList = task2.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    cboxFromShift.setItems(FXCollections.observableList(list1));
                    cboxToShift.setItems(FXCollections.observableList(list1));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.getSelectionModel().select(list1.size() - 1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task3 = new MemberMilkPurchaseRateLoadTask();
        task3.setOnSucceeded(e -> {
            try {
                List<MemberMilkPurchaseRate> list = task3.get();
                if (list != null) {
                    cboxRateChart.setItems(FXCollections.observableList(list));
                    cboxRateChart.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task3).start();
    }

    @Override
    public void loadData() {
        tableCollection.setPlaceholder(new Label("Loading data..."));
        var task = new MilkCollectionLoadTask(
                CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()),
                CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()), 0);
        task.setOnSucceeded(e -> {
            try {
                listMilkCollection = task.get();
                if (listMilkCollection == null) {
                    tableCollection.setPlaceholder(new Label("No data..."));
                    return;
                }

                tableCollection.setItems(FXCollections.observableList(listMilkCollection));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxRateChart.setConverter(new MemberMilkPurchaseRateConvertor(cboxRateChart));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });


    }

    @Override
    public void setupTable() {
        try {
            colSampleNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSampleNo()));
            colMemberCode.setCellValueFactory(data -> new SimpleIntegerProperty(CommonUtils.strToInteger(data.getValue().getMember().getCodeEx())));
            colQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
            colFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFat()));
            colSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSnf()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRtpl()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCollectionDate().toLocalDate()));
            colDate.setCellFactory(new LocalDateCellFactory<>());
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShift()));
            colNewRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNewRate()));
            colNewAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNewAmount()));
        } catch (Exception e) {
            System.out.println("RateRecalculate setuptable Exception");
            e.printStackTrace();
        }
    }
}
