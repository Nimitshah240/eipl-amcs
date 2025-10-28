package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.Mom;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MomSerialize extends JsonSerializer<Mom> {
    @Override
    public void serialize(Mom mom, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(mom.getCode());
    }
}
