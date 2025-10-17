package com.eipl.amcs.operation.procurement.convertor;

import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MilkDispatchConvertor extends StringConverter<MilkDispatch> {

    private final ComboBox<MilkDispatch> cbox;

    public MilkDispatchConvertor(ComboBox<MilkDispatch> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(MilkDispatch Unit) {
        if (Unit == null)
            return null;
        return Unit.getChallanNo();
    }

    @Override
    public MilkDispatch fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
