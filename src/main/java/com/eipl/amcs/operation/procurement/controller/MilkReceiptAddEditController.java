package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.procurement.convertor.MilkDispatchDateWiseConvertor;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchSummaryDto;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptDto;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptSummaryDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import com.eipl.amcs.operation.procurement.task.*;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MilkReceiptAddEditController extends MilkDispatchBaseController implements MyInitialization {

    List<MilkReceiptSummaryDto> milkReceiptSummaryDtoList = new ArrayList<>();
    List<MilkQualityType> milkQualityTypeList = new ArrayList<>();
    List<MilkDispatchTransaction> milkDispatchSummaryDtoList = new ArrayList<>();
    @FXML
    private StackPane root;
    @FXML
    private GridPane gridMaster;
    @FXML
    private E_TextField txtQuanity, txtFat, txtSnf, txtClr,
            txtWater, txtRtpl, txtAmount, txtChamberNo, txtCans;
    @FXML
    private E_DatePicker dpFromDate, dpToDate, dpReceiptDate;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private TableView<MilkReceiptSummaryDto> tableMilkReceiptSummary;
    @FXML
    private TableColumn<MilkReceiptSummaryDto, MilkType> colCollecMilkType;
    @FXML
    private TableColumn<MilkReceiptSummaryDto, BigDecimal> colPurchaseMilk, colLocalMilkSale, colDifference;
    @FXML
    private GridPane gridTransaction;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private ComboBox<MilkDispatch> cboxChallanNo;
    @FXML
    private ComboBox<MilkQualityType> cboxMilkQuality;
    @FXML
    private Label lblQuantity;
    @FXML
    private HBox btnPanel;
    @FXML
    private Button btnAdd, btnDelete;
    @FXML
    private TableView<MilkReceiptTransaction> tableMilkReceipt;
    @FXML
    private TableView<MilkDispatchTransaction> tableMilkDispatch;
    @FXML
    private TableColumn<MilkReceiptTransaction, MilkType> colMilkType;
    @FXML
    private TableColumn<MilkReceiptTransaction, String> colMilkQltyType;
    @FXML
    private TableColumn<MilkDispatchTransaction, MilkType> colMilkType1;
    @FXML
    private TableColumn<MilkReceiptTransaction, BigDecimal> colQuantity, colQuantity2, colFat, colSnf, colRate, colAmount;
    @FXML
    private TableColumn<MilkDispatchTransaction, BigDecimal> colQuantity1, colFat1, colSnf1, colRate1, colAmount1, colWater1;
    @FXML
    private HBox btnMainPanel;
    private ObjectProperty<MilkDispatchSummaryDto> propDto;
    //private ObjectProperty<MilkReceiptSummaryDto> propDto;
    @FXML
    private E_Button btnSaveUpdate, btnClose;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private MilkReceipt dto;
    private MilkReceiptTransaction dtoTxn;
    private MilkReceiptDto receiptDto;
    private final ObjectProperty<MilkReceiptTransaction> propMilkReceiptTransaction;
    private StringBuilder errorMsg = null;
    private ObservableList<MilkReceiptTransaction> listMilkReceipt;
    private final ObservableList<MilkReceiptTransaction> listDeleteTxn;
    private String challanNo;
    private final ObjectProperty<MilkDispatchTransaction> propMilkDispatchTransaction;
    private final ChangeListener<String> qtyRateChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            calculateAmount(txtRtpl.getText(), txtQuanity.getText());
        }
    };
    private final ChangeListener<String> qualityParamChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            fetchRateForReceipt(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue(), CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            calculateClr(txtFat.getText(), txtSnf.getText());
        }
    };

    public MilkReceiptAddEditController() {
        propMilkReceiptTransaction = new SimpleObjectProperty<>();
        listMilkReceipt = FXCollections.observableArrayList();
        listDeleteTxn = FXCollections.observableArrayList();
        propMilkDispatchTransaction = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setMilkReceipt(MilkReceipt milkReceipt) {
        if (milkReceipt != null) {
            if (milkReceipt.getMilkDispatch() == null) {
                btnAdd.setDisable(true);
                btnSaveUpdate.setDisable(true);
                btnDelete.setDisable(true);
                btnAdd.setDisable(true);
                this.dto = milkReceipt;
                loadReceiptDetail(milkReceipt.getCode());
                tableMilkReceipt.setDisable(true);
                tableMilkDispatch.setDisable(true);
                btnPanel.setDisable(true);
                cboxChallanNo.setDisable(true);
            } else {
                this.dto = milkReceipt;
                loadMilkDispatch2();
                setupComboBox();
                btnSaveUpdate.setText(resourceBundle.getString("update"));
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        gridTransaction.setDisable(true);
        dpReceiptDate.setValue(LocalDate.now());
        dpReceiptDate.setDisable(true);
        loadData();
        setupComboBox();
        setupTable();
        setupCollectionTable();
        loadMilkDispatch();
        txtFat.textProperty().addListener(qualityParamChangeListener);
        txtSnf.textProperty().addListener(qualityParamChangeListener);
        txtQuanity.textProperty().addListener(qtyRateChangeListener);
        txtRtpl.textProperty().addListener(qtyRateChangeListener);
        cboxChallanNo.setOnAction(e -> {
            loadMilkDispatchValue();
            loadMilkDispatchSummary(cboxChallanNo.getValue().getChallanNo());
        });
        cboxMilkType.setOnAction(e -> {
            fetchRateForReceipt(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue(), CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            calculateClr(txtFat.getText(), txtSnf.getText());
        });
        cboxMilkQuality.setOnAction(e -> {
            fetchRateForReceipt(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue(), CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            calculateClr(txtFat.getText(), txtSnf.getText());
        });
//        cboxChallanNo.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
//            loadMilkDispatchValue();
//            loadMilkDispatchSummary(newValue.getSelectedItem().getChallanNo());
//        });
        txtChamberNo.setOnAction(e -> {
            FocusUtils.requestFocus(btnAdd);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceipt.fxml")));
        });
        btnAdd.setOnAction(event -> {
            FocusUtils.requestFocus(cboxMilkType);
            if (btnAdd.getText().equals(resourceBundle.getString("add"))) {
//                fetchPurchaseRateCode();
                gridTransaction.setDisable(false);
                btnAdd.setText(resourceBundle.getString("save"));
                btnDelete.setDisable(false);
                btnDelete.setText(resourceBundle.getString("cancel"));
            } else {
                addEntry();
            }
        });
        btnSaveUpdate.setOnAction(e -> {
            validateAndSave();
        });
        btnDelete.setOnAction(e -> {
            if (btnDelete.getText().equals(resourceBundle.getString("cancel"))) {
                if (btnAdd.getText().equals(resourceBundle.getString("save"))) {
                    gridMaster.setDisable(false);
                    gridTransaction.setDisable(true);
                    btnAdd.setText(resourceBundle.getString("add"));
                }
                clearInnerControls();
            } else {
                if (propMilkReceiptTransaction.get() != null) {
//                    deleteTransaction(propMilkReceiptTransaction.get());
                    MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                            resourceBundle.getString("alert.delete"));
                    Optional<ButtonType> resp = alert.createConfirmationAlert();
                    if (resp.isPresent() && resp.get() == ButtonType.OK) {
                        listDeleteTxn.add(propMilkReceiptTransaction.get());
                        listMilkReceipt.remove(propMilkReceiptTransaction.get());
                    }
                }
            }

        });
        propMilkReceiptTransaction.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                btnAdd.setDisable(false);
                btnDelete.setText(resourceBundle.getString("cancel"));
                // btnDelete.setDisable(true);
            } else {
                btnAdd.setText(resourceBundle.getString("add"));
//                btnAdd.setDisable(true);
                btnDelete.setText(resourceBundle.getString("delete"));
                btnDelete.setDisable(false);
//                gridMaster.setDisable(true);
            }
        });
        txtWater.setText("0");
        txtCans.setText("1");
        txtChamberNo.setText("0");

    }


    private void loadMilkDispatch() {
        var task = new MilkDispatchLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkDispatch> list = task.get();
                if (list != null) {
                    cboxChallanNo.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

    }

    private void loadMilkDispatch2() {
        try {
            var task = new MilkDispatchByChallaNoLoadTask(dto.getMilkDispatch().getChallanNo());
            task.setOnSucceeded(e -> {
                try {
                    MilkDispatch list = task.get();
                    if (list != null) {
                        dto.setMilkDispatch(list);
                        loadReceiptDetail(dto.getCode());
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadMilkDispatchSummary(String challanNo) {

        var task = new MilkDispatchTransactionLoadTask(challanNo);
        task.setOnSucceeded(e -> {
            try {
                milkDispatchSummaryDtoList = task.get();
                if (milkDispatchSummaryDtoList != null)
                    tableMilkDispatch.setItems(FXCollections.observableList(milkDispatchSummaryDtoList));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMilkDispatchValue() {
        if (cboxChallanNo.getValue() == null)
            return;
        var task = new MilkDispatchByChallaNoLoadTask(cboxChallanNo.getValue().getChallanNo());
        task.setOnSucceeded(e -> {
            try {
                MilkDispatch milkDispatch = task.get();
                dpFromDate.setValue(milkDispatch.getFromDate().toLocalDate());
                dpToDate.setValue(milkDispatch.getToDate().toLocalDate());
                cboxFromShift.setValue(milkDispatch.getFromShift());
                cboxToShift.setValue(milkDispatch.getToShift());
                fetchPurchaseRateCode();

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }

        });

        new Thread(task).start();
    }

    @Override
    public void loadData() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    cboxFromShift.setItems(FXCollections.observableList(list1));
                    cboxToShift.setItems(FXCollections.observableList(list1));

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                    cboxMilkType.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task2 = new MilkQualityTypeLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                milkQualityTypeList = task2.get();
                if (milkQualityTypeList != null) {
                    cboxMilkQuality.setItems(FXCollections.observableList(milkQualityTypeList));
                    cboxMilkQuality.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();


    }

    @Override
    public void setupComboBox() {
        cboxChallanNo.setConverter(new MilkDispatchDateWiseConvertor(cboxChallanNo));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkQuality.setConverter(new MilkQualityConvertor(cboxMilkQuality));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        dpReceiptDate.setConverter(new LocalDateConvertor());
        dpReceiptDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpReceiptDate.setValue(dpReceiptDate.getConverter().fromString(dpReceiptDate.getEditor().getText()));
            }
        });
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
    public void setupTable() {
        try {
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colQuantity.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
            colQuantity2.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getConvertedQuantity()));
            colFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgFat()));
            colSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgSnf()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colMilkQltyType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQualityType().getName()));

            propMilkReceiptTransaction.bind(tableMilkReceipt.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupCollectionTable() {
        colMilkType1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
        colQuantity1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
        colFat1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgFat()));
        colSnf1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgSnf()));
        colRate1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
        colAmount1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));

        colWater1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWater()));
//        colLocalMilkSale.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkSale()));
//        colDifference.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkBalance()));

        propMilkDispatchTransaction.bind(tableMilkDispatch.getSelectionModel().selectedItemProperty());

    }

    private void validateAndSave() {
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        setValuesInObjectUpdate();

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            updateData();
        } else {
            saveData();
        }
    }

    private void setValuesInObjectUpdate() {
        if (this.dto == null) {
            dto = new MilkReceipt();
        }
        if (listMilkReceipt != null && listMilkReceipt.size() != 0) {

            dto.setMilkDispatch(cboxChallanNo.getValue());
            dto.setFromDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            dto.setToDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()));
            dto.setFromShift(cboxFromShift.getValue());
            dto.setToShift(cboxToShift.getValue());
            dto.setSociety(MainApp.identityDto.getSociety());
            dto.setUnion(MainApp.identityDto.getUnion());
            receiptDto = new MilkReceiptDto(dto, listMilkReceipt);
        }
    }

    @Override
    public void saveData() {
        var task = new MilkReceiptSaveTask(receiptDto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                        resourceBundle.getString("record.save.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceipt.fxml")));

//                Optional<ButtonType> resp = alert.createConfirmationAlert();
//                if (resp.isPresent() && resp.get() == ButtonType.OK){
//                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceipt.fxml")));
//                }else if (resp.isPresent() && resp.get() == ButtonType.CANCEL) {
//                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceipt.fxml")));
//               }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        if (!listDeleteTxn.isEmpty())
            listDeleteTxn.forEach(txn -> {
                deleteTransaction(txn);
            });

        var task = new MilkReceiptSaveTask(receiptDto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                        resourceBundle.getString("record.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceipt.fxml")));
//                if (resp.isPresent() && resp.get() == ButtonType.OK){
//                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceipt.fxml")));
//                }else if (resp.isPresent() && resp.get() == ButtonType.CANCEL) {
//                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceipt.fxml")));
//                }


            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (dpFromDate.getValue() == null || dpToDate.getValue() == null) {
            if (dpFromDate.getValue() == null) {
                errorMsg.append(resourceBundle.getString("fromdatenullerror") + "\n");
            }
            if (dpToDate.getValue() == null) {
                errorMsg.append(resourceBundle.getString("todatenullerror") + "\n");
            }
        } else {
            if (dpFromDate.getValue().isAfter(LocalDate.now())) {
                errorMsg.append(resourceBundle.getString("fromdatesmallerthantodate") + "\n");
            }
            if (dpToDate.getValue().isAfter(LocalDate.now())) {
                errorMsg.append(resourceBundle.getString("futuredate.notallowedfortodate") + "\n");
            }
            if (dpToDate.getValue().isBefore(dpFromDate.getValue())) {
                errorMsg.append(resourceBundle.getString("fromdatesmallerthantodate") + "\n");
            }
        }
        if (cboxFromShift.getSelectionModel().getSelectedItem() == null) {
            errorMsg.append(resourceBundle.getString("firstshifterror") + "\n");
        }
        if (cboxToShift.getSelectionModel().getSelectedItem() == null) {
            errorMsg.append(resourceBundle.getString("secondshifterror") + "\n");
        }

        return errorMsg.length() == 0;
    }

    private boolean validateTxn() {
        errorMsg = new StringBuilder();
        if (dpFromDate.getValue() == null || dpToDate.getValue() == null) {
            if (dpFromDate.getValue() == null) {
                errorMsg.append(resourceBundle.getString("fromdatenullerror") + "\n");
            }
            if (dpToDate.getValue() == null) {
                errorMsg.append(resourceBundle.getString("todatenullerror") + "\n");
            }
        } else {
            if (dpFromDate.getValue().isAfter(LocalDate.now())) {
                errorMsg.append(resourceBundle.getString("fromdatesmallerthantodate") + "\n");
            }
            if (dpToDate.getValue().isAfter(LocalDate.now())) {
                errorMsg.append(resourceBundle.getString("futuredate.notallowedfortodate") + "\n");
            }
            if (dpToDate.getValue().isBefore(dpFromDate.getValue())) {
                errorMsg.append(resourceBundle.getString("fromdatesmallerthantodate") + "\n");
            }
        }
        if (cboxFromShift.getSelectionModel().getSelectedItem() == null) {
            errorMsg.append(resourceBundle.getString("firstshifterror") + "\n");
        }
        if (cboxToShift.getSelectionModel().getSelectedItem() == null) {
            errorMsg.append(resourceBundle.getString("secondshifterror") + "\n");
        }
        return errorMsg.length() == 0;
    }

    private void addEntry() {
        if (validateTransaction()) {
            if (dtoTxn == null) {
                dtoTxn = new MilkReceiptTransaction();
                dtoTxn.setSocietyPurchaseRateCode(societyMilkPurchaseRate.getCode());
            }
            dtoTxn.setMilkQualityType(cboxMilkQuality.getSelectionModel().getSelectedItem());
            dtoTxn.setMilkType(cboxMilkType.getSelectionModel().getSelectedItem());
//            int ReceiptType = cboxReceiptType.getSelectionModel().getSelectedItem()
//                    .equals(resourceBundle.getString("cans")) ? 0 : 1;
            if (!txtClr.getText().trim().isEmpty())
                dtoTxn.setAvgClr(new BigDecimal(txtClr.getText()));
            if (!txtFat.getText().trim().isEmpty())
                dtoTxn.setAvgFat(new BigDecimal(txtFat.getText()));
            if (!txtSnf.getText().trim().isEmpty())
                dtoTxn.setAvgSnf(new BigDecimal(txtSnf.getText()));

            dtoTxn.setQty(new BigDecimal(txtQuanity.getText()));
            dtoTxn.setQuantityMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.RECEIPT_QTY_MODE, "0")));
            dtoTxn.setConvertedQuantity(CommonUtils.convertQty(AppConstant.CollectionType.RECEIPT, txtQuanity.getText()));
            dtoTxn.setConvertedQuantityMode(BigDecimal.valueOf(dtoTxn.getQuantityMode() == 0 ? 1 : 0));
            if (!txtWater.getText().trim().isEmpty())
                dtoTxn.setWater(new BigDecimal(txtWater.getText()));
            dtoTxn.setRate(new BigDecimal(txtRtpl.getText() != null && !txtRtpl.getText().isEmpty() ? txtRtpl.getText() : "0"));
            dtoTxn.setAmount(new BigDecimal(txtAmount.getText() != null && !txtAmount.getText().isEmpty() ? txtAmount.getText() : "0"));
            dtoTxn.setAcidity(new BigDecimal("0"));
            dtoTxn.setDensity(new BigDecimal("0"));
            dtoTxn.setFreezingPoint(new BigDecimal("0"));
            dtoTxn.setLactose(new BigDecimal("0"));
            dtoTxn.setProtein(new BigDecimal("0"));
            dtoTxn.setTemp(new BigDecimal("0"));


            listMilkReceipt.add(dtoTxn);
            dtoTxn = null;
            clearInnerControls();
            gridTransaction.setDisable(true);
            btnAdd.setText(resourceBundle.getString("add"));
            loadMilkReceiptTempData(listMilkReceipt);
        } else {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                    errorMsg.toString());
            alert.createAlert();
        }
    }

    private void clearInnerControls() {
        txtQuanity.setText("");
        txtFat.setText("");
        txtSnf.setText("");
        txtClr.setText("");
        txtWater.setText("");

        txtRtpl.setText("0");
        txtAmount.setText("0");
        txtWater.setText("0");
        txtCans.setText("1");
        txtChamberNo.setText("0");
        cboxMilkType.getSelectionModel().clearSelection();
    }

    private void loadMilkReceiptTempData(ObservableList<MilkReceiptTransaction> listMilkReceiptTemp) {
        if (listMilkReceiptTemp != null && listMilkReceiptTemp.size() != 0) {
            tableMilkReceipt.setItems(listMilkReceipt);
            tableMilkReceipt.getSelectionModel().clearSelection();
        }
    }

    private boolean validateTransaction() {
        errorMsg = new StringBuilder();

        if (cboxMilkQuality.getSelectionModel().getSelectedItem() == null) {
            errorMsg.append(resourceBundle.getString("milkqualityerror") + "\n");
        }
        if (cboxMilkType.getValue() == null) {
            errorMsg.append(resourceBundle.getString("milktypeerror") + "\n");
        }
        if (txtQuanity.getText().trim() == null || txtQuanity.getText().isEmpty()) {
            errorMsg.append(resourceBundle.getString("Receiptedquantitynullerror") + "\n");
        }

        if (MainApp.getProperty("zero.amount.Receipt", "").equalsIgnoreCase("0")) {
            if (txtAmount.getText().trim() == null || txtAmount.getText().isEmpty() || txtAmount.getText().equalsIgnoreCase("0.00")) {
                errorMsg.append(resourceBundle.getString("amount.cannot.be.null") + "\n");
            }
            if (txtRtpl.getText().trim() == null || txtRtpl.getText().isEmpty() || txtRtpl.getText().equalsIgnoreCase("0")) {
                errorMsg.append(resourceBundle.getString("rate.cannot.be.null") + "\n");
            }
        }
        if (!txtFat.isDisable()) {
            if (Double.parseDouble(
                    txtFat.getText() != null && !txtFat.getText().isEmpty() ? txtFat.getText() : "0") == 0) {
                errorMsg.append(resourceBundle.getString("fat.cannot.be.null") + "\n");
            }
        }
        if (!txtSnf.isDisable()) {
            if (Double.parseDouble(
                    txtSnf.getText() != null && !txtSnf.getText().isEmpty() ? txtSnf.getText() : "0") == 0) {
                errorMsg.append(resourceBundle.getString("snf.cannot.be.null") + "\n");
            }
        }
        if (!txtClr.isDisable()) {
            if (txtClr.getText().trim().isEmpty()) {
                errorMsg.append(resourceBundle.getString("clr.cannot.be.null") + "\n");
            }
        }
        if (!txtCans.isDisable()) {
//            if (Double.parseDouble(
//                    txtCans.getText() != null && !txtCans.getText().isEmpty() ? txtCans.getText() : "0") == 0) {
//                errorMsg.append(resourceBundle.getString("noofcans.cannot.be.null") + "\n");

        }
//        if (!txtWater.isDisable()) {
//            if (txtWater.getText() == null && txtWater.getText().isEmpty()) {
//                errorMsg.append(resourceBundle.getString("water.cannot.be.null") + "\n");
//            }
//        }
//        if (!txtChamberNo.isDisable()) {
//            if (Double.parseDouble(
//                    txtChamberNo.getText() != null && !txtChamberNo.getText().isEmpty() ? txtChamberNo.getText() : "0") == 0) {
//                errorMsg.append(resourceBundle.getString("chambernonull") + "\n");
//            }
//        }
        if (errorMsg.length() == 0) {
            if (listMilkReceipt != null & listMilkReceipt.size() != 0 && cboxMilkQuality.getValue() != null) {
                for (MilkReceiptTransaction milkReceipt : listMilkReceipt) {
                    if (!milkReceipt.equals(propMilkReceiptTransaction.get())) {
                        if (milkReceipt.getMilkType().getCode() == cboxMilkType.getSelectionModel()
                                .getSelectedItem().getCode()
                                && milkReceipt.getMilkQualityType().getCode() == cboxMilkQuality
                                .getSelectionModel().getSelectedItem().getCode()) {
                            errorMsg.append(resourceBundle.getString("milktype.milkqualitytype.already.available") + "\n");
                        }
                    }
                }
            }
        }

        return errorMsg.length() == 0;
    }

    @Override
    protected void setClr(String clr) {
        txtClr.setText(clr);
    }

    @Override
    protected void setAmount(String amount) {
        txtAmount.setText(amount);
    }

    @Override
    protected void setRate(String rate) {
        txtRtpl.setText(rate);
    }

    @Override
    protected String getQuantity() {
        return txtQuanity.getText();
    }

    @Override
    protected void setQuantity(String val) {
        txtQuanity.setText(val);
    }

    private void fetchRateDetails(String code) {
        var task = new MilkDispatchRateAndDetailsLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                MilkDispatchRateAndDetailsDto dto = task.get();
                if (dto != null) {
                    societyMilkPurchaseRate = dto.getSocietyMilkPurchaseRate();
                    mapRateDetails = dto.getDetails();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchPurchaseRateCode() {
        var task = new MilkDispatchSocietyPurchaseRateLoadTask(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()),
                cboxToShift.getValue(), MainApp.identityDto.getSociety());
        task.setOnSucceeded(e -> {
            try {
                societyMilkPurchaseRate = task.get();
                if (societyMilkPurchaseRate != null && (short) 1 != societyMilkPurchaseRate.getRateGenMethodCode()) {
                    fetchRateDetails(societyMilkPurchaseRate.getCode());
                } else {
                    txtQuanity.textProperty().removeListener(qtyRateChangeListener);
                    txtRtpl.textProperty().removeListener(qtyRateChangeListener);
                    txtQuanity.textProperty().addListener(qualityParamChangeListener);
                }
                if (societyMilkPurchaseRate != null)
                    fetchRateBased(societyMilkPurchaseRate.getCode());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchRateBased(String code) {
        var task = new SocietyRateBasedLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                listBased = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadReceiptDetail(String challanNo) {
        var task = new MilkReceiptTransactionLoadTask(challanNo);
        task.setOnSucceeded(e -> {
            try {
                List<MilkReceiptTransaction> listMilkReceipttxn = task.get();
                if (listMilkReceipttxn != null) {
                    listMilkReceipt = FXCollections.observableArrayList(listMilkReceipttxn);
                    setValuesInControls();

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInControls() {

        dpFromDate.setValue(dto.getFromDate().toLocalDate());
        cboxFromShift.setValue(dto.getFromShift());
        dpToDate.setValue(dto.getToDate().toLocalDate());
        cboxToShift.setValue(dto.getToShift());

        if (listMilkReceipt != null)
            tableMilkReceipt.setItems(listMilkReceipt);


        cboxChallanNo.setValue(dto.getMilkDispatch() != null ? dto.getMilkDispatch() : null);

    }

    private void deleteTransaction(MilkReceiptTransaction transaction) {

        var task = new MilkReceiptTransactionDeleteTask(transaction.getTxnCode());
        task.setOnSucceeded(e -> {
            try {
                boolean isDelete = task.get();
                if (isDelete) {
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


}
