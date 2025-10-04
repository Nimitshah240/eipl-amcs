package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.operation.model.Formula;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class FormulaConvertor extends StringConverter<Formula> {

    private final ComboBox<Formula> cbox;

    public FormulaConvertor(ComboBox<Formula> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Formula formula) {
        try {
            if (formula == null)
                return null;
            return formula.getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Formula fromString(String s) {
        try {
            if (s == null || s.isEmpty())
                return null;
            return cbox.getItems().stream().filter(p -> s.equals(p.getName())).findAny().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
