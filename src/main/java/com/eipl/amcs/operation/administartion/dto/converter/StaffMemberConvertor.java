package com.eipl.amcs.operation.administartion.dto.converter;

import com.eipl.amcs.operation.administartion.dto.StaffMember;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class StaffMemberConvertor extends StringConverter<StaffMember> {

    private final ComboBox<StaffMember> cbox;

    public StaffMemberConvertor(ComboBox<StaffMember> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(StaffMember staffMember) {
        if (staffMember == null)
            return null;
        return staffMember.getName();
    }

    @Override
    public StaffMember fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.getName())).findAny().orElse(null);
    }
}
