package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.global.service.RateTypeService;
import com.eipl.amcs.master.global.service.ShiftService;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.RateTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.procurement.dto.PurchaseRateGenerate;
import com.eipl.amcs.master.procurement.dto.SocietyMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.*;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.master.procurement.task.SocietyMilkPurchaseRateSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.eipl.amcs.MainApp.context;

public class SocietyMilkPurchaseRateAddEditController implements MyInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyMilkPurchaseRateAddEditController.class);
    private final int SCALE = 2;
    private final RoundingMode RATE_ROUND = RoundingMode.HALF_UP;
    @FXML
    private Button btnBrowse, btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<String> cboxRateGenMethod;
    @FXML
    private ComboBox<Shift> cboxShift, cboxShiftApp;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private ComboBox<MilkQualityType> cboxMilkQualityType;
    @FXML
    private DatePicker dpWefDate;
    @FXML
    private StackPane root;
    @FXML
    private TableView<PurchaseRateGenerate> tableRateDetails;
    @FXML
    private TextField txtBrowse, txtDescription, txtkgfat;
    private ResourceBundle resourceBundle;
    private File selectedFile;
    private List<MilkType> listMilkType;
    private List<MilkQualityType> listMilkQualityType;
    private List<RateType> listRateType;
    private RateType rateType;
    private Map<Integer, List<PurchaseRateGenerate>> mapTableData = new HashMap<>();
    private StringBuilder errorMsg = null;
    private List<SocietyMilkPurchaseRateDetail> listDetails = new ArrayList<>();
    private SocietyMilkPurchaseRateDto dto = null;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        setupComboBox();
        loadData();

        btnBrowse.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Rate Chart Upload");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", "*.xls"));
            selectedFile = fileChooser.showOpenDialog(MainApp.getStage());
            if (selectedFile != null) {
                txtBrowse.setText(selectedFile.getAbsolutePath());
                loadRateFromExcel();
            }
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/procurement/SocietyMilkPurchaseRate.fxml"))));
        btnSaveUpdate.setOnAction(e -> validateAndSave());

    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        setValuesInObject();
        saveData();
    }

    private boolean validate() {
        if (dpWefDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("wefdatenullerror") + "\n");
        if (cboxShift.getValue() == null)
            errorMsg.append(resourceBundle.getString("shift.cannot.null") + "\n");
        if (cboxShiftApp.getValue() == null)
            errorMsg.append(resourceBundle.getString("shift.cannot.null") + "\n");

        return errorMsg.length() == 0;
    }

    private void setValuesInObject() {
        dto = new SocietyMilkPurchaseRateDto();
        // Rate
        SocietyMilkPurchaseRate rate = new SocietyMilkPurchaseRate();
        rate.setDescription(txtDescription.getText());
        rate.setRateGenMethodCode(CommonUtils.getRateGenerationMethodCode(cboxRateGenMethod.getValue()));
        rate.setShift(cboxShift.getValue());
        rate.setShiftApplicable(cboxShiftApp.getValue());
        rate.setUnionCode(MainApp.identityDto.getUnion().getCode());
        rate.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpWefDate.getValue(), cboxShift.getValue()));
        rate.setRateType(rateType);
        rate.setxCol1(txtkgfat.getText());

        // Applicability
        SocietyMilkPurchaseRateApplicability app = new SocietyMilkPurchaseRateApplicability();
        app.setShift(cboxShift.getValue());
        app.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpWefDate.getValue(), cboxShift.getValue()));
        app.setUnionCode(MainApp.identityDto.getUnion().getCode());
        app.setSociety(MainApp.identityDto.getSociety());

        // Details
        List<String> list = listDetails.stream().map(m -> m.getFat() + "#" + m.getSnf() + "#" + m.getRate() + "#" + m.getMilkType().getCode() + "#" + m.getMilkQualityType().getCode())
                .collect(Collectors.toList());

        dto.setPurchaseRate(rate);
        dto.setListApplicability(Arrays.asList(app));
        dto.setListDetail(list);
    }

    @Override
    public void saveData() {
        var task = new SocietyMilkPurchaseRateSaveTask(dto);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societymilkpurchaserate"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                if (obj instanceof String) {
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("societymilkpurchaserate"),
                            resourceBundle.getString("rate.insert.successful"));
                    alert.createAlert();

                    // Close window
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/procurement/SocietyMilkPurchaseRate.fxml")));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societymilkpurchaserate"),
                    task.getException().getMessage());
            alert.createAlert();
        });
        new Thread(task).start();
    }

    private void loadRateFromExcel() {
        var task = new LoadRateFromExcelTask();
        task.setOnSucceeded(e -> {
            try {
                task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("societymilkpurchaserate"),
                        "Rate file is imported");
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });
        task.setOnFailed(e -> {
            if (task.getException() instanceof IllegalArgumentException) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societymilkpurchaserate"),
                        task.getException().getMessage());
                alert.createAlert();
                LOGGER.error(task.getException().getMessage(), task.getException());
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxShift.setConverter(new ShiftConvertor(cboxShift));
        cboxShiftApp.setConverter(new ShiftConvertor(cboxShiftApp));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkQualityType.setConverter(new MilkQualityConvertor(cboxMilkQualityType));
        cboxRateGenMethod.setItems(FXCollections.observableArrayList("Excel"));
        cboxRateGenMethod.getSelectionModel().select(0);
        dpWefDate.setConverter(new LocalDateConvertor());
        dpWefDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpWefDate.setValue(dpWefDate.getConverter().fromString(dpWefDate.getEditor().getText()));
            }
        });
        cboxMilkType.setOnAction(e -> {
            prepareTableData();
        });
    }

    private void prepareTableData() {
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

        List<PurchaseRateGenerate> listRate = mapTableData.get(cboxMilkType.getValue().getCode());
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
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    cboxShiftApp.setItems(FXCollections.observableList(list));
                    List<Shift> listShift = CommonUtils.removeAllShift(list);
                    cboxShift.setItems(FXCollections.observableList(listShift));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task2 = new MilkTypeLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                listMilkType = task2.get();
                if (listMilkType != null)
                    cboxMilkType.setItems(FXCollections.observableList(listMilkType));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

        var task3 = new MilkQualityTypeLoadTask();
        task3.setOnSucceeded(e -> {
            try {
                listMilkQualityType = task3.get();
                if (listMilkQualityType != null)
                    cboxMilkQualityType.setItems(FXCollections.observableList(listMilkQualityType));
                cboxMilkQualityType.getSelectionModel().select(0);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task3).start();

        var task4 = new RateTypeLoadTask();
        task4.setOnSucceeded(e -> {
            try {
                listRateType = task4.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task4).start();
    }


    class LoadRateFromExcelTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            listDetails.clear();

            String fileExtension = CommonUtils.getFileExtension(selectedFile);
            if (fileExtension == null || !"xls".equalsIgnoreCase(fileExtension))
                throw new IllegalArgumentException("Invalid Rate File");

            Workbook workbook = new HSSFWorkbook(new FileInputStream(selectedFile));
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String arr[] = workbook.getSheetName(i).split("-");
                Integer quality = arr[1].equalsIgnoreCase("good") ? 1 : arr[1].equalsIgnoreCase("sour") ? 2 : 3;
                List<SocietyMilkPurchaseRateDetail> listDtl = new ArrayList<>();
                BigDecimal minFat = new BigDecimal(100);
                BigDecimal maxFat = BigDecimal.ZERO;
                BigDecimal minSnf = new BigDecimal(100);
                BigDecimal maxSnf = BigDecimal.ZERO;
                Sheet dataSheet = workbook.getSheetAt(i);

                MilkType milkType = listMilkType.stream().filter(p -> arr[0].equalsIgnoreCase(p.getName())).findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Sheet Name"));
                MilkQualityType milkQualityType = listMilkQualityType.stream().filter(p -> p.getCode() == quality).findAny()
//                MilkQualityType milkQualityType = listMilkQualityType.stream().filter(p -> p.getCode() == 1).findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Milk Quality Type"));
                Iterator<Row> iterator = dataSheet.iterator();
                List<BigDecimal> listFat = new ArrayList<>();
                List<BigDecimal> listSnf = new ArrayList<>();
                boolean firstRow = true;
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();

                    Iterator<Cell> cellIterator = currentRow.iterator();
                    boolean firstCell = true;
                    int index = 0;
                    BigDecimal fat = null;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (firstRow && firstCell) {
                            LOGGER.info("RateType {}", cell.getStringCellValue());
                            rateType = listRateType.stream().filter(p -> cell.getStringCellValue().equalsIgnoreCase(p.getRateType()))
                                    .findAny().orElseThrow(() -> new IllegalArgumentException("Invalid Rate Type"));
                            firstCell = false;
                        } else if (firstRow && !firstCell) {
                            BigDecimal snf = new BigDecimal(cell.getNumericCellValue()).setScale(SCALE, RATE_ROUND);
                            minSnf = minSnf.min(snf);
                            maxSnf = maxSnf.max(snf);
                            LOGGER.info("SNF Value {} at [{}, {}]", snf, cell.getRowIndex(), cell.getColumnIndex());
                            listSnf.add(snf);
                        } else if (!firstRow && firstCell) {
                            fat = new BigDecimal(cell.getNumericCellValue()).setScale(SCALE, RATE_ROUND);
                            minFat = minFat.min(fat);
                            maxFat = maxFat.max(fat);
                            listFat.add(fat);
                            LOGGER.info("FAT Value {} at [{}, {}]", fat, cell.getRowIndex(), cell.getColumnIndex());
                            firstCell = false;
                        } else {
                            BigDecimal rtpl = new BigDecimal(cell.getNumericCellValue()).setScale(SCALE, RATE_ROUND);
                            LOGGER.info("RTPL Value {} at index[{}, {}] and Quality[{}, {}]", rtpl, cell.getRowIndex(), cell.getColumnIndex(),
                                    fat, listSnf.get(index));

                            SocietyMilkPurchaseRateDetail dtl = new SocietyMilkPurchaseRateDetail();
                            dtl.setFat(fat);
                            dtl.setSnf(listSnf.get(index));
                            dtl.setRate(rtpl);
                            dtl.setMilkType(milkType);
                            dtl.setMilkQualityType(milkQualityType);
                            listDtl.add(dtl);
                            listDetails.add(dtl);

                            index++;
                        }
                    }
                    firstRow = false;
                }

                // Validate Range
                BigDecimal fatVal = maxFat.subtract(minFat).multiply(new BigDecimal(10)).setScale(SCALE, RATE_ROUND).add(BigDecimal.ONE);
                BigDecimal snfVal = maxSnf.subtract(minSnf).multiply(new BigDecimal(10)).setScale(SCALE, RATE_ROUND).add(BigDecimal.ONE);
                if (listFat.size() != fatVal.intValue())
                    throw new IllegalArgumentException("FAT parameter range is missing!");
                if (listSnf.size() != snfVal.intValue())
                    throw new IllegalArgumentException("SNF parameter range is missing!");

                // Generate rate data for table show
                List<PurchaseRateGenerate> listRate = new ArrayList<>();
                BigDecimal fat = BigDecimal.ZERO;
                for (SocietyMilkPurchaseRateDetail item : listDtl) {
                    if (!Objects.equals(fat, item.getFat())) {
                        PurchaseRateGenerate g = new PurchaseRateGenerate();
                        g.setFat(item.getFat());
                        Map<BigDecimal, BigDecimal> temp = new TreeMap<>();
                        temp.put(item.getSnf(), item.getRate());
                        g.setMap(temp);

                        listRate.add(g);
                    } else {
                        for (PurchaseRateGenerate g : listRate) {
                            if (Objects.equals(g.getFat(), item.getFat())) {
                                g.getMap().put(item.getSnf(), item.getRate());
                                break;
                            }
                        }
                    }
                    fat = item.getFat();
                }
                Collections.sort(listRate, Comparator.comparing(PurchaseRateGenerate::getFat));
                mapTableData.put(milkType.getCode(), listRate);
            }
            return null;
        }
    }
}
