package com.eipl.amcs.master.geo.converter;

import com.eipl.amcs.master.geo.model.District;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class DistrictConvertor extends StringConverter<District> {

    private final ComboBox<District> cbox;

    public DistrictConvertor(ComboBox<District> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(District district) {
        if (district == null)
            return null;
        return district.toString();
    }

    @Override
    public District fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
