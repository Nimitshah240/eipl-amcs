package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.master.global.model.Unit;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class UnitConvertor extends StringConverter<Unit> {

    private final ComboBox<Unit> cbox;

    public UnitConvertor(ComboBox<Unit> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Unit Unit) {
        if (Unit == null)
            return null;
        return Unit.toString();
    }

    @Override
    public Unit fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
