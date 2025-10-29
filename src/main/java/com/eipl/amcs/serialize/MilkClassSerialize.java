package com.eipl.amcs.serialize;

import com.eipl.amcs.master.global.model.MilkClass;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MilkClassSerialize extends JsonSerializer<MilkClass> {
    @Override
    public void serialize(MilkClass milkClass, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(milkClass.getCode());
    }
}
