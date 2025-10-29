package com.eipl.amcs.base;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.task.IdentityTask;
import com.eipl.amcs.base.task.RateTask;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SplashController implements MyInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(SplashController.class);
    @FXML
    Label lbl;
    @FXML
    StackPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAndSetLocale();

        var task = new AppInitTask();
        task.setOnSucceeded(e -> {
            try {
                File appProperty = new File("resources/app.properties");
                var resp = task.get();
                if (resp && appProperty.exists()) //
                    checkHealth();
                else {
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Activation.fxml")));
                    lbl.setText("An error occurred!");
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            } catch (Exception exception) {
            }
        });
        new Thread(task).start();
    }

    private void checkHealth() {
        lbl.setText("Please wait...");
        initializeIdentity();
    }

    private void initializeIdentity() {
        var task = new IdentityTask(MainApp.getProperty(AppConstant.Props.IDENTITY_DOCK, null),
                MainApp.getProperty(AppConstant.Props.IDENTITY_SOCIETY, null),
                MainApp.getProperty(AppConstant.Props.IDENTITY_UNION, null));
        task.setOnSucceeded(t -> {
            try {
                MainApp.identityDto = task.get();
                if (MainApp.identityDto == null)
                    lbl.setText("Society initialization error!");
                else {
                    RateTask rateTask = new RateTask();
                    rateTask.setOnSucceeded(e -> {
                    });
                    rateTask.setOnFailed(e -> {
                    });
                    new Thread(rateTask).start();
                    lbl.textProperty().bind(rateTask.messageProperty());
                    MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "ABC");
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void createAndSetLocale() {
    }

    class HealthCheckTask extends Task<String> {
        private String response = null;

        @Override
        protected String call() throws Exception {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + "/home";
            do {
                try {
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                            null, String.class);
                    if (response == null || response.getStatusCode() != HttpStatus.OK)
                        return null;
                    this.response = response.getBody();
                } catch (Exception e) {
                    LOGGER.info("Health check {}", e.getMessage());
                    response = e.getMessage();
                    Thread.sleep(5000);
                }
            } while (!"ok".equalsIgnoreCase(response));
            return response;
        }
    }

    class AppInitTask extends Task<Boolean> {

        @Override
        protected Boolean call() throws Exception {
            try {
                return true;
//                EmcsAppContext.initializeEmcsAppContext();
//                return EmcsAppContext.getContext() != null;
            } catch (Exception e) {
                LOGGER.error("AppInitTask: ", e);
            }
            return null;
        }
    }
}
