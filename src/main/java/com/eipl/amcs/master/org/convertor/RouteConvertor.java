package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Route;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class RouteConvertor extends StringConverter<Route> {

    private final ComboBox<Route> cbox;

    public RouteConvertor(ComboBox<Route> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Route route) {
        if (route == null)
            return null;
        return route.toString();
    }

    @Override
    public Route fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
