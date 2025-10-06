package com.eipl.amcs;

import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.base.FxmlLoaderUtil;
import com.eipl.amcs.base.LaunchScreenController;
import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.model.SentBoxCountTask;
import com.eipl.amcs.config.AppConfig;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.model.DpuIncentiveRequest;
import com.eipl.amcs.operation.procurement.serial.AnalyserSerial;
import com.eipl.amcs.operation.procurement.serial.DisplaySerial;
import com.eipl.amcs.operation.procurement.serial.SplitterSerial;
import com.eipl.amcs.operation.procurement.serial.WsSerial;
import com.eipl.amcs.utils.TableColItem;
import com.eipl.amcs.utils.task.DbBackupTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class MainApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
    public static Stage stage;
    public static BorderPane contentPane;
    public static StackPane paneDrop;
    public static Label lblMessage;
    public static String locale;
    public static FxmlLoaderUtil fxmlLoaderUtil;
    public static ResourceBundle bundle;
    public static Long syncCount;
    private static URL urlLoadPath;
    private static URL urlLoadAndSetPath;
    public static final Properties properties = new Properties();
    public static Map<String, List<TableColItem>> tableConfiguration = new HashMap<>();
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static Map<String, String> mapProp = new HashMap<>();
    public static IdentityDto identityDto;
    private static FinancialYear financialYear;
    public static User user;
    public static String systemId = "";

    public static WsSerial wsSerial;
    public static AnalyserSerial analyserSerial;
    public static AnalyserSerial analyserSerial2;
    public static AnalyserSerial analyserSerial3;
    public static AnalyserSerial analyserSerial4;
    public static DisplaySerial displaySerial;
    public static SplitterSerial splitterSerial;

    public static List<Notification> notificationList = new ArrayList<>();
    public static List<DpuIncentiveRequest> timingList = new ArrayList<>();
    public static List<AllowDcsManualCollectionRange> manualCollectionRangeList = new ArrayList<>();
    public static boolean isIncentive = false;
    public static double incentiveValue = 1.0;


    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        try {
            context = SpringApplication.run(AppConfig.class, args);
            launch(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        MainApp.stage = stage;
    }

    public static String getLocale() {
        return locale;
    }

    public static void setLocale(String locale) {
        MainApp.locale = locale;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static void setBundle(ResourceBundle bundle) {
        MainApp.bundle = bundle;
    }

    public static URL getUrlLoadPath() {
        return urlLoadPath;
    }

    public static void setUrlLoadPath(URL urlLoadPath) {
        MainApp.urlLoadPath = urlLoadPath;
    }

    public static URL getUrlLoadAndSetPath() {
        return urlLoadAndSetPath;
    }

    public static void setUrlLoadAndSetPath(URL urlLoadAndSetPath) {
        MainApp.urlLoadAndSetPath = urlLoadAndSetPath;
    }

    public static BorderPane getContentPane() {
        return contentPane;
    }

    public static void setContentPane(BorderPane contentPane) {
        MainApp.contentPane = contentPane;
    }

    public static FxmlLoaderUtil getFxmlLoaderUtil() {
        return fxmlLoaderUtil;
    }

    public static FinancialYear getFinancialYear() {
        return financialYear;
    }

    public static void setFinancialYear(FinancialYear financialYear) {
        MainApp.financialYear = financialYear;
    }

    public static void setUser(User user) {
        MainApp.user = user;
    }

    public static User getUser() {
        return user;
    }

    public static final DecimalFormat DECIMAL_FORMAT_1_DIGIT = new DecimalFormat("0.0");
    public static final DecimalFormat DECIMAL_FORMAT_2_DIGIT = new DecimalFormat("0.00");
    public static final DecimalFormat DECIMAL_FORMAT_3_DIGIT = new DecimalFormat("#.###");
    private static final long FETCH_INTERVAL_MINUTES = 15;

    public static final Socket socket = new Socket();

    @Override
    public void start(Stage stage) throws Exception {

        MainApp.stage = stage;
        LOGGER.info("MainApp init start");
        // Application screen load
        Parent root = FXMLLoader.load(getClass().getResource("view/EmcsApp.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("view/styles.css").toExternalForm());
        stage.setTitle("Everest Milk Collection System");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.getIcons().add(new Image(getClass().getResource("view/images/logo-small.png").toExternalForm()));
        stage.show();

        //----------------------------------------------------------------------------------------------------------
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                EmcsAppContext.initializeEmcsAppContext();
                context = EmcsAppContext.getContext();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            return "Task Completed!";
        });

        future.thenAccept(result -> {
            Platform.runLater(() -> {
                System.out.println("Result: " + result);
                MainApp.contentPane.setCenter(MainApp.fxmlLoaderUtil.load(MainApp.class.getResource("view/Splash.fxml")));
            });
        });


        createAndSetLocale();
        loadProperties();


        stage.setOnCloseRequest(event -> {
            event.consume();
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), bundle.getString("doyouwanttoclose"),
                    bundle.getString("alert.cancel"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                closeApplication();
            }
        });

        fxmlLoaderUtil = new FxmlLoaderUtil();
        // Splash screen load
        MainApp.contentPane.setCenter(MainApp.fxmlLoaderUtil.load(MainApp.class.getResource("view/LaunchScreen.fxml")));

        contentPane.centerProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                closeDeviceIfAny();
            }
        });

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Create a Timer instance
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (MainApp.identityDto != null)
                    checkSentBoxCount();
            }
        }, 0, 2 * 60 * 1000); // 5 minute in milliseconds

    }

    private void checkSentBoxCount() {
        SentBoxCountTask task = new SentBoxCountTask(MainApp.identityDto.getSociety().getCode(), "");
        task.setOnSucceeded(e -> {
            try {
                Map<String, Object> map = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void closeDeviceIfAny() {
        if (MainApp.wsSerial != null) {
            MainApp.wsSerial.disconnect();
            MainApp.wsSerial = null;
        }
        if (MainApp.analyserSerial != null) {
            MainApp.analyserSerial.disconnect();
            MainApp.analyserSerial = null;
        }
        if (MainApp.analyserSerial2 != null) {
            MainApp.analyserSerial2.disconnect();
            MainApp.analyserSerial2 = null;
        }
        if (MainApp.analyserSerial3 != null) {
            MainApp.analyserSerial3.disconnect();
            MainApp.analyserSerial3 = null;
        }
        if (MainApp.analyserSerial4 != null) {
            MainApp.analyserSerial4.disconnect();
            MainApp.analyserSerial4 = null;
        }
        if (MainApp.displaySerial != null) {
            MainApp.displaySerial.disconnect();
            MainApp.displaySerial = null;
        }
        if (MainApp.splitterSerial != null) {
            MainApp.splitterSerial.disconnect();
            MainApp.splitterSerial = null;
        }
    }

    public static void loadProperties() {
        Properties prop = new Properties();
        try (InputStream inputStream = new FileInputStream("resources/app.properties")) {
            prop.load(inputStream);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (prop != null && !prop.isEmpty()) {
            prop.forEach((k, v) -> properties.put(k, new String(Base64.getDecoder().decode(v.toString().getBytes()))));
//            prop.forEach((k, v) -> mapProp.put((String) k, new String(Base64.getDecoder().decode(v.toString().getBytes()))));
        }
    }

    private void createAndSetLocale() {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(new File("resources/locale.txt")))) {
                locale = reader.readLine();
                if (locale != null && locale.length() == 2)
                    Locale.setDefault(new Locale(locale));
                else {
                    locale = "en";
                    Locale.setDefault(new Locale("en"));
                }
            } catch (Exception e) {
                locale = "en";
                Locale.setDefault(new Locale("en"));
            }
            File file = new File("resources/messages/");
            URL[] urls = {file.toURI().toURL()};
            ClassLoader classLoader = new URLClassLoader(urls);

            try {
                bundle = ResourceBundle.getBundle("message", Locale.getDefault(), classLoader);
            } catch (Exception e) {
                e.printStackTrace();
                Locale.setDefault(new Locale("en"));
                bundle = ResourceBundle.getBundle("message", Locale.getDefault(), classLoader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeApplication() {

        if (MainApp.wsSerial != null) {
            MainApp.wsSerial.disconnect();
            MainApp.wsSerial = null;
        }
        if (MainApp.analyserSerial != null) {
            MainApp.analyserSerial.disconnect();
            MainApp.analyserSerial = null;
        }
        if (MainApp.displaySerial != null) {
            MainApp.displaySerial.disconnect();
            MainApp.displaySerial = null;
        }
        if (MainApp.splitterSerial != null) {
            MainApp.splitterSerial.disconnect();
            MainApp.splitterSerial = null;
        }

//        if (syncCount != null && syncCount != 0) {
//            MyAlert alert = new InformationAlert(MainApp.getStage(), bundle.getString("pendingsync"),
//                    bundle.getString("syncdatapending"));
//            alert.createAlert();
        String backupPath = MainApp.getProperty("backuppath", null);
        if (backupPath != null) {
            backupPath = backupPath.replace(" ", "");
        }
        if (backupPath == null || backupPath.isEmpty())
            Platform.exit();
        paneDrop.setVisible(true);
        lblMessage.setText("Backup is running...");
        var task = new DbBackupTask(backupPath);
        task.setOnSucceeded(e -> {
            Platform.exit();
            System.exit(0);
        });
        task.setOnFailed(e -> {
            Platform.exit();
            System.exit(0);
        });
        new Thread(task).start();
    }
//    }
}


