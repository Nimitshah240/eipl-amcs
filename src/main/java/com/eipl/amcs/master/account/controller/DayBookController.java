package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.task.VoucherLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class DayBookController implements MyInitialization, PopupCallback {

    @FXML
    private StackPane root;
    @FXML
    private TableView<VoucherDto> tableVoucher;
    @FXML
    private TableColumn<VoucherDto, String> colType;
    @FXML
    private TableColumn<VoucherDto, String> colVoucherNo;
    @FXML
    private TableColumn<VoucherDto, String> colVoucherDate;
    @FXML
    private TableColumn<VoucherDto, String> colRemarks;
    @FXML
    private TableColumn<VoucherDto, String> colRefNo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnDayBook;
    @FXML
    private Button btnLedger;

    private ResourceBundle resourceBundle;
    private final ObjectProperty<VoucherDto> propVoucherDto = new SimpleObjectProperty<>();

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        propVoucherDto.bind(tableVoucher.getSelectionModel().selectedItemProperty());

        propVoucherDto.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
        });

        setupTable();
        loadData();

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnLedger.setOnAction(e -> {
            if (propVoucherDto.get() != null) {
                VoucherLedgerController controller = (VoucherLedgerController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/master/account/VoucherLedger.fxml"));
                controller.setVoucher(propVoucherDto.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            } else {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("daybook"), resourceBundle.getString("select.record.message"));
                alert.createAlert();
            }
        });

        // TODO: Aapko in buttons ke liye action define karna hoga
        // btnAdd.setOnAction(e -> { ... });
        // btnDelete.setOnAction(e -> { ... });
        // btnDayBook.setOnAction(e -> { ... });
    }

    @Override
    public void setupTable() {
        colType.setCellValueFactory(data -> new SimpleStringProperty(
                MainApp.locale.equalsIgnoreCase("en") ?
                        data.getValue().getVoucher().getVoucherType().getName() :
                        data.getValue().getVoucher().getVoucherType().getNameLocal()));
        colVoucherNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getCode()));
        colVoucherDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getVoucherDate().toString()));
        colRefNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getBillNo()));
        colRemarks.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVoucher().getRemarks()));
    }

    @Override
    public void loadData() {
        tableVoucher.setItems(null);
        var task = new VoucherLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<VoucherDto> list = task.get();
                if (list != null) {
                    tableVoucher.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        // TODO: Agar zaroorat ho to delete functionality implement karein, VoucherController ki tarah
    }
}
