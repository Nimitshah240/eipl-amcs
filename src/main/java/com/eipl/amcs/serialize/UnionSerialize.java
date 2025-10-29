package com.eipl.amcs.serialize;

import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UnionSerialize extends JsonSerializer<Union> {
    @Override
    public void serialize(Union union, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(union.getCode());
    }
}
