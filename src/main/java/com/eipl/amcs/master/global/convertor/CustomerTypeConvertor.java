package com.eipl.amcs.master.global.convertor;

import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class CustomerTypeConvertor extends StringConverter<CustomerTypeKeyValDto> {

    private final ComboBox<CustomerTypeKeyValDto> cbox;

    public CustomerTypeConvertor(ComboBox<CustomerTypeKeyValDto> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(CustomerTypeKeyValDto obj) {
        if (obj == null)
            return null;
        return obj.toString();
    }

    @Override
    public CustomerTypeKeyValDto fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
