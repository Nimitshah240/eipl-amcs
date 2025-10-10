package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.LedgerType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class LedgerTypeConvertor extends StringConverter<LedgerType> {

    private final ComboBox<LedgerType> cbox;

    public LedgerTypeConvertor(ComboBox<LedgerType> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(LedgerType LedgerType) {
        if (LedgerType == null)
            return null;
        return LedgerType.toString();
    }

    @Override
    public LedgerType fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
