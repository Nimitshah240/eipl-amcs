package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Society;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class SocietyConvertor extends StringConverter<Society> {

    private final ComboBox<Society> cbox;

    public SocietyConvertor(ComboBox<Society> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Society society) {
        if (society == null)
            return null;
        return society.toString();
    }

    @Override
    public Society fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
