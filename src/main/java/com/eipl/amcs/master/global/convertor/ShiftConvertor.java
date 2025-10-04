package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.master.global.model.Shift;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class ShiftConvertor extends StringConverter<Shift> {

    private final ComboBox<Shift> cbox;

    public ShiftConvertor(ComboBox<Shift> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Shift Shift) {
        if (Shift == null)
            return null;
        return Shift.toString();
    }

    @Override
    public Shift fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
