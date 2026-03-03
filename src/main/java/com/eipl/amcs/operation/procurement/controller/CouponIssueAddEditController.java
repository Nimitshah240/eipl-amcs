package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.CustomerByIdLoadTask;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.task.CouponIssueGetNextCodeLoadTask;
import com.eipl.amcs.operation.procurement.task.CouponIssueSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class CouponIssueAddEditController implements MyInitialization {

    @FXML
    private E_TextField txtName;
    @FXML
    private E_NumericField txtCode, txtAmount, txtIssueNumber;
    @FXML
    private E_DatePicker dpDate;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxType;
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private E_Button btnClose, btnSaveUpdate;
    @FXML
    private StackPane root;
    private CouponIssue dto = null;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadControls();
        setupTable();
        loadData();
        setupComboBox();

        cboxType.setOnAction(event -> {
            txtCode.setText("");
            txtName.setText("");
        });

        FocusUtils.requestFocus(btnSaveUpdate);

        btnClose.setOnAction(e -> this.stage.close());

        btnSaveUpdate.setOnAction(event -> {
            validateAndSave();
            FocusUtils.requestFocus(txtCode);
        });

        txtCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtCode.getText().length() > 0) {
                if (cboxType.getValue().getKey() < (short) 3) {
                    String code = generateCode(txtCode.getText().trim());
                    getNameFromMemberCode(code);
                } else {
                    String code = generateCode(txtCode.getText().trim());
                    getNameFromCustomerCode(code);
                }
            }
        });
    }

    public void loadMilkType() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                    if (dto != null) {
                        Optional<MilkType> milkType = cboxMilkType.getItems().stream()
                                .filter(p -> p.getCode() == dto.getMilkType().getCode()).findFirst();
                        if (milkType.isPresent())
                            cboxMilkType.getSelectionModel().select(milkType.get());
                    } else {
                        cboxMilkType.getSelectionModel().select(1);
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void loadCustomerType() {
        cboxType.setItems(FXCollections.observableList(CommonUtils.getCustomerTypesForLocalMilkSale()));
        cboxType.getSelectionModel().select(0);
        if (dto != null) {
            Optional<CustomerTypeKeyValDto> dd = cboxType.getItems().stream()
                    .filter(p -> p.getKey() == dto.getConsumerType()).findFirst();
            if (dd.isPresent())
                cboxType.getSelectionModel().select(dd.get());
        } else {
            Optional<CustomerTypeKeyValDto> dd = cboxType.getItems().stream()
                    .filter(p -> p.getKey() == 0).findFirst();
            if (dd.isPresent())
                cboxType.getSelectionModel().select(dd.get());
        }
    }

    private void getNextCouponIssue() {
        var task = new CouponIssueGetNextCodeLoadTask(
                MainApp.identityDto.getSociety());
        new Thread(task).start();
        task.setOnSucceeded(e -> {
            try {
                txtIssueNumber.setText(task.get());
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        });
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
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("membernotfound"));
                    alert.createAlert();
                    txtName.setText("");
                    FocusUtils.requestFocus(txtName);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNameFromCustomerCode(String code) {
        var task = new CustomerByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    Customer customer = task.get();
                    txtName.setText(customer.getName());
                } else {
                    txtName.clear();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("customernotfound"));
                    alert.createAlert();
                    txtCode.setText("");
                    FocusUtils.requestFocus(txtCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setCouponIssue(CouponIssue dto) {
        try {

            if (dto != null) {
                this.dto = dto;
                btnSaveUpdate.setText(resourceBundle.getString("update"));

                cboxType.getSelectionModel().select(dto.getConsumerType() - 1);
                dpDate.setValue(dto.getIssueDate());
                txtIssueNumber.setText(dto.getCode());
                txtName.setText(dto.getConsumerName());
                txtCode.setText(dto.getConsumerCode().substring(MainApp.identityDto.getSociety().getCode().length()));
                txtAmount.setText(String.valueOf(dto.getAmount()));
                cboxMilkType.getSelectionModel().select(dto.getMilkType());
            } else {
                getNextCouponIssue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValuesInObject() {
        dto.setUnion(MainApp.identityDto.getUnion());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setAmount(Double.parseDouble(txtAmount.getText().trim().isEmpty() ? "0" : txtAmount.getText().trim()));
        dto.setConsumerCode(generateCode(txtCode.getText().trim()));
        dto.setConsumerType(cboxType.getSelectionModel().getSelectedIndex() + 1);
        dto.setMilkType(cboxMilkType.getSelectionModel().getSelectedItem());
        dto.setCode(txtIssueNumber.getText().trim());
        dto.setActive(true);
        dto.setIssueDate(dpDate.getValue());
    }

    //CHeck usage and remove
    private String generateCode(String code) {
        if (code == null || code.length() < 4) {
            return MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(code));
        }
        String lastFour = code.substring(code.length() - 4);
        return MainApp.identityDto.getSociety().getCode() + lastFour;
    }

    @Override
    public void setupComboBox() {
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        dpDate.setConverter(new LocalDateConvertor());
    }

    @Override
    public void loadData() {
        loadMilkType();
        loadCustomerType();
    }

    @Override
    public void clearControls() {
        txtAmount.clear();
        txtCode.clear();
        txtName.clear();
        txtIssueNumber.clear();
        dpDate.setValue(LocalDate.now());
        cboxMilkType.valueProperty().set(null);
    }

    @Override
    public void loadControls() {
        txtIssueNumber.setDisable(true);
        dpDate.setValue(LocalDate.now());
    }

    private void validateAndSave() {
        loadControls();
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        boolean isUpdate = btnSaveUpdate.getText().equals(resourceBundle.getString("update"));
        if (isUpdate) {
            if (this.dto == null) return;
            setValuesInObject();
        } else {
            this.dto = new CouponIssue();
            setValuesInObject();
        }

        if (isUpdate) {
            updateData();
        } else {
            saveData();
        }
    }

    @Override
    public void saveData() {
        var task = new CouponIssueSaveTask(
                dto, (short) 0);

        task.setOnSucceeded(event -> {
            Boolean success = task.getValue();
            if (success) {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                        resourceBundle.getString("alert.insert.success"));
                alert.createAlert();

                loadData();
                clearControls();
                getNextCouponIssue();
                this.callback.reloadData(true);
                FocusUtils.requestFocus(btnSaveUpdate);
            } else {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                        resourceBundle.getString("couponissue.alert.insert.message"));
                alert.createAlert();
            }
        });

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                    "Error: " + ex.getMessage());
            alert.createAlert();
        });

        new Thread(task).start();
    }

    @Override
    public void updateData() {
        try {
            var task = new CouponIssueSaveTask(
                    dto, (short) 1);

            task.setOnSucceeded(event -> {
                Boolean success = task.getValue();
                if (success) {
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("alert.update.success"));
                    alert.createAlert();

                    loadData();
                    clearControls();
                    getNextCouponIssue();
                    this.callback.reloadData(true);
                    FocusUtils.requestFocus(btnSaveUpdate);
                } else {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("couponissue.alert.update.message"));
                    alert.createAlert();
                }
            });

            task.setOnFailed(event -> {
                String message = "error.occurred";
                Throwable ex = task.getException();
                if (ex.getMessage() != null && ex.getMessage().equalsIgnoreCase("insufficient.balance")) {
                    message = ex.getMessage();
                }
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                        resourceBundle.getString(message));
                alert.createAlert();
            });

            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validate() {
        if (txtCode.getText() == null || Objects.equals(txtCode.getText(), "0"))
            errorMsg.append(resourceBundle.getString("consumernullerror") + "\n");
        if (txtName.getText() == null || txtName.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("consumernamenullerror") + "\n");
        if (cboxMilkType.getValue() == null)
            errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
        if (txtAmount.getText() == null)
            errorMsg.append(resourceBundle.getString("amount.cannot.be.null") + "\n");

        return errorMsg.length() == 0;
    }
}
