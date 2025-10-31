package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SocietyPaymentCycleSerialize extends JsonSerializer<SocietyPaymentCycle> {
    @Override
    public void serialize(SocietyPaymentCycle societyPaymentCycle, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(societyPaymentCycle.getCode());
    }
}
