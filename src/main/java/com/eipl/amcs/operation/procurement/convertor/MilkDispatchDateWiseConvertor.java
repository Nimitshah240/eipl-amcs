package com.eipl.amcs.operation.procurement.convertor;

import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MilkDispatchDateWiseConvertor extends StringConverter<MilkDispatch> {

    private final ComboBox<MilkDispatch> cbox;

    public MilkDispatchDateWiseConvertor(ComboBox<MilkDispatch> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(MilkDispatch Unit) {
        if (Unit == null || Unit.getFromDate() == null || Unit.getToDate() == null || Unit.getFromShift() == null || Unit.getToShift() == null)
            return null;
        return Unit.getFromDate().toLocalDate() + "-" + Unit.getFromShift().getName().charAt(0) + " " +
                Unit.getToDate().toLocalDate() + "-" + Unit.getToShift().getName().charAt(0);
    }

    @Override
    public MilkDispatch fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
