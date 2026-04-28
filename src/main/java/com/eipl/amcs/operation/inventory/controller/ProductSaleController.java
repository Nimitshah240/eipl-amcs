package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.task.ProductSaleDeleteTask;
import com.eipl.amcs.operation.inventory.task.ProductSaleInstallmentByOnlyInvoiceNoLoadTask;
import com.eipl.amcs.operation.inventory.task.ProductSaleLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ProductSaleController implements MyInitialization, PopupCallback {
    private final ObjectProperty<ProductSale> propProductSaleDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<ProductSale> tableProductSaleToMember;
    @FXML
    TableColumn<ProductSale, Number> colAmount, colNetPayable, colNoOfInstallment;
    @FXML
    TableColumn<ProductSale, String> colInvoiceNo, colConsumerName, colConsumerType;
    @FXML
    TableColumn<ProductSale, LocalDate> colDate, colDeductionStartDate;
    @FXML
    Button btnClose, btnAdd, btnInstallments, btnEdit, btnDelete, btnSearch;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    private ResourceBundle resourceBundle;
    private List<ProductSaleInstallment> installmentList;

    public ProductSaleController() {
        propProductSaleDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        this.resourceBundle = resourceBundle;

        dpFromDate.setValue(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1));
        dpToDate.setValue(LocalDate.now());
        loadData();
        setupTable();
        btnSearch.setOnAction(e -> loadData());
        btnInstallments.setOnAction(e -> {
            loadInstallments();

        });
//        btnAdd.setOnAction(e -> {
//            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_ADD"))
//                throw new UnAuthorizedAccessException();
//            var controller = (ProductSaleAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductSaleAddEdit.fxml"));
//            controller.setProductSale(null);
//            MainApp.contentPane.setCenter(controller.getRoot());
//        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        propProductSaleDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
//        btnEdit.setOnAction(e -> {
//            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_EDIT"))
//                throw new UnAuthorizedAccessException();
//            var controller = (ProductSaleAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductSaleAddEdit.fxml"));
//            controller.setProductSale(propProductSaleDto.get());
//            MainApp.contentPane.setCenter(controller.getRoot());
//        });
        btnDelete.setOnAction(actionEvent -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });

        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductSaleAddEdit", null, this, "ProductSale");
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_PRODUCT_SALE_EDIT"))
                throw new UnAuthorizedAccessException();
            editProductSale(propProductSaleDto.get());
        });

        tableProductSaleToMember.setRowFactory(tv -> {
            TableRow<ProductSale> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    ProductSale data = row.getItem();
                    editProductSale(data);
                }
            });
            return row;
        });

        tableProductSaleToMember.setOnKeyPressed(event -> {
            ProductSale dto = tableProductSaleToMember.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propProductSaleDto.get();
                    if (dto != null)
                        deleteData();
                    break;
                case ENTER:
                    editProductSale(dto);
                    break;
            }
        });
    }

    private void editProductSale(ProductSale productSale) {
        if (productSale != null)
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "ProductSaleAddEdit", productSale, this, "ProductSale");
    }

    @Override
    public void setupTable() {
        try {
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

            colInvoiceNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceNo()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getInvoiceDate()));
            colDate.setCellFactory(new LocalDateCellFactory<>());
            colConsumerType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getCustomerTypeString(data.getValue().getConsumerType())));
            colConsumerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsumerCode()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colNetPayable.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNetAmount()));
            colNoOfInstallment.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNoOfInstallments() == null ? 0 : data.getValue().getNoOfInstallments()));
            colDeductionStartDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDeductionStartDate()));
            propProductSaleDto.bind(tableProductSaleToMember.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("ProductSale setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableProductSaleToMember.setItems(null);
        ProductSaleLoadTask task = new ProductSaleLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<ProductSale> list = task.get();
                if (list != null) tableProductSaleToMember.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("productsale"), resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            ProductSale dto = propProductSaleDto.get();
            if (dto != null) {
                var task = new ProductSaleDeleteTask(dto.getInvoiceNo());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"), resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                task.setOnFailed(e -> {
                    MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("productsale"), resourceBundle.getString("error.occurred"));
                    alert1.createAlert();
                });
                new Thread(task).start();
            }
        }
    }


    private void loadInstallments() {
        var task = new ProductSaleInstallmentByOnlyInvoiceNoLoadTask(propProductSaleDto.get().getInvoiceNo());
        task.setOnSucceeded(e -> {
            try {
                installmentList = task.get();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "InstallmentsAddEdit", installmentList, this);
        });
        new Thread(task).start();
    }
}
