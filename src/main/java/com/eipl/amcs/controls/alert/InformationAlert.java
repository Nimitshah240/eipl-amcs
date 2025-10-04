package com.eipl.amcs.controls.alert;

import com.eipl.amcs.MainApp;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class InformationAlert extends MyAlert {

    public InformationAlert() {

    }

    public InformationAlert(Stage stage, String title, String message) {
        super(stage, title, message);
    }

    @Override
    public void createAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("view/dialog.css").toExternalForm());
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setHeaderText(getTitle());
        alert.setContentText(getMessage());
        alert.setTitle(getTitle());
        alert.initOwner(getStage());
        alert.setResizable(true);
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
        alert.showAndWait();
    }

    @Override
    public Optional<ButtonType> createConfirmationAlert() {
        return null;
    }

    @Override
    public Optional<ButtonType> createYesNoConfirmationAlert() {
        return null;
    }
}
