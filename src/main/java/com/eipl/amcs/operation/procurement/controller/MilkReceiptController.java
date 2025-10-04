package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.task.MilkReceiptDeleteTask;
import com.eipl.amcs.operation.procurement.task.MilkReceiptLoadTask;
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
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MilkReceiptController implements MyInitialization {

    @FXML
    private StackPane root;

    @FXML
    private TableView<MilkReceipt> tableMilkReceipt;

    @FXML
    private TableColumn<MilkReceipt, String> colChallanNo;

    @FXML
    private TableColumn<MilkReceipt, LocalDate> colFromDate, colToDate;

    @FXML
    private TableColumn<MilkReceipt, Shift> colFromShift, colToShift;

    @FXML
    private TableColumn<MilkReceipt, String> colDestinationType;

    @FXML
    private Button btnAdd, btnEdit, btnDelete, btnClose;

    private ResourceBundle resourceBundle;
    private ObjectProperty<MilkReceipt> propMilkReceipt;

    @Override
    public Node getRoot() {
        return root;
    }

    public MilkReceiptController() {
        propMilkReceipt = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        setupTable();
        FocusUtils.requestFocus(btnAdd);

        propMilkReceipt.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnAdd.setOnAction(e -> {
//                    if (!MainApp.user.getPermissions().contains("ACTION_MILK_RECEIPT_ADD"))
//                        throw new UnAuthorizedAccessException();

            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkReceiptAddEdit.fxml")));
        });
        btnEdit.setOnAction(e -> {
            if (propMilkReceipt.get() != null) {
                MilkReceiptAddEditController controller = (MilkReceiptAddEditController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/operation/procurement/MilkReceiptAddEdit.fxml"));
                controller.setMilkReceipt(propMilkReceipt.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            } else {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                        "Automatic Receipt Can not be Edited");
                alert.createAlert();
            }
        });

//        btnDispatchNote.setOnAction(e -> validateAndGenerateReport());
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        // params.put("p_invoice_no", propMilkReceipt.get().getChallanNo());
        params.put("p_locale", MainApp.locale);
        // JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MILK_RECEIPT_CHALLAN, params);
        //  JasperViewer.viewReport(print, false);
    }


    @Override
    public void setupTable() {
        try {
            colChallanNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMilkDispatch() != null ? data.getValue().getMilkDispatch().getChallanNo() : "AUTOMATIC"));
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate().toLocalDate()));
            colFromDate.setCellFactory(new LocalDateCellFactory<>());
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate().toLocalDate()));
            colToDate.setCellFactory(new LocalDateCellFactory<>());
            colFromShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromShift()));
            colToShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToShift()));
            propMilkReceipt.bind(tableMilkReceipt.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void loadData() {
        tableMilkReceipt.setItems(null);
        var task = new MilkReceiptLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkReceipt> list = task.get();
                if (list != null)
                    tableMilkReceipt.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkreceipt"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            var task = new MilkReceiptDeleteTask(propMilkReceipt.get());
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
