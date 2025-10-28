package com.eipl.amcs.serialize;

import com.eipl.amcs.master.geo.model.SubDistrict;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SubDistrictSerialize extends JsonSerializer<SubDistrict> {
    @Override
    public void serialize(SubDistrict subDistrict, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(subDistrict.getCode());
    }
}
