package com.eipl.amcs.auth;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.task.LoginTask;
import com.eipl.amcs.auth.task.VerifyIdentityTask;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.dto.JarUpdate;
import com.eipl.amcs.base.task.DownloadFileTask;
import com.eipl.amcs.base.task.UpdaterCheckTask;
import com.eipl.amcs.base.task.UpdaterLogTask;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_PasswordField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.task.FinancialYearLoadTask;
import com.eipl.amcs.setting.model.GeneralConfig;
import com.eipl.amcs.setting.task.GeneralConfigSaveTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.eipl.amcs.MainApp.*;
import static com.eipl.amcs.utils.AppConstant.baseUrlRealTime;
import static com.eipl.amcs.utils.AppConstant.syncUrlRealTime;

@Slf4j
public class LoginController implements MyInitialization {

    @FXML
    StackPane root;
    @FXML
    Button btnLogin;
    @FXML
    private AutoSearchTextField<FinancialYear> cboxFinancialYear;
    @FXML
    private AutoSearchTextField<String> cboxLang;
    @FXML
    private E_TextField txtUsername;
    @FXML
    private E_PasswordField txtPassword;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        callApi();
        setupComboBox();
        cboxLang.setValue(MainApp.getProperty("application.language", "English"));
        btnLogin.setOnAction(e -> {
            CompletableFuture.runAsync(this::deleteOtherFiles);
            var task = new LoginTask(txtUsername.getText(), txtPassword.getText());
            task.setOnSucceeded(e1 -> {
                try {
                    Object obj = task.get();
                    if (obj == null) {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), "Application",
                                "An error occurred!");
                        alert.createAlert();
                        FocusUtils.requestFocus(txtUsername);
                        return;
                    }

                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), "Application",
                                resourceBundle.getString(error.getMessage()));
                        alert.createAlert();
                        FocusUtils.requestFocus(txtUsername);
                        return;
                    }

                    if (obj instanceof User) {
                        createAndSetLocale();
                        MainApp.setUser((User) obj);
                        MainApp.setFinancialYear(cboxFinancialYear.getValue());
                        Rectangle2D rect = Screen.getPrimary().getVisualBounds();
//                        MainApp.getContentPane().setMaxWidth(rect.getWidth());
//                        MainApp.getContentPane().setMaxHeight(rect.getHeight());
                        MainApp.getContentPane().setTop(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/HeaderBar.fxml")));
                        MainApp.getContentPane().setBottom(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/FooterBar.fxml")));
                        MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
                        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
                    }
                    verifyIdentityAsync();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        });
        cboxLang.setOnAction(e -> {
            FocusUtils.requestFocus(btnLogin);
        });
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 24/06/2026    Nimit             1.0.1      Added line to set Locale variable also.
     */
    private void createAndSetLocale() {
        try {
            setCurrentLocale(new Locale(cboxLang.getValue().substring(0, 2).toLowerCase()));
            updateLocaleFile(cboxLang.getValue().substring(0, 2).toLowerCase());
            Locale.setDefault(getCurrentLocale());
            if (!"en".equalsIgnoreCase(cboxLang.getValue().substring(0, 2))) {
                List<String> lines = Files.readAllLines(new File("gu".equalsIgnoreCase(cboxLang.getValue().substring(0, 2)) ? "resources/messages/guj" : "resources/messages/hi").toPath());
                List<String> nwLines = new ArrayList<>();
                lines.forEach(item -> {
                    String[] arr = item.split("=");
                    nwLines.add(arr[0] + "=" + getUniCode(arr[1]));
                });
                Files.write(new File(String.format("resources/messages/message_%s.properties", cboxLang.getValue().substring(0, 2).toLowerCase())).toPath(), nwLines, StandardCharsets.UTF_8);
                MainApp.locale = cboxLang.getValue().substring(0, 2).toLowerCase();
            }
            File file = new File("resources/messages/");
            URL[] urls = {file.toURI().toURL()};
            ClassLoader classLoader = new URLClassLoader(urls);

            try {
                MainApp.setBundle(ResourceBundle.getBundle("message", Locale.getDefault(), classLoader));
            } catch (Exception e) {
                e.printStackTrace();
                setCurrentLocale(new Locale("en"));
                Locale.setDefault(getCurrentLocale());
                MainApp.setBundle(ResourceBundle.getBundle("message", Locale.getDefault(), classLoader));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getUniCode(String messageVal) {
        String str = "";
        for (char c : messageVal.toCharArray())
            str = str + unicodeEscaped(c);
        return str;
    }

    public String unicodeEscaped(char ch) {
        if (ch == '\\')
            return "\\";
        if (ch == 'n')
            return "n";
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        } else if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }

    @Override
    public void loadData() {
        var task = new FinancialYearLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<FinancialYear> list = task.get();
                if (list != null) {
                    cboxFinancialYear.setItems(FXCollections.observableList(list));
                    LocalDate date = LocalDate.now();
                    for (FinancialYear financialYear : list) {
                        if (date.isAfter(financialYear.getStartDate()) && date.isBefore(financialYear.getEndDate())) {
                            cboxFinancialYear.getSelectionModel().select(financialYear);
                            break;
                        }
                        if (date.isEqual(financialYear.getStartDate()) || date.isEqual(financialYear.getEndDate())) {
                            cboxFinancialYear.getSelectionModel().select(financialYear);
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati").split(",");
        cboxLang.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLang.getSelectionModel().select(0);
    }

    private void deleteOtherFiles() {
        try {
            File file = new File("resources/messages/");
            URL[] urls = {file.toURI().toURL()};
            Path resourcePath = Paths.get(urls[0].toURI());

            try (Stream<Path> stream = Files.walk(resourcePath)) {
                stream.filter(Files::isRegularFile)
                        .forEach(path -> {
                            String fileName = path.getFileName().toString();
                            int dotIndex = fileName.lastIndexOf('.');
                            if (!(fileName.equalsIgnoreCase("guj") || fileName.equalsIgnoreCase("hi") || fileName.equalsIgnoreCase("mar") || fileName.startsWith("message_en"))) {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyIdentityAsync() {
        VerifyIdentityTask task = new VerifyIdentityTask(MainApp.getProperty("client.code", (String) null));
        task.setOnSucceeded((ee) -> {
            try {
                String baseUrls = (String) task.get();
                log.info(baseUrls);
                if (baseUrls != null) {
                    AppConstant.baseUrlRealTime = baseUrls.split("#")[0];
                    AppConstant.syncUrlRealTime = baseUrls.split("#")[1];
                    log.info("Successfully received vendor url: " + baseUrls);
                } else {
                    AppConstant.baseUrlRealTime = MainApp.getProperty("baseurl.realtime", null);
                    AppConstant.syncUrlRealTime = MainApp.getProperty("syncUrl.realtime", null);
                }

                this.writeAppProperty();
            } catch (ExecutionException | InterruptedException var4) {
            }

        });
        (new Thread(task)).start();
    }

    private void writeAppProperty() {
        try {
            Path path = Paths.get("resources/app.properties");
            String targetBaseUrlKey = "baseurl.realtime=";
            String newBaseUrlValue = targetBaseUrlKey + new String(Base64.getEncoder().encode(baseUrlRealTime.getBytes()));
            String targetSyncUrlKey = "syncUrl.realtime=";
            String newSyncUrlValue = targetSyncUrlKey + new String(Base64.getEncoder().encode(syncUrlRealTime.getBytes()));

            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = lines.stream()
                    .map(line -> {
                        if (line.trim().startsWith(targetBaseUrlKey))
                            return newBaseUrlValue;
                        else if (line.trim().startsWith(targetSyncUrlKey))
                            return newSyncUrlValue;
                        else
                            return line;
                    })
                    .collect(Collectors.toList());
            Files.write(path, updatedLines);
            saveData();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void callApi() {
        var task = new UpdaterCheckTask();
        task.setOnSucceeded(e -> {
            try {
                JarUpdate jarUpdate = task.get();
                if (jarUpdate != null) {
//                    Map<String, Object> map = (Map<String, Object>) res.get("data");
                    if (jarUpdate.getResourcePath() != null) {
                        var task1 = new DownloadFileTask(jarUpdate.getResourcePath());
                        task1.setOnSucceeded(e1 -> {

                            var task2 = new UpdaterLogTask(jarUpdate);
                            task2.setOnSucceeded(e2 -> {
                                MyAlert alert = new InformationAlert(MainApp.getStage(), "Application Update Successful",
                                        "Please Restart Your Computer");
                                alert.createAlert();
                            });
                            new Thread(task2).start();


                            try {
                                File dir = new File("resources/appupdate");
                                if (dir.listFiles() != null) {
                                    for (File file : dir.listFiles()) {
                                        for (File listFile : file.listFiles()) {
                                            listFile.delete();
                                        }
                                        file.delete();
                                    }
                                    dir.delete();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                        new Thread(task1).start();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
