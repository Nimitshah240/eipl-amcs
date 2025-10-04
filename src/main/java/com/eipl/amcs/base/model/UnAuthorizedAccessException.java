package com.eipl.amcs.base.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.controls.alert.MyAlertFacade;
import javafx.scene.control.Alert;

public class UnAuthorizedAccessException extends RuntimeException{

        public UnAuthorizedAccessException() {
            super(MainApp.bundle.getString("unauthorized.access"));
            MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.stage,
                    MainApp.bundle.getString("unauthorized.access.title"), MainApp.bundle.getString("unauthorized.access"));
        }

        public UnAuthorizedAccessException(String message) {
            super(message);
            MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.stage,
                    MainApp.bundle.getString("unauthorized.access.title"), MainApp.bundle.getString("unauthorized.access"));
        }

        public UnAuthorizedAccessException(String message, Throwable caues) {
            super(message, caues);
            MyAlertFacade.createAlert(Alert.AlertType.WARNING, MainApp.stage,
                    MainApp.bundle.getString("unauthorized.access.title"), MainApp.bundle.getString("unauthorized.access"));
        }

        @Override
        public String getMessage() {
            return super.getMessage();
        }
    }


