package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Bank;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class BankConvertor extends StringConverter<Bank> {

    private final ComboBox<Bank> cbox;

    public BankConvertor(ComboBox<Bank> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Bank bank) {
        if (bank == null)
            return null;
        return bank.toString();
    }

    @Override
    public Bank fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
