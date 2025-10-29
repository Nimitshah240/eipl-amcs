package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.task.ShareRateSaveTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ShareRateAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private DatePicker dpWefDate;
    @FXML
    private E_NumericField txtRate;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private final ShareRate dto = null;
    private ShareRate shareRate = null;
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
        dpWefDate.setValue(LocalDate.now());
    }

    @Override
    public void loadData() {
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharerate"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        shareRate = new ShareRate();
        setValuesInObject();
        saveData();
    }

    private void setValuesInObject() {
        BigDecimal b = new BigDecimal(txtRate.getText());
        BigDecimal a = b.setScale(2, RoundingMode.HALF_EVEN);
        shareRate.setShareAmount(a);
        shareRate.setWefDate(dpWefDate.getValue());
        shareRate.setUnionCode(MainApp.identityDto.getUnion().getCode());
        shareRate.setSociety(MainApp.identityDto.getSociety());
    }

    private boolean validate() {
        if (dpWefDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("wefdatenullerror") + "\n");
        try {
            if (Double.parseDouble(txtRate.getText()) <= 0 || Double.parseDouble(txtRate.getText()) >= 1000)
                errorMsg.append(resourceBundle.getString("entervalidrate") + "\n");
        } catch (NumberFormatException e) {
            errorMsg.append(resourceBundle.getString("sharerate") + "\n");
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new ShareRateSaveTask(shareRate, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharerate"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("sharerate"),
                        resourceBundle.getString("sharerate.insert.successful"));
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
        var task = new ShareRateSaveTask(shareRate, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharerate"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("sharerate"),
                        resourceBundle.getString("sharerate.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

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
