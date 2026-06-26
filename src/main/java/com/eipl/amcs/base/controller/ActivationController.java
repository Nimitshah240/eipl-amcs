package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.task.IdentityTask;
import com.eipl.amcs.auth.task.VerifyIdentityTask;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.base.task.IdentityCheckTask;
import com.eipl.amcs.base.task.IdentitySaveTask;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.operation.task.MemberDownloadTask;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.org.service.DockService;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import com.eipl.amcs.operation.inventory.repository.ProductStockRepository;
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
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static com.eipl.amcs.utils.AppConstant.*;

public class ActivationController implements MyInitialization, PopupCallback {

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

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.resourceBundle = resourceBundle;
        txtServerDetail.setText("http://localhost:8080/eipl-amcs/");
        checkActivation();
        btnActivate.setOnAction(e -> {
            MainApp.paneDrop.setVisible(true);

            CompletableFuture.runAsync(() -> {
                try {
                    clientCode = AppConstant.ClientCode.valueOf(txtClientCode.getText().toUpperCase());
                } catch (Exception ex) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                            resourceBundle.getString("clientcodewrongerror"));
                    alert.createAlert();
                }
                setFlag();
                DB_LOC = !validateCheckFlag ? "localhost" : txtSampleMilkNo.getText();
                setupPreferenceForDbSettings(DB_LOC);

                CompletableFuture<String> future = verifyIdentityAsync();
                future.thenAccept(resp -> {
                    if (resp == null) {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                                resourceBundle.getString("error.occurred"));
                        alert.createAlert();
                        MainApp.paneDrop.setVisible(false);
                        return;
                    }
                    baseUrlRealTime = resp.split("#")[0];
                    syncUrlRealTime = resp.split("#")[1];
                    System.out.println("Successfully received baseUrl: " + resp);

                    if (!validateCheckFlag) {
                        firstDockProcess();
                    } else {
                        doubleDockProcess();
                    }

                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
            });
        });
        txtDock.textProperty().addListener((observable, oldValue, newValue) -> {
            setFlag();
            if (dockCheckFlag) {
                if (validateCheckFlag) {
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


    /**
     * Summary sentence: Load Property File
     * <p>
     * Load Data from a property file.
     */
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


    /**
     * Summary sentence: Save General Config
     * <p>
     * Save general config from property file.
     */
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

    /**
     * Summary sentence:Validate and callApis
     * <p>
     * First check the validation from setFlag and validate method then first initialize identity
     * and then call api.
     */
    private void firstDockProcess() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        this.society = txtSociety.getText();
        this.union = txtUnion.getText();
        this.dock = txtDock.getText();
        this.sampleNo = txtSampleMilkNo.getText();

        callApi();
    }


    /**
     * Summary sentence: Validate length of dock and society
     * <p>
     * Validate length of dock and society
     */
    private void setFlag() {
        try {
            societyCheckFlag = txtSociety.getText().length() >= 7;
            dockCheckFlag = txtDock.getText().length() >= 9;
            validateCheckFlag = Integer.parseInt(txtDock.getText().substring(txtSociety.getText().length())) > 1;
        } catch (Exception e) {
            System.out.println("error : " + e);
        }
    }


    /**
     * Summary sentence: Validation of input
     * <p>
     * Validate null pointer of input.
     */
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
        if (errorMsg.length() == 0) {
            setFlag();
            if (!societyCheckFlag) {
                errorMsg.append("Please Enter Valid Society No." + "\n");
            }

            if (!dockCheckFlag) {
                errorMsg.append("Please Enter Valid Dock No." + "\n");
            }
        }

        return errorMsg.length() == 0;

    }


    /**
     * Summary sentence: Set DB data in registry
     * <p>
     * Set db location in registry. new for double dock.
     */
    public static void setupPreferenceForDbSettings(String DB_LOC) {
        try {
            Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
            preferences.put("AMCS_DB_LOC", new String(Base64.getEncoder().encode(DB_LOC.getBytes())));
            preferences.put("AMCS_DB_NAME", "ZWlwbF9hbWNzX2Ri");
            preferences.put("AMCS_DB_PASS", "RUFtY3MyMDIx");
            try {
                preferences.flush();
                startSpring();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Summary sentence: Get DB data from registry
     * <p>
     * Get db data from registry to connect db
     */
    public static void recoverFromPreferences() {
        try {
            DB_LOC = null;
            Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
            DB_LOC = new String(Base64.getDecoder().decode(preferences.get("AMCS_DB_LOC", "bG9jYWxob3N0")));
            EIPL_DB_PASS = new String(Base64.getDecoder().decode(preferences.get("AMCS_DB_PASS", "RUFtY3MyMDIx")));
            EIPL_DB_NAME = new String(Base64.getDecoder().decode(preferences.get("AMCS_DB_NAME", "ZWlwbF9hbWNzX2Ri")));
            EIPL_DB_NAME = "eipl_amcs_db_ruppura_fifo";
            DB_LOC="192.168.1.15";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Summary sentence: Start spring application.
     * <p>
     * To start spring application
     */
    public static void startSpring() {
        try {
            EmcsAppContext.initializeEmcsAppContext();
        } catch (Exception e) {
            System.out.println("error : " + e);
            MainApp.paneDrop.setVisible(false);
            throw new RuntimeException(e);
        }
    }


    /**
     * Summary sentence: Validate activation input with DB.
     * <p>
     * Validate union, society and dock with db and set in MainApp.identity
     */
    private void initializeIdentity() {
        var task = new IdentityTask(txtDock.getText(),
                txtSociety.getText(),
                txtUnion.getText());
        task.setOnSucceeded(t -> {
            try {
                MainApp.identityDto = task.get();
                if (MainApp.identityDto == null) {
                    MainApp.paneDrop.setVisible(false);
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                            "Error"); // TODO Make change here later - NIMIT
                    alert.createAlert();
                    return;
                }
                if (!validateCheckFlag) {
                    downloadMembers(MainApp.identityDto.getIdentity());
                }
                openLicenseActivatePopup();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    /**
     * Summary sentence: To get baseUrlRealTime and syncUrlRealTime
     * <p>
     * Call api to get baseUrlRealTime and syncUrlRealTime
     */
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


    /**
     * Summary sentence:Call all necessary apis.
     * <p>
     * First start with the register api, on successful registration get token and
     * set in identity with all necessary identity details. After that call createMembers method
     * and after that identitySaveTask to save identity.
     */
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
                saveIdentity(identity);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void saveIdentity(Identity identity) {
        var task1 = new IdentitySaveTask(identity);
        task1.setOnSucceeded(ex -> {
            initializeIdentity();
            System.out.println("SUCCESS: All tasks are finished.");
        });
        new Thread(task1).start();
    }


    /**
     * Summary sentence:Download already available member
     * <p>
     * Download member from the main db if it isn't found, then create member manually.
     */
    private void downloadMembers(Identity identity) {
        MainApp.paneDrop.setVisible(true);
        var memberDownloadTask = new MemberDownloadTask(identity);
        MainApp.lblMessage.textProperty().bind(memberDownloadTask.messageProperty());

        memberDownloadTask.setOnSucceeded(e -> {
            try {
                boolean needToCreateMember = (boolean) memberDownloadTask.get();
                if (needToCreateMember) {
                    createMembers();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        memberDownloadTask.setOnFailed(e -> {
            System.err.println("Member download failed. Trying to create members if needed.");
            createMembers();
        });

        new Thread(memberDownloadTask).start();
    }


    /**
     * Summary sentence:Manual member creation
     * <p>
     * Create number of members on the basis of the user input if member download failed or
     * on not getting data.
     */
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
            task.setOnSucceeded(e2 -> MainApp.paneDrop.setVisible(false));
            new Thread(task).start();
            MainApp.lblMessage.textProperty().bind(task.messageProperty());

        } catch (Exception e) {
            MainApp.paneDrop.setVisible(false);
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    "Please Enter Valid Range.");
            alert.createAlert();
        }
    }


    /**
     * Summary sentence:LicenseKey Popup
     * <p>
     * Open LicenseKey popup on activation.
     */
    private void openLicenseActivatePopup() {
        try {
            MainApp.paneDrop.setVisible(false);
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LicenseActivatePopUp", null, this, resourceBundle.getString("activation"));
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), "Error", "Could not open License Activation window.");
            alert.createAlert();
        }
    }


    /**
     * Summary sentence: Activation page for DB Failed.
     * <p>
     * Set data on activation page when DB connection failed, mainly for second dock.
     */
    public void checkActivation() {
        if (!MainApp.properties.isEmpty()) {
            txtSociety.setText(MainApp.getProperty("identity.society", ""));
            txtUnion.setText(MainApp.getProperty("identity.union", ""));
            txtDock.setText(MainApp.getProperty("identity.dock", ""));
            txtSampleMilkNo.setText(MainApp.getProperty("samplemilk", ""));
            txtClientCode.setText(MainApp.getProperty("client.code", ""));
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    resourceBundle.getString("databaseerror"));
            alert.createAlert();
            setFlag();
            if (validateCheckFlag) {
                lblSampleNo.setText("Server Details");
                txtCowRange.setDisable(true);
                txtBuffRange.setDisable(true);
            } else {
                lblSampleNo.setText("Sample No.");
                txtCowRange.setDisable(false);
                txtBuffRange.setDisable(false);
            }
        }
    }


    /**
     * Summary sentence: Processes for double dock activation.
     * <p>
     * Validate data and start spring with activation key popup, save second dock in db and also get baseUrlRealtime on successful.
     */
    private void doubleDockProcess() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        this.union = txtUnion.getText();
        this.society = txtSociety.getText();
        this.dock = txtDock.getText();
        this.sampleNo = txtSampleMilkNo.getText();
        txtServerDetail.setText(txtServerDetail.getText().replace("localhost", txtSampleMilkNo.getText()));

        doubleDockSave();
        openLicenseActivatePopup();
    }


    /**
     * Summary sentence: Second Dock save data.
     * <p>
     * Set double dock data and save it in db and call initializeIdentity at last.
     */
    private void doubleDockSave() {
        SocietyRepository societyRepository = EmcsAppContext.getContext().getBean(SocietyRepository.class);
        MilkTypeRepository milkTypeRepository = EmcsAppContext.getContext().getBean(MilkTypeRepository.class);
        Dock dock1 = new Dock();
        dock1.setDockNo(txtDock.getText());
        dock1.setUnionCode(txtUnion.getText());
        dock1.setSociety(societyRepository.findById(txtSociety.getText()).get());
        dock1.setIsDefault((short) 1);
        dock1.setActive(true);
        List<MilkType> milkTypeList = milkTypeRepository.findAll();
        DockMilkTypeDto dto = new DockMilkTypeDto(dock1, milkTypeList);
        DockService service = EmcsAppContext.getContext().getBean(DockService.class);
        if (dto != null)
            service.save(dto, null);
        initializeIdentity();
    }


    /**
     * Summary sentence:Property file creation
     * <p>
     * Create a property file and add data in that file using writeAppProperty method.
     */
    private void makeFile() {
        try {
            File appProperties = new File("resources/app.properties");
            if (appProperties.createNewFile()) {
                Files.write(appProperties.toPath(), writeAppProperty(), StandardCharsets.UTF_8);
                System.out.println("Created app.properties");
                loadProperties();
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Summary sentence: Write app property file
     * <p>
     * Write all data in the app property file
     */
    private List<String> writeAppProperty() {
        List<String> lines = new ArrayList<>();
        lines.add("baseurl=" + new String(Base64.getEncoder().encode(txtServerDetail.getText().getBytes(StandardCharsets.UTF_8))));
        lines.add("baseurl.realtime=" + new String(Base64.getEncoder().encode(baseUrlRealTime.getBytes(StandardCharsets.UTF_8))));
        lines.add("client.code=" + new String(Base64.getEncoder().encode((clientCode.toString().toUpperCase()).getBytes())));
        lines.add("syncUrl.realtime=" + new String(Base64.getEncoder().encode(syncUrlRealTime.getBytes(StandardCharsets.UTF_8))));
        lines.add("app.request.debug=" + new String(Base64.getEncoder().encode("0".getBytes())));
        lines.add("#Languages");
        lines.add("app.languages=" + new String(Base64.getEncoder().encode("English,Gujarati".getBytes(StandardCharsets.UTF_8))));
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
        lines.add("default.snf=" + new String(Base64.getEncoder().encode("0".getBytes())));
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
        lines.add("slip.font=" + new String(Base64.getEncoder().encode("Nirmala UI".getBytes())));
        lines.add("application.language=" + new String(Base64.getEncoder().encode("Hindi".getBytes())));

        File file = new File("D:");
        if (file.exists())
            lines.add("backuppath=" + new String(Base64.getEncoder().encode(("D:\\backup").getBytes())));
        else
            lines.add("backuppath=" + new String(Base64.getEncoder().encode(("E:\\backup").getBytes())));
        lines.add("masetting=" + new String(Base64.getEncoder().encode(("Single MA").getBytes())));
        lines.add("product.purchaserate=" + new String(Base64.getEncoder().encode(("0").getBytes())));
        lines.add("product.salerate=" + new String(Base64.getEncoder().encode(("0").getBytes())));
        lines.add("code.milktype.parsing=" + new String(Base64.getEncoder().encode(("0").getBytes())));
        lines.add("fifo.process=" + new String(Base64.getEncoder().encode((isFifoProcess()).getBytes())));

        return lines;
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            makeFile();
        else {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("activation"),
                    resourceBundle.getString("verificationfailed"));
            alert.createAlert();
        }
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

    public static String isFifoProcess() {
        try {
            ProductStockRepository productStockRepository = EmcsAppContext.getContext().getBean(ProductStockRepository.class);
            List<ProductStock> productStocks = productStockRepository.findAll();
            if (productStocks.isEmpty()) {
                return "fifo";
            }
            if (productStocks.get(0).getBatchNo() == null) {
                return "non-fifo";
            }
            if (productStocks.get(0).getBatchNo() != null) {
                return "fifo";
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "fifo";
    }
}