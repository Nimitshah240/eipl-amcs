package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_Label;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkQualityTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.procurement.dto.CollectionEditDelete;
import com.eipl.amcs.operation.procurement.dto.MemberSocietyInfoDto;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.*;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MilkCollectionEditDeleteController extends MilkCollectionBaseController implements MyInitialization {
    private final ObjectProperty<MilkCollection> propCollection = new SimpleObjectProperty<>();
    public String text = "";
    BigDecimal oldQty = BigDecimal.ZERO;
    BigDecimal oldAmount = BigDecimal.ZERO;
    @FXML
    private StackPane root;
    @FXML
    private TextField txtName;
    @FXML
    private E_NumericField txtCode, txtFat, txtSnf, txtClr, txtQuantity, txtRtpl, txtAmount;
    @FXML
    private E_DatePicker dpDate;
    @FXML
    private HBox hbox;
    @FXML
    private E_Label lblTitle;
    @FXML
    private AutoSearchTextField<Shift> cboxShift;
    @FXML
    private AutoSearchTextField<MilkType> cboxType;
    @FXML
    private AutoSearchTextField<MilkQualityType> cboxQualityType;
    @FXML
    private GridPane gridCollection;
    @FXML
    private TableView<MilkCollection> tableCollection;
    @FXML
    private TableColumn<MilkCollection, MilkType> colMilkType;
    @FXML
    private TableColumn<MilkCollection, Number> colSampleNo, colMemberCode, colQty, colFat, colSnf, colRate, colAmount;
    @FXML
    private Button btnEdit, btnClose, btnDelete;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private MemberSocietyInfoDto memberSocietyInfoDto;
    private MilkCollection milkCollection;
    private StringBuilder errorMsg = null;
    private String operation = "UPDATE";
    private final ChangeListener<String> qualityParamChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            fetchRate(txtFat.getInputText(), txtSnf.getInputText(), cboxType.getValue(), cboxQualityType.getValue());
            calculateClr(txtFat.getInputText(), txtSnf.getInputText());
        }
    };
    private final ChangeListener<String> qtyRateChangeListener = (observableValue, oldVal, newVal) -> {
        if (!newVal.isEmpty()) {
            calculateAmount(txtRtpl.getInputText(), txtQuantity.getInputText());
        }
    };

    private Boolean doubleDock;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void getCollectionDate(LocalDateTime collectionDate) {
        this.collectionDate = collectionDate;
        if (collectionDate != null) {
            dpDate.setValue(collectionDate.toLocalDate());
            var task = new ShiftLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Shift> list = task.get();
                    if (list != null) {
                        List<Shift> list1 = CommonUtils.removeAllShift(list);
                        cboxShift.setItems(FXCollections.observableList(list1));
                        if (collectionDate.getHour() == 6) {
                            cboxShift.getSelectionModel().select(0);
                        } else {
                            cboxShift.getSelectionModel().select(1);
                        }
                        loadAndCheckPreRequisite();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        doubleDock = !MainApp.identityDto.getDock().getDockNo().substring(MainApp.identityDto.getSociety().getCode().length()).equals("01");

        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        gridCollection.setDisable(true);
        setupComboBox();
        setupTable();
        loadData();
        cboxType.setOnAction(e -> {
            fetchRate(txtFat.getInputText(), txtSnf.getInputText(), cboxType.getValue(), cboxQualityType.getValue());
            calculateClr(txtFat.getInputText(), txtSnf.getInputText());
        });
        txtFat.textProperty().addListener(qualityParamChangeListener);
        txtSnf.textProperty().addListener(qualityParamChangeListener);
        txtQuantity.textProperty().addListener(qtyRateChangeListener);
        txtRtpl.textProperty().addListener(qtyRateChangeListener);
        txtCode.setOnAction(e -> fetchMemberSocietyDetails());
        txtFat.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                FocusUtils.requestFocus(btnEdit);
                FocusUtils.requestFocus(btnDelete);
            }
        });
        txtCode.focusedProperty().addListener((ob, oldVal, newVal) -> {
            if (!newVal) fetchMemberSocietyDetails();
            if (btnDelete != null) FocusUtils.requestFocus(btnDelete);
        });
        propCollection.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            }
        });
        btnEdit.setOnAction(e -> {
            if (btnEdit.getText().equalsIgnoreCase(resourceBundle.getString("update"))) {
                validateAndUpdate();
            } else {
                if (propCollection.get() != null) {
                    milkCollection = propCollection.get();
                    setValuesInControls(milkCollection);
                    btnEdit.setText(resourceBundle.getString("update"));
                }
            }
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnClose.setOnAction(e -> this.stage.close());

        root.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    this.stage.close();
                    break;
            }
        });

    }

    private void loadAndCheckPreRequisite() {
        var task = new MilkCollectionPreRequisiteTask(collectionDate, cboxShift.getValue(), MainApp.identityDto.getSociety());
        task.setOnSucceeded(e -> {
            try {
                collectionPreReqDto = task.get();
                if (collectionPreReqDto != null) {
                    cboxQualityType.setDisable(true);
                    cboxType.setDisable("0".equals(MainApp.getProperty(AppConstant.Props.ACCEPT_MILK_OTHERTHAN_DEFAULT_MILKTYPE, "1")));
                    fetchRateDetails();
                    FocusUtils.requestFocus(txtCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    protected void fetchRateDetails() {
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

    @Override
    public void loadData() {
        tableCollection.setItems(null);
        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null) {
                    cboxType.setItems(FXCollections.observableList(list));
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
                    cboxQualityType.setItems(FXCollections.observableList(list));
                    cboxQualityType.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    @Override
    public void setupTable() {
        try {
            colSampleNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSampleNo()));
            colMemberCode.setCellValueFactory(data -> new SimpleIntegerProperty(CommonUtils.strToInteger(data.getValue().getMember().getCodeEx())));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
            colFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFat()));
            colSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSnf()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRtpl()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            TableLocalizationUtil.localizeTable(tableCollection);
            propCollection.bind(tableCollection.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setupComboBox() {
        dpDate.setConverter(new LocalDateConvertor());
        dpDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDate.setValue(dpDate.getConverter().fromString(dpDate.getEditor().getText()));
            }
        });
    }

    public void setValuesInControls(MilkCollection milkCollection) {
        if (milkCollection != null) {
            cboxType.setValue(milkCollection.getMilkType());
            cboxQualityType.setValue(milkCollection.getMilkQualityType());
            txtFat.setText(milkCollection.getFat().toString());
            txtSnf.setText(milkCollection.getSnf().toString());
            txtClr.setText(milkCollection.getClr().toString());
            txtQuantity.setText(milkCollection.getQty().toString());
            oldQty = milkCollection.getQty();
            txtRtpl.setText(milkCollection.getRtpl().toString());
            txtAmount.setText(milkCollection.getAmount().toString());
            oldAmount = milkCollection.getAmount();
        }
    }

    private void validateAndUpdate() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnEdit.getText().equals(resourceBundle.getString("update"))) {

            setValuesInObject();
//            if (MainApp.timingList != null && !MainApp.timingList.isEmpty()) {
//                LocalDateTime fromDate = LocalDateTime.parse(MainApp.timingList.get(0).getFromDate() + " " + MainApp.timingList.get(0).getxCol1(), AppConstant.Formatter6);
//                LocalDateTime toDate = LocalDateTime.parse(MainApp.timingList.get(0).getToDate() + " " + MainApp.timingList.get(0).getxCol2(), AppConstant.Formatter6);
//                LocalDateTime currentDate = CommonUtils.getLocalDateTimeFromDateAndShift(dpDate.getValue(), cboxShift.getValue());
//
//                boolean isBetweenInclusive = (currentDate.isEqual(fromDate) || currentDate.isAfter(fromDate)) &&
//                        (currentDate.isEqual(toDate) || currentDate.isBefore(toDate));
//
//                if (isBetweenInclusive) {
//                    MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("farmervoting"));
//                    Optional<ButtonType> resp = alert.createYesNoConfirmationAlert();
//                    if (resp.isPresent() && resp.get() == ButtonType.YES) {
//                        this.milkCollection.setxCol4("Y");
//                        alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("appreciationfarmerforvoting"));
//                        resp = alert.createYesNoConfirmationAlert();
//                        if (resp.isPresent() && resp.get() == ButtonType.YES) {
//                            MainApp.isIncentive = true;
//                            MainApp.incentiveValue = MainApp.timingList.get(0).getIncRate() <= 0 ? 1 : MainApp.timingList.get(0).getIncRate();
//                            this.milkCollection.setAmount(new BigDecimal(txtAmount.getInputText()).add((BigDecimal.valueOf(MainApp.incentiveValue)).multiply(this.milkCollection.getQty())));
//                            this.milkCollection.setxCol5(this.milkCollection.getQty() + "#" + BigDecimal.valueOf(MainApp.incentiveValue).multiply(this.milkCollection.getQty()));
//                        }
//                    }
//                }
//            }
            updateData();
        }
    }

    private void setValuesInObject() {
        if (tableCollection.getItems().size() == 1) this.milkCollection = tableCollection.getItems().get(0);

        if (this.milkCollection != null) {
            milkCollection.setMilkType(cboxType.getValue());
            milkCollection.setMilkQualityType(cboxQualityType.getValue());
            milkCollection.setFat(new BigDecimal(txtFat.getInputText()));
            milkCollection.setSnf(new BigDecimal(txtSnf.getInputText()));
            milkCollection.setClr(new BigDecimal(txtClr.getInputText()));
            milkCollection.setQty(new BigDecimal(txtQuantity.getInputText()));
            milkCollection.setRtpl(new BigDecimal(txtRtpl.getInputText()));
            milkCollection.setAmount(new BigDecimal(txtAmount.getInputText()));
            milkCollection.setUpdatedBy(MainApp.identityDto.getSociety().getCode());

            milkCollection.setxCol1(new BigDecimal(milkCollection.getxCol1()).subtract(oldQty).add(new BigDecimal(txtQuantity.getInputText())).toString());
            milkCollection.setxCol2(new BigDecimal(milkCollection.getxCol2()).subtract(oldAmount).add(new BigDecimal(txtAmount.getInputText())).toString());

            milkCollection.setQualityAuto(false);
            milkCollection.setWeightAuto(false);
        }
    }

    private boolean validate() {
        if (txtCode.getInputText() == null || txtCode.getInputText().isEmpty())
            errorMsg.append(resourceBundle.getString("membercode.cannot.be.null") + "\n");
        if (txtQuantity.getInputText() == null || txtQuantity.getInputText().isEmpty())
            errorMsg.append(resourceBundle.getString("qty.cannot.be.null") + "\n");
        if (txtFat.getInputText() == null || txtFat.getInputText().isEmpty())
            errorMsg.append(resourceBundle.getString("fat.cannot.be.null") + "\n");
        if (txtSnf.getInputText() == null || txtSnf.getInputText().isEmpty())
            errorMsg.append(resourceBundle.getString("snf.cannot.be.null") + "\n");
        if (txtRtpl.getInputText() == null || txtRtpl.getInputText().isEmpty())
            errorMsg.append(resourceBundle.getString("rate.cannot.be.null") + "\n");
        if (txtAmount.getInputText() == null || txtAmount.getInputText().isEmpty())
            errorMsg.append(resourceBundle.getString("amount.cannot.be.null") + "\n");
        if (!CommonUtils.isNumeric(txtFat.getInputText()) && Integer.parseInt(txtFat.getInputText()) == 0)
            errorMsg.append(resourceBundle.getString("invalid.fat") + "\n");
        if (!CommonUtils.isNumeric(txtSnf.getInputText()) && Integer.parseInt(txtSnf.getInputText()) == 0)
            errorMsg.append(resourceBundle.getString("invalid.snf") + "\n");
        if (!CommonUtils.isNumeric(txtQuantity.getInputText()) && Integer.parseInt(txtQuantity.getInputText()) == 0)
            errorMsg.append(resourceBundle.getString("invalid.qty") + "\n");
        if (!CommonUtils.isNumeric(txtRtpl.getInputText()) && Integer.parseInt(txtRtpl.getInputText()) == 0)
            errorMsg.append(resourceBundle.getString("invalid.rate") + "\n");
        if (!CommonUtils.isNumeric(txtAmount.getInputText()) && Integer.parseInt(txtAmount.getInputText()) == 0)
            errorMsg.append(resourceBundle.getString("invalid.amount") + "\n");

        return errorMsg.length() == 0;
    }

    @Override
    public void updateData() {
        if (!this.milkCollection.getSocietyPaymentCycle().getLockBillingProcess()) {
            var task = new MilkCollectionSaveTask(this.milkCollection, (short) 1, doubleDock);
            writeEditCollection();
            task.setOnSucceeded(e -> {
                try {
                    Object obj = task.get();
                    if (obj instanceof ApiError) {
                        ApiError error = (ApiError) obj;
                        StringBuilder sb = new StringBuilder();

                        for (ApiValidationError subError : error.getSubErrors()) {
                            sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                        }
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), sb.toString());
                        alert.createAlert();
                        return;
                    }
                    MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("record.update.successful"));
                    alert.createAlert();
                    this.callback.reloadData(true);
                    this.stage.close();

                    clearControls();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else {
            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("paymentcyclenotfound"));
            alert1.createAlert();
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            MilkCollection dto = tableCollection.getItems().size() == 1 ? tableCollection.getItems().get(0) : propCollection.get();
            if (dto != null && !dto.getSocietyPaymentCycle().getLockBillingProcess()) {
                writeDeleteCollection();
                var task = new MilkCollectionDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        MyAlert alert1 = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("record.delete.successful"));
                        alert1.createAlert();
                        this.callback.reloadData(true);
                        this.stage.close();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            } else {
                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"), resourceBundle.getString("paymentcyclenotfound"));
                alert1.createAlert();
            }
        }
    }


    private void writeDeleteCollection() {
        try {
            MilkCollection collection = tableCollection.getItems().size() == 1 ? tableCollection.getItems().get(0) : propCollection.get();
            text = collection.getSampleNo() + "#" + collection.getCollectionDate() + "#" + collection.getFat() + "#" + collection.getSnf() + "#" + collection.getClr() + "#" + collection.getWater() + "#" + collection.getDensity() + "#" + collection.getLectose() + "#" + collection.getProtein() + "#" + collection.getRtpl() + "#" + collection.getQty() + "#" + collection.getAmount() + "#" + collection.isWeightAuto() + "#" + collection.isQualityAuto() + "#" + collection.isAvgParam() + "#" + collection.getQualityAt() + "#" + collection.getWeightAt() + "#" + collection.getRateCode() + "#" + collection.getUnionCode() + "#" + collection.getQtyMode() + "#" + collection.getConvertedQty() + "#" + collection.getConvertedQtyMode() + "#" + collection.getSocietyPaymentCycle().getCode() + "#" + collection.getMember().getCode() + "#" + collection.getShift().getCode() + "#" + collection.getMilkType().getCode() + "#" + collection.getMilkQualityType().getCode() + "#" + collection.getSociety().getCode() + "#" + collection.getDock().getDockNo() + "#" + collection.getxCol1() + "#" + collection.getxCol2() + "#" + collection.getxCol3();
            File directory = new File((MainApp.getProperty("backuppath", "") + "1").replace(" ", ""));
            File file = new File(directory.getAbsolutePath() + "/" + CommonUtils.getLocalDateTimeFromDateAndShiftText(dpDate.getValue(), cboxShift.getValue()) + ".txt");
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            List<String> lines = new ArrayList<>();
            List<String> lines1 = new ArrayList<>();
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write("");
            printWriter.close();
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(writer);
            for (String s : lines) {
                String[] ss = s.split("#");
                if (!ss[0].equalsIgnoreCase(text.split("#")[0])) {
                    lines1.add(s);
                }
            }
            for (String s : lines1) {
                bw.write(s);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeEditCollection() {
        try {

            MilkCollection collection = this.milkCollection;
            text = collection.getSampleNo() + "#" + collection.getCollectionDate() + "#" + collection.getFat() + "#" + collection.getSnf() + "#" + collection.getClr() + "#" + collection.getWater() + "#" + collection.getDensity() + "#" + collection.getLectose() + "#" + collection.getProtein() + "#" + collection.getRtpl() + "#" + collection.getQty() + "#" + collection.getAmount() + "#" + collection.isWeightAuto() + "#" + collection.isQualityAuto() + "#" + collection.isAvgParam() + "#" + collection.getQualityAt() + "#" + collection.getWeightAt() + "#" + collection.getRateCode() + "#" + collection.getUnionCode() + "#" + collection.getQtyMode() + "#" + collection.getConvertedQty() + "#" + collection.getConvertedQtyMode() + "#" + collection.getSocietyPaymentCycle().getCode() + "#" + collection.getMember().getCode() + "#" + collection.getShift().getCode() + "#" + collection.getMilkType().getCode() + "#" + collection.getMilkQualityType().getCode() + "#" + collection.getSociety().getCode() + "#" + collection.getDock().getDockNo() + "#" + collection.getxCol1() + "#" + collection.getxCol2() + "#" + collection.getxCol3();
            File directory = new File((MainApp.getProperty("backuppath", "") + "1").replace(" ", ""));
            File file = new File(directory.getAbsolutePath() + "/" + CommonUtils.getLocalDateTimeFromDateAndShiftText(dpDate.getValue(), cboxShift.getValue()) + ".txt");
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            List<String> lines = new ArrayList<>();
            List<String> lines1 = new ArrayList<>();
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write("");
            printWriter.close();
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(writer);
            for (String s : lines) {
                String[] ss = s.split("#");
                if (!ss[0].equalsIgnoreCase(text.split("#")[0])) {
                    lines1.add(s);
                }
            }
            for (String s : lines1) {
                bw.write(s);
                bw.newLine();
            }
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void fetchMemberSocietyDetails() {
        if (txtCode.getInputText().isEmpty()) return;
        String code = MainApp.identityDto.getSociety().getCode() + CommonUtils.getMemberShortCode(txtCode.getInputText());
        var task = new MemberSocietyInfoLoadTask(code, collectionDate);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj == null) return;

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
                    txtName.setText(memberSocietyInfoDto.getMember().toMemberName());
                    cboxType.getSelectionModel().select(memberSocietyInfoDto.getMember().getMilkType());
                    fetchCollection(collectionDate, memberSocietyInfoDto.getMember().getCode());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void fetchCollection(LocalDateTime collectionDate, String code) {
        var task1 = new MilkCollectionFromMemberLoadTask(collectionDate, code);
        task1.setOnSucceeded(e -> {
            try {
                List<MilkCollection> list = task1.get();
                if (list != null) {
                    tableCollection.setItems(FXCollections.observableList(list));
                    if (list.size() == 1) {
                        gridCollection.setDisable(false);
                        setValuesInControls(list.get(0));
                        btnEdit.setDisable(false);
                        btnDelete.setDisable(false);
                        btnEdit.setText(resourceBundle.getString("update"));
                    }
                    FocusUtils.requestFocus(cboxType);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

    @Override
    protected void setRate(String rate) {
        txtRtpl.setText(rate);
    }

    @Override
    protected void setAmount(String amount) {
        txtAmount.setText(amount);
    }

    @Override
    protected String getQty() {
        return txtQuantity.getInputText() == null || txtQuantity.getInputText().isEmpty() ? "0" : txtQuantity.getInputText();
    }

    @Override
    protected void setQty(String qty) {
        txtQuantity.setText(qty);
    }

    @Override
    protected String getFat() {
        return txtFat.getInputText() == null || txtFat.getInputText().isEmpty() ? "0" : txtFat.getInputText();
    }

    @Override
    protected void setFat(String fat) {
        txtFat.setText(fat);
    }

    @Override
    protected String getSnf() {
        return txtSnf.getInputText() == null || txtSnf.getInputText().isEmpty() ? "0" : txtSnf.getInputText();
    }

    @Override
    protected void setSnf(String snf) {
        txtSnf.setText(snf);
    }

    @Override
    protected String getWater() {
        return null;
    }

    @Override
    protected void setWater(String water) {

    }

    @Override
    protected String getClr() {
        return txtClr.getInputText() == null || txtClr.getInputText().isEmpty() ? "0" : txtClr.getInputText();
    }

    @Override
    protected void setClr(String clr) {
        txtClr.setText(clr);
    }

    @Override
    protected void setupAutoManualControls() {

    }

    @Override
    protected MilkType getMilkType() {
        return null;
    }

    @Override
    protected void setMilkType(MilkType milkType) {

    }

    @Override
    protected String getSampleNo() {
        return null;
    }

    @Override
    protected void setSampleNo(String sampleNo) {

    }

    @Override
    protected String getFat1() {
        return null;
    }

    @Override
    protected void setFat1(String fat) {
        txtFat.setText(fat);
    }

    @Override
    protected String getSnf1() {
        return null;
    }

    @Override
    protected void setSnf1(String snf) {
        txtSnf.setText(snf);
    }

    @Override
    protected String getWater1() {
        return null;
    }

    @Override
    protected void setWater1(String water) {

    }

    @Override
    protected String getFat2() {
        return null;
    }

    @Override
    protected void setFat2(String fat) {
        txtFat.setText(fat);

    }

    @Override
    protected String getSnf2() {
        return null;
    }

    @Override
    protected void setSnf2(String snf) {
        txtSnf.setText(snf);

    }

    @Override
    protected String getWater2() {
        return null;
    }

    @Override
    protected void setWater2(String water) {

    }

    @Override
    protected String getFat3() {
        return null;
    }

    @Override
    protected void setFat3(String fat) {
        txtFat.setText(fat);

    }

    @Override
    protected String getSnf3() {
        return null;
    }

    @Override
    protected void setSnf3(String snf) {
        txtSnf.setText(snf);

    }

    @Override
    protected String getWater3() {
        return null;
    }

    @Override
    protected void setWater3(String water) {

    }

    @Override
    protected String getFat4() {
        return null;
    }

    @Override
    protected void setFat4(String fat) {
        txtFat.setText(fat);

    }

    @Override
    protected String getSnf4() {
        return null;
    }

    @Override
    protected void setSnf4(String snf) {
        txtSnf.setText(snf);

    }

    @Override
    protected String getWater4() {
        return null;
    }

    @Override
    protected void setWater4(String water) {

    }

    @Override
    protected void bindFatForAuto() {

    }

    @Override
    protected void bindSnfForAuto() {

    }

    @Override
    protected void unbindFatForAuto() {

    }

    @Override
    protected void unbindSnfForAuto() {

    }

    @Override
    protected void setHardwarePanelDisable() {

    }

    public void setCollectionEditDelete(CollectionEditDelete object) {
        if (object != null) {
            this.collectionDate = object.getCollectionDate();
            this.operation = object.getOperation();
            if (this.operation.equalsIgnoreCase("UPDATE")) {
                hbox.getChildren().remove(btnDelete);
            } else {
                hbox.getChildren().remove(btnEdit);
            }
            if (collectionDate != null) {
                dpDate.setValue(collectionDate.toLocalDate());
                var task = new ShiftLoadTask();
                task.setOnSucceeded(e -> {
                    try {
                        List<Shift> list = task.get();
                        if (list != null) {
                            List<Shift> list1 = CommonUtils.removeAllShift(list);
                            cboxShift.setItems(FXCollections.observableList(list1));
                            if (collectionDate.getHour() == 6) {
                                cboxShift.getSelectionModel().select(0);
                            } else {
                                cboxShift.getSelectionModel().select(1);
                            }
                            loadAndCheckPreRequisite();
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();

            }
        }
    }

}
