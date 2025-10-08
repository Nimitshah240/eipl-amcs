package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import com.eipl.amcs.util.CommonUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class LocalMilkSaleRateController implements MyInitialization, PopupCallback {
    @FXML
    StackPane root;
    @FXML
    TableView<LocalMilkSaleRate> tableLocalMilkSaleRates;
    @FXML
    TableColumn<LocalMilkSaleRate, MilkType> colMilkType;
    @FXML
    TableColumn<LocalMilkSaleRate, MilkClass> colMilkClass;
    @FXML
    TableColumn<LocalMilkSaleRate, Number> colRate;
    @FXML
    TableColumn<LocalMilkSaleRate, LocalDate> colWefDate;
    @FXML
    Button btnClose, btnAdd, btnDelete;

    private ResourceBundle resourceBundle;
    private ObjectProperty<LocalMilkSaleRate> propLocalMilkSaleRate;
    private LocalMilkSaleRateService localMilkSaleRateService;

    public LocalMilkSaleRateController() {
        propLocalMilkSaleRate = new SimpleObjectProperty<>();
        localMilkSaleRateService = context.getBean(LocalMilkSaleRateService.class);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadData();
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LOCAL_MILK_SALE_RATE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LocalMilkSaleRateAddEdit", null, this);
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LOCAL_MILK_SALE_RATE_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
    }

    @Override
    public void setupTable() {
        try {
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate()));
            colWefDate.setCellFactory(new LocalDateCellFactory<>());
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colMilkClass.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkClass()));
            propLocalMilkSaleRate.bind(tableLocalMilkSaleRates.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableLocalMilkSaleRates.setItems(null);
        try {
            List<LocalMilkSaleRate> list = localMilkSaleRateService.findAll();
            if (list != null)
                tableLocalMilkSaleRates.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            LocalMilkSaleRate dto = propLocalMilkSaleRate.get();
            if (dto != null) {
                try {
                    Optional<LocalMilkSaleRate> localMilkSaleRateData = localMilkSaleRateService.findById(dto.getCode());
                    if (localMilkSaleRateData == null || !localMilkSaleRateData.isPresent())
                        return;
                    localMilkSaleRateService.delete(localMilkSaleRateData.get(), CommonUtil.setIdentityHeader());
                    loadData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
