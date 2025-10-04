package com.eipl.amcs.master.procurement.converter;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class SocietyPaymentCycleConvertor extends StringConverter<SocietyPaymentCycle> {

    private final ComboBox<SocietyPaymentCycle> cbox;

    public SocietyPaymentCycleConvertor(ComboBox<SocietyPaymentCycle> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(SocietyPaymentCycle obj) {
        if (obj == null)
            return null;
        return obj.toDateShiftString();
    }

    @Override
    public SocietyPaymentCycle fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toDateShiftString())).findAny().orElse(null);
    }
}
