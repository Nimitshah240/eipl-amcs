package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.global.model.Unit;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UnitSerialize extends JsonSerializer<Unit> {
    @Override
    public void serialize(Unit unit, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(unit.getCode());
    }
}
