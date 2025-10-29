package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.operation.convertor.BillHeadConvertor;
import com.eipl.amcs.master.operation.convertor.FormulaConvertor;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.task.BillCriteriaNumberLoadTask;
import com.eipl.amcs.master.operation.task.BillCriteriaSaveTask;
import com.eipl.amcs.master.operation.task.BillHeadLoadTask;
import com.eipl.amcs.master.operation.task.FormulaLoadTask;
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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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


    /**
     * Method set action on btn close,saveUpdate.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method set comboBox of formula and bill head.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void setupComboBox() {
        try {
            cboxFormula.setConverter(new FormulaConvertor(cboxFormula));
            cboxBillHead.setConverter(new BillHeadConvertor(cboxBillHead));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method get Bill head and set in the comboBox of bill head.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    private void loadBillHead() {
        var task = new BillHeadLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<BillHead> list = task.get();
                if (list != null) {
                    List<BillHead> filteredList = list.stream()
                            .filter(b -> !Boolean.TRUE.equals(b.getDefaultHead())) // Only non-default heads
                            .collect(Collectors.toList());

                    cboxBillHead.setItems(FXCollections.observableList(filteredList));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    /**
     * Method get Formula and set in the comboBox of formula.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    private void loadFormula() {
        try {
            var task = new FormulaLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Formula> list = task.get();
                    if (list != null) {
                        List<Formula> filteredList = list.stream()
                                .filter(b -> b.getType().equals("2")) // Get type=2 for bill head use.
                                .collect(Collectors.toList());
                        cboxFormula.setItems(FXCollections.observableList(filteredList));
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method set bill criteria value if updating and
     * setting new code on creating new bill criteria.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method set bill criteria value in respective field to show.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method gets the new code from the backend for creating new bill criteria.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    private void getNextBillCriteriaCode() {
        try {
            var task = new BillCriteriaNumberLoadTask(MainApp.identityDto.getSociety().getCode());
            task.setOnSucceeded(e -> {
                try {
                    String nextCode = task.get();
                    if (nextCode == null || nextCode.isEmpty())
                        return;
                    txtCode.setText(nextCode);
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method set values in the bill criteria object from the front end.
     *
     * @return BillCriteria
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method first validate fields, then create or update bill criteria
     * depending on the this.billCriteria is null or not respectively.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method checks name and billhead, startdate and formula is not null.
     *
     * @return boolean
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method calls BillCriteriaSaveTask with short 0 to create the bill criteria.
     * Then close the popup and reload the data.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void saveData() {
        try {
            var task = new BillCriteriaSaveTask(billCriteria, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                                sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                            resourceBundle.getString("billcriteria.insert.successful"));
                    alert.createAlert();
                    this.callback.reloadData(true);
                    this.stage.close();

                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method calls BillCriteriaSaveTask with short 1 to update the bill criteria.
     * Then close the popup and reload the data.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void updateData() {
        try {
            var task = new BillCriteriaSaveTask(billCriteria, (short) 1);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                                sb.toString());
                        alert.createAlert();
                        return;
                    }

                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billcriteria"),
                            resourceBundle.getString("billcriteria.update.successful"));
                    alert.createAlert();
                    this.callback.reloadData(true);
                    this.stage.close();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
