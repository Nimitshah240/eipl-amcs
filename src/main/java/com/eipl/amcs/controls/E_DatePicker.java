package com.eipl.amcs.controls;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.utils.FormatterFactory;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeParseException;

import static com.eipl.amcs.utils.AppConstant.DATE_FORMATTER_LOCALE;
import static com.eipl.amcs.utils.FormatterFactory.convertEnglishToLocalizedDigits;
import static com.eipl.amcs.utils.FormatterFactory.convertLocalizedToEnglishDigits;

public class E_DatePicker extends DatePicker {

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 24/06/2026    Nimit             1.0.1      Added things to localised number inside DatePicker.
     */
    public E_DatePicker() {
        setPromptText("DD/MM/YYYY");
        setOnKeyReleased(e -> {
            if (getValue() == null)
                e.consume();
            else
                new FocusHandler();
        });

        setChronology(Chronology.ofLocale(MainApp.getCurrentLocale()));

        // --- FIX FOR POP-UP CALENDAR GRID DIGITS ---
        setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            // Convert the day number string (e.g. "15") into Gujarati digits ("૧૫")
                            String localizedDayNum = convertEnglishToLocalizedDigits(String.valueOf(item.getDayOfMonth()));
                            setText(localizedDayNum);
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        // ------------------------------------------

        getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                String localizedText = convertEnglishToLocalizedDigits(newValue);

                if (!newValue.equals(localizedText)) {
                    int caretPosition = getEditor().getCaretPosition();
                    getEditor().setText(localizedText);
                    getEditor().positionCaret(caretPosition);
                }
            }
        });

        setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate object) {
                if (object == null) return "";
                return FormatterFactory.formatDate(object);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) return null;

                String text = string.trim();
                text = convertLocalizedToEnglishDigits(text);
                if (text.matches("\\d{8}")) {
                    text = text.substring(0, 2) + "/" + text.substring(2, 4) + "/" + text.substring(4);
                }

                try {
                    return LocalDate.parse(text, DATE_FORMATTER_LOCALE);
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
                    break;
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
