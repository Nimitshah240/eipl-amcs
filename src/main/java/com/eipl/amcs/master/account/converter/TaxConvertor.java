package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.Tax;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class TaxConvertor extends StringConverter<Tax> {

    private final ComboBox<Tax> cbox;

    public TaxConvertor(ComboBox<Tax> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Tax tax) {
        if (tax == null)
            return null;
        return tax.toString();
    }

    @Override
    public Tax fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
