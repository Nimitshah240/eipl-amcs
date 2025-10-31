package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.global.model.RateType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RateTypeSerialize extends JsonSerializer<RateType> {
    @Override
    public void serialize(RateType rateType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(rateType.getCode());
    }
}
