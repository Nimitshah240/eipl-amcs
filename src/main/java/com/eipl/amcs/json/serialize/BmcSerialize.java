package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.org.model.Bmc;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BmcSerialize extends JsonSerializer<Bmc> {
    @Override
    public void serialize(Bmc bmc, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(bmc.getCode());
    }
}
