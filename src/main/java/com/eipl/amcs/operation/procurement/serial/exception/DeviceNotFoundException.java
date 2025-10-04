package com.eipl.amcs.operation.procurement.serial.exception;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.controls.alert.MyAlertFacade;
import javafx.scene.control.Alert;

public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException() {
        super("Device not found");
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Device not found");
    }

    public DeviceNotFoundException(String message) {
        super(message);
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Device not found");
    }

    public DeviceNotFoundException(String message, Throwable caues) {
        super(message, caues);
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Device not found");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}