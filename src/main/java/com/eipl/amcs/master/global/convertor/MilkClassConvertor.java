package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.master.global.model.MilkClass;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MilkClassConvertor extends StringConverter<MilkClass> {

    private final ComboBox<MilkClass> cbox;

    public MilkClassConvertor(ComboBox<MilkClass> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(MilkClass Unit) {
        if (Unit == null)
            return null;
        return Unit.toString();
    }

    @Override
    public MilkClass fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
