package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Union;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class UnionConvertor extends StringConverter<Union> {

    private final ComboBox<Union> cbox;

    public UnionConvertor(ComboBox<Union> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Union union) {
        if (union == null)
            return null;
        return union.toString();
    }

    @Override
    public Union fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
