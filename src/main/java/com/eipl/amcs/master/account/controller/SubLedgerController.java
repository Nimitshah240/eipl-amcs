package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
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

public class SubLedgerController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<SubLedger> tableSubLedger;
    @FXML
    TableColumn<SubLedger, String> colCode, colName, colLocalName, colStatus;
    @FXML
    TableColumn<SubLedger, String> colType;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit;
    private ResourceBundle resourceBundle;

    private final ObjectProperty<SubLedger> propSubLedger;
    private SubLedgerService subLedgerService;

    public SubLedgerController() {
        subLedgerService = context.getBean(SubLedgerService.class);
        propSubLedger = new SimpleObjectProperty<>();
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
        FocusUtils.requestFocus(btnAdd);
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAdd.setOnAction(e -> {

            SubLedgerAddEditController controller = (SubLedgerAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/SubLedgerAddEdit.fxml"));
            controller.setSubLedger(null);
            MainApp.getContentPane().setCenter(controller.getRoot());
            //  MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/account/LedgerAddEdit.fxml")));

        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            if (propSubLedger.get() != null) {
                SubLedgerAddEditController controller = (SubLedgerAddEditController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/master/account/SubLedgerAddEdit.fxml"));
                controller.setSubLedger(propSubLedger.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            }
        });
        propSubLedger.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getCustomerTypeStrFromShort(data.getValue().getType())));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));

            propSubLedger.bind(tableSubLedger.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
        }
    }

    @Override
    public void loadData() {
        try {
            tableSubLedger.setItems(null);
            List<SubLedger> list = subLedgerService.findAll();
            if (list != null)
                tableSubLedger.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("subledger"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            SubLedger dto = propSubLedger.get();
            if (dto != null) {
                try {
                    subLedgerService.delete(dto.getCode(), CommonUtil.setIdentityHeader());
                    loadData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("subledger"),
                            resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
                }
            }
        }
    }
}
