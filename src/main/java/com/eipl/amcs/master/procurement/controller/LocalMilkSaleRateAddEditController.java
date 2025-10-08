package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkClassConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.service.MilkClassService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import com.eipl.amcs.util.CommonUtil;
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

import static com.eipl.amcs.MainApp.context;

public class LocalMilkSaleRateAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
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
    private LocalMilkSaleRate dto = null;
    private LocalMilkSaleRate localMilkSaleRate = null;
    private BigDecimal rate;
    private LocalMilkSaleRateService localMilkSaleRateService;
    private MilkTypeService milkTypeService;
    private MilkClassService milkClassService;

    public LocalMilkSaleRateAddEditController() {
        localMilkSaleRateService = context.getBean(LocalMilkSaleRateService.class);
        milkTypeService = context.getBean(MilkTypeService.class);
        milkClassService = context.getBean(MilkClassService.class);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLocalMilkSaleRate(LocalMilkSaleRate localMilkSaleRate) {
        this.localMilkSaleRate = localMilkSaleRate;
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
        BigDecimal b = new BigDecimal(txtRate.getText());
        BigDecimal a = b.setScale(2, RoundingMode.HALF_EVEN);
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
            if (Double.parseDouble(txtRate.getText()) <= 0 || Double.parseDouble(txtRate.getText()) >= 1000)
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
        try {
            localMilkSaleRateService.save(localMilkSaleRate, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                    resourceBundle.getString("localmilksalerate.insert.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateData() {
        try {
            localMilkSaleRateService.update(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                    resourceBundle.getString("localmilksalerate.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadMilkTypes() {
        try {
            List<MilkType> list = milkTypeService.findAll();
            if (list != null) {
                cboxMilkType.setItems(FXCollections.observableList(list));
            }
            cboxMilkType.getSelectionModel().select(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadMilkClass() {
        try {
            List<MilkClass> list = milkClassService.findAll();
            if (list != null)
                cboxMilkClass.setItems(FXCollections.observableList(list));
            cboxMilkClass.getSelectionModel().select(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }
}
