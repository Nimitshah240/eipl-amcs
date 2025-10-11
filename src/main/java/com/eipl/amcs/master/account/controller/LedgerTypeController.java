package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.service.LedgerTypeService;
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

public class LedgerTypeController implements MyInitialization, PopupCallback {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerType> tableLedgerType;
    @FXML
    TableColumn<LedgerType, Number> colCode;
    @FXML
    TableColumn<LedgerType, String> colName, colLocalName, colStatus;
    @FXML
    TableColumn<LedgerType, String> colProfitAndLoss, colBalanceSheet;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete;

    private ResourceBundle resourceBundle;
    private LedgerTypeService ledgerTypeService;

    @Override
    public Node getRoot() {
        return root;
    }

    private final ObjectProperty<LedgerType> propLedgerType;

    public LedgerTypeController() {
        ledgerTypeService = context.getBean(LedgerTypeService.class);
        propLedgerType = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propLedgerType.addListener((observable, oldValue, newValue) -> {
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
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerTypeAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            LedgerType dto = propLedgerType.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerTypeAddEdit", dto, this);
        });
    }

    @Override
    public void setupTable() {
        colCode.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCode()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
        colProfitAndLoss.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isProfitLoss() ?
                resourceBundle.getString("yes") : resourceBundle.getString("no")));
        colBalanceSheet.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isBalanceSheet() ?
                resourceBundle.getString("yes") : resourceBundle.getString("no")));
        propLedgerType.bind(tableLedgerType.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void loadData() {
        try {
            tableLedgerType.setItems(null);
            List<LedgerType> list = ledgerTypeService.findAll();
            if (list != null)
                tableLedgerType.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            LedgerType dto = propLedgerType.get();
            if (dto != null) {
                try {
                    ledgerTypeService.delete(dto.getCode(), CommonUtil.setIdentityHeader());
                    loadData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledgertype"),
                            resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
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
