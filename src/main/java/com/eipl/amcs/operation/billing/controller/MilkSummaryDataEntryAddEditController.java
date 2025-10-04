package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.master.procurement.converter.SocietyPaymentCycleConvertor;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleLoadTask;
import com.eipl.amcs.operation.billing.dto.MilkSummaryDataEntry;
import com.eipl.amcs.operation.billing.task.MilkDataEntrySaveTask;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MilkSummaryDataEntryAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<SocietyPaymentCycle> cboxPaymentCycle;
    @FXML
    private TextField txtMemberCode, txtMemberName, txtMilkQty, txtMilkAmount;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private DatePicker dpDate;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private MilkSummaryDataEntry dto = null;
    private Member member;
    private MilkQualityType milkQualityType;

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

    public void setMilkSummaryDataEntry(MilkCollection dto) {
        if (dto != null) {
            this.dto = new MilkSummaryDataEntry();
            this.dto.setMilkQualityType(dto.getMilkQualityType());
            this.dto.setUnion(MainApp.identityDto.getUnion().getCode());
            this.dto.setSociety(MainApp.identityDto.getSociety());
            this.dto.setDock(MainApp.identityDto.getDock());
            this.dto.setDate(dto.getCollectionDate().toLocalDate());
            this.dto.setPaymentCycle(dto.getSocietyPaymentCycle());
            this.dto.setMember(dto.getMember());
            this.member = dto.getMember();
            this.dto.setMilkType(dto.getMilkType());
            this.dto.setMilkQuantity(dto.getQty());
            this.dto.setMilkAmount(dto.getAmount());
            btnSaveUpdate.setText(CommonUtils.getResourceString(resourceBundle, "update"));
            loadControls();
        }
        loadPaymentCycle();
        loadMilkType();
    }

    private void loadMilkType() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                    if(dto != null) {
                        cboxMilkType.getSelectionModel().select(dto.getMilkType());
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadPaymentCycle() {
        var task = new SocietyPaymentCycleLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SocietyPaymentCycle> list = task.get();
                if (list != null) {
                    cboxPaymentCycle.setItems(FXCollections.observableList(list));
                    if(dto != null) {
                        cboxPaymentCycle.getSelectionModel().select(dto.getPaymentCycle());
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        loadMilkQuality();
        dpDate.setOnAction(e -> {
            LocalDateTime datetime = LocalDateTime.of(dpDate.getValue(), LocalTime.NOON);
            Optional<SocietyPaymentCycle> paymentCycle = cboxPaymentCycle.getItems().stream().filter(p ->
                            datetime.isAfter(p.getFromDate()) && datetime.isBefore(p.getToDate()))
                    .findFirst();
            if (paymentCycle.isPresent())
                cboxPaymentCycle.getSelectionModel().select(paymentCycle.get());
        });

        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());

        txtMemberCode.setOnAction(e -> fetchMemberInformation());
        txtMemberCode.focusedProperty().addListener((ob, oldVal, newVal) -> {
            if (!newVal)
                fetchMemberInformation();
        });
    }

    private void loadMilkQuality() {
        var task = new MilkQualityTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkQualityType> list = task.get();
                if (list != null)
                    milkQualityType = list.get(0);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchMemberInformation() {
        String code = MainApp.identityDto.getSociety().getCode() +
                String.format("%04d", CommonUtils.strToInteger(txtMemberCode.getText().trim()));
        var task = new MemberByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    member = task.get();
                    txtMemberName.setText(member.toMemberName());
                } else {
                    txtMemberCode.clear();
                    txtMemberName.clear();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "milksummarydataentry"),
                            CommonUtils.getResourceString(resourceBundle, "membernotfound"));
                    alert.createAlert();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milksummarydataentry"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            setValuesInObject();
            updateData();
        } else {
            dto = new MilkSummaryDataEntry();
            setValuesInObject();
            saveData();
        }
    }

    @Override
    public void setupComboBox() {
        cboxPaymentCycle.setConverter(new SocietyPaymentCycleConvertor(cboxPaymentCycle));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        dpDate.setConverter(new LocalDateConvertor());
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });
    }

    private void setValuesInObject() {
        dto.setDate(dpDate.getValue());
        dto.setPaymentCycle(cboxPaymentCycle.getValue());
        dto.setMember(member);
        dto.setMilkType(cboxMilkType.getValue());
        dto.setMilkQuantity(new BigDecimal(txtMilkQty.getText()));
        dto.setMilkAmount(new BigDecimal(txtMilkAmount.getText()));
        dto.setDock(MainApp.identityDto.getDock());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnion(MainApp.identityDto.getUnion().getCode());
        dto.setMilkQualityType(milkQualityType);
    }

    @Override
    public void loadControls() {
        dpDate.setValue(dto.getDate());
        txtMemberCode.setText(dto.getMember().getCodeEx());
        txtMemberName.setText(dto.getMember().toMemberName());
        txtMilkQty.setText(dto.getMilkQuantity().toString());
        txtMilkAmount.setText(dto.getMilkAmount().toString());
    }

    private boolean validate() {
        if (cboxPaymentCycle.getValue() == null)
            errorMsg.append("PaymentCycle can not be null or empty\n");
        if (dpDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("product.sale.validation.date.empty") + "\n");
        if (txtMemberCode.getText() == null || txtMemberCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("membernotfound") + "\n");
        if (cboxMilkType.getValue() == null)
            errorMsg.append(resourceBundle.getString("milktypeerror") + "\n");
        if (txtMilkQty.getText() == null || txtMilkQty.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("invalid.qty") + "\n");
        else if (!CommonUtils.isNumeric(txtMilkQty.getText()))
            errorMsg.append(resourceBundle.getString("invalid.qty") + "\n");
        if (txtMilkAmount.getText() == null || txtMilkAmount.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("invalid.amount") + "\n");
        else if (!CommonUtils.isNumeric(txtMilkAmount.getText()))
            errorMsg.append(resourceBundle.getString("invalid.amount") + "\n");

        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new MilkDataEntrySaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milksummarydataentry"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milksummarydataentry"),
                        resourceBundle.getString("milksummarydataentry.insert.successful"));
                alert.createAlert();
//                this.callback.reloadData(true);
//                this.stage.close();
                clearControls();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void clearControls() {
        member = null;
        txtMemberCode.clear();
        txtMemberName.clear();
        txtMilkQty.clear();
        txtMilkAmount.clear();
        FocusUtils.requestFocus(txtMemberCode);
    }

    @Override
    public void updateData() {
        var task = new MilkDataEntrySaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milksummarydataentry"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milksummarydataentry"),
                        resourceBundle.getString("milksummarydataentry.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

}
