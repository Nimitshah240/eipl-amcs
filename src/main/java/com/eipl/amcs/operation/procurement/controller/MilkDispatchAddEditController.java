package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.model.Plant;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.task.BmcLoadTask;
import com.eipl.amcs.master.org.task.MccLoadTask;
import com.eipl.amcs.master.org.task.PlantLoadTask;
import com.eipl.amcs.master.org.task.UnionLoadTask;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchDto;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchSummaryDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.task.*;
import com.eipl.amcs.report.util.ReportGenerate;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MilkDispatchAddEditController extends MilkDispatchBaseController implements MyInitialization {

    private final ObjectProperty<MilkDispatchSummaryDto> propDto;
    private final ObjectProperty<MilkDispatchTransaction> propMilkDispatchTransaction;
    private final ObservableList<MilkDispatchTransaction> listDeleteTxn;

    List<MilkDispatchSummaryDto> milkDispatchSummaryDtoList = new ArrayList<>();
    List<MilkQualityType> milkQualityTypeList = new ArrayList<>();
    @FXML
    private StackPane root;
    @FXML
    private GridPane gridMaster;
    @FXML
    private E_TextField txtChallanNo, txtVehicleNo, txtQuanity, txtFat, txtSnf, txtClr,
            txtWater, txtRtpl, txtAmount, txtChamberNo, txtCans, txtDipStickReadingClosing, txtDipStickReadingOpening, txtRouteNo;
    @FXML
    private TextField txtVehicleInTime, txtVehicleOutTime;
    @FXML
    private E_DatePicker dpFromDate, dpToDate, dpChallanDate;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private ComboBox<String> cboxDispatchType;
    //    @FXML
//    private ComboBox<Route> cboxRouteNo;
    @FXML
    private ComboBox<String> cboxDestinationType;
    @FXML
    private ComboBox<String> cboxDestination;
    @FXML
    private TableView<MilkDispatchSummaryDto> tableMilkDispatchSummary;
    @FXML
    private TableColumn<MilkDispatchSummaryDto, MilkType> colCollecMilkType;
    @FXML
    private TableColumn<MilkDispatchSummaryDto, BigDecimal> colPurchaseMilk, colLocalMilkSale, colDifference;
    @FXML
    private GridPane gridTransaction;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private ComboBox<MilkQualityType> cboxMilkQuality;
    @FXML
    private Label lblQuantity;
    @FXML
    private HBox btnPanel;
    @FXML
    private Button btnAdd, btnEdit, btnDelete, btnCopy;
    @FXML
    private TableView<MilkDispatchTransaction> tableMilkDispatch;
    @FXML
    private TableColumn<MilkDispatchTransaction, MilkType> colMilkType;
    @FXML
    private TableColumn<MilkDispatchTransaction, BigDecimal> colQuantity, colFat, colSnf, colClr, colRate, colAmount, colCans, colWater;
    @FXML
    private HBox btnMainPanel;
    @FXML
    private E_Button btnSaveUpdate, btnClose;
    private ResourceBundle resourceBundle;
    private MilkDispatch dto;
    private MilkDispatchTransaction dtoTxn;
    private MilkDispatchDto dispatchDto;
    private StringBuilder errorMsg = null;
    private ObservableList<MilkDispatchTransaction> listMilkDispatch;
    private String challanNo;

    private final ChangeListener<String> qualityParamChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            fetchRateForDispatch(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue(), CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            calculateClr(txtFat.getText(), txtSnf.getText());
        }
    };
    private final ChangeListener<String> qtyRateChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            calculateAmount(txtRtpl.getText(), txtQuanity.getText());
        }
    };

    public MilkDispatchAddEditController() {
        propMilkDispatchTransaction = new SimpleObjectProperty<>();
        listMilkDispatch = FXCollections.observableArrayList();
        listDeleteTxn = FXCollections.observableArrayList();
        propDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setMilkDispatch(MilkDispatch milkDispatch) {
        if (milkDispatch != null) {
            this.dto = milkDispatch;
            loadDispatchDetail(dto.getChallanNo());
            btnSaveUpdate.setText(resourceBundle.getString("update"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        gridTransaction.setDisable(true);
        dpChallanDate.setValue(LocalDate.now());
        dpChallanDate.setDisable(true);
        loadData();
        setupComboBox();
        setupTable();
        setupCollectionTable();
        FocusUtils.requestFocus(cboxDispatchType);
        btnAdd.setDisable(true);
        //cboxRouteNo.getSelectionModel().select(0);
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("save"))) {
            nextChallanNo();
        }
        cboxMilkType.setOnAction(e -> {
            fetchRateForDispatch(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue(), CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            calculateClr(txtFat.getText(), txtSnf.getText());
        });
        cboxMilkQuality.setOnAction(e -> {
            fetchRateForDispatch(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue(), CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            calculateClr(txtFat.getText(), txtSnf.getText());
        });
        txtFat.textProperty().addListener(qualityParamChangeListener);
        txtSnf.textProperty().addListener(qualityParamChangeListener);
        txtQuanity.textProperty().addListener(qtyRateChangeListener);
        txtRtpl.textProperty().addListener(qtyRateChangeListener);
        cboxDispatchType.getItems().addAll(resourceBundle.getString("cans"),
                resourceBundle.getString("tanker"));
        cboxDispatchType.setOnAction(e -> {
            if (cboxDispatchType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("cans"))) {
                cboxToShift.setDisable(true);
                dpToDate.setDisable(true);
                txtCans.clear();
                txtCans.setDisable(false);
                if (cboxFromShift.getSelectionModel() != null) {
                    cboxToShift.valueProperty().set(null);
                    cboxToShift.setValue(cboxFromShift.getValue());
                }
                if (dpFromDate.getValue() != null) {
                    dpToDate.setValue(null);
                    dpToDate.setValue(dpFromDate.getValue());
                }
                txtChamberNo.clear();
                txtChamberNo.setText("0");
                txtChamberNo.setDisable(true);
            } else {
                txtCans.clear();
                txtCans.setText("0");
                txtCans.setDisable(true);
                cboxToShift.setDisable(false);
                dpToDate.setDisable(false);
                txtChamberNo.clear();
                txtChamberNo.setDisable(false);
            }
        });
        cboxDestinationType.getItems().addAll(resourceBundle.getString("bmc"),
                resourceBundle.getString("mcc"), resourceBundle.getString("plant"), resourceBundle.getString("union"));
        cboxDestinationType.setOnAction(e -> {
            if (cboxDestinationType.getValue() != null) {
                loadDestinationType(cboxDestinationType.getSelectionModel().getSelectedIndex());
            }
        });
        cboxFromShift.setOnAction(e -> {
            if (cboxDispatchType.getSelectionModel().getSelectedIndex() >= 0) {
                if (cboxDispatchType.getSelectionModel().getSelectedItem()
                        .equals(resourceBundle.getString("cans"))) {
                    cboxToShift.setValue(cboxFromShift.getValue());
                }
            }
            loadDispatchSummary();
        });
        dpFromDate.setOnAction(event -> {
            if (cboxDispatchType.getSelectionModel().getSelectedIndex() >= 0) {
                if (cboxDispatchType.getSelectionModel().getSelectedItem()
                        .equals(resourceBundle.getString("cans"))) {
                    dpToDate.setValue(dpFromDate.getValue());
                }
            }
            loadDispatchSummary();
        });
        cboxToShift.setOnAction(e -> loadDispatchSummary());
        dpToDate.setOnAction(e -> loadDispatchSummary());
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));
        });
        btnAdd.setOnAction(event -> {
            if (btnAdd.getText().equals(resourceBundle.getString("add"))) {
                if (validateTxn()) {
                    if (cboxDispatchType.getSelectionModel().getSelectedItem() != null) {
                        cboxDispatchType.setDisable(true);
                        btnAdd.setText(resourceBundle.getString("save"));
                        btnDelete.setDisable(false);
                        btnDelete.setText(resourceBundle.getString("cancel"));
                        gridTransaction.setDisable(false);
                        gridMaster.setDisable(true);
                        btnEdit.setDisable(true);
                    }
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                            errorMsg.toString());
                    alert.createAlert();
                }
            } else {
                addEntry();
            }
        });
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        btnDelete.setOnAction(e -> {
            if (btnDelete.getText().equals(resourceBundle.getString("cancel"))) {
                if (btnAdd.getText().equals(resourceBundle.getString("save"))) {
                    gridMaster.setDisable(false);
                    gridTransaction.setDisable(true);
                    btnAdd.setText(resourceBundle.getString("add"));
                }
                clearInnerControls();
            } else {
                if (propMilkDispatchTransaction.get() != null) {
                    MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                            resourceBundle.getString("alert.delete"));
                    Optional<ButtonType> resp = alert.createConfirmationAlert();
                    if (resp.isPresent() && resp.get() == ButtonType.OK) {
                        listDeleteTxn.add(propMilkDispatchTransaction.get());
                        listMilkDispatch.remove(propMilkDispatchTransaction.get());
                    }
                }
            }

        });
        propMilkDispatchTransaction.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                btnAdd.setDisable(false);
                btnEdit.setDisable(true);
                btnDelete.setText(resourceBundle.getString("cancel"));
            } else {
                btnAdd.setText(resourceBundle.getString("add"));
                btnDelete.setText(resourceBundle.getString("delete"));
                btnDelete.setDisable(false);
            }
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_EDIT"))
                throw new UnAuthorizedAccessException();
        });
        btnCopy.setOnAction(e -> {
            copyData();
        });
    }

    private void copyData() {
        fetchPurchaseRateCode();
        if (societyMilkPurchaseRate == null) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                    resourceBundle.getString("ratemaster.not.found"));
            alert.createAlert();
            return;
        }
        if (milkDispatchSummaryDtoList == null || milkDispatchSummaryDtoList.isEmpty()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                    resourceBundle.getString("select.valid.date"));
            alert.createAlert();
            return;
        }

        listMilkDispatch.clear();
        for (MilkDispatchSummaryDto milkDispatchSummaryDto : milkDispatchSummaryDtoList) {
            dtoTxn = new MilkDispatchTransaction();

            dtoTxn.setMilkQualityType(milkQualityTypeList.get(0));
            dtoTxn.setMilkType(milkDispatchSummaryDto.getMilkType());
            dtoTxn.setAvgFat(milkDispatchSummaryDto.getFat().setScale(1, RoundingMode.DOWN));
            BigDecimal qty = milkDispatchSummaryDto.getMilkCollection().subtract(milkDispatchSummaryDto.getMilkSale());

            if (qty.compareTo(BigDecimal.ZERO) <= 0) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                        resourceBundle.getString("milk.quantity.error"));
                alert.createAlert();
                continue;
            }
            int qtyMode = CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.DISPATCH_QTY_MODE, "1"));

            dtoTxn.setQty(qtyMode == 0 ? qty : CommonUtils.convertQtyToKg(qty.toString()));
            dtoTxn.setQuantityMode(qtyMode);
            dtoTxn.setConvertedQuantity(qtyMode == 0 ? CommonUtils.convertQty(AppConstant.CollectionType.DISPATCH, qty.toString()) : qty);
            dtoTxn.setConvertedQuantityMode(dtoTxn.getQuantityMode() == 0 ? 1 : 0);
            dtoTxn.setAvgSnf(milkDispatchSummaryDto.getSnf().setScale(1, RoundingMode.DOWN));

            fetchRateForDispatchTable(String.valueOf(dtoTxn.getAvgFat()), String.valueOf(dtoTxn.getAvgSnf()), dtoTxn.getMilkType(), dtoTxn.getMilkQualityType(), dpFromDate.getValue().atStartOfDay(), dtoTxn);

            dtoTxn.setRate(milkDispatchSummaryDto.getAmount().divide(milkDispatchSummaryDto.getMilkCollection(), RoundingMode.HALF_UP));
//            dtoTxn.setRate(milkDispatchSummaryDto.getAmount().divide(milkDispatchSummaryDto.getMilkCollection(), RoundingMode.HALF_UP));
            dtoTxn.setWater(BigDecimal.ZERO);
            dtoTxn.setAmount(milkDispatchSummaryDto.getAmount());
            dtoTxn.setNosOfCan(1);
            listMilkDispatch.add(dtoTxn);
        }
        loadMilkDispatchTempData(listMilkDispatch);
        setupTable();
    }

    private void loadDispatchSummary() {
        if (dpFromDate.getValue() != null && cboxFromShift.getValue() != null &&
                dpToDate.getValue() != null && cboxToShift.getValue() != null) {
            var task = new MilkDispatchSummaryTask(
                    CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()),
                    CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue())
            );
            task.setOnSucceeded(e -> {
                try {
                    fetchPurchaseRateCode();
                    milkDispatchSummaryDtoList = task.get();
                    if (milkDispatchSummaryDtoList != null)
                        tableMilkDispatchSummary.setItems(FXCollections.observableList(milkDispatchSummaryDtoList));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }


    private void nextChallanNo() {
        var task = new MilkDispatchGetNextChallanNoTask();
        task.setOnSucceeded(e -> {
            try {
                challanNo = task.get();
                if (challanNo != null) {
                    txtChallanNo.setText(challanNo);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadDestinationType(int selectedIndex) {
        if (selectedIndex == 0) {
            var task = new BmcLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Bmc> list = task.get();
                    List<String> listBmc = new LinkedList<>();
                    if (list != null) {
                        for (Bmc bmc : list) {
                            listBmc.add(bmc.getName());
                        }
                        cboxDestination.setItems(FXCollections.observableList(listBmc));
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();

        } else if (selectedIndex == 1) {
            var task = new MccLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Mcc> list = task.get();
                    List<String> listMcc = new LinkedList<>();
                    if (list != null) {
                        for (Mcc mcc : list) {
                            listMcc.add(mcc.getName());
                        }
                        cboxDestination.setItems(FXCollections.observableList(listMcc));
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else if (selectedIndex == 2) {
            var task = new PlantLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Plant> list = task.get();
                    List<String> listMcc = new LinkedList<>();
                    if (list != null) {
                        for (Plant mcc : list) {
                            listMcc.add(mcc.getName());
                        }
                        cboxDestination.setItems(FXCollections.observableList(listMcc));
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else {
            var task = new UnionLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Union> list = task.get();
                    List<String> listMcc = new LinkedList<>();
                    if (list != null) {
                        for (Union mcc : list) {
                            listMcc.add(mcc.getName());
                        }
                        cboxDestination.setItems(FXCollections.observableList(listMcc));
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
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

//        var task3 = new RouteLoadTask();
//        task3.setOnSucceeded(e -> {
//            try {
//                List<Route> list = task3.get();
//                if (list != null) {
//                    cboxRouteNo.setItems(FXCollections.observableList(list));
//                    new AutoCompleteComboBoxListener<>(cboxRouteNo);
//                    if (dto != null) {
//                        cboxRouteNo.getSelectionModel().select(list.stream().filter(p -> p.getCode().equals(dto.getRouteNo())).findFirst().orElse(null));
//                    }
//                }
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task3).start();
    }

    @Override
    public void setupComboBox() {
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkQuality.setConverter(new MilkQualityConvertor(cboxMilkQuality));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        //cboxRouteNo.setConverter(new RouteConvertor(cboxRouteNo));
        dpChallanDate.setConverter(new LocalDateConvertor());
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
            colFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgFat()));
            colSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgSnf()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colCans.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getNosOfCan()));
            colWater.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWater()));

            propMilkDispatchTransaction.bind(tableMilkDispatch.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupCollectionTable() {
        colCollecMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
        colPurchaseMilk.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkCollection()));
        colLocalMilkSale.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkSale()));
        colDifference.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkBalance()));

        propDto.bind(tableMilkDispatchSummary.getSelectionModel().selectedItemProperty());

    }

    private void validateAndSave() {
        setValuesInObjectUpdate();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            updateData();
        } else {
            saveData();
        }
    }

    private void setValuesInObjectUpdate() {
        if (this.dto == null) {
            dto = new MilkDispatch();
        }
        if (listMilkDispatch != null && listMilkDispatch.size() != 0) {
            if (this.challanNo != null && btnSaveUpdate.getText().equals(resourceBundle.getString("save")))
                dto.setChallanNo(this.challanNo);
            dto.setDestinationCode(cboxDestination.getValue());
            dto.setDestinationCode("1");
            dto.setDispatchType(cboxDispatchType.getSelectionModel().getSelectedIndex());
            dto.setDestinationType(0);
            dto.setRouteNo(txtRouteNo.getText().trim());
            dto.setFromDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
            dto.setToDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()));
            dto.setFromShift(cboxFromShift.getValue());
            dto.setToShift(cboxToShift.getValue());
            try {
                if (txtVehicleInTime.getText().length() != 0)
                    dto.setVehicleInTime(LocalTime.parse(txtVehicleInTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm")));
                if (txtVehicleOutTime.getText().length() != 0)
                    dto.setVehicleOutTime(LocalTime.parse(txtVehicleOutTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm")));
                if (txtVehicleNo.getText().trim().length() != 0)
                    dto.setVehicleNo(txtVehicleNo.getText());
                if (txtDipStickReadingOpening.getText().trim().length() != 0)
                    dto.setDipStickReadingOpening(new BigDecimal(txtDipStickReadingOpening.getText()));
                if (txtDipStickReadingClosing.getText().trim().length() != 0)
                    dto.setDipStickReadingClosing(new BigDecimal(txtDipStickReadingClosing.getText()));
            } catch (Exception e) {
            }
            dto.setSociety(MainApp.identityDto.getSociety());
            dto.setUnion(MainApp.identityDto.getUnion());
            dispatchDto = new MilkDispatchDto(dto, listMilkDispatch);
        }
    }

    @Override
    public void saveData() {
        var task = new MilkDispatchSaveTask(dispatchDto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                if (obj instanceof BusinessValidationFailException) {
                    BusinessValidationFailException exception = (BusinessValidationFailException) obj;
                    StringBuilder sb = new StringBuilder();
                    for (org.springframework.validation.FieldError error : exception.getListFieldErrors()) {
                        sb.append(error.getDefaultMessage()).append("\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"), sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                        resourceBundle.getString("record.save.successful") + "\n"
                                + resourceBundle.getString("alert.dispatchnote"));
                Optional<ButtonType> resp = alert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                    params.put("p_challan_no", dto.getChallanNo());
                    params.put("p_locale", MainApp.locale);
                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_DISPATCH_CHALLAN_FORMAT_THREE, params);
                    JasperViewer.viewReport(print, false);
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));

                } else if (resp.isPresent() && resp.get() == ButtonType.CANCEL) {
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));
                }

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

        var task = new MilkDispatchSaveTask(dispatchDto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                if (obj instanceof BusinessValidationFailException) {
                    BusinessValidationFailException exception = (BusinessValidationFailException) obj;
                    StringBuilder sb = new StringBuilder();
                    for (org.springframework.validation.FieldError error : exception.getListFieldErrors()) {
                        sb.append(error.getDefaultMessage()).append("\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"), sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                        resourceBundle.getString("record.update.successful") + "\n"
                                + resourceBundle.getString("alert.dispatchnote"));
                Optional<ButtonType> resp = alert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                    params.put("p_invoice_no", dto.getChallanNo());
                    params.put("p_locale", MainApp.locale);
                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_DISPATCH_CHALLAN, params);
                    JasperViewer.viewReport(print, false);
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));
                } else if (resp.isPresent() && resp.get() == ButtonType.CANCEL) {
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (dispatchDto == null) {
            errorMsg.append(resourceBundle.getString("dispatchedquantitynullerror") + "\n");
        }
        if (cboxDispatchType.getValue() == null || cboxDispatchType.getValue().isEmpty()) {
            errorMsg.append(resourceBundle.getString("dispatchtypenullerror") + "\n");
        }
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
        if (txtChallanNo.getText().trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("challannullerror") + "\n");
        }
//        if (txtRouteNo.getText().trim().isEmpty()) {
//            errorMsg.append(resourceBundle.getString("routeerror") + "\n");
//        }
        return errorMsg.length() == 0;
    }

    private boolean validateTxn() {
        errorMsg = new StringBuilder();
        if (cboxDispatchType.getValue() == null || cboxDispatchType.getValue().isEmpty()) {
            errorMsg.append(resourceBundle.getString("dispatchtypenullerror") + "\n");
        }
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
        if (txtChallanNo.getText().trim().isEmpty()) {
            errorMsg.append(resourceBundle.getString("challannullerror") + "\n");
        }
//        if (!txtVehicleInTime.getText().isEmpty()) {
//            try {
//                LocalTime.parse(txtVehicleInTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
//            } catch (Exception e) {
//                errorMsg.append(resourceBundle.getString("validintime") + "\n");
//            }
//        }
//        if (!txtVehicleOutTime.getText().isEmpty()) {
//            try {
//                LocalTime.parse(txtVehicleOutTime.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
//            } catch (Exception e) {
//                errorMsg.append(resourceBundle.getString("validouttime") + "\n");
//            }
//        }
//        if (txtRouteNo.getText().trim().isEmpty()) {
//            errorMsg.append(resourceBundle.getString("routeerror") + "\n");
//        }

        return errorMsg.length() == 0;
    }

    private void addEntry() {
        if (validateTransaction()) {
            if (dtoTxn == null) {
                dtoTxn = new MilkDispatchTransaction();
                dtoTxn.setSocietyPurchaseRateCode(societyMilkPurchaseRate.getCode());
            }
            dtoTxn.setMilkQualityType(cboxMilkQuality.getSelectionModel().getSelectedItem());
            dtoTxn.setMilkType(cboxMilkType.getSelectionModel().getSelectedItem());
            if (!txtClr.getText().trim().isEmpty())
                dtoTxn.setAvgClr(new BigDecimal(txtClr.getText()));
            if (!txtFat.getText().trim().isEmpty())
                dtoTxn.setAvgFat(new BigDecimal(txtFat.getText()));
            if (!txtSnf.getText().trim().isEmpty())
                dtoTxn.setAvgSnf(new BigDecimal(txtSnf.getText()));
            if (txtCans.getText().length() != 0)
                dtoTxn.setNosOfCan(Integer.parseInt(txtCans.getText()));
            if (!txtQuanity.getText().trim().isEmpty()) {
                dtoTxn.setQty(new BigDecimal(txtQuanity.getText() == null ? "0" : txtQuanity.getText()));
                dtoTxn.setQuantityMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.DISPATCH_QTY_MODE, "1")));
                dtoTxn.setConvertedQuantity(CommonUtils.convertQty(AppConstant.CollectionType.DISPATCH, txtQuanity.getText()));
                dtoTxn.setConvertedQuantityMode(dtoTxn.getQuantityMode() == 0 ? 1 : 0);
            }
            dtoTxn.setQuantityMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.DISPATCH_QTY_MODE, "0")));
            dtoTxn.setConvertedQuantity(CommonUtils.convertQty(AppConstant.CollectionType.DISPATCH, txtQuanity.getText()));
            dtoTxn.setConvertedQuantityMode(dtoTxn.getQuantityMode() == 0 ? 1 : 0);
            if (!txtWater.getText().trim().isEmpty())
                dtoTxn.setWater(new BigDecimal(txtWater.getText()));
            dtoTxn.setRate(new BigDecimal(txtRtpl.getText() != null && !txtRtpl.getText().isEmpty() ? txtRtpl.getText() : "0"));
            dtoTxn.setAmount(new BigDecimal(
                    txtAmount.getText() != null && !txtAmount.getText().isEmpty() ? txtAmount.getText() : "0"));
            dtoTxn.setChamberNo(txtChamberNo.getText());
            listMilkDispatch.add(dtoTxn);
            dtoTxn = null;
            clearInnerControls();
            gridTransaction.setDisable(true);
            btnAdd.setText(resourceBundle.getString("add"));
            loadMilkDispatchTempData(listMilkDispatch);
        } else {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
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
        if (cboxDispatchType.getValue().equals(resourceBundle.getString("cans"))) {
            txtCans.setText("");
            txtChamberNo.setText("0");
        } else {
            txtCans.setText("0");
            txtChamberNo.setText("");
        }
        txtRtpl.setText("0");
        txtAmount.setText("0");
        cboxMilkType.getSelectionModel().clearSelection();
    }

    private void loadMilkDispatchTempData(ObservableList<MilkDispatchTransaction> listMilkDispatchTemp) {
        if (listMilkDispatchTemp != null && listMilkDispatchTemp.size() != 0) {
            tableMilkDispatch.setItems(listMilkDispatch);
            tableMilkDispatch.getSelectionModel().clearSelection();
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
            errorMsg.append(resourceBundle.getString("dispatchedquantitynullerror") + "\n");
        }

        if (MainApp.getProperty("zero.amount.dispatch", "").equalsIgnoreCase("0")) {
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
            if (Double.parseDouble(
                    txtCans.getText() != null && !txtCans.getText().isEmpty() ? txtCans.getText() : "0") == 0) {
                errorMsg.append(resourceBundle.getString("noofcans.cannot.be.null") + "\n");
            }
        }

        if (Double.parseDouble(
                txtRtpl.getText() != null && !txtRtpl.getText().trim().isEmpty() ? txtRtpl.getText() : "0") == 0) {
            errorMsg.append(resourceBundle.getString("rate.cannot.be.null") + "\n");
        }

        if (!txtWater.isDisable()) {
            if (txtWater.getText() == null && txtWater.getText().isEmpty()) {
                errorMsg.append(resourceBundle.getString("water.cannot.be.null") + "\n");
            }
        }
        if (!txtChamberNo.isDisable()) {
            if (Double.parseDouble(
                    txtChamberNo.getText() != null && !txtChamberNo.getText().isEmpty() ? txtChamberNo.getText() : "0") == 0) {
                errorMsg.append(resourceBundle.getString("chambernonull") + "\n");
            }
        }
        if (errorMsg.length() == 0) {
            if (listMilkDispatch != null & listMilkDispatch.size() != 0 && cboxMilkQuality.getValue() != null) {
                for (MilkDispatchTransaction milkDispatch : listMilkDispatch) {
                    if (!milkDispatch.equals(propMilkDispatchTransaction.get())) {
                        if (milkDispatch.getMilkType().getCode() == cboxMilkType.getSelectionModel()
                                .getSelectedItem().getCode()
                                && milkDispatch.getMilkQualityType().getCode() == cboxMilkQuality
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
        var task = new MilkDispatchSocietyPurchaseRateLoadTask
                (CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()),
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

//                if (listBased != null) {
                listBased = task.get();
//                }else{
//                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
//                            resourceBundle.getString("ratemaster.not.found"));
//                    alert.createAlert();
//                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadDispatchDetail(String challanNo) {
        var task = new MilkDispatchTransactionLoadTask(challanNo);
        task.setOnSucceeded(e -> {
            try {
                List<MilkDispatchTransaction> listMilkDispatchtxn = task.get();
                if (listMilkDispatchtxn != null) {
                    this.listMilkDispatch = FXCollections.observableArrayList(listMilkDispatchtxn);
                    loadData();
                    setValuesInControls();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInControls() {
        txtChallanNo.setText(dto.getChallanNo());
        int dispatch = dto.getDispatchType().intValue();
        cboxDispatchType.setValue(cboxDispatchType.getItems().get(dispatch));
        if (!txtDipStickReadingOpening.getText().isEmpty() && dto != null && dto.getDipStickReadingOpening() != null) {
            txtDipStickReadingOpening.setText(String.valueOf(dto.getDipStickReadingOpening()));
        }
        if (!txtDipStickReadingClosing.getText().isEmpty() && dto != null && dto.getDipStickReadingClosing() != null) {
            txtDipStickReadingClosing.setText(String.valueOf(dto.getDipStickReadingClosing()));
        }
        dpFromDate.setValue(dto.getFromDate().toLocalDate());
        cboxFromShift.setValue(dto.getFromShift());
        dpToDate.setValue(dto.getToDate().toLocalDate());
        cboxToShift.setValue(dto.getToShift());
        cboxDestinationType.setValue(dto.getDestinationType() == 0 ? resourceBundle.getString("bmc") : dto.getDestinationType() == 1 ?
                resourceBundle.getString("mcc") : (dto.getDestinationType() == 2 ? resourceBundle.getString("plant") :
                resourceBundle.getString("union")));
        cboxDestination.setValue(dto.getDestinationCode());
        if (dto.getVehicleNo() != null)
            txtVehicleNo.setText(dto.getVehicleNo());
        if (dto.getVehicleInTime() != null)
            txtVehicleInTime.setText(dto.getVehicleInTime().toString());
        if (dto.getVehicleOutTime() != null)
            txtVehicleOutTime.setText(dto.getVehicleOutTime().toString());
        if (this.listMilkDispatch != null)
            tableMilkDispatch.setItems(listMilkDispatch);
        if (dto.getRouteNo() != null)
            txtRouteNo.setText(dto.getRouteNo());

    }

    private void deleteTransaction(MilkDispatchTransaction transaction) {
        var task = new MilkDispatchTransactionDeleteTask(transaction.getTxnCode());
        task.setOnSucceeded(e -> {
            try {
                boolean isDelete = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}