package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.Ledger;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class LedgerConvertor extends StringConverter<Ledger> {

    private final ComboBox<Ledger> cbox;

    public LedgerConvertor(ComboBox<Ledger> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Ledger Ledger) {
        if (Ledger == null)
            return null;
        return Ledger.toString();
    }

    @Override
    public Ledger fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equalsIgnoreCase(p.toString())).findAny().orElse(null);
    }
}
