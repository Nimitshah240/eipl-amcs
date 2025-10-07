package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.convertor.BillHeadConvertor;
import com.eipl.amcs.master.operation.convertor.FormulaConvertor;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.repository.FormulaRepository;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
import com.eipl.amcs.master.operation.service.BillHeadService;
import com.eipl.amcs.util.CommonUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.eipl.amcs.MainApp.context;

/**
 * This class acts as a controller for BillCriteria Add-Edit Popup.
 *
 * @author Nimit Shah
 * @createdOn 30-06-2025
 */
public class BillCriteriaAddEditController implements MyInitialization {

    private Stage stage;
    private PopupCallback callback;
    @FXML
    private StackPane root;

    private ResourceBundle resourceBundle;

    private StringBuilder errorMsg = null;

    @FXML
    private E_TextField txtCode, txtName;

    @FXML
    private ComboBox<BillHead> cboxBillHead;

    @FXML
    private ComboBox<Formula> cboxFormula;

    @FXML
    private E_DatePicker dpStartDate, dpEndDate;

    @FXML
    private CheckBox chkStatus;

    @FXML
    private Button btnClose, btnSaveUpdate;

    private BillCriteria billCriteria = null;


    private BillHeadService billHeadService;
    private FormulaRepository formulaRepository;
    private NextCodeService nextCodeService;
    private BillCriteriaService billCriteriaService;

    public BillCriteriaAddEditController() {
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
            btnSaveUpdate.setOnAction(e -> saveUpdateBillCriteria());
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
            cboxFormula.setConverter(new FormulaConvertor(cboxFormula));
            cboxBillHead.setConverter(new BillHeadConvertor(cboxBillHead));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadBillHead() {
        try {
            List<BillHead> list = billHeadService.findAll();
            if (list != null) {
                List<BillHead> filteredList = list.stream()
                        .filter(b -> !Boolean.TRUE.equals(b.getDefaultHead())) // Only non-default heads
                        .collect(Collectors.toList());

                cboxBillHead.setItems(FXCollections.observableList(filteredList));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadFormula() {
        try {
            List<Formula> list = formulaRepository.findAll();
            if (list != null) {
                List<Formula> filteredList = list.stream()
                        .filter(b -> Boolean.TRUE.equals(b.getType().equals("2"))) // Get type=2 for bill head use.
                        .collect(Collectors.toList());
                cboxFormula.setItems(FXCollections.observableList(filteredList));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBillCriteria(BillCriteria billCriteria) {
        try {
            if (billCriteria != null) {
                this.billCriteria = billCriteria;
                btnSaveUpdate.setText(resourceBundle.getString("update"));
                loadBillCriteria();
            } else {
                getNextBillCriteriaCode();
            }
            loadBillHead();
            loadFormula();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBillCriteria() {
        try {
            txtCode.setText(billCriteria.getCode());
            txtName.setText(billCriteria.getCriteria());
            chkStatus.setSelected(billCriteria.isActive());
            dpStartDate.setValue(billCriteria.getStartDate());
            dpEndDate.setValue(billCriteria.getEndDate());
            cboxBillHead.getSelectionModel().select(billCriteria.getBillHeadCode());
            cboxFormula.getSelectionModel().select(billCriteria.getFormulaCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void getNextBillCriteriaCode() {
        try {
            String nextCode = nextCodeService.getNextCode("BillCriteria", "code", MainApp.identityDto.getSociety().getCode(), 3);
            if (nextCode == null || nextCode.isEmpty())
                return;
            txtCode.setText(nextCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BillCriteria setValuesInObject() {
        try {
            billCriteria.setCode(txtCode.getText());
            billCriteria.setCriteria(txtName.getText());
            billCriteria.setStartDate(dpStartDate.getValue());
            billCriteria.setEndDate(dpEndDate.getValue());
            billCriteria.setBillHeadCode(cboxBillHead.getValue());
            billCriteria.setActive(chkStatus.isSelected());
            billCriteria.setFormulaCode(cboxFormula.getValue());
            billCriteria.setSociety(MainApp.identityDto.getSociety());
            billCriteria.setUnion(MainApp.identityDto.getUnion());
            return billCriteria;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUpdateBillCriteria() {
        try {
            errorMsg = new StringBuilder();
            if (!validate()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                        errorMsg.toString());
                alert.createAlert();
                return;
            }

            if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
                if (this.billCriteria != null) {
                    billCriteria = setValuesInObject();
                    updateData();
                }
            } else {
                billCriteria = new BillCriteria();
                billCriteria = setValuesInObject();
                saveData();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private boolean validate() {
        try {
            if (txtName.getText() == null || txtName.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("billcriterianamenullerror") + "\n");
            if (cboxBillHead.getValue() == null)
                errorMsg.append(resourceBundle.getString("billcriteriabillheadnullerror") + "\n");
            if (dpStartDate.getValue() == null)
                errorMsg.append(resourceBundle.getString("billcriteriastartdatenullerror") + "\n");
            if (dpEndDate.getValue() == null)
                errorMsg.append(resourceBundle.getString("billcriteriaenddatenullerror") + "\n");
            if (cboxFormula.getValue() == null)
                errorMsg.append(resourceBundle.getString("billcriteriaformulanullerror") + "\n");
            if (dpStartDate.getValue().isAfter(dpEndDate.getValue() != null ? dpEndDate.getValue() : LocalDate.now()))
                errorMsg.append(resourceBundle.getString("startdatesmallerthanenddate") + "\n");

            return errorMsg.length() == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveData() {
        try {
            billCriteriaService.saveBillCriteria(billCriteria, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                    resourceBundle.getString("billcriteria.insert.successful"));
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
            billCriteriaService.updateBillCriteria(billCriteria, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                    resourceBundle.getString("billcriteria.update.successful"));
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
