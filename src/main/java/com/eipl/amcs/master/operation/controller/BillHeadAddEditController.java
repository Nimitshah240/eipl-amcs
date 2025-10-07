package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.repository.FormulaRepository;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import com.eipl.amcs.master.operation.service.BillHeadService;
import com.eipl.amcs.util.CommonUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

/**
 * This class acts as a controller for BillHead Add-Edit Popup.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillHeadAddEditController implements MyInitialization {

    private Stage stage;
    private PopupCallback callback;
    @FXML
    private StackPane root;

    @FXML
    private ComboBox cboxDisbursment;

    @FXML
    private Button btnClose, btnSaveUpdate;

    private BillHead billHead = null;

    @FXML
    private E_TextField txtCode, txtName, txtLocalName;

    private ResourceBundle resourceBundle;

    @FXML
    private CheckBox chkStatus;

    private StringBuilder errorMsg = null;

    private BillHeadService billHeadService;
    private FormulaRepository formulaRepository;
    private NextCodeService nextCodeService;
    private BillCriteriaService billCriteriaService;


    public BillHeadAddEditController() {
        billHeadService = context.getBean(BillHeadService.class);
        formulaRepository = context.getBean(FormulaRepository.class);
        nextCodeService = context.getBean(NextCodeService.class);
        billCriteriaService = context.getBean(BillCriteriaService.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.resourceBundle = resourceBundle;
            setupComboBox();
            btnClose.setOnAction(e -> this.stage.close());
            btnSaveUpdate.setOnAction(e -> saveUpdateBillHead());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }


    @Override
    public void setupComboBox() {
        try {
            cboxDisbursment.getItems().addAll(resourceBundle.getString("yes"), resourceBundle.getString("no"));
            cboxDisbursment.getSelectionModel().select(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBillHead(BillHead billHead) {
        try {
            if (billHead != null) {
                this.billHead = billHead;
                btnSaveUpdate.setText(resourceBundle.getString("update"));
                loadBillHead();
            } else {
                getNextBillHeadCode();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBillHead() {
        try {
            txtCode.setText(billHead.getCode());
            txtName.setText(billHead.getName());
            txtLocalName.setText(billHead.getNameLocal());
            cboxDisbursment.setValue(billHead.getDisburseAllowed() ? resourceBundle.getString("yes") : resourceBundle.getString("no"));
            chkStatus.setSelected(billHead.isActive());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getNextBillHeadCode() {
        try {
            String nextCode = nextCodeService.getNextCode("BillHead", "code", MainApp.identityDto.getSociety().getCode(), 3);
            if (nextCode == null || nextCode.isEmpty())
                return;
            txtCode.setText(nextCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BillHead setValuesInObject() {
        try {
            billHead.setCode(txtCode.getText());
            billHead.setName(txtName.getText());
            billHead.setNameLocal(txtLocalName.getText());
            billHead.setDisburseAllowed(cboxDisbursment.getValue().toString().equalsIgnoreCase(resourceBundle.getString("yes")));
            billHead.setActive(chkStatus.isSelected());
            billHead.setDefaultHead(false);
            billHead.setSociety(MainApp.identityDto.getSociety());
            billHead.setUnion(MainApp.identityDto.getUnion());
            billHead.setAllowAdjustment((short) 1);
            return billHead;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUpdateBillHead() {
        try {
            errorMsg = new StringBuilder();
            if (!validate()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                        errorMsg.toString());
                alert.createAlert();
                return;
            }

            if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
                if (this.billHead != null) {
                    billHead = setValuesInObject();
                    updateData();
                }
            } else {
                billHead = new BillHead();
                billHead = setValuesInObject();
                saveData();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private boolean validate() {
        try {
            if (txtName.getText() == null || txtName.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("billheadnamenullerror") + "\n");
            return errorMsg.length() == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveData() {
        try {
            billHeadService.saveBillHead(billHead, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                    resourceBundle.getString("billhead.insert.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateData() {
        try {
            billHeadService.updateBillHead(billHead, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                    resourceBundle.getString("billhead.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
