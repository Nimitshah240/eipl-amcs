package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.master.global.model.MilkType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MilkTypeConvertor extends StringConverter<MilkType> {

    private final ComboBox<MilkType> cbox;

    public MilkTypeConvertor(ComboBox<MilkType> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(MilkType Unit) {
        if (Unit == null)
            return null;
        return Unit.toString();
    }

    @Override
    public MilkType fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
