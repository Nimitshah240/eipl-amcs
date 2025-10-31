package com.eipl.amcs.json.serialize;

import com.eipl.amcs.master.account.model.BasicTax;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BasicTaxSerialize extends JsonSerializer<BasicTax> {
    @Override
    public void serialize(BasicTax basicTax, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(basicTax.getCode());
    }
}
