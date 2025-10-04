package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.LedgerGroup;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class LedgerGroupConvertor extends StringConverter<LedgerGroup> {

    private final ComboBox<LedgerGroup> cbox;

    public LedgerGroupConvertor(ComboBox<LedgerGroup> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(LedgerGroup LedgerGroup) {
        if (LedgerGroup == null)
            return null;
        return LedgerGroup.toString();
    }

    @Override
    public LedgerGroup fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
