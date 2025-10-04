package com.eipl.amcs.master.geo.converter;

import com.eipl.amcs.master.geo.model.SubDistrict;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class SubDistrictConvertor extends StringConverter<SubDistrict> {

    private final ComboBox<SubDistrict> cbox;

    public SubDistrictConvertor(ComboBox<SubDistrict> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(SubDistrict subDistrict) {
        if (subDistrict == null)
            return null;
        return subDistrict.toString();
    }

    @Override
    public SubDistrict fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
