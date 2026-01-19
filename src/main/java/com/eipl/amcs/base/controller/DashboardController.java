package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.task.PendingSyncTask;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.DpuIncentiveRequestLoadTask;
import com.eipl.amcs.operation.procurement.task.MemberDataSummaryLoadTask;
import com.eipl.amcs.operation.procurement.task.MilkCollectionLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.task.BroadcastedGroupDataTask;
import com.eipl.amcs.utils.task.BroadcastedTask;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    public DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ObservableList<MilkCollection> listMilkCollection = FXCollections.observableArrayList();
    ObservableList<RowData> listCollectionSummary = FXCollections.observableArrayList();
    List<MilkType> listMilkType = new ArrayList<>();
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblCode, lblName, lblDock, lblFinancialYear, lblLanguage, lblUserName, lblSyncCount,lbltollfree,lbltiming,lblemail;
    @FXML
    private TableView<RowData> tableCollection;
    @FXML
    private TableView<MilkCollectionSummaryData> tableCollectionFarmers;
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
    private ComboBox<String> cboxYear, cboxYearNotification, cboxMonth, cboxMonthMember, cboxYearMember;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private Button btnSearch;
    @FXML
    private LineChart<String, Number> lineChart;

    private final ObservableList<TableData> tableDataList = FXCollections.observableArrayList();
    @FXML
    private TableView<TableData> tablePendingSync;
    @FXML
    private TableColumn<TableData, String> colTableName;
//    @FXML
//    private E_Button btnClose;
    @FXML
    private TableColumn<TableData, Integer> colPendingData;

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
        if (cboxNotification == null || lv == null || cboxYearNotification == null || cboxMonth == null) {
            return;
        }
        int selectedTypeIndex = cboxNotification.getSelectionModel().getSelectedIndex();
        String selectedYear = cboxYearNotification.getSelectionModel().getSelectedItem();
        String selectedMonth = cboxMonth.getSelectionModel().getSelectedItem();

        List<Notification> filtered = MainApp.notificationList.stream()
                .filter(n -> {
                    if (n.getWefDate() == null) return false;
                    String entryYear = String.valueOf(n.getWefDate().getYear());
                    String entryMonth = n.getWefDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                    boolean matchesDate = entryYear.equals(selectedYear) && entryMonth.equals(selectedMonth);
                    boolean matchesType;
                    if (selectedTypeIndex == 0) {
                        matchesType = true;
                    } else {
                        matchesType = (n.getNotificationType() != null && n.getNotificationType() == selectedTypeIndex);
                    }
                    return matchesDate && matchesType;
                })
                .collect(Collectors.toList());
        lv.setItems(FXCollections.observableList(filtered));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        loadControls();
        setupComboBox();
        loadShift();
        loadMilkType();
        setupTable();
        setupDateFilters();
        loadFarmers();
        cboxMonthMember.setOnAction(e -> loadFarmers());
        cboxYearMember.setOnAction(e -> loadFarmers());
        cboxMilkType.setOnAction(e -> loadFarmers());
        if (cboxNotification != null) {
            cboxNotification.getItems().addAll("All", "Alert", "Paripatra", "Special Message", "Milk Bill", "Bacteria Test", "EVEREST Bill");
            cboxNotification.getSelectionModel().select(0);
        }
        cboxYearNotification.setOnAction(e -> loadNotificationList());
        cboxMonth.setOnAction(e -> loadNotificationList());
        cboxNotification.setOnAction(e -> loadNotificationList());

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
//                MainApp.getContentPane().setMaxWidth(rect.getWidth());
//                MainApp.getContentPane().setMaxHeight(rect.getHeight());
                MainApp.getContentPane().setTop(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/HeaderBar.fxml")));
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
            });
        }
        if (btnLoad != null) btnLoad.setOnAction(e -> loadData());
        if (dpDate != null) dpDate.setValue(LocalDate.now());
        loadMsg();

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
            root.setFocusTraversable(true);
            Platform.runLater(() -> root.requestFocus());
            root.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
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
        }
        fetchPendingSync();
        loadTimingList();
        loadChartData();
        // if (btnSearch != null) btnSearch.setOnAction(e -> loadChartData());
        cboxYear.setOnAction(e -> loadChartData());
    }

    private void setupDateFilters() {
        // year
        int currentYear = LocalDate.now().getYear();
        List<String> years = new ArrayList<>();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            years.add(String.valueOf(i));
        }
        ObservableList<String> yearOptions = FXCollections.observableArrayList(years);
        if (cboxYear != null) {
            cboxYear.setItems(yearOptions);
            cboxYear.getSelectionModel().selectFirst();
        }
        if (cboxYearNotification != null) {
            cboxYearNotification.setItems(yearOptions);
            cboxYearNotification.getSelectionModel().selectFirst();
        }
        if (cboxYearMember != null) {
            cboxYearMember.setItems(yearOptions);
            cboxYearMember.getSelectionModel().selectFirst();
        }

        //  month
        ObservableList<String> months = FXCollections.observableArrayList();
        for (Month month : Month.values()) {
            months.add(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        }
        String currentMonthName = LocalDate.now()
                .getMonth()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        if (cboxMonth != null) {
            cboxMonth.setItems(months);
            cboxMonth.getSelectionModel().select(currentMonthName);
        }
        if (cboxMonthMember != null) {
            cboxMonthMember.setItems(months);
            cboxMonthMember.getSelectionModel().select(currentMonthName);
        }
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

            int seriesIndex = 1;

            String cssColor = "";
            if ("Cow".equalsIgnoreCase(series.getName())) {
                cssColor = "orange";
            } else if ("Buffalo".equalsIgnoreCase(series.getName())) {
                cssColor = "blue";
            } else if ("Mix".equalsIgnoreCase(series.getName())) {
                cssColor = "red";
            }

            if (!cssColor.isEmpty()) {
                lineChart.setStyle(lineChart.getStyle() + String.format("CHART_COLOR_%d: %s;", seriesIndex, cssColor));
            }
            seriesIndex++;

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
        lbltollfree.setText("0000-0000-0000");
        lblemail.setText("0000-0000-0000");
        lbltiming.setText(LocalDateTime.now().format(formatter1));
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

                    RowData rowMember = new RowData(getString(resources.getString("members")));
                    RowData rowQty = new RowData(getString(resources.getString("qty")));
                    RowData rowAmount = new RowData(getString(resources.getString("amount")));
                    RowData rowFat = new RowData(getString(resources.getString("avgfat")));
                    RowData rowSnf = new RowData(getString(resources.getString("avgsnf")));

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
                    listCollectionSummary.addAll(rowMember, rowQty, rowFat, rowSnf, rowAmount);
                    tableCollection.setItems(listCollectionSummary);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        tableDataList.clear();

        Task<Map<String, Integer>> task2 = new BroadcastedGroupDataTask();
        task2.setOnSucceeded(e -> {
            tableDataList.clear();
            Map<String, Integer> result = task2.getValue();
            if (result != null) {
                for (Map.Entry<String, Integer> entry : result.entrySet()) {
                    tableDataList.add(new TableData(entry.getKey(), entry.getValue()));
                }
            }
        });

        task2.setOnFailed(e -> {
            Throwable ex = task2.getException();
            ex.printStackTrace();
        });

        new Thread(task2).start();
    }

    private void loadFarmers() {
        if (tableCollectionFarmers == null) return;
        tableCollectionFarmers.setPlaceholder(new Label("Loading top 10 members..."));

        Month month = Month.from(CommonUtils.MONTH_SHORT_FORMATTER.parse(cboxMonthMember.getSelectionModel().getSelectedItem()));
        int selectedMonth = month.getValue();
        int selectedYear = Integer.parseInt(cboxYearMember.getSelectionModel().getSelectedItem());

        Integer selectedMilkTypeCode = null;
//        Object selectedItem = cboxMilkType.getValue();
//        if (selectedItem instanceof MilkType) {
//            MilkType mt = (MilkType) selectedItem;
//            if (!"All".equalsIgnoreCase(mt.getName())) {
//                selectedMilkTypeCode = mt.getCode();
//            }
//        }
        var selectedItem = cboxMilkType.getValue();
        if (selectedItem != null && !"All".equalsIgnoreCase(selectedItem.getName())) {
            selectedMilkTypeCode = selectedItem.getCode();
        }
        setupSummaryTableColumns();
        var summaryTask = new MemberDataSummaryLoadTask(selectedYear, selectedMonth, selectedMilkTypeCode);
        summaryTask.setOnSucceeded(e -> {
            try {
                List<MilkCollectionSummaryData> summaries = summaryTask.get();
                if (summaries == null || summaries.isEmpty()) {
                    tableCollectionFarmers.getItems().clear();
                    tableCollectionFarmers.setPlaceholder(new Label("No member data found."));
                } else {
                    ObservableList<MilkCollectionSummaryData> observableSummaries = FXCollections.observableArrayList(summaries);
                    tableCollectionFarmers.setItems(observableSummaries);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                tableCollectionFarmers.setPlaceholder(new Label("Error loading member data."));
            }
        });

        new Thread(summaryTask).start();
    }

    private void setupSummaryTableColumns() {
        tableCollectionFarmers.getColumns().clear();
        TableColumn<MilkCollectionSummaryData, String> memberCodeCol = new TableColumn<>(resources.getString("members"));
        memberCodeCol.setCellValueFactory(cellData -> {
            String fullCode = cellData.getValue().getMember().getCode();
            if (fullCode != null && fullCode.length() >= 4) {
                String lastFour = fullCode.substring(fullCode.length() - 4);
                return new SimpleStringProperty(lastFour);
            }
            return new SimpleStringProperty(fullCode == null ? "" : fullCode);
        });

        TableColumn<MilkCollectionSummaryData, String> milkTypeCodeCol = new TableColumn<>(resources.getString("milktype"));
        milkTypeCodeCol.setCellValueFactory(cellData -> {
            int code = cellData.getValue().getMilkType().getCode();

            String typeName = listMilkType.stream()
                    .filter(t -> t.getCode() == code)
                    .map(MilkType::getName)
                    .findFirst()
                    .orElse("Unknown (" + code + ")");

            return new SimpleStringProperty(typeName);
        });
        TableColumn<MilkCollectionSummaryData, BigDecimal> qtyCol = new TableColumn<>(resources.getString("totalqty"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("milkQuantity"));
        qtyCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<MilkCollectionSummaryData, BigDecimal> amountCol = new TableColumn<>(resources.getString("totalamount"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("milkAmount"));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        tableCollectionFarmers.getColumns().addAll(memberCodeCol, milkTypeCodeCol, qtyCol, amountCol);
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
        tableCollection.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableCollectionFarmers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colTableName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTableName()));
        colPendingData.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPendingCount()).asObject());
        tablePendingSync.setItems(tableDataList);
        tablePendingSync.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    private void loadMilkType() {
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null) {
                    listMilkType.addAll(list);
                    List<MilkType> comboList = new ArrayList<>();
                    MilkType milkType = new MilkType();
                    milkType.setName(resources.getString("all"));
                    comboList.add(0, milkType);
                    comboList.addAll(list);
                    cboxMilkType.setItems(FXCollections.observableList(comboList));
                    cboxMilkType.getSelectionModel().selectFirst();
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

    @Getter
    @AllArgsConstructor
    public static class TableData {
        private final String tableName;
        private final int pendingCount;
    }
}