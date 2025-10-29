package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.global.model.Shift;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ShiftSerialize extends JsonSerializer<Shift> {
    @Override
    public void serialize(Shift shift, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(shift.getCode());
    }
}
