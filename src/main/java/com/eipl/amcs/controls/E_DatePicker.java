package com.eipl.amcs.controls;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class E_DatePicker extends DatePicker {
    final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public E_DatePicker() {
        setPromptText("DD/MM/YYYY");
        setOnKeyReleased(e -> {
            if (getValue() == null)
                e.consume();
            else
                new FocusHandler();
        });

        setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate object) {
                return (object == null) ? "" : object.format(FMT);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) return null;

                String text = string.trim();
                if (text.matches("\\d{8}")) {
                    text = text.substring(0, 2) + "/" + text.substring(2, 4) + "/" + text.substring(4);
                }

                try {
                    return LocalDate.parse(text, FMT);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });

        addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case ENTER:
                    String text = getEditor().getText();
                    LocalDate parsedDate = getConverter().fromString(text);
                    setValue(parsedDate);

                    if (getValue() != null) {
                        getEditor().setText(getConverter().toString(getValue()));
                    } else {
                        getEditor().clear();
                    }

                    this.fireEvent(new javafx.event.ActionEvent(this, null));
                    Platform.runLater(() -> {
                        getEditor().fireEvent(new KeyEvent(
                                KeyEvent.KEY_PRESSED, "", "",
                                KeyCode.TAB, false, false, false, false
                        ));
                    });
                    event.consume();

                case RIGHT:
                    if (event.isControlDown()) {

                        this.fireEvent(new javafx.event.ActionEvent(this, null));
                        Platform.runLater(() -> {
                            this.fireEvent(new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "",
                                    KeyCode.TAB, false, false, false, false
                            ));
                        });
                        event.consume();
                    }
                    break;
                case LEFT:
                    if (event.isControlDown()) {

                        this.fireEvent(new javafx.event.ActionEvent(this, null));
                        Platform.runLater(() -> {
                            this.fireEvent(new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "",
                                    KeyCode.TAB, true, false, false, false
                            ));
                        });
                        event.consume();
                    }
                    break;
            }
        });

        focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue && getValue() != null) {
                Platform.runLater(() -> getEditor().selectAll());
            }
        });
    }
}
