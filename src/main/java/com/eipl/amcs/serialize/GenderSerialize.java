package com.eipl.amcs.serialize;

import com.eipl.amcs.master.global.model.Gender;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenderSerialize extends JsonSerializer<Gender> {
    @Override
    public void serialize(Gender gender, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(gender.getCode());
    }
}
