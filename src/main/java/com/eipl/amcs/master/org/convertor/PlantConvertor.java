package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Plant;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class PlantConvertor extends StringConverter<Plant> {

    private final ComboBox<Plant> cbox;

    public PlantConvertor(ComboBox<Plant> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Plant plant) {
        if (plant == null)
            return null;
        return plant.toString();
    }

    @Override
    public Plant fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
