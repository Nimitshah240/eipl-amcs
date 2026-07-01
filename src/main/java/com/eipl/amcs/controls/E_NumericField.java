package com.eipl.amcs.controls;

import com.eipl.amcs.utils.FormatterFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Locale;

import static com.eipl.amcs.MainApp.getCurrentLocale;

public class E_NumericField extends TextField {
    private final String PATTERN_INT = "[0-9]*";
    private final String PATTERN_REAL = "[0-9]+(\\.[0-9]{0,3})?";
    private final IntegerProperty maxLength;
    private final BooleanProperty real;

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 24/06/2026    Nimit             1.0.0      Added method to get locale number input in English in backend.
     */
    public String getInputText() {
        try {
            String rawText = getText();
            if (rawText == null || rawText.trim().isEmpty()) {
                return "";
            }
            return FormatterFactory.convertLocalizedToEnglishDigits(rawText.trim());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 24/06/2026    Nimit             1.0.1      Added method to get locale number input in English in backend.
     * 26/06/2026    Nimit             1.0.2      Added Listener to change focus on Ctrl + Arrow Click.
     * 30/06/2026    Nimit             1.0.3      Resolve bug of removing prefix zero.
     */
    public E_NumericField() {
        try {
//            TextFormatter<Number> txtInputFormatter = FormatterFactory.createNumericFormatter();
            TextFormatter<String> txtInputFormatter = FormatterFactory.createNumericFormatter();
            setTextFormatter(txtInputFormatter);
            txtInputFormatter.setValue(null);
            this.clear();
            this.maxLength = new SimpleIntegerProperty(Integer.MAX_VALUE);
            this.real = new SimpleBooleanProperty(true);

            setOnKeyReleased(new FocusHandler());

            Locale currentLocale = getCurrentLocale();
            if (currentLocale.getLanguage().equalsIgnoreCase("en")) {
                textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.isEmpty()) {
                        if (newValue.length() > getMaxLength()) {
                            setText(oldValue);
                        } else {
                            if (newValue.matches(getPattern()))
                                setText(newValue);
                            else
                                setText(oldValue);
                        }
                    }
                });
            }

            addEventFilter(KeyEvent.KEY_RELEASED, event -> {
                switch (event.getCode()) {
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
                    default:
                        break;
                }
            });
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPattern() {
        return isReal() ? PATTERN_REAL : PATTERN_INT;
    }

    public IntegerProperty maxLengthProperty() {
        return this.maxLength;
    }

    public int getMaxLength() {
        return this.maxLengthProperty().get();
    }

    public void setMaxLength(final int maxLength) {
        this.maxLengthProperty().set(maxLength);
    }

    public BooleanProperty realProperty() {
        return this.real;
    }

    public boolean isReal() {
        return this.realProperty().get();
    }

    public void setReal(final boolean real) {
        this.realProperty().set(real);
    }
}
