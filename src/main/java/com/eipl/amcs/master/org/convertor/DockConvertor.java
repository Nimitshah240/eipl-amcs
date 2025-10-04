package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Dock;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class DockConvertor extends StringConverter<Dock> {

    private final ComboBox<Dock> cbox;

    public DockConvertor(ComboBox<Dock> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Dock dock) {
        if (dock == null)
            return null;
        return dock.toString();
    }

    @Override
    public Dock fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
