package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.geo.model.District;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DistrictSerialize extends JsonSerializer<District> {
    @Override
    public void serialize(District district, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(district.getCode());
    }
}
