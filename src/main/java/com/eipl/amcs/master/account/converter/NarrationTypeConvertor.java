package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.NarrationType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class NarrationTypeConvertor extends StringConverter<NarrationType> {

    private final ComboBox<NarrationType> cbox;

    public NarrationTypeConvertor(ComboBox<NarrationType> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(NarrationType NarrationType) {
        if (NarrationType == null)
            return null;
        return NarrationType.toString();
    }

    @Override
    public NarrationType fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equalsIgnoreCase(p.toString())).findAny().orElse(null);
    }
}
