package com.eipl.amcs.serialize;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SocietyMilkPurchaseRateSerialize extends JsonSerializer<SocietyMilkPurchaseRate> {
    @Override
    public void serialize(SocietyMilkPurchaseRate societyMilkPurchaseRate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(societyMilkPurchaseRate.getCode());
    }
}
