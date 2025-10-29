package com.eipl.amcs.serialize;

import com.eipl.amcs.master.account.model.Designation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DesignationSerialize extends JsonSerializer<Designation> {
    @Override
    public void serialize(Designation designation, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(designation.getCode());
    }
}
