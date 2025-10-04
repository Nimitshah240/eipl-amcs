package com.eipl.amcs.operation.procurement.convertor;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MemberMilkPurchaseRateConvertor extends StringConverter<MemberMilkPurchaseRate> {

    private final ComboBox<MemberMilkPurchaseRate> cbox;

    public MemberMilkPurchaseRateConvertor(ComboBox<MemberMilkPurchaseRate> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(MemberMilkPurchaseRate memberMilkPurchaseRate) {
        if (memberMilkPurchaseRate == null)
            return null;
        return memberMilkPurchaseRate.getWefDate().toString().replace("T"," ");
    }

    @Override
    public MemberMilkPurchaseRate fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.getWefDate().toString().replace("T"," "))).findAny().orElse(null);
    }
}
