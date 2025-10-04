package com.eipl.amcs.operation.procurement.serial.exception;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.controls.alert.MyAlertFacade;
import javafx.scene.control.Alert;

public class CommportNotBindException extends RuntimeException {

    public CommportNotBindException() {
        super("Comm port is not available");
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Comm port is not available");
    }

    public CommportNotBindException(String message) {
        super(message);
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Comm port is not available");
    }

    public CommportNotBindException(String message, Throwable caues) {
        super(message, caues);
        MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.getStage(), "Hardware Device",
                "Comm port is not available");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}