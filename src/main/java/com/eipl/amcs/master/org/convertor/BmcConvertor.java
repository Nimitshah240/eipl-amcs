package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Bmc;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class BmcConvertor extends StringConverter<Bmc> {

    private final ComboBox<Bmc> cbox;

    public BmcConvertor(ComboBox<Bmc> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Bmc bmc) {
        if (bmc == null)
            return null;
        return bmc.toString();
    }

    @Override
    public Bmc fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
