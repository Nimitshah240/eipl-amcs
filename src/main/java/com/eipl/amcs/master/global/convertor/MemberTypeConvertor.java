package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.master.global.model.MemberType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MemberTypeConvertor extends StringConverter<MemberType> {

    private final ComboBox<MemberType> cbox;

    public MemberTypeConvertor(ComboBox<MemberType> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(MemberType memberType) {
        if (memberType == null)
            return null;
        return memberType.toString();
    }

    @Override
    public MemberType fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
