package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.procurement.converter.HardwareDeviceConvertor;
import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.master.procurement.task.HardwareDeviceLoadTask;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.task.HardwareDeviceConfigLoadTask;
import com.eipl.amcs.setting.task.HardwareDeviceConfigSaveTask;
import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class HardwareDeviceConfigurationController implements MyInitialization {
    public static final String WS = "WS";
    public static final String ANALYSER = "ANALYSER";
    public static final String ANALYSER2 = "ANALYSER2";
    public static final String ANALYSER3 = "ANALYSER3";
    public static final String ANALYSER4 = "ANALYSER4";
    public static final String DISPLAY = "DISPLAY";
    public static final String SPLITTER = "SPLITTER";
    public static final String PRINTER = "PRINTER";
    @FXML
    StackPane root;
    @FXML
    Button btnClose, btnSave;
    String[] arrQuality = {"Sequence Wise", "Milk Type Wise"};
    @FXML
    private ComboBox<String> cboxWsPort, cboxAnalyserPort, cboxAnalyserPort2, cboxAnalyserPort3, cboxAnalyserPort4, cboxDisplayPort, cboxPrinter, cboxSplitterPort, cboxQualityMachine, cboxQualityMachine2, cboxQualityMachine3, cboxQualityMachine4;
    @FXML
    private ComboBox<HardwareDevice> cboxWs, cboxAnalyser, cboxAnalyser2, cboxAnalyser3, cboxAnalyser4, cboxDisplay, cboxSplitter;
    @FXML
    private ComboBox<MilkType> cboxMilkType, cboxMilkType2, cboxMilkType3, cboxMilkType4;
    private List<HardwareDeviceConfig> deviceConfigList;
    private StringBuilder errorMsg = null;
    private final List<String> commPorts = new ArrayList<>();
    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setComboBox();
        loadData();

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnSave.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_HARDWARE_DEVICE_CONFIGURATION_SAVE"))
                throw new UnAuthorizedAccessException();
            validateAndSave();
        });

        cboxQualityMachine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!"Sequence Wise".equals(newValue)) {
                cboxMilkType.setDisable(false);
            } else {
                cboxMilkType.setDisable(true);
                cboxMilkType.getSelectionModel().clearSelection(); // Optional: Clear selection when disabled
            }
        });

        cboxQualityMachine2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!"Sequence Wise".equals(newValue)) {
                cboxMilkType2.setDisable(false);
            } else {
                cboxMilkType2.setDisable(true);
                cboxMilkType2.getSelectionModel().clearSelection();
            }
        });

        cboxQualityMachine3.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!"Sequence Wise".equals(newValue)) {
                cboxMilkType3.setDisable(false);
            } else {
                cboxMilkType3.setDisable(true);
                cboxMilkType3.getSelectionModel().clearSelection();
            }
        });

        cboxQualityMachine4.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!"Sequence Wise".equals(newValue)) {
                cboxMilkType4.setDisable(false);
            } else {
                cboxMilkType4.setDisable(true);
                cboxMilkType4.getSelectionModel().clearSelection();
            }
        });
    }

    @Override
    public void loadData() {
        // load serial ports
        commPorts.add("NA");
        for (SerialPort port : SerialPort.getCommPorts()) {
            commPorts.add(port.getSystemPortName());
        }

        cboxWsPort.setItems(FXCollections.observableList(commPorts));
        cboxAnalyserPort.setItems(FXCollections.observableList(commPorts));
        cboxAnalyserPort2.setItems(FXCollections.observableList(commPorts));
        cboxAnalyserPort3.setItems(FXCollections.observableList(commPorts));
        cboxAnalyserPort4.setItems(FXCollections.observableList(commPorts));
        cboxDisplayPort.setItems(FXCollections.observableList(commPorts));
        cboxSplitterPort.setItems(FXCollections.observableList(commPorts));

        cboxWsPort.getSelectionModel().select(0);
        cboxAnalyserPort.getSelectionModel().select(0);
        cboxAnalyserPort2.getSelectionModel().select(0);
        cboxAnalyserPort3.getSelectionModel().select(0);
        cboxAnalyserPort4.getSelectionModel().select(0);
        cboxDisplayPort.getSelectionModel().select(0);
        cboxSplitterPort.getSelectionModel().select(0);

        var task = new HardwareDeviceLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<HardwareDevice> list = task.get();
                if (list == null || list.isEmpty()) {
                    return;
                }
                cboxWs.setItems(FXCollections.observableList(list.stream().filter(p -> p.getDeviceType() == (short) 0).collect(Collectors.toList())));
                cboxAnalyser.setItems(FXCollections.observableList(list.stream().filter(p -> p.getDeviceType() == (short) 1).collect(Collectors.toList())));
                cboxAnalyser2.setItems(FXCollections.observableList(list.stream().filter(p -> p.getDeviceType() == (short) 1).collect(Collectors.toList())));
                cboxAnalyser3.setItems(FXCollections.observableList(list.stream().filter(p -> p.getDeviceType() == (short) 1).collect(Collectors.toList())));
                cboxAnalyser4.setItems(FXCollections.observableList(list.stream().filter(p -> p.getDeviceType() == (short) 1).collect(Collectors.toList())));
                cboxDisplay.setItems(FXCollections.observableList(list.stream().filter(p -> p.getDeviceType() == (short) 2).collect(Collectors.toList())));
                cboxSplitter.setItems(FXCollections.observableList(list.stream().filter(p -> p.getDeviceType() == (short) 3).collect(Collectors.toList())));

                // load prev
                var task1 = new HardwareDeviceConfigLoadTask();
                task1.setOnSucceeded(e1 -> {
                    try {
                        List<HardwareDeviceConfig> list1 = task1.get();
                        if (list1 == null || list1.isEmpty())
                            return;

                        setupPreviousSelection(list1.stream().filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo())).collect(Collectors.toList()));
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task1).start();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                    cboxMilkType2.setItems(FXCollections.observableList(list));
                    cboxMilkType3.setItems(FXCollections.observableList(list));
                    cboxMilkType4.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        try {
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            for (PrintService service : services) {
                cboxPrinter.getItems().add(service.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPreviousSelection(List<HardwareDeviceConfig> list) {
        HardwareDeviceConfig configWs = list.stream().filter(p -> p.getDeviceType().equals(WS)).findFirst().orElse(null);
        if (configWs != null) {
            cboxWs.setValue(configWs.getHardwareDevice());
            cboxWsPort.setValue(configWs.getCommPort());
        }

        HardwareDeviceConfig configAnalyser = list.stream().filter(p -> p.getDeviceType().equals(ANALYSER)).findFirst().orElse(null);
        if (configAnalyser != null) {
            cboxAnalyser.setValue(configAnalyser.getHardwareDevice());
            cboxAnalyserPort.setValue(configAnalyser.getCommPort());
            cboxQualityMachine.setValue(cboxQualityMachine.getItems().get((configAnalyser.getAnalyserModeType() < 0 ? 0 : configAnalyser.getAnalyserModeType())));
//            cboxQualityMachine.setValue(cboxQualityMachine.getItems().get(configAnalyser.getAnalyserModeType()));
            cboxMilkType.setValue(cboxMilkType.getItems().stream().filter(milkType -> milkType.getCode().equals(configAnalyser.getAnalyserMilkType())).findAny().orElse(null));
        }

        HardwareDeviceConfig configAnalyser2 = list.stream().filter(p -> p.getDeviceType().equals(ANALYSER2)).findFirst().orElse(null);
        if (configAnalyser2 != null) {
            cboxAnalyser2.setValue(configAnalyser2.getHardwareDevice());
            cboxAnalyserPort2.setValue(configAnalyser2.getCommPort());
            cboxQualityMachine2.setValue(cboxQualityMachine2.getItems().get((configAnalyser2.getAnalyserModeType() < 0 ? 0 : configAnalyser2.getAnalyserModeType())));
//            cboxQualityMachine2.setValue(cboxQualityMachine2.getItems().get(configAnalyser2.getAnalyserModeType()));
            cboxMilkType2.setValue(cboxMilkType2.getItems().stream().filter(milkType -> milkType.getCode().equals(configAnalyser2.getAnalyserMilkType())).findAny().orElse(null));
        }
        HardwareDeviceConfig configAnalyser3 = list.stream().filter(p -> p.getDeviceType().equals(ANALYSER3)).findFirst().orElse(null);
        if (configAnalyser3 != null) {
            cboxAnalyser3.setValue(configAnalyser3.getHardwareDevice());
            cboxAnalyserPort3.setValue(configAnalyser3.getCommPort());
            cboxQualityMachine3.setValue(cboxQualityMachine3.getItems().get((configAnalyser3.getAnalyserModeType() < 0 ? 0 : configAnalyser3.getAnalyserModeType())));
//            cboxQualityMachine3.setValue(cboxQualityMachine3.getItems().get(configAnalyser3.getAnalyserModeType()));
            cboxMilkType3.setValue(cboxMilkType3.getItems().stream().filter(milkType -> milkType.getCode().equals(configAnalyser3.getAnalyserMilkType())).findAny().orElse(null));

        }
        HardwareDeviceConfig configAnalyser4 = list.stream().filter(p -> p.getDeviceType().equals(ANALYSER4)).findFirst().orElse(null);
        if (configAnalyser4 != null) {
            cboxAnalyser4.setValue(configAnalyser4.getHardwareDevice());
            cboxAnalyserPort4.setValue(configAnalyser4.getCommPort());
            cboxQualityMachine4.setValue(cboxQualityMachine4.getItems().get((configAnalyser4.getAnalyserModeType() < 0 ? 0 : configAnalyser4.getAnalyserModeType())));
//            cboxQualityMachine4.setValue(cboxQualityMachine4.getItems().get(configAnalyser4.getAnalyserModeType()));
            cboxMilkType4.setValue(cboxMilkType4.getItems().stream().filter(milkType -> milkType.getCode().equals(configAnalyser4.getAnalyserMilkType())).findAny().orElse(null));
        }

        HardwareDeviceConfig configDisp = list.stream().filter(p -> p.getDeviceType().equals(DISPLAY)).findFirst().orElse(null);
        if (configDisp != null) {
            cboxDisplay.setValue(configDisp.getHardwareDevice());
            cboxDisplayPort.setValue(configDisp.getCommPort());
        }

        HardwareDeviceConfig configSplitter = list.stream().filter(p -> p.getDeviceType().equals(SPLITTER)).findFirst().orElse(null);
        if (configSplitter != null) {
            cboxSplitter.setValue(configSplitter.getHardwareDevice());
            cboxSplitterPort.setValue(configSplitter.getCommPort());
        }

        HardwareDeviceConfig configPrinter = list.stream().filter(p -> p.getDeviceType().equals(PRINTER)).findFirst().orElse(null);
        if (configPrinter != null) {
            cboxPrinter.setValue(configPrinter.getxCol1());
        }
//        try {
//            cboxQualityMachine.getSelectionModel().select(arrQuality[Integer.parseInt(MainApp.getProperty("masetting", "")) - 1]);
//        } catch (Exception e) {
//            cboxQualityMachine.getSelectionModel().select(0);
//        }
//        try {
//            cboxQualityMachine2.getSelectionModel().select(arrQuality[Integer.parseInt(MainApp.getProperty("masetting", "")) - 1]);
//        } catch (Exception e) {
//            cboxQualityMachine2.getSelectionModel().select(0);
//        }
//        try {
//            cboxQualityMachine3.getSelectionModel().select(arrQuality[Integer.parseInt(MainApp.getProperty("masetting", "")) - 1]);
//        } catch (Exception e) {
//            cboxQualityMachine3.getSelectionModel().select(0);
//        }
//        try {
//            cboxQualityMachine4.getSelectionModel().select(arrQuality[Integer.parseInt(MainApp.getProperty("masetting", "")) - 1]);
//        } catch (Exception e) {
//            cboxQualityMachine4.getSelectionModel().select(0);
//        }
    }

    public void setComboBox() {
        cboxWs.setConverter(new HardwareDeviceConvertor(cboxWs));
        cboxAnalyser.setConverter(new HardwareDeviceConvertor(cboxAnalyser));
        cboxAnalyser2.setConverter(new HardwareDeviceConvertor(cboxAnalyser2));
        cboxAnalyser3.setConverter(new HardwareDeviceConvertor(cboxAnalyser3));
        cboxAnalyser4.setConverter(new HardwareDeviceConvertor(cboxAnalyser4));
        cboxDisplay.setConverter(new HardwareDeviceConvertor(cboxDisplay));
        cboxSplitter.setConverter(new HardwareDeviceConvertor(cboxSplitter));
//        cboxSplitter.setConverter(new HardwareDeviceConvertor(cboxSplitter));
        cboxQualityMachine.setItems(FXCollections.observableList(Arrays.asList(arrQuality)));
        cboxQualityMachine2.setItems(FXCollections.observableList(Arrays.asList(arrQuality)));
        cboxQualityMachine3.setItems(FXCollections.observableList(Arrays.asList(arrQuality)));
        cboxQualityMachine4.setItems(FXCollections.observableList(Arrays.asList(arrQuality)));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMilkType2.setConverter(new MilkTypeConvertor(cboxMilkType2));
        cboxMilkType3.setConverter(new MilkTypeConvertor(cboxMilkType3));
        cboxMilkType4.setConverter(new MilkTypeConvertor(cboxMilkType4));
    }

    public void saveData() {
        var task = new HardwareDeviceConfigSaveTask(deviceConfigList);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("hardwaredeviceconfig"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("hardwaredeviceconfig"),
                        resourceBundle.getString("hardwaredeviceconfig.insert.successful"));
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("hardwaredeviceconfig"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        setValuesInObject();
        saveData();
    }

    private boolean validate() {
        errorMsg = new StringBuilder();
        if (!cboxWsPort.getValue().equalsIgnoreCase("NA") && cboxWs.getValue() == null)
            errorMsg.append(resourceBundle.getString("ws.cannot.null" + "\n"));
        if (!cboxAnalyserPort.getValue().equalsIgnoreCase("NA") && cboxAnalyser.getValue() == null)
            errorMsg.append(resourceBundle.getString("analyser.cannot.null" + "\n"));
        if (!cboxAnalyserPort2.getValue().equalsIgnoreCase("NA") && cboxAnalyser2.getValue() == null)
            errorMsg.append(resourceBundle.getString("analyser.cannot.null" + "\n"));
        if (!cboxAnalyserPort3.getValue().equalsIgnoreCase("NA") && cboxAnalyser3.getValue() == null)
            errorMsg.append(resourceBundle.getString("analyser.cannot.null" + "\n"));
        if (!cboxAnalyserPort4.getValue().equalsIgnoreCase("NA") && cboxAnalyser4.getValue() == null)
            errorMsg.append(resourceBundle.getString("analyser.cannot.null" + "\n"));
        if (!cboxDisplayPort.getValue().equalsIgnoreCase("NA") && cboxDisplay.getValue() == null)
            errorMsg.append(resourceBundle.getString("display.cannot.null" + "\n"));
        if (!cboxSplitterPort.getValue().equalsIgnoreCase("NA") && cboxSplitter.getValue() == null)
            errorMsg.append(resourceBundle.getString("splitter.cannot.null" + "\n"));

        Map<String, Integer> map = new HashMap<>();
        if (map.get(cboxWsPort.getValue()) == null)
            map.put(cboxWsPort.getValue(), 1);
        else
            map.put(cboxWsPort.getValue(), map.get(cboxWsPort.getValue()) + 1);

        if (map.get(cboxAnalyserPort.getValue()) == null)
            map.put(cboxAnalyserPort.getValue(), 1);
        else
            map.put(cboxAnalyserPort.getValue(), map.get(cboxAnalyserPort.getValue()) + 1);

        if (map.get(cboxDisplayPort.getValue()) == null)
            map.put(cboxDisplayPort.getValue(), 1);
        else
            map.put(cboxDisplayPort.getValue(), map.get(cboxDisplayPort.getValue()) + 1);

        map.forEach((k, v) -> {
            if (!k.equals("NA") && v > 1) {
                errorMsg.append(k + " " + resourceBundle.getString("cannot.be.duplicate"));
            }
        });
        return errorMsg.length() == 0;
    }

    private void setValuesInObject() {
        deviceConfigList = new ArrayList<>();

        HardwareDeviceConfig ews = new HardwareDeviceConfig();
        ews.setAnalyserModeType(0);
        ews.setAnalyserMilkType(0);
        setDefaultValues(ews, cboxWsPort.getValue().equalsIgnoreCase("NA") ? null : cboxWs.getValue(), WS, cboxWsPort.getValue());
        deviceConfigList.add(ews);

        HardwareDeviceConfig analyser = new HardwareDeviceConfig();
        analyser.setAnalyserModeType(cboxQualityMachine.getSelectionModel().getSelectedIndex());
        analyser.setAnalyserMilkType(cboxMilkType.getValue() != null ? cboxMilkType.getValue().getCode() : 0);
        setDefaultValues(analyser, cboxAnalyserPort.getValue().equalsIgnoreCase("NA") ? null : cboxAnalyser.getValue(), ANALYSER, cboxAnalyserPort.getValue());
        deviceConfigList.add(analyser);

        HardwareDeviceConfig analyser2 = new HardwareDeviceConfig();
        analyser2.setAnalyserModeType(cboxQualityMachine2.getSelectionModel().getSelectedIndex());
        analyser2.setAnalyserMilkType(cboxMilkType2.getValue() != null ? cboxMilkType2.getValue().getCode() : 0);
        setDefaultValues(analyser2, cboxAnalyserPort2.getValue().equalsIgnoreCase("NA") ? null : cboxAnalyser2.getValue(), ANALYSER2, cboxAnalyserPort2.getValue());
        deviceConfigList.add(analyser2);

        HardwareDeviceConfig analyser3 = new HardwareDeviceConfig();
        analyser3.setAnalyserModeType(cboxQualityMachine3.getSelectionModel().getSelectedIndex());
        analyser3.setAnalyserMilkType(cboxMilkType3.getValue() != null ? cboxMilkType3.getValue().getCode() : 0);
        setDefaultValues(analyser3, cboxAnalyserPort3.getValue().equalsIgnoreCase("NA") ? null : cboxAnalyser3.getValue(), ANALYSER3, cboxAnalyserPort3.getValue());
        deviceConfigList.add(analyser3);

        HardwareDeviceConfig analyser4 = new HardwareDeviceConfig();
        analyser4.setAnalyserModeType(cboxQualityMachine4.getSelectionModel().getSelectedIndex());
        analyser4.setAnalyserMilkType(cboxMilkType4.getValue() != null ? cboxMilkType4.getValue().getCode() : 0);
        setDefaultValues(analyser4, cboxAnalyserPort4.getValue().equalsIgnoreCase("NA") ? null : cboxAnalyser4.getValue(), ANALYSER4, cboxAnalyserPort4.getValue());
        deviceConfigList.add(analyser4);

        HardwareDeviceConfig display = new HardwareDeviceConfig();
        display.setAnalyserModeType(0);
        display.setAnalyserMilkType(0);
        setDefaultValues(display, cboxDisplayPort.getValue().equalsIgnoreCase("NA") ? null : cboxDisplay.getValue(), DISPLAY, cboxDisplayPort.getValue());
        deviceConfigList.add(display);

        HardwareDeviceConfig splitter = new HardwareDeviceConfig();
        splitter.setAnalyserModeType(0);
        splitter.setAnalyserMilkType(0);
        setDefaultValues(splitter, cboxSplitterPort.getValue().equalsIgnoreCase("NA") ? null : cboxSplitter.getValue(), SPLITTER, cboxSplitterPort.getValue());
        deviceConfigList.add(splitter);

        HardwareDeviceConfig printer = new HardwareDeviceConfig();
        printer.setAnalyserModeType(0);
        printer.setAnalyserMilkType(0);
        setPrinterValues(printer, cboxPrinter.getValue(), PRINTER);
        deviceConfigList.add(printer);

    }

    private void setDefaultValues(HardwareDeviceConfig hdc, HardwareDevice device, String deviceType, String commport) {
        hdc.setHardwareDevice(device);
        hdc.setDeviceType(deviceType);
        hdc.setCommPort(commport);
    }

    private void setPrinterValues(HardwareDeviceConfig hdc, String deviceName, String deviceType) {
        hdc.setxCol1(deviceName);
        hdc.setDeviceType(deviceType);
        hdc.setHardwareDevice(null);
    }
}