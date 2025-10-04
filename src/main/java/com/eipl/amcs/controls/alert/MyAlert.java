package com.eipl.amcs.controls.alert;

import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public abstract class MyAlert {
    protected Stage stage;
    protected String title;
    protected String message;

    public MyAlert() {

    }

    public MyAlert(Stage stage, String title, String message) {
        this.stage = stage;
        this.title = title;
        this.message = message;
    }

    public abstract void createAlert();

    public abstract Optional<ButtonType> createConfirmationAlert();

    public abstract Optional<ButtonType> createYesNoConfirmationAlert();

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
