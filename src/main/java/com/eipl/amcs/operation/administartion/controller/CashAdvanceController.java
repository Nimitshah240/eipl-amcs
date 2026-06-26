package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.master.account.dto.CashAdvanceDto;
import com.eipl.amcs.master.account.model.CashAdvance;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.FetchAllPaymentCycleLoadTask;
import com.eipl.amcs.operation.administartion.task.CashAdvanceDeleteTask;
import com.eipl.amcs.operation.administartion.task.CashAdvanceLoadTask;
import com.eipl.amcs.operation.administartion.task.CashAdvanceSaveTask;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.task.ProductSaleInstallmentByOnlyInvoiceNoLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class CashAdvanceController implements MyInitialization, PopupCallback {

    private final ObjectProperty<CashAdvance> propCashAdvanceDto;
    @FXML
    StackPane root;
    @FXML
    TableView<CashAdvance> tableCashAdvance;
    @FXML
    TableColumn<CashAdvance, String> colCode, colName;
    @FXML
    TableColumn<CashAdvance, BigDecimal> colAmount;
    @FXML
    TableColumn<CashAdvance, String> colNoOfInstallment;
    @FXML
    GridPane gridMaster;
    @FXML
    VBox vbox;
    @FXML
    Button btnClose, btnSave, btnDelete, btnView;
    @FXML
    private TextField txtAmount, txtMemberName, txtMemberCode, txtNoOfInstallment;
    @FXML
    private DatePicker dpDate;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private CashAdvance cashAdvance;
    private Member member;
    private List<SocietyPaymentCycle> paymentCycleList;
    private List<ProductSaleInstallment> installmentList = new ArrayList<>();
    private CashAdvanceDto dto;

    public CashAdvanceController() {
        propCashAdvanceDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSave.setText(resourceBundle.getString("add"));
        dpDate.setValue(LocalDate.now());
        vbox.getChildren().remove(gridMaster);
        this.resourceBundle = resourceBundle;
        btnDelete.setDisable(true);
        btnView.setDisable(true);
        propCashAdvanceDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDelete.setDisable(false);
                btnView.setDisable(false);
            } else {
                btnDelete.setDisable(true);
                btnView.setDisable(true);
            }
        });
        txtNoOfInstallment.setOnAction(e -> {
            FocusUtils.requestFocus(btnSave);
        });
        btnSave.setOnAction(e -> {
            if (btnSave.getText().equalsIgnoreCase(resourceBundle.getString("add"))) {
                vbox.getChildren().add(1, gridMaster);
                FocusUtils.requestFocus(txtMemberCode);
                btnSave.setText(resourceBundle.getString("save"));
            } else {
                checkAndSave();
                btnSave.setText(resourceBundle.getString("add"));
                vbox.getChildren().remove(gridMaster);
            }
        });
        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        txtMemberCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtMemberCode.getText().length() > 0) {
                String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtMemberCode.getText()));
                setMemberName(code);
            }
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });

        btnView.setOnAction(e -> {
            showInstallments();
        });

    }

    private void showInstallments() {
        var task = new ProductSaleInstallmentByOnlyInvoiceNoLoadTask(propCashAdvanceDto.get().getCode());
        task.setOnSucceeded(e -> {
            try {
                installmentList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "InstallmentsAddEdit", installmentList, this,  resourceBundle.getString("installmenttitle"));
        });
        new Thread(task).start();
    }

    private void checkAndSave() {
        checkPaymentCycleAndSaveInstallment();
    }

    private void setMemberName(String text) {
        var task = new MemberByIdLoadTask(text);
        task.setOnSucceeded(e -> {
            try {
                member = task.get();
                if (member != null) {
                    txtMemberName.setText(member.toMemberName());
                    txtNoOfInstallment.setText("1");
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void saveData() {
        setValuesInObject();
        dto = new CashAdvanceDto();
        dto.setCashAdvance(cashAdvance);
        dto.setInstallmentList(installmentList);

        if (!dto.getInstallmentList().get(0).getSocietyPaymentCycle().getLockBillingProcess()) {
            var task = new CashAdvanceSaveTask(dto, (short) 0);
            task.setOnSucceeded(e -> {
                loadData();
                clearControls();
            });
            task.setOnFailed(e -> {
                MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("cashadvance"),
                        resourceBundle.getString("error.occurred"));
                alert.createAlert();
            });
            new Thread(task).start();
        } else {
            MyAlert alert = new WarningAlert(MainApp.stage, resourceBundle.getString("cashadvance"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
            clearControls();
        }
    }

    private void setValuesInObject() {
        cashAdvance = new CashAdvance();
        cashAdvance.setSociety(MainApp.identityDto.getSociety());
        cashAdvance.setUnionCode(MainApp.identityDto.getUnion().getCode());
        cashAdvance.setAmount(new BigDecimal(txtAmount.getText()));
        cashAdvance.setNoOfInstallment(Integer.parseInt(txtNoOfInstallment.getText()));
        cashAdvance.setMember(member);
        cashAdvance.setDate(LocalDate.now());
        cashAdvance.setInstallmentDate(dpDate.getValue());

    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getCode()));
            colNoOfInstallment.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNoOfInstallment().toString()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMember().toMemberName()));

            propCashAdvanceDto.bind(tableCashAdvance.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableCashAdvance);

        } catch (Exception e) {
            System.out.println("CashAdvance setuptable Exception");
            e.printStackTrace();
        }
    }


    private void checkPaymentCycleAndSaveInstallment() {
        try {
            int noOfInstallments = Integer.parseInt(txtNoOfInstallment.getText());
            var task = new FetchAllPaymentCycleLoadTask(dpDate.getValue(), noOfInstallments);
            task.setOnSucceeded(e -> {
                try {
                    paymentCycleList = task.get();
                    if (paymentCycleList.size() >= Integer.parseInt(txtNoOfInstallment.getText())) {
                        prepareInstallment();
                        saveData();
                    } else {
                        MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("societypaymentcycle"),
                                resourceBundle.getString("societypaymentcycle.not.available"));
                        alert.createAlert();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void prepareInstallment() {
        try {
            if (installmentList != null)
                installmentList.clear();
            Integer loopNo = Integer.parseInt(txtNoOfInstallment.getText());
            BigDecimal totalAmount = new BigDecimal(txtAmount.getText());
            BigDecimal noOfInstallment = new BigDecimal(txtNoOfInstallment.getText());
            BigDecimal num = totalAmount.divide(noOfInstallment, RoundingMode.HALF_DOWN);
            BigDecimal actualInstallment = new BigDecimal(txtAmount.getText());
            BigDecimal lastAmount = actualInstallment.subtract(num.multiply(new BigDecimal(loopNo - 1)));
            for (int i = 1; i <= loopNo; i++) {
                ProductSaleInstallment psi = new ProductSaleInstallment();
                psi.setActualInstallment(actualInstallment);
                if (i == loopNo) {
                    psi.setInstallmentAmount(lastAmount);
                } else {
                    psi.setInstallmentAmount(num);
                }
                psi.setDeductionDate(paymentCycleList.get(i - 1).getToDate().toLocalDate());
                psi.setSocietyPaymentCycle(paymentCycleList.get(i - 1));
                psi.setSocietyCode(MainApp.identityDto.getSociety().getCode());
                psi.setUnionCode(MainApp.identityDto.getUnion().getCode());
                psi.setMember(member);
                psi.setBilling(false);
                psi.setType(3);
                if (installmentList == null) {
                    installmentList = new ArrayList<>();
                }
                if (psi != null) {
                    installmentList.add(psi);
                } else {
                    System.err.println("Warning: psi object is null, cannot add to list.");
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("cashadvance"),
                            resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println(" error : " + e);
            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("cashadvance"),
                    resourceBundle.getString("error.occurred"));
            alert1.createAlert();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadData() {
        tableCashAdvance.setItems(null);
        CashAdvanceLoadTask task = new CashAdvanceLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<CashAdvance> list = task.get();
                if (list != null)
                    tableCashAdvance.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("cashadvance"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            CashAdvance dto = propCashAdvanceDto.get();
            if (dto != null) {
                var task = new CashAdvanceDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("cashadvance"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void clearControls() {
        txtMemberCode.setText("");
        txtMemberName.setText("");
        txtNoOfInstallment.setText("");
        txtAmount.setText("");
    }
}
