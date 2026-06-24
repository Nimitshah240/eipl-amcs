package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkClassConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkClassLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.task.LocalMilkSaleRateSaveTask;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LocalMilkSaleRateAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private ComboBox<MilkClass> cboxMilkClass;
    @FXML
    private DatePicker dpWefDate;
    @FXML
    private E_NumericField txtRate;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private LocalMilkSaleRate localMilkSaleRate = null;
    private BigDecimal rate;

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

        setupComboBox();
        loadData();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    @Override
    public void setupComboBox() {
        dpWefDate.setConverter(new LocalDateConvertor());
        dpWefDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpWefDate.setValue(dpWefDate.getConverter().fromString(dpWefDate.getEditor().getText()));
            }
        });
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkClass.setConverter(new MilkClassConvertor(cboxMilkClass));
    }

    @Override
    public void loadData() {
        loadMilkClass();
        loadMilkTypes();
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        localMilkSaleRate = new LocalMilkSaleRate();
        setValuesInObject();
        saveData();
    }

    private void setValuesInObject() {
        BigDecimal b = new BigDecimal(txtRate.getInputText());
        BigDecimal a = b.setScale(2, RoundingMode.HALF_EVEN);
        System.out.println(a);
        localMilkSaleRate.setRate(a);
        localMilkSaleRate.setMilkClass(cboxMilkClass.getValue());
        localMilkSaleRate.setMilkType(cboxMilkType.getValue());
        localMilkSaleRate.setWefDate(dpWefDate.getValue());
        localMilkSaleRate.setUnionCode(MainApp.identityDto.getUnion().getCode());
        localMilkSaleRate.setSociety(MainApp.identityDto.getSociety());
    }

    private boolean validate() {
        if (dpWefDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("wefdatenullerror") + "\n");
        try {
            if (Double.parseDouble(txtRate.getInputText()) <= 0 || Double.parseDouble(txtRate.getInputText()) >= 1000)
                errorMsg.append(resourceBundle.getString("entervalidrate") + "\n");
        } catch (NumberFormatException e) {
            errorMsg.append(resourceBundle.getString("localmilksaleratenullerror") + "\n");
        }
        if (cboxMilkClass.getValue() == null)
            errorMsg.append(resourceBundle.getString("milkclassnullerror") + "\n");

        if (cboxMilkType.getValue() == null)
            errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new LocalMilkSaleRateSaveTask(localMilkSaleRate, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                        resourceBundle.getString("localmilksalerate.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new LocalMilkSaleRateSaveTask(localMilkSaleRate, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                        resourceBundle.getString("localmilksalerate.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMilkTypes() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                }
                cboxMilkType.getSelectionModel().select(0);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMilkClass() {
        var task = new MilkClassLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkClass> list = task.get();
                if (list != null)
                    cboxMilkClass.setItems(FXCollections.observableList(list));
                cboxMilkClass.getSelectionModel().select(0);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }
}
