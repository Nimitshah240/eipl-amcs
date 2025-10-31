package com.eipl.amcs.controls.alert;

import com.eipl.amcs.MainApp;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class WarningAlert extends MyAlert {

    public WarningAlert() {

    }

    public WarningAlert(Stage stage, String title, String message) {
        super(stage, title, message);
    }

    @Override
    public void createAlert() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.getDialogPane().getStylesheets().add(MainApp.class.getResource("view/dialog.css").toExternalForm());
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setTitle(getTitle());
        alert.setHeaderText(getTitle());
        alert.initOwner(getStage());
        alert.setResizable(true);


        Label messageLabel = new Label(getMessage());
        messageLabel.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(messageLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(200);
        scrollPane.setMaxHeight(300);
        scrollPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        alert.getDialogPane().setContent(scrollPane);

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
