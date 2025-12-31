package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.task.PendingSyncTask;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.DpuIncentiveRequestLoadTask;
import com.eipl.amcs.operation.procurement.task.MilkCollectionLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.task.BroadcastedTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
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
    ObservableList<RowData> listCollectionSummary = FXCollections.observableArrayList();
    List<MilkType> listMilkType = new ArrayList<>();
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblCode, lblName, lblDock, lblFinancialYear, lblLanguage, lblUserName, lblSyncCount;
    @FXML
    private TableView<RowData> tableCollection;
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
    @FXML
    private ComboBox<String> cboxYear;
    @FXML
    private Button btnSearch;
    @FXML
    private LineChart<String, Number> lineChart;

    private Stage stage;
    private ResourceBundle resources;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setResizable(false);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void loadNotificationList() {
        if (cboxNotification == null || lv == null) return;
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
        this.resources = resourceBundle;
        loadControls();
        setupComboBox();
        loadShift();
        loadMilkType();
        setupTable();
        if (cboxNotification != null) {
            cboxNotification.getItems().addAll("All", "Alert", "Paripatra", "Special Message", "Milk Bill", "Bacteria Test", "EVEREST Bill");
            cboxNotification.getSelectionModel().select(0);
            cboxNotification.setOnAction(e -> {
                loadNotificationList();
            });
        }
        loadData();
//        if (btnMilkCollection != null) FocusUtils.requestFocus(btnMilkCollection);

        if (cboxLang != null) {
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
                MainApp.getContentPane().setTop(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/HeaderBar.fxml")));
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
            });
        }

        if (btnLoad != null) btnLoad.setOnAction(e -> loadData());
        if (dpDate != null) dpDate.setValue(LocalDate.now());

//        if (btnMilkCollection != null) {
//            btnMilkCollection.setOnAction(e -> openMilkCollection());
//            btnMilkCollection.setText(btnMilkCollection.getText() + " (F1)");
//        }
//        if (btnLocalMilkSale != null) {
//            btnLocalMilkSale.setText(btnLocalMilkSale.getText() + " (F2)");
//            btnLocalMilkSale.setOnAction(e -> openLocalMilkSale());
//        }
//        if (btnMilkDispatch != null) {
//            btnMilkDispatch.setText(btnMilkDispatch.getText() + " (F3)");
//            btnMilkDispatch.setOnAction(e -> openMilkDispatch());
//        }
//        if (btnProductSale != null) {
//            btnProductSale.setText(btnProductSale.getText() + " (F5)");
//            btnProductSale.setOnAction(e -> openProductSale());
//        }
//        if (btnBilling != null) {
//            btnBilling.setText(btnBilling.getText() + " (F7)");
//            btnBilling.setOnAction(e -> openBilling());
//        }
//        if (btnKapaat != null) {
//            btnKapaat.setText(btnKapaat.getText() + " (F6)");
//            btnKapaat.setOnAction(e -> openKapaat());
//        }
//        if (btnMilkReceipt != null) {
//            btnMilkReceipt.setText(btnMilkReceipt.getText() + " (F4)");
//            btnMilkReceipt.setOnAction(e -> openReceipt());
//        }

        loadMsg();
        loadNotification();

        if (btnSync != null) {
            btnSync.setOnAction(e -> {
                MainApp.paneDrop.setVisible(true);
                MainApp.lblMessage.setText("Data Syncing...");
                BroadcastedTask task = new BroadcastedTask();
                task.setOnSucceeded(e1 -> {
                    System.out.println("Done");
                    MainApp.paneDrop.setVisible(false);
                    if (lblSyncCount != null) lblSyncCount.setText("0");
                });
                new Thread(task).start();
            });
        }
        if (btnPendingSync != null) {
            btnPendingSync.setOnAction(e -> {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SyncDataList", null, this);
            });
        }
        if (root != null) {
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
                        if (cboxLang != null) {
                            if (cboxLang.getSelectionModel().getSelectedIndex() == 0)
                                cboxLang.getSelectionModel().select(1);
                            else if (cboxLang.getSelectionModel().getSelectedIndex() == 1)
                                cboxLang.getSelectionModel().select(2);
                            else
                                cboxLang.getSelectionModel().select(0);
                        }else{
                            if (cboxLang.getSelectionModel().getSelectedIndex() == 0)
                                cboxLang.getSelectionModel().select(1);
                            else if (cboxLang.getSelectionModel().getSelectedIndex() == 1)
                                cboxLang.getSelectionModel().select(2);
                            else
                                cboxLang.getSelectionModel().select(0);
                        }
                        break;
                }
            });
        }
        fetchPendingSync();
        loadTimingList();

        // Initialize Year ComboBox
        int currentYear = LocalDate.now().getYear();
        List<String> years = new ArrayList<>();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            years.add(String.valueOf(i));
        }
        if (cboxYear != null) {
            cboxYear.setItems(FXCollections.observableArrayList(years));
            cboxYear.getSelectionModel().selectFirst();
        }

        if (btnSearch != null) btnSearch.setOnAction(e -> loadChartData());
        loadChartData();
    }

    private void loadChartData() {
        if (cboxYear == null || lineChart == null) return;
        String selectedYearStr = cboxYear.getValue();
        if (selectedYearStr == null) return;

        int selectedYear = Integer.parseInt(selectedYearStr);
        LocalDateTime fromDate = LocalDateTime.of(selectedYear, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(selectedYear, 12, 31, 23, 59);

        var task = new MilkCollectionLoadTask(fromDate, toDate, 0);
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollection> list = task.get();
                if (list != null) {
                    updateLineChart(list);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void updateLineChart(List<MilkCollection> list) {
        if (lineChart == null) return;
        lineChart.setAnimated(false);
        lineChart.getData().clear();

        // Configure X Axis (CategoryAxis)
        if (lineChart.getXAxis() instanceof CategoryAxis) {
            CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
            ObservableList<String> months = FXCollections.observableArrayList();
            for (Month month : Month.values()) {
                months.add(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            }
            xAxis.getCategories().setAll(months);
        }

        // Configure Y Axis (NumberAxis)
        if (lineChart.getYAxis() instanceof NumberAxis) {
            NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(10000);
            yAxis.setTickUnit(1000);
        }

        Map<String, Map<Month, Double>> dataMap = new HashMap<>();

        for (MilkCollection collection : list) {
            if (collection.getMilkType() != null && collection.getQty() != null) {
                String typeName = collection.getMilkType().getName();
                if (typeName == null) typeName = "Unknown";

                dataMap.putIfAbsent(typeName, new EnumMap<>(Month.class));
                Map<Month, Double> monthData = dataMap.get(typeName);

                Month month = collection.getCollectionDate().getMonth();
                double currentQty = monthData.getOrDefault(month, 0.0);
                monthData.put(month, currentQty + collection.getQty().doubleValue());
            }
        }

        for (Map.Entry<String, Map<Month, Double>> entry : dataMap.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey());

            Map<Month, Double> monthData = entry.getValue();
            for (Month month : Month.values()) {
                String monthName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                series.getData().add(new XYChart.Data<>(monthName, monthData.getOrDefault(month, 0.0)));
            }
            lineChart.getData().add(series);

            String color = null;
            if ("Cow".equalsIgnoreCase(entry.getKey())) {
                color = "orange";
            } else if ("Buffalo".equalsIgnoreCase(entry.getKey())) {
                color = "blue";
            } else if ("Mix".equalsIgnoreCase(entry.getKey())) {
                color = "red";
            }

            if (color != null) {
                final String cssColor = color;
                javafx.application.Platform.runLater(() -> {
                    Node line = series.getNode();
                    if (line != null) {
                        line.setStyle("-fx-stroke: " + cssColor + ";");
                    }
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        Node symbol = data.getNode();
                        if (symbol != null) {
                            symbol.setStyle("-fx-background-color: " + cssColor + ", white;");
                        }
                    }
                    for (Node n : lineChart.lookupAll(".chart-legend-item")) {
                        if (n instanceof Label && ((Label) n).getText().equalsIgnoreCase(entry.getKey())) {
                            Node symbol = n.lookup(".chart-legend-item-symbol");
                            if (symbol != null) {
                                symbol.setStyle("-fx-background-color: " + cssColor + ", white;");
                            }
                        }
                    }
                });
            }
        }
        lineChart.setLegendSide(javafx.geometry.Side.BOTTOM);
    }

    private void loadTimingList() {
        if (dpDate == null) return;
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
        if (cboxLang == null) return;
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
                    if (lblSyncCount != null) lblSyncCount.setText("NA");
                    return;
                }
                if (lblSyncCount != null) lblSyncCount.setText(count.toString());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
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
        if (lblCode != null) lblCode.setText(MainApp.identityDto.getSociety().getCodeEx());
        if (lblName != null) lblName.setText(MainApp.identityDto.getSociety().getName());
        if (lblDock != null) lblDock.setText(MainApp.identityDto.getDock().getDockNo());
        if (lblUserName != null) lblUserName.setText(MainApp.user.getUsername());
        try {
            if (lblFinancialYear != null) lblFinancialYear.setText(MainApp.getFinancialYear().toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void loadMsg() {
        if (lv == null) return;
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
        if (lv == null) return;
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

    @Override
    public void loadData() {
        if (tableCollection == null || dpDate == null || cboxShift == null) return;
        tableCollection.setPlaceholder(new Label("Loading data..."));
        var task = new MilkCollectionLoadTask(
                CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue()));
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollection> list = task.get();
                if (list == null) {
                    tableCollection.setPlaceholder(new Label("No data..."));
                } else {
                    listMilkCollection.clear();
                    listMilkCollection.addAll(list);

                    listCollectionSummary.clear();
                    tableCollection.getColumns().clear();

                    RowData rowMember = new RowData(getString("member.nos"));
                    RowData rowQty = new RowData(getString("qty"));
                    RowData rowAmount = new RowData(getString("amount"));
                    RowData rowFat = new RowData(getString("avgfat"));
                    RowData rowSnf = new RowData(getString("avgsnf"));

                    TableColumn<RowData, String> colDesc = new TableColumn<>("");
                    colDesc.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDescription()));
                    tableCollection.getColumns().add(colDesc);

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
                            
                            BigDecimal avgFat = BigDecimal.ZERO;
                            if (qty.doubleValue() > 0)
                                avgFat = BigDecimal.valueOf(kgFat.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP);
                            
                            BigDecimal avgSnf = BigDecimal.ZERO;
                            if (qty.doubleValue() > 0)
                                avgSnf = BigDecimal.valueOf(snf.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP);

                            String key = item.getCode().toString();
                            rowMember.getData().put(key, memberCount);
                            rowQty.getData().put(key, qty.setScale(SCALE, ROUND));
                            rowAmount.getData().put(key, amt.setScale(SCALE, ROUND));
                            rowFat.getData().put(key, avgFat);
                            rowSnf.getData().put(key, avgSnf);

                            TableColumn<RowData, Object> col = new TableColumn<>(item.toString());
                            col.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getData().get(key)));
                            col.setStyle("-fx-alignment: CENTER-RIGHT;");
                            tableCollection.getColumns().add(col);
                        }
                    }
                    listCollectionSummary.addAll(rowMember, rowQty, rowAmount, rowFat, rowSnf);
                    tableCollection.setItems(listCollectionSummary);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private String getString(String key) {
        try {
            if (resources != null) {
                return resources.getString(key);
            }
        } catch (Exception e) {
            // ignore
        }
        return key;
    }

    @Override
    public void setupTable() {
        // Columns are generated dynamically in loadData
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
        if (dpDate != null) {
            dpDate.setConverter(new LocalDateConvertor());
            dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
                }
            });
        }

        if (cboxShift != null) cboxShift.setConverter(new ShiftConvertor(cboxShift));
    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    if (cboxShift != null) {
                        cboxShift.setItems(FXCollections.observableList(list1));
                        if (LocalTime.now().isBefore(LocalTime.of(15, 0))) {
                            cboxShift.getSelectionModel().select(0);
                        } else {
                            cboxShift.getSelectionModel().select(1);
                        }
                    }
                    loadData();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public static class RowData {
        private String description;
        private Map<String, Object> data = new HashMap<>();

        public RowData(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public Map<String, Object> getData() {
            return data;
        }
    }
}