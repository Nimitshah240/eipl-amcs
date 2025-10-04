package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.SubLedger;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class SubLedgerConvertor extends StringConverter<SubLedger> {

    private final ComboBox<SubLedger> cbox;

    public SubLedgerConvertor(ComboBox<SubLedger> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(SubLedger SubLedger) {
        if (SubLedger == null)
            return null;
        return SubLedger.toString();
    }

    @Override
    public SubLedger fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
