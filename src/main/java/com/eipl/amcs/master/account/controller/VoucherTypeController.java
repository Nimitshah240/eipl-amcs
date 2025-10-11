package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import com.eipl.amcs.util.CommonUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class VoucherTypeController implements MyInitialization, PopupCallback {

    @FXML
    AnchorPane root;
    @FXML
    TableView<VoucherType> tableVoucherType;
    @FXML
    TableColumn<VoucherType, Number> colCode;
    @FXML
    TableColumn<VoucherType, String> colName, colLocalName, colStatus;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete;

    private ResourceBundle resourceBundle;
    private VoucherTypeService voucherTypeService;

    @Override
    public Node getRoot() {
        return root;
    }

    private final ObjectProperty<VoucherType> propVoucherType;

    public VoucherTypeController() {
        voucherTypeService = context.getBean(VoucherTypeService.class);
        propVoucherType = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propVoucherType.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        setupTable();
        loadData();
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherTypeAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            VoucherType dto = propVoucherType.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "VoucherTypeAddEdit", dto, this);
        });
    }

    @Override
    public void setupTable() {
        colCode.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCode()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ?
                resourceBundle.getString("active") : resourceBundle.getString("inactive")));
        propVoucherType.bind(tableVoucherType.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void loadData() {
        try {
            tableVoucherType.setItems(null);
            List<VoucherType> list = voucherTypeService.findAll();
            if (list != null)
                tableVoucherType.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            VoucherType dto = propVoucherType.get();
            if (dto != null) {
                try {
                    voucherTypeService.delete(dto.getCode(), CommonUtil.setIdentityHeader());
                    loadData();
                } catch (Exception ex) {
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("vouchertype"),
                            resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
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
