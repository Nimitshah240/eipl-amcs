package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.procurement.task.MemberBillLoadByDateTask;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.task.MemberBillSummaryLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MemberBillSummaryController implements MyInitialization, PopupCallback {

    private final ObjectProperty<MemberBillSummary> propSummary;
    @FXML
    private StackPane root;
    @FXML
    private TableView<MemberBillSummary> tableBillSummary;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private TableColumn<MemberBillSummary, String> colPaymentCycle;
    @FXML
    private TableColumn<MemberBillSummary, LocalDate> colFromDate, colToDate;
    @FXML
    private TableColumn<MemberBillSummary, Number> colMilkQty, colMilkAmount, colProductSale, colLocalSale, colLoan,
            colOtherAdd, colOtherDed, colNetAmount, colDisbursedAmount;
    @FXML
    private TableColumn<MemberBillSummary, String> colStatus;
    @FXML
    private Button btnAdd, btnEdit, btnClose, btnPaymentRegister, btnGeneral, btnSearch;
    @FXML
    private RadioButton rbtnCash, rbtnBank;
    @FXML
    private ToggleGroup paymentMode;
    private ResourceBundle resourceBundle;

    public MemberBillSummaryController() {
        propSummary = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        rbtnCash.setSelected(true);
        loadData();
        setupTable();
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
        btnSearch.setOnAction(e -> loadData());
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_BILL_SUMMARY_ADD"))
                throw new UnAuthorizedAccessException();
            MemberBillController controller = (MemberBillController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/MemberBill.fxml"));
            controller.setBillSummary(null);
            MainApp.getContentPane().setCenter(controller.getRoot());
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_BILL_SUMMARY_EDIT"))
                throw new UnAuthorizedAccessException();
            if (propSummary.get() != null) {
                MemberBillController controller = (MemberBillController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/operation/billing/MemberBill.fxml"));
                controller.setBillSummary(propSummary.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            }
        });
        propSummary.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnGeneral.setDisable(false);
                btnPaymentRegister.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnGeneral.setDisable(true);
                btnPaymentRegister.setDisable(true);
            }
        });
        btnPaymentRegister.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnGeneral.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "GeneralReportPopup", propSummary.get(), this);
        });
    }

    private void validateAndGenerateReport() {

        if (rbtnCash.isSelected()) {
            Map<String, Object> params = new HashMap<>();
            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
            params.put("p_society_payment_cycle_code", propSummary.get().getPaymentCycle().getCode());
            params.put("p_payment_mode", 0);
            params.put("p_locale", MainApp.locale);
            JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PAYMENT_REGISTER_CASH, params);
            JasperViewer.viewReport(print, false);
        } else if (rbtnBank.isSelected()) {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "BankReportPopup", propSummary.get(), this);
        }
    }

    @Override
    public void setupTable() {
        try {
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPaymentCycle().getFromDate().toLocalDate()));
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPaymentCycle().getToDate().toLocalDate()));
            colPaymentCycle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymentCycle().toDateShiftString()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getPaymentSummaryStatus(data.getValue().getStatus())));
            colMilkQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQty()));
            colMilkAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkAmount()));
            colProductSale.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProductSaleAmount()));
            colLocalSale.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLocalSaleAmount()));
            colLoan.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLoanAmount()));
            colOtherAdd.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getOtherAddAmount()));
            colOtherDed.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getOtherDedAmount()));
            colNetAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNetAmount()));
            colDisbursedAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDisbursedAmount()));

            propSummary.bind(tableBillSummary.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("MemberBillSummary setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        if (dpFromDate.getValue() == null) {
            var task = new MemberBillSummaryLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<MemberBillSummary> list = task.get();
                    if (list == null)
                        return;
                    tableBillSummary.setItems(FXCollections.observableList(list));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else {
            var task = new MemberBillLoadByDateTask(dpFromDate.getValue(), dpToDate.getValue());
            task.setOnSucceeded(e -> {
                try {
                    List<MemberBillSummary> list = task.get();
                    if (list != null)
                        tableBillSummary.setItems(FXCollections.observableList(list));
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }
}
