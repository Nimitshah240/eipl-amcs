package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.setting.model.GeneralConfig;
import com.eipl.amcs.setting.task.GeneralConfigSaveTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.task.DbBackupTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class GeneralConfigController implements MyInitialization {

    File appProperty, old;
    String[] arrQuality = {"Single MA", "Dual MA By Milk", "Dual MA By Sequence"};
    @FXML
    private AnchorPane root;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabMilkConfig, tabProductConfig, tabPaymentMode;
    @FXML
    private TextField txtLtrToKg, txtClrConst1, txtClrConst2, txtDefaultSnfValue, txtSampleSize, txtAvgPBasedOnPrevShift,
            txtAvgPIfMachineOff, txtSampleMilk, txtBackupPath, txtSpace, txtHrs, txtCollectionSlip, txtDecimalValue,
    txtVariationQty, txtVariationFat, txtVariationSnf, txtNo;
    @FXML
    private ComboBox<String> cboxDefaultSnf, cboxWeightSetting, cboxQualitySetting, cboxMemberCollectionQtyMode,
            cboxBmcCollectionQtyMode, cboxLocalMilkSaleQtyMode, cboxDispatchMilkQtyMode, cboxReceiptMilkQtyMode, cboxPaymentMode,
            cboxPaymentOption, cboxQualityMachine;
//    , cboxAvgBasedOn, cboxShift
    @FXML
    private CheckBox chkAcceptOtherMilk, chkAllowMultiEntry, chkAllowMultiEntryDiffType, chkPurchaseRate,
            chkSaleRate, chkPaymentMode, chkAvgParam, chkAllowZeroDispatch, chkBlockQty, chkBlockFat, chkBlockSnf,chkCodeMilkTypeParsing;
    @FXML
    private Button btnSave, btnClose, btnSave1, btnClose1, btnSave2, btnClose2, btnBrowse, btnBackup;
    @FXML
    private ComboBox<String> cboxSlipLanguage, cboxApplicationLanguage;
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resourceBundle = resourceBundle;
        setValuesInComboBox();
        setupComboBox();
        setValuesInControls();
        btnSave.setText("Next");
        btnSave1.setText("Next");
        btnSave.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_GENERAL_CONFIG_SAVE"))
                throw new UnAuthorizedAccessException();
            tabPane.getSelectionModel().select(tabProductConfig);
        });
        btnSave1.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_GENERAL_CONFIG_SAVE"))
                throw new UnAuthorizedAccessException();
            tabPane.getSelectionModel().select(tabPaymentMode);
        });
        btnSave2.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_GENERAL_CONFIG_SAVE"))
                throw new UnAuthorizedAccessException();
            updateProperty();
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnClose1.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnClose2.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnBrowse.setOnAction(e -> {
            File file = CommonUtils.openDirectoryDialog("Application");
            if (file != null)
                txtBackupPath.setText(file.getAbsolutePath());
        });

        txtLtrToKg.textProperty().addListener((observable, oldValue, newValue) -> {

        });
        txtSampleSize.textProperty().addListener((observable, oldValue, newValue) -> {
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                System.out.println(newValue.getId());
            }
        });

        txtAvgPIfMachineOff.setDisable(!chkAvgParam.isSelected());
        chkAvgParam.setOnAction(e -> {
            txtAvgPIfMachineOff.setDisable(!chkAvgParam.isSelected());
        });
        chkAvgParam.selectedProperty().addListener((observable, oldValue, newValue) -> {
            txtAvgPIfMachineOff.setDisable(!newValue.equals(true));
        });


        btnBackup.setOnAction(e -> {
            String backupPath = MainApp.getProperty("backuppath", null).replace(" ", "");
            if (backupPath == null || backupPath.isEmpty())
                return;
            var task = new DbBackupTask(backupPath);
            task.setOnSucceeded(ee -> {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("backup"),
                        resourceBundle.getString("successful"));
                alert.createAlert();
            });
            new Thread(task).start();
        });
    }

    private void setValuesInControls() {
        txtLtrToKg.setText(MainApp.getProperty("ltr.to.kg", "1.03"));
        txtClrConst1.setText(MainApp.getProperty("clr.const1", "0.21"));
        txtClrConst2.setText(MainApp.getProperty("clr.const2", "0.66"));
        cboxDefaultSnf.setValue(MainApp.getProperty("default.snf", "1").equalsIgnoreCase("0") ? "No" : "Yes");
        txtDefaultSnfValue.setText(MainApp.getProperty("deafult.snf.value", "0"));
        cboxWeightSetting.setValue(setWeightSetting(MainApp.getProperty("qty.reading.rounding", "0")));
        cboxQualitySetting.setValue(MainApp.getProperty("quality.reading.rounding", "0").equalsIgnoreCase("0") ? "Round" : "Truncate");
        txtSampleSize.setText(MainApp.getProperty("sample.milk.size", "0"));
        chkAcceptOtherMilk.setSelected(MainApp.getProperty("accept.milktype.otherthen.default", "1").equalsIgnoreCase("1"));
        chkAllowMultiEntry.setSelected(MainApp.getProperty("allow.multipleentry.samemilktype", "1").equalsIgnoreCase("1"));
        chkAllowMultiEntryDiffType.setSelected(MainApp.getProperty("allow.multipleentry.diffmilktype", "1").equalsIgnoreCase("1"));
        cboxMemberCollectionQtyMode.setValue(MainApp.getProperty("member.collection.qty.mode", "0").equalsIgnoreCase("0") ? resourceBundle.getString("liter") : resourceBundle.getString("kg"));
        cboxBmcCollectionQtyMode.setValue(MainApp.getProperty("society.collection.qty.mode", "0").equalsIgnoreCase("0") ? resourceBundle.getString("liter") : resourceBundle.getString("kg"));
        cboxLocalMilkSaleQtyMode.setValue(MainApp.getProperty("milksale.qty.mode", "0").equalsIgnoreCase("0") ? resourceBundle.getString("liter") : resourceBundle.getString("kg"));
        cboxDispatchMilkQtyMode.setValue(MainApp.getProperty("dispatch.qty.mode", "1").equalsIgnoreCase("1") ? resourceBundle.getString("kg") : resourceBundle.getString("litre"));
        cboxReceiptMilkQtyMode.setValue(MainApp.getProperty("receipt.qty.mode", "1").equalsIgnoreCase("1") ? resourceBundle.getString("kg") : resourceBundle.getString("litre"));
        chkPurchaseRate.setSelected(!MainApp.getProperty("product.purchaserate", "0").equalsIgnoreCase("0"));
        chkSaleRate.setSelected(!MainApp.getProperty("product.salerate", "0").equalsIgnoreCase("0"));
        chkAllowZeroDispatch.setSelected(!MainApp.getProperty("zero.amount.dispatch", "0").equalsIgnoreCase("0"));
        chkPaymentMode.setSelected(MainApp.getProperty("allow.cashpayment", "1").equalsIgnoreCase("1"));
        cboxPaymentMode.setValue(MainApp.getProperty("payment.mode", "2").equalsIgnoreCase("2") ? "Local disburse" : MainApp.getProperty("payment.mode", "2").equalsIgnoreCase("0") ? "Society Bank" : "Union Bank");
        cboxPaymentOption.setValue(MainApp.getProperty("payment.option", "0").equalsIgnoreCase("0") ? "Actual amount" : "Decimal truncate");

        txtVariationQty.setText(MainApp.getProperty("variation.qty", "20.0"));
        txtVariationFat.setText(MainApp.getProperty("variation.fat", "30.0"));
        txtVariationSnf.setText(MainApp.getProperty("variation.snf", "30.0"));

//        cboxShift.setValue(MainApp.getProperty("shift.param", "All").equalsIgnoreCase("All") ? "All" : "Morning/Evening");
//        cboxAvgBasedOn.setValue(
//                MainApp.getProperty("based.on.param", "Shift").equalsIgnoreCase("Shift") ? "Shift" :
//                        MainApp.getProperty("based.on.param", "Shift").equalsIgnoreCase("Day") ? "Day" : "Payment Cycle"
//        );
        txtNo.setText(MainApp.getProperty("variation.no.param", "5"));
        chkBlockQty.setSelected(MainApp.getProperty("variation.qty.block", "0").equalsIgnoreCase("1"));
        chkBlockFat.setSelected(MainApp.getProperty("variation.fat.block", "0").equalsIgnoreCase("1"));
        chkBlockSnf.setSelected(MainApp.getProperty("variation.snf.block", "0").equalsIgnoreCase("1"));

        // txtAvgPBasedOnPrevShift.setText(MainApp.getProperty("avg.param.capture.shift.value", "5"));
        txtAvgPBasedOnPrevShift.setText(MainApp.getProperty("avg.param.prev.shiftcount", "5"));
        txtAvgPIfMachineOff.setText(MainApp.getProperty("avg.param.capture.value", "5"));
        chkAvgParam.setSelected(MainApp.getProperty("avg.param.capture", "1").equalsIgnoreCase("1"));
        txtDecimalValue.setText(MainApp.getProperty("decimalvalue", "2"));
        txtHrs.setText(MainApp.getProperty("hrs", "72"));
        txtSampleMilk.setText(MainApp.getProperty("samplemilk", ""));
        txtBackupPath.setText(MainApp.getProperty("backuppath", ""));
        txtSpace.setText(MainApp.getProperty("no.of.enter", "0"));
        txtCollectionSlip.setText(MainApp.getProperty("no.of.enters.collection.slip", "0"));
        chkCodeMilkTypeParsing.setSelected(MainApp.getProperty("code.milktype.parsing", "0").equalsIgnoreCase("1"));
        cboxSlipLanguage.getSelectionModel().select(MainApp.getProperty("slip.language", ""));
        cboxApplicationLanguage.getSelectionModel().select(MainApp.getProperty("application.language", "English"));
        try {
            cboxQualityMachine.getSelectionModel().select(arrQuality[Integer.parseInt(MainApp.getProperty("masetting", "")) - 1]);
        } catch (Exception e) {
            cboxQualityMachine.getSelectionModel().select(0);
        }
    }

    private List<String> writeAppProperty() {
        List<String> lines = new ArrayList<>();
        lines.add("baseurl=" + new String(Base64.getEncoder().encode(MainApp.getProperty("baseurl", "http://localhost:8080/eipl-amcs/").getBytes())));
        lines.add("baseurl.realtime=" + new String(Base64.getEncoder().encode(AppConstant.baseUrlRealTime.getBytes())));
        lines.add("client.code=" + new String(Base64.getEncoder().encode(MainApp.getProperty("client.code", null).getBytes())));
        lines.add("syncUrl.realtime=" + new String(Base64.getEncoder().encode(AppConstant.syncUrlRealTime.getBytes())));
        lines.add("app.request.debug=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("#Languages");
        lines.add("app.languages=" + new String(Base64.getEncoder().encode("English,Gujarati,Hindi".getBytes(StandardCharsets.UTF_8))));
        lines.add("#Identity details");
        lines.add("identity.union=" + new String(Base64.getEncoder().encode(MainApp.identityDto.getUnion().getCode().getBytes())));
        lines.add("identity.society=" + new String(Base64.getEncoder().encode(MainApp.identityDto.getSociety().getCode().getBytes())));
        lines.add("identity.dock=" + new String(Base64.getEncoder().encode(MainApp.identityDto.getDock().getDockNo().getBytes())));
        lines.add("identity.id=" + new String(Base64.getEncoder().encode(MainApp.systemId.getBytes())));
        lines.add("identity.version=" + new String(Base64.getEncoder().encode(AppConstant.versionNo.getBytes())));
        lines.add("identity.activation=" + new String(Base64.getEncoder().encode(MainApp.getProperty("identity.activation", "").getBytes())));
        lines.add("updater.url=" + new String(Base64.getEncoder().encode("http://client.emilkpro.in/webservice/eipl/v1/free-access/latest-app".getBytes())));
        lines.add("#Configurations");
        lines.add("default.creditlimit=" + new String(Base64.getEncoder().encode("100000".getBytes())));
        lines.add("ltr.to.kg=" + new String(Base64.getEncoder().encode((txtLtrToKg.getText().trim() != null ? txtLtrToKg.getText() : "1.028").getBytes())));
        lines.add("clr.const1=" + new String(Base64.getEncoder().encode((txtClrConst1.getText().trim() != null ? txtClrConst1.getText() : "0.21").getBytes())));
        lines.add("clr.const2=" + new String(Base64.getEncoder().encode((txtClrConst2.getText().trim() != null ? txtClrConst2.getText() : "0.66").getBytes())));
        lines.add("hrs=" + new String(Base64.getEncoder().encode((txtHrs.getText().trim() != null ? txtHrs.getText() : "72").getBytes())));
        lines.add("default.snf=" + new String(Base64.getEncoder().encode((cboxDefaultSnf.getValue().equalsIgnoreCase("no") ? "0" : "1").getBytes())));
        lines.add("deafult.snf.value=" + new String(Base64.getEncoder().encode(((txtDefaultSnfValue.isDisable() || txtDefaultSnfValue.getText().trim() == null) ? "0" : txtDefaultSnfValue.getText().trim()).getBytes())));
        lines.add("qty.reading.rounding=" + new String(Base64.getEncoder().encode((cboxWeightSetting.getValue() != null ? getWeightSetting(cboxWeightSetting.getValue()) : "0").getBytes())));
        lines.add("quality.reading.rounding=" + new String(Base64.getEncoder().encode((cboxQualitySetting.getValue() == null || cboxQualitySetting.getValue().equalsIgnoreCase("Round") ? "0" : "1").getBytes())));
        lines.add("sample.milk.size=" + new String(Base64.getEncoder().encode((txtSampleSize.getText().trim() != null ? txtSampleSize.getText().trim() : "0").getBytes())));
        lines.add("accept.milktype.otherthen.default=" + new String(Base64.getEncoder().encode((chkAcceptOtherMilk.isSelected() ? "1" : "0").getBytes())));
        lines.add("allow.multipleentry.samemilktype=" + new String(Base64.getEncoder().encode((chkAllowMultiEntry.isSelected() ? "1" : "0").getBytes())));
        lines.add("allow.multipleentry.diffmilktype=" + new String(Base64.getEncoder().encode((chkAllowMultiEntryDiffType.isSelected() ? "1" : "0").getBytes())));
        lines.add("member.collection.qty.mode=" + new String(Base64.getEncoder().encode((cboxMemberCollectionQtyMode.getValue() == null || cboxMemberCollectionQtyMode.getValue().equalsIgnoreCase(resourceBundle.getString("liter")) ? "0" : "1").getBytes())));
        lines.add("society.collection.qty.mode=" + new String(Base64.getEncoder().encode((cboxBmcCollectionQtyMode.getValue() == null || cboxBmcCollectionQtyMode.getValue().equalsIgnoreCase(resourceBundle.getString("liter")) ? "0" : "1").getBytes())));
        lines.add("milksale.qty.mode=" + new String(Base64.getEncoder().encode((cboxLocalMilkSaleQtyMode.getValue() == null || cboxLocalMilkSaleQtyMode.getValue().equalsIgnoreCase(resourceBundle.getString("liter")) ? "0" : "1").getBytes())));
        lines.add("dispatch.qty.mode=" + new String(Base64.getEncoder().encode((cboxDispatchMilkQtyMode.getValue() == null || cboxDispatchMilkQtyMode.getValue().equalsIgnoreCase(resourceBundle.getString("kg")) ? "1" : "0").getBytes())));
        lines.add("receipt.qty.mode=" + new String(Base64.getEncoder().encode((cboxReceiptMilkQtyMode.getValue() == null || cboxReceiptMilkQtyMode.getValue().equalsIgnoreCase(resourceBundle.getString("kg")) ? "1" : "0").getBytes())));
        lines.add("samplemilk=" + new String(Base64.getEncoder().encode(txtSampleMilk.getText().getBytes())));

        lines.add("no.of.enter=" + new String(Base64.getEncoder().encode(txtSpace.getText().getBytes(StandardCharsets.UTF_8))));
        lines.add("no.of.enters.collection.slip=" + new String(Base64.getEncoder().encode(txtCollectionSlip.getText().getBytes(StandardCharsets.UTF_8))));
        lines.add("zero.amount.dispatch=" + new String(Base64.getEncoder().encode((chkAllowZeroDispatch.isSelected() ? "1" : "0").getBytes())));

        lines.add("avg.param.prev.shiftcount=" + new String(Base64.getEncoder().encode(CommonUtils.isNumeric(txtAvgPBasedOnPrevShift.getText()) ? txtAvgPBasedOnPrevShift.getText().trim().getBytes() : "0".getBytes())));
        lines.add("avg.param.capture=" + new String(Base64.getEncoder().encode((chkAvgParam.isSelected() ? "1" : "0").getBytes())));
        lines.add("avg.param.capture.shift.value=" + new String(Base64.getEncoder().encode(((txtAvgPIfMachineOff.isDisable() || txtAvgPIfMachineOff.getText().trim() == null) ? "5" : txtAvgPIfMachineOff.getText().trim()).getBytes())));

        lines.add("#Billing configuration");
        lines.add("allow.billingmilkamountzero=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("allow.cashpayment=" + new String(Base64.getEncoder().encode((chkPaymentMode.isSelected() ? "1" : "0").getBytes())));
        lines.add("#0-Society Bank, 1-Union Bank, 2-Local disburse");
        lines.add("payment.mode=" + new String(Base64.getEncoder().encode((cboxPaymentMode.getValue() == null || cboxPaymentMode.getValue().equalsIgnoreCase("Local disburse") ? "2" :
                cboxPaymentMode.getValue().equalsIgnoreCase("Society Bank") ? "0" : "1").getBytes())));
        lines.add("#0-actual amount, 1-decimal truncate");
        lines.add("payment.option=" + new String(Base64.getEncoder().encode((cboxPaymentOption.getValue().equalsIgnoreCase("decimal truncate") ? "1" : "0").getBytes())));
        lines.add("product.purchaserate=" + new String(Base64.getEncoder().encode((chkPurchaseRate.isSelected() ? "1" : "0").getBytes())));
        lines.add("product.salerate=" + new String(Base64.getEncoder().encode((chkSaleRate.isSelected() ? "1" : "0").getBytes())));

        lines.add("slip.language=" + new String(Base64.getEncoder().encode(cboxSlipLanguage.getValue().getBytes())));
        lines.add("application.language=" + new String(Base64.getEncoder().encode(cboxApplicationLanguage.getValue().getBytes())));
        lines.add("backuppath=" + new String(Base64.getEncoder().encode((txtBackupPath.getText() == null || txtBackupPath.getText().isEmpty() ? "" : txtBackupPath.getText()).getBytes())));
        lines.add("masetting=" + new String(Base64.getEncoder().encode(String.valueOf(cboxQualityMachine.getSelectionModel().getSelectedIndex() + 1).getBytes())));
        lines.add("decimalvalue=" + new String(Base64.getEncoder().encode(txtDecimalValue.getText().getBytes())));

        lines.add("variation.qty.block=" + new String(Base64.getEncoder().encode((chkBlockQty.isSelected() ? "1" : "0").getBytes())));
        lines.add("variation.fat.block=" + new String(Base64.getEncoder().encode((chkBlockFat.isSelected() ? "1" : "0").getBytes())));
        lines.add("variation.snf.block=" + new String(Base64.getEncoder().encode((chkBlockSnf.isSelected() ? "1" : "0").getBytes())));
        lines.add("variation.qty=" + new String(Base64.getEncoder().encode(txtVariationQty.getText().trim().getBytes())));
        lines.add("variation.fat=" + new String(Base64.getEncoder().encode(txtVariationFat.getText().trim().getBytes())));
        lines.add("variation.snf=" + new String(Base64.getEncoder().encode(txtVariationSnf.getText().trim().getBytes())));
        lines.add("code.milktype.parsing=" + new String(Base64.getEncoder().encode((chkCodeMilkTypeParsing.isSelected() ? "1" : "0").getBytes())));

//        lines.add("shift.param=" + new String(Base64.getEncoder().encode(
//                (cboxShift.getValue() != null ? cboxShift.getValue() : "").getBytes(StandardCharsets.UTF_8))));
//        lines.add("based.on.param=" + new String(Base64.getEncoder().encode(
//                (cboxAvgBasedOn.getValue() != null ? cboxAvgBasedOn.getValue() : "").getBytes(StandardCharsets.UTF_8))));
        lines.add("variation.no.param=" + new String(Base64.getEncoder().encode(txtNo.getText().trim().getBytes())));
        return lines;
    }

    private void updateProperty() {
        try {
            appProperty = new File("resources/app.properties");
            old = new File("resources/old_app.properties");
            if (appProperty.renameTo(old)) {
                Files.write(appProperty.toPath(), writeAppProperty(), StandardCharsets.UTF_8);
                saveData();
                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("generalconfiguration"),
                        resourceBundle.getString("alert.propertychange"));
                Optional<ButtonType> resp = alert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    Files.deleteIfExists(Path.of("resources/old_app.properties"));
                    System.exit(0);
                } else {
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
                }
            }
        } catch (IOException | NullPointerException ex) {
            if (old.renameTo(appProperty)) {
                System.out.println("Changes not applied");
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("generalconfiguration"),
                        resourceBundle.getString("changes.not.applied"));
                alert.createAlert();
            }
        }
    }

    String getWeightSetting(String s) {
        if (s.equalsIgnoreCase("Single Digit Truncate")) {
            return "0";
        } else if (s.equalsIgnoreCase("Double Digit Truncate")) {
            return "1";
        } else if (s.equalsIgnoreCase("Single Digit Round")) {
            return "2";
        } else if (s.equalsIgnoreCase("Double Digit Round")) {
            return "3";
        } else {
            return "0";
        }
    }

    @Override
    public void saveData() {
        MainApp.loadProperties();
        List<GeneralConfig> list = new ArrayList<>();
        MainApp.properties.forEach((k, v) -> {
            GeneralConfig generalConfig = new GeneralConfig();
            generalConfig.setKey((String) k);
            generalConfig.setValue((String) v);
            generalConfig.setSociety(MainApp.identityDto.getSociety());
            list.add(generalConfig);
        });
        System.out.println(list);
        var task = new GeneralConfigSaveTask(list);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("generalconfig"),
                            sb.toString());
                    alert.createAlert();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    String setWeightSetting(String s) {
        if (s.equalsIgnoreCase("0")) {
            return "Single Digit Truncate";
        } else if (s.equalsIgnoreCase("1")) {
            return "Double Digit Truncate";
        } else if (s.equalsIgnoreCase("2")) {
            return "Single Digit Round";
        } else if (s.equalsIgnoreCase("3")) {
            return "Double Digit Round";
        } else {
            return "Single Digit Truncate";
        }
    }

    private void setValuesInComboBox() {
        cboxDefaultSnf.setItems(FXCollections.observableArrayList("No", "Yes"));
        cboxWeightSetting.setItems(FXCollections.observableArrayList("Single Digit Truncate", "Double Digit Truncate",
                "Single Digit Round", "Double Digit Round"));
        cboxQualitySetting.setItems(FXCollections.observableArrayList("Round", "Truncate"));
        cboxMemberCollectionQtyMode.getItems().addAll(resourceBundle.getString("litre"), resourceBundle.getString("kg"));
        cboxBmcCollectionQtyMode.getItems().addAll(resourceBundle.getString("litre"), resourceBundle.getString("kg"));
        cboxLocalMilkSaleQtyMode.getItems().addAll(resourceBundle.getString("litre"), resourceBundle.getString("kg"));
        cboxDispatchMilkQtyMode.getItems().addAll(resourceBundle.getString("litre"), resourceBundle.getString("kg"));
        cboxReceiptMilkQtyMode.getItems().addAll(resourceBundle.getString("litre"), resourceBundle.getString("kg"));
        cboxPaymentMode.setItems(FXCollections.observableArrayList("Society Bank", "Union Bank", "Local disburse"));
        cboxPaymentOption.setItems(FXCollections.observableArrayList("Actual amount", "Decimal truncate"));

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "English").split(",");
        cboxSlipLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxApplicationLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxQualityMachine.setItems(FXCollections.observableList(Arrays.asList(arrQuality)));
//        cboxShift.setItems(FXCollections.observableList(Arrays.asList(arrQuality)));
//        cboxAvgBasedOn.setItems(FXCollections.observableList(Arrays.asList(arrQuality)));
//        cboxShift.setItems(FXCollections.observableArrayList("All", "Morning/Evening"));
//        cboxAvgBasedOn.setItems(FXCollections.observableArrayList("Shift", "Day", "Payment Cycle"));
    }

    @Override
    public void setupComboBox() {
        cboxDefaultSnf.setOnAction(event -> {
            if (cboxDefaultSnf.getSelectionModel().getSelectedIndex() == 0) {
                txtDefaultSnfValue.setDisable(true);
                txtDefaultSnfValue.setText("0.0");
            } else {
                txtDefaultSnfValue.setDisable(false);
            }
        });
        cboxPaymentMode.setOnAction(event -> {
            chkPaymentMode.setSelected(cboxPaymentMode.getSelectionModel().getSelectedIndex() == 2);
        });
    }
}