package com.eipl.amcs.serialize;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MilkQualityTypeSerialize extends JsonSerializer<MilkQualityType> {
    @Override
    public void serialize(MilkQualityType milkQualityType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(milkQualityType.getCode());
    }
}
