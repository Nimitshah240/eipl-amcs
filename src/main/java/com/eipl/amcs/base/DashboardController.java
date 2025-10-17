package com.eipl.amcs.base;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.NotificationAcknowledgementTask;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.procurement.dto.CollectionSummary;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.DpuIncentiveRequestLoadTask;
import com.eipl.amcs.operation.procurement.task.MilkCollectionLoadTask;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.task.BroadcastedTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DashboardController implements MyInitialization, PopupCallback {

    protected final int SCALE = 2;
    protected final RoundingMode ROUND = RoundingMode.HALF_UP;
    @FXML
    public ListView<Notification> lv;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    ObservableList<MilkCollection> listMilkCollection = FXCollections.observableArrayList();
    ObservableList<CollectionSummary> listCollectionSummary = FXCollections.observableArrayList();
    List<MilkType> listMilkType = new ArrayList<>();
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblCode, lblName, lblDock, lblFinancialYear, lblLanguage, lblUserName, lblSyncCount;
    @FXML
    private TableView<CollectionSummary> tableCollection;
    @FXML
    private TableColumn<CollectionSummary, MilkType> colSummaryMilkType;
    @FXML
    private TableColumn<CollectionSummary, Number> colSummaryMemberNos;
    @FXML
    private TableColumn<CollectionSummary, BigDecimal> colSummaryQty, colSummaryAmount, colAvgFat, colAvgSnf;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<Shift> cboxShift;
    @FXML
    private ComboBox<String> cboxLang;
    @FXML
    private ComboBox<String> cboxNotification;
    @FXML
    private Button btnLoad, btnMilkCollection, btnLocalMilkSale, btnMilkDispatch, btnProductSale, btnBilling, btnKapaat, btnMilkReceipt, btnSync, btnPendingSync;
    private PopupCallback callback;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setResizable(false);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void loadNotificationList() {
        if (cboxNotification.getSelectionModel().getSelectedIndex() == 0)
            lv.setItems(FXCollections.observableList(MainApp.notificationList));
        else if (cboxNotification.getSelectionModel().getSelectedIndex() == 1)
            lv.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 1).collect(Collectors.toList())));
        else if (cboxNotification.getSelectionModel().getSelectedIndex() == 2)
            lv.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 2).collect(Collectors.toList())));
        else if (cboxNotification.getSelectionModel().getSelectedIndex() == 3)
            lv.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 3).collect(Collectors.toList())));
        else if (cboxNotification.getSelectionModel().getSelectedIndex() == 4)
            lv.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 4).collect(Collectors.toList())));
        else if (cboxNotification.getSelectionModel().getSelectedIndex() == 5)
            lv.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 5).collect(Collectors.toList())));
        else if (cboxNotification.getSelectionModel().getSelectedIndex() == 6)
            lv.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 6).collect(Collectors.toList())));
        else
            lv.setItems(FXCollections.observableList(MainApp.notificationList));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadControls();
        setupComboBox();
        loadShift();
        loadMilkType();
        setupTable();
        cboxNotification.getItems().addAll("All", "Alert", "Paripatra", "Special Message", "Milk Bill", "Bacteria Test", "EVEREST Bill");
        cboxNotification.getSelectionModel().select(0);
        cboxNotification.setOnAction(e -> {
            loadNotificationList();
        });
        loadData();
        FocusUtils.requestFocus(btnMilkCollection);
        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati").split(",");
        cboxLang.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLang.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLang.setValue("Hindi");
        } else {
            cboxLang.setValue("English");
        }
        cboxLang.setOnAction(e -> {
            createAndSetLocale();
            Rectangle2D rect = Screen.getPrimary().getVisualBounds();
            MainApp.getContentPane().setMaxWidth(rect.getWidth());
            MainApp.getContentPane().setMaxHeight(rect.getHeight());
            MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnLoad.setOnAction(e -> loadData());
        dpDate.setValue(LocalDate.now());
        btnMilkCollection.setOnAction(e -> openMilkCollection());
        btnMilkCollection.setText(btnMilkCollection.getText() + " (F1)");
        btnLocalMilkSale.setText(btnLocalMilkSale.getText() + " (F2)");
        btnMilkDispatch.setText(btnMilkDispatch.getText() + " (F3)");
        btnProductSale.setText(btnProductSale.getText() + " (F5)");
        btnBilling.setText(btnBilling.getText() + " (F7)");
        btnKapaat.setText(btnKapaat.getText() + " (F6)");
        btnMilkReceipt.setText(btnMilkReceipt.getText() + " (F4)");
        btnLocalMilkSale.setOnAction(e -> openLocalMilkSale());
        btnMilkDispatch.setOnAction(e -> openMilkDispatch());
        btnProductSale.setOnAction(e -> openProductSale());
        btnKapaat.setOnAction(e -> openKapaat());
        btnMilkReceipt.setOnAction(e -> openReceipt());
        btnBilling.setOnAction(e -> openBilling());
        loadMsg();
        loadNotification();

        btnSync.setOnAction(e -> {
            MainApp.paneDrop.setVisible(true);
            MainApp.lblMessage.setText("Data Syncing...");
            BroadcastedTask task = new BroadcastedTask();
            task.setOnSucceeded(e1 -> {
                System.out.println("Done");
                MainApp.paneDrop.setVisible(false);
                lblSyncCount.setText("0");
            });
            new Thread(task).start();
        });
        btnPendingSync.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SyncDataList", null, this);
        });
        root.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case F1:
                    openMilkCollection();
                    break;
                case F2:
                    openLocalMilkSale();
                    break;
                case F3:
                    openMilkDispatch();
                    break;
                case F4:
                    openReceipt();
                    break;
                case F5:
                    openProductSale();
                    break;
                case F6:
                    openKapaat();
                    break;
                case F7:
                    openBilling();
                    break;
                case L:
                    if (cboxLang.getSelectionModel().getSelectedIndex() == 0)
                        cboxLang.getSelectionModel().select(1);
                    else if (cboxLang.getSelectionModel().getSelectedIndex() == 1)
                        cboxLang.getSelectionModel().select(2);
                    else
                        cboxLang.getSelectionModel().select(0);
                    break;
            }
        });
        fetchPendingSync();
        loadTimingList();
    }

    private void loadTimingList() {
        var task1 = new DpuIncentiveRequestLoadTask(dpDate.getValue(), dpDate.getValue());
        task1.setOnSucceeded(e1 -> {
            try {
                if (task1.get() != null) {
                    MainApp.timingList = new ArrayList<>();
                    MainApp.timingList.add(task1.get());
                }
            } catch (InterruptedException | ExecutionException ee) {
                throw new RuntimeException(ee);
            }
        });
        new Thread(task1).start();
    }


    private void createAndSetLocale() {
        try {
            Locale.setDefault(new Locale(cboxLang.getValue().substring(0, 2).toLowerCase()));
            MainApp.locale = Locale.getDefault().toString();
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


    private void fetchPendingSync() {
        var task = new PendingSyncTask();
        task.setOnSucceeded(e -> {
            try {
                Long count = task.get();
                MainApp.syncCount = count;
                if (count == null) {
                    lblSyncCount.setText("NA");
                    return;
                }
                lblSyncCount.setText(count.toString());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void openSocietyInfo() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/org/Society.fxml")));
    }

    private void openNotification() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/administration/Notification.fxml")));
    }

    private void openBilling() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/billing/MemberBillSummary.fxml")));
    }

    private void openProductSale() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/ProductSale.fxml")));
    }

    private void openMilkDispatch() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));
    }

    private void openLocalMilkSale() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/LocalMilkSale.fxml")));
    }

    private void openMilkCollection() {
        MainApp.getContentPane().setLeft(null);
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkCollectionAdd.fxml")));
    }

    private void openKapaat() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/inventory/Kapaat.fxml")));
    }

    private void openReceipt() {
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceiptAddEdit.fxml")));
    }


    @Override
    public void loadControls() {
        lblCode.setText(MainApp.identityDto.getSociety().getCodeEx());
        lblName.setText(MainApp.identityDto.getSociety().getName());
        lblDock.setText(MainApp.identityDto.getDock().getDockNo());
        lblUserName.setText(MainApp.user.getUsername());
        try {
            lblFinancialYear.setText(MainApp.getFinancialYear().toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void loadMsg() {
        lv.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(null);
                setEditable(false);
                setGraphic(createNode(item));
            }
        });
    }

    public void loadNotification() {
        lv.setItems(FXCollections.observableArrayList(MainApp.notificationList));
    }


    private Node createNode(Notification item) {
        Label lblTitle = new Label(item.getTitle());
        Label lblMessage = new Label(item.getMessage());
        Label lblDate = new Label(item.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        HBox hBox;
        if (item.getFilePath() == null) {
            hBox = new HBox(5, lblDate);
        } else {
            Hyperlink button = new Hyperlink("Attachment");
//            button.getStyleClass().add("button-primary");
            button.setOnMouseClicked(e -> {
                try {
                    Desktop.getDesktop().browse(new URI(item.getFilePath()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            Label lSpace = new Label();
            lSpace.setPrefWidth(100);
            lSpace.setMaxWidth(100);
            hBox = new HBox(5, lblDate, lSpace, button);
        }

        hBox.setAlignment(Pos.CENTER_LEFT);
        VBox vb = new VBox(5, lblTitle, lblMessage, hBox, new Separator());
        return vb;
    }

    private void sendAcknowledgerment() {
        StringBuffer code = new StringBuffer();
        for (Notification notification : MainApp.notificationList) {
            code.append(notification.getBulkNotificationId());
            code.append(",");
        }
        var task = new NotificationAcknowledgementTask(code.substring(0, code.length() - 1));
        task.setOnSucceeded(e -> {
            System.out.println("Done");
        });
        new Thread(task).start();
    }


    @Override
    public void loadData() {
        tableCollection.setPlaceholder(new Label("Loading data..."));
        var task = new MilkCollectionLoadTask(
                CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue()));
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollection> list = task.get();
                if (list == null) {
                    tableCollection.setPlaceholder(new Label("No data..."));
                } else {
                    if (listCollectionSummary != null && !listCollectionSummary.isEmpty())
                        listCollectionSummary.clear();
                    listMilkCollection.clear();
                    listMilkCollection.addAll(list);
                    if (listMilkType != null && !listMilkType.isEmpty()) {
                        for (MilkType item : listMilkType) {
                            int memberCount = 0;
                            BigDecimal amt = BigDecimal.ZERO;
                            BigDecimal qty = BigDecimal.ZERO;
                            BigDecimal kgFat = BigDecimal.ZERO;
                            BigDecimal snf = BigDecimal.ZERO;
                            for (MilkCollection milkCollection : listMilkCollection.stream().filter(p -> p.getMilkType().getCode() ==
                                    item.getCode()).collect(Collectors.toList())) {
                                memberCount++;
                                amt = amt.add(milkCollection.getAmount());
                                qty = qty.add(milkCollection.getQty());
                                kgFat = kgFat.add(milkCollection.getFat().multiply(milkCollection.getQty()).
                                                divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
                                        .setScale(2, RoundingMode.HALF_UP);
                                snf = snf.add(milkCollection.getSnf().multiply(milkCollection.getQty()).
                                                divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
                                        .setScale(2, RoundingMode.HALF_UP);
                            }
                            CollectionSummary summary = new CollectionSummary(item, memberCount, qty.setScale(SCALE, ROUND),
                                    amt.setScale(SCALE, ROUND));
                            if (qty.doubleValue() > 0)
                                summary.setAvgFat(BigDecimal.valueOf(kgFat.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
                            else
                                summary.setAvgFat(BigDecimal.ZERO);
                            if (qty.doubleValue() > 0)
                                summary.setAvgSnf(BigDecimal.valueOf(snf.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
                            else
                                summary.setAvgSnf(BigDecimal.ZERO);
                            listCollectionSummary.add(summary);
                            tableCollection.setItems(listCollectionSummary);
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        try {
            colSummaryMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colSummaryMemberNos.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMemberCount()));
            colSummaryQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
            colSummaryAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colAvgFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgFat()));
            colAvgSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgSnf()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMilkType() {
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null) {
                    listMilkType.addAll(list);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

    @Override
    public void setupComboBox() {
        dpDate.setConverter(new LocalDateConvertor());
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });

        cboxShift.setConverter(new ShiftConvertor(cboxShift));
    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    cboxShift.setItems(FXCollections.observableList(list1));
                    if (LocalTime.now().isBefore(LocalTime.of(15, 0))) {
                        cboxShift.getSelectionModel().select(0);
                    } else {
                        cboxShift.getSelectionModel().select(1);
                    }
                    loadData();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    class PendingSyncTask extends Task<Long> {

        @Override
        protected Long call() throws Exception {
            try {
                BroadcastedRepository repository = EmcsAppContext.getContext().getBean(BroadcastedRepository.class);
                return repository.count();
            } catch (Exception e) {
                return 0L;
            }
        }
    }
}
