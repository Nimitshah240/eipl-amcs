package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.procurement.dto.PurchaseRateGenerate;
import com.eipl.amcs.master.procurement.dto.RateViewDto;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
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

import static com.eipl.amcs.MainApp.context;

public class MilkRateViewController implements MyInitialization {
    @FXML
    private Button btnClose;
    @FXML
    private ComboBox<MilkQualityType> cboxMilkQualityType;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private Label lblTitle;
    @FXML
    private StackPane root;
    @FXML
    private TableView<PurchaseRateGenerate> tableRateDetails;

    private Map<String, List<PurchaseRateGenerate>> mapTableData = new HashMap<>();
    private RateViewDto rateViewDto;
    private RateType rateType;
    private ResourceBundle resourceBundle;
    private Stage stage;
    private MemberMilkPurchaseRateService memberMilkPurchaseRateService;
    private SocietyMilkPurchaseRateService societyMilkPurchaseRateService;
    private MilkTypeService milkTypeService;
    private MilkQualityTypeService milkQualityTypeService;

    public MilkRateViewController() {
        memberMilkPurchaseRateService = context.getBean(MemberMilkPurchaseRateService.class);
        societyMilkPurchaseRateService = context.getBean(SocietyMilkPurchaseRateService.class);
        milkTypeService = context.getBean(MilkTypeService.class);
        milkQualityTypeService = context.getBean(MilkQualityTypeService.class);
    }

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
        tableRateDetails.getItems().clear();
        tableRateDetails.setPlaceholder(new Label("Loading data..."));
        try {
            if (rateViewDto.getRateType() == (short) 0) {
                List<String> list = memberMilkPurchaseRateService.fetchRateDetails(rateViewDto.getRateType() == (short) 0 ? rateViewDto.getMemberRate().getCode() : rateViewDto.getSocietyRate().getCode(),
                        cboxMilkType.getValue().getCode(), cboxMilkQualityType.getValue().getCode());
                if (list == null || list.isEmpty()) {
                    tableRateDetails.setPlaceholder(new Label("No data..."));
                    return;
                }
                prepareMap(list);
                prepareTableData();
            } else {
                List<String> list = societyMilkPurchaseRateService.fetchRateDetails(rateViewDto.getRateType() == (short) 0 ? rateViewDto.getMemberRate().getCode() : rateViewDto.getSocietyRate().getCode(),
                        cboxMilkType.getValue().getCode(), cboxMilkQualityType.getValue().getCode());
                if (list == null || list.isEmpty()) {
                    tableRateDetails.setPlaceholder(new Label("No data..."));
                    return;
                }
                prepareMap(list);
                prepareTableData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        if (tableRateDetails.getItems() != null)
            tableRateDetails.getItems().clear();
        if (tableRateDetails.getColumns() != null)
            tableRateDetails.getColumns().clear();

        TableColumn<PurchaseRateGenerate, Number> col1 = new TableColumn<>(rateType != null ? rateType.getRateType() : "");
        col1.setCellValueFactory(
                cellData -> new SimpleDoubleProperty(cellData.getValue().getFat().doubleValue()));
        col1.getStyleClass().add("rate-first-column");
        col1.getStyleClass().add("cell-center-aligned");
        tableRateDetails.getColumns().add(col1);

        List<PurchaseRateGenerate> listRate = mapTableData.get(cboxMilkType.getValue().getCode() + "#" + cboxMilkQualityType.getValue().getCode());
        listRate.get(0).getMap().forEach((k, v) -> {
            TableColumn<PurchaseRateGenerate, String> col = new TableColumn<>(String.valueOf(k));
            col.getStyleClass().add("cell-center-aligned");
            col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMap().get(k).toString()));
            tableRateDetails.getColumns().add(col);
        });
        tableRateDetails.setItems(FXCollections.observableArrayList(listRate));
    }

    @Override
    public void loadData() {
        //Nimit- ASYNC
        try {
            List<MilkType> list = milkTypeService.findAll();
            if (list != null)
                cboxMilkType.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            List<MilkQualityType> list = milkQualityTypeService.findAll();
            if (list != null)
                cboxMilkQualityType.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
