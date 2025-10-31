package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MemberMilkPurchaseRateSerialize extends JsonSerializer<MemberMilkPurchaseRate> {
    @Override
    public void serialize(MemberMilkPurchaseRate memberMilkPurchaseRate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(memberMilkPurchaseRate.getCode());
    }
}
