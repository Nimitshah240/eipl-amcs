package com.eipl.amcs.controls;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                if (object == null)
                    return null;
                return object.format(FMT);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty())
                    return null;
                return LocalDate.parse(string, FMT);
            }
        });

        focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue && getValue() != null) {
                Platform.runLater(() -> getEditor().selectAll());
            }
        });
    }
}
