package com.eipl.amcs.serialize;

import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SocietySerialize extends JsonSerializer<Society> {
    @Override
    public void serialize(Society society, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(society.getCode());
    }
}
