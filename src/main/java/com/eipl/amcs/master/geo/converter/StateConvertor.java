package com.eipl.amcs.master.geo.converter;

import com.eipl.amcs.master.geo.model.State;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class StateConvertor extends StringConverter<State> {

    private final ComboBox<State> cbox;

    public StateConvertor(ComboBox<State> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(State state) {
        if (state == null)
            return null;
        return state.toString();
    }

    @Override
    public State fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
