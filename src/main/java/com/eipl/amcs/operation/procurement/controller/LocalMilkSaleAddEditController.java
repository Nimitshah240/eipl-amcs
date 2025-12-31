package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.master.global.convertor.MilkClassConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkClassLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.CustomerByIdLoadTask;
import com.eipl.amcs.master.operation.task.LocalMilkSaleRateTask;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleGetInvoiceNoTask;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleSaveTask;
import com.eipl.amcs.utils.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LocalMilkSaleAddEditController implements MyInitialization {
    public String invoice = "";
    @FXML
    private StackPane root;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxConsumertype;
    @FXML
    private ComboBox<String> cboxPaymentType;
    @FXML
    private E_ComboBox<MilkType> cboxMilkType;
    @FXML
    private E_ComboBox<MilkClass> cboxClass;
    @FXML
    private E_ComboBox<Shift> cboxShift;
    @FXML
    private E_TextField txtDiscount, txtRate, txtAmount, txtQuantity, txtCash, txtCredit, txtCoupon, txtInvoiceNo, txtConsumerName, txtConsumerCode;
    @FXML
    private E_Button btnSaveUpdate, btnClose;
    @FXML
    private E_DatePicker dpSellDate;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private LocalMilkSale dto = null;
    private PopupCallback callback;
    private BigDecimal rate;

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

    public void setLocalMilkSaleDto(LocalMilkSale dto) {
        try {
            if (dto != null) {
                this.dto = dto;
                btnSaveUpdate.setText(resourceBundle.getString("update"));
                loadControls();
                if (dto.getConsumerType() < (short) 3) {
                    getNameFromMemberCode(dto.getConsumerCode());
                } else {
                    getNameFromCustomerCode(dto.getConsumerCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtQuantity.setText("0");
        dpSellDate.setValue(LocalDate.now());
        this.resourceBundle = resourceBundle;
        txtQuantity.setText("0");
        txtCash.setDisable(true);
        txtCredit.setDisable(true);
        txtCoupon.setDisable(true);
        txtAmount.setDisable(true);
        txtRate.setDisable(true);
        loadShift();
        setupComboBox();
        loadCustomerType();
        loadMilkTypes();
        loadMilkClass();
        getNextCode();
        cboxPaymentType.getItems().addAll(resourceBundle.getString("cash"), resourceBundle.getString("credit"), resourceBundle.getString("coupon"));
        cboxPaymentType.getSelectionModel().select(0);
        cboxPaymentType.setOnAction(e -> {
            paymentSelection();
            if (cboxPaymentType.isFocused())
                calculateValues();
        });

        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());

        txtQuantity.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (txtQuantity.getText().trim().isEmpty() || Double.parseDouble(txtQuantity.getText().trim()) == 0) {

                } else {
                    calculate();
                    FocusUtils.requestFocus(btnSaveUpdate);
                }
            }
        });

        cboxClass.setOnAction(e -> {
            if (!cboxMilkType.getSelectionModel().isEmpty()) {
                getRate(dpSellDate.getValue(), cboxMilkType.getValue(), cboxClass.getValue());
            }
        });
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            paymentSelection();
        });
        FocusUtils.requestFocus(txtConsumerCode);

        txtConsumerCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtConsumerCode.getText().length() > 0) {
                getRate(dpSellDate.getValue(), cboxMilkType.getValue(), cboxClass.getValue());
                if (cboxConsumertype.getValue().getKey() < (short) 3) {
                    String code = generateCode(txtConsumerCode.getText().trim());
                    getNameFromMemberCode(code);
                } else {
                    String code = generateCode(txtConsumerCode.getText().trim());
                    getNameFromCustomerCode(code);
                }
            }
        });

    }

    @Override
    public void setupComboBox() {
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxClass.setConverter(new MilkClassConvertor(cboxClass));
        cboxShift.setConverter(new ShiftConvertor(cboxShift));
        dpSellDate.setConverter(new LocalDateConvertor());

        cboxConsumertype.setConverter(new CustomerTypeConvertor(cboxConsumertype));

    }

    public void loadCustomerType() {
        cboxConsumertype.setItems(FXCollections.observableList(CommonUtils.getCustomerTypesForLocalMilkSale()));
        cboxConsumertype.getSelectionModel().select(0);
        if (dto != null) {
            Optional<CustomerTypeKeyValDto> dd = cboxConsumertype.getItems().stream()
                    .filter(p -> p.getKey() == dto.getConsumerType()).findFirst();
            if (dd.isPresent())
                cboxConsumertype.getSelectionModel().select(dd.get());
        } else {
            Optional<CustomerTypeKeyValDto> dd = cboxConsumertype.getItems().stream()
                    .filter(p -> p.getKey() == 0).findFirst();
            if (dd.isPresent())
                cboxConsumertype.getSelectionModel().select(dd.get());
        }
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                setValuesInObject();
                updateData();
            }
        } else {
            dto = new LocalMilkSale();
            setValuesInObject();
            saveData();
        }
    }

    private void setValuesInObject() {
        dto.setConsumerCode(generateCode(txtConsumerCode.getText().trim()));
        dto.setInvoiceNo(invoice);
        dto.setConsumerType(cboxConsumertype.getValue().getKey());
        dto.setPaymentMode((short) (cboxPaymentType.getSelectionModel().getSelectedIndex() == 0 ? 0 :
                (cboxPaymentType.getSelectionModel().getSelectedIndex() == 1 ? 1 : 0)));
        dto.setMilkType(cboxMilkType.getValue());
        dto.setMilkClass(cboxClass.getValue());
        dto.setQuantity(new BigDecimal(txtQuantity.getText().trim()));
        dto.setQuantityMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MILKSALE_QTY_MODE, "0")).shortValue());
        dto.setConvertedQuantity(CommonUtils.convertQty(AppConstant.CollectionType.LOCAL_SALE, txtQuantity.getText()));
        dto.setConvertedQuantityMode(dto.getQuantityMode() == 0 ? (short) 1 : (short) 0);
        dto.setRate(new BigDecimal(txtRate.getText()));
        dto.setAmount(new BigDecimal(txtAmount.getText().trim()));
        dto.setCash(new BigDecimal(txtCash.getText().trim()));
        if (Double.parseDouble(txtCoupon.getText().trim()) > 0) {
            dto.setCoupon(BigDecimal.ZERO);
            dto.setCoupon(new BigDecimal(txtCoupon.getText().trim()));
        }
        dto.setCredit(new BigDecimal(txtCredit.getText().trim()));
        dto.setSaleDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpSellDate.getValue(), cboxShift.getValue()));
        dto.setShift(cboxShift.getValue());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnionCode(MainApp.identityDto.getUnion().getCode());
        dto.setDock(MainApp.identityDto.getDock());
        dto.setEntryType((short) 1);
    }

    private void getNextCode() {
        var task = new LocalMilkSaleGetInvoiceNoTask();
        task.setOnSucceeded(event -> {
            try {
                invoice = task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private boolean validate() {
        try {
            if (txtRate.getText() == null || Objects.equals(txtRate.getText(), "0")) {
                errorMsg.append(resourceBundle.getString("localmilksaleratenotavailable") + "\n");
            }
            if (dpSellDate.getValue() == null)
                errorMsg.append(resourceBundle.getString("datenullerror") + "\n");
            if (cboxShift.getValue() == null)
                errorMsg.append(resourceBundle.getString("shifttypenullerror") + "\n");
            if (txtConsumerCode.getText() == null || txtConsumerCode.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("consumernullerror") + "\n");
            if (txtConsumerName.getText() == null || txtConsumerName.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("consumernamenullerror") + "\n");
            if (cboxPaymentType.getValue() == null || cboxPaymentType.getSelectionModel().isEmpty())
                errorMsg.append(resourceBundle.getString("paymenttypenullerror") + "\n");
            if (cboxMilkType.getValue() == null)
                errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
            if (cboxClass.getValue() == null)
                errorMsg.append(resourceBundle.getString("classnullerror") + "\n");
            if (txtQuantity.getText() == null || txtQuantity.getText().isEmpty())
                errorMsg.append(resourceBundle.getString("quantitynullerror") + "\n");
            try {
                if (Double.parseDouble(txtQuantity.getText()) <= 0 || Double.parseDouble(txtQuantity.getText()) >= 1000)
                    errorMsg.append(resourceBundle.getString("quantitynullerror") + "\n");
                Double.parseDouble(txtQuantity.getText().trim());
            } catch (NumberFormatException e) {
                errorMsg.append(resourceBundle.getString("quantitynullerror") + "\n");
            }
            if (cboxPaymentType.getSelectionModel().getSelectedItem() != null) {
                if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("cash"))) {
                    if (txtCredit.getText().trim() == null || txtCredit.getText().isEmpty())
                        errorMsg.append(resourceBundle.getString("creditnullerror") + "\n");
                    if (txtCoupon.getText().trim() == null || txtCoupon.getText().isEmpty())
                        errorMsg.append(resourceBundle.getString("couponnullerror") + "\n");
                } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("credit"))) {
                    if (txtCash.getText().trim() == null || txtCash.getText().isEmpty())
                        errorMsg.append(resourceBundle.getString("cashnullerror") + "\n");
                    if (txtCoupon.getText().trim() == null || txtCoupon.getText().isEmpty())
                        errorMsg.append(resourceBundle.getString("couponnullerror") + "\n");
                } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("coupon"))) {
                    if (txtCredit.getText().trim() == null || txtCredit.getText().isEmpty())
                        errorMsg.append(resourceBundle.getString("creditnullerror") + "\n");
                    if (txtCash.getText().trim() == null || txtCash.getText().isEmpty())
                        errorMsg.append(resourceBundle.getString("cashnullerror") + "\n");
                }
            }
            if (errorMsg.length() == 0) {
                double cash = 0, credit = 0, coupon = 0, amount = 0, discount = 0;
                try {
                    cash = Double.parseDouble(txtCash.getText().trim());
                } catch (NumberFormatException e) {

                }
                try {
                    credit = Double.parseDouble(txtCredit.getText().trim());
                } catch (NumberFormatException e) {

                }
                try {
                    coupon = Double.parseDouble(txtCoupon.getText().trim());
                } catch (NumberFormatException e) {

                }
                try {
                    amount = Double.parseDouble(txtAmount.getText().trim());
                } catch (NumberFormatException e) {
                }
                if ((amount - discount) != (cash + credit + coupon)) {
                    errorMsg.append(resourceBundle.getString("wrongcalculation"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorMsg.length() == 0;
    }


    @Override
    public void saveData() {
        var task = new LocalMilkSaleSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                        resourceBundle.getString("localmilksale.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                reloadPage();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new LocalMilkSaleSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                        resourceBundle.getString("localmilksale.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            System.out.println("Error : " + e);
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    private void paymentSelection() {
        if (cboxPaymentType.getSelectionModel().getSelectedItem() != null) {
            if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("cash"))) {
                txtCash.setText(txtAmount.getText());
                txtCoupon.setText("0.0");
                txtCredit.setText("0.0");
            } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("credit"))) {
                txtCash.setText("0.0");
                txtCredit.setText(txtAmount.getText());
                txtCoupon.setText("0.0");
            } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("coupon"))) {
                txtCash.setText("0.0");
                txtCredit.setText("0.0");
                txtCoupon.setText(txtAmount.getText());
            }
        }
    }

    public void calculateValues() {
        double cash = Double.parseDouble(txtCash.getText().isEmpty() ? "0" : txtCash.getText());
        double credit = Double.parseDouble(txtCredit.getText().isEmpty() ? "0" : txtCredit.getText());
        double coupon = Double.parseDouble(txtCoupon.getText().isEmpty() ? "0" : txtCoupon.getText());
        double amount = Double.parseDouble(txtAmount.getText().isEmpty() ? "0" : txtAmount.getText());
        double discount = 0;

        if (cboxPaymentType.getValue().equals(resourceBundle.getString("cash"))) {
            if ((amount - discount) - credit - coupon < 0) {
                txtCash.setText("0");
            } else {
                txtCash.setText(String.valueOf((amount - discount) - credit - coupon));
            }
        } else if (cboxPaymentType.getValue().equals(resourceBundle.getString("credit"))) {
            if ((amount - discount) - cash - coupon < 0) {
                txtCredit.setText("0");
            } else {
                txtCredit.setText(String.valueOf((amount - discount) - cash - coupon));
            }
        } else if (cboxPaymentType.getValue().equals(resourceBundle.getString("coupon"))) {
            if ((amount - discount) - credit - cash < 0) {
                txtCoupon.setText("0");
            } else {
                txtCoupon.setText(String.valueOf((amount - discount) - cash - credit));
            }
        }
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

                if (dto != null) {
                    Optional<Shift> shift = cboxShift.getItems().stream()
                            .filter(p -> p.getCode() == dto.getShift().getCode()).findFirst();
                    if (shift.isPresent())
                        cboxShift.getSelectionModel().select(shift.get());
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

    private void loadMilkClass() {
        var task = new MilkClassLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkClass> list = task.get();
                if (list != null) {
                    cboxClass.setItems(FXCollections.observableList(list));
                }
                if (dto != null) {
                    Optional<MilkClass> milkClass = cboxClass.getItems().stream()
                            .filter(p -> p.getCode() == dto.getMilkClass().getCode()).findFirst();
                    if (milkClass.isPresent())
                        cboxClass.getSelectionModel().select(milkClass.get());
                } else {
                    cboxClass.getSelectionModel().select(0);
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
                    txtConsumerName.setText(member.toMemberName());
                } else {
                    txtConsumerName.clear();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                            resourceBundle.getString("membernotfound"));
                    alert.createAlert();
                    txtConsumerCode.setText("");
                    FocusUtils.requestFocus(txtConsumerCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void getNameFromCustomerCode(String code) {
        //Code and name from customer code
        var task = new CustomerByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    Customer customer = task.get();
                    txtConsumerName.setText(customer.getName());
                } else {
                    txtConsumerName.clear();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                            resourceBundle.getString("customernotfound"));
                    alert.createAlert();
                    txtConsumerCode.setText("");
                    FocusUtils.requestFocus(txtConsumerCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private String generateCode(String code) {
        return MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(code));
    }

    private void calculate() {
        if (txtQuantity.getText().trim() != null && txtQuantity.getText().trim().length() > 0
                && cboxMilkType.getSelectionModel().getSelectedItem() != null
                && cboxClass.getSelectionModel().getSelectedItem() != null) {

            if (Double.parseDouble(txtQuantity.getText()) > 0) {
                getRate(dpSellDate.getValue(), cboxMilkType.getValue(), cboxClass.getValue());
            } else {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                        resourceBundle.getString("qtyzero"));
                alert.createAlert();
                txtQuantity.clear();
            }
        }
    }

    private void getRate(LocalDate sellDate, MilkType milkType, MilkClass milkClass) {
        var task = new LocalMilkSaleRateTask(sellDate, milkType.getCode(), milkClass.getCode());
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    this.rate = task.get().getRate();
                    txtRate.setText(rate.toString());
                } else {
                    txtRate.setText("0");
                }
                calculateAmount();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void calculateAmount() {
        try {
            String rateText = txtRate.getText().trim();
            if (rateText != null && !Objects.equals(txtRate.getText(), "0")) {
                if (txtRate.getText() != null && txtRate.getText().length() > 0
                        && Double.parseDouble(txtRate.getText()) > 0 && Double.parseDouble(txtQuantity.getText()) > 0) {
                    if (Double.parseDouble(txtQuantity.getText()) <= 0) {

                    } else {
                        txtAmount.setText(String.valueOf(NumberUtil.round(
                                Double.parseDouble(txtRate.getText()) * Double.parseDouble(txtQuantity.getText()), 2)));
                    }
                }
            } else {
                paymentSelection();
                txtAmount.setText("0");
                txtRate.setText("0");
                txtCash.setText("0");
                txtCoupon.setText("0");
                txtCredit.setText("0");
//                txtDiscount.setText("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadControls() {
        if (this.dto != null) {
            loadCustomerType();
            dpSellDate.setValue(dto.getSaleDate().toLocalDate());
            cboxShift.getSelectionModel().select(dto.getShift());
            if (txtConsumerCode.getText() != null)
                txtConsumerCode.setText(dto.getConsumerCode().substring(MainApp.getUser().getSociety().getCode().length()));
            cboxPaymentType.getSelectionModel().select(dto.getPaymentMode() == 0 ? resourceBundle.getString("cash")
                    : dto.getPaymentMode() == 1 ? resourceBundle.getString("credit")
                    : resourceBundle.getString("coupon"));
            cboxMilkType.getSelectionModel().select(dto.getMilkType());
            cboxClass.getSelectionModel().select(dto.getMilkClass());
            txtQuantity.setText(dto.getQuantity().toString());
            txtRate.setText(dto.getRate().toString());
            txtAmount.setText(dto.getAmount().toString());
            txtCash.setText(dto.getCash().toString());
            txtCredit.setText(dto.getCredit().toString());
            txtCoupon.setText(dto.getCoupon() == null ? "0" : dto.getCoupon().toString());
        }
    }

    private void reloadPage() {
        try {
            txtConsumerCode.setText("");
            txtConsumerName.setText("");
            txtQuantity.setText("0");
            txtAmount.setText("0");
            txtRate.setText("0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
