package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.task.BonusDeleteTask;
import com.eipl.amcs.operation.billing.task.BonusSummaryLoadTask;
import com.eipl.amcs.report.dto.BonusRegister;
import com.eipl.amcs.report.task.BonusRegisterReportLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class BonusSummaryController implements MyInitialization, PopupCallback {

    @FXML
    private StackPane root;
    @FXML
    private TableView<BonusSummary> tableBonusSummary;
    @FXML
    private TableColumn<BonusSummary, LocalDate> colFromDate, colToDate;
    @FXML
    private TableColumn<BonusSummary, Number> colTotalQty, colTotalAmt, colBonusAmt;
    @FXML
    private TableColumn<BonusSummary, String> colStatus, colMilkType;
    @FXML
    private Button btnAdd, btnEdit, btnClose, btnDelete, btnDisburse, btnReport, btnGeneral;

    private final ObjectProperty<BonusSummary> propSummary;

    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg;

    public BonusSummaryController() {
        propSummary = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        setupTable();
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_BONUS_SUMMARY_ADD"))
                throw new UnAuthorizedAccessException();
            BonusController controller = (BonusController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/BonusAddEdit.fxml"));
            MainApp.getContentPane().setCenter(controller.getRoot());
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_BONUS_SUMMARY_EDIT"))
                throw new UnAuthorizedAccessException();
            if (propSummary.get() != null) {
                if (propSummary.get().getStatus() == 1) {
                    MyAlert alert1 = new InformationAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
                            resourceBundle.getString("disburse.cannot.edit"));
                    alert1.createAlert();
                } else {
                    BonusController controller = (BonusController) MainApp.getFxmlLoaderUtil()
                            .loadAndSet(MainApp.class.getResource("view/operation/billing/BonusAddEdit.fxml"));
                    controller.setBonusSummary(propSummary.get());
                    MainApp.getContentPane().setCenter((controller).getRoot());
                }
            }
        });
        btnDisburse.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_BONUS_SUMMARY_DISBURSE"))
                throw new UnAuthorizedAccessException();
            if (propSummary.get() != null) {
                if (propSummary.get().getStatus() == 1) {
                    MyAlert alert1 = new InformationAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
                            resourceBundle.getString("disburse.cannot.disburse"));
                    alert1.createAlert();
                } else {
                    BonusDisburseController controller = (BonusDisburseController) MainApp.getFxmlLoaderUtil()
                            .loadAndSet(MainApp.class.getResource("view/operation/billing/BonusDisburse.fxml"));
                    controller.setBonusSummary(propSummary.get());
                    MainApp.getContentPane().setCenter((controller).getRoot());
                }
            }
        });
        propSummary.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDisburse.setDisable(false);
                btnDelete.setDisable(false);
                btnReport.setDisable(false);
            } else {
                btnDisburse.setDisable(true);
                btnDelete.setDisable(false);
                btnDelete.setDisable(false);
                btnReport.setDisable(true);
            }
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_BONUS_SUMMARY_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnReport.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "BonusReportPopup", propSummary.get(), this);
        });
        btnGeneral.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "BonusReportGeneralPopup", null, this);
        });
    }


    private void validateAndGenerateReport() {


        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "bonusregister"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        BonusRegisterReportLoadTask task = new BonusRegisterReportLoadTask(MainApp.identityDto.getSociety().getCode(), propSummary.get().getCode());
        task.setOnSucceeded(e -> {
            try {
                List<BonusRegister> list = task.get();
                if (list != null && !list.isEmpty()) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                    params.put("p_bonus_summary_code", propSummary.get().getCode());
                    params.put("p_locale", MainApp.getLocale());
                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_REGISTER, params,
                            new JRBeanCollectionDataSource(list));
                    JasperViewer.viewReport(print, false);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private boolean validate() {
        return true;
    }


    @Override
    public void setupTable() {
        try {
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate()));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getxCol1().equalsIgnoreCase("1") ?
                    resourceBundle.getString("cow") : data.getValue().getxCol1().equalsIgnoreCase("2") ?
                    resourceBundle.getString("buffalo") : data.getValue().getxCol1().equalsIgnoreCase("3") ? resourceBundle.getString("mix")
                    : data.getValue().getxCol1().equalsIgnoreCase("4") ? resourceBundle.getString("A2_Cow") : resourceBundle.getString("all")));
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() == 0 ? "PENDING" : "DONE"));
            colTotalQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotalMilkQty()));
            colTotalAmt.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotalMilkAmount()));
            colBonusAmt.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBonusCriteriaAmount()));
            propSummary.bind(tableBonusSummary.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("BonusSummary setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableBonusSummary.setItems(null);
        var task = new BonusSummaryLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<BonusSummary> list = task.get();
                if (list == null)
                    return;
                tableBonusSummary.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {

        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            BonusSummary dto = propSummary.get();
            if (dto != null) {
                if (dto.getStatus() == 1) {
                    MyAlert alert1 = new InformationAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
                            resourceBundle.getString("disburse.cannot.delete"));
                    alert1.createAlert();
                } else {
                    var task = new BonusDeleteTask(dto.getCode());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
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
    }

}
