package com.eipl.amcs.serialize;

import com.eipl.amcs.master.geo.model.Village;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class VillageSerialize extends JsonSerializer<Village> {
    @Override
    public void serialize(Village village, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(village.getCode());
    }
}
