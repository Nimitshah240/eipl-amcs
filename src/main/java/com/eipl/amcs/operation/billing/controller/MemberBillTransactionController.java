package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;
import com.eipl.amcs.operation.billing.task.MemberBillTransactionLoadTask;
import com.eipl.amcs.operation.billing.task.MemberBillTransactionSaveTask;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MemberBillTransactionController implements MyInitialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberBillTransactionController.class);
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnUpdate;
    @FXML
    private TableColumn<MemberBillTransaction, Number> colPrevDue, colTotalAmount, colValue, colDue;
    @FXML
    private TableColumn<MemberBillTransaction, String> colParticulars, colType, colAdjusted;
    @FXML
    private Label lblAmount, lblMember, lblPaymentCycle, lblQty;
    @FXML
    private TableView<MemberBillTransaction> tableMemberBillTransaction;
    @FXML
    private TextField txtDue, txtNetPayable;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private final StringBuilder errorMsg = null;
    private List<MemberBillTransaction> listTransaction;
    private PopupCallback callback;
    private final ObjectProperty<MemberBillTransaction> propMemberBillTransaction;
    private final BigDecimal actualPayable = BigDecimal.ZERO;
    private BigDecimal netPayable = BigDecimal.ZERO;
    private final BigDecimal adjustment = BigDecimal.ZERO;
    private BigDecimal due = BigDecimal.ZERO;
    private final List<MemberBillTransaction> txn = null;
    private final List<MemberBillTransaction> billOtherTxn = null;
    private MemberBillTransaction txnNetPay;
    private MemberBill memberBill = null;

    public MemberBillTransactionController() {
        propMemberBillTransaction = new SimpleObjectProperty<>();
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        btnClose.setOnAction(e -> this.stage.close());
        btnUpdate.setOnAction(e -> updateData());
        propMemberBillTransaction.addListener((observable, oldValue, newValue) -> {
            btnUpdate.setDisable(newValue == null);
        });
    }

    public void setupTable() {
        try {
            colParticulars.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBillHead().getName()));
            colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType() == (short) 1 ? "Addition" : "Deduction"));
            colAdjusted.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdjustment().toString()));
            colAdjusted.setCellFactory(TextFieldTableCell.forTableColumn());
            colAdjusted.setOnEditCommit(event -> {
                if (event.getRowValue().getBillHead().getAllowAdjustment() == (short) 0) {
                    throw new IllegalArgumentException("Invalid Value!");
                } else {
                    try {
                        MemberBillTransaction w = event.getRowValue();
                        if (new BigDecimal(event.getNewValue()).compareTo(w.getAmount()) > 0) {
                            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member.bill.transaction"),
                                    resourceBundle.getString("adjustment.greaterthan.amount"));
                            alert.createAlert();
                            throw new IllegalArgumentException();
                        } else {
                            w.setAdjustment(new BigDecimal(event.getNewValue()));
                            w.setDue(w.getAmount().add(w.getPrevDue()).subtract(w.getAdjustment()));
                            setControls();
                        }
                    } catch (Exception e) {
//                    throw new E("Invalid Value!");
                    }
                }
            });
            colPrevDue.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrevDue()));
            colValue.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colTotalAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount().add(data.getValue().getPrevDue())));
            colDue.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDue()));
            propMemberBillTransaction.bind(tableMemberBillTransaction.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("MemberBillTransaction setuptable Exception");
            e.printStackTrace();
        }
    }

    private void setControls() {
        if (listTransaction == null)
            return;
        due = BigDecimal.ZERO;
        netPayable = BigDecimal.ZERO;

        for (MemberBillTransaction mb : listTransaction) {
            due = due.add(mb.getDue());
            if (mb.getType() == (short) 1) {
                netPayable = netPayable.add(mb.getAdjustment());
            } else {
                netPayable = netPayable.subtract(mb.getAdjustment());
            }
        }
        txtNetPayable.setText(netPayable.toString());
        txtDue.setText(due.toString());

        tableMemberBillTransaction.refresh();
    }

    public void setMemberBill(MemberBill memberBill) {
        this.memberBill = memberBill;
        lblMember.setText(memberBill.getMember().toMemberName());
        lblPaymentCycle.setText(memberBill.getPaymentCycle().toDateShiftString());
        lblQty.setText(memberBill.getMilkQty().toString());
        lblAmount.setText(memberBill.getMilkAmount().toString() != null ? memberBill.getMilkAmount().toString() : "0");
        System.out.println(memberBill);
        loadData();
    }


    @Override
    public void loadData() {
        var task = new MemberBillTransactionLoadTask(memberBill.getCode());
        task.setOnSucceeded(e -> {
            try {
                List<MemberBillTransaction> list = task.get();
                if (list != null) {
                    listTransaction = new ArrayList<>();
                    for (MemberBillTransaction memberBillTransaction : list) {
                        if (memberBillTransaction.getBillHead().getCode().equalsIgnoreCase("105")) {
                            txnNetPay = memberBillTransaction;
                            continue;
                        }
                        listTransaction.add(memberBillTransaction);
                    }
                    tableMemberBillTransaction.setItems(FXCollections.observableList(listTransaction));
                    setControls();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void updateData() {
//        txnNetPay.setAmount(new BigDecimal(txtNetPayable.getText()));

        if (memberBill.getPaymentCycle().getLockBillingProcess()) {
            MyAlert alert = new WarningAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
            return;
        }

        txnNetPay.setAdjustment(new BigDecimal(txtNetPayable.getText()));
        listTransaction.add(txnNetPay);
        var task = new MemberBillTransactionSaveTask(listTransaction);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member.bill.transaction"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member.bill.transaction"),
                        resourceBundle.getString("transaction.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

}

