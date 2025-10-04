package com.eipl.amcs.master.geo.converter;

import com.eipl.amcs.master.geo.model.Village;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class VillageConvertor extends StringConverter<Village> {

    private final ComboBox<Village> cbox;

    public VillageConvertor(ComboBox<Village> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Village village) {
        if (village == null)
            return null;
        return village.toString();
    }

    @Override
    public Village fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
