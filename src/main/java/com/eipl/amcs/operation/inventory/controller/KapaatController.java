package com.eipl.amcs.operation.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.task.ProductSaleDeleteTask;
import com.eipl.amcs.operation.inventory.task.ProductSaleLoadTask;
import com.eipl.amcs.utils.*;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class KapaatController implements MyInitialization, PopupCallback {

    private final ObjectProperty<ProductSale> propProductSaleDto;
    @FXML
    StackPane root;
    @FXML
    TableView<ProductSale> tableData;
    @FXML
    TableColumn<ProductSale, Number> colAmount, colNetPayable;
    @FXML
    TableColumn<ProductSale, String> colConsumerName, colConsumerType, colMobileNo;
    @FXML
    TableColumn<ProductSale, LocalDate> colDate, colDeductionStartDate;
    @FXML
    E_DatePicker dpFromDate, dpToDate;
    @FXML
    VBox vbox;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnSearch, btnExport;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private Map<String, String> memberMobileNumbers;

    public KapaatController() {
        propProductSaleDto = new SimpleObjectProperty<>();
        memberMobileNumbers = new HashMap<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        FocusUtils.requestFocus(btnAdd);
        btnDelete.setDisable(true);
        propProductSaleDto.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
        });
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "KapaatAddEdit", null, this, resourceBundle.getString("kapaat"));
        });
        setupTable();

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnDelete.setOnAction(e -> {
            deleteData();
        });

        btnExport.setOnAction(e -> {
            exportToCsv();
        });

        dpFromDate.setValue(MainApp.getFinancialYear().getStartDate());
        dpToDate.setValue(MainApp.getFinancialYear().getEndDate());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
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
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"), resourceBundle.getString("error.occurred"));
                                alert1.createAlert();
                                return;
                            }
                            loadData();
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    task.setOnFailed(e -> {
                        Throwable t = task.getException();
                        String errorMessage = "error.occurred";
                        if (t.getMessage().contains("billing.already.completed")) {
                            errorMessage = "billing.already.done";
                        }
                        MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"),
                                resourceBundle.getString(errorMessage));
                        alert1.createAlert();
                    });
                    new Thread(task).start();
                }
            }
        }
    }

    @Override
    public void setupTable() {
        try {
            colDate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getInvoiceDate().format(AppConstant.Formatter3)));
            colConsumerType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getCustomerTypeString(data.getValue().getConsumerType())));
            colConsumerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsumerCode().replace(MainApp.identityDto.getSociety().getCode(), "")));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colNetPayable.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNetAmount()));
            colDeductionStartDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDeductionStartDate()));
            colMobileNo.setCellValueFactory(data -> new SimpleStringProperty(memberMobileNumbers.getOrDefault(data.getValue().getConsumerCode(), "")));
            propProductSaleDto.bind(tableData.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableData);
        } catch (Exception e) {
            System.out.println("Kapaat setuptable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        tableData.setItems(null);
        memberMobileNumbers.clear();
        ProductSaleLoadTask task = new ProductSaleLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<ProductSale> list = task.get().stream().filter(ps -> ps.getXCol3() != null && ps.getXCol3().equalsIgnoreCase("kapaat")).collect(Collectors.toList());
                if (list != null) {
                    tableData.setItems(FXCollections.observableList(list));
                    list.stream()
                            .map(ProductSale::getConsumerCode)
                            .distinct()
                            .forEach(consumerCode -> {
                                var memberTask = new MemberByIdLoadTask(consumerCode);
                                memberTask.setOnSucceeded(event -> {
                                    try {
                                        Member member = memberTask.get();
                                        if (member != null) {
                                            memberMobileNumbers.put(consumerCode, member.getMobileNo());
                                            tableData.refresh();
                                        }
                                    } catch (InterruptedException | ExecutionException ex) {
                                        ex.printStackTrace();
                                    }
                                });
                                new Thread(memberTask).start();
                            });
                }
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

    private String getLocalizedConsumerType(short consumerType) {
        String type = CommonUtils.getCustomerTypeString(consumerType);
        String key = "non-member";
        if (type.equalsIgnoreCase("M")) {
            key = "member";
        }
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return type; // Fallback to the original type if the key is not found
        }
    }

    private void exportToCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as CSV");
        fileChooser.setInitialFileName("KapaatDetails.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(MainApp.getStage());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                writer.write('\uFEFF'); // Add BOM for UTF-8
                // File Header
                writer.append(String.format("\"%s (%s)\"", MainApp.identityDto.getSociety().toString(), FormatterFactory.formatNumber(MainApp.identityDto.getSociety().getCodeEx())));
                writer.newLine();
                writer.append("\"" + resourceBundle.getString("kapaat.details.excel") + "\"");
                writer.newLine();
                writer.append("\"" + resourceBundle.getString("export.date") + ": " + FormatterFactory.formatDate(LocalDate.now()) + "\"");
                writer.newLine();
                writer.newLine();

                // Column Headers
                writer.append(escapeCsv(colDate.getText())).append(',');
                writer.append(escapeCsv(colConsumerType.getText())).append(',');
                writer.append(escapeCsv(colConsumerName.getText())).append(',');
                writer.append(escapeCsv(colMobileNo.getText())).append(',');
                writer.append(escapeCsv(colAmount.getText())).append(',');
                writer.append(escapeCsv(colNetPayable.getText())).append(',');
                writer.append(escapeCsv(colDeductionStartDate.getText())).append('\n');


                // Data
                for (ProductSale sale : tableData.getItems()) {
                    writer.append(escapeCsv(FormatterFactory.formatDate(sale.getInvoiceDate()))).append(',');
                    writer.append(escapeCsv(getLocalizedConsumerType(sale.getConsumerType()))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatNumber(sale.getConsumerCode() != null ? sale.getConsumerCode().replace(MainApp.identityDto.getSociety().getCode(), "") : ""))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatNumber(memberMobileNumbers.getOrDefault(sale.getConsumerCode(), "")))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatNumber(sale.getAmount()))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatNumber(sale.getNetAmount()))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatDate(sale.getDeductionStartDate()))).append('\n');
                }
                MyAlert infoAlert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("kapaat"), "Data exported successfully to " + file.getName());
                infoAlert.createAlert();
            } catch (IOException | MissingResourceException ex) {
                MyAlert errorAlert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("kapaat"), "Error exporting data: " + ex.getMessage());
                errorAlert.createAlert();
                ex.printStackTrace();
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            escaped = "\"" + escaped + "\"";
        }
        return escaped;
    }
}