package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.VoucherType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class VoucherTypeConvertor extends StringConverter<VoucherType> {

    private final ComboBox<VoucherType> cbox;

    public VoucherTypeConvertor(ComboBox<VoucherType> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(VoucherType VoucherType) {
        if (VoucherType == null)
            return null;
        return VoucherType.toString();
    }

    @Override
    public VoucherType fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
