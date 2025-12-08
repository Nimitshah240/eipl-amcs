package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.task.VerificationTask;
import com.eipl.amcs.auth.task.VerifyIdentityTask;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.base.task.IdentityCheckTask;
import com.eipl.amcs.base.task.IdentitySaveTask;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.setting.model.GeneralConfig;
import com.eipl.amcs.setting.task.GeneralConfigSaveTask;
import com.eipl.amcs.utils.ActivationUtil;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.task.MemberCreateTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ActivationController implements MyInitialization {

    public static final Properties properties1 = new Properties();
    @FXML
    private StackPane root;
    @FXML
    private TextField txtServerDetail, txtUnion, txtSociety, txtDock, txtCowRange, txtBuffRange, txtSampleMilkNo, txtClientCode;
    @FXML
    private Button btnActivate;
    @FXML
    private Label lblSampleNo;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg;
    private String union, society, dock, activationKey, sampleNo;
    private boolean societyCheckFlag = false;
    private boolean dockCheckFlag = false;
    private boolean validateCheckFlag = false;
    private AppConstant.ClientCode clientCode;
    private String baseUrlRealTime;
    private String syncUrlRealTime;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resourceBundle = resourceBundle;
        txtServerDetail.setText("http://localhost:8080/eipl-amcs/");

        btnActivate.setOnAction(e -> {
            try {
                clientCode = AppConstant.ClientCode.valueOf(txtClientCode.getText().toUpperCase());
            } catch (Exception ex) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                        resourceBundle.getString("clientcodewrongerror"));
                alert.createAlert();
            }
            setFlag();
            if (validateCheckFlag) {
                try {
                    CompletableFuture<String> future = verifyIdentityAsync();
                    future.thenAccept(resp -> {
                        if (resp == null) {
                            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                                    resourceBundle.getString("error.occurred"));
                            alert.createAlert();
                            return;
                        }
                        baseUrlRealTime = resp.split("#")[0];
                        syncUrlRealTime = resp.split("#")[1];
                        System.out.println("Successfully received baseUrl: " + resp);
                        validateAndMakeFile();
                    }).exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
                } catch (Exception exe) {
                    throw new RuntimeException(exe);
                }

            } else {
                errorMsg = new StringBuilder();
                if (!validate()) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                            errorMsg.toString());
                    alert.createAlert();
                    return;
                }

                this.union = txtUnion.getText();
                if (!societyCheckFlag) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                            "Please Enter Valid Society No.");
                    alert.createAlert();
                    return;
                }
                this.society = txtSociety.getText();
                if (!dockCheckFlag) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                            "Please Enter Valid Dock No.");
                    alert.createAlert();
                    return;
                }
                this.dock = txtDock.getText();
                this.sampleNo = txtSampleMilkNo.getText();
                txtServerDetail.setText(txtServerDetail.getText().replace("localhost", txtSampleMilkNo.getText()));
                CompletableFuture<String> future = verifyIdentityAsync();
                future.thenAccept(resp -> {
                    if (resp == null) {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                                resourceBundle.getString("error.occurred"));
                        alert.createAlert();
                        return;
                    }
                    baseUrlRealTime = resp.split("#")[0];
                    syncUrlRealTime = resp.split("#")[1];
                    makeFile();
                    confirmAndClose();
                    System.out.println("Successfully received baseUrl: " + resp);
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
            }
        });
        txtDock.textProperty().addListener((observable, oldValue, newValue) -> {
            setFlag();
            if (dockCheckFlag) {
                if (!validateCheckFlag) {
                    lblSampleNo.setText("Server Details");
                    txtCowRange.setDisable(true);
                    txtBuffRange.setDisable(true);
                } else {
                    lblSampleNo.setText("Sample No.");
                    txtCowRange.setDisable(false);
                    txtBuffRange.setDisable(false);
                }
            }
        });
    }

    public void loadProperties() {
        Properties prop = new Properties();
        try (InputStream inputStream = new FileInputStream("resources/app.properties")) {
            prop.load(inputStream);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (prop != null && !prop.isEmpty()) {
            prop.forEach((k, v) -> properties1.put(k, new String(Base64.getDecoder().decode(v.toString().getBytes()))));
        }
        saveData();
    }

    @Override
    public void saveData() {

        List<GeneralConfig> list = new ArrayList<>();
        properties1.forEach((k, v) -> {
            GeneralConfig generalConfig = new GeneralConfig();
            generalConfig.setKey((String) k);
            generalConfig.setValue((String) v);
            list.add(generalConfig);
        });
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

    private void validateAndMakeFile() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        setFlag();
        this.union = txtUnion.getText();
        if (!societyCheckFlag) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    "Please Enter Valid Society No.");
            alert.createAlert();
            return;
        }
        this.society = txtSociety.getText();
        if (!dockCheckFlag) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    "Please Enter Valid Dock No.");
            alert.createAlert();
            return;
        }
        this.dock = txtDock.getText();
        this.sampleNo = txtSampleMilkNo.getText();
        createMembers();
        makeFile();
    }

    private void createMembers() {
        try {

            if (txtCowRange.getText() == null || txtCowRange.getText().isEmpty() ||
                    txtBuffRange.getText() == null || txtBuffRange.getText().isEmpty())
                return;
            String[] arrCow = txtCowRange.getText().split("-");
            String[] arrBuff = txtBuffRange.getText().split("-");
            int cowMin = CommonUtils.strToInteger(arrCow[0]);
            int cowMax = CommonUtils.strToInteger(arrCow[1]);
            int buffMin = CommonUtils.strToInteger(arrBuff[0]);
            int buffMax = CommonUtils.strToInteger(arrBuff[1]);

            MainApp.paneDrop.setVisible(true);
            var task = new MemberCreateTask(this.society, txtServerDetail.getText(), cowMin, cowMax, buffMin, buffMax,
                    CommonUtils.strToInteger(txtSampleMilkNo.getText()));
            task.setOnSucceeded(e -> callApi());
            new Thread(task).start();
            MainApp.lblMessage.textProperty().bind(task.messageProperty());

        } catch (RuntimeException e) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    "Please Enter Valid Range.");
            alert.createAlert();
        }
    }


    private void callApi() {
        var task = new IdentityCheckTask(txtSociety.getText(), baseUrlRealTime);
        task.setOnSucceeded(e -> {
            try {
                Identity identity = new Identity();

                Map<String, Object> data = task.get();

                if (data != null) {
                    identity.setSocietyRefCode((String) data.get("orgPkCode"));
                    identity.setToken((String) data.get("token"));
                }
                identity.setDockNo(txtDock.getText());
                identity.setSocietyCode(txtSociety.getText());
                identity.setSystemMac(MainApp.getProperty("identity.id", ""));

                var verificationRunnable = new VerificationTask(txtSociety.getText(), identity.getToken(), baseUrlRealTime);
                var identitySaveRunnable = new IdentitySaveTask(identity);

                CompletableFuture<Void> comptask1 = CompletableFuture.runAsync(verificationRunnable);
                CompletableFuture<Void> comptask2 = CompletableFuture.runAsync(identitySaveRunnable);

                CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(comptask1, comptask2);
                combinedFuture.join();

                System.out.println("SUCCESS: All tasks are finished.");
                System.exit(0);
            } catch (Exception exception) {
                exception.printStackTrace();
                System.exit(0);
            }
        });
        new Thread(task).start();
    }

    private void confirmAndClose() {
        callApi();
    }

    private void makeFile() {
        try {
            File appProperties = new File("resources/app.properties");
            if (appProperties.createNewFile()) {
                Files.write(appProperties.toPath(), writeAppProperty(), StandardCharsets.UTF_8);
                System.out.println("Created app.properties");
                loadProperties();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean validate() {

        if (txtUnion.getText().trim() == null || !CommonUtils.isNumeric(txtUnion.getText().trim())) {
            errorMsg.append(resourceBundle.getString("unionnullerror") + "\n");
        }
        if (txtSociety.getText().trim() == null || !CommonUtils.isNumeric(txtSociety.getText().trim())) {
            errorMsg.append(resourceBundle.getString("societynullerror") + "\n");
        }
        if (txtDock.getText().trim() == null || !CommonUtils.isNumeric(txtDock.getText().trim())) {
            errorMsg.append(resourceBundle.getString("docknullerror") + "\n");
        }
        if (clientCode == null) {
            errorMsg.append(resourceBundle.getString("clientcodenullerror") + "\n");
        }
        return errorMsg.length() == 0;

    }

    private List<String> writeAppProperty() {
        List<String> lines = new ArrayList<>();
        lines.add("baseurl=" + new String(Base64.getEncoder().encode(txtServerDetail.getText().getBytes(StandardCharsets.UTF_8))));
        lines.add("baseurl.realtime=" + new String(Base64.getEncoder().encode(baseUrlRealTime.getBytes(StandardCharsets.UTF_8))));
        lines.add("client.code=" + new String(Base64.getEncoder().encode((clientCode.toString().toUpperCase()).getBytes())));
        lines.add("syncUrl.realtime=" + new String(Base64.getEncoder().encode(syncUrlRealTime.getBytes(StandardCharsets.UTF_8))));
        lines.add("app.request.debug=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("#Languages");
        lines.add("app.languages=" + new String(Base64.getEncoder().encode("English,Gujarati,Hindi".getBytes(StandardCharsets.UTF_8))));
        lines.add("#Identity details");
        lines.add("identity.union=" + new String(Base64.getEncoder().encode(union.getBytes())));
        lines.add("identity.society=" + new String(Base64.getEncoder().encode(society.getBytes())));
        lines.add("identity.dock=" + new String(Base64.getEncoder().encode(dock.getBytes())));
        lines.add("identity.id=" + new String(Base64.getEncoder().encode("ABC".getBytes())));
        lines.add("identity.version=" + new String(Base64.getEncoder().encode(AppConstant.versionNo.getBytes())));
        lines.add("updater.url=" + new String(Base64.getEncoder().encode("http://client.emilkpro.in/webservice/eipl/v1/free-access/latest-app".getBytes())));
        lines.add("identity.activation=" + new String(Base64.getEncoder().encode("ABC".getBytes())));
        lines.add("#Configurations");
        lines.add("default.creditlimit=" + new String(Base64.getEncoder().encode("10000".getBytes())));
        lines.add("ltr.to.kg=" + new String(Base64.getEncoder().encode("1.03".getBytes())));
        lines.add("clr.const1=" + new String(Base64.getEncoder().encode("0.21".getBytes())));
        lines.add("clr.const2=" + new String(Base64.getEncoder().encode("0.66".getBytes())));
        lines.add("default.snf=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("deafult.snf.value=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("hrs=" + new String(Base64.getEncoder().encode("72".getBytes())));
        lines.add("qty.reading.rounding=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("quality.reading.rounding=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("sample.milk.size=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("accept.milktype.otherthen.default=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("allow.multipleentry.samemilktype=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("allow.multipleentry.diffmilktype=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("member.collection.qty.mode=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("society.collection.qty.mode=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("milksale.qty.mode=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("dispatch.qty.mode=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("receipt.qty.mode=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("no.of.enter=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("no.of.enters.collection.slip=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("zero.amount.dispatch=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("samplemilk=" + new String(Base64.getEncoder().encode(txtSampleMilkNo.getText().getBytes())));


        lines.add("avg.param.prev.shiftcount=" + new String(Base64.getEncoder().encode("5".getBytes())));
        lines.add("avg.param.capture=" + new String(Base64.getEncoder().encode(("0".getBytes()))));
        lines.add("avg.param.capture.shift.value=" + new String(Base64.getEncoder().encode("3".getBytes())));


        lines.add("#Billing configuration");
        lines.add("allow.billingmilkamountzero=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("allow.cashpayment=" + new String(Base64.getEncoder().encode("1".getBytes())));
        lines.add("#0-Society Bank, 1-Union Bank, 2-Local disburse");
        lines.add("payment.mode=" + new String(Base64.getEncoder().encode("2".getBytes())));
        lines.add("#0-actual amount, 1-decimal truncate");
        lines.add("payment.option=" + new String(Base64.getEncoder().encode("0".getBytes())));

        lines.add("slip.language=" + new String(Base64.getEncoder().encode("English".getBytes())));
        lines.add("application.language=" + new String(Base64.getEncoder().encode("Hindi".getBytes())));

        File file = new File("D:");
        if (file.exists())
            lines.add("backuppath=" + new String(Base64.getEncoder().encode(("D:\\backup").getBytes())));
        else
            lines.add("backuppath=" + new String(Base64.getEncoder().encode(("E:\\backup").getBytes())));
        lines.add("masetting=" + new String(Base64.getEncoder().encode(("Single MA").getBytes())));
        lines.add("product.purchaserate=" + new String(Base64.getEncoder().encode(("0").getBytes())));
        lines.add("product.salerate=" + new String(Base64.getEncoder().encode(("0").getBytes())));

        return lines;
    }

    private String appKeyGenerator() {
        try {
            String appGetKey = "1814";
            appGetKey = (ActivationUtil.decrypt(appGetKey,
                    "76599" + union + "88121659"));
            String keyStr = "";
            for (Character ch : appGetKey.toCharArray()) {
                if (Character.isDigit(ch)) {
                    keyStr += ch;
                } else if (Character.isAlphabetic(ch)) {
                    keyStr += (int) ch;
                }
            }
            long key = Long.parseLong(keyStr);
            String mo = ActivationUtil.decrypt(sampleNo);
            mo = "1" + mo.substring(1);
            long mobileNo = Long.parseLong(mo);
            long dcsCode = Long.parseLong(society);
            long date = Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            long res = (mobileNo / dcsCode) * (date / key);
            res = res + mobileNo + dcsCode + date + key;
            StringBuilder response = new StringBuilder(String.valueOf(res));

            int last2 = Integer.parseInt(response.substring(response.length() - 2));
            response = new StringBuilder(response.substring(0, response.length() - 2));
            response.insert(2, last2 < 65 ? "A" : last2 > 90 ? "Z" : (char) last2);
            last2 = Integer.parseInt(response.substring(response.length() - 2));
            response = new StringBuilder(response.substring(0, response.length() - 2));
            response.append(last2 < 65 ? "A" : last2 > 90 ? "Z" : (char) last2);

            // Step-2 Generate Encryption using new key
            String data = ActivationUtil.decrypt(this.sampleNo)
                    + society
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + appGetKey;
            String finalKey = response.toString();
            String tempKey = finalKey + finalKey + finalKey + finalKey + finalKey;
            finalKey = tempKey.substring(0, 16);
            String resultForKeyGen = ActivationUtil.encrypt(data, finalKey);
            if (resultForKeyGen != null)
                resultForKeyGen = resultForKeyGen.substring(resultForKeyGen.length() - 16);
            resultForKeyGen = resultForKeyGen.replaceAll("[^a-zA-Z0-9]", "");
            System.out.println(resultForKeyGen);
            return resultForKeyGen.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setFlag() {
        try {
            societyCheckFlag = txtSociety.getText().length() >= 7;
            dockCheckFlag = txtDock.getText().length() >= 9;
            validateCheckFlag = txtDock.getText().substring(txtSociety.getText().length()).equalsIgnoreCase("01");

//            TODO Remove this before production - NIMIT
            switch (clientCode) {
                case AMULAMCS:
                    break;
                case JAIPURAMCS:
                    break;
                case LACTALIS_QA:
                    validateCheckFlag = true;
                    societyCheckFlag = true;
                    dockCheckFlag = true;
                    break;
            }
        } catch (Exception e) {
            System.out.println("error : " + e);
        }
    }

    private CompletableFuture<String> verifyIdentityAsync() {
        CompletableFuture<String> futureBaseUrl = new CompletableFuture<>();
        var task = new VerifyIdentityTask(txtClientCode.getText());
        task.setOnSucceeded(ee -> {
            try {
                String baseUrls = task.get();
                futureBaseUrl.complete(baseUrls);
            } catch (InterruptedException | ExecutionException ex) {
                futureBaseUrl.completeExceptionally(ex);
            }
        });

        task.setOnFailed(ee -> {
            futureBaseUrl.completeExceptionally(task.getException());
        });

        new Thread(task).start();
        return futureBaseUrl;
    }
}
