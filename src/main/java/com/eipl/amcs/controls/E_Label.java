package com.eipl.amcs.controls;

import com.eipl.amcs.utils.FormatterFactory;
import javafx.scene.control.Label;

public class E_Label extends Label {

    private boolean localized = true;

    public E_Label() {
        super();
        initLocalizationListener();
    }

    public E_Label(String text) {
        super(FormatterFactory.convertEnglishToLocalizedDigits(text));
        initLocalizationListener();
    }

    /**
     * Listens to any text changes on the label and localizes digits on the fly.
     */
    private void initLocalizationListener() {
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isLocalized() || newValue == null || newValue.isEmpty()) {
                return;
            }
            String localizedText = FormatterFactory.convertEnglishToLocalizedDigits(newValue);
            if (!newValue.equals(localizedText)) {
                setText(localizedText);
            }
        });
    }

    public boolean isLocalized() {
        return localized;
    }

    public void setLocalized(boolean localized) {
        this.localized = localized;
        if (localized && getText() != null) {
            setText(FormatterFactory.convertEnglishToLocalizedDigits(getText()));
        }
    }
}