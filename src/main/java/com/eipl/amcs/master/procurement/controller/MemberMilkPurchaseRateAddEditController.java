package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkQualityConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.RateTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.convertor.FormulaConvertor;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.task.FormulaLoadTask;
import com.eipl.amcs.master.procurement.dto.MemberMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.dto.PurchaseRateGenerate;
import com.eipl.amcs.master.procurement.dto.RateViewDto;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateDetail;
import com.eipl.amcs.master.procurement.task.GenerateFromParamTask;
import com.eipl.amcs.master.procurement.task.MemberMilkPurchaseRateSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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

public class MemberMilkPurchaseRateAddEditController implements MyInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberMilkPurchaseRateAddEditController.class);
    private final int SCALE = 2;
    private final RoundingMode RATE_ROUND = RoundingMode.HALF_UP;
    @FXML
    private StackPane root;
    @FXML
    private ComboBox<String> cboxRateGenMethod, cboxDeductionType, cboxRefType, cboxQualityParam;
    @FXML
    private DatePicker dpWefDate;
    @FXML
    private ComboBox<Shift> cboxShift, cboxShiftApp;
    @FXML
    private ComboBox<RateType> cboxRateType;
    @FXML
    private TextField txtDescription, txtBrowse, txtkgfat, txtkgfat1, txtFixedPoint, txtStep, txtValue, txtFrom, txtTo;
    @FXML
    private Button btnBrowse, btnClose, btnSaveUpdate, btnAddBased, btnGenerate, btnCancel, btnDelete, btnView;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private ComboBox<MilkQualityType> cboxMilkQualityType;
    @FXML
    private ComboBox<Formula> cboxFormula;
    @FXML
    private GridPane grid1;

    private final List<MemberMilkPurchaseRateDetail> listDetails = new ArrayList<>();
    private List<Formula> formulaList;
    private List<MilkType> listMilkType;
    private List<MilkQualityType> listMilkQualityType;
    private List<RateType> listRateType;
    private ResourceBundle resourceBundle;
    private File selectedFile;
    private RateType rateType;
    private StringBuilder errorMsg = null;
    private MemberMilkPurchaseRateDto dto = null;
    private boolean isRateAdded = false;
    @FXML
    private TableView<MemberMilkPurchaseRateBased> tablePurchaseBased;

    private List<MemberMilkPurchaseRateBased> memberMilkPurchaseRateBasedList = new ArrayList<>();

    @FXML
    private TableColumn<MemberMilkPurchaseRateBased, Number> colFrom, colValue, colFixedPoint, colKgRate, colTo, colStep;
    @FXML
    private TableColumn<MemberMilkPurchaseRateBased, Number> colRefType, colQualityParam;
    @FXML
    private TableColumn<MemberMilkPurchaseRateBased, String> colDeductionType;
    @FXML
    private TableColumn<MemberMilkPurchaseRateBased, String> colFormula;
    @FXML
    private TableColumn<MemberMilkPurchaseRateBased, MilkQualityType> colMilkQuality;
    @FXML
    private TableColumn<MemberMilkPurchaseRateBased, MilkType> colMilkType;
    @FXML
    private TableColumn<MemberMilkPurchaseRateBased, Number> colRateType;
    private final ObjectProperty<MemberMilkPurchaseRateBased> propMemberMilkPurchaseRateBased;

    private RateViewDto rateViewDto = new RateViewDto();

    public MemberMilkPurchaseRateAddEditController() {
        this.propMemberMilkPurchaseRateBased = new SimpleObjectProperty<>();
    }


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        setupComboBox();
        loadData();
        loadControls();
        setupTable();

        btnView.setDisable(true);
        btnDelete.setDisable(true);
        btnGenerate.setDisable(true);
        rateViewDto.setRateType((short) 0);

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

        cboxRateGenMethod.setOnAction(e -> {
            clearControls();
            if (cboxRateGenMethod.getSelectionModel().getSelectedIndex() == 0) {
                grid1.setDisable(true);
                txtBrowse.setDisable(false);
                btnBrowse.setDisable(false);
                txtkgfat.setDisable(false);
                btnGenerate.setDisable(true);
                btnDelete.setDisable(true);
            } else {
                grid1.setDisable(false);
                btnGenerate.setDisable(false);
                btnDelete.setDisable(false);
                txtBrowse.setDisable(true);
                btnBrowse.setDisable(true);
                txtkgfat.setDisable(true);
            }

        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/procurement/MemberMilkPurchaseRate.fxml"))));
        btnSaveUpdate.setOnAction(e -> validateAndSave());

        btnAddBased.setOnAction(e -> {
            addBased();
        });
        btnGenerate.setOnAction(e -> {
            generateRate();
        });
        btnCancel.setOnAction(e -> {
            clearBased();
        });
        btnDelete.setOnAction(e -> {
            deleteBased();
        });
        btnView.setOnAction(e -> {
            openRateView();
        });
    }

    @Override
    public void loadControls() {
        try {
            grid1.setDisable(true);
            tablePurchaseBased.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupTable() {
        colRateType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRateType()));
        colQualityParam.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQualityParam()));
        colFrom.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartVal()));
        colTo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEndVal()));
        colKgRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getKgRate()));
        colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
        colMilkQuality.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQualityType()));
        colRefType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRefType()));
        colFixedPoint.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFixedPoint()));
        colStep.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStep()));
        colValue.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getVal()));
        colFormula.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFormula().getFormula()));
        propMemberMilkPurchaseRateBased.bind(tablePurchaseBased.getSelectionModel().selectedItemProperty());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksalerate"), errorMsg.toString());
            alert.createAlert();
            return;
        }
        setValuesInObject();

        MainApp.paneDrop.setVisible(true);
        MainApp.lblMessage.setText("Preparing Member Milk Purchase Rate...");
        saveData();
    }

    private boolean validate() {
        if (dpWefDate.getValue() == null) errorMsg.append(resourceBundle.getString("wefdatenullerror") + "\n");
        if (cboxShift.getValue() == null) errorMsg.append(resourceBundle.getString("shift.cannot.null") + "\n");
        if (cboxShiftApp.getValue() == null) errorMsg.append(resourceBundle.getString("shift.cannot.null") + "\n");
        if (!isRateAdded) errorMsg.append(resourceBundle.getString("rate.cannot.be.null") + "\n");

        return errorMsg.length() == 0;
    }

    private void setValuesInObject() {
        dto = new MemberMilkPurchaseRateDto();
        // Rate
        MemberMilkPurchaseRate rate = new MemberMilkPurchaseRate();
        Short rateGenMethod = CommonUtils.getRateGenerationMethodCode(cboxRateGenMethod.getValue());
        rate.setDescription(txtDescription.getText());
        rate.setRateGenMethodCode(rateGenMethod);
        rate.setShift(cboxShift.getValue());
        rate.setShiftApplicable(cboxShiftApp.getValue());
        rate.setUnionCode(MainApp.identityDto.getUnion().getCode());
        rate.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpWefDate.getValue(), cboxShift.getValue()));
        rate.setSociety(MainApp.identityDto.getSociety());
        rate.setRateType(rateType);
        rate.setxCol1(rateGenMethod == 1 ? txtkgfat.getText() : txtkgfat1.getText());

        // Applicability
        MemberMilkPurchaseRateApplicability app = new MemberMilkPurchaseRateApplicability();
        app.setShift(cboxShift.getValue());
        app.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(dpWefDate.getValue(), cboxShift.getValue()));
        app.setUnionCode(MainApp.identityDto.getUnion().getCode());
        app.setSociety(MainApp.identityDto.getSociety());

        List<String> list = new ArrayList<>();

        if (rateGenMethod == 1) {
            // Details         //Based
            BigDecimal firstFat = BigDecimal.valueOf(0.0);
            BigDecimal firstSnf = BigDecimal.valueOf(0.0);
            BigDecimal lastFat = BigDecimal.valueOf(0.0);
            BigDecimal lastSnf = BigDecimal.valueOf(0.0);
            MilkQualityType tempMilkQualityType = null;
            MilkType tempmilkType = null;
            Optional<Formula> formula = null;
            MemberMilkPurchaseRateBased memberMilkPurchaseRateBasedFat = new MemberMilkPurchaseRateBased();
            MemberMilkPurchaseRateBased memberMilkPurchaseRateBasedSnf = new MemberMilkPurchaseRateBased();
            List<MemberMilkPurchaseRateBased> memberMilkPurchaseRateBasedList = new ArrayList<>();

            for (MemberMilkPurchaseRateDetail m : listDetails) {
                list.add(m.getFat() + "#" + m.getSnf() + "#" + m.getRate() + "#" + m.getMilkType().getCode() + "#" + m.getMilkQualityType().getCode());

                switch (m.getMilkType().getCode()) {
                    case 1:
                        formula = formulaList.stream().filter(f -> f.getName().toLowerCase().contains("cow")).findFirst();

                        break;
                    case 2:
                        formula = formulaList.stream().filter(f -> f.getName().toLowerCase().contains("buff")).findFirst();
                        break;
                    case 3:
                        formula = formulaList.stream()
                                .filter(f -> f.getName().toLowerCase().contains("mix")).findFirst();
                        break;
                }

                if (tempmilkType == null || tempMilkQualityType == null) {
                    tempmilkType = m.getMilkType();
                    tempMilkQualityType = m.getMilkQualityType();
                    firstSnf = m.getSnf();
                    firstFat = m.getFat();
                    memberMilkPurchaseRateBasedFat.setStartVal(firstFat);
                    memberMilkPurchaseRateBasedFat.setMilkType(tempmilkType);
                    memberMilkPurchaseRateBasedFat.setMilkQualityType(tempMilkQualityType);
                    memberMilkPurchaseRateBasedFat.setQualityParam(1);
                    memberMilkPurchaseRateBasedFat.setKgRate(new BigDecimal(txtkgfat.getText()));
                    memberMilkPurchaseRateBasedFat.setVal(new BigDecimal("0.0"));
                    memberMilkPurchaseRateBasedFat.setRateType(rateType.getCode());
                    memberMilkPurchaseRateBasedFat.setFormula(formula.orElse(null));
                    memberMilkPurchaseRateBasedFat.setFixedPoint(new BigDecimal("0.0"));

                    memberMilkPurchaseRateBasedSnf.setStartVal(firstSnf);
                    memberMilkPurchaseRateBasedSnf.setMilkType(tempmilkType);
                    memberMilkPurchaseRateBasedSnf.setMilkQualityType(tempMilkQualityType);
                    memberMilkPurchaseRateBasedSnf.setQualityParam(2);
                    memberMilkPurchaseRateBasedSnf.setKgRate(new BigDecimal(txtkgfat.getText()));
                    memberMilkPurchaseRateBasedSnf.setVal(new BigDecimal("0.0"));
                    memberMilkPurchaseRateBasedSnf.setRateType(rateType.getCode());
                    memberMilkPurchaseRateBasedSnf.setFormula(formula.orElse(null));
                    memberMilkPurchaseRateBasedSnf.setFixedPoint(new BigDecimal("0.0"));
                }

                if (m.getMilkType() != tempmilkType || m.getMilkQualityType() != tempMilkQualityType) {
                    memberMilkPurchaseRateBasedFat.setEndVal(lastFat);
                    memberMilkPurchaseRateBasedSnf.setEndVal(lastSnf);

                    memberMilkPurchaseRateBasedList.add(memberMilkPurchaseRateBasedFat);
                    if (rateType.getCode() == 2) memberMilkPurchaseRateBasedList.add(memberMilkPurchaseRateBasedSnf);

                    tempmilkType = m.getMilkType();
                    tempMilkQualityType = m.getMilkQualityType();
                    firstSnf = m.getSnf();
                    firstFat = m.getFat();
                    memberMilkPurchaseRateBasedFat = new MemberMilkPurchaseRateBased();
                    memberMilkPurchaseRateBasedFat.setStartVal(firstFat);
                    memberMilkPurchaseRateBasedFat.setMilkType(tempmilkType);
                    memberMilkPurchaseRateBasedFat.setMilkQualityType(tempMilkQualityType);
                    memberMilkPurchaseRateBasedFat.setQualityParam(1);
                    memberMilkPurchaseRateBasedFat.setKgRate(new BigDecimal(txtkgfat.getText()));
                    memberMilkPurchaseRateBasedFat.setVal(new BigDecimal("0.0"));
                    memberMilkPurchaseRateBasedFat.setRateType(rateType.getCode());
                    memberMilkPurchaseRateBasedFat.setFormula(formula.orElse(null));
                    memberMilkPurchaseRateBasedFat.setFixedPoint(new BigDecimal("0.0"));

                    memberMilkPurchaseRateBasedSnf = new MemberMilkPurchaseRateBased();
                    memberMilkPurchaseRateBasedSnf.setStartVal(firstSnf);
                    memberMilkPurchaseRateBasedSnf.setMilkType(tempmilkType);
                    memberMilkPurchaseRateBasedSnf.setMilkQualityType(tempMilkQualityType);
                    memberMilkPurchaseRateBasedSnf.setQualityParam(2);
                    memberMilkPurchaseRateBasedSnf.setKgRate(new BigDecimal(txtkgfat.getText()));
                    memberMilkPurchaseRateBasedSnf.setVal(new BigDecimal("0.0"));
                    memberMilkPurchaseRateBasedSnf.setRateType(rateType.getCode());
                    memberMilkPurchaseRateBasedSnf.setFormula(formula.orElse(null));
                    memberMilkPurchaseRateBasedSnf.setFixedPoint(new BigDecimal("0.0"));
                }
                lastSnf = m.getSnf();
                lastFat = m.getFat();
            }
            memberMilkPurchaseRateBasedFat.setEndVal(lastFat);
            memberMilkPurchaseRateBasedSnf.setEndVal(lastSnf);
            memberMilkPurchaseRateBasedList.add(memberMilkPurchaseRateBasedFat);
            if (rateType.getCode() == 2) memberMilkPurchaseRateBasedList.add(memberMilkPurchaseRateBasedSnf);
        } else {
            // Details
            list = listDetails.stream().map(m -> m.getFat() + "#" + m.getSnf() + "#" + m.getRate() + "#" + m.getMilkType().getCode() + "#" + m.getMilkQualityType().getCode())
                    .collect(Collectors.toList());
        }
        dto.setListRateBased(memberMilkPurchaseRateBasedList);
        dto.setPurchaseRate(rate);
        dto.setListApplicability(List.of(app));
        dto.setListDetail(list);
    }

    @Override
    public void saveData() {
        var task = new MemberMilkPurchaseRateSaveTask(dto);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MainApp.paneDrop.setVisible(false);
                if (obj == null) {
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("membermilkpurchaserate"), resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }

                if (obj instanceof String) {

                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("membermilkpurchaserate"), resourceBundle.getString("rate.insert.successful"));
                    alert.createAlert();

                    // Close window
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/procurement/MemberMilkPurchaseRate.fxml")));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("membermilkpurchaserate"), task.getException().getMessage());
            alert.createAlert();
        });
        new Thread(task).start();
    }

    private void loadRateFromExcel() {
        var task = new LoadRateFromExcelTask();
        task.setOnSucceeded(e -> {
            try {
                task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("membermilkpurchaserate"), "Rate file is imported");
                alert.createAlert();
                isRateAdded = true;
            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });
        task.setOnFailed(e -> {
            isRateAdded = false;
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("membermilkpurchaserate"), resourceBundle.getString("error.occurred"));
            alert.createAlert();
            if (task.getException() instanceof IllegalArgumentException) {
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
        cboxFormula.setConverter(new FormulaConvertor(cboxFormula));
        cboxMilkQualityType.setConverter(new MilkQualityConvertor(cboxMilkQualityType));
        cboxRateGenMethod.setItems(FXCollections.observableArrayList("Excel", "Manual"));
        cboxDeductionType.setItems(FXCollections.observableArrayList("NA", "Value Addition", "Value Deduction", "Percentage Addition", "Percentage Deduction"));
        cboxRefType.setItems(FXCollections.observableArrayList("NA", "Fixed Point", "Actual"));
        cboxRateGenMethod.getSelectionModel().select(0);
        dpWefDate.setConverter(new LocalDateConvertor());
        dpWefDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpWefDate.setValue(dpWefDate.getConverter().fromString(dpWefDate.getEditor().getText()));
            }
        });
        cboxRateType.setOnAction(e -> {
            if (cboxRateType != null) {
                if (cboxRateType.getSelectionModel().getSelectedItem() instanceof RateType) {
                    rateType = cboxRateType.getSelectionModel().getSelectedItem();
                    if (rateType.getRateType().contains("+")) {
                        cboxQualityParam.setItems(FXCollections.observableArrayList(rateType.getRateType().split("\\+")));
                    } else {
                        cboxQualityParam.setItems(FXCollections.observableArrayList(rateType.getRateType()));
                    }
                }
            }

        });

        cboxDeductionType.getSelectionModel().select(0);
        cboxDeductionType.setOnAction(event -> {
            if (cboxDeductionType.getSelectionModel().getSelectedIndex() == 0) {
                cboxRefType.setDisable(true);
                cboxRefType.getSelectionModel().select(0);
                txtFixedPoint.setDisable(true);
                txtStep.setDisable(true);
                txtValue.setDisable(true);
                txtFixedPoint.clear();
                txtStep.clear();
                txtValue.clear();
            } else {
                cboxRefType.setDisable(false);
            }
        });

        cboxRefType.getSelectionModel().select(0);
        cboxRefType.setOnAction(event -> {
            if (cboxRefType.getSelectionModel().getSelectedIndex() == 1) {
                txtFixedPoint.setDisable(false);
                txtStep.setDisable(false);
                txtValue.setDisable(false);
            } else if (cboxRefType.getSelectionModel().getSelectedIndex() == 2) {
                txtFixedPoint.setDisable(true);
                txtStep.setDisable(true);
                txtValue.setDisable(false);
            } else {
                txtFixedPoint.setDisable(true);
                txtStep.setDisable(true);
                txtValue.setDisable(true);
                txtFixedPoint.clear();
                txtStep.clear();
                txtValue.clear();
            }
        });
    }

    @Override
    public void clearControls() {
        dto = new MemberMilkPurchaseRateDto();
        memberMilkPurchaseRateBasedList = new ArrayList<>();
        tablePurchaseBased.setItems(FXCollections.observableList(memberMilkPurchaseRateBasedList));
        rateType = new RateType();
        listDetails.clear();
        isRateAdded = false;
        rateViewDto.setMemberRate(null);
        btnView.setDisable(true);
        cboxDeductionType.getSelectionModel().select(0);
        cboxRefType.getSelectionModel().select(0);
        clearBased();
    }

    @Override
    public void loadData() {
        loadMilkType();
        loadFormula();
        loadShift();
        loadRateType();
        loadMilkQualityType();
    }

    class LoadRateFromExcelTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            try {
                listDetails.clear();

                String fileExtension = CommonUtils.getFileExtension(selectedFile);
                if (!"xls".equalsIgnoreCase(fileExtension)) throw new IllegalArgumentException("Invalid Rate File");

                Workbook workbook = new HSSFWorkbook(new FileInputStream(selectedFile));
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    List<MemberMilkPurchaseRateDetail> listDtl = new ArrayList<>();
                    BigDecimal minFat = new BigDecimal(100);
                    BigDecimal maxFat = BigDecimal.ZERO;
                    BigDecimal minSnf = new BigDecimal(100);
                    BigDecimal maxSnf = BigDecimal.ZERO;
                    Sheet dataSheet = workbook.getSheetAt(i);

                    MilkType milkType = listMilkType.stream().filter(p -> dataSheet.getSheetName().equalsIgnoreCase(p.getName())).findAny().orElseThrow(() -> new IllegalArgumentException("Invalid Sheet Name"));
                    MilkQualityType milkQualityType = listMilkQualityType.stream().filter(p -> p.getCode() == 1).findAny().orElseThrow(() -> new IllegalArgumentException("Invalid Milk Quality Type"));

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
                                rateType = listRateType.stream().filter(p -> cell.getStringCellValue().equalsIgnoreCase(p.getRateType())).findAny().orElseThrow(() -> new IllegalArgumentException("Invalid Rate Type"));
                                firstCell = false;
                            } else if (firstRow && !firstCell) {
                                BigDecimal snf = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(SCALE, RATE_ROUND);
                                minSnf = minSnf.min(snf);
                                maxSnf = maxSnf.max(snf);
                                LOGGER.info("SNF Value {} at [{}, {}]", snf, cell.getRowIndex(), cell.getColumnIndex());
                                listSnf.add(snf);
                            } else if (!firstRow && firstCell) {
                                fat = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(SCALE, RATE_ROUND);
                                minFat = minFat.min(fat);
                                maxFat = maxFat.max(fat);
                                listFat.add(fat);
                                LOGGER.info("FAT Value {} at [{}, {}]", fat, cell.getRowIndex(), cell.getColumnIndex());
                                firstCell = false;
                            } else {
                                BigDecimal rtpl = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(SCALE, RATE_ROUND);
                                LOGGER.info("RTPL Value {} at index[{}, {}] and Quality[{}, {}]", rtpl, cell.getRowIndex(), cell.getColumnIndex(), fat, listSnf.get(index));

                                MemberMilkPurchaseRateDetail dtl = new MemberMilkPurchaseRateDetail();
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
                    for (MemberMilkPurchaseRateDetail item : listDtl) {
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
                    MemberMilkPurchaseRate tempMemberMilkPurchaseRate = new MemberMilkPurchaseRate();
                    tempMemberMilkPurchaseRate.setListDetails(listDetails);
                    rateViewDto.setMemberRate(tempMemberMilkPurchaseRate);
                    btnView.setDisable(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void loadMilkType() {
        try {
            var task2 = new MilkTypeLoadTask();
            task2.setOnSucceeded(e -> {
                try {
                    listMilkType = task2.get();
                    if (listMilkType != null) cboxMilkType.setItems(FXCollections.observableList(listMilkType));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task2).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMilkQualityType() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFormula() {
        try {
            var task5 = new FormulaLoadTask();
            task5.setOnSucceeded(e -> {
                try {
                    formulaList = task5.get();
                    if (!formulaList.isEmpty()) cboxFormula.setItems(FXCollections.observableList(formulaList));

                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task5).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRateType() {
        try {
            var task4 = new RateTypeLoadTask();
            task4.setOnSucceeded(e -> {
                try {
                    listRateType = task4.get();
                    if (!listRateType.isEmpty()) cboxRateType.setItems(FXCollections.observableList(listRateType));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task4).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadShift() {
        try {
            var task = new ShiftLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Shift> list = task.get();
                    if (list != null) {
                        cboxShiftApp.setItems(FXCollections.observableList(list));
                        List<Shift> listShift = CommonUtils.removeAllShift(list);
                        cboxShift.setItems(FXCollections.observableList(listShift));
                        cboxShift.getSelectionModel().select(0);
                        cboxShiftApp.getSelectionModel().select(2);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBased() {
        if (cboxRateType.getSelectionModel().getSelectedItem() == null || cboxQualityParam.getSelectionModel().getSelectedItem() == null
                || cboxMilkType.getSelectionModel().getSelectedItem() == null || cboxMilkQualityType.getSelectionModel().getSelectedItem() == null ||
                txtFrom.getText().isBlank() || txtTo.getText().isBlank() || txtkgfat1.getText().isBlank()
                || cboxFormula.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        MemberMilkPurchaseRateBased memberMilkPurchaseRateBased = new MemberMilkPurchaseRateBased();

        memberMilkPurchaseRateBased.setRateType(rateType.getRateType().contains("+") ? 2 : 1);
        memberMilkPurchaseRateBased.setQualityParam(cboxQualityParam.getSelectionModel().getSelectedIndex() + 1);
        memberMilkPurchaseRateBased.setMilkType(cboxMilkType.getSelectionModel().getSelectedItem());
        memberMilkPurchaseRateBased.setMilkQualityType(cboxMilkQualityType.getSelectionModel().getSelectedItem());
        memberMilkPurchaseRateBased.setStartVal(new BigDecimal(txtFrom.getText()));
        memberMilkPurchaseRateBased.setEndVal(new BigDecimal(txtTo.getText()));
        memberMilkPurchaseRateBased.setKgRate(new BigDecimal(txtkgfat1.getText()));
        memberMilkPurchaseRateBased.setDeductionType(cboxDeductionType.getSelectionModel().getSelectedIndex());
        memberMilkPurchaseRateBased.setRefType(cboxRefType.getSelectionModel().getSelectedIndex() + 1);
        memberMilkPurchaseRateBased.setFixedPoint(new BigDecimal(txtFixedPoint.getText().isBlank() ? "0" : txtFixedPoint.getText()));
        memberMilkPurchaseRateBased.setStep(Integer.parseInt(txtStep.getText().isBlank() ? "0" : txtStep.getText()));
        memberMilkPurchaseRateBased.setVal(new BigDecimal(txtValue.getText().isBlank() ? "0" : txtValue.getText()));
        memberMilkPurchaseRateBased.setFormula(cboxFormula.getSelectionModel().getSelectedItem());
        memberMilkPurchaseRateBasedList.add(memberMilkPurchaseRateBased);
        tablePurchaseBased.setItems(FXCollections.observableList(memberMilkPurchaseRateBasedList));
    }

    private void deleteBased() {
        MemberMilkPurchaseRateBased memberMilkPurchaseRateBased = propMemberMilkPurchaseRateBased.get();
        if (memberMilkPurchaseRateBased != null) {
            memberMilkPurchaseRateBasedList.remove(memberMilkPurchaseRateBased);
            tablePurchaseBased.setItems(FXCollections.observableList(memberMilkPurchaseRateBasedList));
        }
    }

    private void clearBased() {
        cboxMilkType.getSelectionModel().clearSelection();
        cboxQualityParam.getSelectionModel().clearSelection();
        cboxRefType.getSelectionModel().clearSelection();
        cboxDeductionType.getSelectionModel().clearSelection();
        cboxFormula.getSelectionModel().clearSelection();

        txtFrom.setText("");
        txtTo.setText("");
        txtkgfat1.setText("");
        txtFixedPoint.setText("");
        txtStep.setText("");
        txtValue.setText("");
    }

    private void generateRate() {
        if (!memberMilkPurchaseRateBasedList.isEmpty()) {

            MemberMilkPurchaseRate memberMilkPurchaseRate = new MemberMilkPurchaseRate();

            rateType = cboxRateType.getSelectionModel().getSelectedItem();

            MainApp.paneDrop.setVisible(true);
            MainApp.lblMessage.setText("Generating Milk Rate . . . ");

            var task = new GenerateFromParamTask(memberMilkPurchaseRate, memberMilkPurchaseRateBasedList);
            task.setOnSucceeded(e -> {
                try {
                    MemberMilkPurchaseRate memberMilkPurchaseRate1 = task.get();

                    if (memberMilkPurchaseRate1 == null || memberMilkPurchaseRate1.getListDetails() == null)
                        return;

                    listDetails.addAll(memberMilkPurchaseRate1.getListDetails());
                    isRateAdded = true;

                    rateViewDto.setMemberRate(memberMilkPurchaseRate1);
                    MainApp.paneDrop.setVisible(false);
                    btnView.setDisable(false);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            new Thread(task).start();
        }
    }

    private void openRateView() {
        if (rateViewDto != null) {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "RateView", rateViewDto, null);
        } else {
            btnView.setDisable(true);
        }
    }
}
