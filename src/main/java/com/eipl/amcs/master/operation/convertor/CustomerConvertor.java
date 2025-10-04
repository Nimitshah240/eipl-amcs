package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.operation.model.Customer;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class CustomerConvertor extends StringConverter<Customer> {

    private final ComboBox<Customer> cbox;

    public CustomerConvertor(ComboBox<Customer> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Customer Customer) {
        if (Customer == null)
            return null;
        return Customer.toString();
    }

    @Override
    public Customer fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
