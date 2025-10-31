package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.org.model.Mcc;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MccSerialize extends JsonSerializer<Mcc> {
    @Override
    public void serialize(Mcc mcc, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(mcc.getCode());
    }
}
