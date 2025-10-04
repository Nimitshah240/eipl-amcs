package com.eipl.amcs.master.geo.converter;

import com.eipl.amcs.master.geo.model.Hamlet;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class HamletConvertor extends StringConverter<Hamlet> {

    private final ComboBox<Hamlet> cbox;

    public HamletConvertor(ComboBox<Hamlet> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Hamlet hamlet) {
        if (hamlet == null)
            return null;
        return hamlet.toString();
    }

    @Override
    public Hamlet fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
