package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.*;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.model.Message;
import com.eipl.amcs.master.operation.model.SchemeRateApplicability;
import com.eipl.amcs.master.operation.task.SchemeRateApplicabilityLoadTask;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.operation.administartion.task.MessagesForDateAndShiftLoadTask;
import com.eipl.amcs.operation.procurement.dto.*;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.eipl.amcs.operation.procurement.task.*;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.repository.AccountPostingRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.NumberUtil;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.eipl.amcs.MainApp.manualCollectionRangeList;
import static com.eipl.amcs.utils.CommonUtils.MY_DECIMAL32;

public class MilkCollectionAddController extends MilkCollectionBaseController implements MyInitialization, PopupCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkCollectionAddController.class);
    private final ObservableList<MilkCollection> listCollection = FXCollections.observableArrayList();
    private final ObservableList<MilkCollection> listPrevCollection = FXCollections.observableArrayList();
    private final ObservableList<CollectionSummary> listCollectionSummary = FXCollections.observableArrayList();
    private final ObjectProperty<MilkCollection> propCollection;
    private final ObjectProperty<MilkCollection> propCollectionSummary;

    //Printer
    private final ArrayList<String> masterLines = new ArrayList<>();
    List<Shift> shiftList = new ArrayList<>();
    String text;
    DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd/MM/yy");
    DateTimeFormatter dTF1 = DateTimeFormatter.ofPattern("HH:mm:ss");
    @FXML
    private StackPane root;
    @FXML
    private Label lblTitle, lblSave;
    @FXML
    private E_DatePicker dpDate;
    @FXML
    private ComboBox<Shift> cboxShift;
    @FXML
    private E_ComboBox<MilkType> cboxMilkType;
    @FXML
    private E_ComboBox<MilkQualityType> cboxMilkQuality;
    @FXML
    private E_ComboBox<String> cboxShortCut;
    @FXML
    private E_TextField txtName;
    @FXML
    private E_NumericField txtSampleNo, txtCode, txtQty, txtFat, txtSnf1, txtFat1, txtSnf2, txtFat2, txtSnf3, txtFat3, txtSnf4, txtFat4, txtSnf, txtClr, txtWater, txtRate, txtAmount;
    @FXML
    private E_Button btnSave, btnClose, btnStart, btnExport, btnDispatch, btnLocalMilkSale, btnSetting, btnShiftReport;
    @FXML
    private Label lblAvgFat, lblAvgSnf, lblAvgQty, lblShiftTime, lblStartTime, lblEndTime, lblKgFatRate, lblManual, lblLocalTime, lblEdited;
    @FXML
    private TableView<CollectionSummary> tableSummary;
    @FXML
    private TableColumn<CollectionSummary, MilkType> colSummaryMilkType;
    @FXML
    private TableColumn<CollectionSummary, Number> colSummaryMemberNos, colAvgFat, colAvgSnf, colSummaryQty, colSummaryAmount;
    @FXML
    private TableView<MilkCollection> tableCollection, tablePrevCollection;
    @FXML
    private TableColumn<MilkCollection, MilkType> colCollMilkType;
    @FXML
    private TableColumn<MilkCollection, MilkType> colCollMilkType1;
    @FXML
    private TableColumn<MilkCollection, Number> colCollSampleNo, colCollMemberCode, colCollQty, colCollFat, colCollSnf, colCollRate, colCollAmount;
    @FXML
    private TableColumn<MilkCollection, Number> colCollQty1, colCollFat1, colCollSnf1, colCollRate1, colCollAmount1;
    @FXML
    private TableColumn<MilkCollection, Object> colCollMemberCode1;
    @FXML
    private GridPane gridCollection, gridMa;
    @FXML
    private HBox hboxDataShift;
    @FXML
    private Label lblShortcut;
    @FXML
    private ToggleGroup tagMa;
    @FXML
    private RadioButton rbtMa1, rbtMa2, rbtMa3, rbtMa4;
    private MemberSocietyInfoDto memberSocietyInfoDto;
    private PrinterHelper printerHelper;
    private String slipLanguage = "English";
    private StringBinding bindingFat1, bindingFat2, bindingFat3, bindingFat4;
    private StringBinding bindingSnf1, bindingSnf2, bindingSnf3, bindingSnf4;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private boolean printOnOff = true;
    private File slipFile = null;
    Boolean doubleDock = false;

    private SchemeRateApplicability schemeRateApplicability;

    private List<Message> messageList = new ArrayList<>();

    private final ChangeListener<String> qtyRateChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            calculateAmount(txtRate.getText(), txtQty.getText());
            if (MainApp.displaySerial != null)
                MainApp.displaySerial.displayQuantity(getStringForDisplay("QTY"));
        }
        loadSchemeRate();
    };
    private final ChangeListener<String> qualityParamChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            fetchRate(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue());
            calculateClr(txtFat.getText(), txtSnf.getText());
        }

        if (MainApp.displaySerial != null) {
            MainApp.displaySerial.displayQuantity(getStringForDisplay("QLTY"));
        }
    };

    public MilkCollectionAddController() {
        propCollection = new SimpleObjectProperty<>();
        propCollectionSummary = new SimpleObjectProperty<>();

    }

    private void exportExcel(List<MilkCollection> list) {
        try {
            boolean exported = true;
            boolean cancelled = false;
            CollectionSummary summary = listCollectionSummary.stream().filter(p -> p.getMilkType().getName().equalsIgnoreCase("ALL")).findFirst().orElse(null);

            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle(resourceBundle.getString("milkcollection"));
                fileDialog.setInitialFileName("Milk_Collection_Report-" + dpDate.getValue() + "-" + cboxShift.getValue() + ".xls");
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Excel File (2003-2007)", "*.xls")
                );
                File file = fileDialog.showSaveDialog(MainApp.stage);
                if (file != null) {
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Milk Collection");

                    List<String> headers = Arrays.asList(
                            "Sample No", "Member Code", "Milk Type", "Qty",
                            "Fat", "Snf", "Rate", "Amount"
                    );

                    HSSFFont boldFont = workbook.createFont();
                    boldFont.setBold(true);

                    Consumer<HSSFCellStyle> applyBorders = style -> {
                        style.setBorderTop(BorderStyle.THIN);
                        style.setBorderBottom(BorderStyle.THIN);
                        style.setBorderLeft(BorderStyle.THIN);
                        style.setBorderRight(BorderStyle.THIN);
                    };

                    HSSFCellStyle boldStyle = workbook.createCellStyle();
                    boldStyle.setFont(boldFont);

                    HSSFCellStyle centerStyle = workbook.createCellStyle();
                    centerStyle.setAlignment(HorizontalAlignment.CENTER);
                    centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                    HSSFCellStyle leftStyle = workbook.createCellStyle();
                    leftStyle.setAlignment(HorizontalAlignment.LEFT);
                    leftStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                    HSSFCellStyle boldCenterStyle = workbook.createCellStyle();
                    boldCenterStyle.setFont(boldFont);
                    boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);
                    boldCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                    HSSFCellStyle borderStyle = workbook.createCellStyle();
                    applyBorders.accept(borderStyle);

                    HSSFCellStyle boldBorderStyle = workbook.createCellStyle();
                    boldBorderStyle.setFont(boldFont);
                    applyBorders.accept(boldBorderStyle);

                    HSSFRow row = sheet.createRow(0);
                    HSSFCell cell = row.createCell(0);

                    Map<Integer, HSSFRow> rowMap = IntStream.rangeClosed(1, 5)
                            .boxed()
                            .collect(Collectors.toMap(
                                    rowNum -> rowNum,
                                    rowNum -> {
                                        HSSFRow toprow = sheet.createRow(rowNum);
                                        IntStream.rangeClosed(0, 3).forEach(col -> toprow.createCell(col));
                                        return toprow;
                                    }
                            ));

                    rowMap.get(1).getCell(0).setCellValue("Code:");
                    rowMap.get(1).getCell(1).setCellValue(MainApp.identityDto.getSociety().getCode());

                    rowMap.get(2).getCell(0).setCellValue("Name:");
                    rowMap.get(2).getCell(1).setCellValue(MainApp.identityDto.getSociety().getName());

                    rowMap.get(3).getCell(0).setCellValue("Date:");
                    rowMap.get(3).getCell(1).setCellValue(String.valueOf(dpDate.getValue()));

                    rowMap.get(4).getCell(0).setCellValue("Shift:");
                    rowMap.get(4).getCell(1).setCellValue(String.valueOf(cboxShift.getValue()));

                    rowMap.get(5).getCell(0).setCellValue("Total Sample:");
                    rowMap.get(5).getCell(1).setCellValue(String.valueOf(summary.getMemberCount()));

                    for (int rowCount = 0; rowCount < 6; rowCount++) {
                        sheet.getRow(rowCount).forEach(cell1 -> {
                            cell1.setCellStyle(cell1.getColumnIndex() == 0 ? boldBorderStyle : borderStyle);
                        });
                        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 3));
                    }

                    HSSFRow headerRow = sheet.createRow(7);
                    for (int i = 0; i < headers.size(); i++) {
                        cell = headerRow.createCell(i);
                        cell.setCellValue(headers.get(i));
                        cell.setCellStyle(boldCenterStyle);
                    }

                    for (int rowIndex = 0; rowIndex < list.size(); rowIndex++) {
                        MilkCollection item = list.get(rowIndex);
                        row = sheet.createRow(rowIndex + 8);

                        row.createCell(0).setCellValue(item.getSampleNo());
                        row.getCell(0).setCellStyle(centerStyle);
                        row.createCell(1).setCellValue(item.getMember().getCodeEx());
                        row.createCell(2).setCellValue(item.getMilkType().getName());
                        row.createCell(3).setCellValue(item.getQty().doubleValue());
                        row.getCell(3).setCellStyle(leftStyle);
                        row.createCell(4).setCellValue(item.getFat().doubleValue());
                        row.getCell(4).setCellStyle(leftStyle);
                        row.createCell(5).setCellValue(item.getSnf().doubleValue());
                        row.getCell(5).setCellStyle(leftStyle);
                        row.createCell(6).setCellValue(item.getRtpl().doubleValue());
                        row.getCell(6).setCellStyle(leftStyle);
                        row.createCell(7).setCellValue(item.getAmount().doubleValue());
                        row.getCell(7).setCellStyle(leftStyle);
                    }

                    int summaryRowNumber = list.size() + 8;
                    row = sheet.createRow(summaryRowNumber);
                    row.createCell(0).setCellValue("Total");
                    sheet.addMergedRegion(new CellRangeAddress(summaryRowNumber, summaryRowNumber, 0, 2));
                    row.createCell(3).setCellValue(String.valueOf(summary.getQty()));
                    row.createCell(4).setCellValue(String.valueOf(summary.getAvgFat()));
                    row.createCell(5).setCellValue(String.valueOf(summary.getAvgSnf()));
                    row.createCell(6).setCellValue("-");
                    row.createCell(7).setCellValue(String.valueOf(summary.getAmount()));
                    row.forEach(cells -> {
                        cells.setCellStyle(leftStyle);
                        cells.setCellStyle(boldStyle);
                        if (cells.getColumnIndex() == 0)
                            cells.setCellStyle(boldCenterStyle);
                    });

                    FileOutputStream outputStream = new FileOutputStream(file);
                    workbook.write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                    workbook.close();
                } else {
                    cancelled = true;
                    exported = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                exported = false;
            }

            MyAlert alert;
            if (cancelled) {
                alert = new InformationAlert(MainApp.stage,
                        resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("export.cancelled"));
            } else if (exported) {
                alert = new InformationAlert(MainApp.stage,
                        resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("successful"));
            } else {
                alert = new ErrorAlert(MainApp.stage,
                        resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("error.occurred"));
            }
            alert.createAlert();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getStringForDisplay(String type) {
        if (MainApp.displaySerial != null) {
            switch (Integer.parseInt(MainApp.displaySerial.getDispDevice().getxCol2())) {
                case 1: // EVEREST
                    switch (type) {
                        case "MCODE":
                            return "(A" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + ")";
                        case "QTY":
                            return "(S" + MainApp.displaySerial.getWgtDecimalFormat().format(Double.parseDouble(txtQty.getText() != null ? txtQty.getText().trim() : "0")) + ")";
                        case "QLTY":
                            return "(F" + MainApp.displaySerial.getQtyDecimalFormat().format(Double.parseDouble(txtFat.getText() != null && txtFat.getText().trim().length() > 0 ? txtFat.getText().trim() : "00.0")) + MainApp.displaySerial.getQtyDecimalFormat().format(Double.parseDouble(txtSnf.getText() != null && txtSnf.getText().trim().length() > 0 ? txtSnf.getText().trim() : "00.0")) + MainApp.displaySerial.getQtyDecimalFormat().format(Double.parseDouble(txtWater.getText() != null && txtWater.getText().trim().length() > 0 ? txtWater.getText().trim() : "00.0")) + ")";
                        case "RTPL":
                            return "(J" + MainApp.displaySerial.getRateDecimalFormat().format(Double.parseDouble(txtRate.getText() != null ? txtRate.getText().trim() : "0")).replace(".", "") + ")";
                        case "AMT":
                            return "(G" + MainApp.displaySerial.getWgtDecimalFormat().format(Double.parseDouble(txtAmount.getText() != null ? txtAmount.getText().trim() : "0")) + ")";
                        case "ANIMAL":
                            return "(D" + (cboxMilkType.getSelectionModel().getSelectedItem().getName().toUpperCase().startsWith("C") ? "c" : cboxMilkType.getSelectionModel().getSelectedItem().getName().toUpperCase().startsWith("B") ? "b" : "m") + ")";

                        case "RESET":
                            return "H";
                        default:
                            return null;
                    }
                case 2: // REIL
                    String a = "";
                    int verient = 1;
                    try {
                        verient = Integer.parseInt(MainApp.displaySerial.getDispDevice().getDeviceName().split("-")[1].trim());
                    } catch (Exception e) {
                    }
                    switch (verient) {
                        case 1:
                            a = "A" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + String.format("%03d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtFat.getText().trim())).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtQty.getText().trim())).replace(".", "") : "0")) + String.format("%05d", Integer.parseInt(txtAmount.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtAmount.getText().trim())).replace(".", "") : "0")) + "B";
                            break;
                        case 2:
                            a = "A" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + String.format("%04d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtQty.getText().trim())).replace(".", "") : "0")) + String.format("%03d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtFat.getText().trim())).replace(".", "") : "0")) + String.format("%05d", Integer.parseInt(txtAmount.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtAmount.getText().trim())).replace(".", "") : "0")) + "B";
                            break;
                        case 3:
                            String amt = String.format("%06d", Integer.parseInt(txtAmount.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtAmount.getText().trim())).replace(".", "") : "0"));
                            amt = amt.substring(1) + amt.charAt(0);
                            a = "$A" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + String.format("%04d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtQty.getText().trim())).replace(".", "") : "0")) + String.format("%03d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtFat.getText().trim())).replace(".", "") : "0")) + amt + String.format("%04d", Integer.parseInt(txtSnf.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtSnf.getText().trim())).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtRate.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtRate.getText().trim())).replace(".", "") : "0")) + "B";
                            break;
                        case 4:
                            String amt1 = String.format("%06d", Integer.parseInt(txtAmount.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtAmount.getText().trim())).replace(".", "") : "0"));
                            amt1 = amt1.substring(1) + amt1.charAt(0);
                            a = "$A" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + String.format("%05d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtQty.getText().trim())).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtFat.getText().trim())).replace(".", "") : "0")) + amt1 + String.format("%04d", Integer.parseInt(txtSnf.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtSnf.getText().trim())).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtRate.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtRate.getText().trim())).replace(".", "") : "0")) + "B";
                            break;
                        case 5:
                            String qty5 = String.format("%04d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtQty.getText().trim())).replace(".", "") : "0"));
                            qty5 = qty5.substring(1) + qty5.charAt(0);
                            String amt5 = String.format("%06d", Integer.parseInt(txtAmount.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtAmount.getText().trim())).replace(".", "") : "0"));
                            amt5 = amt5.substring(1) + amt5.charAt(0);

                            a = "$A" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + qty5 + String.format("%03d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtFat.getText().trim())).replace(".", "") : "0")) + amt5 + String.format("%04d", Integer.parseInt(txtSnf.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtSnf.getText().trim())).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtRate.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtRate.getText().trim())).replace(".", "") : "0")) + "B";
                            break;
                        default:
                            break;
                    }
                    return a;
                case 3:
                    String c = "";
                    try {
                        c = "$A" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + String.format("%04d", Integer.parseInt(txtQty.getText() != null ? txtQty.getText().trim().replace(".", "") : "0")) + String.format("%03d", Integer.parseInt(txtFat.getText() != null ? txtFat.getText().trim().replace(".", "") : "0")) + String.format("%06d", Integer.parseInt(txtAmount.getText() != null ? txtAmount.getText().trim().replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtSnf.getText() != null ? txtSnf.getText().trim().replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtRate.getText() != null ? txtRate.getText().trim().replace(".", "") : "0"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return c;
                case 4: // PROMPT
                    String prompStr = "";
                    int verientPrompt = 1;
                    try {
                        verientPrompt = Integer.parseInt(MainApp.displaySerial.getDispDevice().getDeviceName().split("-")[1].trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    switch (verientPrompt) {
                        case 1:
                            try {
                                prompStr = "D" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + String.format("%05d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtQty.getText() != null && !txtQty.getText().equalsIgnoreCase("") ? txtQty.getText().trim() : "0")).replace(".", "") : "0")) + String.format("%03d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtFat.getText() != null && !txtFat.getText().equalsIgnoreCase("") ? txtFat.getText().trim() : "0")).replace(".", "") : "0")) + (cboxMilkType.getValue().getName().charAt(0) + "").toUpperCase() + String.format("%03d", Integer.parseInt(txtSnf.getText() != null ? MainApp.DECIMAL_FORMAT_1_DIGIT.format(Double.parseDouble(txtSnf.getText() != null && !txtSnf.getText().equalsIgnoreCase("") ? txtSnf.getText().trim() : "0")).replace(".", "") : "0")) + String.format("%05d", Integer.parseInt(txtRate.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtRate.getText() != null && !txtRate.getText().equalsIgnoreCase("") ? txtRate.getText().trim() : "0")).replace(".", "") : "0")) + String.format("%06d", Integer.parseInt(txtAmount.getText() != null && txtAmount.getText().equalsIgnoreCase("") ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtAmount.getText() != null && !txtAmount.getText().equalsIgnoreCase("") ? txtAmount.getText().trim() : "0")).replace(".", "") : "0")) + "\r\n";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                prompStr = "D" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + String.format("%05d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtQty.getText() != null && !txtQty.getText().equalsIgnoreCase("") ? txtQty.getText().trim() : "0")).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtFat.getText() != null && !txtFat.getText().equalsIgnoreCase("") ? txtFat.getText().trim() : "0")).replace(".", "") : "0")) + (cboxMilkType.getValue().getName().charAt(0) + "").toUpperCase() + String.format("%04d", Integer.parseInt(txtSnf.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtSnf.getText() != null && !txtSnf.getText().equalsIgnoreCase("") ? txtSnf.getText().trim() : "0")).replace(".", "") : "0")) + String.format("%05d", Integer.parseInt(txtRate.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtRate.getText() != null && !txtRate.getText().equalsIgnoreCase("") ? txtRate.getText().trim() : "0")).replace(".", "") : "0")) + String.format("%06d", Integer.parseInt(txtAmount.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtAmount.getText() != null && !txtAmount.getText().equalsIgnoreCase("") ? txtAmount.getText().trim() : "0")).replace(".", "") : "0")) + "\r\n";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                    return prompStr;
                case 7:
                    String akashganga = "";
                    akashganga = "$%" + String.format("%04d", Integer.parseInt(txtCode.getText().trim())) + "   " + (cboxMilkType.getValue().getName().charAt(0) + "").toUpperCase() + String.format("%05d", Integer.parseInt(txtQty.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtQty.getText().trim())).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtFat.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtFat.getText().trim())).replace(".", "") : "0")) + String.format("%04d", Integer.parseInt(txtSnf.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtSnf.getText().trim())).replace(".", "") : "0")) + String.format("%05d", Integer.parseInt(txtRate.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtRate.getText().trim())).replace(".", "") : "0")) + String.format("%06d", Integer.parseInt(txtAmount.getText() != null ? MainApp.DECIMAL_FORMAT_2_DIGIT.format(Double.parseDouble(txtAmount.getText().trim())).replace(".", "") : "0")) + "\\r";
                    return akashganga;
                default:
                    break;
            }
        }
        return null;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        lblTitle.setText(resourceBundle.getString("milkcollection") + " (" + (MainApp.identityDto.getSociety().getName()) + " )");
        collectionType = AppConstant.CollectionType.MEMBER_COLL;
        dpDate.setValue(LocalDate.now());
        FocusUtils.requestFocus(btnStart);

        cboxShortCut.getItems().addAll("ShortCut List", "M → MilkType", "Space → Weight Lock", "ESC → Exit", "F3 → Add Member", "F4 → Delete", "F5 / F6 → Refresh", "F7 / S → Setting", "F8 → Local Milk Sale", "F9 → Print", "F10 / P → Reprint", "F11 / T → Tare", "C → Farmer Code", "D → Milk Dispatch", "E → Edit");
        cboxShortCut.getSelectionModel().select(0);

        doubleDock = !MainApp.identityDto.getDock().getDockNo().substring(MainApp.identityDto.getSociety().getCode().length()).equals("01");

        bindingFat1 = Bindings.createStringBinding(() -> txtFat1.getText(), txtFat1.textProperty());
        bindingFat1.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            }
        });
        bindingFat2 = Bindings.createStringBinding(() -> txtFat2.getText(), txtFat2.textProperty());
        bindingFat3 = Bindings.createStringBinding(() -> txtFat3.getText(), txtFat3.textProperty());
        bindingFat4 = Bindings.createStringBinding(() -> txtFat4.getText(), txtFat4.textProperty());

        bindingSnf1 = Bindings.createStringBinding(() -> txtSnf1.getText(), txtSnf1.textProperty());
        bindingSnf2 = Bindings.createStringBinding(() -> txtSnf2.getText(), txtSnf2.textProperty());
        bindingSnf3 = Bindings.createStringBinding(() -> txtSnf3.getText(), txtSnf3.textProperty());
        bindingSnf4 = Bindings.createStringBinding(() -> txtSnf4.getText(), txtSnf4.textProperty());

        fatStringProp.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            }
        });


        loadData();

        setupComboBox();
        setupTable();
        gridCollection.setDisable(true);
        gridMa.setDisable(true);
        tableCollection.setItems(listCollection);
        tableSummary.setItems(listCollectionSummary);
        btnStart.setOnAction(e -> {
            loadMessages();
            LocalDateTime collectionDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue());
            MilkDispatchRepository milkDispatchRepository = EmcsAppContext.getContext().getBean(MilkDispatchRepository.class);
            AccountPostingRepository accountPostingRepository = EmcsAppContext.getContext().getBean(AccountPostingRepository.class);
            if (milkDispatchRepository.existsByFromDateAndFromShift(collectionDateTime, cboxShift.getValue())) {
                MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("milkcollection"), resourceBundle.getString("dispatch.already.done"));
                alert.createAlert();
            } else if (accountPostingRepository.findValidRange(dpDate.getValue(), cboxShift.getSelectionModel().getSelectedItem().getCode(), (short) 2, AppConstant.EventCode.MILK_COLLECTION) > 0) {
                MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("milkcollection"), resourceBundle.getString("account.posting.already.done"));
                alert.createAlert();
            } else {
                loadRequestData();
                btnExport.setDisable(false);
            }
        });
        btnExport.setOnAction(event -> {
            if (listCollection == null || listCollection.isEmpty()) {
                MyAlert alert = new InformationAlert(MainApp.stage,
                        resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("no.data"));
                alert.createAlert();
                return;
            }
            exportExcel(listCollection);
        });
        btnClose.setOnAction(e -> {
            if (manualCollectionRangeList != null && !manualCollectionRangeList.isEmpty() && manualCollectionRangeList.stream().filter(e2 -> e2.getxCol1().equalsIgnoreCase("0") && e2.getFromShift().getCode() == cboxShift.getValue().getCode()).anyMatch(e3 -> e3.getStatus() == 2)) {
                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("finalmilkcollection"));
                Optional<ButtonType> resp = alert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    AllowDcsManualCollectionRange collectionRange = manualCollectionRangeList.stream().filter(e1 -> e1.getxCol1().equalsIgnoreCase("0") &&
                            Objects.equals(e1.getFromShift().getCode(), cboxShift.getValue().getCode()) && e1.getStatus() == 2).findFirst().get();
                    collectionRange.setStatus(4);
                    var task = new AllowDcsManualCollectionRangeSaveTask(collectionRange, (short) 1);
                    task.setOnSucceeded(ee -> {
                        closeDevicesIfAny();
                        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkCollection.fxml")));
                    });
                    new Thread(task).start();
                }
            } else {
                closeDevicesIfAny();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkCollection.fxml")));
            }
        });
        btnSave.setOnAction(e -> validateAndSave());
        btnDispatch.setOnAction(e -> {
            if (MainApp.contentPane.getLeft() == null) {
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
            }
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));
        });
        btnLocalMilkSale.setOnAction(e -> {
            if (MainApp.contentPane.getLeft() == null) {
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
            }
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/LocalMilkSale.fxml")));
        });
        btnSetting.setOnAction(e -> {
            if (MainApp.contentPane.getLeft() == null) {
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
            }
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CollectionSetting", null, this);
        });
        btnShiftReport.setOnAction(e -> {
            if (MainApp.contentPane.getLeft() == null) {
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
            }
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/report/ShiftReportCode.fxml")));
        });
        txtFat.textProperty().addListener(qualityParamChangeListener);
        txtSnf.textProperty().addListener(qualityParamChangeListener);
        txtQty.textProperty().addListener(qtyRateChangeListener);
        txtRate.textProperty().addListener(qtyRateChangeListener);
        txtCode.focusedProperty().addListener((ob, oldVal, newVal) -> {
//            if (!newVal) fetchMemberSocietyDetails();
            if (!newVal) {
                boolean milkTypeSetBySuffix = false;
                if (MainApp.getProperty("code.milktype.parsing", "0").equalsIgnoreCase("1")) {
                    String code = txtCode.getText();
//                    if (code != null && !code.isEmpty()) {
                    if (code != null && code.length() > 1) {
                        String lastDigit = code.substring(code.length() - 1);
                        if (CommonUtils.isNumeric(lastDigit)) {
                            int typeCode = Integer.parseInt(lastDigit);
                            if (typeCode >= 1 && typeCode <= 4) {
                                String memberCode = code.substring(0, code.length() - 1);
                                txtCode.setText(memberCode);
                                cboxMilkType.getItems().stream()
                                        .filter(mt -> mt.getCode() == typeCode)
                                        .findFirst()
                                        .ifPresent(mt -> cboxMilkType.getSelectionModel().select(mt));
                                milkTypeSetBySuffix = true;
                            }
                        }
                    }
                }
                fetchMemberSocietyDetails(milkTypeSetBySuffix);
            }
        });
        txtRate.focusedProperty().addListener((ob, oldVal, newVal) -> {
            if (!newVal) {
                MainApp.displaySerial.displayQuantity(getStringForDisplay("RTPL"));

            }
        });
        txtAmount.focusedProperty().addListener((ob, oldVal, newVal) -> {
            if (!newVal) {
                MainApp.displaySerial.displayQuantity(getStringForDisplay("AMT"));
            }
        });
        cboxMilkType.setOnAction(e -> {
            if (txtFat.getText() != null && txtSnf.getText() != null) {
                String status = String.valueOf((MainApp.getProperty("avg.param.capture", "0")));
                if (status.equals("1")) {
                    setParams();
                }
                calculateAvgAndSet();
                fetchRate(txtFat.getText(), txtSnf.getText(), cboxMilkType.getValue(), cboxMilkQuality.getValue());
                calculateClr(txtFat.getText(), txtSnf.getText());

                if (MainApp.displaySerial != null)
                    MainApp.displaySerial.displayQuantity(getStringForDisplay("ANIMAL"));
            }
            setupBindingForAutoResult();
            if (memberRateBasedList != null && !memberRateBasedList.isEmpty()) {
                var matchedData = memberRateBasedList.stream()
                        .filter(data -> data.getMilkType().getCode().equals(cboxMilkType.getValue().getCode()))
                        .findFirst()
                        .orElse(memberRateBasedList.get(0));
                BigDecimal rate = matchedData.getKgRate();
                lblKgFatRate.setText(String.valueOf(rate));
            }
        });

        root.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case F7:
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CollectionSetting", null, this);
                    break;
                case F3:
//                    if (MainApp.getProperty("hrs", "72") == null || !collectionDate.isBefore(LocalDateTime.now().minusHours(Long.parseLong(MainApp.getProperty("hrs", "72"))))) {
//                        CollectionEditDelete obj = new CollectionEditDelete(collectionDate != null ? collectionDate : null, "UPDATE");
//                        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkCollectionEditDelete", obj, this);
//                        reloadData(true);
//                    }
//                    break;
                    openMemberAdd();
                    break;
                case F8:
                    openLocalMilkSale();
                    break;
                case F4:
                    if (MainApp.getProperty("hrs", "72") == null || !collectionDate.isBefore(LocalDateTime.now().minusHours(Long.parseLong(MainApp.getProperty("hrs", "72"))))) {
                        CollectionEditDelete obj1 = new CollectionEditDelete(collectionDate != null ? collectionDate : null, "DELETE");
                        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkCollectionEditDelete", obj1, this);
                        reloadData(true);
                    }
                    break;
                case F11:
                case T:
                    tareWs();
                    break;
                case D:
                    openMilkDispatch();
                    break;
                case S:
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CollectionSetting", null, this);
                    break;
                case F10:
                case P:
                    if ((propCollection.get() != null)) {
                        fetchMemberSocietyDetailsForReprint();

                    } else {
                        RePrintForSummary();
                    }
                    break;
                case F9:
                    printToggle();
                    break;
                case F6:
                    refreshDevice();
                    break;
                case F5:
//                    if (!getQty().isEmpty()) {
//                        weightLock = new BigDecimal(getQty());
//                        tareWs();
//                    }
                    refreshDevice();
                    break;
                case F1:
                case C:
                    FocusUtils.requestFocus(txtCode);
                    break;
                case E:
                    if (MainApp.getProperty("hrs", "72") == null || !collectionDate.isBefore(LocalDateTime.now().minusHours(Long.parseLong(MainApp.getProperty("hrs", "72"))))) {
                        CollectionEditDelete obj = new CollectionEditDelete(collectionDate != null ? collectionDate : null, "UPDATE");
                        MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkCollectionEditDelete", obj, this);
                        reloadData(true);
                    }
                case F:
                    FocusUtils.requestFocus(txtFat);
                    break;
                case M:
                    int totalItems = cboxMilkType.getItems().size();
                    if (totalItems > 0) {
                        int currentIndex = cboxMilkType.getSelectionModel().getSelectedIndex();
                        int nextIndex = (currentIndex + 1) % totalItems;
                        cboxMilkType.getSelectionModel().select(nextIndex);
                    }
                    break;
                case Q:
                case L:
                    FocusUtils.requestFocus(txtQty);
                    break;
                case W:
                    if (!getQty().isEmpty()) {
                        weightLock = new BigDecimal(getQty());
                        tareWs();
                    }
                case ESCAPE:
                    btnClose.fire();
                    break;
            }
        });

        slipLanguage = MainApp.getProperty("slip.language", "English");

        rbtMa1.setOnAction(e -> setCurrentMaSelection("MA1"));
        rbtMa2.setOnAction(e -> setCurrentMaSelection("MA2"));
        rbtMa3.setOnAction(e -> setCurrentMaSelection("MA3"));
        rbtMa4.setOnAction(e -> setCurrentMaSelection("MA4"));
    }


    private void openMemberAdd() {
        if (MainApp.contentPane.getLeft() == null) {
            MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
        }
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/MemberAddEdit.fxml")));
    }

    private void openLocalMilkSale() {
        if (MainApp.contentPane.getLeft() == null) {
            MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
        }
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/LocalMilkSale.fxml")));
    }

    private void openMilkDispatch() {
        if (MainApp.contentPane.getLeft() == null) {
            MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
        }
        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatch.fxml")));
    }

    private void calculateAvgAndSet() {
        resetAvgLabels();
        if (memberSocietyInfoDto.getMember() == null || cboxMilkType.getValue() == null ||
                cboxShift.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        String memberCode = memberSocietyInfoDto.getMember().getCode();
        //memberSocietyInfoDto.getCurrentPaymentCycleData();
        var task = new MilkCollectionLoadTask(memberCode);

        task.setOnSucceeded(e -> {
            List<MilkCollection> listCollection = task.getValue();
            if (listCollection == null || listCollection.isEmpty()) return;

            Integer currentMilkCode = cboxMilkType.getValue().getCode();
            String currentShiftName = cboxShift.getSelectionModel().getSelectedItem().getName();
            int lastNShifts = Integer.parseInt(MainApp.getProperty("variation.no.param", "5"));

            List<MilkCollection> filteredList = listCollection.stream()
                    .filter(p -> p.getMilkType().getCode().equals(currentMilkCode))
                    .filter(p -> p.getShift().getName().equalsIgnoreCase(currentShiftName))
                    // .filter(p -> p.getSocietyPaymentCycle().getSociety().equals(cur))
                    .sorted((c1, c2) -> c2.getCollectionDate().compareTo(c1.getCollectionDate()))
                    .collect(Collectors.toList());

            if (filteredList.isEmpty()) return;

            List<String> uniqueShiftKeys = filteredList.stream()
                    .map(coll -> coll.getCollectionDate().toLocalDate().toString() + coll.getShift().getName())
                    .distinct()
                    .limit(lastNShifts)
                    .collect(Collectors.toList());


            List<MilkCollection> finalProcessingList = filteredList.stream()
                    .filter(coll -> uniqueShiftKeys.contains(
                            coll.getCollectionDate().toLocalDate().toString() + coll.getShift().getName()))
                    .collect(Collectors.toList());

            BigDecimal kgFat = BigDecimal.ZERO;
            BigDecimal kgSnf = BigDecimal.ZERO;
            BigDecimal totalLtr = BigDecimal.ZERO;

            for (MilkCollection coll : finalProcessingList) {
                kgFat = kgFat.add(CommonUtils.calculateKgFat(coll.getFat(), coll.getQty().toString()));
                kgSnf = kgSnf.add(CommonUtils.calculateKgFat(coll.getSnf(), coll.getQty().toString()));
                totalLtr = totalLtr.add(coll.getQty());
            }

            try {
                if (totalLtr.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal avgFat = CommonUtils.scale1RoundUp(kgFat.divide(totalLtr, MY_DECIMAL32).multiply(BigDecimal.valueOf(100)));
                    BigDecimal avgSnf = CommonUtils.scale1RoundUp(kgSnf.divide(totalLtr, MY_DECIMAL32).multiply(BigDecimal.valueOf(100)));

                    lblAvgFat.setText(avgFat.toString());
                    lblAvgSnf.setText(avgSnf.toString());
                    lblAvgQty.setText(totalLtr.toString());
                }
            } catch (ArithmeticException ex) {
                resetAvgLabels();
            }
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            resetAvgLabels();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void resetAvgLabels() {
        lblAvgFat.setText("0.0");
        lblAvgSnf.setText("0.0");
        lblAvgQty.setText("0.0");
    }

    private void printToggle() {
        printOnOff = !printOnOff;
//        if (printOnOff)
//            lblShortcut.setText(resourceBundle.getString("F11SwitchCollectionModeF7SettingF3EditDelete"));
//        else lblShortcut.setText(resourceBundle.getString("F11SwitchCollectionModeF7SettingF3EditDelete1"));
    }

    private void RePrint() {
        MilkCollection mc = propCollection.get();
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;
        if (memberSocietyInfoDto.getCurrentPaymentCycleData() != null) {
            for (MilkCollection coll : memberSocietyInfoDto.getCurrentPaymentCycleData()) {
                totalQty = totalQty.add(coll.getQty());
                totalAmt = totalAmt.add(coll.getAmount());
            }
        }
        mc.setxCol1(String.valueOf(totalQty));
        mc.setxCol2(String.valueOf(totalAmt));
        if (printerHelper != null) {
            placeVariables(mc, null);
            print(masterLines);
        }
    }

    private void RePrintForSummary() {
        MilkCollection mc = propCollectionSummary.get();
        if (printerHelper != null) {
            placeVariables(mc, null);
            print(masterLines);
        }
    }

    private void setParams() {
        if (txtCode.getText().isEmpty()) return;
        String code = MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(txtCode.getText());
        var task = new MemberAvgParametersLoadTask(code, MainApp.getProperty("avg.param.capture.shift.value", "5"), cboxMilkType.getValue().getName().equalsIgnoreCase("cow") ? "1" : "2", dpDate.getValue(), cboxShift.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                Map<String, BigDecimal> avg = (Map<String, BigDecimal>) task.get();
                if (!avg.isEmpty()) {
                    txtFat.setText(String.valueOf(new BigDecimal(String.valueOf(avg.get("fat"))).setScale(1, RoundingMode.HALF_UP)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        colCollMemberCode.setCellValueFactory(data -> new SimpleIntegerProperty(CommonUtils.strToInteger(data.getValue().getMember().getCodeEx())));
        colCollSampleNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSampleNo()));
        colCollQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
        colCollFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFat()));
        colCollSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSnf()));
        colCollRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRtpl()));
        colCollAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
        colCollMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));

        colSummaryAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
        colSummaryQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
        colAvgFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgFat()));
        colAvgSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAvgSnf()));
        colSummaryMemberNos.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getMemberCount()));
        colSummaryMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));


        colCollMemberCode1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCollectionDate().getDayOfMonth() + "-" + data.getValue().getShift().getName().charAt(0)));
        colCollQty1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
        colCollFat1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFat()));
        colCollSnf1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSnf()));
        colCollRate1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRtpl()));
        colCollAmount1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
        colCollMilkType1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
        tableSummary.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableCollection.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablePrevCollection.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        propCollection.bind(tableCollection.getSelectionModel().selectedItemProperty());
        propCollectionSummary.bind(tablePrevCollection.getSelectionModel().selectedItemProperty());
    }

    private boolean isQtyVariationValid() {
        String variationProp = MainApp.getProperty("variation.qty", "20");
        double variationLimit = Double.parseDouble(variationProp);
        if (variationLimit > 0
                && !lblAvgQty.getText().trim().isEmpty()
                && Double.parseDouble(lblAvgQty.getText().trim()) > 0) {

            double currentQty = Double.parseDouble(txtQty.getText().trim().isEmpty() ? "0" : txtQty.getText().trim());
            double avgQty = Double.parseDouble(lblAvgQty.getText().trim());
            double variationPercentage = (currentQty * 100) / avgQty;

            return variationPercentage <= (100 + variationLimit);
        }
        return true;
    }

    private boolean isFatVariationValid() {
        double fatLimit = Double.parseDouble(MainApp.getProperty("variation.fat", "30"));
        if (fatLimit > 0 && !lblAvgFat.getText().trim().isEmpty()) {
            double avgFat = Double.parseDouble(lblAvgFat.getText().trim());
            if (avgFat > 0) {
                double currentFat = Double.parseDouble(txtFat.getText().trim().isEmpty() ? "0" : txtFat.getText().trim());
                return currentFat >= (avgFat - fatLimit) && currentFat <= (avgFat + fatLimit);
            }
        }
        return true;
    }

    private boolean isSnfVariationValid() {
        double snfLimit = Double.parseDouble(MainApp.getProperty("variation.snf", "30"));
        if (snfLimit > 0 && !lblAvgSnf.getText().trim().isEmpty()) {
            double avgSnf = Double.parseDouble(lblAvgSnf.getText().trim());
            if (avgSnf > 0) {
                double currentSnf = Double.parseDouble(txtSnf.getText().trim().isEmpty() ? "0" : txtSnf.getText().trim());
                return currentSnf >= (avgSnf - snfLimit) && currentSnf <= (avgSnf + snfLimit);
            }
        }
        return true;
    }


    private void validateAndSave() {
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), errorMsg.toString());
            alert.createAlert();
            return;
        }

        boolean isBlockEnabled = "1".equalsIgnoreCase(MainApp.getProperty("variation.qty.block", "0"));
        if (!isQtyVariationValid() && !isBlockEnabled) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(),
                    resourceBundle.getString("milkcollection"),
                    resourceBundle.getString("milkcollection.alert.qtyvariation"));

            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isEmpty() || resp.get() != ButtonType.OK) {
                return;
            }
        }
        boolean isFatBlockEnabled = "1".equalsIgnoreCase(MainApp.getProperty("variation.fat.block", "0"));
        if (!isFatVariationValid() && !isFatBlockEnabled) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(),
                    resourceBundle.getString("milkcollection"),
                    resourceBundle.getString("milkcollection.alert.fatvariation"));

            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isEmpty() || resp.get() != ButtonType.OK) {
                return;
            }
        }
        boolean isSnfBlockEnabled = "1".equalsIgnoreCase(MainApp.getProperty("variation.snf.block", "0"));
        if (!isSnfVariationValid() && !isSnfBlockEnabled) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(),
                    resourceBundle.getString("milkcollection"),
                    resourceBundle.getString("milkcollection.alert.snfvariation"));

            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isEmpty() || resp.get() != ButtonType.OK) {
                return;
            }
        }

        if (memberSocietyInfoDto.getMemberCollection().size() > 0) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("member.duplicate"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {


            } else {
                clearControls();
                FocusUtils.requestFocus(txtCode);
                return;
            }
        }

        MainApp.isIncentive = false;


        setValuesInObjectUpdate();
        // DpuIncentive Load Data comment

//        if (MainApp.timingList != null && !MainApp.timingList.isEmpty()) {
//            LocalDateTime fromDate = LocalDateTime.parse(MainApp.timingList.get(0).getFromDate() + " " + MainApp.timingList.get(0).getxCol1(), AppConstant.Formatter6);
//            LocalDateTime toDate = LocalDateTime.parse(MainApp.timingList.get(0).getToDate() + " " + MainApp.timingList.get(0).getxCol2(), AppConstant.Formatter6);
//            LocalDateTime currentDate = CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue());
//
//            boolean isBetweenInclusive = (currentDate.isEqual(fromDate) || currentDate.isAfter(fromDate)) &&
//                    (currentDate.isEqual(toDate) || currentDate.isBefore(toDate));
//
//            if (isBetweenInclusive) {
//                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("farmervoting"));
//                Optional<ButtonType> resp = alert.createYesNoConfirmationAlert();
//                if (resp.isPresent() && resp.get() == ButtonType.YES) {
//                    collection.setxCol4("Y");
//                    alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("appreciationfarmerforvoting"));
//                    resp = alert.createYesNoConfirmationAlert();
//                    if (resp.isPresent() && resp.get() == ButtonType.YES) {
//                        MainApp.isIncentive = true;
//                        MainApp.incentiveValue = MainApp.timingList.get(0).getIncRate() <= 0 ? 1 : MainApp.timingList.get(0).getIncRate();
//                        collection.setAmount(new BigDecimal(txtAmount.getText()).add((BigDecimal.valueOf(MainApp.incentiveValue)).multiply(collection.getQty())));
//                        collection.setxCol5(collection.getQty() + "#" + BigDecimal.valueOf(MainApp.incentiveValue).multiply(collection.getQty()));
//                    }
//
//                }
//            }
//        }
        saveData();
        FocusUtils.requestFocus(txtCode);
        setupTable();
    }

    @Override
    public void saveData() {
        if (!collection.getSocietyPaymentCycle().getLockBillingProcess()) {
            var task = getSaveTask();
            new Thread(task).start();
        } else {
            warningAlert("milkcollection", "paymentcyclenotfound");
        }
    }

    private boolean shouldShowConfirmation(boolean isShift0Selected, boolean isShift1Selected) {
        if (manualCollectionRangeList != null && !manualCollectionRangeList.isEmpty()) {
            List<AllowDcsManualCollectionRange> rangeList = manualCollectionRangeList;
            if (MainApp.timingList != null && !MainApp.timingList.isEmpty()) {
                if (isShift0Selected) {
                    if (isBeforeOrAfter(MainApp.timingList.get(0).getMstime(), MainApp.timingList.get(0).getMltime())) {
                        if (rangeList.isEmpty()) {
                            return true;
                        } else if (rangeList.stream().filter(e -> e.getxCol1().equalsIgnoreCase("0")).anyMatch(e1 -> e1.getStatus() == 2)) {
                            return false;
                        } else
                            return rangeList.stream().filter(e -> e.getxCol1().equalsIgnoreCase("0")).anyMatch(e1 -> e1.getStatus() != 2);
                    }
                    return false;
                } else {
                    if (isBeforeOrAfter(MainApp.timingList.get(0).getEstime(), MainApp.timingList.get(0).getEltime())) {
                        if (rangeList.isEmpty()) {
                            return true;
                        } else if (rangeList.stream().filter(e -> e.getxCol1().equalsIgnoreCase("0")).anyMatch(e1 -> e1.getStatus() == 2)) {
                            return false;
                        } else
                            return rangeList.stream().filter(e -> e.getxCol1().equalsIgnoreCase("0")).anyMatch(e1 -> e1.getStatus() != 2);
                    }
                    return false;
                }
            }
        } else if (MainApp.timingList != null && !MainApp.timingList.isEmpty()) {
            if (isShift0Selected) {
                return isBeforeOrAfter(MainApp.timingList.get(0).getMstime(), MainApp.timingList.get(0).getMltime());
            } else {
                return isBeforeOrAfter(MainApp.timingList.get(0).getEstime(), MainApp.timingList.get(0).getEltime());
            }
        }
        return false;
    }

    private boolean isBeforeOrAfter(LocalTime startTime, LocalTime endTime) {
        return LocalTime.now().isBefore(startTime) || LocalTime.now().isAfter(endTime.plusMinutes(1));
    }

    private boolean confirmAction(String titleKey, String messageKey, int type) {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, titleKey), CommonUtils.getResourceString(resourceBundle, messageKey));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            clearControls();
            AllowDcsManualCollectionRangeController controller = (AllowDcsManualCollectionRangeController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/AllowDcsManualCollectionRange.fxml"));
            controller.setType(type);
            MainApp.getContentPane().setCenter(controller.getRoot());
            return true;
        } else {
            clearControls();
            return false;
        }
    }

    private void warningAlert(String titleKey, String messageKey) {
        MyAlert alert = new WarningAlert(MainApp.stage, resourceBundle.getString(titleKey), resourceBundle.getString(messageKey));
        alert.createAlert();
        clearControls();
    }

    private MilkCollectionSaveTask getSaveTask() {
        var task = new MilkCollectionSaveTask(collection, (short) 0, doubleDock);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof MilkCollection) {
                    MilkCollection collNew = (MilkCollection) obj;
                    lblSave.setVisible(true);
                    // reset reading
                    tempQty = BigDecimal.ZERO;
                    weightLock = BigDecimal.ZERO;

                    FadeTransition ft = new FadeTransition(Duration.millis(1000), lblSave);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.setCycleCount(2);
                    ft.setAutoReverse(true);
                    ft.play();
                    clearControls();
                    setDataInTables(collNew);

                    writeCollection();

                    if (printOnOff && printerHelper != null) {
                        placeVariables(collection, null);
                        print(masterLines);
                    }
                    tareWs();
                    if (MainApp.displaySerial != null)
                        MainApp.displaySerial.displayQuantity(getStringForDisplay("RESET"));

                    changeCurrentMaSelection(collNew.getMilkType().getCode(), collNew);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        return task;
    }

    private void setCurrentMaSelection(String maTag) {
        if (!qualityAuto)
            return;
        if (analyserSeq)
            analyserMilkTypeMappingLastSavedFrom.put(0, maTag);
        else
            analyserMilkTypeMappingLastSavedFrom.put(cboxMilkType.getValue().getCode(), maTag);
        setupBindingForAutoResult();
    }

    private void changeCurrentMaSelection(int milkTypeCode, MilkCollection mc) {
        if (qualityAuto) {
            String currentFromMa;
            if (analyserSeq) {
                currentFromMa = analyserMilkTypeMappingLastSavedFrom.get(0);
                String newFromMa = null;
                String strMtToAnalyser = analyserMilkTypeMapping.get(0);
                if (strMtToAnalyser.contains(",")) {
                    String[] arr = strMtToAnalyser.split(",");
                    boolean captureNext = false;
                    for (String s : arr) {
                        if (captureNext) {
                            newFromMa = s;
                            break;
                        }
                        if (s.equalsIgnoreCase(currentFromMa))
                            captureNext = true;
                    }
                    if (captureNext && newFromMa == null)
                        newFromMa = arr[0];
                    analyserMilkTypeMappingLastSavedFrom.put(0, newFromMa);
                }
                LOGGER.debug("Current milk analyser: {}", analyserMilkTypeMappingLastSavedFrom.get(0));
            } else {
                currentFromMa = analyserMilkTypeMappingLastSavedFrom.get(milkTypeCode);
                String newFromMa = null;
                String strMtToAnalyser = analyserMilkTypeMapping.get(milkTypeCode);
                if (strMtToAnalyser != null && strMtToAnalyser.contains(",")) {
                    String[] arr = strMtToAnalyser.split(",");
                    boolean captureNext = false;
                    for (String s : arr) {
                        if (captureNext) {
                            newFromMa = s;
                            break;
                        }
                        if (s.equalsIgnoreCase(currentFromMa))
                            captureNext = true;
                    }
                    if (captureNext && newFromMa == null)
                        newFromMa = arr[0];
                    analyserMilkTypeMappingLastSavedFrom.put(milkTypeCode, newFromMa);
                }
                LOGGER.debug("Current milk analyser: {}", analyserMilkTypeMappingLastSavedFrom.get(milkTypeCode));
            }
            clearCurrentAnalyserToZero(currentFromMa == null ? "MA1" : currentFromMa, mc);
        }
    }

    private void clearCurrentAnalyserToZero(String currentFromMa, MilkCollection mc) {
        switch (currentFromMa) {
            case "MA1":
                prevFat1 = mc.getFat();
                prevSnf1 = mc.getSnf();
                setFat1("0");
                setSnf1("0");
                break;
            case "MA2":
                prevFat2 = mc.getFat();
                prevSnf2 = mc.getSnf();
                setFat2("0");
                setSnf2("0");
                break;
            case "MA3":
                prevFat3 = mc.getFat();
                prevSnf3 = mc.getSnf();
                setFat3("0");
                setSnf3("0");
                break;
            case "MA4":
                prevFat4 = mc.getFat();
                prevSnf4 = mc.getSnf();
                setFat4("0");
                setSnf4("0");
                break;
        }
    }

    private MilkCollectionSaveTask getMilkCollectionSaveTask() {
        var task = new MilkCollectionSaveTask(collection, (short) 0, doubleDock);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof MilkCollection) {
                    MilkCollection collNew = (MilkCollection) obj;
                    lblSave.setVisible(true);
                    // reset reading
                    resetReadings();
                    fadeInLabel();
                    clearControls();
                    setDataInTables(collNew);
                    writeCollection();
                    if (printOnOff && printerHelper != null) {
                        placeVariables(collection, null);
                        print(masterLines);
                    }
                    tareWs();
                    if (MainApp.displaySerial != null)
                        MainApp.displaySerial.displayQuantity(getStringForDisplay("RESET"));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        return task;
    }

    private void resetReadings() {
        weightLock = BigDecimal.ZERO;
    }

    private void fadeInLabel() {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), lblSave);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
    }

    private void writeCollection() {
        try {

            text = collection.getSampleNo() + "#" + collection.getCollectionDate() + "#" + collection.getFat() + "#" + collection.getSnf() + "#" + collection.getClr() + "#" + collection.getWater() + "#" + collection.getDensity() + "#" + collection.getLectose() + "#" + collection.getProtein() + "#" + collection.getRtpl() + "#" + collection.getQty() + "#" + collection.getAmount() + "#" + collection.isWeightAuto() + "#" + collection.isQualityAuto() + "#" + collection.isAvgParam() + "#" + collection.getQualityAt() + "#" + collection.getWeightAt() + "#" + collection.getRateCode() + "#" + collection.getUnionCode() + "#" + collection.getQtyMode() + "#" + collection.getConvertedQty() + "#" + collection.getConvertedQtyMode() + "#" + collection.getSocietyPaymentCycle().getCode() + "#" + collection.getMember().getCode() + "#" + collection.getShift().getCode() + "#" + collection.getMilkType().getCode() + "#" + collection.getMilkQualityType().getCode() + "#" + collection.getSociety().getCode() + "#" + collection.getDock().getDockNo() + "#" + collection.getxCol1() + "#" + collection.getxCol2() + "#" + collection.getxCol3();
            File directory = new File((MainApp.getProperty("backuppath", "") + "1").replace(" ", ""));
            if (!directory.exists()) directory.mkdir();
            File file = new File(directory.getAbsolutePath() + "/" + CommonUtils.getLocalDateTimeFromDateAndShiftText(dpDate.getValue(), cboxShift.getValue()) + ".txt");
            if (!file.exists()) file.createNewFile();
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataInTables(MilkCollection collNew) {
        listCollection.add(0, collNew);
        updateStartEndTime();
        CollectionSummary summary = listCollectionSummary.stream().filter(p -> p.getMilkType().getCode() == collNew.getMilkType().getCode()).findAny().orElse(null);
        if (summary == null) {
            summary = new CollectionSummary(collNew.getMilkType(), 1, collNew.getQty(), collNew.getAmount());
            summary.setAvgFat(collNew.getFat());
            summary.setAvgSnf(collNew.getSnf());
            listCollectionSummary.add(summary);
            if (listCollectionSummary.size() == 1) tableSummary.setItems(listCollectionSummary);
        } else {
            summary.setMemberCount(summary.getMemberCount() + 1);

            // calculate avg fat
            BigDecimal kgFat = collNew.getFat().multiply(collNew.getQty()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal kgFat1 = summary.getAvgFat().multiply(summary.getQty()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            BigDecimal avgFat = BigDecimal.valueOf((kgFat.doubleValue() + kgFat1.doubleValue()) / (summary.getQty().doubleValue() + collNew.getQty().doubleValue()) * 100).setScale(2, RoundingMode.HALF_UP);
            BigDecimal kgSnf = collNew.getSnf().multiply(collNew.getQty()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal kgSnf1 = summary.getAvgSnf().multiply(summary.getQty()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            BigDecimal avgSnf = BigDecimal.valueOf((kgSnf.doubleValue() + kgSnf1.doubleValue()) / (summary.getQty().doubleValue() + collNew.getQty().doubleValue()) * 100).setScale(2, RoundingMode.HALF_UP);
            summary.setAvgFat(avgFat);
            summary.setAvgSnf(avgSnf);
            summary.setQty(summary.getQty().add(collNew.getQty()).setScale(SCALE, ROUND));
            summary.setAmount(summary.getAmount().add(collNew.getAmount()).setScale(SCALE, ROUND));
        }

        //ALL
        BigDecimal amt = BigDecimal.ZERO;
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal kgFat = BigDecimal.ZERO;
        BigDecimal kgSnf = BigDecimal.ZERO;

        int memberCount = 0;
        for (CollectionSummary collectionSummary : listCollectionSummary) {
            if (collectionSummary.getMilkType().getName().equalsIgnoreCase("ALL")) continue;
            memberCount += collectionSummary.getMemberCount();
            amt = amt.add(collectionSummary.getAmount());
            qty = qty.add(collectionSummary.getQty());
            kgFat = kgFat.add(collectionSummary.getAvgFat().multiply(collectionSummary.getQty()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
            kgSnf = kgSnf.add(collectionSummary.getAvgSnf().multiply(collectionSummary.getQty()).
                            divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        summary = listCollectionSummary.stream().filter(p -> p.getMilkType().getName().equalsIgnoreCase("ALL")).findFirst().orElse(null);
        summary.setMemberCount(memberCount);
        summary.setAmount(amt);
        summary.setQty(qty);
        if (qty.doubleValue() > 0)
            summary.setAvgFat(BigDecimal.valueOf(kgFat.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
        else summary.setAvgFat(BigDecimal.ZERO);
        if (qty.doubleValue() > 0)
            summary.setAvgSnf(BigDecimal.valueOf(kgSnf.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
        else
            summary.setAvgSnf(BigDecimal.ZERO);
        tableSummary.refresh();
        tableCollection.setItems(listCollection);

    }

    @Override
    public void clearControls() {
        try {
            txtSampleNo.setText("");
            txtCode.setText("");
            txtName.setText("");
            txtQty.setText("");
            txtFat.setText("");
            if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0")))
                txtSnf.setText(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0"));
            else
                txtSnf.setText("");
            txtClr.setText("");
            txtRate.setText("");
            txtAmount.setText("");
            txtWater.setText("0");
            memberSocietyInfoDto = null;
            FocusUtils.requestFocus(txtCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fetchNextSampleNo();
    }

    private void setValuesInObjectUpdate() {
        collection = new MilkCollection();
        collection.setCollectionDate(collectionDate);
        collection.setSampleNo(CommonUtils.strToInteger(txtSampleNo.getText()));
        collection.setFat(new BigDecimal(txtFat.getText()));
        collection.setSnf(new BigDecimal(txtSnf.getText()));
        collection.setClr(new BigDecimal(txtClr.getText()));
        collection.setWater(new BigDecimal(txtWater.getText()));
        collection.setDensity(new BigDecimal("0"));
        collection.setLectose(new BigDecimal("0"));
        collection.setProtein(new BigDecimal("0"));
        collection.setQty(new BigDecimal(txtQty.getText()));
        collection.setRtpl(new BigDecimal(txtRate.getText()));
        collection.setAmount(new BigDecimal(txtAmount.getText()));
        if (weightAuto) {
            collection.setWeightAuto(weightAuto);
            collection.setWeightAt(LocalDateTime.now());
        }
        if (qualityAuto) {
            collection.setQualityAuto(qualityAuto);
            collection.setQualityAt(LocalDateTime.now());
        }
        collection.setAvgParam(false);

        collection.setRateCode(memberMilkPurchaseRate.getCode());
        collection.setWsCode(null);
        collection.setAnalyserCode(null);

        collection.setUnionCode(MainApp.identityDto.getUnion().getCode());
        collection.setQtyMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MEMBER_COLLECTION_QTY_MODE, "0")));
        collection.setConvertedQty(CommonUtils.convertQty(AppConstant.CollectionType.MEMBER_COLL, txtQty.getText()));
        collection.setConvertedQtyMode(collection.getQtyMode() == 0 ? 1 : 0);
        collection.setSocietyPaymentCycle(collectionPreReqDto.getPaymentCycle());
        collection.setMember(memberSocietyInfoDto.getMember());
        collection.setShift(cboxShift.getValue());
        collection.setMilkType(cboxMilkType.getValue());
        collection.setMilkQualityType(cboxMilkQuality.getValue());
        collection.setSociety(MainApp.identityDto.getSociety());
        collection.setDock(MainApp.identityDto.getDock());
//        collection.setUpdatedBy(MainApp.identityDto.getSociety().getCode());

        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmt = BigDecimal.ZERO;
        if (memberSocietyInfoDto.getCurrentPaymentCycleData() != null) {
            for (MilkCollection coll : memberSocietyInfoDto.getCurrentPaymentCycleData()) {
                totalQty = totalQty.add(coll.getQty());
                totalAmt = totalAmt.add(coll.getAmount());
            }
        }
        collection.setxCol1(CommonUtils.scale2RoundUp(totalQty.add(collection.getQty())).toString());
        collection.setxCol2(CommonUtils.scale2RoundUp(totalAmt.add(collection.getAmount())).toString());
        collection.setxCol3(CommonUtils.fetchCollectionSlipDateFormatted(collectionPreReqDto.getPaymentCycle().getFromDate().toLocalDate()) + "-" + CommonUtils.fetchCollectionSlipDateFormatted(dpDate.getValue()) + " (" + (memberSocietyInfoDto.getCurrentPaymentCycleData() != null ? memberSocietyInfoDto.getCurrentPaymentCycleData().size() + 1 : "1") + ")");
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        boolean isBlockEnabled = "1".equalsIgnoreCase(MainApp.getProperty("variation.qty.block", "0"));
        if (!isQtyVariationValid() && isBlockEnabled) {
            errorMsg.append(resourceBundle.getString("milkcollection.error.qtyvariation")).append("\n");
        }
        boolean isFatBlockEnabled = "1".equalsIgnoreCase(MainApp.getProperty("variation.fat.block", "0"));
        if (!isFatVariationValid() && isFatBlockEnabled) {
            errorMsg.append(resourceBundle.getString("milkcollection.error.fatvariation")).append("\n");
        }
        boolean isSnfBlockEnabled = "1".equalsIgnoreCase(MainApp.getProperty("variation.snf.block", "0"));
        if (!isSnfVariationValid() && isSnfBlockEnabled) {
            errorMsg.append(resourceBundle.getString("milkcollection.error.snfvariation")).append("\n");
        }
        if (txtSampleNo.getText() == null || txtSampleNo.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("sampleno.cannot.be.null") + "\n");
        if (txtCode.getText() == null || txtCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("membercode.cannot.be.null") + "\n");
        if (memberSocietyInfoDto == null || memberSocietyInfoDto.getMember() == null) {
            errorMsg.append(resourceBundle.getString("members.not.available") + "\n");
        }
        if (txtQty.getText() == null || txtQty.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("qty.cannot.be.null") + "\n");
        if (txtFat.getText() == null || txtFat.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("fat.cannot.be.null") + "\n");
        if (txtSnf.getText() == null || txtSnf.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("snf.cannot.be.null") + "\n");
        if (txtRate.getText() == null || txtRate.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("rate.cannot.be.null") + "\n");
        if (txtAmount.getText() == null || txtAmount.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("amount.cannot.be.null") + "\n");
        if (!CommonUtils.isNumeric(txtFat.getText()) || Double.parseDouble(txtFat.getText()) <= 0)
            errorMsg.append(resourceBundle.getString("invalid.fat") + "\n");
        if (!CommonUtils.isNumeric(txtQty.getText()) || Double.parseDouble(txtQty.getText()) <= 0)
            errorMsg.append(resourceBundle.getString("invalid.qty") + "\n");
        if (!CommonUtils.isNumeric(txtRate.getText()) || Double.parseDouble(txtRate.getText()) <= 0)
            errorMsg.append(resourceBundle.getString("invalid.rate") + "\n");
        if (!CommonUtils.isNumeric(txtAmount.getText()) || Double.parseDouble(txtAmount.getText()) <= 0)
            errorMsg.append(resourceBundle.getString("invalid.amount") + "\n");

        if ("0".equalsIgnoreCase(MainApp.getProperty(AppConstant.Props.ALLOW_MULTIENTRY_SAMEMILKTYPE, "0")) && memberSocietyInfoDto.getMemberCollection().stream().filter(p -> p.getMilkType().getCode() == cboxMilkType.getValue().getCode()).findAny().isPresent()) {
            errorMsg.append(resourceBundle.getString("samemilktype.isnotallowed") + "\n");
        }
        if ("0".equalsIgnoreCase(MainApp.getProperty(AppConstant.Props.ALLOW_MULTIENTRY_DIFFMILKTYPE, "0")) && memberSocietyInfoDto.getMemberCollection().stream().filter(p -> p.getMilkType().getCode() != cboxMilkType.getValue().getCode()).findAny().isPresent()) {
            errorMsg.append(resourceBundle.getString("diffmilktype.isalreadythere") + "\n");
        }

        return errorMsg.length() == 0;
    }

    //    private void fetchMemberSocietyDetails() {
    private void fetchMemberSocietyDetails(boolean milkTypeAlreadySet) {
        if (txtCode.getText().isEmpty()) return;
        String code = MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(txtCode.getText());
        LOGGER.info("Fetch member info for {}", code);

        var task = new MemberSocietyInfoLoadTask(code, collectionDate);
        // task.setCount(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.AVG_PARAM_PREV_SHIFTS, "5")));
        task.setCount(CommonUtils.strToInteger(MainApp.getProperty("variation.no.param", "5")));
        task.setPaymentCycleCode(collectionPreReqDto.getPaymentCycle().getCode());
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj == null) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                            resourceBundle.getString("members.not.available"));
                    alert.createAlert();
                }

                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    txtCode.setText("");
                    txtName.setText("");
                    memberSocietyInfoDto = null;
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), sb.toString());
                    alert.createAlert();
                    return;
                }

                if (obj instanceof MemberSocietyInfoDto) {
                    memberSocietyInfoDto = (MemberSocietyInfoDto) obj;

                    // prev collection data
                    listPrevCollection.clear();
                    listPrevCollection.addAll(memberSocietyInfoDto.getPrevCollectionData());
                    tablePrevCollection.setItems(listPrevCollection);

                    if (memberSocietyInfoDto.getMemberCollection().size() > 0) {
                        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("member.duplicate"));
                        Optional<ButtonType> resp = alert.createConfirmationAlert();
                        if (resp.isPresent() && resp.get() == ButtonType.OK) {
                            txtName.setText(memberSocietyInfoDto.getMember().toMemberName());
//                            cboxMilkType.getSelectionModel().select(memberSocietyInfoDto.getMember().getMilkType());
                            if (!milkTypeAlreadySet) {
                                cboxMilkType.getSelectionModel().select(memberSocietyInfoDto.getMember().getMilkType());
                            }
                            FocusUtils.requestFocus(cboxMilkType);
                        } else {
                            clearControls();
                            FocusUtils.requestFocus(txtCode);
                            return;
                        }
                    } else {
                        txtName.setText(memberSocietyInfoDto.getMember().toMemberName());
//                        cboxMilkType.getSelectionModel().select(memberSocietyInfoDto.getMember().getMilkType());
                        if (!milkTypeAlreadySet) {
                            cboxMilkType.getSelectionModel().select(memberSocietyInfoDto.getMember().getMilkType());
                        }
                    }
                    if (MainApp.displaySerial != null) {
                        MainApp.displaySerial.displayQuantity(getStringForDisplay("ANIMAL"));
                        MainApp.displaySerial.displayQuantity(getStringForDisplay("MCODE"));
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchMemberSocietyDetailsForReprint() {
        String code = MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(propCollection.get().getMember().getCodeEx());
        LOGGER.info("Fetch member info for {}", code);

        var task = new MemberSocietyInfoLoadTask(code, collectionDate);
        //  task.setCount(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.AVG_PARAM_PREV_SHIFTS, "5")));
        task.setCount(CommonUtils.strToInteger(MainApp.getProperty("variation.no.param", "5")));
        task.setPaymentCycleCode(collectionPreReqDto.getPaymentCycle().getCode());
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj == null) return;
                if (obj instanceof MemberSocietyInfoDto) {
                    memberSocietyInfoDto = (MemberSocietyInfoDto) obj;
                    RePrint();

                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadAndCheckPreRequisite() {
        readFile();
        var task = new MilkCollectionPreRequisiteTask(CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue()), cboxShift.getValue(), MainApp.identityDto.getSociety());
        task.setOnSucceeded(e -> {
            try {
                collectionPreReqDto = task.get();
                if (collectionPreReqDto != null) {
                    if (collectionPreReqDto.getPaymentCycle() == null) {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "milkcollection"), CommonUtils.getResourceString(resourceBundle, "societypaymentcycle.not.available"));
                        alert.createAlert();
                        return;
                    }
                    collectionDate = CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue());
                    hboxDataShift.getChildren().remove(btnStart);
                    dpDate.setDisable(true);
                    cboxShift.setDisable(true);
                    gridCollection.setDisable(false);
                    gridMa.setDisable(false);

                    txtClr.setDisable(true);
                    txtWater.setDisable(true);
                    txtSnf.setDisable("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0")));
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0")))
                        txtSnf.setText(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0"));
                    cboxMilkQuality.setDisable(true);
                    cboxMilkType.setDisable("0".equals(MainApp.getProperty(AppConstant.Props.ACCEPT_MILK_OTHERTHAN_DEFAULT_MILKTYPE, "1")));

                    fetchNextSampleNo();
                    fetchRateDetails();
                    FocusUtils.requestFocus(txtCode);
                    fetchCurrentShiftCollection();
                    setupAutoManual();

                    String printer = getPrinterName(collectionPreReqDto.getHardwareConfigList());
                    if (printer != null && !printer.isEmpty()) {
                        if (printerHelper == null)
                            printerHelper = new PrinterHelper(printer, MainApp.getProperty("slip.font", "Nirmala UI"), 11);
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void updateStartEndTime() {
        if (!listCollection.isEmpty()) {
            LocalDateTime minTime = listCollection.stream()
                    .map(MilkCollection::getCreatedAt)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime maxTime = listCollection.stream()
                    .map(MilkCollection::getCreatedAt)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            if (minTime != null) {
                lblStartTime.setText(minTime.format(dTF1));
            } else {
                lblStartTime.setText("");
            }
            if (maxTime != null) {
                lblEndTime.setText(maxTime.format(dTF1));
            } else {
                lblEndTime.setText("");
            }
        } else {
            lblStartTime.setText("");
            lblEndTime.setText("");
        }
        if (cboxShift.getSelectionModel().getSelectedIndex() == 0) {
            LocalTime mTime = MainApp.timingList.get(0).getMstime();
            lblLocalTime.setText(mTime.format(dTF1));
        } else {
            LocalTime eTime = MainApp.timingList.get(0).getEstime();
            lblLocalTime.setText(eTime.format(dTF1));
        }
        updateManualCountLabel();
        EditableCountLabel();
    }

    private void updateManualCountLabel() {
        long count = listCollection.stream()
                .filter(item -> !item.isQualityAuto() && !item.isWeightAuto())
                .count();
        lblManual.setText(String.valueOf(count));
    }

    private void EditableCountLabel() {
        long editedCount = listCollection.stream()
                .filter(item -> item.getUpdatedBy() != null && item.getUpdatedBy().equals(MainApp.identityDto.getSociety().getCode()))
                .count();
        lblEdited.setText(String.valueOf(editedCount));
    }

    private void fetchCurrentShiftCollection() {
        var task = new MilkCollectionLoadTask(collectionDate, collectionDate, MainApp.identityDto.getDock().getDockNo(), 0);
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollection> temp = task.get();
                if (temp != null && !temp.isEmpty()) {
                    if (listCollection != null && !listCollection.isEmpty()) listCollection.clear();
                    if (listCollectionSummary != null && !listCollectionSummary.isEmpty())
                        listCollectionSummary.clear();

                    listCollection.addAll(temp);
                    listCollection.sort(Comparator.comparingInt(MilkCollection::getSampleNo).reversed());
                    tableCollection.setItems(listCollection);
                    updateStartEndTime();

                    // summary
                    for (MilkType item : cboxMilkType.getItems()) {
                        int memberCount = 0;
                        BigDecimal amt = BigDecimal.ZERO;
                        BigDecimal qty = BigDecimal.ZERO;
                        BigDecimal snf = BigDecimal.ZERO;
                        BigDecimal kgFat = BigDecimal.ZERO;
                        for (MilkCollection milkCollection : listCollection.stream().filter(p -> p.getMilkType().getCode() == item.getCode()).collect(Collectors.toList())) {
                            memberCount++;
                            amt = amt.add(milkCollection.getAmount());
                            qty = qty.add(milkCollection.getQty());
                            kgFat = kgFat.add(milkCollection.getFat().multiply(milkCollection.getQty()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
                            snf = snf.add(milkCollection.getSnf().multiply(milkCollection.getQty()).
                                            divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
                                    .setScale(2, RoundingMode.HALF_UP);
                        }
                        CollectionSummary summary = new CollectionSummary(item, memberCount, qty.setScale(SCALE, ROUND), amt.setScale(SCALE, ROUND));
                        if (qty.doubleValue() > 0)
                            summary.setAvgFat(BigDecimal.valueOf(kgFat.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
                        else summary.setAvgFat(BigDecimal.ZERO);
                        if (qty.doubleValue() > 0)
                            summary.setAvgSnf(BigDecimal.valueOf(snf.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
                        else
                            summary.setAvgSnf(BigDecimal.ZERO);
                        listCollectionSummary.add(summary);
                    }
                    //All
                    BigDecimal amt = BigDecimal.ZERO;
                    BigDecimal qty = BigDecimal.ZERO;
                    BigDecimal kgFat = BigDecimal.ZERO;
                    BigDecimal snf = BigDecimal.ZERO;
                    int memberCount = 0;
                    for (CollectionSummary collectionSummary : listCollectionSummary) {
                        memberCount += collectionSummary.getMemberCount();
                        amt = amt.add(collectionSummary.getAmount());
                        qty = qty.add(collectionSummary.getQty());
                        kgFat = kgFat.add(collectionSummary.getAvgFat().multiply(collectionSummary.getQty()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
                        snf = snf.add(collectionSummary.getAvgSnf().multiply(collectionSummary.getQty()).
                                        divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
                                .setScale(2, RoundingMode.HALF_UP);
                    }

                    CollectionSummary summary = new CollectionSummary(new MilkType("ALL"), memberCount, qty.setScale(SCALE, ROUND), amt.setScale(SCALE, ROUND));
                    if (qty.doubleValue() > 0)
                        summary.setAvgFat(BigDecimal.valueOf(kgFat.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
                    else summary.setAvgFat(BigDecimal.ZERO);
                    if (qty.doubleValue() > 0)
                        summary.setAvgSnf(BigDecimal.valueOf(snf.doubleValue() / qty.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP));
                    else
                        summary.setAvgSnf(BigDecimal.ZERO);
                    listCollectionSummary.add(summary);
                    tableSummary.setItems(listCollectionSummary);
                } else {
                    listCollection.clear();
                    listCollectionSummary.clear();
                    tableCollection.setItems(listCollection);
                    updateStartEndTime();

                    CollectionSummary summary = null;
                    for (MilkType item : cboxMilkType.getItems()) {
                        summary = new CollectionSummary(item, 0, BigDecimal.ZERO, BigDecimal.ZERO);
                        summary.setAvgFat(BigDecimal.ZERO);
                        summary.setAvgSnf(BigDecimal.ZERO);
                        listCollectionSummary.add(summary);
                    }
                    summary = new CollectionSummary(new MilkType("ALL"), 0, BigDecimal.ZERO, BigDecimal.ZERO);
                    summary.setAvgFat(BigDecimal.ZERO);
                    summary.setAvgSnf(BigDecimal.ZERO);
                    listCollectionSummary.add(summary);
                    tableSummary.setItems(listCollectionSummary);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    protected void fetchRateDetails() {
        fetchRateBased();
        var task = new MilkRateAndDetailsLoadTask(collectionPreReqDto.getMemberRate());
        task.setOnSucceeded(e -> {
            try {
                MilkRateAndDetailsDto dto = task.get();
                if (dto != null) {
                    memberMilkPurchaseRate = dto.getMemberPurchaseRate();
                    mapRateDetails = dto.getDetails();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchRateBased() {
        var task = new MilkRateBasedLoadTask(collectionPreReqDto.getMemberRate());
        task.setOnSucceeded(e -> {
            try {
                memberRateBasedList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchNextSampleNo() {
        var task = new SampleNoLoadTask(collectionDate, MainApp.identityDto.getDock(), null);
        task.setOnSucceeded(e -> {
            try {
                Number sampleNo = task.get();
                if (sampleNo != null)
                    txtSampleNo.setText(sampleNo.toString());
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setupBindingForAutoResult() {
        if (!qualityAuto)
            return;

        int mt = getMilkType().getCode();
        String currentFromMa;
        switch (analyserCount) {
            case 1:
                setupAnalyserBinding(1);
                rbtMa1.setSelected(true);
                break;
            case 2:
                if (analyserSeq) {
                    currentFromMa = analyserMilkTypeMappingLastSavedFrom.get(0);
                    setupMultiAnalyser(currentFromMa);
                } else {
                    if (mt == 1) {
                        setupAnalyserBinding(1);
                        rbtMa1.setSelected(true);
                    } else {
                        setupAnalyserBinding(2);
                        rbtMa2.setSelected(true);
                    }
                }
                break;
            case 3:
                if (analyserSeq) {
                    currentFromMa = analyserMilkTypeMappingLastSavedFrom.get(0);

                } else {
                    currentFromMa = analyserMilkTypeMappingLastSavedFrom.get(mt);
                }
                setupMultiAnalyser(currentFromMa);
                break;
            case 4:
                if (analyserSeq) {
                    currentFromMa = analyserMilkTypeMappingLastSavedFrom.get(0);
                } else {
                    currentFromMa = analyserMilkTypeMappingLastSavedFrom.get(mt);
                }
                setupMultiAnalyser(currentFromMa);
                break;
        }

        // disbale other milktype radio button
        if (!analyserSeq) {
            String strMAs = null;
            switch (mt) {
                case 1:
                    strMAs = analyserMilkTypeMapping.get(2);
                    for (String ma : strMAs.split(",")) {
                        disableMa(ma);
                    }
                    strMAs = analyserMilkTypeMapping.get(1);
                    for (String ma : strMAs.split(",")) {
                        enableMa(ma);
                    }
                    break;
                case 2:
                    strMAs = analyserMilkTypeMapping.get(1);
                    for (String ma : strMAs.split(",")) {
                        disableMa(ma);
                    }
                    strMAs = analyserMilkTypeMapping.get(2);
                    for (String ma : strMAs.split(",")) {
                        enableMa(ma);
                    }
                    break;
            }
        }
    }

    private void enableMa(String strTag) {
        switch (strTag) {
            case "MA1":
                rbtMa1.setDisable(false);
                break;
            case "MA2":
                rbtMa2.setDisable(false);
                break;
            case "MA3":
                rbtMa3.setDisable(false);
                break;
            case "MA4":
                rbtMa4.setDisable(false);
                break;
        }
    }

    private void disableMa(String strTag) {
        switch (strTag) {
            case "MA1":
                rbtMa1.setDisable(true);
                break;
            case "MA2":
                rbtMa2.setDisable(true);
                break;
            case "MA3":
                rbtMa3.setDisable(true);
                break;
            case "MA4":
                rbtMa4.setDisable(true);
                break;
        }
    }

    private void setupMultiAnalyser(String strTag) {
        switch (strTag) {
            case "MA1":
                setupAnalyserBinding(1);
                rbtMa1.setSelected(true);
                break;
            case "MA2":
                setupAnalyserBinding(2);
                rbtMa2.setSelected(true);
                break;
            case "MA3":
                setupAnalyserBinding(3);
                rbtMa3.setSelected(true);
                break;
            case "MA4":
                setupAnalyserBinding(4);
                rbtMa4.setSelected(true);
                break;
        }
    }

    private void setupAnalyserBinding(int tag) {
        fatStringProp.unbind();
        snfStringProp.unbind();
        switch (tag) {
            case 1:
                fatStringProp.bind(bindingFat1);
                snfStringProp.bind(bindingSnf1);
                break;
            case 2:
                fatStringProp.bind(bindingFat2);
                snfStringProp.bind(bindingSnf2);
                break;
            case 3:
                fatStringProp.bind(bindingFat3);
                snfStringProp.bind(bindingSnf3);
                break;
            case 4:
                fatStringProp.bind(bindingFat4);
                snfStringProp.bind(bindingSnf4);
                break;
        }
    }

    @Override
    public void setupComboBox() {
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkQuality.setConverter(new MilkQualityConvertor(cboxMilkQuality));
        cboxShift.setConverter(new ShiftConvertor(cboxShift));
        dpDate.setConverter(new LocalDateConvertor());
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });

    }

    @Override
    public void loadData() {

        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                shiftList = task.get();
                if (shiftList != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(shiftList);
                    cboxShift.setItems(FXCollections.observableList(list1));
                    if (LocalTime.now().isBefore(LocalTime.of(16, 0))) {
                        cboxShift.getSelectionModel().select(0);
                    } else {
                        cboxShift.getSelectionModel().select(1);
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task2 = new MilkQualityTypeLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<MilkQualityType> list = task2.get();
                if (list != null) {
                    cboxMilkQuality.setItems(FXCollections.observableList(list));
                    cboxMilkQuality.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    // DpuIncentive Load Data comment
    public void loadRequestData() {
//        if (MainApp.timingList != null && !MainApp.timingList.isEmpty()) {
//            if (MainApp.locale.equalsIgnoreCase("en")) {
//                lblShiftTime.setText("Shift Timing :- Morning : " + MainApp.timingList.get(0).getMstime() + "-" + MainApp.timingList.get(0).getMltime() + " | " + "Evening : " + MainApp.timingList.get(0).getEstime().minusHours(12) + "-" + MainApp.timingList.get(0).getEltime().minusHours(12));
//            } else {
//                lblShiftTime.setText("શિફ્ટ નો સમય :- સવાર : " + MainApp.timingList.get(0).getMstime() + "-" + MainApp.timingList.get(0).getMltime() + " | " + "સાંજ : " + MainApp.timingList.get(0).getEstime().minusHours(12) + "-" + MainApp.timingList.get(0).getEltime().minusHours(12));
//            }
//        }

        var task2 = new AllowDcsManualCollectionDateShiftLoadTask(CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue()), CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue()), null);
        task2.setOnSucceeded(e1 -> {
            try {
                if (task2.get() != null) {
                    manualCollectionRangeList = new ArrayList<>();
                    manualCollectionRangeList.addAll(task2.get());
                } else {
                    manualCollectionRangeList = new ArrayList<>();
                }
                if (CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue()).isBefore(LocalDateTime.now().minusHours(Long.parseLong(MainApp.getProperty("hrs", "72"))))) {
                    if (!manualCollectionRangeList.isEmpty() && manualCollectionRangeList.stream().filter(e2 ->
                            e2.getxCol1().equalsIgnoreCase("0")).anyMatch(e3 -> e3.getStatus() == 2)) {
                        loadAndCheckPreRequisite();
                    } else {
                        MyAlert alert = new WarningAlert(MainApp.stage, resourceBundle.getString("milkcollection"), resourceBundle.getString("You.will.not.be.able.to.collection.after.72.hours"));
                        alert.createAlert();
                        return;
                    }
                }
                loadAndCheckPreRequisite();
            } catch (InterruptedException | ExecutionException ee) {
                throw new RuntimeException(ee);
            }
        });
        new Thread(task2).start();
    }

    @Override
    protected void setRate(String rate) {
        txtRate.setText(rate);
    }

    @Override
    protected void setAmount(String amount) {
        txtAmount.setText(amount);
        try {
            if (MainApp.displaySerial != null) {
                MainApp.displaySerial.displayQuantity(getStringForDisplay("RTPL"));
                MainApp.displaySerial.displayQuantity(getStringForDisplay("AMT"));
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected String getQty() {
        return txtQty.getText() == null || txtQty.getText().isEmpty() ? "0" : txtQty.getText();
    }

    @Override
    protected void setQty(String qty) {
        txtQty.setText(qty);
    }

    @Override
    protected String getFat() {
        return txtFat.getText() == null || txtFat.getText().isEmpty() ? "0" : txtFat.getText();
    }

    @Override
    protected void setFat(String fat) {
        txtFat.setText(fat);
    }

    @Override
    protected String getSnf() {
        return txtSnf.getText() == null || txtSnf.getText().isEmpty() ? "0" : txtSnf.getText();
    }

    @Override
    protected void setSnf(String snf) {
        if (!"1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
            txtSnf.setText(snf);
        } else {
            txtSnf.setText(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0"));
        }
    }

    @Override
    protected String getWater() {
        return txtWater.getText() == null || txtWater.getText().isEmpty() ? "0" : txtWater.getText();
    }

    @Override
    protected void setWater(String water) {
        txtWater.setText(water);
    }

    @Override
    protected String getClr() {
        return txtClr.getText() == null || txtClr.getText().isEmpty() ? "0" : txtClr.getText();
    }

    @Override
    protected void setClr(String clr) {
        txtClr.setText(clr);
    }

    @Override
    protected MilkType getMilkType() {
        return cboxMilkType.getValue();
    }

    @Override
    protected void setMilkType(MilkType milkType) {

    }

    @Override
    protected String getSampleNo() {
        return txtSampleNo.getText();
    }

    @Override
    protected void setSampleNo(String sampleNo) {

    }

    @Override
    protected void setupAutoManualControls() {
        txtQty.setDisable(weightAuto);
        txtFat.setDisable(qualityAuto);
        if (!"1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0")))
            txtSnf.setDisable(qualityAuto);
        else
            txtSnf.setText(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0"));
        txtWater.setDisable(qualityAuto);
        txtWater.setDisable(true);
    }

    @Override
    public void reloadHardwareSetting(boolean flag) {
        if (flag) {
            closeDevicesIfAny();
            setupAutoManual();
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag) fetchCurrentShiftCollection();
    }

    private void readFile() {
        try {
            if (slipLanguage.equalsIgnoreCase("English")) slipFile = new File("resources/collection/PrintSlip.txt");
            else if (slipLanguage.equalsIgnoreCase("Hindi"))
                slipFile = new File("resources/collection/PrintSlipLocalHindi.txt");
            else slipFile = new File("resources/collection/PrintSlipLocal.txt");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String getPrinterName(List<HardwareDeviceConfig> devices) {
        for (HardwareDeviceConfig device : devices) {
            if (device.getDeviceType().equalsIgnoreCase("PRINTER")) {
                return device.getxCol1();
            }
        }
        return null;
    }

    private void placeVariables(MilkCollection collection, Object[] resp) {
        try {
            List<String> lines = null;

            masterLines.clear();
            lines = Files.readAllLines(slipFile.toPath(), StandardCharsets.UTF_8);
            int removepdline = -1;
            if (lines == null) return;
            for (int i = 0; i < lines.size(); ) {
                String s = lines.get(i);
                if (s.contains("{dcsshort}")) {
                    s = s.replace("{dcsshort}", MainApp.identityDto.getSociety().getName() + "-" + MainApp.identityDto.getSociety().getCodeEx());
                }
                if (s.contains("{membername}")) {
                    if (s.contains("{membername}")) {
                        if (MainApp.getProperty("slip.language", "").equalsIgnoreCase("Gujarati"))
                            s = s.replace("{membername}", collection.getMember().toMemberName("gu"));
                        else s = s.replace("{membername}", collection.getMember().toMemberName("en"));
                    }
                }
                if (s.contains("{code}")) {
                    s = s.replace("{code}", collection.getMember().getCodeEx());
                }
                if (s.contains("{date}")) {
                    s = s.replace("{date}", collection.getCollectionDate().format(dTF));
                }
                if (s.contains("{time}")) {
                    s = s.replace("{time}", LocalTime.now().format(dTF1));
                }
                if (s.contains("{dateRange}")) {
                    s = s.replace("{dateRange}", collection.getxCol3() != null ? collection.getxCol3() : "");
                }
                if (s.contains("{shift}")) {
                    s = s.replace("{shift}", collection.getShift().getName().substring(0, 1));
                }
                if (s.contains("{type}")) {
                    s = s.replace("{type}", collection.getMilkType().getName());
                }
                if (s.contains("{kgfat}")) {
                    if (memberRateBasedList != null) {
                        Optional<MemberMilkPurchaseRateBased> basedFat = memberRateBasedList.stream().filter(p -> p.getMilkType().getCode().compareTo(collection.getMilkType().getCode()) == 0 && collection.getFat().compareTo(p.getStartVal()) >= 0 && collection.getFat().compareTo(p.getEndVal()) <= 0).findFirst();
                        if (basedFat.isPresent()) {
                            s = s.replace("{kgfat}", basedFat.get().getKgRate().toString());
                        } else {
                            s = s.replace("{kgfat}", "");
                        }
                    } else {
                        String[] arr;
                        if (memberMilkPurchaseRate.getxCol1() != null) {
                            arr = memberMilkPurchaseRate.getxCol1().split("-");
                            s = s.replace("{kgfat}", collection.getMilkType().getCode() == 1 ? arr[0] : arr[1]);
                        }
                    }
                }

                if (s.contains("{qty}")) {
                    s = s.replace("{qty}", collection.getQty().toString() + (collection.isWeightAuto() ? "" : " *"));
                }
                if (s.contains("{fat}")) {
                    s = s.replace("{fat}", collection.getFat().toString() + (collection.isQualityAuto() ? "" : " *"));
                }
                if (s.contains("{snf}")) {
                    s = s.replace("{snf}", collection.getSnf().toString() + (collection.isQualityAuto() ? "" : " *"));
                }
                if (s.contains("{clr}")) {
                    s = s.replace("{clr}", collection.getClr().toString() + (collection.isQualityAuto() ? "A" : "M"));
                }
                if (s.contains("{rate}")) {
                    s = s.replace("{rate}", String.valueOf(collection.getRtpl()));
                }
                if (s.contains("{amount}")) {
                    s = s.replace("{amount}", String.valueOf(collection.getAmount()));
                }
                if (s.contains("{tQty}")) {
                    s = s.replace("{tQty}", collection.getxCol1() != null ? collection.getxCol1() : "");
                }
                if (s.contains("{tAmt}")) {

                    s = s.replace("{tAmt}", collection.getxCol2() != null ? collection.getxCol2() : "");
                }

                if (s.contains("{pdRate}")) {
                    if (schemeRateApplicability != null) {
                        s = s.replace("{pdRate}", schemeRateApplicability.getRtpl() != null ? schemeRateApplicability.getRtpl().toString() : "0");
                    } else {
                        s = s.replace("{pdRate}", "0");
                    }
                }
                if (s.contains("{pdAmt}")) {
                    if (schemeRateApplicability != null) {
                        s = s.replace("{pdAmt}", schemeRateApplicability.getRtpl() != null ? (schemeRateApplicability.getRtpl().multiply(collection.getQty()).add(collection.getAmount())).toString() : "0");
                    } else {
                        s = s.replace("{pdAmt}", "0");
                    }
                }


                if (resp != null) {
                    if (s.contains("{totalQty}")) {
                        s = s.replace("{totalQty}", String.valueOf(NumberUtil.round((double) resp[0], 2)));
                    }
                    if (s.contains("{totalAmount}")) {
                        s = s.replace("{totalAmount}", String.valueOf(NumberUtil.round((double) resp[1], 2)));
                    }
                }
                lines.set(i, s);
                if (s.contains("{")) continue;
                else i++;

                masterLines.add(s);
                s = null;
            }

            // CHINTAN | 23.05.2026 : To get message on print slip.
            if (messageList != null && !messageList.isEmpty()) {
                for (Message message : messageList) {
                    if (slipLanguage.equalsIgnoreCase("English")) {
                        masterLines.add(message.getMessage());
                    } else {
                        masterLines.add(message.getMessageLocal());
                    }
                }
            }

            if (removepdline >= 0) {
                lines.remove(removepdline);
                masterLines.remove(removepdline);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 23/05/2026    Chintan             1.0.0     To load messages of print slip for the day and shift
     */
    private void loadMessages() {
        var task = new MessagesForDateAndShiftLoadTask(dpDate.getValue(), cboxShift.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                messageList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error("Failed to load messages", ex);
            }
        });
        task.setOnFailed(e -> {
            LOGGER.error("Failed to load messages", task.getException());
        });
        new Thread(task).start();
    }

    private void print(List<String> masterLines) {
        for (Notification notification : MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 3).collect(Collectors.toList())) {
            if (notification.getToDate().isAfter(LocalDateTime.now())) {
                masterLines.add(notification.getMessage());
            }
        }
        if (MainApp.isIncentive) {
            masterLines.add(resourceBundle.getString("incentiveamount") + " : " + collection.getQty().multiply(BigDecimal.valueOf(MainApp.incentiveValue)));
        }

        for (int i = 0; i < Integer.parseInt(MainApp.getProperty("no.of.enter", "0")); i++) {
            masterLines.add("\n");
        }
        masterLines.add(".");
        printerHelper.print(masterLines);
    }

    private void processMargin(List<String> lines) {
        for (String s : lines) {
            if (s.equals("{forward}")) {
                printerHelper.forwardByLine(1);
            }
            if (s.equals("{reverse}")) {
                printerHelper.reverseByLine(1);
            }
        }
    }

    @Override
    protected String getFat1() {
        return txtFat1.getText() == null || txtFat1.getText().isEmpty() ? "0" : txtFat1.getText();
    }

    @Override
    protected void setFat1(String fat) {
        txtFat1.setText(fat);
    }

    @Override
    protected String getSnf1() {
        return txtSnf1.getText() == null || txtSnf1.getText().isEmpty() ? "0" : txtSnf1.getText();
    }

    @Override
    protected void setSnf1(String snf) {
        txtSnf1.setText(snf);
    }

    @Override
    protected String getWater1() {
        return "";
    }

    @Override
    protected void setWater1(String water) {

    }

    @Override
    protected String getFat2() {
        return txtFat2.getText() == null || txtFat2.getText().isEmpty() ? "0" : txtFat2.getText();
    }

    @Override
    protected void setFat2(String fat) {
        txtFat2.setText(fat);
    }

    @Override
    protected String getSnf2() {
        return txtSnf2.getText() == null || txtSnf2.getText().isEmpty() ? "0" : txtSnf2.getText();
    }

    @Override
    protected void setSnf2(String snf) {
        txtSnf2.setText(snf);
    }

    @Override
    protected String getWater2() {
        return "";
    }

    @Override
    protected void setWater2(String water) {

    }

    @Override
    protected String getFat3() {
        return txtFat3.getText() == null || txtFat3.getText().isEmpty() ? "0" : txtFat3.getText();
    }

    @Override
    protected void setFat3(String fat) {
        txtFat3.setText(fat);
    }

    @Override
    protected String getSnf3() {
        return txtSnf3.getText() == null || txtSnf3.getText().isEmpty() ? "0" : txtSnf3.getText();
    }

    @Override
    protected void setSnf3(String snf) {
        txtSnf3.setText(snf);
    }

    @Override
    protected String getWater3() {
        return "";
    }

    @Override
    protected void setWater3(String water) {

    }

    @Override
    protected String getFat4() {
        return txtFat4.getText() == null || txtFat4.getText().isEmpty() ? "0" : txtFat4.getText();
    }

    @Override
    protected void setFat4(String fat) {
        txtFat4.setText(fat);
    }

    @Override
    protected String getSnf4() {
        return txtSnf4.getText() == null || txtSnf4.getText().isEmpty() ? "0" : txtSnf4.getText();
    }

    @Override
    protected void setSnf4(String snf) {
        txtSnf4.setText(snf);
    }

    @Override
    protected String getWater4() {
        return "";
    }

    @Override
    protected void setWater4(String water) {

    }

    @Override
    protected void bindFatForAuto() {
        txtFat.textProperty().bind(fatStringProp);
    }

    @Override
    protected void bindSnfForAuto() {
        txtSnf.textProperty().bind(snfStringProp);
    }

    @Override
    protected void unbindFatForAuto() {
        txtFat.textProperty().unbind();
    }

    @Override
    protected void unbindSnfForAuto() {
        txtSnf.textProperty().unbind();
    }

    @Override
    protected void setHardwarePanelDisable() {
        switch (analyserCount) {
            case 1:
                rbtMa2.setDisable(true);
                rbtMa3.setDisable(true);
                rbtMa4.setDisable(true);
                break;
            case 2:
                rbtMa3.setDisable(true);
                rbtMa4.setDisable(true);
                break;
            case 3:
                rbtMa4.setDisable(true);
                break;
        }

        if (analyserSeq)
            return;

        analyserMilkTypeMapping.forEach((k, v) -> {
            switch (k) {
                case 1:
                    for (String s : v.split(",")) {
                        updateRadioLabel(s, "Cow");
                    }
                    break;
                case 2:
                    for (String s : v.split(",")) {
                        updateRadioLabel(s, "Buff");
                    }
                    break;
            }
        });
    }

    private void updateRadioLabel(String maTag, String milkType) {
        switch (maTag) {
            case "MA1":
                rbtMa1.setText("MA1-" + milkType);
                break;
            case "MA2":
                rbtMa2.setText("MA2-" + milkType);
                break;
            case "MA3":
                rbtMa3.setText("MA3-" + milkType);
                break;
            case "MA4":
                rbtMa4.setText("MA4-" + milkType);
                break;
        }
    }

    private void loadSchemeRate() {
        var task = new SchemeRateApplicabilityLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SchemeRateApplicability> schemeRateApplicabilities = task.get();
                if (schemeRateApplicabilities == null || schemeRateApplicabilities.isEmpty())
                    return;
                for (SchemeRateApplicability schemeRateApplicability1 : schemeRateApplicabilities) {
                    if (collectionDate.toLocalDate().isAfter(schemeRateApplicability1.getFromDate().toLocalDate())
                            && collectionDate.toLocalDate().isBefore(schemeRateApplicability1.getToDate().toLocalDate())) {
                        this.schemeRateApplicability = schemeRateApplicability1;
                    } else if ((collectionDate.toLocalDate().isEqual(schemeRateApplicability1.getFromDate().toLocalDate()) && (schemeRateApplicability1.getFromShift() <= cboxShift.getValue().getCode()) || (collectionDate.toLocalDate().isEqual(schemeRateApplicability1.getToDate().toLocalDate()) && schemeRateApplicability1.getToShift() >= cboxShift.getValue().getCode()))) {
                        this.schemeRateApplicability = schemeRateApplicability1;
                    }
                }
                if (schemeRateApplicability != null)
                    LOGGER.info(String.valueOf(schemeRateApplicability.getSchemeRateAppCode()));
                else
                    LOGGER.info("No Scheme Found");
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

}