package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.BasicTax;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class BasicTaxConvertor extends StringConverter<BasicTax> {

    private final ComboBox<BasicTax> cbox;

    public BasicTaxConvertor(ComboBox<BasicTax> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(BasicTax BasicTax) {
        if (BasicTax == null)
            return null;
        return BasicTax.toString();
    }

    @Override
    public BasicTax fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
