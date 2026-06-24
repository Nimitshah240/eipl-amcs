package com.eipl.amcs.controls;

import com.eipl.amcs.utils.FormatterFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

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
     */
    public E_NumericField() {
        try {
            TextFormatter<Number> txtInputFormatter = FormatterFactory.createNumericFormatter();
            setTextFormatter(txtInputFormatter);
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
