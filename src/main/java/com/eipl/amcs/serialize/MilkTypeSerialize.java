package com.eipl.amcs.serialize;

import com.eipl.amcs.master.global.model.MilkType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MilkTypeSerialize extends JsonSerializer<MilkType> {
    @Override
    public void serialize(MilkType milkType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(milkType.getCode());
    }
}
