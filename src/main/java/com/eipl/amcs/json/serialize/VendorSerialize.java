package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.operation.model.Vendor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class VendorSerialize extends JsonSerializer<Vendor> {
    @Override
    public void serialize(Vendor vendor, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(vendor.getCode());
    }
}
