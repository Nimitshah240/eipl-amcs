package com.eipl.amcs.auth;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.LoginDto;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.service.UserService;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_PasswordField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.converter.FinancialYearConvertor;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.service.FinancialYearService;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.eipl.amcs.MainApp.context;

public class LoginController implements MyInitialization {

    @FXML
    StackPane root;
    @FXML
    Button btnLogin;
    @FXML
    private ComboBox<FinancialYear> cboxFinancialYear;
    @FXML
    private ComboBox<String> cboxLang;
    @FXML
    private E_TextField txtUsername;
    @FXML
    private E_PasswordField txtPassword;

    private UserService userService;
    private FinancialYearService financialYearService;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = context.getBean(UserService.class);
        financialYearService = context.getBean(FinancialYearService.class);

        loadData();
        setupComboBox();
        cboxLang.getSelectionModel().select(0);

        btnLogin.setOnAction(e -> {
// ------------- MERGING ----------------------
            CompletableFuture future = CompletableFuture.runAsync(() -> createAndSetLocale());

            LoginDto dto = new LoginDto(txtUsername.getText(), txtPassword.getText(), MainApp.identityDto.getSociety());
            User user = userService.authenticate(dto);

            if (user == null) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), "Application",
                        "An error occurred!");
                alert.createAlert();
                FocusUtils.requestFocus(txtUsername);
            } else {
                MainApp.setUser(user);
                MainApp.setFinancialYear(cboxFinancialYear.getValue());
                Rectangle2D rect = Screen.getPrimary().getVisualBounds();
                MainApp.getContentPane().setMaxWidth(rect.getWidth());
                MainApp.getContentPane().setMaxHeight(rect.getHeight());
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
            }

// ------------- MERGING ----------------------

        });
        cboxLang.setOnAction(e -> {
            FocusUtils.requestFocus(btnLogin);
        });
    }

    @Override
    public void setupComboBox() {
        cboxFinancialYear.setConverter(new FinancialYearConvertor(cboxFinancialYear));
    }

    private String locale;

    private void createAndSetLocale() {
        try {
            Locale.setDefault(new Locale(cboxLang.getValue().substring(0, 2).toLowerCase()));
            if ("gu".equalsIgnoreCase(cboxLang.getValue().substring(0, 2).toLowerCase())) {
                List<String> lines = Files.readAllLines(new File("resources/messages/guj").toPath());
                List<String> nwLines = new ArrayList<>();
                lines.forEach(item -> {
                    String[] arr = item.split("=");
                    nwLines.add(arr[0] + "=" + getUniCode(arr[1]));
                });
                Files.write(new File("resources/messages/message_gu.properties").toPath(), nwLines, Charset.forName("UTF-8"));
                MainApp.locale = "gu";
            }
            File file = new File("resources/messages/");
            URL[] urls = {file.toURI().toURL()};
            ClassLoader classLoader = new URLClassLoader(urls);

            try {
                MainApp.setBundle(ResourceBundle.getBundle("message", Locale.getDefault(), classLoader));
            } catch (Exception e) {
                e.printStackTrace();
                Locale.setDefault(new Locale("en"));
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

//        --------------- MERGING ----------------------------
        List<FinancialYear> list = financialYearService.findAll();
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

//        --------------- MERGING ----------------------------


        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati").split(",");
        cboxLang.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLang.getSelectionModel().select(0);
    }
}
