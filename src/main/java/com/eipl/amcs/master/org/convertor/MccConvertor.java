package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Mcc;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MccConvertor extends StringConverter<Mcc> {

    private final ComboBox<Mcc> cbox;

    public MccConvertor(ComboBox<Mcc> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Mcc mcc) {
        if (mcc == null)
            return null;
        return mcc.toString();
    }

    @Override
    public Mcc fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
