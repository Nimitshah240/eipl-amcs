package com.eipl.amcs.operation.procurement.serial.exception;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.controls.alert.MyAlertFacade;
import javafx.scene.control.Alert;

public class UnableToOpenSerialPort extends RuntimeException {

    public UnableToOpenSerialPort() {
        super("Unable to open comm port");
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Unable to open comm port");
    }

    public UnableToOpenSerialPort(String message) {
        super(message);
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Unable to open comm port");
    }

    public UnableToOpenSerialPort(String message, Throwable caues) {
        super(message, caues);
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Unable to open comm port");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}