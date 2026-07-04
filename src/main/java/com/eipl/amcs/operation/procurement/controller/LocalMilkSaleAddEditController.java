package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.*;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
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
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.eipl.amcs.operation.procurement.task.CouponBalanceForConsumerFetchTask;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleGetInvoiceNoTask;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleSaveTask;
import com.eipl.amcs.setting.repository.AccountPostingRepository;
import com.eipl.amcs.utils.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LocalMilkSaleAddEditController implements MyInitialization {
    public String invoice = "";
    @FXML
    private StackPane root;
    @FXML
    private AutoSearchTextField<CustomerTypeKeyValDto> cboxConsumertype;
    @FXML
    private AutoSearchTextField<String> cboxPaymentType;
    @FXML
    private AutoSearchTextField<MilkType> cboxMilkType;
    @FXML
    private AutoSearchTextField<MilkClass> cboxClass;
    @FXML
    private AutoSearchTextField<Shift> cboxShift;
    @FXML
    private E_TextField txtInvoiceNo, txtConsumerName;
    @FXML
    private E_NumericField txtDiscount, txtRate, txtAmount, txtQuantity, txtCash, txtCredit, txtCoupon, txtConsumerCode, txtCouponBalance;
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
    private CouponBalance couponBalance;

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
                    getNameFromCustomerCode(dto.getConsumerCode(), Integer.valueOf(dto.getConsumerType()));
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
        fetchCouponBalance();
        cboxConsumertype.setDisable(true);
        cboxPaymentType.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;

            if (newValue.equals(resourceBundle.getString("cash"))) {
                cboxConsumertype.getSelectionModel().select(2); // Customer
                txtConsumerCode.setText("1");
                txtConsumerCode.setDisable(true);
                cboxConsumertype.setDisable(true);
                getNameFromCustomerCode(generateCode("1"), Integer.valueOf(cboxConsumertype.getSelectionModel().getSelectedItem().getKey()));
            } else if (newValue.equals(resourceBundle.getString("credit")) || newValue.equals(resourceBundle.getString("coupon"))) {
                cboxConsumertype.getSelectionModel().select(0); // Member
                txtConsumerCode.clear();
                txtConsumerName.clear();
                txtConsumerCode.setDisable(false);
            }
        });

        cboxPaymentType.getSelectionModel().addAll(resourceBundle.getString("cash"), resourceBundle.getString("credit"), resourceBundle.getString("coupon"));
        cboxPaymentType.getSelectionModel().select(0);
        cboxPaymentType.setOnAction(e -> {
            paymentSelection();
            fetchCouponBalance();
            if (cboxPaymentType.isFocused())
                calculateValues();
        });

        cboxMilkType.setOnAction(e -> {
            fetchCouponBalance();
        });

        cboxConsumertype.setOnAction(e -> {
            fetchCouponBalance();
            txtConsumerCode.clear();
            txtConsumerName.clear();
        });

        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> {
            LocalDateTime saleDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(dpSellDate.getValue(), cboxShift.getValue());
            MilkDispatchRepository milkDispatchRepository = EmcsAppContext.getContext().getBean(MilkDispatchRepository.class);
            AccountPostingRepository accountPostingRepository = EmcsAppContext.getContext().getBean(AccountPostingRepository.class);

            int eventCode = Integer.parseInt(String.valueOf(AppConstant.EventCode.LOCAL_MILK_SALE).concat(String.valueOf(cboxPaymentType.getSelectionModel().getSelectedIndex() + 1)));

            if (milkDispatchRepository.existsByFromDateAndFromShift(saleDateTime, cboxShift.getValue())) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"), resourceBundle.getString("dispatch.already.done"));
                alert.createAlert();
            } else if (accountPostingRepository.findValidRange(dpSellDate.getValue(), cboxShift.getSelectionModel().getSelectedItem().getCode(), (short) 2, eventCode) > 0) {
                MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("localmilksale"), resourceBundle.getString("account.posting.already.done"));
                alert.createAlert();
            } else {
                validateAndSave();
            }
        });

        txtQuantity.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                double qty = parseDouble(txtQuantity.getInputText());
                if (qty == 0) {

                } else {
                    calculate();
                    FocusUtils.requestFocus(btnSaveUpdate);
                }
            }
        });

        cboxClass.setOnAction(e -> {
            if (!cboxMilkType.getText().isEmpty()) {
                getRate(dpSellDate.getValue(), cboxMilkType.getValue(), cboxClass.getValue());
            }
        });
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            paymentSelection();
        });
        FocusUtils.requestFocus(txtConsumerCode);

        txtConsumerCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtConsumerCode.getInputText().length() > 0) {
                fetchCouponBalance();
                getRate(dpSellDate.getValue(), cboxMilkType.getValue(), cboxClass.getValue());
                if (cboxConsumertype.getValue().getKey() < (short) 3) {
                    String code = generateCode(txtConsumerCode.getInputText().trim());
                    getNameFromMemberCode(code);
                } else {
                    String code = generateCode(txtConsumerCode.getInputText().trim());
                    getNameFromCustomerCode(code, Integer.valueOf(cboxConsumertype.getSelectionModel().getSelectedItem().getKey()));
                }
            }
        });

    }

    @Override
    public void setupComboBox() {
        dpSellDate.setConverter(new LocalDateConvertor());
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

        if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("coupon"))) {
//           DO NOT CHANGE THIS POSITION - NIMIT
            reCalculateCouponBalance();
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
        dto.setConsumerCode(generateCode(txtConsumerCode.getInputText().trim()));
        dto.setInvoiceNo(invoice);
        dto.setConsumerType(cboxConsumertype.getValue().getKey());
        dto.setPaymentMode((short) cboxPaymentType.getSelectionModel().getSelectedIndex());
        dto.setMilkType(cboxMilkType.getValue());
        dto.setMilkClass(cboxClass.getValue());
        dto.setQuantity(parseBigDecimal(txtQuantity.getInputText()));
        dto.setQuantityMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MILKSALE_QTY_MODE, "0")).shortValue());
        dto.setConvertedQuantity(CommonUtils.convertQty(AppConstant.CollectionType.LOCAL_SALE, txtQuantity.getInputText()));
        dto.setConvertedQuantityMode(dto.getQuantityMode() == 0 ? (short) 1 : (short) 0);
        dto.setRate(parseBigDecimal(txtRate.getInputText()));
        dto.setAmount(parseBigDecimal(txtAmount.getInputText()));
        dto.setCash(parseBigDecimal(txtCash.getInputText()));
        dto.setCoupon(parseBigDecimal(txtCoupon.getInputText()));
        dto.setCredit(parseBigDecimal(txtCredit.getInputText()));
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

//    private boolean validate() {
//        try {
//            if (cboxConsumertype.getSelectionModel().getSelectedItem().getKey() > 2 && cboxPaymentType.getSelectionModel().getSelectedItem().equalsIgnoreCase("credit")) {
//                errorMsg.append(resourceBundle.getString("customer.credit.not.allowed") + "\n");
//            }
//            try {
//                if (txtRate.getInputText() == null || txtRate.getInputText().trim().isEmpty() || Double.parseDouble(txtRate.getInputText()) <= 0) {
//                    errorMsg.append(resourceBundle.getString("localmilksaleratenotavailable") + "\n");
//                }
//            } catch (NumberFormatException e) {
//                errorMsg.append(resourceBundle.getString("localmilksaleratenotavailable") + "\n");
//            }
//            if (dpSellDate.getValue() == null)
//                errorMsg.append(resourceBundle.getString("datenullerror") + "\n");
//            if (cboxShift.getValue() == null)
//                errorMsg.append(resourceBundle.getString("shifttypenullerror") + "\n");
//            if (txtConsumerCode.getInputText() == null || txtConsumerCode.getInputText().isEmpty())
//                errorMsg.append(resourceBundle.getString("consumernullerror") + "\n");
//            if (txtConsumerName.getText() == null || txtConsumerName.getText().isEmpty())
//                errorMsg.append(resourceBundle.getString("consumernamenullerror") + "\n");
//            if (cboxPaymentType.getValue() == null || cboxPaymentType.getText().isEmpty())
//                errorMsg.append(resourceBundle.getString("paymenttypenullerror") + "\n");
//            if (cboxMilkType.getValue() == null)
//                errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
//            if (cboxClass.getValue() == null)
//                errorMsg.append(resourceBundle.getString("classnullerror") + "\n");
//            try {
//                if (txtQuantity.getInputText() == null || txtQuantity.getInputText().trim().isEmpty() || Double.parseDouble(txtQuantity.getInputText()) <= 0 || Double.parseDouble(txtQuantity.getInputText()) >= 1000)
//                    errorMsg.append(resourceBundle.getString("quantitynullerror") + "\n");
//            } catch (NumberFormatException e) {
//                errorMsg.append(resourceBundle.getString("quantitynullerror") + "\n");
//            }
//
//            try {
//                if (txtAmount.getInputText() == null || txtAmount.getInputText().trim().isEmpty() || Double.parseDouble(txtAmount.getInputText()) <= 0)
//                    errorMsg.append(resourceBundle.getString("quantitynullerror") + "\n");
//            } catch (NumberFormatException e) {
//                errorMsg.append(resourceBundle.getString("quantitynullerror") + "\n");
//            }
//
//            if (cboxPaymentType.getSelectionModel().getSelectedItem() != null) {
//                if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("cash"))) {
//                    if (txtCredit.getInputText().trim() == null || txtCredit.getInputText().isEmpty())
//                        errorMsg.append(resourceBundle.getString("creditnullerror") + "\n");
//                    if (txtCoupon.getInputText().trim() == null || txtCoupon.getInputText().isEmpty())
//                        errorMsg.append(resourceBundle.getString("couponnullerror") + "\n");
//                } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("credit"))) {
//                    if (txtCash.getInputText().trim() == null || txtCash.getInputText().isEmpty())
//                        errorMsg.append(resourceBundle.getString("cashnullerror") + "\n");
//                    if (txtCoupon.getInputText().trim() == null || txtCoupon.getInputText().isEmpty())
//                        errorMsg.append(resourceBundle.getString("couponnullerror") + "\n");
//                } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("coupon"))) {
//                    if (txtCredit.getInputText().trim() == null || txtCredit.getInputText().isEmpty())
//                        errorMsg.append(resourceBundle.getString("creditnullerror") + "\n");
//                    if (txtCash.getInputText().trim() == null || txtCash.getInputText().isEmpty())
//                        errorMsg.append(resourceBundle.getString("cashnullerror") + "\n");
//                    if (checkBalance())
//                        errorMsg.append(resourceBundle.getString("insufficient.balance") + "\n");
//                }
//            }
//            if (errorMsg.length() == 0) {
//                double cash = 0, credit = 0, coupon = 0, amount = 0, discount = 0;
//                try {
//                    cash = Double.parseDouble(txtCash.getInputText().trim());
//                } catch (NumberFormatException e) {
//
//                }
//                try {
//                    credit = Double.parseDouble(txtCredit.getInputText().trim());
//                } catch (NumberFormatException e) {
//
//                }
//                try {
//                    coupon = Double.parseDouble(txtCoupon.getInputText().trim());
//                } catch (NumberFormatException e) {
//
//                }
//                try {
//                    amount = Double.parseDouble(txtAmount.getInputText().trim());
//                } catch (NumberFormatException e) {
//                }
//                if ((amount - discount) != (cash + credit + coupon)) {
//                    errorMsg.append(resourceBundle.getString("wrongcalculation"));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return errorMsg.length() == 0;
//    }

    private boolean validate() {
        try {
            // 1. Extract values and default empty financial fields to "0" instead of blank
            String quantityStr = txtQuantity.getInputText() != null && !txtQuantity.getInputText().trim().isEmpty() ? txtQuantity.getInputText().trim() : "0";
            String rateStr = txtRate.getInputText() != null && !txtRate.getInputText().trim().isEmpty() ? txtRate.getInputText().trim() : "0";
            String amountStr = txtAmount.getInputText() != null && !txtAmount.getInputText().trim().isEmpty() ? txtAmount.getInputText().trim() : "0";

            String cashField = txtCash.getInputText() != null && !txtCash.getInputText().trim().isEmpty() ? txtCash.getInputText().trim() : "0";
            String creditField = txtCredit.getInputText() != null && !txtCredit.getInputText().trim().isEmpty() ? txtCredit.getInputText().trim() : "0";
            String couponField = txtCoupon.getInputText() != null && !txtCoupon.getInputText().trim().isEmpty() ? txtCoupon.getInputText().trim() : "0";

            String consumerCode = txtConsumerCode.getInputText() != null ? txtConsumerCode.getInputText().trim() : "";
            String consumerName = txtConsumerName.getText() != null ? txtConsumerName.getText().trim() : "";

            // 2. Dropdown & Identity Valuations
            if (cboxConsumertype.getSelectionModel().getSelectedItem() != null &&
                    cboxConsumertype.getSelectionModel().getSelectedItem().getKey() > 2 &&
                    cboxPaymentType.getSelectionModel().getSelectedItem() != null &&
                    cboxPaymentType.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("credit")) {
                errorMsg.append(resourceBundle.getString("customer.credit.not.allowed")).append("\n");
            }
            if (dpSellDate.getValue() == null)
                errorMsg.append(resourceBundle.getString("datenullerror")).append("\n");
            if (cboxShift.getValue() == null)
                errorMsg.append(resourceBundle.getString("shifttypenullerror")).append("\n");
            if (consumerCode.isEmpty())
                errorMsg.append(resourceBundle.getString("consumernullerror")).append("\n");
            if (consumerName.isEmpty())
                errorMsg.append(resourceBundle.getString("consumernamenullerror")).append("\n");
            if (cboxPaymentType.getSelectionModel().getSelectedItem() == null)
                errorMsg.append(resourceBundle.getString("paymenttypenullerror")).append("\n");
            if (cboxMilkType.getValue() == null)
                errorMsg.append(resourceBundle.getString("milktypenullerror")).append("\n");
            if (cboxClass.getValue() == null)
                errorMsg.append(resourceBundle.getString("classnullerror")).append("\n");

            // 3. Rate Numeric Check (Accepts 0)
            try {
                if (Double.parseDouble(rateStr) < 0) {
                    errorMsg.append(resourceBundle.getString("localmilksaleratenotavailable")).append("\n");
                }
            } catch (NumberFormatException e) {
                errorMsg.append(resourceBundle.getString("localmilksaleratenotavailable")).append("\n");
            }

            // 4. Quantity Numeric Check (Accepts 0)
            try {
                double qty = Double.parseDouble(quantityStr);
                if (qty < 0 || qty >= 1000) {
                    errorMsg.append(resourceBundle.getString("quantitynullerror")).append("\n");
                }
            } catch (NumberFormatException e) {
                errorMsg.append(resourceBundle.getString("quantitynullerror")).append("\n");
            }

            // 5. Amount Numeric Check (Accepts 0)
            try {
                if (Double.parseDouble(amountStr) < 0) {
                    errorMsg.append(resourceBundle.getString("amountnullerror")).append("\n");
                }
            } catch (NumberFormatException e) {
                errorMsg.append(resourceBundle.getString("amountnullerror")).append("\n");
            }

            if (cboxPaymentType.getSelectionModel().getSelectedItem() != null) {
                String selectedPayment = cboxPaymentType.getSelectionModel().getSelectedItem().toString();

                if (selectedPayment.equals(resourceBundle.getString("coupon")) && couponBalance == null) {
                    errorMsg.append(resourceBundle.getString("localmilksaleratenotavailable")).append("\n");
                }
                if (Double.parseDouble(amountStr) > 0) {
                    if (selectedPayment.equals(resourceBundle.getString("cash"))) {
                        if (txtCredit.getInputText() == null || txtCredit.getInputText().trim().isEmpty())
                            errorMsg.append(resourceBundle.getString("creditnullerror")).append("\n");
                        if (txtCoupon.getInputText() == null || txtCoupon.getInputText().trim().isEmpty() || parseDouble(txtCoupon.getInputText()) < 0)
                            errorMsg.append(resourceBundle.getString("couponnullerror")).append("\n");
                    } else if (selectedPayment.equals(resourceBundle.getString("credit"))) {
                        if (txtCash.getInputText() == null || txtCash.getInputText().trim().isEmpty() || parseDouble(txtCash.getInputText()) < 0)
                            errorMsg.append(resourceBundle.getString("cashnullerror")).append("\n");
                        if (txtCoupon.getInputText() == null || txtCoupon.getInputText().trim().isEmpty())
                            errorMsg.append(resourceBundle.getString("couponnullerror")).append("\n");
                    } else if (selectedPayment.equals(resourceBundle.getString("coupon"))) {
                        if (txtCredit.getInputText() == null || txtCredit.getInputText().trim().isEmpty())
                            errorMsg.append(resourceBundle.getString("creditnullerror")).append("\n");
                        if (txtCash.getInputText() == null || txtCash.getInputText().trim().isEmpty())
                            errorMsg.append(resourceBundle.getString("cashnullerror")).append("\n");
                        if (checkBalance())
                            errorMsg.append(resourceBundle.getString("insufficient.balance")).append("\n");
                    }
                }
            }
            if (errorMsg.length() == 0) {
                double cash = parseDouble(cashField);
                double credit = parseDouble(creditField);
                double coupon = parseDouble(couponField);
                double amount = parseDouble(amountStr);
                double discount = 0;

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
        LocalMilkSaleSaveTask newTask = null;
        if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("coupon"))) {
            newTask = new LocalMilkSaleSaveTask(dto, (short) 0, couponBalance);
        } else {
            newTask = new LocalMilkSaleSaveTask(dto, (short) 0);
        }
        var task = newTask;
//        var task = new LocalMilkSaleSaveTask(dto, (short) 0);
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
            Throwable t = task.getException();
            String message = t.getMessage();
            if (!message.contains("paymentcyclenotfound"))
                message = "error.occurred";

            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                    resourceBundle.getString(message));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        LocalMilkSaleSaveTask newTask = null;
        if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("coupon"))) {
            newTask = new LocalMilkSaleSaveTask(dto, (short) 1, couponBalance);
        } else {
            newTask = new LocalMilkSaleSaveTask(dto, (short) 1);
        }
        var task = newTask;
//        var task = new LocalMilkSaleSaveTask(dto, (short) 1);
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
                txtCash.setText(txtAmount.getInputText());
                txtCoupon.setText("0.0");
                txtCouponBalance.setText("0.0");
                txtCredit.setText("0.0");
            } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("credit"))) {
                txtCash.setText("0.0");
                txtCredit.setText(txtAmount.getInputText());
                txtCoupon.setText("0.0");
                txtCouponBalance.setText("0.0");
            } else if (cboxPaymentType.getSelectionModel().getSelectedItem().equals(resourceBundle.getString("coupon"))) {
                txtCash.setText("0.0");
                txtCredit.setText("0.0");
                txtCoupon.setText(txtAmount.getInputText());
            }
        }
    }

    public void calculateValues() {
        double cash = parseDouble(txtCash.getInputText());
        double credit = parseDouble(txtCredit.getInputText());
        double coupon = parseDouble(txtCoupon.getInputText());
        double amount = parseDouble(txtAmount.getInputText());
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

    private void getNameFromCustomerCode(String code, Integer type) {
        //Code and name from customer code
        var task = new CustomerByIdLoadTask(code, type);
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
        if (txtQuantity.getInputText().trim() != null && txtQuantity.getInputText().trim().length() > 0
                && cboxMilkType.getSelectionModel().getSelectedItem() != null
                && cboxClass.getSelectionModel().getSelectedItem() != null) {

            if (parseDouble(txtQuantity.getInputText()) > 0) {
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
            double rateVal = parseDouble(txtRate.getInputText());
            double qtyVal = parseDouble(txtQuantity.getInputText());

            if (rateVal > 0 && qtyVal > 0) {
                txtAmount.setText(String.valueOf(NumberUtil.round(rateVal * qtyVal, 2)));
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
            if (txtConsumerCode.getInputText() != null)
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
            txtQuantity.setText("0");
            txtAmount.setText("0");
            txtRate.setText("0");
            txtCouponBalance.setText("0");

            if (cboxPaymentType.getText().equals(resourceBundle.getString("cash"))) {
                getNameFromCustomerCode(generateCode("1"), (int) cboxConsumertype.getSelectionModel().getSelectedItem().getKey());
            } else {
                txtConsumerCode.setText("");
                txtConsumerName.setText("");
                txtConsumerCode.setDisable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchCouponBalance() {
        try {
            String selectedPayment = cboxPaymentType.getSelectionModel().getSelectedItem();
            MilkType milkType = cboxMilkType.getSelectionModel().getSelectedItem();
            String consumerCode = generateCode(txtConsumerCode.getInputText());
            CustomerTypeKeyValDto consumerType = cboxConsumertype.getSelectionModel().getSelectedItem();
            boolean isCouponPayment = selectedPayment != null && selectedPayment.equals(resourceBundle.getString("coupon"));

            if (isCouponPayment && milkType != null && consumerCode != null && consumerType != null) {
                CouponBalanceForConsumerFetchTask balanceTask = new CouponBalanceForConsumerFetchTask(Integer.valueOf(consumerType.getKey()), consumerCode, milkType);
                balanceTask.setOnSucceeded(e -> {
                    try {
                        couponBalance = balanceTask.get();
                        if (couponBalance != null)
                            txtCouponBalance.setText(String.valueOf(couponBalance.getBalance()));
                        else
                            txtCouponBalance.setText("0.0");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

                balanceTask.setOnFailed(e -> {
                    balanceTask.getException().printStackTrace();

                });
                new Thread(balanceTask).start();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkBalance() {
        BigDecimal newAmount = parseBigDecimal(txtAmount.getInputText());
        BigDecimal currentCouponBalance = parseBigDecimal(txtCouponBalance.getInputText());
        // return true means insufficient balance
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            BigDecimal oldAmount = this.dto.getAmount();
            BigDecimal oldCouponBalance = currentCouponBalance.add(oldAmount);
            return !(oldCouponBalance.subtract(newAmount).floatValue() >= 0f);
        } else {
            return !(currentCouponBalance.subtract(newAmount).floatValue() >= 0f);
        }
    }

    private void reCalculateCouponBalance() {
        if (this.couponBalance == null) {
            return;
        }
        BigDecimal newAmount = parseBigDecimal(txtAmount.getInputText());
        BigDecimal currentCouponBalance = parseBigDecimal(txtCouponBalance.getInputText());
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            BigDecimal oldAmount = this.dto.getAmount();
            BigDecimal oldCouponBalance = currentCouponBalance.add(oldAmount);
            this.couponBalance.setBalance(oldCouponBalance.subtract(newAmount).doubleValue());
        } else {
            this.couponBalance.setBalance(currentCouponBalance.subtract(newAmount).doubleValue());
        }
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

    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}