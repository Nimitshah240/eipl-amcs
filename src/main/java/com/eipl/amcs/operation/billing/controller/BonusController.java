package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
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
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class BonusController extends SocietyPaymentCycleEditController implements MyInitialization, PopupCallback {
    public List<MilkType> milkTypeList = new ArrayList<>();
    BigDecimal qty = BigDecimal.ZERO;
    BigDecimal amt = BigDecimal.ZERO;
    BigDecimal kapaat = BigDecimal.ZERO;
    BigDecimal bonus = BigDecimal.ZERO;
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnSaveUpdate, btnEdit, btnClose, btnExport;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private ComboBox<String> cboxType, cboxCriteria;
    private final StringBuilder errorMsg = null;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    private Stage stage;
    @FXML
    private TextField txtBonusValue;
    @FXML
    private TableView<Bonus> tableBonus;
    @FXML
    private ComboBox<Shift> cboxToShift, cboxFromShift;
    @FXML
    private TableColumn<Bonus, String> colMemberCode, colMemberName, colStatus, colType, colKapaat, colRemarks, colTotal;
    @FXML
    private TableColumn<Bonus, Number> colMilkQty, colMilkAmount, colBonusAmt;
    private BonusSummary bonusSummary;
    private List<Bonus> bonusList;
    private ResourceBundle resourceBundle;
    private final ObjectProperty<Bonus> propBonus;
    private BonusDto dto = null;
    private List<String> criteriaList;
    private List<String> typeList;
    private BigDecimal totalAmt = BigDecimal.ZERO;

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
            dpFromDate.setValue(bonusSummary.getFromDate());
            dpToDate.setValue(bonusSummary.getToDate());
            cboxCriteria.getSelectionModel().select(bonusSummary.getBonusCriteria() == 0 ? 0 : bonusSummary.getBonusCriteria() == 1 ? 1 : 2);
            cboxType.getSelectionModel().select(bonusSummary.getType() == 0 ? 0 : bonusSummary.getType() == 1 ? 1 : 2);
            txtBonusValue.setText(String.valueOf(bonusSummary.getBonusCriteriaValue()));

            loadControls();
        }
    }

    @Override
    public void loadControls() {
        var task = new BonusListLoadTask(bonusSummary.getCode());
        task.setOnSucceeded(event -> {
            try {
                dto = task.get();
                bonusList = dto.getBonusList();
                tableBonus.setItems(FXCollections.observableList(bonusList));
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
//            if (!MainApp.user.getPermissions().contains("ACTION_BONUS_GENERATE"))
//                throw new UnAuthorizedAccessException();
            if (bonusSummary != null) {
                MyAlert calert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("bonus"), resourceBundle.getString("prevdatawillbedeleted"));
                Optional<ButtonType> resp = calert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    try {
                        if (dpFromDate.getValue() != null && dpToDate.getValue() != null && cboxType.getValue() != null && cboxCriteria.getValue() != null && txtBonusValue.getText() != null && Double.parseDouble(txtBonusValue.getText()) > 0)
                            loadData(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()), CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()), cboxType.getValue(), cboxCriteria.getValue(), txtBonusValue.getText(), cboxMilkType.getValue());
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
                    if (dpFromDate.getValue() != null && dpToDate.getValue() != null && cboxType.getValue() != null && cboxCriteria.getValue() != null && txtBonusValue.getText() != null && Double.parseDouble(txtBonusValue.getText()) > 0)
                        loadData(CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()), CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()), cboxType.getValue(), cboxCriteria.getValue(), txtBonusValue.getText(), cboxMilkType.getValue());
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

//        propBonus.addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                btnEdit.setDisable(false);
//            } else {
//                btnEdit.setDisable(true);
//            }
//        });


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
        if (criteria.equalsIgnoreCase(resourceBundle.getString("percentage"))) {
            for (Bonus b : bonusList) {
                bonus = b.getMilkAmount().multiply(new BigDecimal(bonusValue)).divide(BigDecimal.valueOf(100));
                b.setBonusAmount(bonus);
                b.setType(cboxType.getValue().equalsIgnoreCase(resourceBundle.getString("unionbonus")) ? (short) 0 : (short) 1);
                b.setSociety(MainApp.identityDto.getSociety());
                b.setUnion(MainApp.identityDto.getUnion());
            }
            tableBonus.setItems(FXCollections.observableList(bonusList));
        } else if (criteria.equalsIgnoreCase(resourceBundle.getString("rs"))) {
            for (Bonus b : bonusList) {
                bonus = b.getMilkQty().multiply(new BigDecimal(bonusValue));
                b.setBonusAmount(bonus);
                b.setType(cboxType.getValue().equalsIgnoreCase(resourceBundle.getString("unionbonus")) ? (short) 0 : (short) 1);
                b.setSociety(MainApp.identityDto.getSociety());
                b.setUnion(MainApp.identityDto.getUnion());
            }
            tableBonus.setItems(FXCollections.observableList(bonusList));
        } else {

            for (Bonus b1 : bonusList) {
                totalAmt = totalAmt.add(b1.getMilkAmount());
            }
            for (Bonus b : bonusList) {
                bonus = new BigDecimal(bonusValue).divide(totalAmt, 25, RoundingMode.HALF_DOWN);
                b.setBonusAmount(bonus.multiply(b.getMilkAmount()));
                b.setType(cboxType.getValue().equalsIgnoreCase(resourceBundle.getString("unionbonus")) ? (short) 0 : (short) 1);
                b.setSociety(MainApp.identityDto.getSociety());
                b.setUnion(MainApp.identityDto.getUnion());
            }
            tableBonus.setItems(FXCollections.observableList(bonusList));
        }
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
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
    }

    @Override
    public void setupTable() {
        try {
            colMemberCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getCode()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getFirstName()));
            colBonusAmt.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBonusAmount().setScale(2,
                    RoundingMode.HALF_DOWN)));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() == 0 ? "PENDING" : "DONE"));
            colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() == 0 ? "Union Bonus" : "Society Bonus"));
            colKapaat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getxCol1()));
            colKapaat.setCellFactory(TextFieldTableCell.forTableColumn());
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

            colTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getxCol3()));
            colMilkQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQty()));
            colMilkAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkAmount()));
            propBonus.bind(tableBonus.getSelectionModel().selectedItemProperty());

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
            dto.getBonusSummary().setBonusCriteriaValue(new BigDecimal(txtBonusValue.getText()));
            dto.getBonusSummary().setFromDate(dpFromDate.getValue());
            dto.getBonusSummary().setToDate(dpToDate.getValue());
            for (Bonus b : bonusList) {
                b.selectedProperty().setValue(true);
                qty = qty.add(b.getMilkQty());
                amt = amt.add(b.getMilkAmount());
                kapaat = kapaat.add(new BigDecimal(b.getxCol1() != null ? b.getxCol1() : "0"));
                bonus = bonus.add(b.getBonusAmount());
                if (b.getxCol1() == null) {
                    b.setxCol1("0");
                    b.setxCol3("0");
                }
            }
            if (cboxCriteria.getValue().equalsIgnoreCase(resourceBundle.getString("total"))) {
                dto.getBonusSummary().setBonusCriteriaAmount(new BigDecimal(txtBonusValue.getText()));
            } else {
                dto.getBonusSummary().setBonusCriteriaAmount(bonus);
            }

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
            bs.setBonusCriteriaValue(new BigDecimal(txtBonusValue.getText()));
            bs.setSociety(MainApp.identityDto.getSociety());
            bs.setUnion(MainApp.identityDto.getUnion());
            bs.setxCol1(String.valueOf(cboxMilkType.getValue().getCode()));
            for (Bonus b : bonusList) {
                b.selectedProperty().setValue(true);
                qty = qty.add(b.getMilkQty());
                amt = amt.add(b.getMilkAmount());
                bonus = bonus.add(b.getBonusAmount());
                kapaat = kapaat.add(new BigDecimal(b.getxCol1() != null ? b.getxCol1() : "0"));
                if (b.getxCol1() == null) {
                    b.setxCol1("0");
                    b.setxCol3("0");
                }
            }
            if (cboxCriteria.getValue().equalsIgnoreCase(resourceBundle.getString("total"))) {
                bs.setBonusCriteriaAmount(new BigDecimal(txtBonusValue.getText()));
            } else {
                bs.setBonusCriteriaAmount(bonus);
            }
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
