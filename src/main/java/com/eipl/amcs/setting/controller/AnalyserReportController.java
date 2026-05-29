package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.task.DockLoadTask;
import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.master.procurement.task.HardwareDeviceLoadTask;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.task.HardwareDeviceConfigLoadTask;
import com.eipl.amcs.setting.task.MilkCollectionLoadTaskByAnalyzeAndShift;
import com.eipl.amcs.utils.CommonUtils;
import com.fazecast.jSerialComm.SerialPort;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.eipl.amcs.setting.controller.HardwareDeviceConfigurationController.ANALYSER;

public class AnalyserReportController implements MyInitialization {

    private final List<String> commPorts = new ArrayList<>();

    @FXML
    private ComboBox<HardwareDevice> cboxHardware;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private ComboBox<Dock> cboxDock;
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

    private ResourceBundle resourceBundle;
    private List<Shift> shiftList;
    private List<MilkCollection> listMilkCollection;

    @FXML
    Button btnSearch, btnClose, btnExport;

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        loadData();
        setupTable();
        btnSearch.setOnAction(e -> searchData());
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnExport.setOnAction(e -> exportToExcel());
    }

    private void exportToExcel() {
        if (listMilkCollection == null || listMilkCollection.isEmpty()) {
            new InformationAlert(MainApp.getStage(), "No Data", "There is no data to export.").createAlert();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls"));
        File file = fileChooser.showSaveDialog(MainApp.getStage());

        if (file != null) {
            try (Workbook workbook = new HSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Analyser Report");

                // Header Row
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < tableCollection.getColumns().size(); i++) {
                    headerRow.createCell(i).setCellValue(tableCollection.getColumns().get(i).getText());
                }

                // Data Rows
                int rowNum = 1;
                for (MilkCollection mc : listMilkCollection) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(mc.getCollectionDate().toLocalDate().toString());
                    row.createCell(1).setCellValue(mc.getShift().toString());
                    row.createCell(2).setCellValue(mc.getSampleNo());
                    row.createCell(3).setCellValue(mc.getMember().getCodeEx());
                    row.createCell(4).setCellValue(mc.getMember().toMemberName());
                    row.createCell(5).setCellValue(mc.getMilkType().toString());
                    row.createCell(6).setCellValue(mc.getQty().doubleValue());
                    row.createCell(7).setCellValue(mc.getFat().doubleValue());
                    row.createCell(8).setCellValue(mc.getSnf().doubleValue());
                    row.createCell(9).setCellValue(mc.getClr() != null ? mc.getClr().doubleValue() : 0.0);
                    row.createCell(10).setCellValue(mc.getWater() != null ? mc.getWater().doubleValue() : 0.0);
                    row.createCell(11).setCellValue(mc.getDensity() != null ? mc.getDensity().doubleValue() : 0.0);
                    row.createCell(12).setCellValue(mc.getLectose() != null ? mc.getLectose().doubleValue() : 0.0);
                    row.createCell(13).setCellValue(mc.getProtein() != null ? mc.getProtein().doubleValue() : 0.0);
                    row.createCell(14).setCellValue(mc.getRtpl().doubleValue());
                    row.createCell(15).setCellValue(mc.getAmount().doubleValue());
                }

                for (int i = 0; i < tableCollection.getColumns().size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    new InformationAlert(MainApp.getStage(), "Export Successful", "Data has been exported to " + file.getName()).createAlert();
                }

            } catch (IOException e) {
                e.printStackTrace();
                new ErrorAlert(MainApp.getStage(), "Export Error", "An error occurred during the Excel export.").createAlert();
            }
        }
    }

    private void searchData() {
        if (cboxHardware.getValue() == null) {
            new ErrorAlert(MainApp.getStage(), "Error", "Please select hardware").createAlert();
            return;
        }
        if (dpFromDate.getValue() == null) {
            new ErrorAlert(MainApp.getStage(), "Error", "Please select from date").createAlert();
            return;
        }
        if (dpToDate.getValue() == null) {
            new ErrorAlert(MainApp.getStage(), "Error", "Please select to date").createAlert();
            return;
        }
        if (cboxDock.getValue() == null) {
            new ErrorAlert(MainApp.getStage(), "Error", "Please select dock").createAlert();
            return;
        }

        var task = new MilkCollectionLoadTaskByAnalyzeAndShift(
                cboxHardware.getValue().getCode(),
                CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue()),
                CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue()),
                cboxDock.getValue().getDockNo()
        );
        task.setOnSucceeded(e -> {
            try {
                listMilkCollection = task.get();
                tableCollection.setItems(FXCollections.observableArrayList(listMilkCollection));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setupPreviousSelection(List<HardwareDeviceConfig> list) {
        HardwareDeviceConfig configAnalyser = list.stream().filter(p -> p.getDeviceType().equals(ANALYSER)).findFirst().orElse(null);
        if (configAnalyser != null) {
            cboxHardware.setValue(configAnalyser.getHardwareDevice());
        }
    }

    @Override
    public void loadData() {
        // load serial ports
        commPorts.add("NA");
        for (SerialPort port : SerialPort.getCommPorts()) {
            commPorts.add(port.getSystemPortName());
        }

        var hardwareTask = new HardwareDeviceLoadTask();
        hardwareTask.setOnSucceeded(e -> {
            try {
                List<HardwareDevice> list = hardwareTask.get();
                if (list != null && !list.isEmpty()) {
                    cboxHardware.setItems(FXCollections.observableList(list));
                    var configTask = new HardwareDeviceConfigLoadTask();
                    configTask.setOnSucceeded(e1 -> {
                        try {
                            List<HardwareDeviceConfig> list1 = configTask.get();
                            if (list1 != null && !list1.isEmpty()) {
                                setupPreviousSelection(list1.stream().filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo())).collect(Collectors.toList()));
                            }
                            if (cboxHardware.getSelectionModel().getSelectedItem() == null) {
                                cboxHardware.getSelectionModel().select(0);
                            }
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(configTask).start();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(hardwareTask).start();

        var shiftTask = new ShiftLoadTask();
        shiftTask.setOnSucceeded(e -> {
            try {
                shiftList = shiftTask.get();
                if (shiftList != null && !shiftList.isEmpty()) {
                    cboxFromShift.setItems(FXCollections.observableArrayList(shiftList));
                    cboxToShift.setItems(FXCollections.observableArrayList(shiftList));
                    cboxFromShift.getSelectionModel().select(0);
                    cboxToShift.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(shiftTask).start();

        var dockTask = new DockLoadTask();
        dockTask.setOnSucceeded(e -> {
            try {
                List<DockMilkTypeDto> dockMilkTypeList = dockTask.get();
                if (dockMilkTypeList != null && !dockMilkTypeList.isEmpty()) {
                    List<Dock> dockList = dockMilkTypeList.stream()
                            .map(DockMilkTypeDto::getDock)
                            .distinct()
                            .collect(Collectors.toList());
                    cboxDock.setItems(FXCollections.observableArrayList(dockList));
                    cboxDock.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(dockTask).start();
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
