package com.eipl.amcs.master.account.converter;

import com.eipl.amcs.master.account.model.FinancialYear;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class FinancialYearConvertor extends StringConverter<FinancialYear> {

    private final ComboBox<FinancialYear> cbox;

    public FinancialYearConvertor(ComboBox<FinancialYear> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(FinancialYear FinancialYear) {
        if (FinancialYear == null)
            return null;
        return FinancialYear.toString();
    }

    @Override
    public FinancialYear fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
