package com.eipl.amcs.operation.administartion.converter;

import com.eipl.amcs.master.account.model.Designation;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class DesignationConvertor extends StringConverter<Designation> {

    private final ComboBox<Designation> cbox;

    public DesignationConvertor(ComboBox<Designation> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Designation designation) {
        if (designation == null)
            return null;
        return designation.toString();
    }

    @Override
    public Designation fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
