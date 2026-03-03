package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.task.BillHeadDeleteTask;
import com.eipl.amcs.master.operation.task.BillHeadLoadTask;
import com.eipl.amcs.utils.CommonUtils;
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
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class BillHeadController implements MyInitialization, PopupCallback {
    private final ObjectProperty<BillHead> propBillHeadDto;
    @FXML
    StackPane root;
    @FXML
    TableView<BillHead> tableBillHead;
    @FXML
    TableColumn<BillHead, String> colCode, colName, colDefault, colDisburseAllowed, colStatus;
    @FXML
    TableColumn<BillHead, String> colLocalName;
    @FXML
    Button btnAdd, btnEdit, btnClose, btnDelete;
    private ResourceBundle resourceBundle;

    public BillHeadController() {
        this.propBillHeadDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.resourceBundle = resourceBundle;
            setupTable();
            loadData();

            btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

            propBillHeadDto.addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (!newValue.getDefaultHead()) {
                        btnEdit.setDisable(false);
                        btnDelete.setDisable(false);
                    } else {
                        btnEdit.setDisable(true);
                        btnDelete.setDisable(true);
                    }
                } else {
                    btnEdit.setDisable(true);
                    btnDelete.setDisable(true);
                }
            });

            btnAdd.setOnAction(e -> MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "BillHeadAddEdit", null, this));

            btnEdit.setOnAction(e -> {
                if (propBillHeadDto.get().getCreatedBy() == null || propBillHeadDto.get().getCreatedBy().equalsIgnoreCase("SYSTEM")) {
                    if (!MainApp.user.getPermissions().contains("ACTION_BILL_HEAD_EDIT"))
                        throw new UnAuthorizedAccessException();
                    BillHead dto = propBillHeadDto.get();
                    if (dto != null)
                        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "BillHeadAddEdit", dto, this);
                }
            });

            btnDelete.setOnAction(e -> {
                if (propBillHeadDto.get().getCreatedBy() == null || propBillHeadDto.get().getCreatedBy().equalsIgnoreCase("SYSTEM")) {
                    if (!MainApp.user.getPermissions().contains("ACTION_BILL_HEAD_DELETE"))
                        throw new UnAuthorizedAccessException();
                    deleteData();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colDefault.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getResourceString(resourceBundle, data.getValue().getDefaultHead() ? "yes" : "no")));
            colDisburseAllowed.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getResourceString(resourceBundle, data.getValue().getDisburseAllowed() ? "yes" : "no")));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getResourceString(resourceBundle, data.getValue().isActive() ? "active" : "inactive")));
            propBillHeadDto.bind(tableBillHead.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("BillHead setuptable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        tableBillHead.setItems(null);
        var task = new BillHeadLoadTask("MEMBER");
        task.setOnSucceeded(e -> {
            try {
                List<BillHead> list = task.get();
                if (list != null) tableBillHead.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                BillHead billHead = propBillHeadDto.get();
                if (billHead != null) {
                    var task = new BillHeadDeleteTask(billHead.getCode());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                                        resourceBundle.getString("error.occurred"));
                                alert1.createAlert();
                                return;
                            }
                            loadData();
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(task).start();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag) loadData();
    }
}
