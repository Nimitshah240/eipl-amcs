package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.org.convertor.DockConvertor;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.task.DockLoadTask;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.MilkCollectionImportTask;
import com.eipl.amcs.operation.procurement.task.MilkCollectionListSaveTask;
import com.eipl.amcs.operation.procurement.task.MilkCollectionLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MilkCollectionController implements MyInitialization, PopupCallback {
    @FXML
    StackPane root;
    @FXML
    TableView<MilkCollection> tableCollection;
    @FXML
    TableColumn<MilkCollection, Number> colSampleNo, colMemberCode, colQty, colFat, colSnf, colRate, colAmount;
    @FXML
    TableColumn<MilkCollection, MilkType> colMilkType;
    @FXML
    TableColumn<MilkCollection, Shift> colShift;
    @FXML
    TableColumn<MilkCollection, LocalDate> colDate;
    @FXML
    TableColumn<MilkCollection, String> colMemberName;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    Button btnSearch, btnClose, btnStartCollection, btnImport, btnExport, btnSync;
    @FXML
    private ComboBox<Dock> cboxDock;
    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<MilkType> milkTypeList;
    private List<Member> memberList;
    private List<MilkCollection> listMilkCollection;

    @Override
    public Node getRoot() {
        return root;
    }

    /**
     * @updatedBy Nimit Shah
     * @updatedOn - 07-07-2025
     * @update - added condition on btnExport action to show no data on empty list.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FocusUtils.requestFocus(btnStartCollection);
        this.resourceBundle = resourceBundle;
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        setupTable();
        setupComboBox();
        loadShift();
        cboxDock.getSelectionModel().select(0);

        btnSearch.setOnAction(e -> loadData());
        btnImport.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_COLLECTION_IMPORT"))
                throw new UnAuthorizedAccessException();
            loadImportPreReq();
            File file = CommonUtils.openExcelFileDialog(resourceBundle.getString("milkcollection"));
            if (file == null) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("select.file"));
                alert.createAlert();
                return;
            }
//
//            if (!file.getName().contains(MainApp.identityDto.getSociety().getCode())) {
//                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
//                        resourceBundle.getString("invalid.file"));
//                alert.createAlert();
//                return;
//            }

            MainApp.paneDrop.setVisible(true);
            MainApp.lblMessage.setText("Preparing Milk Collection...");
            startImport(file);
        });
        btnExport.setOnAction(event -> {
            if (listMilkCollection == null || listMilkCollection.isEmpty()) {
                MyAlert alert = new InformationAlert(MainApp.stage,
                        resourceBundle.getString("milkcollection"),
                        resourceBundle.getString("no.data"));
                alert.createAlert();
                return;
            }
            List<MilkCollection> list = listMilkCollection.stream().collect(Collectors.toList());
            exportExcel(list);
        });
        btnSync.setDisable(true);
        btnSync.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SyncPopup", null, this);
        });
        btnClose.setOnAction(e ->
                MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml"))));
        btnStartCollection.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/procurement/MilkCollectionAdd.fxml"))));
    }

    private void exportExcel(List<MilkCollection> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Collection");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("Sample No.", "Date", "Shift", "Member Code", "Milk Type",
                        "Fat", "Snf", "Qty", "Rate", "Amount");
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
                for (MilkCollection item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        switch (columnTitle) {
                            case "Sample No.":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getSampleNo());
                                break;
                            case "Date":
                                cell = row.createCell(cellValueHeading++);
                                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                String text = item.getCollectionDate().toLocalDate().format(formatters);
                                cell.setCellValue(text);
                                break;
                            case "Shift":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getShift().getName().substring(0, 1));
                                break;
                            case "Member Code":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMember().getCodeEx());
                                break;
                            case "Milk Type":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMilkType().getName());
                                break;
                            case "Fat":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getFat().doubleValue());
                                break;
                            case "Snf":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getSnf().doubleValue());
                                break;
                            case "Qty":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getQty().doubleValue());
                                break;
                            case "Rate":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getRtpl().doubleValue());
                                break;
                            case "Amount":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getAmount().doubleValue());
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
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("milkcollection"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("milkcollection"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    private void startImport(File file) {
        var task = new MilkCollectionImportTask(file, milkTypeList, shiftList, memberList);
        task.setOnSucceeded(e -> {
            try {
                listMilkCollection = task.get();
                if (listMilkCollection == null || listMilkCollection.isEmpty()) {
                    MainApp.paneDrop.setVisible(false);
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                            resourceBundle.getString("invalid.file"));
                    alert.createAlert();
                    return;
                }
                MainApp.lblMessage.setText("Importing Milk Collections...");
                startImportProcess();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void startImportProcess() {
        var task = new MilkCollectionListSaveTask(listMilkCollection);
        task.setOnSucceeded(e -> {
            try {
                MainApp.paneDrop.setVisible(false);
                List<CollectionImportDto> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
                String builder = "Import success: " +
                        list.stream().filter(p -> p.getStatus().equalsIgnoreCase("success")).count() +
                        "\n" +
                        "Import fail: " +
                        list.stream().filter(p -> p.getStatus().equalsIgnoreCase("error")).count() +
                        "\n";

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollection"),
                        builder);
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void loadImportPreReq() {

        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                milkTypeList = task1.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                shiftList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task2 = new MemberLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                memberList = task2.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    List<Shift> list1 = CommonUtils.removeAllShift(list);
                    cboxFromShift.setItems(FXCollections.observableList(list1));
                    cboxToShift.setItems(FXCollections.observableList(list1));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.getSelectionModel().select(list1.size() - 1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        // dock load
        var task2 = new DockLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                List<DockMilkTypeDto> list = task2.get();
                if (list != null) {
                    List<Dock> listDock = list.stream()
                            .map(m -> m.getDock()).collect(Collectors.toList());
                    if (listDock.size() == 1) {
                        cboxDock.setItems(FXCollections.observableList(listDock));
                        cboxDock.getSelectionModel().select(0);
                    } else {
                        listDock.add(0, new Dock("All"));
                        cboxDock.setItems(FXCollections.observableList(listDock));
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    @Override
    public void loadData() {
        tableCollection.setPlaceholder(new Label("Loading data..."));
        var task = new MilkCollectionLoadTask(
                CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()),
                CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()),
                cboxDock.getValue().getDockNo(), 0);
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollection> list = task.get();
                if (list == null) {
                    tableCollection.setPlaceholder(new Label("No data..."));
                    return;
                }
                listMilkCollection = list;
                tableCollection.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        cboxDock.setConverter(new DockConvertor(cboxDock));
        cboxDock.getSelectionModel().select(0);
    }

    @Override
    public void setupTable() {
        try {
            colSampleNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSampleNo()));
            colMemberCode.setCellValueFactory(data -> new SimpleIntegerProperty(CommonUtils.strToInteger(data.getValue().getMember().getCodeEx())));
            colQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
            colFat.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFat()));
            colSnf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSnf()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRtpl()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCollectionDate().toLocalDate()));
            colDate.setCellFactory(new LocalDateCellFactory<>());
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShift()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
