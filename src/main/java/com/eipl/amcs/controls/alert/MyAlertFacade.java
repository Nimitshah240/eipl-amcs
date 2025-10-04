package com.eipl.amcs.controls.alert;

import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MyAlertFacade {

    public static void createAlert(AlertType alertType, Stage owner, String title, String message) {
        if (alertType == AlertType.INFORMATION) {
            com.eipl.amcs.controls.alert.MyAlert alt = new com.eipl.amcs.controls.alert.InformationAlert(owner, title, message);
            alt.createAlert();
            alt = null;
        } else if (alertType == AlertType.ERROR) {
            com.eipl.amcs.controls.alert.MyAlert alt = new com.eipl.amcs.controls.alert.ErrorAlert(owner, title, message);
            alt.createAlert();
            alt = null;
        } else if (alertType == AlertType.WARNING) {
            com.eipl.amcs.controls.alert.MyAlert alt = new com.eipl.amcs.controls.alert.WarningAlert(owner, title, message);
            alt.createAlert();
            alt = null;
        } else if (alertType == AlertType.CONFIRMATION) {
            com.eipl.amcs.controls.alert.MyAlert alt = new com.eipl.amcs.controls.alert.ConfirmationAlert(owner, title, message);
            alt.createConfirmationAlert();
            alt = null;
        }
    }
}
