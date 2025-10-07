package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.operation.model.BillHead;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class BillHeadConvertor extends StringConverter<BillHead> {

    private final ComboBox<BillHead> cbox;

    public BillHeadConvertor(ComboBox<BillHead> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(BillHead billHead) {
        if (billHead == null)
            return null;
        return billHead.getName();
    }

    @Override
    public BillHead fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.getName())).findAny().orElse(null);
    }
}
