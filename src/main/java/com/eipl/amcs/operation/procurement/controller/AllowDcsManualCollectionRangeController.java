package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.task.AllowDcsManualCollectionDateShiftValidationLoadTask;
import com.eipl.amcs.operation.procurement.task.AllowDcsManualCollectionRangeLoadTask;
import com.eipl.amcs.operation.procurement.task.AllowDcsManualCollectionRangeSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class AllowDcsManualCollectionRangeController implements MyInitialization, PopupCallback {

    @FXML
    private StackPane root;
    @FXML
    private GridPane gridMaster;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    TableView<AllowDcsManualCollectionRange> tableManualRequest;
    @FXML
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private ComboBox<String> cboxStatus;
    @FXML
    private ComboBox<String> cboxType;
    @FXML
    private CheckBox chkIsQualityManual, chkIsWeightManual;

    @FXML
    private TextField txtRemarks;
    @FXML
    private TableColumn<AllowDcsManualCollectionRange, LocalDate> colFromDate, colToDate;

    @FXML
    private TableColumn<AllowDcsManualCollectionRange, Shift> colFromShift, colToShift;
    @FXML
    private TableColumn<AllowDcsManualCollectionRange, String> colRemarks, colType;
    @FXML
    private TableColumn<AllowDcsManualCollectionRange, Integer> colStatus;
    @FXML
    private TableColumn<AllowDcsManualCollectionRange, String> colIsQuality, colIsWeight;
    private final ObjectProperty<AllowDcsManualCollectionRange> propManualRequestDto;
    public List<AllowDcsManualCollectionRange> requestList = new ArrayList<>();
    @FXML
    private Button btnCancel, btnSave, btnClose, btnRefresh, btnGoMilkCollection;
    private Stage stage;
    private AllowDcsManualCollectionRange manualRequest;
    private AllowDcsManualCollectionRange dto;
    private ResourceBundle resourceBundle;

    public AllowDcsManualCollectionRangeController() {
        propManualRequestDto = new SimpleObjectProperty<>();
    }


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        setupTable();
        setupComboBox();
        setType(cboxType.getSelectionModel().getSelectedIndex());
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        cboxStatus.getItems().addAll("PENDING", "Approved", "REJECT", "CLOSE");
        cboxStatus.getSelectionModel().select(0);
        btnSave.setOnAction(event -> {
            if (cboxType.getSelectionModel().getSelectedIndex() == 1) {
                if (!chkIsQualityManual.isSelected() && !chkIsWeightManual.isSelected()) {
                    MyAlert alert = new ErrorAlert(MainApp.stage, MainApp.getBundle().getString("milkcollection"),
                            MainApp.getBundle().getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
            }
            setValuesInObject();
//            setupTable();
            saveData();
        });
        cboxType.setOnAction(event -> {
            if (cboxType.getSelectionModel().getSelectedIndex() == 0) {
                cboxToShift.setDisable(true);
                chkIsQualityManual.setDisable(true);
                chkIsWeightManual.setDisable(true);
                dpToDate.setDisable(true);
                clearControls();
            } else {
                cboxToShift.setDisable(false);
                chkIsQualityManual.setDisable(false);
                chkIsWeightManual.setDisable(false);
                dpToDate.setDisable(false);
//                clearControls();
            }
        });
        cboxFromShift.setOnAction(event ->
                cboxToShift.setValue(cboxFromShift.getValue()));
        dpFromDate.setOnAction(e ->
                dpToDate.setValue(dpFromDate.getValue()));
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnRefresh.setOnAction(e -> {
            reloadData(true);
        });
        btnGoMilkCollection.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkCollectionAdd.fxml")));
        });
        btnCancel.setOnAction(e -> {
            if (propManualRequestDto.get().getStatus() >= 2) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("allowdcsmanualcollectionrange"),
                        resourceBundle.getString("cancelled.can.not.delete"));
                alert.createAlert();
            } else {
                AllowDcsManualCollectionRangeSaveTask task = new AllowDcsManualCollectionRangeSaveTask(propManualRequestDto.get(), (short) 1);
                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("allowdcsmanualcollectionrange"),
                        resourceBundle.getString("alert.cancel"));
                Optional<ButtonType> resp = alert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    task.setOnSucceeded(e1 -> {
                        cboxStatus.getSelectionModel().select(2);
                        loadData();
                    });
                    new Thread(task).start();
                }
            }
        });
    }

    @Override
    public void setupComboBox() {  //resourceBundle.getString("ShiftLock"), resourceBundle.getString("ManualCollection")
        cboxType.getItems().addAll(resourceBundle.getString("shiftlock"), resourceBundle.getString("manualcollection"));
        cboxType.getSelectionModel().select(0);
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
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
            colFromShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromShift()));
            colToShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToShift()));
            colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getxCol1().equalsIgnoreCase("0") ? resourceBundle.getString("shiftlock") : resourceBundle.getString("manualcollection")));
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate().toLocalDate()));
            colFromDate.setCellFactory(new LocalDateCellFactory<>());
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate().toLocalDate()));
            colToDate.setCellFactory(new LocalDateCellFactory<>());
            colRemarks.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRemarks()));
            colStatus.setCellValueFactory(data -> new SimpleObjectProperty(CommonUtils.getRequestStatus(data.getValue().getStatus())));
            colIsWeight.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWeightManual() ? resourceBundle.getString("yes") : resourceBundle.getString("no")));
            colIsQuality.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getQualityManual() ? resourceBundle.getString("yes") : resourceBundle.getString("no")));
            propManualRequestDto.bind(tableManualRequest.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("ManualRequest setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void saveData() {
        var task1 = new AllowDcsManualCollectionDateShiftValidationLoadTask(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()), CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()), cboxType.getSelectionModel().getSelectedIndex(), chkIsWeightManual.isSelected(), chkIsQualityManual.isSelected());
        task1.setOnSucceeded(e -> {
            try {
                if (task1.get()) {
                    var task = new AllowDcsManualCollectionRangeSaveTask(manualRequest, (short) 0);
                    task.setOnSucceeded(e1 -> {
                        try {
                            try {
                                Object obj = task.get();
                                if (obj instanceof ApiError) {
                                    ApiError error = (ApiError) obj;
                                    StringBuilder sb = new StringBuilder();
                                    for (ApiValidationError subError : error.getSubErrors()) {
                                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                                    }
                                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("allowdcsmanualcollectionrange"),
                                            sb.toString());
                                    alert.createAlert();
                                    return;
                                }
                                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("allowdcsmanualcollectionrange"),
                                        resourceBundle.getString("allowdcsmanualcollectionrange.insert.successful"));
                                alert.createAlert();
                                loadData();
                                clearControls();
                            } catch (InterruptedException | ExecutionException ex) {
                                ex.printStackTrace();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(task).start();
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milk.collection.request"),
                            resourceBundle.getString("milk.collection.request.can.not.send"));
                    alert.createAlert();
                    clearControls();
                    return;
                }
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task1).start();

    }

    private void setValuesInObject() {
        manualRequest = new AllowDcsManualCollectionRange();
        manualRequest.setSociety(MainApp.identityDto.getSociety());
        manualRequest.setUnionCode(MainApp.identityDto.getUnion().getCode());
        manualRequest.setToDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()));
        manualRequest.setFromDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()));
        manualRequest.setToShift(cboxToShift.getValue());
        manualRequest.setFromShift(cboxFromShift.getValue());
        manualRequest.setWeightManual(chkIsWeightManual.isSelected());
        manualRequest.setQualityManual(chkIsQualityManual.isSelected());
        manualRequest.setRemarks(txtRemarks.getText());
        manualRequest.setxCol1(String.valueOf(cboxType.getSelectionModel().getSelectedIndex()));
        manualRequest.setStatus(1);
    }


    @Override
    public void loadData() {
        tableManualRequest.setItems(null);

        AllowDcsManualCollectionRangeLoadTask task = new AllowDcsManualCollectionRangeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<AllowDcsManualCollectionRange> list = task.get();
                if (list != null)
                    tableManualRequest.setItems(FXCollections.observableList(list));
                cboxStatus.getSelectionModel().select(0);

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new ShiftLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Shift> list = task1.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    cboxFromShift.setItems(FXCollections.observableList(list1));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.setItems(FXCollections.observableList(list1));
                    cboxToShift.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

    @Override
    public void clearControls() {
        txtRemarks.setText("");
        chkIsQualityManual.setSelected(true);
        chkIsWeightManual.setSelected(true);
        dpToDate.setValue(LocalDate.now());
        dpFromDate.setValue(LocalDate.now());
        cboxFromShift.getSelectionModel().select(0);
        cboxToShift.getSelectionModel().select(0);
        cboxType.getSelectionModel().select(0);
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }

    public void setType(int i) {
        cboxType.getSelectionModel().select(i);
        if (i == 0) {
            dpToDate.setDisable(true);
            cboxToShift.setDisable(true);
            chkIsQualityManual.setDisable(true);
            chkIsWeightManual.setDisable(true);
        } else {
            dpToDate.setDisable(false);
            cboxToShift.setDisable(false);
            chkIsQualityManual.setDisable(false);
            chkIsWeightManual.setDisable(false);
        }
    }
}
