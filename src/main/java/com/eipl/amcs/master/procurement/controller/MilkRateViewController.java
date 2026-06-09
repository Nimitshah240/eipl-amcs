package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.procurement.dto.PurchaseRateGenerate;
import com.eipl.amcs.master.procurement.dto.RateViewDto;
import com.eipl.amcs.master.procurement.task.NewGeneratedRateViewTask;
import com.eipl.amcs.master.procurement.task.RateViewTask;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MilkRateViewController implements MyInitialization {
    private final Map<String, List<PurchaseRateGenerate>> mapTableData = new HashMap<>();
    @FXML
    private E_Button btnClose;
    @FXML
    private ComboBox<MilkQualityType> cboxMilkQualityType;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private Label lblTitle;
    @FXML
    private StackPane root;
    @FXML
    private TableView<PurchaseRateGenerate> tableFixed;
    @FXML
    private TableView<PurchaseRateGenerate> tableRateDetails;
    private RateViewDto rateViewDto;
    private RateType rateType;
    private ResourceBundle resourceBundle;
    private Stage stage;

    public void setRateViewDto(RateViewDto rateViewDto) {
        this.rateViewDto = rateViewDto;
        if (rateViewDto != null) {
            rateType = rateViewDto.getMemberRate() != null ? rateViewDto.getMemberRate().getRateType() : rateViewDto.getSocietyRate().getRateType();
            lblTitle.setText(rateViewDto.getMemberRate() != null ? resourceBundle.getString("membermilkpurchaserate") : resourceBundle.getString("societymilkpurchaserate"));
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        setupComboBox();

        btnClose.setOnAction(e -> stage.close());

        tableFixed.addEventFilter(javafx.scene.input.ScrollEvent.ANY, event -> {
            tableRateDetails.fireEvent(event);
            event.consume();
        });
    }

    private void syncScrollBars() {
        Node fixedScroll = tableFixed.lookup(".scroll-bar:vertical");
        Node scrollableScroll = tableRateDetails.lookup(".scroll-bar:vertical");
        if (fixedScroll instanceof ScrollBar && scrollableScroll instanceof ScrollBar) {
            ScrollBar s1 = (ScrollBar) fixedScroll;
            ScrollBar s2 = (ScrollBar) scrollableScroll;
            if (s1.getUserData() == null) {
                s1.valueProperty().bindBidirectional(s2.valueProperty());
                s1.setVisible(false);
                s1.setMaxWidth(0);
                s1.setMinWidth(0);
                s1.setPrefWidth(0);
                s1.setUserData("bound");
            }
        }
    }

    @Override
    public void setupComboBox() {
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkQualityType.setConverter(new MilkQualityConvertor(cboxMilkQualityType));

        cboxMilkType.setOnAction(e -> {
            loadRateDetailsData();
        });
        cboxMilkQualityType.setOnAction(e -> {
            loadRateDetailsData();
        });
    }

    private void loadRateDetailsData() {
        if (cboxMilkType.getValue() == null || cboxMilkQualityType.getValue() == null)
            return;
        tableFixed.getItems().clear();
        tableRateDetails.getItems().clear();
        tableRateDetails.setPlaceholder(new Label("Loading data..."));
        String code = rateViewDto.getRateType() == (short) 0 ? rateViewDto.getMemberRate().getCode() : rateViewDto.getSocietyRate().getCode();
        if (code != null) {
            var task = new RateViewTask(rateViewDto.getRateType(), rateViewDto.getRateType() == (short) 0 ? rateViewDto.getMemberRate().getCode() : rateViewDto.getSocietyRate().getCode(),
                    cboxMilkType.getValue().getCode(), cboxMilkQualityType.getValue().getCode());
            task.setOnSucceeded(e -> {
                try {
                    List<String> list = task.get();
                    if (list == null || list.isEmpty()) {
                        tableRateDetails.setPlaceholder(new Label("No data..."));
                        return;
                    }
                    prepareMap(list);
                    prepareTableData();

                    tableFixed.applyCss();
                    tableFixed.layout();
                    tableRateDetails.applyCss();
                    tableRateDetails.layout();
                    syncScrollBars();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else {
            var task = new NewGeneratedRateViewTask(rateViewDto,
                    cboxMilkType.getValue(), cboxMilkQualityType.getValue());
            task.setOnSucceeded(e -> {
                try {
                    List<String> list = task.get();
                    if (list == null || list.isEmpty()) {
                        tableRateDetails.setPlaceholder(new Label("No data..."));
                        return;
                    }
                    prepareMap(list);
                    prepareTableData();
                    tableFixed.applyCss();
                    tableFixed.layout();
                    tableRateDetails.applyCss();
                    tableRateDetails.layout();
                    syncScrollBars();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    private void prepareMap(List<String> details) {
        if (details == null || details.isEmpty())
            return;

        BigDecimal fat = BigDecimal.ZERO;
        List<PurchaseRateGenerate> listRate = new ArrayList<>();
        for (String detail : details) {
            String[] arr = detail.split("#");
            BigDecimal f = new BigDecimal(arr[0]);
            if (!Objects.equals(fat, new BigDecimal(arr[0]))) {
                PurchaseRateGenerate g = new PurchaseRateGenerate();
                g.setFat(f);
                Map<BigDecimal, BigDecimal> temp = new TreeMap<>();
                temp.put(new BigDecimal(arr[1]), new BigDecimal(arr[2]));
                g.setMap(temp);

                listRate.add(g);
            } else {
                for (PurchaseRateGenerate g : listRate) {
                    if (Objects.equals(g.getFat(), f)) {
                        g.getMap().put(new BigDecimal(arr[1]), new BigDecimal(arr[2]));
                        break;
                    }
                }
            }
            fat = f;
        }
        Collections.sort(listRate, Comparator.comparing(PurchaseRateGenerate::getFat));
        mapTableData.put(cboxMilkType.getValue().getCode() + "#" + cboxMilkQualityType.getValue().getCode(), listRate);
    }

    private void prepareTableData() {
        if (cboxMilkType.getValue() == null || cboxMilkQualityType.getValue() == null)
            return;

        if (tableFixed.getItems() != null)
            tableFixed.getItems().clear();
        if (tableFixed.getColumns() != null)
            tableFixed.getColumns().clear();
        if (tableRateDetails.getItems() != null)
            tableRateDetails.getItems().clear();
        if (tableRateDetails.getColumns() != null)
            tableRateDetails.getColumns().clear();

        TableColumn<PurchaseRateGenerate, Number> col1 = new TableColumn<>(rateType != null ? rateType.getRateType() : "");
        col1.setCellValueFactory(
                cellData -> new SimpleDoubleProperty(cellData.getValue().getFat().doubleValue()));
        col1.getStyleClass().add("rate-first-column");
        col1.getStyleClass().add("cell-center-aligned");
        col1.setPrefWidth(98);
        tableFixed.getColumns().add(col1);

        List<PurchaseRateGenerate> listRate = mapTableData.get(cboxMilkType.getValue().getCode() + "#" + cboxMilkQualityType.getValue().getCode());
        listRate.get(0).getMap().forEach((k, v) -> {
            TableColumn<PurchaseRateGenerate, String> col = new TableColumn<>(String.valueOf(k));
            col.getStyleClass().add("cell-center-aligned");
            col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMap().get(k).toString()));
            tableRateDetails.getColumns().add(col);
        });
        tableFixed.setItems(FXCollections.observableArrayList(listRate));
        tableRateDetails.setItems(FXCollections.observableArrayList(listRate));
    }

    @Override
    public void loadData() {
        var task2 = new MilkTypeLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task2.get();
                if (list != null)
                    cboxMilkType.setItems(FXCollections.observableList(list));
                cboxMilkType.getSelectionModel().selectFirst();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

        var task3 = new MilkQualityTypeLoadTask();
        task3.setOnSucceeded(e -> {
            try {
                List<MilkQualityType> list = task3.get();
                if (list != null)
                    cboxMilkQualityType.setItems(FXCollections.observableList(list));
                cboxMilkQualityType.getSelectionModel().selectFirst();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task3).start();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
