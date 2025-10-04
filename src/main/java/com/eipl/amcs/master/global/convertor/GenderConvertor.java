package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.master.global.model.Gender;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class GenderConvertor extends StringConverter<Gender> {

    private final ComboBox<Gender> cbox;

    public GenderConvertor(ComboBox<Gender> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Gender Gender) {
        if (Gender == null)
            return null;
        return Gender.toString();
    }

    @Override
    public Gender fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
