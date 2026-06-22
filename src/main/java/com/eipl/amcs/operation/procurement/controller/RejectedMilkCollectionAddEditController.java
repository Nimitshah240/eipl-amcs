package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.task.RejectedMilkCollectionSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class RejectedMilkCollectionAddEditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private AutoSearchTextField<Shift> cboxShift;
    @FXML
    private AutoSearchTextField<MilkType> cboxMilkType;
    @FXML
    private E_DatePicker dpDate;
    @FXML
    private E_TextField txtCode, txtName, txtFat, txtRemarks, txtQty, txtSnf;
    @FXML
    private E_Button btnSaveUpdate, btnClose;

    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private RejectedMilkCollection dto = null;
    private PopupCallback callback;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setRejectedMilkCollection(RejectedMilkCollection dto) {
        if (dto != null) {
            this.dto = dto;
            if (dto.getMilkCollectionRejectedCode() != null && !dto.getMilkCollectionRejectedCode().trim().isEmpty()) {
                btnSaveUpdate.setText(resourceBundle.getString("update"));
            } else {
                btnSaveUpdate.setText(resourceBundle.getString("add"));
            }
            loadControls();
        } else {
            clearControls();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        
        loadShift();
        loadMilkTypes();

        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());

        txtCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtCode.getText().length() > 0) {
                String code = generateCode(txtCode.getText().trim());
                getNameFromMemberCode(code);
            }
        });

        FocusUtils.requestFocus(dpDate);

        txtRemarks.setOnAction(e -> {
            FocusUtils.requestFocus(btnSaveUpdate);
            e.consume();
        });
        
        clearControls();
    }

    @Override
    public void clearControls() {
        dpDate.setValue(LocalDate.now());
        txtCode.clear();
        txtName.clear();
        txtFat.clear();
        txtSnf.clear();
        txtQty.clear();
        txtRemarks.clear();
        if(cboxShift.getItems() != null)
            cboxShift.getSelectionModel().clearSelection();
        if(cboxMilkType.getItems() != null)
            cboxMilkType.getSelectionModel().clearSelection();
        dto = null;
        btnSaveUpdate.setText(resourceBundle.getString("add"));
        FocusUtils.requestFocus(dpDate);
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), "Validation Error", errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                setValuesInObject();
                updateData();
            }
        } else {
            dto = new RejectedMilkCollection();
            setValuesInObject();
            saveData();
        }
    }

    private boolean validate() {
        if (dpDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("purchase.date.null.error") + "\n");
        if (cboxShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("fromshiftnullerror") + "\n");
        if (cboxMilkType.getValue() == null)
            errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
        if (txtCode.getText() == null || txtCode.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("membercode.cannot.be.null") + "\n");
        if (txtName.getText() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("membernamenullerror") + "\n");
        if (txtQty.getText() == null || parseBigDecimal(txtQty.getText()).compareTo(BigDecimal.ZERO) <= 0)
            errorMsg.append(resourceBundle.getString("qty.cannot.be.null") + "\n");
        if (txtFat.getText() == null || parseBigDecimal(txtFat.getText()).compareTo(BigDecimal.ZERO) < 0)
            errorMsg.append(resourceBundle.getString("fat.cannot.be.null") + "\n");
        if (txtSnf.getText() == null || parseBigDecimal(txtSnf.getText()).compareTo(BigDecimal.ZERO) < 0)
            errorMsg.append(resourceBundle.getString("snf.cannot.be.null") + "\n");

        return errorMsg.length() == 0;
    }

    private void setValuesInObject() {
        if (dto == null) {
            dto = new RejectedMilkCollection();
        }

        dto.setDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue()));
        dto.setShift(cboxShift.getValue());
        dto.setMilkType(cboxMilkType.getValue());

        Member member = new Member();
        member.setCode(generateCode(txtCode.getText().trim()));
        dto.setMember(member);

        dto.setQty(parseBigDecimal(txtQty.getText()));
        dto.setFat(parseBigDecimal(txtFat.getText()));
        dto.setSnf(parseBigDecimal(txtSnf.getText()));
        dto.setRemark(txtRemarks.getText());
        dto.setDock(MainApp.identityDto.getDock());

        if (btnSaveUpdate.getText().equalsIgnoreCase(resourceBundle.getString("update"))) {
            dto.setupdateData();
        } else {
            dto.setInitData();
        }
    }

    @Override
    public void saveData() {
        var task = new RejectedMilkCollectionSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                RejectedMilkCollection savedDto = task.get();
                if (savedDto != null) {
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilkaddedit"), resourceBundle.getString("rejectedmilk.insert.successful"));
                    alert.createAlert();
                    if (callback != null) {
                        callback.reloadData(true);
                    }
                    if (stage != null) {
                        stage.close();
                    }
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilkaddedit"), resourceBundle.getString("rejectedmilk.insert.failed"));
                    alert.createAlert();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new RejectedMilkCollectionSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                RejectedMilkCollection savedDto = task.get();
                if (savedDto != null) {
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilkaddedit"), resourceBundle.getString("rejectedmilk.update.successful"));
                    alert.createAlert();
                    if (callback != null) {
                        callback.reloadData(true);
                    }
                    if (stage != null) {
                        stage.close();
                    }
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilkaddedit"), resourceBundle.getString("rejectedmilk.update.failed"));
                    alert.createAlert();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    cboxShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                }
                if (LocalTime.now().isBefore(LocalTime.of(16, 0))) {
                    cboxShift.getSelectionModel().select(list.stream().filter(p -> p.getName().equalsIgnoreCase("morning")).findFirst().orElse(null));
                } else {
                    cboxShift.getSelectionModel().select(list.stream().filter(p -> p.getName().equalsIgnoreCase("evening")).findFirst().orElse(null));
                }
                if (dto != null && dto.getShift() != null) {
                    cboxShift.getSelectionModel().select(dto.getShift());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMilkTypes() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                    if (dto != null && dto.getMilkType() != null) {
                        cboxMilkType.getSelectionModel().select(dto.getMilkType());
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNameFromMemberCode(String code) {
        var task = new MemberByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    Member member = task.get();
                    txtName.setText(member.toMemberName());
                } else {
                    txtName.clear();
                    new ErrorAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilkaddedit"), resourceBundle.getString("membernotfound")).createAlert();
                    txtCode.setText("");
                    FocusUtils.requestFocus(txtCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void loadControls() {
        if (this.dto != null) {
            dpDate.setValue(dto.getDate().toLocalDate());
            if (dto.getShift() != null) cboxShift.getSelectionModel().select(dto.getShift());
            if (dto.getMilkType() != null) cboxMilkType.getSelectionModel().select(dto.getMilkType());
            if (dto.getMember() != null) {
                txtCode.setText(dto.getMember().getCode().substring(MainApp.getUser().getSociety().getCode().length()));
                txtName.setText(dto.getMember().getFirstName());
            }
            txtQty.setText(dto.getQty().toPlainString());
            txtFat.setText(dto.getFat().toPlainString());
            txtSnf.setText(dto.getSnf().toPlainString());
            txtRemarks.setText(dto.getRemark());
        }
    }

    private String generateCode(String code) {
        return MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(code));
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
