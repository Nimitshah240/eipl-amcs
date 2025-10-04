package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.task.MemberBillSummaryLoadTask;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.task.ProductSaleDeleteTask;
import com.eipl.amcs.operation.inventory.task.ProductSaleLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;


public class KapaatController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<ProductSale> tableData;
    @FXML
    TableColumn<ProductSale, Number> colAmount, colDiscount, colTax, colNetPayable, colNoOfInstallment;
    @FXML
    TableColumn<ProductSale, String> colInvoiceNo, colConsumerCode, colConsumerName, colConsumerType;
    @FXML
    TableColumn<ProductSale, LocalDate> colDate, colDeductionStartDate;
    @FXML
    GridPane gridMaster;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    VBox vbox;

    @FXML
    Button btnClose, btnAdd, btnDelete, btnSearch;

    private Stage stage;
    private PopupCallback callback;

    private ResourceBundle resourceBundle;

    private final ObjectProperty<ProductSale> propProductSaleDto;

    public KapaatController() {
        propProductSaleDto = new SimpleObjectProperty<>();
    }

    private ProductSale productSale;
    private Member member;
    private List<SocietyPaymentCycle> paymentCycleList;
    private List<ProductSaleInstallment> installmentList = new ArrayList<>();
    private ProductSaleDto dto;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        FocusUtils.requestFocus(btnAdd);
        propProductSaleDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDelete.setDisable(false);
            } else {
                btnDelete.setDisable(true);
            }
        });
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "KapaatAddEdit", null, this);
        });
        setupTable();

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnDelete.setOnAction(e -> {
            deleteData();
        });
        dpFromDate.setValue(LocalDate.now().withDayOfMonth(1));
        dpToDate.setValue(LocalDate.now());
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

        btnSearch.setOnAction(e -> {
            loadData();
        });

        loadData();
    }


    @Override
    public void deleteData() {
        {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("kapaat"), resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                ProductSale dto = propProductSaleDto.get();
                if (dto != null) {
                    var task = new ProductSaleDeleteTask(dto.getInvoiceNo());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || respDelete.booleanValue() == false) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"), resourceBundle.getString("error.occurred"));
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

    @Override
    public void setupTable() {
        try {
//            colInvoiceNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceNo()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getInvoiceDate().format(AppConstant.Formatter3)));
            colConsumerType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getCustomerTypeString(data.getValue().getConsumerType())));
            colConsumerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsumerCode().replace(MainApp.identityDto.getSociety().getCode(),"")));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colNetPayable.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNetAmount()));
//            colNoOfInstallment.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNoOfInstallments() == null ? 0 : data.getValue().getNoOfInstallments()));
            colDeductionStartDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDeductionStartDate()));
            propProductSaleDto.bind(tableData.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("Kapaat setuptable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
      tableData.setItems(null);
        ProductSaleLoadTask task = new ProductSaleLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<ProductSale> list = task.get();
                if (list != null) tableData.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void reloadData(boolean flag) {
        loadData();
    }
}
