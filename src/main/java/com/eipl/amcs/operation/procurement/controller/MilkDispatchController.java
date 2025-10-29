package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.task.MilkDispatchDeleteTask;
import com.eipl.amcs.operation.procurement.task.MilkDispatchLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
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

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MilkDispatchController implements MyInitialization, PopupCallback {

    @FXML
    private StackPane root;

    @FXML
    private TableView<MilkDispatch> tableMilkDispatch;

    @FXML
    private TableColumn<MilkDispatch, String> colChallanNo;

    @FXML
    private TableColumn<MilkDispatch, LocalDate> colFromDate, colToDate;

    @FXML
    private TableColumn<MilkDispatch, Shift> colFromShift, colToShift;

    @FXML
    private TableColumn<MilkDispatch, String> colDestinationType;

    @FXML
    private Button btnAdd, btnEdit, btnDelete, btnClose, btnDispatchNote;

    private ResourceBundle resourceBundle;
    private final ObjectProperty<MilkDispatch> propMilkDispatch;

    public MilkDispatchController() {
        propMilkDispatch = new SimpleObjectProperty<>();
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
        FocusUtils.requestFocus(btnAdd);

        propMilkDispatch.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_DISPATCH_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_DISPATCH_ADD"))
                throw new UnAuthorizedAccessException();

            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkDispatchAddEdit.fxml")));
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_DISPATCH_EDIT"))
                throw new UnAuthorizedAccessException();
            if (propMilkDispatch.get() != null) {
                MilkDispatchAddEditController controller = (MilkDispatchAddEditController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/operation/procurement/MilkDispatchAddEdit.fxml"));
                controller.setMilkDispatch(propMilkDispatch.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            }
        });

        btnDispatchNote.setOnAction(e -> {
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkDispatchReportPopup", propMilkDispatch.get(), this);
                }
        );
    }

//    private void validateAndGenerateReport() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
//        params.put("p_invoice_no", propMilkDispatch.get().getChallanNo());
//        params.put("p_locale", MainApp.locale);
//        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_DISPATCH_CHALLAN, params);
//        JasperViewer.viewReport(print, false);
//    }
//
//    private void validateAndGenerateReportTwo() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
//        params.put("p_challan_no", propMilkDispatch.get().getChallanNo());
//        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_DISPATCH_CHALLAN_FORMAT_TWO, params);
//        JasperViewer.viewReport(print, false);
//    }


    @Override
    public void setupTable() {
        try {
            colChallanNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChallanNo()));
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate().toLocalDate()));
            colFromDate.setCellFactory(new LocalDateCellFactory<>());
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate().toLocalDate()));
            colToDate.setCellFactory(new LocalDateCellFactory<>());
            colFromShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromShift()));
            colToShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToShift()));
            colDestinationType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getDispatchDestinationType(data.getValue().getDestinationType())));
            propMilkDispatch.bind(tableMilkDispatch.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void loadData() {
        tableMilkDispatch.setItems(null);
        var task = new MilkDispatchLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkDispatch> list = task.get();
                if (list != null)
                    tableMilkDispatch.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkdispatch"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            var task = new MilkDispatchDeleteTask(propMilkDispatch.get());
            task.setOnSucceeded(e -> {
                try {
                    boolean isDelete = task.get();
                    if (isDelete)
                        loadData();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
                loadData();
            });
            new Thread(task).start();
        }
    }
}
