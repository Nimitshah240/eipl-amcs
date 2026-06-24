package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.account.converter.LedgerGroupConvertor;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.task.LedgerDeleteTask;
import com.eipl.amcs.master.account.task.LedgerGroupLoadTask;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class LedgerController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Ledger> propLedger;
    @FXML
    AnchorPane root;
    @FXML
    TableView<Ledger> tableLedger;
    @FXML
    TableColumn<Ledger, String> colName, colLocalName, colCode;
    @FXML
    TableColumn<Ledger, LedgerGroup> colLedgerGroup;
    @FXML
    ComboBox<LedgerGroup> cboxLedgerGroup;
    @FXML
    TextField txtSearch;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnClear, btnExport;
    private ResourceBundle resourceBundle;
    private List<Ledger> ledgerList = new ArrayList<>();
    private List<LedgerGroup> ledgerGroupList = new ArrayList<>();

    public LedgerController() {
        propLedger = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadData();
        loadLedgerGroups();
        FocusUtils.requestFocus(btnAdd);
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LEDGER_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LEDGER_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerAddEdit", null, this, resourceBundle.getString("ledger"));

//            LedgerAddEditController controller = (LedgerAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/LedgerAddEdit.fxml"));
//            controller.setLedger(null);
//            MainApp.getContentPane().setCenter(controller.getRoot());
        });
        btnExport.setOnAction(e -> {
            loadExcel();
        });

        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LEDGER_EDIT"))
                throw new UnAuthorizedAccessException();
            if (propLedger.get() != null) {
//                LedgerAddEditController controller = (LedgerAddEditController) MainApp.getFxmlLoaderUtil()
//                        .loadAndSet(MainApp.class.getResource("view/master/account/LedgerAddEdit.fxml"));
//                controller.setLedger(propLedger.get());
//                MainApp.getContentPane().setCenter((controller).getRoot());

                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerAddEdit", propLedger.get(), this, resourceBundle.getString("ledger"));

            }
        });
        propLedger.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            cboxLedgerGroup.getSelectionModel().clearSelection();
            search(oldValue, newValue);
        });

        btnClear.setOnAction(e -> {
                    tableLedger.setItems(FXCollections.observableList(ledgerList));
                    txtSearch.setText("");
                    cboxLedgerGroup.getSelectionModel().clearSelection();
                }
        );
        cboxLedgerGroup.setOnAction(e -> {
            txtSearch.setText("");
            if (cboxLedgerGroup.getValue() != null)
                tableLedger.setItems(FXCollections.observableList(ledgerList.stream().filter(
                                e1 -> e1.getLedgerGroup() != null && e1.getLedgerGroup().getCode() == cboxLedgerGroup.getValue().getCode()).
                        collect(Collectors.toList())));
        });


        tableLedger.setRowFactory(tv -> {
            TableRow<Ledger> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Ledger data = row.getItem();
                    editLedger(data);
                }
            });
            return row;
        });

        tableLedger.setOnKeyPressed(event -> {
            Ledger dto = tableLedger.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case ENTER:
                    editLedger(dto);
                    break;
                case DELETE:
                    deleteData();
                    break;
            }
        });
    }

    private void editLedger(Ledger ledger) {
        try {
            if (ledger != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LedgerAddEdit", ledger, this, resourceBundle.getString("ledger"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadExcel() {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Ledger Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("Name", "Local Name", "Ledger Group");
                List<String> strColumnTodisplay = null;
                List<String> items = null;
                strColumnTodisplay = new ArrayList<>(strColumns);
                List<String> finalResultToDisplay = strColumnTodisplay.stream().collect(Collectors.toList());
                // Create header column
                HSSFRow row = sheet.createRow(0);
                HSSFCell cell = null;
                int cellValueHeading = 0;
                for (String columnTitle : finalResultToDisplay) {
                    cell = row.createCell(cellValueHeading);
                    cell.setCellValue(columnTitle);
                    cellValueHeading++;
                }
                int rowCnt = 1;
                for (Ledger item : ledgerList) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        switch (columnTitle) {
                            case "Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getName());
                                break;
                            case "Local Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getNameLocal());
                                break;
                            case "Ledger Group":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getLedgerGroup()));
                                break;
                            default:
                                break;
                        }
                    }
                    rowCnt++;
                }
                try {
                    wb.close();
                } catch (IOException e1) {
                    exported = false;
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    wb.write(out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    exported = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exported = false;
        }
        MyAlert alert;
        if (exported) {
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    public void search(String oldVal, String newVal) {
        if (!newVal.equalsIgnoreCase("")) {
            tableLedger.setItems(FXCollections.observableList(ledgerList.stream().
                    filter(
                            e1 ->
                                    e1.getCode().toLowerCase().contains(newVal.toLowerCase()) ||
                                            e1.getName().toLowerCase().contains(newVal.toLowerCase()) ||
                                            (e1.getNameLocal() != null && e1.getNameLocal().toLowerCase().contains(newVal.toLowerCase()))
                    ).collect(Collectors.toList())));
        } else {
            tableLedger.setItems(FXCollections.observableList(ledgerList));
        }
    }

    @Override
    public void setupTable() {
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
        colLedgerGroup.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLedgerGroup()));
        TableLocalizationUtil.localizeTable(tableLedger);

        propLedger.bind(tableLedger.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void loadData() {
        tableLedger.setItems(null);
        LedgerLoadTask task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                ledgerList = task.get();
                if (ledgerList != null)
                    tableLedger.setItems(FXCollections.observableList(ledgerList));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadLedgerGroups() {
        cboxLedgerGroup.setConverter(new LedgerGroupConvertor(cboxLedgerGroup));
        LedgerGroupLoadTask task = new LedgerGroupLoadTask();
        task.setOnSucceeded(e -> {
            try {
                ledgerGroupList = task.get();
                if (ledgerGroupList != null)
                    cboxLedgerGroup.setItems(FXCollections.observableList(ledgerGroupList));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Ledger dto = propLedger.get();
            if (dto != null) {
                var task = new LedgerDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("ledger"),
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
