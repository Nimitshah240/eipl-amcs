package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import com.eipl.amcs.util.CommonUtil;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class BillCriteriaController implements MyInitialization, PopupCallback {
    @FXML
    StackPane root;
    @FXML
    TableView<BillCriteria> tableBillCriteria;
    @FXML
    TableColumn<BillCriteria, String> colCode, colCriteria, colFormula, colStatus, colBillHead;
    @FXML
    TableColumn<BillCriteria, LocalDate> colStartDate, colEndDate;

    @FXML
    Button btnAdd, btnEdit, btnClose, btnDelete;

    private ResourceBundle resourceBundle;

    private final ObjectProperty<BillCriteria> propBillCriteriaDto;

    private BillCriteriaService service;
    private NextCodeService nextCodeService;

    public BillCriteriaController() {
        this.propBillCriteriaDto = new SimpleObjectProperty<>();
        service = context.getBean(BillCriteriaService.class);
        nextCodeService = context.getBean(NextCodeService.class);
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
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        propBillCriteriaDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnAdd.setOnAction(e -> MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "BillCriteriaAddEdit", null, this));

        btnEdit.setOnAction(e -> {
            if (propBillCriteriaDto.get().getCreatedBy() == null || propBillCriteriaDto.get().getCreatedBy().equalsIgnoreCase("SYSTEM")) {
                if (!MainApp.user.getPermissions().contains("ACTION_BILL_CRITERIA_EDIT"))
                    throw new UnAuthorizedAccessException();
                BillCriteria dto = propBillCriteriaDto.get();
                if (dto != null)
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "BillCriteriaAddEdit", dto, this);
            }
        });

        btnDelete.setOnAction(e -> {
            if (propBillCriteriaDto.get().getCreatedBy() == null || propBillCriteriaDto.get().getCreatedBy().equalsIgnoreCase("SYSTEM")) {
                if (!MainApp.user.getPermissions().contains("ACTION_BILL_HEAD_DELETE"))
                    throw new UnAuthorizedAccessException();
                deleteData();
            }
        });
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colCriteria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCriteria()));
            colFormula.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormulaCode().getName()));
            colBillHead.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBillHeadCode().getName()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getResourceString(resourceBundle, data.getValue().isActive() ? "active" : "inactive")));
            colStartDate.setCellValueFactory(data -> new SimpleObjectProperty<LocalDate>(data.getValue().getStartDate()));
            colEndDate.setCellValueFactory(data -> new SimpleObjectProperty<LocalDate>(data.getValue().getEndDate()));
            propBillCriteriaDto.bind(tableBillCriteria.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            tableBillCriteria.setItems(null);
            List<BillCriteria> list = service.findAll();
            if (list != null)
                tableBillCriteria.setItems(FXCollections.observableList(list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData() {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                BillCriteria billCriteria = propBillCriteriaDto.get();
                if (billCriteria != null) {
                    service.delete(billCriteria.getCode(), CommonUtil.setIdentityHeader());
                    loadData();
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
