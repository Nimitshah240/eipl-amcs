package com.eipl.amcs.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

public class E_NumericField extends TextField {
    private IntegerProperty maxLength;
    private BooleanProperty real;
    private final String PATTERN_INT = "[0-9]*";
    private final String PATTERN_REAL = "[0-9]+(\\.[0-9]{0,3})?";

    public E_NumericField() {
        this.maxLength = new SimpleIntegerProperty(Integer.MAX_VALUE);
        this.real = new SimpleBooleanProperty(true);

        setOnKeyReleased(new FocusHandler());

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
