package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.master.global.model.MilkQualityType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MilkQualityConvertor extends StringConverter<MilkQualityType> {

    private final ComboBox<MilkQualityType> cbox;

    public MilkQualityConvertor(ComboBox<MilkQualityType> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(MilkQualityType Unit) {
        if (Unit == null)
            return null;
        return Unit.toString();
    }

    @Override
    public MilkQualityType fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
