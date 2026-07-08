package com.eipl.amcs.operation.billing.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_CheckBox;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.procurement.controller.SocietyPaymentCycleEditController;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.task.BonusEditTask;
import com.eipl.amcs.operation.billing.task.BonusListLoadTask;
import com.eipl.amcs.operation.billing.task.BonusLoadTask;
import com.eipl.amcs.operation.billing.task.BonusSaveTask;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.repository.ShareRateRepository;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FormatterFactory;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BonusController extends SocietyPaymentCycleEditController implements MyInitialization, PopupCallback {
    private final ObjectProperty<Bonus> propBonus;
    public List<MilkType> milkTypeList = new ArrayList<>();
    BigDecimal qty = BigDecimal.ZERO;
    BigDecimal amt = BigDecimal.ZERO;
    BigDecimal kapaat = BigDecimal.ZERO;
    BigDecimal bonus = BigDecimal.ZERO;
    @FXML
    private StackPane root;
    @FXML
    private GridPane grid;
    @FXML
    private Button btnGenerate, btnSaveUpdate, btnClose, btnExport;
    @FXML
    private E_DatePicker dpFromDate, dpToDate;
    @FXML
    private AutoSearchTextField<String> cboxType, cboxCriteria;
    @FXML
    private AutoSearchTextField<MilkType> cboxMilkType;
    private Stage stage;
    @FXML
    private E_NumericField txtBonusValue, txtDebanture, txtSocietyKapaat;
    @FXML
    private E_CheckBox chkDebanture;
    @FXML
    private TableView<Bonus> tableBonus;
    @FXML
    private AutoSearchTextField<Shift> cboxToShift, cboxFromShift;
    @FXML
    private TableColumn<Bonus, String> colMemberCode, colMemberName, colStatus, colType, colKapaat, colRemarks, colBank, colAccount, colIfsc, colMobile;
    @FXML
    private TableColumn<Bonus, Number> colSrNo, colMilkQty, colMilkAmount, colBonusAmt, colDebanture, colTotal, colSocietyKapaat, colUnionBonus, colPayableUnionBonus;

    @FXML
    private Label lblDebantureKapaat, lblSocietyKapaat, lblBonusValue;
    @FXML
    private HBox hBoxDebanture;
    private BonusSummary bonusSummary;
    private List<Bonus> bonusList;
    private ResourceBundle resourceBundle;
    private BonusDto dto = null;
    private List<String> criteriaList;
    private List<String> typeList;
    private BigDecimal totalDebantureAmt = BigDecimal.ZERO;
    private BigDecimal totalSocietyAmount = BigDecimal.ZERO;

    private ShareRateRepository shareRateRepository;

    public BonusController() {
        propBonus = new SimpleObjectProperty<>();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBonusSummary(BonusSummary bonusSummary) {
        this.bonusSummary = bonusSummary;

        if (bonusSummary != null) {
            loadMilkType();
            loadShift();
            txtDebanture.setDisable(!bonusSummary.isDebanture());
            dpFromDate.setValue(bonusSummary.getFromDate());
            dpToDate.setValue(bonusSummary.getToDate());
            cboxCriteria.getSelectionModel().select(bonusSummary.getBonusCriteria() == 0 ? 0 : bonusSummary.getBonusCriteria() == 1 ? 1 : 2);
            cboxType.getSelectionModel().select(bonusSummary.getType() == 0 ? 0 : bonusSummary.getType() == 1 ? 1 : 2);
            txtBonusValue.setText(String.valueOf(bonusSummary.getBonusCriteriaValue()));
            txtDebanture.setText(String.valueOf(bonusSummary.getDebantureAmount()));
            txtSocietyKapaat.setText(String.valueOf(bonusSummary.getSocietyAmount()));
            chkDebanture.setSelected(bonusSummary.isDebanture());
            loadControls();
        }
    }

    @Override
    public void loadControls() {
        var task = new BonusListLoadTask(bonusSummary.getCode());
        task.setOnSucceeded(event -> {
            try {
                dto = task.get();
                // Create a mutable list and sort by member code
                bonusList = new ArrayList<>(dto.getBonusList());
                bonusList.sort(Comparator.comparing(b -> b.getMember().getCodeEx()));
                tableBonus.setItems(FXCollections.observableArrayList(bonusList));
            } catch (Exception exception) {

            }
        });
        new Thread(task).start();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        if (EmcsAppContext.getContext() != null) {
            shareRateRepository = EmcsAppContext.getContext().getBean(ShareRateRepository.class);
        }
        txtDebanture.setDisable(true);
        colKapaat.setVisible(false);
        btnExport.setDisable(false);
        bonusList = new ArrayList<>();
        criteriaList = new ArrayList<>();
        criteriaList.add(resourceBundle.getString("percentage"));
        criteriaList.add(resourceBundle.getString("rs"));
        criteriaList.add(resourceBundle.getString("total"));
        typeList = new ArrayList<>();
        typeList.add(resourceBundle.getString("unionbonus"));
        typeList.add(resourceBundle.getString("societybonus"));
        cboxCriteria.getItems().addAll(criteriaList);
        cboxType.getItems().addAll(typeList);
        cboxCriteria.getSelectionModel().select(0);
        cboxType.getSelectionModel().select(0);
        setupTable();
        setupComboBox();
        loadData();
        loadMilkType();
        loadShift();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/billing/BonusSummary.fxml"))));

        btnGenerate.setDisable(!(this.bonusSummary == null || bonusSummary.getStatus() < (short) 2));
        btnGenerate.setOnAction(e -> {
            if (bonusSummary != null) {
                MyAlert calert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("prevdatawillbedeleted"));
                Optional<ButtonType> resp = calert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    try {
                        if (dpFromDate.getValue() != null && dpToDate.getValue() != null && cboxType.getValue() != null && cboxCriteria.getValue() != null && txtBonusValue.getInputText() != null && Double.parseDouble(txtBonusValue.getInputText()) > 0)
                            loadData(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()), CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()), cboxType.getValue(), cboxCriteria.getValue(), txtBonusValue.getInputText(), cboxMilkType.getValue());
                        else {
                            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("entervalid"));
                            alert.createAlert();
                        }
                    } catch (NumberFormatException ee) {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("entervalid"));
                        alert.createAlert();
                    }
                }
            } else {
                try {
                    if (dpFromDate.getValue() != null && dpToDate.getValue() != null && cboxType.getValue() != null && cboxCriteria.getValue() != null && txtBonusValue.getInputText() != null && Double.parseDouble(txtBonusValue.getInputText()) > 0)
                        loadData(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()), CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()), cboxType.getValue(), cboxCriteria.getValue(), txtBonusValue.getInputText(), cboxMilkType.getValue());
                    else {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("entervalid"));
                        alert.createAlert();
                    }
                } catch (NumberFormatException ee) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("entervalid"));
                    alert.createAlert();
                }
            }
        });
        btnSaveUpdate.setOnAction(event -> {
            saveData();
        });

        btnExport.setOnAction(event -> exportToCsv());

        txtDebanture.setText("0");
        txtSocietyKapaat.setText("0");
        cboxType.textProperty().addListener(e -> {
            if (cboxType.getSelectionModel().getSelectedIndex() == 1) {

                colDebanture.setVisible(false);
                txtDebanture.setVisible(false);
                lblDebantureKapaat.setVisible(false);
                hBoxDebanture.setVisible(false);

                colKapaat.setVisible(false);
                colSocietyKapaat.setVisible(false);

                hBoxDebanture.setManaged(false);
                lblDebantureKapaat.setManaged(false);
                txtDebanture.setManaged(false);

                // Hide Society Kapaat
                lblSocietyKapaat.setVisible(false);
                lblSocietyKapaat.setManaged(false);
                txtSocietyKapaat.setVisible(false);
                txtSocietyKapaat.setManaged(false);
                colPayableUnionBonus.setVisible(false);

                // Move Bonus Value to Society Kapaat position
                GridPane.setColumnIndex(lblBonusValue, 2);
                GridPane.setColumnIndex(txtBonusValue, 2);

                GridPane.setColumnIndex(btnGenerate, 0);
                GridPane.setRowIndex(btnGenerate, 5);

            } else {

                colDebanture.setVisible(true);
                txtDebanture.setVisible(true);
                lblDebantureKapaat.setVisible(true);
                hBoxDebanture.setVisible(true);

                colKapaat.setVisible(false);
                colSocietyKapaat.setVisible(true);

                hBoxDebanture.setManaged(true);
                lblDebantureKapaat.setManaged(true);
                txtDebanture.setManaged(true);

                // Show Society Kapaat again
                lblSocietyKapaat.setVisible(true);
                lblSocietyKapaat.setManaged(true);
                txtSocietyKapaat.setVisible(true);
                txtSocietyKapaat.setManaged(true);
                colPayableUnionBonus.setVisible(true);

                // Move Bonus Value back
                GridPane.setColumnIndex(lblBonusValue, 3);
                GridPane.setColumnIndex(txtBonusValue, 3);

                GridPane.setColumnIndex(btnGenerate, 3);
                GridPane.setRowIndex(btnGenerate, 5);
            }
        });

        chkDebanture.setOnAction(e -> {
            txtDebanture.setDisable(!chkDebanture.isSelected());
        });
    }

    private void exportToCsv() {
        if (tableBonus.getItems() == null || tableBonus.getItems().isEmpty()) {
            new InformationAlert(MainApp.getStage(), resourceBundle.getString("export"), resourceBundle.getString("no.data.to.export")).createAlert();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("export"));
        fileChooser.setInitialFileName("Bonus_Details.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {

                // Add UTF-8 BOM
                fos.write(0xEF);
                fos.write(0xBB);
                fos.write(0xBF);

                // Custom Header
                String societyName = MainApp.identityDto.getSociety().getName() + " (" + MainApp.identityDto.getSociety().getCode() + ")";
                writer.append(societyName).append('\n');
                writer.append("Bonus Details").append('\n');
                String exportDate = "Export Date" + ": " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                writer.append(exportDate).append('\n');
                writer.append('\n');

                // Get visible columns
                List<TableColumn<Bonus, ?>> visibleColumns = tableBonus.getColumns().stream()
                        .filter(TableColumn::isVisible)
                        .collect(Collectors.toList());

                // Write header row
                String header = visibleColumns.stream()
                        .map(TableColumn::getText)
                        .collect(Collectors.joining(","));
                writer.append(header).append('\n');

                // Write data rows
                for (Bonus item : tableBonus.getItems()) {
                    String row = visibleColumns.stream()
                            .map(col -> {
                                ObservableValue<?> observable = col.getCellObservableValue(item);
                                if (observable == null || observable.getValue() == null) {
                                    return "";
                                }
                                return escapeCsv(String.valueOf(observable.getValue()));
                            })
                            .collect(Collectors.joining(","));
                    writer.append(row).append('\n');
                }

                // Summary Row
                writer.append('\n');
                List<String> summaryRow = new ArrayList<>();
                summaryRow.add("Total");
                for (int i = 1; i < visibleColumns.size(); i++) {
                    TableColumn<Bonus, ?> col = visibleColumns.get(i);
                    if (col instanceof TableColumn && ((TableColumn) col).getCellObservableValue(0) != null &&
                            ((TableColumn) col).getCellObservableValue(0).getValue() instanceof Number) {
                        BigDecimal total = tableBonus.getItems().stream()
                                .map(item -> {
                                    ObservableValue<?> cellValue = col.getCellObservableValue(item);
                                    if (cellValue != null && cellValue.getValue() instanceof Number) {
                                        return new BigDecimal(String.valueOf(cellValue.getValue()));
                                    }
                                    return BigDecimal.ZERO;
                                })
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        summaryRow.add(escapeCsv(FormatterFactory.formatNumber(total.setScale(2, RoundingMode.HALF_DOWN))));
                    } else {
                        summaryRow.add("");
                    }
                }
                writer.append(String.join(",", summaryRow)).append('\n');

                new InformationAlert(MainApp.getStage(), resourceBundle.getString("export"), resourceBundle.getString("successful")).createAlert();

            } catch (IOException e) {
                e.printStackTrace();
                new ErrorAlert(MainApp.getStage(), resourceBundle.getString("export"), resourceBundle.getString("failed")).createAlert();
            }
        }
    }


    private String escapeCsv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    private void loadData(LocalDateTime value, LocalDateTime value1, String type, String criteria, String bonusValue, MilkType milkType) {
        var task = new BonusLoadTask(value, value1, milkType.getCode());
        task.setOnSucceeded(e -> {
            try {
                List<Bonus> list = task.get();
                if (list != null) {
                    bonusList.clear();
                    bonusList.addAll(list);
                    getBonusValue(type, criteria, bonusValue);
                }
            } catch (Exception exception) {
                exception.printStackTrace();

            }
        });
        new Thread(task).start();
    }

    private void getBonusValue(String type, String criteria, String bonusValue) {
        BigDecimal bonus;
        BigDecimal debanture = new BigDecimal(txtDebanture.getInputText());
        BigDecimal societyKapaat = new BigDecimal(txtSocietyKapaat.getInputText());
        BigDecimal totalAmt = BigDecimal.ZERO;
        for (Bonus b1 : bonusList) {
            if (b1.getMilkAmount() != null) {
                totalAmt = totalAmt.add(b1.getMilkAmount());
            }
        }

        if (totalAmt.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        System.out.println("shareRateRepository = " + shareRateRepository);
        System.out.println("dpToDate = " + dpToDate);
        System.out.println("dpToDate value = " + (dpToDate == null ? "NULL" : dpToDate.getValue()));

        ShareRate shareRate = shareRateRepository.findTop1ByWefDateLessThanEqualOrderByWefDate(dpToDate.getValue());
        BigDecimal rate = (shareRate == null || shareRate.getShareAmount() == null || shareRate.getShareAmount().compareTo(BigDecimal.ZERO) == 0)
                ? BigDecimal.TEN
                : shareRate.getShareAmount();
           System.out.println("rate = " + rate);

        if (criteria.equalsIgnoreCase(resourceBundle.getString("percentage"))) {
            for (Bonus b : bonusList) {
                if (b.getMilkAmount() == null) {
                    continue;
                }
                bonus = b.getMilkAmount().multiply(new BigDecimal(bonusValue)).divide(BigDecimal.valueOf(100));
                b.setBonusAmount(bonus);
                b.setType(type.equalsIgnoreCase(resourceBundle.getString("unionbonus")) ? (short) 0 : (short) 1);
                b.setSociety(MainApp.identityDto.getSociety());
                b.setUnion(MainApp.identityDto.getUnion());
                if (chkDebanture.isSelected() && debanture.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal rawDebantureAmount = b.getMilkAmount()
                            .multiply(debanture)
                            .divide(totalAmt, 6, RoundingMode.HALF_UP);
                    BigDecimal roundedAmount = rawDebantureAmount
                            .divide(rate, 0, RoundingMode.DOWN)
                            .multiply(rate);

                    b.setDebantureAmount(roundedAmount);
                } else {
                    b.setDebantureAmount(BigDecimal.ZERO);
                }
                if (societyKapaat.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal rawSocietyAmount = b.getMilkAmount()
                            .multiply(societyKapaat)
                            .divide(totalAmt, 6, RoundingMode.HALF_UP);
                    b.setSocietyAmount(rawSocietyAmount);
                } else {
                    b.setSocietyAmount(BigDecimal.ZERO);
                }
//                b.setTotalAmount(b.getBonusAmount().subtract(b.getDebantureAmount()).add(b.getSocietyAmount()));
                if (type.equalsIgnoreCase(resourceBundle.getString("societybonus"))) {
                    b.setTotalAmount(b.getBonusAmount());
                } else {
                    b.setTotalAmount(
                            b.getBonusAmount()
                                    .subtract(b.getDebantureAmount())
                                    .add(b.getSocietyAmount())
                    );
                }
            }
        } else if (criteria.equalsIgnoreCase(resourceBundle.getString("rs"))) {
            for (Bonus b : bonusList) {
                if (b.getMilkAmount() == null || b.getMilkQty() == null) {
                    continue;
                }
                bonus = b.getMilkQty().multiply(new BigDecimal(bonusValue));
                b.setBonusAmount(bonus);
                b.setType(type.equalsIgnoreCase(resourceBundle.getString("unionbonus")) ? (short) 0 : (short) 1);
                b.setSociety(MainApp.identityDto.getSociety());
                b.setUnion(MainApp.identityDto.getUnion());
                if (chkDebanture.isSelected() && debanture.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal rawDebantureAmount = b.getMilkAmount()
                            .multiply(debanture)
                            .divide(totalAmt, 6, RoundingMode.HALF_UP);

                    BigDecimal roundedAmount = rawDebantureAmount
                            .divide(rate, 0, RoundingMode.DOWN)
                            .multiply(rate);

                    b.setDebantureAmount(roundedAmount);
                } else {
                    b.setDebantureAmount(BigDecimal.ZERO);
                }
                if (societyKapaat.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal rawSocietyAmount = b.getMilkAmount()
                            .multiply(societyKapaat)
                            .divide(totalAmt, 6, RoundingMode.HALF_UP);
                    b.setSocietyAmount(rawSocietyAmount);
                } else {
                    b.setSocietyAmount(BigDecimal.ZERO);
                }
//                b.setTotalAmount(b.getBonusAmount().subtract(b.getDebantureAmount()).add(b.getSocietyAmount()));
                if (type.equalsIgnoreCase(resourceBundle.getString("societybonus"))) {
                    b.setTotalAmount(b.getBonusAmount());
                } else {
                    b.setTotalAmount(
                            b.getBonusAmount()
                                    .subtract(b.getDebantureAmount())
                                    .add(b.getSocietyAmount())
                    );
                }
            }
        } else {
            for (Bonus b : bonusList) {
                if (b.getMilkAmount() == null) {
                    continue;
                }
                bonus = new BigDecimal(bonusValue).divide(totalAmt, 25, RoundingMode.HALF_DOWN);
                b.setBonusAmount(bonus.multiply(b.getMilkAmount()));
                b.setType(type.equalsIgnoreCase(resourceBundle.getString("unionbonus")) ? (short) 0 : (short) 1);
                b.setSociety(MainApp.identityDto.getSociety());
                b.setUnion(MainApp.identityDto.getUnion());
                if (chkDebanture.isSelected() && debanture.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal rawDebantureAmount = b.getMilkAmount()
                            .multiply(debanture)
                            .divide(totalAmt, 6, RoundingMode.HALF_UP);

                    BigDecimal roundedAmount = rawDebantureAmount
                            .divide(rate, 0, RoundingMode.DOWN)
                            .multiply(rate);

                    b.setDebantureAmount(roundedAmount);
                } else {
                    b.setDebantureAmount(BigDecimal.ZERO);
                }
                if (societyKapaat.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal rawSocietyAmount = b.getMilkAmount()
                            .multiply(societyKapaat)
                            .divide(totalAmt, 6, RoundingMode.HALF_UP);
                    b.setSocietyAmount(rawSocietyAmount);
                } else {
                    b.setSocietyAmount(BigDecimal.ZERO);
                }
//                b.setTotalAmount(b.getBonusAmount().subtract(b.getDebantureAmount()).add(b.getSocietyAmount()));
                if (type.equalsIgnoreCase(resourceBundle.getString("societybonus"))) {
                    b.setTotalAmount(b.getBonusAmount());
                } else {
                    b.setTotalAmount(
                            b.getBonusAmount()
                                    .subtract(b.getDebantureAmount())
                                    .add(b.getSocietyAmount())
                    );
                }
            }
        }

        bonusList.sort(Comparator.comparing(b -> b.getMember().getCodeEx()));
        tableBonus.setItems(FXCollections.observableArrayList(bonusList));
    }


    @Override
    public void setupComboBox() {
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
//        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
//        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
//        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
    }

    @Override
    public void setupTable() {
        try {
            colSrNo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(tableBonus.getItems().indexOf(cellData.getValue()) + 1));
            colMemberCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getCodeEx()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colMobile.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getMobileNo()));
            colBonusAmt.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBonusAmount().setScale(2,
                    RoundingMode.HALF_DOWN)));
//            colUnionBonus.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBonusAmount().setScale(2,
//                    RoundingMode.HALF_DOWN)));
            colPayableUnionBonus.setCellValueFactory(data -> {
                Bonus bonus = data.getValue();
                BigDecimal payableUnionBonus = bonus.getBonusAmount().subtract(bonus.getDebantureAmount());
                return new SimpleObjectProperty<>(payableUnionBonus.setScale(2, RoundingMode.HALF_DOWN));
            });
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() == 0 ? "PENDING" : "DONE"));
            colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType() == 0 ? resourceBundle.getString("unionbonus") : resourceBundle.getString("societybonus")));
            tableBonus.setEditable(true);
            colKapaat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getxCol1()));
            colKapaat.setCellFactory(column -> new TextFieldTableCell<Bonus, String>(new DefaultStringConverter()) {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setStyle("");
                    } else {
                        Bonus bonus = getTableRow().getItem();
                        // Visually indicate if the cell is non-editable (Union Bonus is type 0)
                        if (bonus.getType() == 0) {
                            setStyle("-fx-background-color: #f4f4f4; -fx-text-fill: #a0a0a0;");
                        } else {
                            setStyle("");
                        }
                    }
                }

                @Override
                public void startEdit() {
                    Bonus bonus = getTableRow().getItem();
                    // Prevent editing if the row is "Union Bonus" (type == 0)
                    if (bonus != null && bonus.getType() == 0) {
                        return;
                    }
                    super.startEdit();
                }
            });
            colKapaat.setOnEditCommit(e -> {
                Bonus r = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase("")) {
                    r.setxCol1(e.getNewValue());
                    setControls();
                }
            });
            colRemarks.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getxCol2()));
            colRemarks.setCellFactory(TextFieldTableCell.forTableColumn());
            colRemarks.setOnEditCommit(e -> {
                Bonus r = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase("")) {
                    r.setxCol2(e.getNewValue());
                    setControls();
                }
            });

//            colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotalAmount()));
            colTotal.setCellValueFactory(data -> {
                BigDecimal total = data.getValue().getTotalAmount();
                return new SimpleObjectProperty<>(
                        total == null ? BigDecimal.ZERO : total.setScale(2, RoundingMode.HALF_DOWN)
                );
            });
            colMilkQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQty()));
            colMilkAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkAmount()));

            colAccount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAccountNo()));
            colBank.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBankName()));
            colIfsc.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIfsc()));
            colDebanture.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDebantureAmount()));
            colSocietyKapaat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSocietyAmount()));
            propBonus.bind(tableBonus.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableBonus);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setControls() {
        for (Bonus b : bonusList) {
            b.setxCol3(String.valueOf(b.getBonusAmount().subtract(
                            new BigDecimal(b.getxCol1() != null ? b.getxCol1() : "0"))
                    .setScale(2, RoundingMode.HALF_DOWN)));
        }
        tableBonus.refresh();
    }

    @Override
    public void saveData() {
        if (bonusSummary != null) {
            dto.setBonusList(bonusList);
            dto.getBonusSummary().setBonusCriteria(cboxCriteria.getValue().equalsIgnoreCase("percentage") ? (short) 0 : cboxCriteria.getValue().equalsIgnoreCase("rs") ? (short) 1 : (short) 2);
            dto.getBonusSummary().setBonusCriteriaValue(new BigDecimal(txtBonusValue.getInputText()));
            dto.getBonusSummary().setFromDate(dpFromDate.getValue());
            dto.getBonusSummary().setToDate(dpToDate.getValue());
            dto.getBonusSummary().setDebanture(chkDebanture.isSelected());
            for (Bonus b : bonusList) {
                b.selectedProperty().setValue(true);
                qty = qty.add(b.getMilkQty());
                amt = amt.add(b.getMilkAmount());
                kapaat = kapaat.add(new BigDecimal(b.getxCol1() != null ? b.getxCol1() : "0"));
                bonus = bonus.add(b.getBonusAmount());
                totalDebantureAmt = totalDebantureAmt.add(new BigDecimal(txtDebanture.getInputText()));
                totalSocietyAmount = totalSocietyAmount.add(b.getSocietyAmount());
                if (b.getxCol1() == null) {
                    b.setxCol1("0");
                    b.setxCol3("0");
                }
            }
            if (cboxCriteria.getValue().equalsIgnoreCase(resourceBundle.getString("total"))) {
                dto.getBonusSummary().setBonusCriteriaAmount(new BigDecimal(txtBonusValue.getInputText()));
            } else {
                dto.getBonusSummary().setBonusCriteriaAmount(bonus);
            }

            dto.getBonusSummary().setDebantureAmount(txtDebanture.getInputText() != null ? new BigDecimal(txtDebanture.getInputText()) : BigDecimal.ZERO);
            dto.getBonusSummary().setSocietyAmount(totalSocietyAmount);
            dto.getBonusSummary().setxCol1(String.valueOf(cboxMilkType.getValue().getCode()));
            dto.getBonusSummary().setxCol2(kapaat.toString());
            var task = new BonusEditTask(dto);
            task.setOnSucceeded(event -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"), sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("bonus.update.successful"));
                    alert.createAlert();
                    tableBonus.refresh();
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/billing/BonusSummary.fxml")));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else {
            BonusDto dto = new BonusDto();
            BonusSummary bs = new BonusSummary();
            bs.setFromDate(dpFromDate.getValue());
            bs.setToDate(dpToDate.getValue());
            bs.setBonusCriteria(cboxCriteria.getValue().equalsIgnoreCase(resourceBundle.getString("percentage")) ? (short) 0 : cboxCriteria.getValue().equalsIgnoreCase(resourceBundle.getString("rs")) ? (short) 1 : (short) 2);
            bs.setBonusCriteriaValue(new BigDecimal(txtBonusValue.getInputText()));
            bs.setSociety(MainApp.identityDto.getSociety());
            bs.setUnion(MainApp.identityDto.getUnion());
            bs.setxCol1(String.valueOf(cboxMilkType.getValue().getCode()));
            bs.setDebanture(chkDebanture.isSelected());

            for (Bonus b : bonusList) {
                b.selectedProperty().setValue(true);
                qty = qty.add(b.getMilkQty());
                amt = amt.add(b.getMilkAmount());
                bonus = bonus.add(b.getBonusAmount());
                kapaat = kapaat.add(new BigDecimal(b.getxCol1() != null ? b.getxCol1() : "0"));
                totalDebantureAmt = totalDebantureAmt.add(b.getDebantureAmount());
                totalSocietyAmount = totalSocietyAmount.add(b.getSocietyAmount());
                if (b.getxCol1() == null) {
                    b.setxCol1("0");
                    b.setxCol3("0");
                }
            }
            if (cboxCriteria.getValue().equalsIgnoreCase(resourceBundle.getString("total"))) {
                bs.setBonusCriteriaAmount(new BigDecimal(txtBonusValue.getInputText()));
            } else {
                bs.setBonusCriteriaAmount(bonus);
            }
            bs.setDebantureAmount(txtDebanture.getInputText() != null ? new BigDecimal(txtDebanture.getInputText()) : BigDecimal.ZERO);
            bs.setSocietyAmount(totalSocietyAmount);
            bs.setTotalMilkQty(qty);
            bs.setxCol2(String.valueOf(kapaat));
            bs.setTotalMilkAmount(amt);
            dto.setBonusSummary(bs);
            dto.setBonusList(bonusList);
            var task = new BonusSaveTask(dto, (short) 0);
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"), sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("bonus.insert.successful"));
                    alert.createAlert();
                    tableBonus.refresh();
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/billing/BonusSummary.fxml")));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    public void loadMilkType() {
        MilkTypeLoadTask task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    List<MilkType> list2 = new ArrayList<>();
                    MilkType m = new MilkType();
                    m.setCode(0);
                    m.setName(resourceBundle.getString("all"));
                    list2.add(m);
                    list2.addAll(list);
                    cboxMilkType.setItems(FXCollections.observableList(list2));
                    cboxMilkType.getSelectionModel().select(0);
                    if (bonusSummary != null) {
                        cboxMilkType.setValue(list2.stream().filter(e1 -> e1.getCode().toString().
                                equalsIgnoreCase(bonusSummary.getxCol1())).findAny().orElse(list2.get(0)));
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadShift() {
        ShiftLoadTask task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> shiftList = task.get();
                cboxFromShift.setItems(FXCollections.observableList(shiftList));
                cboxToShift.setItems(FXCollections.observableList(shiftList));
                cboxFromShift.getSelectionModel().select(0);
                cboxToShift.getSelectionModel().select(1);
                if (bonusSummary == null) {
                    dpFromDate.setValue(LocalDate.now());
                    dpToDate.setValue(LocalDate.now());
                }
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }
}
