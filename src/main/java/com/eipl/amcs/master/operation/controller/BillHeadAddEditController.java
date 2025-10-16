package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.task.BillHeadNumberLoadTask;
import com.eipl.amcs.master.operation.task.BillHeadSaveTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

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


    /**
     * Method set comboBox of disbursement with default no.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void setupComboBox() {
        try {
            cboxDisbursment.getItems().addAll(resourceBundle.getString("yes"), resourceBundle.getString("no"));
            cboxDisbursment.getSelectionModel().select(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method set bill head value if updating and
     * setting new code on creating new bill head.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method set bill head value in respective field to show in the frontend.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method gets the new code from the backend for creating new bill head.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    private void getNextBillHeadCode() {
        try {
            var task = new BillHeadNumberLoadTask(MainApp.identityDto.getSociety().getCode());
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
     * Method set values in the bill head object from the front end.
     *
     * @return BillHead
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method first validate fields, then create or update bill head
     * depending on the this.billHead is null or not respectively.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
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

    /**
     * Method checks name is not null.
     *
     * @return boolean
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    private boolean validate() {
        try {
            if (txtName.getText() == null || txtName.getText().trim().isEmpty())
                errorMsg.append(resourceBundle.getString("billheadnamenullerror") + "\n");
            return errorMsg.length() == 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method calls BillHeadSaveTask with short 0 to create the bill head.
     * Then close the popup and reload the data.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void saveData() {
        try {
            var task = new BillHeadSaveTask(billHead, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                                sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                            resourceBundle.getString("billhead.insert.successful"));
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
     * Method calls BillHeadSaveTask with short 1 to update the bill head.
     * Then close the popup and reload the data.
     *
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void updateData() {
        try {
            var task = new BillHeadSaveTask(billHead, (short) 1);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                                sb.toString());
                        alert.createAlert();
                        return;
                    }

                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("billhead"),
                            resourceBundle.getString("billhead.update.successful"));
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
